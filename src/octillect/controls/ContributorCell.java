package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.LeftDrawerController;
import octillect.controllers.BoardController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.TaskRepository;
import octillect.database.repositories.UserRepository;
import octillect.models.Contributor;
import octillect.models.Task;
import octillect.models.TaskBase;

import org.kordamp.ikonli.javafx.FontIcon;

public class ContributorCell extends ListCell<Contributor> implements Injectable<ApplicationController> {

    // Local Variables
    private Mode mode;

    // FXML Fields
    @FXML private BorderPane contributorCellBorderPane;
    @FXML private Circle contributorImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private FontIcon deleteContributorIcon;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private LeftDrawerController leftDrawerController;
    private TaskSettingsController taskSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        leftDrawerController       = applicationController.leftDrawerController;
        taskSettingsController     = applicationController.rightDrawerController.taskSettingsController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("ContributorCell cannot be initialized");
    }

    public ContributorCell(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void updateItem(Contributor contributorItem, boolean empty) {

        super.updateItem(contributorItem, empty);

        if (empty || contributorItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/ContributorCellView.fxml"));
            fxmlLoader.setController(this);
            contributorCellBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        contributorImage.setFill(new ImagePattern(contributorItem.getImage()));
        usernameLabel.setText(contributorItem.getName());
        emailLabel.setText(contributorItem.getEmail());

        if (mode != Mode.VIEW_ONLY) { /* TODO : Remove this condition after handling roles bug */
            roleLabel.setText(contributorItem.getRole().toString());
        }

        if (mode == Mode.VIEW_ONLY) {
            roleLabel.setOpacity(0);
            setGraphic(contributorCellBorderPane.getLeft());
            return;
        } else if (mode == Mode.BOARD) {

            if (boardController.currentBoard.getUserRole(applicationController.user.getId())
                    .equals(Contributor.Role.viewer)) {
                deleteContributorIcon.setDisable(true);
                deleteContributorIcon.setOpacity(0);
            }

            deleteContributorIcon.setOnMouseClicked(event -> {
                /* TODO: Add Confirmation Here. */
                BoardRepository.getInstance().deleteContributor(boardController.currentBoard, getItem());
                UserRepository.getInstance().deleteBoardId(getItem().getId(), boardController.currentBoard.getId());

                if (boardController.currentBoard.getContributors().size() == 1) {
                    // Delete whole board in case no contributors left
                    BoardRepository.getInstance().delete(boardController.currentBoard);
                }

                if (getItem().getId().equals(applicationController.user.getId())) {
                    applicationController.user.getBoards().remove(boardController.currentBoard);
                    boardController.init();
                } else {
                    BoardRepository.getInstance().deleteContributor(boardController.currentBoard, getItem());

                    for (TaskBase column : boardController.currentBoard.getChildren()) {
                        for (TaskBase task : column.getChildren()) {
                            for (Contributor contributor : ((Task) task).getAssignees()) {
                                if (contributor.getId().equals(getItem().getId())) {
                                    ((Task) task).getAssignees().remove(contributor);
                                    break;
                                }
                            }
                        }
                    }

                    boardController.currentBoard.getContributors().remove(getItem());
                    boardController.boardListView.refresh();
                }
            });

        } else if (mode == Mode.TASK) {
            deleteContributorIcon.setOnMouseClicked(event -> {
                TaskRepository.getInstance().deleteAssigneeId(taskSettingsController.currentTask.getId(), contributorItem.getId());

                int columnIndex = boardController.currentBoard.getChildren().indexOf(taskSettingsController.parentColumn);
                int taskIndex = boardController.currentBoard.getChildren().get(columnIndex)
                        .getChildren().indexOf(taskSettingsController.currentTask);

                ((Task) boardController.currentBoard.getChildren().get(columnIndex)
                        .getChildren().get(taskIndex)).getAssignees().remove(contributorItem);
                boardController.boardListView.refresh();
            });
        }


        setGraphic(contributorCellBorderPane);
    }

}
