package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.BoardRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.builders.ColumnBuilder;

public class NewColumnDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newColumnDialog;
    @FXML public JFXTextField newColumnNameTextField;
    @FXML public OButton addColumnButton;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
    }

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

    @FXML
    public void handleAddColumnButtonAction(ActionEvent actionEvent) {

        newColumnNameTextField.validate();

        if (!requiredFieldValidator.getHasErrors()) {

            Column newColumn = new ColumnBuilder().with($ -> {
                $.id = FirestoreAPI.getInstance().encryptWithDateTime(newColumnNameTextField.getText()
                        + applicationController.user.getId());
                $.name = newColumnNameTextField.getText();
            }).build();

            BoardRepository.getInstance().addColumnId(boardController.currentBoard.getId(), newColumn.getId());
            ColumnRepository.getInstance().add(newColumn);
            boardController.currentBoard.<Column>getChildren().add(newColumn);

            newColumnDialog.close();
        }
    }

    @FXML
    public void handleNewColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newColumnNameTextField.resetValidation();
        newColumnNameTextField.setText(null);
    }
}
