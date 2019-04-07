package octillect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    public static Image O_ICON = new Image("/octillect/resources/o-icon.png");
    public static Stage signingStage;
    public static Stage applicationStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Create Signing Stage and Sign in Scene
        Parent root = FXMLLoader.load(getClass().getResource("views/SignInView.fxml"));
        signingStage = primaryStage;
        signingStage.setTitle("Octillect | Sign in/up");
        signingStage.setScene(new Scene(root));
        signingStage.show();

        // Set stage Icon
        signingStage.getIcons().add(O_ICON);

        // Center stage according to screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        signingStage.setX((primScreenBounds.getWidth() - signingStage.getWidth()) / 2);
        signingStage.setY((primScreenBounds.getHeight() - signingStage.getHeight()) / 2);

        // Make Stage not resizable
        signingStage.setResizable(false);

    }

    public static void runApplication() throws Exception {

        // Create Application Stage and Scene
        Parent root = FXMLLoader.load(Main.class.getResource("views/ApplicationView.fxml"));
        applicationStage = new Stage();
        applicationStage.setTitle("Octillect");
        applicationStage.setScene(new Scene(root));
        applicationStage.show();

        // Set stage Icon
        applicationStage.getIcons().add(O_ICON);

        // Center stage according to screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        applicationStage.setX((primScreenBounds.getWidth() - applicationStage.getWidth()) / 2);
        applicationStage.setY((primScreenBounds.getHeight() - applicationStage.getHeight()) / 2);

        // Close Signing Stage
        signingStage.close();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
