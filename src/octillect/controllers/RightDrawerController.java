package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;

import javafx.fxml.FXML;

public class RightDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer rightDrawer;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

}