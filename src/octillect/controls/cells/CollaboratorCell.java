package octillect.controls.cells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.controllers.util.Injectable;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.TaskRepository;
import octillect.database.repositories.UserRepository;
import octillect.models.Column;
import octillect.models.Collaborator;
import octillect.models.Task;

import org.kordamp.ikonli.javafx.FontIcon;

public class CollaboratorCell extends ListCell<Collaborator> implements Injectable<ApplicationController> {

    // Local Variables
    private Mode mode;

    // FXML Fields
    @FXML private BorderPane rootBorderPane;
    @FXML private Circle collaboratorImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private FontIcon deleteCollaboratorIcon;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private TaskSettingsController taskSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        taskSettingsController     = applicationController.rightDrawerController.taskSettingsController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("CollaboratorCell cannot be initialized");
    }

    public CollaboratorCell(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void updateItem(Collaborator collaboratorItem, boolean empty) {

        super.updateItem(collaboratorItem, empty);

        if (empty || collaboratorItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/CollaboratorCellView.fxml"));
            fxmlLoader.setController(this);
            rootBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        collaboratorImage.setFill(new ImagePattern(collaboratorItem.getImage()));
        usernameLabel.setText(collaboratorItem.getName());
        emailLabel.setText(collaboratorItem.getEmail());

        if (mode == Mode.BOARD) { /* TODO : Remove this condition after handling roles bug */
            roleLabel.setText(collaboratorItem.getRole().toString());
        } else {
            roleLabel.setOpacity(0);
        }

        if (mode == Mode.VIEW_ONLY) {
            setGraphic(rootBorderPane.getLeft());
            return;
        } else if (mode == Mode.BOARD) {

            if (boardController.currentBoard.getUserRole(applicationController.user.getId())
                    .equals(Collaborator.Role.viewer)) {
                deleteCollaboratorIcon.setDisable(true);
                deleteCollaboratorIcon.setOpacity(0);
            }

            deleteCollaboratorIcon.setOnMouseClicked(event -> {
                /* TODO: Add Confirmation Here. */
                BoardRepository.getInstance().deleteCollaborator(boardController.currentBoard, getItem());
                UserRepository.getInstance().deleteBoardId(getItem().getId(), boardController.currentBoard.getId());

                if (boardController.currentBoard.getCollaborators().size() == 1) {
                    // Delete the whole board in case no collaborators left
                    BoardRepository.getInstance().delete(boardController.currentBoard);
                }

                if (getItem().getId().equals(applicationController.user.getId())) {
                    applicationController.user.getBoards().remove(boardController.currentBoard);
                    boardController.loadFirstBoard();
                } else {
                    BoardRepository.getInstance().deleteCollaborator(boardController.currentBoard, getItem());

                    for (Column column : boardController.currentBoard.<Column>getChildren()) {
                        for (Task task : column.<Task>getChildren()) {
                            for (Collaborator collaborator : task.getAssignees()) {
                                if (collaborator.getId().equals(getItem().getId())) {
                                    task.getAssignees().remove(collaborator);
                                    break;
                                }
                            }
                        }
                    }

                    boardController.currentBoard.getCollaborators().remove(getItem());
                    boardController.boardListView.refresh();
                }
            });

        } else if (mode == Mode.TASK) {
            deleteCollaboratorIcon.setOnMouseClicked(event -> {
                TaskRepository.getInstance().deleteAssigneeId(taskSettingsController.currentTask.getId(), collaboratorItem.getId());
                taskSettingsController.currentTask.getAssignees().remove(collaboratorItem);
                taskSettingsController.loadAssignees();
                boardController.boardListView.refresh();
            });
        }


        setGraphic(rootBorderPane);
    }

}
