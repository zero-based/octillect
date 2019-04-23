package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

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

    private HamburgerBackArrowBasicTransition hamburgerTransition;
    private JFXDrawersStack drawersStack;
    private JFXDrawer leftDrawer;
    private JFXDrawer rightDrawer;

    @FXML
    public void initialize() {
        hamburgerTransition = new HamburgerBackArrowBasicTransition(hamburgerButton);
        hamburgerTransition.setRate(-1);
    }

    public void handleHamburgerButtonAction(MouseEvent mouseEvent) {
        hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
        hamburgerTransition.play();
        drawersStack.toggle(leftDrawer);
    }

    public void handleNotificationIconAction(MouseEvent mouseEvent) {
        drawersStack.toggle(rightDrawer);
    }

    public void handleSettingsIconAction(MouseEvent mouseEvent) {
        drawersStack.toggle(rightDrawer);
    }

    public void handleUserIconAction(MouseEvent mouseEvent) {
        drawersStack.toggle(rightDrawer);
    }

    public void setDrawersStack(JFXDrawersStack drawersStack) {
        this.drawersStack = drawersStack;
    }

    public void setLeftDrawer(JFXDrawer leftDrawer) {
        this.leftDrawer = leftDrawer;
    }

    public void setRightDrawer(JFXDrawer rightDrawer) {
        this.rightDrawer = rightDrawer;
    }

}
