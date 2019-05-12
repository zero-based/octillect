package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.settings.TaskSettingsController;
import octillect.database.repositories.TaskRepository;
import octillect.models.Task;

import org.kordamp.ikonli.javafx.FontIcon;

public class SubTaskCell extends ListCell<Task> implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML private BorderPane subTaskBorderPane;
    @FXML private CheckBox subTaskCheckBox;
    @FXML private FontIcon deleteSubTaskIcon;

    // Injected Controllers
    private ApplicationController applicationController;
    private TaskSettingsController taskSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        taskSettingsController     = applicationController.rightDrawerController.taskSettingsController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("SubTaskCell cannot be initialized");
    }

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

        deleteSubTaskIcon.setOnMouseClicked(event -> {
            TaskRepository.getInstance().deleteSubTask(taskSettingsController.currentTask.getId(), getItem());
            taskSettingsController.currentTask.getChildren().remove(getItem());
            getListView().getItems().remove(getItem());
        });

        setGraphic(subTaskBorderPane);
    }
}


