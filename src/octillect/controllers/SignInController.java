package octillect.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import octillect.Main;
import octillect.controls.validators.CustomValidator;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.User;
import octillect.styles.Animation;

public class SignInController {

    // FXML Fields
    @FXML public StackPane signingStackPane;
    @FXML private HBox signInHBox;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXCheckBox keepMeSignedInCheckBox;

    // Validators
    private RequiredValidator requiredValidator;
    private CustomValidator emailValidator;
    private CustomValidator passwordValidator;

    @FXML
    public void initialize() {
        requiredValidator = new RequiredValidator();
        emailValidator    = new CustomValidator("This account doesn't exist.");
        passwordValidator = new CustomValidator("Incorrect Password!");

        ValidationManager.addValidator(true, requiredValidator, emailTextField, passwordField);
        ValidationManager.addValidator(true, emailValidator, emailTextField);
        ValidationManager.addValidator(true, passwordValidator, passwordField);
    }

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) {

        emailTextField.validate();
        passwordField.validate();

        if (!requiredValidator.getHasErrors()) {
            if (!UserRepository.getInstance().isUserFound(emailTextField.getText())) {
                emailValidator.showMessage();
            } else if (!UserRepository.getInstance().authenticate(emailTextField.getText(), passwordField.getText())) {
                passwordValidator.showMessage();
            } else {
                User user = UserRepository.getInstance().get(FirestoreAPI.getInstance().encrypt(emailTextField.getText()));
                if (keepMeSignedInCheckBox.isSelected()) {
                    // Save User's data in octillect's file.
                    UserRepository.getInstance().rememberUser(user);
                }
                resetSignInView();
                Main.initApplicationStage(user);
                Main.showApplicationStage();
            }
        }

    }

    @FXML
    void handleSignInWithGitHubButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleCreateAnAccountAction(ActionEvent actionEvent) throws Exception {
        StackPane root = (StackPane) Main.signingStage.getScene().getRoot();
        HBox signUpHBox = FXMLLoader.load(getClass().getResource("/octillect/views/SignUpView.fxml"));
        Animation.easeOut(root, signInHBox, Duration.seconds(0.8), Animation.Direction.LEFT);
        Animation.easeIn(root, signUpHBox, Duration.seconds(0.8), Animation.Direction.LEFT);
        resetSignInView();
    }

    private void resetSignInView() {
        emailTextField.setText(null);
        passwordField.setText(null);
        keepMeSignedInCheckBox.setSelected(false);
    }

}
