package octillect.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import org.kordamp.ikonli.javafx.FontIcon;

public class TitleBarController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXHamburger hamburgerButton;
    @FXML public Label projectNameLabel;
    @FXML public FontIcon notificationIcon;
    @FXML public FontIcon settingsIcon;
    @FXML public FontIcon userIcon;

    public HamburgerBackArrowBasicTransition hamburgerTransition;

    // Injected Controllers
    private ApplicationController applicationController;
    private LeftDrawerController leftDrawerController;
    private RightDrawerController rightDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        leftDrawerController       = applicationController.leftDrawerController;
        rightDrawerController      = applicationController.rightDrawerController;
    }

    @FXML
    public void initialize() {
        hamburgerTransition = new HamburgerBackArrowBasicTransition(hamburgerButton);
        hamburgerTransition.setRate(-1);
    }

    @FXML
    public void handleHamburgerButtonMouseClicked(MouseEvent mouseEvent) {
        if (leftDrawerController.leftDrawer.isClosed()) {
            hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
            hamburgerTransition.play();
        }
        applicationController.drawersStack.toggle(leftDrawerController.leftDrawer);
    }

    @FXML
    public void handleNotificationIconMouseClicked(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    @FXML
    public void handleSettingsIconMouseClicked(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    @FXML
    public void handleUserIconMouseClicked(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

}