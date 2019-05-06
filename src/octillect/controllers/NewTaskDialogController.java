package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;
import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.TaskRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;
import octillect.models.builders.TaskBuilder;

public class NewTaskDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newTaskDialog;
    @FXML public JFXTextField newTaskNameTextField;
    @FXML public JFXTextArea newTaskDescriptionTextArea;
    @FXML public OButton addTaskButton;

    public Column currentColumn;

    // Injected Controllers
    private ApplicationController applicationController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @Override
    public void init() {
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

            Task newTask = new TaskBuilder().with($ -> {
                $.id = FirestoreAPI.encryptWithDateTime(newTaskNameTextField.getText() + applicationController.user.getId());
                $.name = newTaskNameTextField.getText();

                if (!newTaskDescriptionTextArea.getText().equals("")) {
                    $.description = newTaskDescriptionTextArea.getText();
                }

                $.creationDate = Calendar.getInstance().getTime();
                $.creator = applicationController.user;
            }).build();

            currentColumn.getTasks().add(newTask);

            ColumnRepository.addTask(currentColumn.getId(), newTask.getId());
            TaskRepository.add(newTask);

            newTaskDialog.close();
        }
    }

    public void handleNewTaskDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newTaskNameTextField.resetValidation();
        newTaskNameTextField.setText("");
        newTaskDescriptionTextArea.setText("");
    }
}
