package octillect.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import octillect.Main;
import octillect.controls.OButton;
import octillect.database.accessors.UserRepository;
import octillect.models.User;

public class SignInController {

    @FXML private HBox signInHBox;
    @FXML private OButton signInButton;
    @FXML private OButton createAnAccountButton;
    @FXML private OButton signInWithGitHubButton;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordTextField;

    private RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
    private RegexValidator passwordValidator              = new RegexValidator();
    private RegexValidator emailValidator                 = new RegexValidator();

    // TextFields Validation
    @FXML
    public void initialize() {
        requiredFieldValidator.setMessage("Required field.");
        emailTextField        .getValidators().add(requiredFieldValidator);
        passwordTextField     .getValidators().add(requiredFieldValidator);

        // Email validations
        emailValidator.setRegexPattern("^((?!.*" + emailTextField.getText() + ".*).)*$");
        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            emailTextField.getValidators().remove(requiredFieldValidator);
            emailTextField.getValidators().remove(emailValidator);
            passwordTextField.getValidators().remove(passwordValidator);
            if (!newValue) {
                emailTextField.validate();
                signInButton.setOnAction(this::handleSignInButtonAction);
            }
        });

        // Password validations
        passwordValidator.setRegexPattern("^((?!.*" + passwordTextField.getText() + ".*).)*$");
        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            emailTextField.getValidators().remove(emailValidator);
            passwordTextField.getValidators().remove(requiredFieldValidator);
            if (!newValue) {
                passwordTextField.validate();
                signInButton.setOnAction(this::handleSignInButtonAction);
            }
        });
    }

    @FXML void handleSignInWithGitHubButtonAction(ActionEvent actionEvent)
    {
    }

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) {

        passwordTextField.getValidators().add(requiredFieldValidator);
        passwordTextField.validate();
        emailTextField.getValidators().add(requiredFieldValidator);
        emailTextField.validate();

        User user = UserRepository.get(UserRepository.encrypt(emailTextField.getText()));

        if (requiredFieldValidator.getHasErrors()) {
            signInButton.setOnAction(null);
        } else if (user != null) {

            // Check if the user entered the right email and password
            if (user.getPassword().equals(UserRepository.encrypt(passwordTextField.getText()))) {
                Main.runApplication(user);
            } else {
                passwordValidator.setMessage("Incorrect Password!");
                passwordTextField.getValidators().add(passwordValidator);
                passwordTextField.validate();
                signInButton     .setOnAction(null);
            }
        } else {
            emailValidator.setMessage("That Octillect account doesn't exist.");
            emailTextField.getValidators().add(emailValidator);
            emailTextField.validate();
            signInButton  .setOnAction(null);
        }
    }

    @FXML
    public void handleCreateAnAccountAction(ActionEvent actionEvent) throws Exception{
        // Initialize values
        Parent signUpRoot = FXMLLoader.load(getClass().getResource("/octillect/views/SignUpView.fxml"));
        StackPane parentStackPane = (StackPane) signInHBox.getParent();
        double startValue = parentStackPane.getWidth();
        double endValue = 0;
        signUpRoot.translateXProperty().set(startValue);
        parentStackPane.getChildren().add(signUpRoot);

        // Animation
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(signUpRoot.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(650), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
