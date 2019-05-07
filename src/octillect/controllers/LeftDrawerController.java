package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.controllers.dialogs.NewBoardDialogController;
import octillect.models.Board;

import org.kordamp.ikonli.javafx.FontIcon;

public class LeftDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer leftDrawer;
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
        boardController = applicationController.boardController;
        newBoardDialogController = applicationController.newBoardDialogController;
        titleBarController         = applicationController.titleBarController;
    }

    @Override
    public void init() {
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
