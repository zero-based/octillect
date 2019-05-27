package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.application.LeftDrawerController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.OButton;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.*;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Tag;
import octillect.models.Task;

public class NewBoardDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newBoardDialog;
    @FXML public JFXTextField newBoardNameTextField;
    @FXML public JFXTextArea newBoardDescriptionTextArea;
    @FXML public OButton addBoardButton;

    // Validators
    private RequiredValidator requiredValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private LeftDrawerController leftDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        leftDrawerController       = applicationController.leftDrawerController;
    }

    @PostLoad
    public void initValidators() {
        requiredValidator = new RequiredValidator();
        ValidationManager.addValidator(true, requiredValidator, newBoardNameTextField);
    }

    @FXML
    public void handleAddBoardButtonAction(ActionEvent actionEvent) {

        newBoardNameTextField.validate();

        if (!requiredValidator.getHasErrors()) {

            Board newBoard = new Board.TemplateBoard(newBoardNameTextField.getText(),
                    newBoardDescriptionTextArea.getText(), applicationController.user);

            // Accessing the database.
            UserRepository.getInstance().addBoardId(applicationController.user.getId(), newBoard.getId());
            BoardRepository.getInstance().add(newBoard);

            for (Column column : newBoard.<Column>getChildren()){
                ColumnRepository.getInstance().add(column);
                for (Task task : column.<Task>getChildren()){
                    TaskRepository.getInstance().add(task);
                }
            }

            for (Tag tag : newBoard.getTags()){
                TagRepository.getInstance().add(tag);
            }

            boardController.loadBoard(newBoard);
            leftDrawerController.userBoardsListView.getItems().add(newBoard);
            leftDrawerController.userBoardsListView.getSelectionModel().selectLast();
            newBoardDialog.close();
        }
    }

    @FXML
    public void handleNewBoardDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newBoardNameTextField.resetValidation();
        newBoardNameTextField.setText(null);
        newBoardDescriptionTextArea.setText(null);
    }
}
