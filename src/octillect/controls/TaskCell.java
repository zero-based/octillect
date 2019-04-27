package octillect.controls;

import com.jfoenix.controls.JFXNodesList;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import octillect.models.Task;

public class TaskCell extends ListCell<Task> {

    @FXML private VBox taskCellVBox;
    @FXML private Label taskNameLabel;
    @FXML private Label taskDueDateLabel;
    @FXML private FlowPane taskIconsFlowPane;
    @FXML private JFXNodesList taskAssigneesNodesList;
    @FXML private BorderPane taskInfoBorderPane;

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
            if (event.getGestureSource() instanceof TaskCell && getItem() != null) {

                Dragboard dragboard = event.getDragboard();
                ListView currentListView = getListView();
                Task task = null;

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

                // Get the Source Task using ID
                for (ListView<Task> listView : allTasksListViews) {
                    for (Task currentTask : listView.getItems()) {
                        if (currentTask.getId() == dragboard.getString()) {
                            task = currentTask;
                        }
                    }
                }

                // Remove the Source Task from all Columns
                for (ListView<Task> listView : allTasksListViews) {
                    listView.getItems().remove(task);
                }

                // Add task to current position
                currentListView.getItems().add(getIndex(), task);
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/TaskCellView.fxml"));
            fxmlLoader.setController(this);
            taskCellVBox = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* TODO: Populate the TaskCell view here */
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

        if (taskItem.getDueDate() == null && !taskItem.isCompleted()
                && taskItem.getDescription() == null && taskItem.getAssignees() == null)
            taskCellVBox.getChildren().remove(taskInfoBorderPane);

        if (taskItem.getDueDate() == null)
            taskIconsFlowPane.getChildren().remove(2);

        if (!taskItem.isCompleted())
            taskIconsFlowPane.getChildren().remove(1);

        if (taskItem.getDescription() == null)
            taskIconsFlowPane.getChildren().remove(0);

        if (taskItem.getAssignees() == null)
            taskAssigneesNodesList.setVisible(false);
    }

}
