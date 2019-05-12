package octillect.controllers.dialogs;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.LeftDrawerController;
import octillect.controls.OButton;
import octillect.database.repositories.*;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Tag;
import octillect.models.Task;
import octillect.models.TaskBase;

public class NewBoardDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newBoardDialog;
    @FXML public JFXTextField newBoardNameTextField;
    @FXML public JFXTextArea newBoardDescriptionTextArea;
    @FXML public OButton addBoardButton;

    // Validators
    private RequiredFieldValidator requiredFieldValidator;

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

    @Override
    public void init() {
        requiredFieldValidator = new RequiredFieldValidator("Required field.");
        newBoardNameTextField.getValidators().add(requiredFieldValidator);
        newBoardNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newBoardNameTextField.validate();
            }
        });
    }

    @FXML
    public void handleAddBoardButtonAction(ActionEvent actionEvent) {
        newBoardNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {

            Board newBoard = new Board.TemplateBoard(newBoardNameTextField.getText(),
                    newBoardDescriptionTextArea.getText(), applicationController.user);

            // Accessing the database.
            UserRepository.getInstance().addBoardId(applicationController.user.getId(), newBoard.getId());
            BoardRepository.getInstance().add(newBoard);

            for (TaskBase column : newBoard.getChildren()){
                ColumnRepository.getInstance().add((Column) column);
                for (TaskBase task : column.getChildren()){
                    TaskRepository.getInstance().add((Task) task);
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
