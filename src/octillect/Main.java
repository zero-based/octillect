package octillect;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import octillect.database.accessors.UserRepository;
import octillect.database.firebase.Connection;
import octillect.models.User;
import octillect.styles.Fonts;

public class Main extends Application {

    public static Image O_ICON = new Image("/octillect/resources/o-icon.png");
    public static Stage signingStage;
    public static Stage applicationStage;
    public static User signedUser;
    public static File octillectFile = new File(FileSystemView.getFileSystemView().getHomeDirectory().getParent() + "/.octillet");

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Initialize Firebase connection
        Connection.initializeFirebase();

        // Load Application Fonts
        Fonts.load();

        if (octillectFile.exists()) {
            signedUser = UserRepository.getRememberedUser();
            if (signedUser != null)
                runApplication(signedUser);
        } else {
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
    }

    public static void runApplication(User user) {

        signedUser = user;
        // Create Application Stage and Scene
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("views/ApplicationView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        /* Close Signing stage and Check if it's not null in case of auto-signing-in.*/
        if (signingStage != null)
            signingStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
