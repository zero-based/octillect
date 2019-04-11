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
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import octillect.Main;
import octillect.controls.OButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML private HBox signUpHBox;
    @FXML private OButton signUpButton;
    @FXML private OButton signUpWithGitHubButton;
    @FXML private OButton backButton;
    @FXML private OButton imageButton;
    @FXML private Circle userImage;
    @FXML private JFXTextField firstNameTextField;
    @FXML private JFXTextField lastNameTextField;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private JFXPasswordField confirmPasswordTextField;

    private RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
    private RegexValidator emailValidator                 = new RegexValidator();
    private RegexValidator passwordValidator              = new RegexValidator();
    private RegexValidator confirmPasswordValidator       = new RegexValidator();

    private String chosenImagePath;

    @FXML
    public void handleSignUpButtonAction(ActionEvent actionEvent) {
        firstNameTextField      .validate();
        lastNameTextField       .validate();
        emailTextField          .validate();
        passwordTextField       .getValidators().add(requiredFieldValidator);
        passwordTextField       .validate();
        confirmPasswordTextField.getValidators().add(requiredFieldValidator);
        confirmPasswordTextField.validate();

        // set Sign up Button's action handler to null when there's any Validation errors
        if (requiredFieldValidator.getHasErrors() || emailValidator.getHasErrors() || passwordValidator.getHasErrors()) {
            signUpButton.setOnAction(null);
        } else {
            // Add new user
        }
    }

    @FXML
    public void handleImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Profile picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(Main.signingStage);

        if (file != null) {
            chosenImagePath = file.getPath();
            try {
                FileInputStream image = new FileInputStream(chosenImagePath);
                userImage.setFill(new ImagePattern(new Image(image)));
                userImage.setOpacity(100);
                imageButton.setOpacity(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            imageButton.setOpacity(100);
            userImage.setFill(null);
        }
    }


    @FXML
    public void handleSignUpWithGitHubButtonAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleBackButtonAction(ActionEvent actionEvent) {
        // Initialize values
        StackPane parentStackPane = (StackPane) signUpHBox.getParent();
        double startValue = 0;
        double endValue = parentStackPane.getWidth();
        signUpHBox.translateXProperty().set(startValue);

        // Animation
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(signUpHBox.translateXProperty(), endValue, Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(650), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> parentStackPane.getChildren().remove(signUpHBox));
        timeline.play();
    }

    // TextFields Validation
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameTextField      .getValidators().add(requiredFieldValidator);
        lastNameTextField       .getValidators().add(requiredFieldValidator);
        emailTextField          .getValidators().add(emailValidator);

        requiredFieldValidator.setMessage("Required field.");

        emailValidator.setRegexPattern("([a-z0-9_\\.-]+)@[\\da-z\\.-]+[a-z\\.]{2,5}");
        emailValidator.setMessage("Invalid Email.");

        passwordValidator.setRegexPattern("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{8,}$");
        passwordValidator.setMessage("Use 8 or more characters with a mix of letters and numbers.");

        confirmPasswordValidator.setMessage("Those passwords didn't match. Try again.");

        firstNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                firstNameTextField.validate();
                signUpButton.setOnAction(this::handleSignUpButtonAction);
            }
        });

        lastNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                lastNameTextField.validate();
                signUpButton.setOnAction(this::handleSignUpButtonAction);
            }
        });

        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                emailTextField.validate();
                signUpButton.setOnAction(this::handleSignUpButtonAction);
            }
        });

        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            passwordTextField.getValidators().add(passwordValidator);
            if (!newValue) {
                passwordTextField.validate();
                signUpButton.setOnAction(this::handleSignUpButtonAction);
            }
        });

        confirmPasswordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            confirmPasswordValidator.setRegexPattern(passwordTextField.getText());
            if (!newValue && !passwordTextField.getText().equals("")) {
                confirmPasswordTextField.getValidators().add(confirmPasswordValidator);
                confirmPasswordTextField.validate();
                signUpButton.setOnAction(this::handleSignUpButtonAction);
            }
        });
    }
}
