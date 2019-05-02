package octillect.controllers;

import com.jfoenix.controls.JFXDrawersStack;

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

    // Nested Controllers
    @FXML public TitleBarController titleBarController;
    @FXML public LeftDrawerController leftDrawerController;
    @FXML public RightDrawerController rightDrawerController;
    @FXML public ProjectController projectController;

    // Dialogs' Controllers
    private final String NEW_PROJECT_DIALOG_VIEW = "/octillect/views/NewProjectDialogView.fxml";
    public NewProjectDialogController newProjectDialogController;

    private final String NEW_TASK_DIALOG_VIEW = "/octillect/views/NewTaskDialogView.fxml";
    public NewTaskDialogController newTaskDialogController;

    private final String NEW_COLUMN_DIALOG_VIEW = "/octillect/views/NewColumnDialogView.fxml";
    public NewColumnDialogController newColumnDialogController;

    public ApplicationController() {
        // Load Dialogs' Controllers
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(NEW_PROJECT_DIALOG_VIEW));
            fxmlLoader.load();
            newProjectDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(NEW_TASK_DIALOG_VIEW));
            fxmlLoader.load();
            newTaskDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(NEW_COLUMN_DIALOG_VIEW));
            fxmlLoader.load();
            newColumnDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Injecting the main instance of ApplicationController
        // into the instances of other sub-controllers
        titleBarController        .inject(this);
        leftDrawerController      .inject(this);
        rightDrawerController     .inject(this);
        projectController         .inject(this);
        newProjectDialogController.inject(this);
        newTaskDialogController   .inject(this);
        newColumnDialogController .inject(this);

        leftDrawerController.userProjects.setAll(user.getProjects());
        leftDrawerController.userProjectsListView.getSelectionModel().selectFirst();
        projectController.loadProject(user.getProjects().get(0));

    }

}