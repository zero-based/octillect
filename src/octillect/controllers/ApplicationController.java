package octillect.controllers;

import com.jfoenix.controls.JFXDrawersStack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import octillect.Main;
import octillect.controllers.dialogs.*;
import octillect.models.User;

import java.util.ArrayList;

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
    @FXML public BoardController boardController;

    // Dialogs' Controllers
    public NewBoardDialogController newBoardDialogController;
    public NewTaskDialogController newTaskDialogController;
    public NewColumnDialogController newColumnDialogController;
    public NewRepositoryDialogController newRepositoryDialogController;
    public EditColumnDialogController editColumnDialogController;

    public ApplicationController() {

        // Load Dialogs' Controllers
        try {
            FXMLLoader fxmlLoader;

            final String NEW_BOARD_DIALOG_VIEW = "/octillect/views/dialogs/NewBoardDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_BOARD_DIALOG_VIEW));
            fxmlLoader.load();
            newBoardDialogController = fxmlLoader.getController();

            final String NEW_TASK_DIALOG_VIEW = "/octillect/views/dialogs/NewTaskDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_TASK_DIALOG_VIEW));
            fxmlLoader.load();
            newTaskDialogController = fxmlLoader.getController();

            final String NEW_COLUMN_DIALOG_VIEW = "/octillect/views/dialogs/NewColumnDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_COLUMN_DIALOG_VIEW));
            fxmlLoader.load();
            newColumnDialogController = fxmlLoader.getController();

            final String NEW_REPOSITORY_DIALOG_VIEW = "/octillect/views/dialogs/NewRepositoryDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(NEW_REPOSITORY_DIALOG_VIEW));
            fxmlLoader.load();
            newRepositoryDialogController = fxmlLoader.getController();

            final String EDIT_COLUMN_DIALOG_VIEW = "/octillect/views/dialogs/EditColumnDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(EDIT_COLUMN_DIALOG_VIEW));
            fxmlLoader.load();
            editColumnDialogController = fxmlLoader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {

        // Add dialogs' controllers to descendants
        descendants.add(newBoardDialogController);
        descendants.add(newTaskDialogController);
        descendants.add(newColumnDialogController);
        descendants.add(newRepositoryDialogController);
        descendants.add(editColumnDialogController);

        // Add nested & sub-nested controllers to descendants
        descendants.add(rightDrawerController);
        descendants.add(rightDrawerController.taskSettingsController);
        descendants.add(rightDrawerController.gitHubRepositoryController);
        descendants.add(rightDrawerController.boardSettingsController);
        descendants.add(rightDrawerController.userSettingsController);
        descendants.add(leftDrawerController);
        descendants.add(titleBarController);
        descendants.add(boardController);

        for (Injectable<ApplicationController> controller : descendants) {
            controller.inject(this);
            controller.init();
        }

    }

}