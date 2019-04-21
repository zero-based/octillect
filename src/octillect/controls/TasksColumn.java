package octillect.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import octillect.models.Column;
import octillect.models.Task;

public class TasksColumn extends ListCell<Column> {

    @FXML private VBox tasksColumnVBox;
    @FXML private Label nameLabel;
    @FXML private ListView tasksListView;

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

        /* TODO: Populate the TasksColumn view here */
        nameLabel.setText(columnItem.getName());

        // Populate the TasksColumn's tasksListView with columnItem's tasks
        ObservableList<Task> tasksObservableList = FXCollections.observableArrayList();
        tasksObservableList.setAll(columnItem.getTasks());
        tasksListView.setItems(tasksObservableList);
        tasksListView.setCellFactory(param -> new TaskCell());

        setGraphic(tasksColumnVBox);

    }

    public ListView getTasksListView() {
        return tasksListView;
    }
}