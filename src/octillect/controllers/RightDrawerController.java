package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RightDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer rightDrawer;
    @FXML public VBox userSettings;
    @FXML public VBox projectSettings;
    @FXML public VBox taskSettings;
    @FXML public VBox gitHubRepository;

    //Nested Controllers
    @FXML public UserSettingsController userSettingsController;
    @FXML public ProjectSettingsController projectSettingsController;
    @FXML public TaskSettingsController taskSettingsController;
    @FXML public GitHubRepositoryController gitHubRepositoryController;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {
    }

    public void show(Pane pane) {
        userSettings.setVisible(false);
        projectSettings.setVisible(false);
        taskSettings.setVisible(false);
        gitHubRepository.setVisible(false);
        pane.setVisible(true);
    }
    
}