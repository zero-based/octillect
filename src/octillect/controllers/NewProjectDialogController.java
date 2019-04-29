package octillect.controllers;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;

public class NewProjectDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newProjectDialog;
    @FXML public JFXTextField newProjectNameTextField;
    @FXML public JFXTextArea newProjectDescriptionTextArea;
    @FXML public OButton addProjectButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @FXML
    public void initialize() {

        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        newProjectNameTextField.getValidators().add(requiredFieldValidator);
        newProjectNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newProjectNameTextField.validate();
            }
        });
    }

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
    }

    @FXML
    public void handleAddProjectButtonAction(ActionEvent actionEvent) {
        newProjectNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {
            /* TODO: Update TasksColumn's List Here. */
            newProjectDialog.close();
        }
    }

    @FXML
    public void handleNewProjectDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newProjectNameTextField.resetValidation();
        newProjectNameTextField.setText("");
        newProjectDescriptionTextArea.setText("");
    }
}
