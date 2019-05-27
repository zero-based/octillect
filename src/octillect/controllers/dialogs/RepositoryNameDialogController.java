package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.RightDrawerController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.OButton;
import octillect.controls.validators.RepositoryValidtor;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.BoardRepository;

public class RepositoryNameDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog repositoryNameDialog;
    @FXML public JFXTextField repositoryNameTextField;
    @FXML public OButton addRepositoryButton;

    // Validators
    private RepositoryValidtor nameRegexValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private RightDrawerController rightDrawerController;
    private BoardController boardController;
    private GitHubRepositoryController gitHubRepositoryController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        rightDrawerController      = applicationController.rightDrawerController;
        gitHubRepositoryController = rightDrawerController.gitHubRepositoryController;
    }

    @PostLoad
    public void initValidators() {
        nameRegexValidator = new RepositoryValidtor();
        ValidationManager.addValidator(true, nameRegexValidator, repositoryNameTextField);
    }

    @FXML
    public void handleAddRepositoryButtonAction(ActionEvent actionEvent) {

        repositoryNameTextField.validate();

        if (!nameRegexValidator.getHasErrors()) {

            BoardRepository.getInstance().updateRepositoryName(boardController.currentBoard.getId(), repositoryNameTextField.getText());
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
        repositoryNameTextField.setText(null);
        repositoryNameTextField.setText(null);
    }
}
