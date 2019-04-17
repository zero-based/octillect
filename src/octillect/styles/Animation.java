package octillect.styles;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
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

    // Ease in transition
    public static void easeIn(Pane parent, Pane child) {
        double startValue = parent.getWidth();
        double endValue = 0;
        child.translateXProperty().set(startValue);
        parent.getChildren().add(child);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(child.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(650), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
