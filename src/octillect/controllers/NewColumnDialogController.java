package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;

public class NewColumnDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newColumnDialog;
    @FXML public JFXTextField newColumnNameTextField;
    @FXML public OButton addColumnButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @FXML
    public void initialize() {

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
        projectController          = applicationController.projectController;
    }

    @FXML
    public void handleAddColumnButtonAction(ActionEvent actionEvent) {
        newColumnNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {
            /* TODO: Update Column's List Here. */
            newColumnDialog.close();
        }
    }

    @FXML
    public void handleNewColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newColumnNameTextField.resetValidation();
        newColumnNameTextField.setText("");
    }
}
