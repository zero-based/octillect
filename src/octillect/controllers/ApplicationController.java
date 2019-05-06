package octillect.controllers;

import com.jfoenix.controls.JFXDrawersStack;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import octillect.Main;
import octillect.models.User;

public class ApplicationController {

    // Get the current user
    public User user = Main.signedUser;

    // FXML Fields
    @FXML public StackPane rootStackPane;
    @FXML public JFXDrawersStack drawersStack;

    private ArrayList<Injectable<ApplicationController>> descendants = new ArrayList<>();

    // Nested Controllers
    @FXML public TitleBarController titleBarController;
    @FXML public LeftDrawerController leftDrawerController;
    @FXML public RightDrawerController rightDrawerController;
    @FXML public ProjectController projectController;

    // Dialogs' Controllers
    public NewProjectDialogController newProjectDialogController;
    public NewTaskDialogController newTaskDialogController;
    public NewColumnDialogController newColumnDialogController;

    public ApplicationController() {

        // Load Dialogs' Controllers
        try {
            FXMLLoader fxmlLoader;

            final String NEW_PROJECT_DIALOG_VIEW = "/octillect/views/NewProjectDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_PROJECT_DIALOG_VIEW));
            fxmlLoader.load();
            newProjectDialogController = fxmlLoader.getController();

            final String NEW_TASK_DIALOG_VIEW = "/octillect/views/NewTaskDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_TASK_DIALOG_VIEW));
            fxmlLoader.load();
            newTaskDialogController = fxmlLoader.getController();

            final String NEW_COLUMN_DIALOG_VIEW = "/octillect/views/NewColumnDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_COLUMN_DIALOG_VIEW));
            fxmlLoader.load();
            newColumnDialogController = fxmlLoader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {

        // Add dialogs' controllers to descendants
        descendants.add(newProjectDialogController);
        descendants.add(newTaskDialogController);
        descendants.add(newColumnDialogController);

        // Add nested controllers to descendants
        descendants.add(titleBarController);
        descendants.add(leftDrawerController);
        descendants.add(rightDrawerController);
        descendants.add(projectController);

        // Add sub-nested controllers to descendants
        descendants.add(rightDrawerController.taskSettingsController);
        descendants.add(rightDrawerController.gitHubRepositoryController);
        descendants.add(rightDrawerController.projectSettingsController);
        descendants.add(rightDrawerController.userSettingsController);

        for (Injectable<ApplicationController> controller : descendants) {
            controller.inject(this);
            controller.init();
        }

    }

}