package octillect.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import octillect.Main;
import octillect.controls.OButton;
import octillect.database.accessors.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.User;
import octillect.styles.Animation;

public class SignInController {

    @FXML private HBox signInHBox;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private JFXCheckBox keepMeSignedInCheckBox;
    @FXML private OButton signInButton;
    @FXML private OButton signInWithGitHubButton;
    @FXML private Hyperlink createAnAccountButton;
    @FXML private ImageView backgroundImageView;


    private RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
    private RegexValidator passwordValidator              = new RegexValidator();
    private RegexValidator emailValidator                 = new RegexValidator();

    // TextFields Validation
    @FXML
    public void initialize() {

        // Animation
        Animation.rotate(backgroundImageView);
        
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

        User user = UserRepository.get(FirestoreAPI.encrypt(emailTextField.getText()));

        if (requiredFieldValidator.getHasErrors()) {
            signInButton.setOnAction(null);
        } else if (user != null) {

            // Check if the user entered the right email and password
            if (user.getPassword().equals(FirestoreAPI.encrypt(passwordTextField.getText()))) {
                Main.runApplication(user);
                /* if a validated user check keepMeSignedInCheckBox, his/her data will be saved in octillect's file. */
                if (keepMeSignedInCheckBox.isSelected())
                    UserRepository.rememberUser(user);
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
    public void handleCreateAnAccountAction(ActionEvent actionEvent) throws Exception {

        HBox signUpHBox = FXMLLoader.load(getClass().getResource("/octillect/views/SignUpView.fxml"));
        StackPane parentStackPane = (StackPane) signInHBox.getParent();

        Animation.easeIn(parentStackPane, signUpHBox);
    }
}
