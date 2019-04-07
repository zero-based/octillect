package octillect.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import octillect.Main;
import octillect.controls.OButton;

public class SignInController {

    @FXML public HBox signInHBox;
    @FXML public OButton signInButton;
    @FXML public OButton createAnAccountButton;

    @FXML
    public void handleSignInButtonAction(ActionEvent actionEvent) {
        /* SIGN IN CODE HERE */
        try {
            Main.runApplication();
        } catch( Exception e ) {
            System.out.println("Couldn't start Application.");
        }
    }

    @FXML
    public void handleCreateAnAccountAction(ActionEvent actionEvent) {
    }

}
