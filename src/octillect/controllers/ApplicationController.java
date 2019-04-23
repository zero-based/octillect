package octillect.controllers;

import com.jfoenix.controls.JFXDrawersStack;

import javafx.fxml.FXML;

public class ApplicationController {

    @FXML private JFXDrawersStack drawersStack;
    @FXML private TitleBarController titleBarController;
    @FXML private LeftDrawerController leftDrawerController;
    @FXML private RightDrawerController rightDrawerController;
    @FXML private ProjectController projectController;

    @FXML
    public void initialize() {
        leftDrawerController.setProjectController(projectController);
        titleBarController.setDrawersStack(drawersStack);
        titleBarController.setLeftDrawer(leftDrawerController.getLeftDrawer());
        titleBarController.setRightDrawer(rightDrawerController.getRightDrawer());
    }

}