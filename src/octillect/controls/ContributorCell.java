package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.LeftDrawerController;
import octillect.controllers.BoardController;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.UserRepository;
import octillect.models.Board;
import octillect.models.User;

import org.kordamp.ikonli.javafx.FontIcon;

public class ContributorCell extends ListCell<Pair<User, Board.Role>> implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML private BorderPane contributorCellBorderPane;
    @FXML private Circle contributorImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private FontIcon deleteContributorIcon;

    //Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private LeftDrawerController leftDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController = applicationController.boardController;
        leftDrawerController       = applicationController.leftDrawerController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("ContributorCell cannot be initialized");
    }

    @Override
    public void updateItem(Pair<User, Board.Role> contributorItem, boolean empty) {

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

        if (contributorItem.getKey().getImage() != null) {
            contributorImage.setFill(new ImagePattern(contributorItem.getKey().getImage()));
        }
        usernameLabel.setText(contributorItem.getKey().getName());
        emailLabel.setText(contributorItem.getKey().getEmail());
        roleLabel.setText(contributorItem.getValue().toString());

        if (boardController.currentBoard.getUserRole(applicationController.user.getId())
                .equals(Board.Role.viewer)) {
            deleteContributorIcon.setDisable(true);
            deleteContributorIcon.setOpacity(0);
        }

        deleteContributorIcon.setOnMouseClicked(event -> {
            /* TODO: Add Confirmation Here. */
            BoardRepository.getInstance().deleteContributor(boardController.currentBoard.getId(),
                    getItem().getKey().getEmail(), getItem().getValue());
            UserRepository.getInstance().deleteBoardId(getItem().getKey().getId(), boardController.currentBoard.getId());

            if (boardController.currentBoard.getContributors().size() == 1) {
                // Delete whole board in case no contributors left
                BoardRepository.getInstance().delete(boardController.currentBoard);
            }

            if (getItem().getKey().getId().equals(applicationController.user.getId())) {
                applicationController.user.getBoards().remove(boardController.currentBoard);
                boardController.init();
                leftDrawerController.init();
            } else {
                int index = applicationController.user.getBoards().indexOf(boardController.currentBoard);
                applicationController.user.getBoards().get(index).getContributors().remove(getItem());
                boardController.currentBoard.getContributors().remove(getItem());
                getListView().getItems().remove(getItem());
            }

        });

        setGraphic(contributorCellBorderPane);
    }

}
