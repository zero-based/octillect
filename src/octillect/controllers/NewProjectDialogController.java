package octillect.controllers;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;

import octillect.controls.OButton;
import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.ProjectRepository;
import octillect.database.accessors.TaskRepository;
import octillect.database.accessors.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.ProjectBuilder;
import octillect.models.builders.TaskBuilder;

public class NewProjectDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newProjectDialog;
    @FXML public JFXTextField newProjectNameTextField;
    @FXML public JFXTextArea newProjectDescriptionTextArea;
    @FXML public OButton addProjectButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;
    private LeftDrawerController leftDrawerController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @Override
    public void init() {
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
        leftDrawerController       = applicationController.leftDrawerController;
    }

    @FXML
    public void handleAddProjectButtonAction(ActionEvent actionEvent) {
        newProjectNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {
            
            // Add project.
            Project newProject = new ProjectBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime(newProjectNameTextField.getText()))
                    .withName(newProjectNameTextField.getText())
                    .withDescription(newProjectDescriptionTextArea.getText())
                    .withContributors(FXCollections.observableArrayList(new Pair<>(applicationController.user, Project.Role.owner)))
                    .build();

            // Add column.
            Column untitledColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Untitled Column" + applicationController.user.getId()))
                    .withName("Untitled Column")
                    .build();

            newProject.setColumns(FXCollections.observableArrayList(untitledColumn));

            // Add task.
            Task untitledTask = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Untitled Task" + applicationController.user.getId()))
                    .withName("Untitled Task")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(applicationController.user)
                    .build();

            untitledColumn.setTasks(FXCollections.observableArrayList(untitledTask));

            // Accessing the database.
            UserRepository.getInstance().addProject(newProject.getId(), applicationController.user.getId());
            ProjectRepository.getInstance().add(newProject);
            ColumnRepository.getInstance().add(untitledColumn);
            TaskRepository.getInstance().add(untitledTask);

            projectController.loadProject(newProject);
            leftDrawerController.userProjectsListView.getItems().add(newProject);
            leftDrawerController.userProjectsListView.getSelectionModel().selectLast();
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
