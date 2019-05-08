package octillect.controllers.dialogs;

import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;
import octillect.database.repositories.ColumnRepository;
import octillect.models.Column;
import octillect.models.TaskBase;

public class EditColumnDialogController implements Injectable<ApplicationController> {

    @FXML public JFXDialog editColumnDialog;
    @FXML public JFXTextField editColumnTextField;
    @FXML public OButton editColumnButton;

    public Column currentColumn;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    // Empty field validation
    private RequiredFieldValidator requiredFieldValidator;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
    }

    @Override
    public void init() {
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        editColumnTextField.getValidators().add(requiredFieldValidator);
        editColumnTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editColumnTextField.validate();
            }
        });
    }

    @FXML
    public void handleEditColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        editColumnTextField.resetValidation();
        editColumnTextField.setText("");
    }

    @FXML
    public void handleEditColumnButtonAction(ActionEvent actionEvent) {
        requiredFieldValidator.validate();
        if (!requiredFieldValidator.getHasErrors()) {

            ColumnRepository.getInstance().updateName(currentColumn.getId(), editColumnTextField.getText());

            currentColumn.setName(editColumnTextField.getText());
            int index = boardController.boardListView.getItems().indexOf(currentColumn);
            boardController.boardListView.getItems().set(index, currentColumn);

            for (TaskBase column : boardController.currentBoard.getChildren()) {
                if (column.getId().equals(currentColumn.getId())) {
                    column.setName(editColumnTextField.getText());
                    break;
                }
            }
            editColumnDialog.close();
        }
    }
}
