package octillect.controllers.application;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.controllers.dialogs.NewBoardDialogController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.models.Board;

import org.kordamp.ikonli.javafx.FontIcon;

public class LeftDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer leftDrawer;
    @FXML public Circle imageCircle;
    @FXML public Label nameLabel;
    @FXML public Label emailLabel;
    @FXML public FontIcon addNewBoardIcon;
    @FXML public ListView<Board> userBoardsListView;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private NewBoardDialogController newBoardDialogController;
    private TitleBarController titleBarController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        newBoardDialogController   = applicationController.newBoardDialogController;
        titleBarController         = applicationController.titleBarController;
    }

    @PostLoad
    public void initUserCard() {
        imageCircle.setFill(new ImagePattern(applicationController.user.getImage()));
        nameLabel.setText(applicationController.user.getName());
        emailLabel.setText(applicationController.user.getEmail());
    }

    @PostLoad
    public void initListView() {
        userBoardsListView.setItems(applicationController.user.getBoards());
        userBoardsListView.setCellFactory(param -> new ListCell<Board>() {
            {
                prefWidthProperty().bind(userBoardsListView.widthProperty().subtract(16));
                setMaxWidth(Control.USE_PREF_SIZE);
            }

            @Override
            protected void updateItem(Board item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @FXML
    public void handleUserCardMouseClicked(MouseEvent mouseEvent) {
        titleBarController.handleUserIconMouseClicked(mouseEvent);
    }

    @FXML
    public void handleAddNewBoardIconMouseClicked(MouseEvent mouseEvent) {
        newBoardDialogController.newBoardDialog.show(applicationController.rootStackPane);
        leftDrawer.close();
    }

    @FXML
    public void handleUserBoardsListViewMouseClicked(MouseEvent mouseEvent) {
        boardController.loadBoard(userBoardsListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleLeftDrawerClosed(JFXDrawerEvent jfxDrawerEvent) {
        titleBarController.hamburgerTransition.setRate(titleBarController.hamburgerTransition.getRate() * -1);
        titleBarController.hamburgerTransition.play();
    }

}
