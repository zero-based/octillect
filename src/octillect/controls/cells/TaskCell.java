package octillect.controls.cells;

import com.jfoenix.controls.JFXNodesList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import octillect.controllers.util.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.TaskRepository;
import octillect.models.Column;
import octillect.models.Tag;
import octillect.models.Task;
import octillect.styles.Palette;

import org.kordamp.ikonli.javafx.FontIcon;

public class TaskCell extends ListCell<Task> implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML private VBox taskCellVBox;
    @FXML private FontIcon taskMoreIcon;
    @FXML private Label taskNameLabel;
    @FXML private Label taskDueDateLabel;
    @FXML private BorderPane taskInfoBorderPane;
    @FXML private FlowPane taskIconsFlowPane;
    @FXML private FlowPane tagsFlowPane;
    @FXML private JFXNodesList taskAssigneesNodesList;
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

                // Disable glowing and selection effects
                getListView().getSelectionModel().clearSelection();

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

                // Get all Tasks ListViews in the current Board
                List<ListView<Task>> allTasksListViews = new ArrayList<>();

                this.getListView()                 // Gets tasksListView
                        .getParent()               // Gets columnCellVBox
                        .getParent()               // Gets ListCell<Column>
                        .getParent()               // Gets boardListView
                        .getChildrenUnmodifiable() // Gets All ListCell<Column>'s
                        .forEach(columnCell -> {
                            if (((ColumnCell) columnCell).getTasksListView() != null)
                                allTasksListViews.add(((ColumnCell) columnCell).getTasksListView());
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
                    FilteredList<Task> items = (FilteredList<Task>) listView.getItems();
                    items.getSource().remove(sourceTask);
                }

                // Add task to current position
                FilteredList<Task> items = (FilteredList<Task>) getListView().getItems();
                ObservableList<Task> source = (ObservableList<Task>) items.getSource();
                source.add(getIndex(), sourceTask);

            }
        });

        setOnDragDropped(event -> {
            if (event.getGestureSource() instanceof TaskCell
                    && event.getGestureTarget() instanceof TaskCell) {

                TaskCell sourceTask = (TaskCell) event.getGestureSource();
                TaskCell targetTask = (TaskCell) event.getGestureTarget();

                Column sourceColumn = ((ColumnCell) sourceTask.getListView()    // Gets tasksListView
                        .getParent()                                            // Gets columnCellVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                Column targetColumn = ((ColumnCell) targetTask.getListView()    // Gets tasksListView
                        .getParent()                                            // Gets columnCellVBox
                        .getParent())                                           // Gets ListCell<Column>
                        .getItem();

                ArrayList<String> sourceTasksIds = new ArrayList<>();
                ArrayList<String> targetTasksIds = new ArrayList<>();

                sourceColumn.getChildren().forEach(task -> sourceTasksIds.add(task.getId()));
                targetColumn.getChildren().forEach(task -> targetTasksIds.add(task.getId()));

                ColumnRepository.getInstance().updateTasksIds(sourceColumn.getId(), sourceTasksIds);
                ColumnRepository.getInstance().updateTasksIds(targetColumn.getId(), targetTasksIds);

            }
        });

        setOnMouseClicked(event -> {
            if (getItem() != null) {
                Column parentColumn = ((ColumnCell) getListView().getParent().getParent()).getItem();
                taskSettingsController.loadTask(getItem(), parentColumn);
            } else {
                getListView().getSelectionModel().clearSelection();
            }
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/TaskCellView.fxml"));
            fxmlLoader.setController(this);
            taskCellVBox = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        taskAssigneesNodesList.setOnMouseEntered(event -> taskAssigneesNodesList.animateList());
        taskAssigneesNodesList.setOnMouseExited(event -> taskAssigneesNodesList.animateList());

        taskMoreButton.setOnMouseClicked(event -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(editButton, deleteButton);
            contextMenu.show(taskMoreButton, Side.RIGHT, 0, 0);
        });

        editButton.setOnAction(event -> {
            Column parentColumn = ((ColumnCell) getListView()    // Gets tasksListView
                    .getParent()                                 // Gets columnCellVBox
                    .getParent())                                // Gets ListCell<Column>
                    .getItem();
            taskSettingsController.loadTask(getItem(), parentColumn);
        });

        deleteButton.setOnAction(event -> {
            Column parentColumn = ((ColumnCell) getListView()    // Gets tasksListView
                    .getParent()                                 // Gets columnCellVBox
                    .getParent())                                // Gets ListCell<Column>
                    .getItem();

            ColumnRepository.getInstance().deleteTaskId(parentColumn.getId(), getItem().getId());
            TaskRepository.getInstance().delete(getItem());
            parentColumn.getChildren().remove(getItem());
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
                && taskItem.getAssignees().isEmpty() && taskItem.<Task>getChildren().isEmpty()
                && (taskItem.getDescription() == null || taskItem.getDescription().equals(""))) {
            taskCellVBox.getChildren().remove(taskInfoBorderPane);
        }

        if (taskItem.getDueDate() == null) {
            taskIconsFlowPane.getChildren().remove(3);
        } else {
            updateDueDateLabel(taskItem, taskDueDateLabel);
        }

        if (taskItem.getChildren().isEmpty()) {
            taskIconsFlowPane.getChildren().remove(2);
        }

        if (!taskItem.getIsCompleted()) {
            taskIconsFlowPane.getChildren().remove(1);
        }

        if (taskItem.getDescription() == null || taskItem.getDescription().equals("")) {
            taskIconsFlowPane.getChildren().remove(0);
        }

        if (taskItem.getAssignees().isEmpty()) {
            taskAssigneesNodesList.setVisible(false);
        } else {
            updateAssigneesNodesList(taskItem, taskAssigneesNodesList);
        }

        if (taskItem.getTags().isEmpty()) {
            taskCellVBox.getChildren().remove(tagsFlowPane);
        } else {
            updateTagsFlowPane(taskItem, tagsFlowPane);
        }
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
     * Populates the TaskCell's tagsFlowPane with Task Tags,
     * and sets the styling of Tag.
     *
     * @param taskItem     Task item which we need to populate it's tags.
     * @param tagsFlowPane FlowPane Control which will be controlled.
     */
    private void updateTagsFlowPane(Task taskItem, FlowPane tagsFlowPane)
    {
        for (Tag tag : taskItem.getTags()) {
            Label label = new Label(tag.getName());
            label.setStyle("-fx-background-color: " + tag.getColorHex() + "; -fx-background-radius: 4px; " +
                           "-fx-padding: 4 8;" + TagCell.determineTextFillStyle(tag.getColor()));

            tagsFlowPane.getChildren().add(label);
        }
    }

    /**
     * Populates the TaskCell's dueDateLabel with Task DueDate formatted with d-MMM Capitalized Date Format,
     * and sets the Tag's TextFill to Palette.DANGER if the Due Date is Past.
     *
     * @param taskItem         Task item which we need to populate it's assignees dueDateLabel.
     * @param taskDueDateLabel Label Control which will be controlled.
     */
    private void updateDueDateLabel(Task taskItem, Label taskDueDateLabel) {
        SimpleDateFormat sdf = new SimpleDateFormat("d-MMM");
        String date = sdf.format(taskItem.getDueDate().getTime());
        taskDueDateLabel.setText(date.toUpperCase());

        /* Set the Tag's TextFill to Palette.DANGER if the Due Date is Past. */
        if(taskItem.getDueDate().before(Calendar.getInstance().getTime())) {
            taskDueDateLabel.textFillProperty().unbind();
            taskDueDateLabel.setTextFill(Palette.DANGER);
        }
    }
}
