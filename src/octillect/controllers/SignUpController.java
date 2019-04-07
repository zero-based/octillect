package octillect.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import octillect.controls.OButton;

public class SignUpController {

    @FXML public HBox signUpHBox;
    @FXML public OButton signUpButton;
    @FXML public OButton signUpWithGitHubButton;
    @FXML public OButton backButton;

    @FXML
    public void handleSignUpButtonAction(ActionEvent actionEvent) {
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
