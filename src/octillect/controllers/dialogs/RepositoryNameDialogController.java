package octillect.controllers.dialogs;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controls.OButton;
import octillect.database.repositories.BoardRepository;

public class RepositoryNameDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog repositoryNameDialog;
    @FXML public JFXTextField repositoryNameTextField;
    @FXML public OButton addRepositoryButton;

    // Validators
    private RegexValidator nameRegexValidator;

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

    @Override
    public void init() {

        nameRegexValidator = new RegexValidator("Invalid repository name.");
        nameRegexValidator.setRegexPattern("^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}" +
                "[/](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){1,38}$");
        repositoryNameTextField.getValidators().add(nameRegexValidator);

        repositoryNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                repositoryNameTextField.validate();
            }
        });

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
