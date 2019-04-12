package octillect.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import octillect.Main;
import octillect.controls.OButton;
import octillect.models.User;

public class SignInController {

    @FXML public HBox signInHBox;
    @FXML public OButton signInButton;
    @FXML public OButton createAnAccountButton;

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) {
        /* Must be initialized with user we got from database instead of null */
        User user = null;

        /* SIGN IN CODE HERE */
        Main.runApplication(user);
    }

    @FXML
    public void handleCreateAnAccountAction(ActionEvent actionEvent) throws Exception{
        // Initialize values
        Parent signUpRoot = FXMLLoader.load(getClass().getResource("/octillect/views/SignUpView.fxml"));
        StackPane parentStackPane = (StackPane) signInHBox.getParent();
        double startValue = parentStackPane.getWidth();
        double endValue = 0;
        signUpRoot.translateXProperty().set(startValue);
        parentStackPane.getChildren().add(signUpRoot);

        // Animation
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(signUpRoot.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(650), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
