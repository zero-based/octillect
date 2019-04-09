package octillect.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import octillect.Main;
import octillect.controls.OButton;

import java.io.File;

public class SignUpController {

    @FXML private HBox signUpHBox;
    @FXML private OButton signUpButton;
    @FXML private OButton signUpWithGitHubButton;
    @FXML private OButton backButton;
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

        if (file != null)
            chosenImagePath = file.getPath();
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
