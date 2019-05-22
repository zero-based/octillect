package octillect.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

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
import octillect.controls.validators.CustomValidator;
import octillect.controls.validators.EmailValidator;
import octillect.controls.validators.MatchPasswordValidator;
import octillect.controls.validators.PasswordValidator;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
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
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXPasswordField confirmPasswordField;

    // Validators
    private RequiredValidator requiredValidator;
    private EmailValidator emailValidator;
    private CustomValidator emailUsedValidator;
    private PasswordValidator passwordValidator;
    private MatchPasswordValidator matchPasswordValidator;

    @FXML
    public void initialize() {
        requiredValidator      = new RequiredValidator();
        emailValidator         = new EmailValidator();
        emailUsedValidator     = new CustomValidator("Email in use. Try another.");
        passwordValidator      = new PasswordValidator();
        matchPasswordValidator = new MatchPasswordValidator(passwordField);

        ValidationManager.addValidator(true, requiredValidator, firstNameTextField, lastNameTextField, confirmPasswordField);
        ValidationManager.addValidator(true, emailValidator, emailTextField);
        ValidationManager.addValidator(true, emailUsedValidator, emailTextField);
        ValidationManager.addValidator(true, passwordValidator, passwordField);
        ValidationManager.addValidator(true, matchPasswordValidator, confirmPasswordField);
    }

    @FXML
    public void handleSignUpButtonAction(ActionEvent actionEvent) throws IOException {

        firstNameTextField  .validate();
        lastNameTextField   .validate();
        emailTextField      .validate();
        passwordField       .validate();
        confirmPasswordField.validate();

        if (!requiredValidator.getHasErrors() && !emailValidator.getHasErrors()
            && !passwordValidator.getHasErrors() && !matchPasswordValidator.getHasErrors()) {

            if (UserRepository.getInstance().isUserFound(emailTextField.getText())) {
                emailUsedValidator.showMessage();
            } else {
                User user = new UserBuilder().with($ -> {
                    $.id = FirestoreAPI.getInstance().encrypt(emailTextField.getText());
                    $.name = firstNameTextField.getText() + " " + lastNameTextField.getText();
                    $.email = emailTextField.getText();
                    $.password = FirestoreAPI.getInstance().encrypt(passwordField.getText());

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
        passwordField.setText(null);
        confirmPasswordField.setText(null);
        chosenImage = null;
        userImage.setOpacity(0);
        imageButton.setOpacity(1);
    }

}
