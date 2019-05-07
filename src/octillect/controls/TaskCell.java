package octillect.controls;

import com.jfoenix.controls.JFXNodesList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controllers.TaskSettingsController;
import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.TaskRepository;
import octillect.models.Column;
import octillect.models.Task;
import octillect.styles.Palette;

import org.kordamp.ikonli.javafx.FontIcon;

public class TaskCell extends ListCell<Task> implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML private VBox taskCellVBox;
    @FXML private FontIcon taskMoreIcon;
    @FXML private Label taskNameLabel;
    @FXML private Label taskDueDateLabel;
    @FXML private FlowPane taskIconsFlowPane;
    @FXML private JFXNodesList taskAssigneesNodesList;
    @FXML private BorderPane taskInfoBorderPane;
    @FXML private MenuItem editButton;
    @FXML private MenuItem deleteButton;
    @FXML private Button taskMoreButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private RightDrawerController rightDrawerController;
    private TaskSettingsController taskSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        rightDrawerController      = applicationController.rightDrawerController;
        taskSettingsController     = rightDrawerController.taskSettingsController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("TaskCell cannot be initialized");
    }

    public TaskCell() {

        setOnDragDetected(event -> {
            if (getItem() != null) {
                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);

                Image snapshot = snapshot(parameters, null);
                dragboard.setDragView(snapshot, event.getX(), event.getY());

                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(getItem().getId()); // Save the Source Task ID
                dragboard.setContent(clipboardContent);
            }
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() instanceof TaskCell && getItem() != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        setOnDragEntered(event -> {

            String sourceTaskId = event.getDragboard().getString();

            if (event.getGestureSource() instanceof TaskCell && getItem() != null
                    && getItem().getId() != sourceTaskId) {

                // Get all Tasks ListViews in the current Project
                List<ListView<Task>> allTasksListViews = new ArrayList<>();

                this.getListView()                 // Gets tasksListView
                        .getParent()               // Gets tasksColumnVBox
                        .getParent()               // Gets ListCell<Column>
                        .getParent()               // Gets projectListView
                        .getChildrenUnmodifiable() // Gets All ListCell<Column>'s
                        .forEach(tasksColumn -> {
                            if (((TasksColumn) tasksColumn).getTasksListView() != null)
                                allTasksListViews.add(((TasksColumn) tasksColumn).getTasksListView());
                        });

                Task sourceTask = null;

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

                // Add task to current position
                getListView().getItems().add(getIndex(), sourceTask);

            }
        });

        setOnDragDropped(event -> {
            if (event.getGestureSource() instanceof TaskCell
                    && event.getGestureTarget() instanceof TaskCell) {

                TaskCell sourceTask = (TaskCell) event.getGestureSource();
                TaskCell targetTask = (TaskCell) event.getGestureTarget();

                Column sourceColumn = ((TasksColumn) sourceTask.getListView()   // Gets tasksListView
                        .getParent()                                            // Gets tasksColumnVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                Column targetColumn = ((TasksColumn) targetTask.getListView()   // Gets tasksListView
                        .getParent()                                            // Gets tasksColumnVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                ArrayList<String> sourceTasksIds = new ArrayList<>();
                ArrayList<String> targetTasksIds = new ArrayList<>();

                sourceColumn.getTasks().forEach(task -> sourceTasksIds.add(task.getId()));
                targetColumn.getTasks().forEach(task -> targetTasksIds.add(task.getId()));

                ColumnRepository.getInstance().updateTasksIds(sourceColumn.getId(), sourceTasksIds);
                ColumnRepository.getInstance().updateTasksIds(targetColumn.getId(), targetTasksIds);

            }
        });

        setOnMouseClicked(event -> {
            Column parentColumn = ((TasksColumn) (getListView().getParent().getParent())).getItem();
            taskSettingsController.loadTask(getItem(), parentColumn);
        });

    }

    @Override
    public void updateItem(Task taskItem, boolean empty) {

        super.updateItem(taskItem, empty);

        if (empty || taskItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/TaskCellView.fxml"));
            fxmlLoader.setController(this);
            taskCellVBox = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        taskMoreButton.setOnMouseClicked(event -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(editButton, deleteButton);
            contextMenu.show(taskMoreButton, Side.RIGHT, 0, 0);
        });

        editButton.setOnAction(event -> {
            Column parentColumn = ((TasksColumn) (getListView().getParent().getParent())).getItem();
            taskSettingsController.loadTask(getItem(), parentColumn);
        });

        deleteButton.setOnAction(event -> {
            // Get tasksListView, Get tasksColumnVBox, Get ListCell<Column>
            Column parentColumn = ((TasksColumn) (getListView().getParent().getParent())).getItem();

            TaskRepository.getInstance().delete(getItem().getId());
            ColumnRepository.getInstance().deleteTaskId(parentColumn.getId(), getItem().getId());

            parentColumn.getTasks().remove(getItem());
        });

        updateTaskInfo(taskItem);
        setGraphic(taskCellVBox);

    }

    /**
     * Updates all TaskCell controls with a given TaskItem Model.
     *
     * @param taskItem TaskItem Which the cell will be populated with its values.
     */
    private void updateTaskInfo(Task taskItem) {

        taskNameLabel.setText(taskItem.getName());

        if (taskItem.getDueDate() == null && !taskItem.getIsCompleted()
                && taskItem.getDescription() == null && taskItem.getAssignees() == null)
            taskCellVBox.getChildren().remove(taskInfoBorderPane);

        if (taskItem.getDueDate() == null)
            taskIconsFlowPane.getChildren().remove(2);
        else
            updateDueDateLabel(taskItem, taskDueDateLabel);

        if (!taskItem.getIsCompleted())
            taskIconsFlowPane.getChildren().remove(1);

        if (taskItem.getDescription() == null)
            taskIconsFlowPane.getChildren().remove(0);

        if (taskItem.getAssignees() == null)
            taskAssigneesNodesList.setVisible(false);
        else
            updateAssigneesNodesList(taskItem, taskAssigneesNodesList);

    }

    /**
     * Populates the TaskCell's AssigneesNodesList with Task Assignees' images according to specific conditions.
     *
     * @param taskItem  Task item which we need to populate it's assignees NodesList.
     * @param nodesList NodesList Control which will be controlled.
     */
    private void updateAssigneesNodesList(Task taskItem, JFXNodesList nodesList) {
        for (int i = 0; i < 4 && taskItem.getAssignees().size() != i; i++) {

            Circle circle = new Circle();
            circle.setRadius(16);
            circle.setFill(new ImagePattern(taskItem.getAssignees().get(i).getImage()));
            circle.setStrokeWidth(1.8);
            circle.setStroke(Palette.DARK_300);

            nodesList.getChildren().add(circle);
        }

        if (taskItem.getAssignees().size() > 1) {
            StackPane stackPane = new StackPane();

            Circle circle = new Circle();
            circle.setRadius(16);
            circle.setFill(Palette.PRIMARY_DARK);
            circle.setStrokeWidth(1.8);
            circle.setStroke(Palette.DARK_300);
            stackPane.getChildren().add(circle);

            Label label = new Label();
            if (taskItem.getAssignees().size() <= 9)
                label.setText(String.valueOf(taskItem.getAssignees().size()));
            else
                label.setText("9+");
            
            label.setTextFill(Palette.DARK_300);

            stackPane.getChildren().add(label);

            nodesList.getChildren().add(0, stackPane);
        }
    }

    /**
     * Populates the TaskCell's dueDateLabel with Task DueDate formatted with d-MMM Capitalized Date Format,
     * and sets the Label's TextFill to Palette.DANGER if the Due Date is Past.
     *
     * @param taskItem         Task item which we need to populate it's assignees dueDateLabel.
     * @param taskDueDateLabel Label Control which will be controlled.
     */
    private void updateDueDateLabel(Task taskItem, Label taskDueDateLabel) {
        SimpleDateFormat sdf = new SimpleDateFormat("d-MMM");
        String date = sdf.format(taskItem.getDueDate().getTime());
        taskDueDateLabel.setText(date.toUpperCase());

        /* Set the Label's TextFill to Palette.DANGER if the Due Date is Past. */
        if(taskItem.getDueDate().before(Calendar.getInstance().getTime())) {
            taskDueDateLabel.textFillProperty().unbind();
            taskDueDateLabel.setTextFill(Palette.DANGER);
        }
    }

}
