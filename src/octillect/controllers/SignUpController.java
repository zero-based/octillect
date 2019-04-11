package octillect.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class SignUpController {

    @FXML private HBox signUpHBox;
    @FXML private OButton signUpButton;
    @FXML private OButton signUpWithGitHubButton;
    @FXML private OButton backButton;
    @FXML private OButton imageButton;
    @FXML private Circle userImage;

    private String chosenImagePath;

    @FXML
    public void handleSignUpButtonAction(ActionEvent actionEvent) {
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
}
