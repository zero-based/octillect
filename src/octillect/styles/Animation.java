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

    public static void easeOut(Pane parent, Pane child) {
        double startValue = 0;
        double endValue = parent.getWidth();
        parent.translateXProperty().set(startValue);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(child.translateXProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(650), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> parent.getChildren().remove(child));
        timeline.play();
    }

}
