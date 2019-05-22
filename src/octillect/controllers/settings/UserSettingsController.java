package octillect.controllers.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import octillect.Main;
import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.TitleBarController;
import octillect.controls.validators.CustomValidator;
import octillect.controls.validators.EmailValidator;
import octillect.controls.validators.MatchPasswordValidator;
import octillect.controls.validators.PasswordValidator;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.repositories.UserRepository;
import octillect.models.*;

public class UserSettingsController implements Injectable<ApplicationController> {

    // FXML fields
    @FXML public JFXTextField nameTextField;
    @FXML public JFXTextField emailTextField;
    @FXML public JFXPasswordField oldPasswordField;
    @FXML public JFXPasswordField newPasswordField;
    @FXML public JFXPasswordField confirmPasswordField;

    // Validators
    private RequiredValidator requiredValidator;
    private EmailValidator emailValidator;
    private CustomValidator emailUsedValidator;
    private CustomValidator oldPasswordValidator;
    private PasswordValidator passwordValidator;
    private MatchPasswordValidator matchPasswordValidator;

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
        requiredValidator      = new RequiredValidator();
        emailValidator         = new EmailValidator();
        emailUsedValidator     = new CustomValidator("Email in use. Try another.");
        oldPasswordValidator   = new CustomValidator("Incorrect Password.");
        passwordValidator      = new PasswordValidator();
        matchPasswordValidator = new MatchPasswordValidator(newPasswordField);

        ValidationManager.addValidator(false, requiredValidator, nameTextField, confirmPasswordField);
        ValidationManager.addValidator(false, emailValidator, emailTextField);
        ValidationManager.addValidator(false, emailUsedValidator, emailTextField);
        ValidationManager.addValidator(false, oldPasswordValidator, oldPasswordField);
        ValidationManager.addValidator(false, passwordValidator, newPasswordField);
        ValidationManager.addValidator(false, matchPasswordValidator, confirmPasswordField);
    }

    @FXML
    public void handleSaveProfileButton(MouseEvent mouseEvent) {

        nameTextField.validate();
        emailTextField.validate();

        boolean nameChanged  = !nameTextField.getText().equals(applicationController.user.getName());
        boolean emailChanged = !emailTextField.getText().equals(applicationController.user.getEmail());

        if (!requiredValidator.getHasErrors()) {

            if (nameChanged) {
                UserRepository.getInstance().updateName(applicationController.user.getId(), nameTextField.getText());
                applicationController.user.setName(nameTextField.getText());
            }

            if (!emailValidator.getHasErrors() && emailChanged) {
                if (UserRepository.getInstance().isUserFound(emailTextField.getText())) {
                    emailUsedValidator.showMessage();
                } else {
                    UserRepository.getInstance().updateEmail(applicationController.user, emailTextField.getText());
                    updateUserEmail(emailTextField.getText());
                    boardController.boardListView.refresh();
                }
            }

        }
    }

    @FXML
    public void handleSavePasswordButton(MouseEvent mouseEvent) {

        oldPasswordField.validate();
        String encryptedOldPassword = FirestoreAPI.getInstance().encrypt(oldPasswordField.getText());
        boolean isCorrect = applicationController.user.getPassword().equals(encryptedOldPassword);

        if (isCorrect) {
            newPasswordField.validate();
            confirmPasswordField.validate();
            if (!requiredValidator.getHasErrors() && !passwordValidator.getHasErrors()
                && !matchPasswordValidator.getHasErrors()) {
                String newPassword = FirestoreAPI.getInstance().encrypt(newPasswordField.getText());
                UserRepository.getInstance().updatePassword(applicationController.user.getId(), newPassword);
                applicationController.user.setPassword(newPassword);
                oldPasswordField    .setText("");
                newPasswordField    .setText("");
                confirmPasswordField.setText("");
            }
        } else {
            oldPasswordValidator.showMessage();
        }

    }

    @FXML
    public void handleLogOutButtonAction(MouseEvent mouseEvent) {
        Main.showSigningStage();
    }

    public void LoadUserSettings() {
        nameTextField       .setText(applicationController.user.getName());
        emailTextField      .setText(applicationController.user.getEmail());
        oldPasswordField    .setText("");
        newPasswordField    .setText("");
        confirmPasswordField.setText("");

        nameTextField       .resetValidation();
        emailTextField      .resetValidation();
        oldPasswordField    .resetValidation();
        newPasswordField    .resetValidation();
        confirmPasswordField.resetValidation();
    }

    private void updateUserEmail(String newEmail) {

        String newId = FirestoreAPI.getInstance().encrypt(newEmail);
        Image newImage = SwingFXUtils.toFXImage(UserRepository
                .getInstance()
                .generateIdenticon(newId, 256), null);

        for (Board board : applicationController.user.getBoards()) {

            // Update Contributors emails
            for (Contributor contributor : board.getContributors()) {
                if (contributor.getEmail().equals(applicationController.user.getEmail())) {
                    contributor.setId(newId);
                    contributor.setEmail(newEmail);
                    contributor.setImage(newImage);
                }
            }

            for (Column column : board.<Column>getChildren()) {

                // Update tasks' creatorId
                for (Task task : column.<Task>getChildren()) {
                    if (task.getCreator().getEmail().equals(applicationController.user.getEmail())) {
                        task.getCreator().setId(newId);
                        task.getCreator().setEmail(newEmail);
                        task.getCreator().setImage(newImage);
                    }

                    // Update tasks' assignees' emails

                    task.getAssignees().forEach(assignee -> {
                        if (assignee.getEmail().equals(applicationController.user.getEmail())) {
                            assignee.setId(FirestoreAPI.getInstance().encrypt(newEmail));
                            assignee.setEmail(newEmail);
                            assignee.setImage(newImage);
                        }
                    });

                }
            }
        }

        applicationController.user.setId(newId);
        applicationController.user.setEmail(newEmail);
        applicationController.user.setImage(newImage);
        titleBarController.loadUserImage(newImage);

        if (Main.octillectFile.exists()) {
            UserRepository.getInstance().rememberUser(applicationController.user);
        }
    }

}
