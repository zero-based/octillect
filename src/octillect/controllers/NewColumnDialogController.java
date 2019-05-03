package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;
import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.ProjectRepository;
import octillect.database.accessors.TaskRepository;
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

            Column newColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime(newColumnNameTextField.getText() + applicationController.user.getId()))
                    .withName(newColumnNameTextField.getText())
                    .build();

            Task untitledTask = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Untitled Task" + applicationController.user.getId()))
                    .withName("Untitled Task")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(applicationController.user)
                    .build();

            newColumn.setTasks(FXCollections.observableArrayList(untitledTask));
            projectController.currentProject.getColumns().add(newColumn);

            ProjectRepository.addColumn(projectController.currentProject.getId(), newColumn.getId());
            ColumnRepository.add(newColumn);
            TaskRepository.add(untitledTask);

            newColumnDialog.close();
        }
    }

    @FXML
    public void handleNewColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newColumnNameTextField.resetValidation();
        newColumnNameTextField.setText("");
    }
}
