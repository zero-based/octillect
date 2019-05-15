package octillect.controllers.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import octillect.Main;
import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.TitleBarController;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.repositories.UserRepository;
import octillect.models.*;


public class UserSettingsController implements Injectable<ApplicationController> {

    // FXML fields
    @FXML public JFXTextField nameTextField;
    @FXML public JFXTextField emailTextField;
    @FXML public JFXPasswordField newPasswordPasswordField;
    @FXML public JFXPasswordField oldPasswordPasswordField;
    @FXML public JFXPasswordField confirmPasswordPasswordField;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;
    private RegexValidator emailUsedValidator;
    private RegexValidator passwordValidator;
    private RequiredFieldValidator oldPasswordValidator;
    private RegexValidator confirmPasswordValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private TitleBarController titleBarController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController  = applicationController;
        boardController             = applicationController.boardController;
        titleBarController          = applicationController.titleBarController;
    }

    @Override
    public void init() {

        LoadUserSettings();

        // Initialize Validators
        requiredFieldValidator   = new RequiredFieldValidator("Required field.");
        emailValidator           = new RegexValidator("Invalid Email.");
        emailUsedValidator       = new RegexValidator("This email already have an account. Try another.");
        oldPasswordValidator     = new RequiredFieldValidator("Incorrect Password.");
        passwordValidator        = new RegexValidator("Use 8 or more characters with a mix of letters and numbers.");
        confirmPasswordValidator = new RegexValidator("Those passwords didn't match. Try again.");

        emailValidator.setRegexPattern("([a-z0-9_\\.-]+)@[\\da-z\\.-]+[a-z\\.]{2,5}");
        emailUsedValidator.setRegexPattern("^((?!.*" + emailTextField.getText() + ".*).)*$");
        passwordValidator.setRegexPattern("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{8,}$");

        nameTextField               .getValidators().add(requiredFieldValidator);
        emailTextField              .getValidators().add(requiredFieldValidator);
        oldPasswordPasswordField    .getValidators().add(oldPasswordValidator);
        newPasswordPasswordField    .getValidators().add(requiredFieldValidator);
        newPasswordPasswordField    .getValidators().add(passwordValidator);
        confirmPasswordPasswordField.getValidators().add(requiredFieldValidator);
        confirmPasswordPasswordField.getValidators().add(confirmPasswordValidator);

        // TextFields' Listeners

        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                nameTextField.validate();
            }
        });

        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                emailTextField.validate();
                if (UserRepository.getInstance().get(FirestoreAPI.getInstance().encrypt(emailTextField.getText())) != null) {
                    emailTextField.getValidators().add(emailUsedValidator);
                    emailTextField.validate();
                    emailTextField.getValidators().remove(emailUsedValidator);
                }
            }
        });

        oldPasswordPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                oldPasswordPasswordField.validate();
                String oldPassword = FirestoreAPI.getInstance().encrypt(oldPasswordPasswordField.getText());
                if (!oldPassword.equals(applicationController.user.getPassword())) {
                    oldPasswordPasswordField.setText("");
                    oldPasswordValidator.validate();

                }
            }
        });

        newPasswordPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newPasswordPasswordField.validate();
            }
        });

        confirmPasswordPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            confirmPasswordValidator.setRegexPattern(newPasswordPasswordField.getText());
            if (!newValue) {
                confirmPasswordPasswordField.validate();
            }
        });

    }

    @FXML
    public void handleSaveProfileButton(MouseEvent mouseEvent) {

        nameTextField.resetValidation();
        nameTextField.resetValidation();

        if (!requiredFieldValidator.getHasErrors() && !nameTextField.getText().equals(applicationController.user.getName())) {
            UserRepository.getInstance().updateName(applicationController.user.getId(),
                    nameTextField.getText());
            applicationController.user.setName(nameTextField.getText());
        }

        if (!requiredFieldValidator.getHasErrors() && !emailValidator.getHasErrors() && !emailUsedValidator.getHasErrors()
                && !emailTextField.getText().equals(applicationController.user.getEmail())) {
            UserRepository.getInstance().updateEmail(applicationController.user, emailTextField.getText());
            updateUser(emailTextField.getText());

            boardController.init();
        }

    }

    @FXML
    public void handleSavePasswordButton(MouseEvent mouseEvent) {

        oldPasswordPasswordField.resetValidation();
        newPasswordPasswordField.resetValidation();
        confirmPasswordPasswordField.resetValidation();

        oldPasswordPasswordField.validate();
        newPasswordPasswordField.validate();
        confirmPasswordPasswordField.validate();

        if (!applicationController.user.getPassword().equals(FirestoreAPI.getInstance().encrypt(newPasswordPasswordField.getText()))
                && !passwordValidator.getHasErrors() && !requiredFieldValidator.getHasErrors()
                && !oldPasswordValidator.getHasErrors()) {

            String newPassword = FirestoreAPI.getInstance().encrypt(newPasswordPasswordField.getText());
            UserRepository.getInstance().updatePassword(applicationController.user.getId(), newPassword);
            
            applicationController.user.setPassword(newPassword);
        }

    }

    @FXML
    public void handleLogOutButtonAction(MouseEvent mouseEvent) {
        Main.showSigningStage();
    }

    private void LoadUserSettings() {
        nameTextField.setText(applicationController.user.getName());
        emailTextField.setText(applicationController.user.getEmail());
    }

    private void updateUser(String updatedEmail) {

        String updatedId = FirestoreAPI.getInstance().encrypt(updatedEmail);
        Image updatedImage = SwingFXUtils.toFXImage(UserRepository
                .getInstance()
                .generateIdenticon(updatedId, 256), null);

        for (Board board : applicationController.user.getBoards()) {

            // Update Contributors emails
            for (Contributor contributor : board.getContributors()) {
                if (contributor.getEmail().equals(applicationController.user.getEmail())) {
                    contributor.setId(updatedId);
                    contributor.setEmail(updatedEmail);
                    contributor.setImage(updatedImage);
                }
            }

            for (Column column : board.<Column>getChildren()) {

                // Update tasks' creatorId
                for (Task task : column.<Task>getChildren()) {
                    if (task.getCreator().getEmail().equals(applicationController.user.getEmail())) {
                        task.getCreator().setId(updatedId);
                        task.getCreator().setEmail(updatedEmail);
                        task.getCreator().setImage(updatedImage);
                    }

                    // Update tasks' assignees' emails

                    task.getAssignees().forEach(assignee -> {
                        if (assignee.getEmail().equals(applicationController.user.getEmail())) {
                            assignee.setId(FirestoreAPI.getInstance().encrypt(updatedEmail));
                            assignee.setEmail(updatedEmail);
                            assignee.setImage(updatedImage);
                        }
                    });

                }
            }
        }

        applicationController.user.setId(updatedId);
        applicationController.user.setEmail(updatedEmail);
        applicationController.user.setImage(updatedImage);
        titleBarController.loadUserImage(updatedImage);

        if (Main.octillectFile.exists()) {
            Main.octillectFile.delete();
        }
    }

}
