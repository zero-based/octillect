package octillect.controllers;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import octillect.controls.ContributorCell;
import octillect.controls.LabelCell;
import octillect.controls.OButton;
import octillect.database.accessors.LabelRepository;
import octillect.database.accessors.ProjectRepository;
import octillect.database.accessors.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Label;
import octillect.models.Project;
import octillect.models.User;
import octillect.models.builders.LabelBuilder;

public class ProjectSettingsController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public TitledPane editNameTitledPane;
    @FXML public TitledPane editDescriptionTitledPane;
    @FXML public TitledPane contributorsTitledPane;
    @FXML public TitledPane labelsTitledPane;
    @FXML public TitledPane deleteProjectTitledPane;
    @FXML public BorderPane inviteContributorBorderPane;
    @FXML public BorderPane newLabelBorderPane;
    @FXML public JFXTextField editNameTextField;
    @FXML public JFXTextField inviteContributorByEmailTextField;
    @FXML public JFXTextField newLabelTextField;
    @FXML public JFXTextArea editDescriptionTextArea;
    @FXML public JFXListView<Pair<User, Project.Role>> contributorsListView;
    @FXML public JFXListView<Label> labelsListView;
    @FXML public JFXComboBox<Project.Role> rolesComboBox;
    @FXML public JFXColorPicker labelColorPicker;
    @FXML public OButton inviteContributorButton;
    @FXML public OButton addLabelButton;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;
    private LeftDrawerController leftDrawerController;
    private TitleBarController titleBarController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
        leftDrawerController       = applicationController.leftDrawerController;
        titleBarController         = applicationController.titleBarController;
    }

    @Override
    public void init() {

        // Validators

        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        emailValidator = new RegexValidator();
        emailValidator.setRegexPattern("^((?!.*" + inviteContributorByEmailTextField.getText() + ".*).)*$");

        newLabelTextField.getValidators().add(requiredFieldValidator);
        rolesComboBox.getValidators().add(requiredFieldValidator);
        inviteContributorByEmailTextField.getValidators().add(requiredFieldValidator);
        inviteContributorByEmailTextField.getValidators().add(emailValidator);


        // TextFields' Listeners

        editNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && projectController.currentProject != null) {
                ProjectRepository.updateName(projectController.currentProject.getId(), editNameTextField.getText());
                projectController.currentProject.setName(editNameTextField.getText());
                titleBarController.projectNameLabel.setText(editNameTextField.getText());
                int index = leftDrawerController.userProjectsListView.getItems().indexOf(projectController.currentProject);
                leftDrawerController.userProjectsListView.getItems().set(index, projectController.currentProject);
            }
        });

        editDescriptionTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && projectController.currentProject != null) {
                ProjectRepository.updateDescription(projectController.currentProject.getId(), editDescriptionTextArea.getText());
                projectController.currentProject.setDescription(editDescriptionTextArea.getText());
            }
        });

        rolesComboBox.setItems(FXCollections.observableArrayList(Project.Role.values()));
        contributorsListView.setCellFactory(param -> {
            ContributorCell contributorCell = new ContributorCell();
            contributorCell.inject(applicationController);
            return contributorCell;
        });

        labelsListView.setCellFactory(param -> {
            LabelCell labelCell = new LabelCell();
            labelCell.inject(applicationController);
            return labelCell;
        });

    }

    @FXML
    public void handleTitledPaneOnAction(MouseEvent mouseEvent) {
        editNameTitledPane.setExpanded(false);
        editDescriptionTitledPane.setExpanded(false);
        contributorsTitledPane.setExpanded(false);
        labelsTitledPane.setExpanded(false);
        ((TitledPane) mouseEvent.getSource()).setExpanded(true);
    }

    @FXML
    public void handleInviteContributorButtonAction(MouseEvent mouseEvent) {

        resetRequiredFieldValidators();
        inviteContributorByEmailTextField.validate();
        rolesComboBox.validate();
        boolean isContributor = false;

        if (!requiredFieldValidator.getHasErrors()) {

            for (Pair<User, Project.Role> contributor : projectController.currentProject.getContributors()) {
                if (contributor.getKey().getEmail().equals(inviteContributorByEmailTextField.getText())) {
                    isContributor = true;
                }
            }

            if (isContributor) {
                emailValidator.setMessage("Already a Contributor.");
                inviteContributorByEmailTextField.getValidators().add(emailValidator);
                inviteContributorByEmailTextField.validate();
                inviteContributorByEmailTextField.getValidators().remove(emailValidator);
            } else {
                User user = UserRepository.get(FirestoreAPI.encrypt(inviteContributorByEmailTextField.getText()));

                if (user == null) {
                    emailValidator.setMessage("That Octillect account doesn't exist.");
                    inviteContributorByEmailTextField.getValidators().add(emailValidator);
                    inviteContributorByEmailTextField.validate();
                    inviteContributorByEmailTextField.getValidators().remove(emailValidator);
                } else {
                    ProjectRepository.addContributor(projectController.currentProject.getId(),
                            inviteContributorByEmailTextField.getText(), rolesComboBox.getValue());
                    UserRepository.addProject(projectController.currentProject.getId(),
                            FirestoreAPI.encrypt(inviteContributorByEmailTextField.getText()));
                    contributorsListView.getItems().add(new Pair<>(user, rolesComboBox.getValue()));
                    resetRequiredFieldValidators();
                }
            }
        }
    }

    @FXML
    public void handleAddLabelButtonAction(MouseEvent mouseEvent) {

        resetRequiredFieldValidators();
        newLabelTextField.validate();

        if (!requiredFieldValidator.getHasErrors()) {

            Label label = new LabelBuilder().with($ -> {
                $.id = FirestoreAPI.encryptWithDateTime(newLabelTextField.getText() + applicationController.user.getId());
                $.name = newLabelTextField.getText();
                $.color = labelColorPicker.getValue();
            }).build();

            LabelRepository.add(label);
            ProjectRepository.addLabelId(projectController.currentProject.getId(), label.getId());
            labelsListView.getItems().add(label);
            resetRequiredFieldValidators();
        }
    }

    @FXML
    public void handleDeleteProjectAction(MouseEvent mouseEvent) {
        ProjectRepository.delete(projectController.currentProject);
        for (Pair<User, Project.Role> collaborator : projectController.currentProject.getContributors()) {
            UserRepository.deleteProjectId(applicationController.user.getId(), projectController.currentProject.getId());
        }

        applicationController.user.getProjects().remove(projectController.currentProject);
        projectController.init();
        leftDrawerController.init();
    }

    private void resetRequiredFieldValidators() {
        inviteContributorByEmailTextField.resetValidation();
        newLabelTextField.resetValidation();
        rolesComboBox.resetValidation();
    }

    public void loadProjectSettings() {
        editNameTextField.setText(projectController.currentProject.getName());
        editDescriptionTextArea.setText(projectController.currentProject.getDescription());
        controlRoleAccess(projectController.currentProject.getUserRole(applicationController.user.getId()));
        loadContributors();
        loadLabels();
    }

    private void loadContributors() {
        ObservableList<Pair<User, Project.Role>> users = FXCollections.observableArrayList();
        projectController.currentProject.getContributors().forEach(users::add);
        contributorsListView.setItems(users);
    }

    private void loadLabels() {
        if (projectController.currentProject.getLabels() != null) {
            ObservableList<Label> labels = FXCollections.observableArrayList();
            projectController.currentProject.getLabels().forEach(labels::add);
            labelsListView.setItems(labels);
        }
    }

    public void resetProjectSettings() {
        editNameTextField.setText(null);
        editDescriptionTextArea.setText(null);
        contributorsListView.getItems().clear();
        inviteContributorByEmailTextField.setText(null);
        rolesComboBox.getSelectionModel().clearSelection();
        labelsListView.getItems().clear();
        newLabelTextField.setText(null);
        labelColorPicker.setValue(Color.WHITE);
    }

    private void controlRoleAccess(Project.Role role) {

        if (role.equals(Project.Role.owner)) {
            deleteProjectTitledPane.setDisable(false);
            deleteProjectTitledPane.setOpacity(1);
        } else {
            deleteProjectTitledPane.setDisable(true);
            deleteProjectTitledPane.setOpacity(0);
        }

        if (role.equals(Project.Role.viewer)) {

            inviteContributorByEmailTextField.setDisable(true);
            rolesComboBox.setDisable(true);
            inviteContributorButton.setDisable(true);
            inviteContributorBorderPane.setManaged(false);
            inviteContributorBorderPane.setOpacity(0);

            newLabelTextField.setDisable(true);
            labelColorPicker.setDisable(true);
            addLabelButton.setDisable(true);
            newLabelBorderPane.setDisable(true);
            newLabelBorderPane.setManaged(false);
            newLabelBorderPane.setOpacity(0);

        } else {

            inviteContributorByEmailTextField.setDisable(false);
            rolesComboBox.setDisable(false);
            inviteContributorButton.setDisable(false);
            inviteContributorBorderPane.setManaged(true);
            inviteContributorBorderPane.setOpacity(1);

            newLabelTextField.setDisable(false);
            labelColorPicker.setDisable(false);
            addLabelButton.setDisable(false);
            newLabelBorderPane.setDisable(false);
            newLabelBorderPane.setManaged(true);
            newLabelBorderPane.setOpacity(1);

        }

    }

}
