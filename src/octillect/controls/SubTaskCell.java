package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import octillect.models.Task;

import org.kordamp.ikonli.javafx.FontIcon;

public class SubTaskCell extends ListCell<Task> {

    // FXML Fields
    @FXML private BorderPane subTaskBorderPane;
    @FXML private CheckBox subTaskCheckBox;
    @FXML private FontIcon deleteSubTaskIcon;

    public void updateItem(Task subTaskItem, boolean empty) {

        super.updateItem(subTaskItem, empty);

        if (empty || subTaskItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/SubTaskCellView.fxml"));
            fxmlLoader.setController(this);
            subTaskBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        subTaskCheckBox.setText(subTaskItem.getName());
        subTaskCheckBox.setSelected(subTaskItem.getIsCompleted());

        deleteSubTaskIcon.setOnMouseClicked(event -> {

        });

        setGraphic(subTaskBorderPane);
    }

}


