package octillect.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class TitleBarController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXHamburger hamburgerButton;
    @FXML public Label projectNameLabel;
    @FXML public MaterialDesignIconView notificationIcon;
    @FXML public MaterialDesignIconView settingsIcon;
    @FXML public MaterialDesignIconView userIcon;

    private HamburgerBackArrowBasicTransition hamburgerTransition;

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

    public void handleHamburgerButtonAction(MouseEvent mouseEvent) {
        hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
        hamburgerTransition.play();
        applicationController.drawersStack.toggle(leftDrawerController.leftDrawer);
    }

    public void handleNotificationIconAction(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void handleSettingsIconAction(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void handleUserIconAction(MouseEvent mouseEvent) {
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

}