package octillect.controllers;

import com.jfoenix.controls.JFXHamburger;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class TitleBarController {

    @FXML private JFXHamburger hamburgerButton;
    @FXML private Label projectNameLabel;
    @FXML private MaterialDesignIconView notificationIcon;
    @FXML private MaterialDesignIconView settingsIcon;
    @FXML private MaterialDesignIconView userIcon;

    public void handleHamburgerButtonAction(MouseEvent mouseEvent) {
    }

    public void handleNotificationIconAction(MouseEvent mouseEvent) {
    }

    public void handleSettingsIconAction(MouseEvent mouseEvent) {
    }

    public void handleUserIconAction(MouseEvent mouseEvent) {
    }
}
