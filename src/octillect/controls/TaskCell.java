package octillect.controls;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import octillect.models.Task;

public class TaskCell extends ListCell<Task> {

    @FXML private VBox taskCellVBox;
    @FXML private Label taskNameLabel;

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
        taskNameLabel.setText(taskItem.getName());
        setGraphic(taskCellVBox);

    }
}
