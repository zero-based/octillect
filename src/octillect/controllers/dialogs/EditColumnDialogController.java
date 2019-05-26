package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.OButton;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.ColumnRepository;
import octillect.models.Column;

public class EditColumnDialogController implements Injectable<ApplicationController> {

    // Local Fields
    public Column currentColumn;

    // FXML Fields
    @FXML public JFXDialog editColumnDialog;
    @FXML public JFXTextField editColumnTextField;
    @FXML public OButton editColumnButton;

    // Validators
    private RequiredValidator requiredValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
    }

    @PostLoad
    public void initValidators() {
        requiredValidator = new RequiredValidator();
        ValidationManager.addValidator(true, requiredValidator, editColumnTextField);
    }

    @FXML
    public void handleEditColumnButtonAction(ActionEvent actionEvent) {

        editColumnTextField.validate();

        if (!requiredValidator.getHasErrors()) {
            ColumnRepository.getInstance().updateName(currentColumn.getId(), editColumnTextField.getText());
            currentColumn.setName(editColumnTextField.getText());
            boardController.boardListView.refresh();
            editColumnDialog.close();
        }

    }

    @FXML
    public void handleEditColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        editColumnTextField.resetValidation();
        editColumnTextField.setText(null);
    }
}
