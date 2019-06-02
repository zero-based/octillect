package octillect.controllers.application;

import com.jfoenix.controls.JFXDrawersStack;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import octillect.Main;
import octillect.controllers.dialogs.*;
import octillect.controllers.util.Injectable;
import octillect.models.User;

public class ApplicationController {

    // Local Fields
    public User user = Main.signedUser;

    // FXML Fields
    @FXML public StackPane rootStackPane;
    @FXML public JFXDrawersStack drawersStack;

    // Nested Controllers
    @FXML public TitleBarController titleBarController;
    @FXML public LeftDrawerController leftDrawerController;
    @FXML public RightDrawerController rightDrawerController;
    @FXML public BoardController boardController;

    // Dialogs' Controllers
    public NewBoardDialogController newBoardDialogController;
    public NewTaskDialogController newTaskDialogController;
    public NewColumnDialogController newColumnDialogController;
    public RepositoryNameDialogController repositoryNameDialogController;
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

            final String REPOSITORY_NAME_DIALOG_VIEW = "/octillect/views/dialogs/RepositoryNameDialogView.fxml";
            fxmlLoader = new FXMLLoader(getClass().getResource(REPOSITORY_NAME_DIALOG_VIEW));
            fxmlLoader.load();
            repositoryNameDialogController = fxmlLoader.getController();

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

        ArrayList<Injectable<ApplicationController>> descendants = new ArrayList<>();

        // Add dialogs' controllers to descendants
        descendants.add(newBoardDialogController);
        descendants.add(newTaskDialogController);
        descendants.add(newColumnDialogController);
        descendants.add(repositoryNameDialogController);
        descendants.add(editColumnDialogController);

        // Add nested controllers to descendants
        descendants.add(titleBarController);
        descendants.add(leftDrawerController);
        descendants.add(rightDrawerController);
        descendants.add(boardController);

        descendants.forEach(controller -> {
            controller.inject(this);
            controller.init();
        });

    }

}