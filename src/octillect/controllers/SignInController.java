package octillect.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import octillect.Main;
import octillect.database.repositories.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.User;
import octillect.styles.Animation;

public class SignInController {

    // FXML Fields
    @FXML public StackPane signingStackPane;
    @FXML private HBox signInHBox;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private JFXCheckBox keepMeSignedInCheckBox;
    @FXML private ImageView backgroundImageView;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;
    private RegexValidator passwordValidator;

    @FXML
    public void initialize() {

        // Animation
        Animation.rotate(backgroundImageView, Duration.seconds(16));

        // Initialize Validations
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        emailValidator         = new RegexValidator("That Octillect account doesn't exist.");
        passwordValidator      = new RegexValidator("Incorrect Password!");

        emailTextField.getValidators().add(requiredFieldValidator);
        emailValidator.setRegexPattern("^((?!.*" + emailTextField.getText() + ".*).)*$");
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            emailTextField.resetValidation();
            if (!newValue) {
                emailTextField.validate();
            }
        });

        passwordTextField.getValidators().add(requiredFieldValidator);
        passwordValidator.setRegexPattern("^((?!.*" + passwordTextField.getText() + ".*).)*$");
        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            passwordTextField.resetValidation();
            if (!newValue) {
                passwordTextField.validate();
            }
        });

    }

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) {

        emailTextField.validate();
        passwordTextField.validate();

        if (!requiredFieldValidator.getHasErrors()) {
            User user = UserRepository.getInstance().get(FirestoreAPI.getInstance().encrypt(emailTextField.getText()));
            if (user == null) {
                emailTextField.getValidators().add(emailValidator);
                emailTextField.validate();
                emailTextField.getValidators().remove(emailValidator);
            } else if (!user.getPassword().equals(FirestoreAPI.getInstance().encrypt(passwordTextField.getText()))) {
                passwordTextField.getValidators().add(passwordValidator);
                passwordTextField.validate();
                passwordTextField.getValidators().remove(passwordValidator);
            } else {
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
        Animation.easeOut(root, signInHBox, Duration.seconds(0.8), Animation.Direction.RIGHT);
        Animation.easeIn(root, signUpHBox, Duration.seconds(0.8), Animation.Direction.RIGHT);
        resetSignInView();
    }

    private void resetSignInView() {
        emailTextField.setText(null);
        passwordTextField.setText(null);
        keepMeSignedInCheckBox.setSelected(false);
    }

}
