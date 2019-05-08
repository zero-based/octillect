package octillect.controllers.dialogs;

import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controls.OButton;
import octillect.database.firebase.FirestoreAPI;

public class NewRepositoryDialogController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public JFXDialog newRepoDialog;
    @FXML public JFXTextField usernameAndRepositoryNameTextField;
    @FXML public OButton addRepositoryButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private RightDrawerController rightDrawerController;
    private BoardController boardController;
    private GitHubRepositoryController gitHubRepositoryController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

    @Override
    public void init(){
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        usernameAndRepositoryNameTextField.getValidators().add(requiredFieldValidator);
        usernameAndRepositoryNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                usernameAndRepositoryNameTextField.validate();
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
        usernameAndRepositoryNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {
            FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardController.currentBoard.getId(), "repositoryName", usernameAndRepositoryNameTextField.getText());
            int index = applicationController.user.getBoards().indexOf(boardController.currentBoard);
            applicationController.user.getBoards().get(index).setRepositoryName(usernameAndRepositoryNameTextField.getText());
            boardController.currentBoard.setRepositoryName(usernameAndRepositoryNameTextField.getText());
            gitHubRepositoryController.loadGithubRepository();
            rightDrawerController.show(rightDrawerController.gitHubRepository);
            applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
            newRepoDialog.close();
        }
    }

    @FXML
    public void handleNewRepoDialogClosed(JFXDialogEvent jfxDialogEvent) {
        usernameAndRepositoryNameTextField.resetValidation();
        usernameAndRepositoryNameTextField.setText("");
        usernameAndRepositoryNameTextField.setText("");
    }
}
