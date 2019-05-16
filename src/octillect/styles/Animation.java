package octillect.styles;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Animation {

    public enum Direction {
        LEFT,
        RIGHT
    }

    // Rotate Transition
    public static void rotate(Node node, Duration duration) {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(node);
        rotateTransition.setDuration(duration);
        rotateTransition.setFromAngle(0);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
    }

    public static void easeIn(Pane parent, Pane child, Duration duration, Direction direction) {
        int directionValue = direction == Direction.RIGHT ? 1 : -1;
        double startValue = parent.getWidth() * directionValue;
        double endValue = 0;
        child.translateXProperty().set(startValue);
        parent.getChildren().add(child);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(child.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void easeOut(Pane parent, Pane child, Duration duration, Direction direction) {
        int directionValue = direction == Direction.RIGHT ? 1 : -1;
        double startValue = 0;
        double endValue = parent.getWidth() * directionValue;
        parent.translateXProperty().set(startValue);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(child.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> parent.getChildren().remove(child));
        timeline.play();
    }

}
