package octillect.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import octillect.Main;
import octillect.controls.OButton;
import octillect.database.accessors.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.User;
import octillect.models.builders.UserBuilder;
import octillect.styles.Animation;

public class SignUpController {

    @FXML private HBox signUpHBox;
    @FXML private OButton signUpButton;
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
    private RegexValidator emailUsedValidator             = new RegexValidator();
    private RegexValidator passwordValidator              = new RegexValidator();
    private RegexValidator confirmPasswordValidator       = new RegexValidator();

    private String chosenImagePath = null;

    // TextFields Validation
    @FXML
    public void initialize() {
        firstNameTextField.getValidators().add(requiredFieldValidator);
        lastNameTextField .getValidators().add(requiredFieldValidator);
        emailTextField    .getValidators().add(emailValidator);

        requiredFieldValidator.setMessage("Required field.");

        emailValidator.setRegexPattern("([a-z0-9_\\.-]+)@[\\da-z\\.-]+[a-z\\.]{2,5}");
        emailValidator.setMessage("Invalid Email.");

        emailUsedValidator.setRegexPattern("^((?!.*"+ emailTextField.getText() +".*).)*$");

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
            emailTextField.getValidators().remove(emailUsedValidator);
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
        } else if (UserRepository.get(FirestoreAPI.encrypt(emailTextField.getText())) != null) {
            emailUsedValidator.setMessage("This email already have an account. Try another.");
            emailTextField.getValidators().add(emailUsedValidator);
            emailTextField.validate();
            signUpButton.setOnAction(null);
        } else {
            User user = new UserBuilder().with($ -> {
                $.id       = FirestoreAPI.encrypt(emailTextField.getText());
                $.email    = emailTextField.getText();
                $.password = FirestoreAPI.encrypt(passwordTextField.getText());
                $.name     = firstNameTextField.getText() + " " + lastNameTextField.getText();
            }).build();

            UserRepository.add(user);

            if (chosenImagePath != null) {
                UserRepository.setImage(user.getId(), getChosenImage(chosenImagePath));
                user.setImage(SwingFXUtils.toFXImage(getChosenImage(chosenImagePath), null));
            } else {
                // Generate an Identicon for the user in case of not choosing a photo
                UserRepository.setImage(user.getId(), UserRepository.generateIdenticon(user.getId(), 256));
                Image image = SwingFXUtils.toFXImage(UserRepository.generateIdenticon(user.getId(), 256), null);
                user.setImage(image);
            }

            Main.runApplication(user);
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
            ImagePattern imagePattern = new ImagePattern(SwingFXUtils.toFXImage(getChosenImage(chosenImagePath), null));
            userImage.setFill(imagePattern);
            userImage.setOpacity(100);
            imageButton.setOpacity(0);
        } else {
            // This Code Block Handles choosing a picture then opening file chooser and then choose nothing
            imageButton.setOpacity(100);
            userImage.setFill(null);
        }
    }

    /**
     * Converts image to a BufferedImage.
     *
     * @param imagePath Image File path.
     * @return BufferedImage of the given image.
     */
    public BufferedImage getChosenImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void handleBackButtonAction(ActionEvent actionEvent) {
        StackPane parentStackPane = (StackPane) signUpHBox.getParent();
        Animation.easeOut(parentStackPane, signUpHBox);
    }

}
