package octillect.controls.cells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.controllers.util.Injectable;
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
    private BoardController boardController;
    private TaskSettingsController taskSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
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
            taskSettingsController.currentTask.<Task>getChildren().remove(getItem());
            boardController.boardListView.refresh();
        });

        setGraphic(subTaskBorderPane);
    }
}


