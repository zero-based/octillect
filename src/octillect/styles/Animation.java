package octillect.styles;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animation {

    // Rotate Transition
    public static void rotate(Node node) {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(node);
        rotateTransition.setDuration(Duration.seconds(8));
        rotateTransition.setFromAngle(0);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

    }

}
