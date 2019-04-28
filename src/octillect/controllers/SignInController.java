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

    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;
    private RegexValidator passwordValidator;

    @FXML
    public void initialize() {

        // Animation
        Animation.rotate(backgroundImageView);

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
            User user = UserRepository.get(FirestoreAPI.encrypt(emailTextField.getText()));
            if (user == null) {
                emailTextField.getValidators().add(emailValidator);
                emailTextField.validate();
                emailTextField.getValidators().remove(emailValidator);
            } else if (!user.getPassword().equals(FirestoreAPI.encrypt(passwordTextField.getText()))) {
                passwordTextField.getValidators().add(passwordValidator);
                passwordTextField.validate();
                passwordTextField.getValidators().remove(passwordValidator);
            } else {
                if (keepMeSignedInCheckBox.isSelected()) {
                    // Save User's data in octillect's file.
                    UserRepository.rememberUser(user);
                }
                Main.runApplication(user);
            }
        }

    }

    @FXML
    void handleSignInWithGitHubButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleCreateAnAccountAction(ActionEvent actionEvent) throws Exception {
        HBox signUpHBox = FXMLLoader.load(getClass().getResource("/octillect/views/SignUpView.fxml"));
        StackPane parentStackPane = (StackPane) signInHBox.getParent();
        Animation.easeIn(parentStackPane, signUpHBox);
    }

}
