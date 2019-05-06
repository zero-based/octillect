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
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import javafx.util.StringConverter;

import octillect.controls.ContributorCell;
import octillect.controls.LabelCell;
import octillect.controls.OButton;
import octillect.database.accessors.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Project;
import octillect.models.User;
import octillect.models.builders.LabelBuilder;
import octillect.models.builders.UserBuilder;

public class ProjectSettingsController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public TitledPane editNameTitledPane;
    @FXML public JFXTextField editNameTextArea;
    @FXML public OButton saveNameButton;
    @FXML public TitledPane editDescriptionTitledPane;
    @FXML public JFXTextArea editDescriptionTextArea;
    @FXML public OButton saveDescriptionButton;
    @FXML public TitledPane contributorsTitledPane;
    @FXML public JFXListView<Pair<User, Project.Role>> contributorsListView;
    @FXML public JFXTextField inviteContributorByEmailTextField;
    @FXML public JFXComboBox rolesComboBox;
    @FXML public OButton inviteContributorButton;
    @FXML public TitledPane labelsTitledPane;
    @FXML public JFXListView labelsListView;
    @FXML public JFXTextField newLabelTextField;
    @FXML public JFXColorPicker labelColorPicker;
    @FXML public OButton addLabelButton;
    @FXML public TitledPane deleteProjectTitledPane;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    ProjectController projectController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
    }

    @Override
    public void init() {

        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        emailValidator = new RegexValidator();
        emailValidator.setRegexPattern("^((?!.*" + inviteContributorByEmailTextField.getText() + ".*).)*$");

        newLabelTextField.getValidators().add(requiredFieldValidator);
        rolesComboBox.getValidators().add(requiredFieldValidator);
        inviteContributorByEmailTextField.getValidators().add(requiredFieldValidator);
        inviteContributorByEmailTextField.getValidators().add(emailValidator);

        //Initializing rolesComboBox
        for (Project.Role role : Project.Role.values()) {
            rolesComboBox.getItems().add(new Label(role.toString()));
        }

        rolesComboBox.setEditable(true);
        rolesComboBox.setConverter(new StringConverter<Label>() {
            @Override
            public String toString(Label object) {
                return object == null ? "" : object.getText();
            }

            @Override
            public Label fromString(String string) {
                return new Label(string);
            }
        });

        //Initializing usersListView
        User user1 = new UserBuilder().with($ -> {
            $.name = "Monica Adel";
            $.email = "monica@gmail.com";
        }).build();
        User user2 = new UserBuilder().with($ -> {
            $.name = "Youssef Raafat";
            $.email = "usf@gmail.com";
        }).build();
        User user3 = new UserBuilder().with($ -> {
            $.name = "Monica Atef";
            $.email = "monica@hotmail.com";
        }).build();

        ObservableList<Pair<User, Project.Role>> contributors = FXCollections.observableArrayList(
                new Pair(user1, Project.Role.owner),
                new Pair(user2, Project.Role.admin),
                new Pair(user3, Project.Role.viewer));
        contributorsListView.setItems(contributors);
        contributorsListView.setCellFactory(param -> {
            ContributorCell contributorCell = new ContributorCell();
            contributorCell.inject(applicationController);
            return contributorCell;
        });

        //Initializing labelsListView
        octillect.models.Label label1 = new LabelBuilder().with($_label1 -> {
            $_label1.name = "back-end";
            $_label1.color = Color.WHITE;
        }).build();
        octillect.models.Label label2 = new LabelBuilder().with($_label2 -> {
            $_label2.name = "bug";
            $_label2.color = Color.LIGHTPINK;
        }).build();
        octillect.models.Label label3 = new LabelBuilder().with($_label3 -> {
            $_label3.name = "improvement";
            $_label3.color = Color.BLACK;
        }).build();

        ObservableList<octillect.models.Label> labels = FXCollections.observableArrayList(label1, label2, label3);
        labelsListView.setItems(labels);
        labelsListView.setCellFactory(param -> {
            LabelCell labelCell = new LabelCell();
            labelCell.inject(applicationController);
            return labelCell;
        });

    }

    public void handleTitledPaneOnAction(MouseEvent mouseEvent) {
        editNameTitledPane.setExpanded(false);
        editDescriptionTitledPane.setExpanded(false);
        contributorsTitledPane.setExpanded(false);
        labelsTitledPane.setExpanded(false);
        ((TitledPane) mouseEvent.getSource()).setExpanded(true);
    }

    public void handleSaveNameButtonAction(MouseEvent mouseEvent) {
    }

    public void handleSaveDescriptionButtonAction(MouseEvent mouseEvent) {
    }

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
                    /*TODO: Add Contributor to Database/ListView Here.*/
                    resetRequiredFieldValidators();
                }
            }
        }
    }

    public void handleAddLabelButtonAction(MouseEvent mouseEvent) {

        resetRequiredFieldValidators();
        newLabelTextField.validate();

        if (!requiredFieldValidator.getHasErrors()) {

            octillect.models.Label label = new LabelBuilder().with($ -> {
                $.id = FirestoreAPI.encryptWithDateTime(newLabelTextField.getText() + applicationController.user.getId());
                $.name = newLabelTextField.getText();
                $.color = labelColorPicker.getValue();
            }).build();

            /*TODO: Add Label to Database/ListView Here.*/
            resetRequiredFieldValidators();
        }
    }

    public void handleDeleteProjectAction(MouseEvent mouseEvent) {
    }

    private void resetRequiredFieldValidators() {
        inviteContributorByEmailTextField.resetValidation();
        newLabelTextField.resetValidation();
        rolesComboBox.resetValidation();
    }

}
