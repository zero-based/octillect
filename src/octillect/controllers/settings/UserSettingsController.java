package octillect.controllers.settings;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;

public class UserSettingsController implements Injectable<ApplicationController> {

    //Fxml fields
    @FXML public OButton saveButton;
    @FXML public TitledPane logOutTitlePane;
    @FXML public JFXTextField changeNameText;
    @FXML public JFXTextField changeEmailText;
    @FXML public JFXTextField newPasswordText;
    @FXML public JFXTextField oldPasswordText;
    @FXML public JFXTextField confirmPasswordText;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {
    }

    @FXML
    public void HandleLogOutButton(MouseEvent mouseEvent) {
    }

}
