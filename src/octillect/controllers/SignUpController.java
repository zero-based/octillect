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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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

    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;
    private RegexValidator emailUsedValidator;
    private RegexValidator passwordValidator;
    private RegexValidator confirmPasswordValidator;

    private BufferedImage chosenImage = null;

    @FXML
    public void initialize() {

        requiredFieldValidator   = new RequiredFieldValidator("Required field.");
        emailValidator           = new RegexValidator("Invalid Email.");
        emailUsedValidator       = new RegexValidator("This email already have an account. Try another.");
        passwordValidator        = new RegexValidator("Use 8 or more characters with a mix of letters and numbers.");
        confirmPasswordValidator = new RegexValidator("Those passwords didn't match. Try again.");

        emailValidator          .setRegexPattern("([a-z0-9_\\.-]+)@[\\da-z\\.-]+[a-z\\.]{2,5}");
        emailUsedValidator      .setRegexPattern("^((?!.*" + emailTextField.getText() + ".*).)*$");
        passwordValidator       .setRegexPattern("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{8,}$");

        firstNameTextField      .getValidators().add(requiredFieldValidator);
        lastNameTextField       .getValidators().add(requiredFieldValidator);
        emailTextField          .getValidators().add(emailValidator);
        passwordTextField       .getValidators().add(passwordValidator);
        confirmPasswordTextField.getValidators().add(requiredFieldValidator);

        firstNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                firstNameTextField.validate();
            }
        });

        lastNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                lastNameTextField.validate();
            }
        });

        emailTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                emailTextField.validate();
            }
        });

        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                passwordTextField.validate();
            }
        });

        confirmPasswordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            confirmPasswordTextField.getValidators().add(confirmPasswordValidator);
            confirmPasswordValidator.setRegexPattern(passwordTextField.getText());
            if (!newValue) {
                confirmPasswordTextField.validate();
                confirmPasswordTextField.getValidators().remove(confirmPasswordValidator);
            }
        });
    }

    @FXML
    public void handleSignUpButtonAction(ActionEvent actionEvent) {

        firstNameTextField      .validate();
        lastNameTextField       .validate();
        emailTextField          .validate();
        passwordTextField       .validate();
        confirmPasswordTextField.validate();

        if (!requiredFieldValidator.getHasErrors() && !emailValidator.getHasErrors()
            && !passwordValidator.getHasErrors() && !confirmPasswordValidator.getHasErrors()) {

            if (UserRepository.get(FirestoreAPI.encrypt(emailTextField.getText())) != null) {
                emailTextField.getValidators().add(emailUsedValidator);
                emailTextField.validate();
                emailTextField.getValidators().remove(emailUsedValidator);
            } else {
                User user = new UserBuilder().with($ -> {
                    $.id = FirestoreAPI.encrypt(emailTextField.getText());
                    $.name = firstNameTextField.getText() + " " + lastNameTextField.getText();
                    $.email = emailTextField.getText();
                    $.password = FirestoreAPI.encrypt(passwordTextField.getText());

                    if (chosenImage != null) {
                        $.image = SwingFXUtils.toFXImage(chosenImage, null);
                    } else {
                        // Generate an Identicon for the user in case of not choosing a photo.
                        BufferedImage identicon = UserRepository.generateIdenticon($.id, 256);
                        $.image = SwingFXUtils.toFXImage(identicon, null);
                    }
                }).build();

                UserRepository.add(user);
                Main.runApplication(user);

            }
        }
    }

    @FXML
    public void handleImageButtonAction(ActionEvent actionEvent) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Profile picture");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(Main.signingStage);

        if (file != null) {
            chosenImage = ImageIO.read(new File(file.getPath()));
            userImage.setFill(new ImagePattern(SwingFXUtils.toFXImage(chosenImage, null)));
            userImage.setOpacity(100);
            imageButton.setOpacity(0);
        }

    }

    @FXML
    public void handleBackButtonAction(ActionEvent actionEvent) {
        StackPane parentStackPane = (StackPane) signUpHBox.getParent();
        Animation.easeOut(parentStackPane, signUpHBox);
    }

}
