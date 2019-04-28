package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;

public class NewTaskDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newTaskDialog;
    @FXML public JFXTextField newTaskNameTextField;
    @FXML public JFXTextArea newTaskDescriptionTextArea;
    @FXML public OButton addTaskButton;

    // Injected Controllers
    private ApplicationController applicationController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @FXML
    public void initialize() {

        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        newTaskNameTextField.getValidators().add(requiredFieldValidator);

        newTaskNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newTaskNameTextField.validate();
            }
        });
    }

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public void handleAddTaskButtonAction(ActionEvent actionEvent) {
        newTaskNameTextField.validate();
        if(!requiredFieldValidator.getHasErrors()) {
            /* TODO: Update TasksColumn's List Here. */
            newTaskDialog.close();
        }
    }

    public void handleNewTaskDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newTaskNameTextField.resetValidation();
        newTaskNameTextField.setText("");
        newTaskDescriptionTextArea.setText("");
    }
}
