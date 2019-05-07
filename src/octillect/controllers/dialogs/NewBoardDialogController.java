package octillect.controllers.dialogs;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.LeftDrawerController;
import octillect.controls.OButton;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.TaskRepository;
import octillect.database.repositories.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Board;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.BoardBuilder;
import octillect.models.builders.TaskBuilder;

public class NewBoardDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newBoardDialog;
    @FXML public JFXTextField newBoardNameTextField;
    @FXML public JFXTextArea newBoardDescriptionTextArea;
    @FXML public OButton addBoardButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private LeftDrawerController leftDrawerController;

    // Empty field validation
    RequiredFieldValidator requiredFieldValidator;

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

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController = applicationController.boardController;
        leftDrawerController       = applicationController.leftDrawerController;
    }

    @FXML
    public void handleAddBoardButtonAction(ActionEvent actionEvent) {
        newBoardNameTextField.validate();
        if (!requiredFieldValidator.getHasErrors()) {
            
            // Add board.
            Board newBoard = new BoardBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime(newBoardNameTextField.getText()))
                    .withName(newBoardNameTextField.getText())
                    .withDescription(newBoardDescriptionTextArea.getText())
                    .withContributors(FXCollections.observableArrayList(new Pair<>(applicationController.user, Board.Role.owner)))
                    .build();

            // Add column.
            Column untitledColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Untitled Column" + applicationController.user.getId()))
                    .withName("Untitled Column")
                    .build();

            newBoard.setColumns(FXCollections.observableArrayList(untitledColumn));

            // Add task.
            Task untitledTask = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Untitled Task" + applicationController.user.getId()))
                    .withName("Untitled Task")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(applicationController.user)
                    .build();

            untitledColumn.setTasks(FXCollections.observableArrayList(untitledTask));

            // Accessing the database.
            UserRepository.getInstance().addBoardId(applicationController.user.getId(), newBoard.getId());
            BoardRepository.getInstance().add(newBoard);
            ColumnRepository.getInstance().add(untitledColumn);
            TaskRepository.getInstance().add(untitledTask);

            boardController.loadBoard(newBoard);
            leftDrawerController.userBoardsListView.getItems().add(newBoard);
            leftDrawerController.userBoardsListView.getSelectionModel().selectLast();
            newBoardDialog.close();
        }
    }

    @FXML
    public void handleNewBoardDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newBoardNameTextField.resetValidation();
        newBoardNameTextField.setText("");
        newBoardDescriptionTextArea.setText("");
    }
}
