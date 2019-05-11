package octillect.controllers.dialogs;


import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controls.OButton;
import octillect.database.firebase.FirestoreAPI;

public class RepositoryNameDialogController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public JFXDialog repositoryNameDialog;
    @FXML public JFXTextField repositoryNameTextField;
    @FXML public OButton addRepositoryButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private RightDrawerController rightDrawerController;
    private BoardController boardController;
    private GitHubRepositoryController gitHubRepositoryController;

    // Empty field validation
    private RequiredFieldValidator requiredFieldValidator;
    private RegexValidator nameRegexValidator;

    @Override
    public void init(){
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        nameRegexValidator     = new RegexValidator("Invalid repository name.");

        nameRegexValidator.setRegexPattern("^(?=.*?[0-9a-zA-Z])[0-9a-zA-Z]*[/][0-9a-zA-Z]*$");

        repositoryNameTextField.getValidators().add(requiredFieldValidator);
        repositoryNameTextField.getValidators().add(nameRegexValidator);

        repositoryNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                repositoryNameTextField.validate();
            }
        });
    }

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        rightDrawerController      = applicationController.rightDrawerController;
        boardController            = applicationController.boardController;
        gitHubRepositoryController = rightDrawerController.gitHubRepositoryController;
    }

    @FXML
    public void handleAddRepositoryButtonAction(ActionEvent actionEvent) {

        repositoryNameTextField.validate();

        if (!requiredFieldValidator.getHasErrors() && !nameRegexValidator.getHasErrors()) {

            FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardController.currentBoard.getId(), "repositoryName", repositoryNameTextField.getText());

            int index = applicationController.user.getBoards().indexOf(boardController.currentBoard);
            applicationController.user.getBoards().get(index).setRepositoryName(repositoryNameTextField.getText());
            boardController.currentBoard.setRepositoryName(repositoryNameTextField.getText());
            gitHubRepositoryController.loadGitHubRepository();

            if (rightDrawerController.rightDrawer.isClosed()) {
                // Prevent Drawer toggling if the drawer is already open
                rightDrawerController.show(rightDrawerController.gitHubRepository);
                applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
            }

            repositoryNameDialog.close();
        }
    }

    @FXML
    public void handleNewRepoDialogClosed(JFXDialogEvent jfxDialogEvent) {
        repositoryNameTextField.resetValidation();
        repositoryNameTextField.setText("");
        repositoryNameTextField.setText("");
    }
}
