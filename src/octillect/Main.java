package octillect;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import octillect.database.repositories.UserRepository;
import octillect.models.User;
import octillect.styles.Fonts;

public class Main extends Application {

    public static Image O_ICON = new Image("/octillect/resources/o-icon.png");
    public static Stage signingStage;
    public static Stage applicationStage;
    public static User signedUser;
    public static File octillectFile = new File(System.getProperty("user.home") + "/.octillet");

    @Override
    public void start(Stage primaryStage) {

        Fonts.load();
        signedUser = UserRepository.getInstance().getRememberedUser();
        initSigningStage(primaryStage);

        if (signedUser == null) {
            showSigningStage();
        } else {
            initApplicationStage(signedUser);
            showApplicationStage();
        }

    }

    private static void initSigningStage(Stage primaryStage) {

        signingStage = primaryStage;

        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("views/SignInView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        signingStage.setTitle("Octillect | Sign in/up");
        signingStage.getIcons().add(O_ICON);
        signingStage.setScene(new Scene(root, 1024, 768));

    }

    public static void initApplicationStage(User user) {

        signedUser = user;

        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("views/ApplicationView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        applicationStage = new Stage();
        applicationStage.setTitle("Octillect");
        applicationStage.getIcons().add(O_ICON);
        applicationStage.setScene(new Scene(root));

    }

    public static void showSigningStage() {

        signingStage.show();
        double screenWidth  = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        signingStage.setX((screenWidth  - signingStage.getWidth())  / 2);
        signingStage.setY((screenHeight - signingStage.getHeight()) / 2);

        octillectFile.delete();

        if (applicationStage != null) {
            applicationStage.close();
        }

    }

    public static void showApplicationStage() {
        applicationStage.show();
        applicationStage.setMaximized(true);
        signingStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
