package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

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

    public void HandleLogOutButton(MouseEvent mouseEvent) {
    }

}
