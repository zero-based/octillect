package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.TaskRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.TaskBuilder;

public class NewColumnDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newColumnDialog;
    @FXML public JFXTextField newColumnNameTextField;
    @FXML public OButton addColumnButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @Override
    public void init() {
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        newColumnNameTextField.getValidators().add(requiredFieldValidator);
        newColumnNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newColumnNameTextField.validate();
            }
        });
    }

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController = applicationController.boardController;
    }

    @FXML
    public void handleAddColumnButtonAction(ActionEvent actionEvent) {
        newColumnNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {

            Column newColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime(newColumnNameTextField.getText() + applicationController.user.getId()))
                    .withName(newColumnNameTextField.getText())
                    .build();

            Task untitledTask = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Untitled Task" + applicationController.user.getId()))
                    .withName("Untitled Task")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(applicationController.user)
                    .build();

            newColumn.setTasks(FXCollections.observableArrayList(untitledTask));
            boardController.currentBoard.getColumns().add(newColumn);

            BoardRepository.getInstance().addColumnId(boardController.currentBoard.getId(), newColumn.getId());
            ColumnRepository.getInstance().add(newColumn);
            TaskRepository.getInstance().add(untitledTask);

            newColumnDialog.close();
        }
    }

    @FXML
    public void handleNewColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newColumnNameTextField.resetValidation();
        newColumnNameTextField.setText("");
    }
}
