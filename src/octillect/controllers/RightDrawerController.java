package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import octillect.controllers.settings.BoardSettingsController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.controllers.settings.UserSettingsController;

import java.util.ArrayList;

public class RightDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer rightDrawer;
    @FXML public VBox userSettings;
    @FXML public VBox boardSettings;
    @FXML public VBox taskSettings;
    @FXML public StackPane gitHubRepository;

    // Nested Controllers
    @FXML public BoardSettingsController boardSettingsController;
    @FXML public GitHubRepositoryController gitHubRepositoryController;
    @FXML public TaskSettingsController taskSettingsController;
    @FXML public UserSettingsController userSettingsController;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {

        ArrayList<Injectable<ApplicationController>> descendants = new ArrayList<>();

        descendants.add(boardSettingsController);
        descendants.add(gitHubRepositoryController);
        descendants.add(taskSettingsController);
        descendants.add(userSettingsController);

        descendants.forEach(controller -> {
            controller.inject(applicationController);
            controller.init();
        });

    }

    public void show(Node node) {
        userSettings.setVisible(false);
        boardSettings.setVisible(false);
        taskSettings.setVisible(false);
        gitHubRepository.setVisible(false);
        node.setVisible(true);
    }
    
}