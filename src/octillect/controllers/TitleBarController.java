package octillect.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.controllers.settings.UserSettingsController;

import org.kordamp.ikonli.javafx.FontIcon;

public class TitleBarController implements Injectable<ApplicationController> {

    // Local Fields
    public HamburgerBackArrowBasicTransition hamburgerTransition;

    // FXML Fields
    @FXML public JFXHamburger hamburgerButton;
    @FXML public Label boardNameLabel;
    @FXML public FontIcon notificationIcon;
    @FXML public FontIcon settingsIcon;
    @FXML public Circle userImageCircle;

    // Injected Controllers
    private ApplicationController applicationController;
    private LeftDrawerController leftDrawerController;
    private RightDrawerController rightDrawerController;
    private UserSettingsController userSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        leftDrawerController       = applicationController.leftDrawerController;
        rightDrawerController      = applicationController.rightDrawerController;
        userSettingsController     = rightDrawerController.userSettingsController;
    }

    @Override
    public void init() {
        loadUserImage(applicationController.user.getImage());
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
        rightDrawerController.show(rightDrawerController.boardSettings);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    @FXML
    public void handleUserIconMouseClicked(MouseEvent mouseEvent) {
        userSettingsController.LoadUserSettings();
        rightDrawerController.show(rightDrawerController.userSettings);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void loadUserImage(Image image) {
        userImageCircle.setFill(new ImagePattern(image));
    }

}