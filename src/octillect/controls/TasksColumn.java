package octillect.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.NewTaskDialogController;
import octillect.controllers.ProjectController;
import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.ProjectRepository;
import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;

import org.kordamp.ikonli.javafx.FontIcon;

public class TasksColumn extends ListCell<Column> implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML private VBox tasksColumnVBox;
    @FXML private FontIcon addNewTaskIcon;
    @FXML private FontIcon columnMoreIcon;
    @FXML private Label columnNameLabel;
    @FXML private ListView tasksListView;
    @FXML private MenuItem editButton;
    @FXML private MenuItem deleteButton;
    @FXML private Button columnMoreButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private NewTaskDialogController newTaskDialogController;
    private ProjectController projectController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        newTaskDialogController    = applicationController.newTaskDialogController;
        projectController          = applicationController.projectController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("TasksColumn cannot be initialized");
    }

    public TasksColumn() {

        setOnDragDetected(event -> {
            if (getItem() != null) {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);

                Image snapshot = snapshot(parameters, null);
                dragboard.setDragView(snapshot, event.getX(), event.getY());

                ClipboardContent content = new ClipboardContent();
                content.putString(getItem().getId()); // Save the Source Column ID
                dragboard.setContent(content);
            }
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() instanceof TasksColumn
                    && event.getGestureSource() != this && getItem() != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() instanceof TasksColumn
                    && event.getGestureSource() != this) {
                setOpacity(0.32); // Set Opacity of Column on mouse enter to 0.32
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() instanceof TasksColumn
                    && event.getGestureSource() != this) {
                setOpacity(1); // Reset Opacity of Column on mouse exit to 1
            }
        });

        setOnDragDropped(event -> {
            if (event.getGestureSource() instanceof TasksColumn
                    && event.getDragboard().hasString()) {
                int sourceIndex = ((TasksColumn) event.getGestureSource()).getIndex();
                int targetIndex = ((TasksColumn) event.getGestureTarget()).getIndex();
                Collections.swap(getListView().getItems(), sourceIndex, targetIndex);

                ArrayList<String> columnsIds = new ArrayList<>();
                Project project = projectController.currentProject;
                project.getColumns().forEach(column -> {
                    columnsIds.add(column.getId());
                });
                ProjectRepository.updateColumnsIds(project.getId(), columnsIds);
            }
        });

    }

    @Override
    public void updateItem(Column columnItem, boolean empty) {

        super.updateItem(columnItem, empty);

        if (empty || columnItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/TasksColumnView.fxml"));
            fxmlLoader.setController(this);
            tasksColumnVBox = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addNewTaskIcon.setOnMouseClicked(event -> {
            newTaskDialogController.newTaskDialog.show(this.applicationController.rootStackPane);
            newTaskDialogController.currentColumn = getItem();
        });

        columnMoreButton.setOnMouseClicked(event -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(editButton, deleteButton);
            contextMenu.show(columnMoreButton, Side.RIGHT, 0, 0);
        });

        /* TODO: Add ContextMenu 's methods here */
        editButton.setOnAction(event -> {

        });

        deleteButton.setOnAction(event -> {
            ColumnRepository.delete(getItem());
            ProjectRepository.deleteColumnId(projectController.currentProject.getId(), getItem().getId());

            projectController.currentProject.getColumns().remove(getItem());
        });

        columnNameLabel.setText(columnItem.getName());

        // Populate the TasksColumn's tasksListView with columnItem's tasks
        tasksListView.setItems(columnItem.getTasks());
        tasksListView.setCellFactory(param -> {
            TaskCell taskCell = new TaskCell();
            taskCell.inject(applicationController);
            return taskCell;
        });

        tasksListView.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof TaskCell) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        tasksListView.setOnDragEntered(event -> {
            if (event.getGestureSource() instanceof TaskCell) {

                // Get all Tasks ListViews in the current Project
                List<ListView<Task>> allTasksListViews = new ArrayList<>();

                tasksListView.getParent()               // Gets tasksColumnVBox
                        .getParent()                    // Gets ListCell<Column>
                        .getParent()                    // Gets projectListView
                        .getChildrenUnmodifiable()      // Gets All ListCell<Column>'s
                        .forEach(tasksColumn -> {
                            if (((TasksColumn) tasksColumn).getTasksListView() != null)
                                allTasksListViews.add(((TasksColumn) tasksColumn).getTasksListView());
                        });

                Task sourceTask = null;
                String sourceTaskId = event.getDragboard().getString();

                // Get the Source Task using ID
                for (ListView<Task> listView : allTasksListViews) {
                    for (Task currentTask : listView.getItems()) {
                        if (currentTask.getId().equals(sourceTaskId)) {
                            sourceTask = currentTask;
                        }
                    }
                }

                // Remove the Source Task from all Columns
                for (ListView<Task> listView : allTasksListViews) {
                    listView.getItems().remove(sourceTask);
                }

                // Add task to current ListView
                tasksListView.getItems().add(sourceTask);
            }
        });

        tasksListView.setOnDragDropped(event -> {
            if (event.getGestureSource() instanceof TaskCell
                    && event.getGestureTarget() instanceof ListView) {

                TaskCell sourceTask = (TaskCell) event.getGestureSource();

                Column sourceColumn = ((TasksColumn) sourceTask.getListView()   // Gets tasksListView
                        .getParent()                                            // Gets tasksColumnVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                Column targetColumn = ((TasksColumn) tasksListView.getParent()  // Gets tasksColumnVBox                                // Gets tasksColumnVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                ArrayList<String> sourceTasksIds = new ArrayList<>();
                ArrayList<String> targetTasksIds = new ArrayList<>();

                sourceColumn.getTasks().forEach(task -> sourceTasksIds.add(task.getId()));
                targetColumn.getTasks().forEach(task -> targetTasksIds.add(task.getId()));

                ColumnRepository.updateTasksIds(sourceColumn.getId(), sourceTasksIds);
                ColumnRepository.updateTasksIds(targetColumn.getId(), targetTasksIds);

            }
        });

        setGraphic(tasksColumnVBox);

    }

    public ListView getTasksListView() {
        return tasksListView;
    }
}