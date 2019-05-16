package octillect.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

import octillect.Main;
import octillect.controls.OButton;
import octillect.database.repositories.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Board;
import octillect.models.User;
import octillect.models.builders.UserBuilder;
import octillect.styles.Animation;

public class SignUpController {

    // Local Fields
    private BufferedImage chosenImage = null;

    // FXML Fields
    @FXML private HBox signUpHBox;
    @FXML private OButton imageButton;
    @FXML private Circle userImage;
    @FXML private JFXTextField firstNameTextField;
    @FXML private JFXTextField lastNameTextField;
    @FXML private JFXTextField emailTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private JFXPasswordField confirmPasswordTextField;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator emailValidator;
    private RegexValidator emailUsedValidator;
    private RegexValidator passwordValidator;
    private RegexValidator confirmPasswordValidator;

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
    public void handleSignUpButtonAction(ActionEvent actionEvent) throws IOException {

        firstNameTextField      .validate();
        lastNameTextField       .validate();
        emailTextField          .validate();
        passwordTextField       .validate();
        confirmPasswordTextField.validate();

        if (!requiredFieldValidator.getHasErrors() && !emailValidator.getHasErrors()
            && !passwordValidator.getHasErrors() && !confirmPasswordValidator.getHasErrors()) {

            if (UserRepository.getInstance().get(FirestoreAPI.getInstance().encrypt(emailTextField.getText())) != null) {
                emailTextField.getValidators().add(emailUsedValidator);
                emailTextField.validate();
                emailTextField.getValidators().remove(emailUsedValidator);
            } else {
                User user = new UserBuilder().with($ -> {
                    $.id = FirestoreAPI.getInstance().encrypt(emailTextField.getText());
                    $.name = firstNameTextField.getText() + " " + lastNameTextField.getText();
                    $.email = emailTextField.getText();
                    $.password = FirestoreAPI.getInstance().encrypt(passwordTextField.getText());

                    if (chosenImage != null) {
                        $.image = SwingFXUtils.toFXImage(chosenImage, null);
                    } else {
                        // Generate an Identicon for the user in case of not choosing a photo.
                        BufferedImage identicon = UserRepository.getInstance().generateIdenticon($.id, 256);
                        $.image = SwingFXUtils.toFXImage(identicon, null);
                    }
                }).build();

                user.setBoards(FXCollections.observableArrayList(new Board.WelcomeBoard(user)));
                UserRepository.getInstance().add(user);

                closeSignUpView();

                Main.initApplicationStage(user);
                Main.showApplicationStage();
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
            userImage.setOpacity(1);
            imageButton.setOpacity(0);
        }

    }

    @FXML
    public void handleBackButtonAction() throws IOException {
        closeSignUpView();
    }

    private void closeSignUpView() throws IOException {
        StackPane root = (StackPane) Main.signingStage.getScene().getRoot();
        StackPane signingStackPane = FXMLLoader.load(getClass().getResource("/octillect/views/SignInView.fxml"));
        HBox signInHBox = (HBox) signingStackPane.lookup("#signInHBox");
        Animation.easeOut(root, signUpHBox, Duration.seconds(0.8), Animation.Direction.LEFT);
        Animation.easeIn(root, signInHBox, Duration.seconds(0.8), Animation.Direction.LEFT);
        resetSignUpView();
    }

    private void resetSignUpView() {
        firstNameTextField.setText(null);
        lastNameTextField.setText(null);
        emailTextField.setText(null);
        passwordTextField.setText(null);
        confirmPasswordTextField.setText(null);
        chosenImage = null;
        userImage.setOpacity(0);
        imageButton.setOpacity(1);
    }

}
