package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import octillect.controllers.dialogs.NewBoardDialogController;
import octillect.controllers.dialogs.NewColumnDialogController;
import octillect.controllers.dialogs.RepositoryNameDialogController;
import octillect.controllers.settings.BoardSettingsController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controls.OButton;
import octillect.controls.TasksColumn;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Task;

public class BoardController implements Injectable<ApplicationController> {

    // Local Fields
    public Board currentBoard;

    enum FilterTarget {
        TASK_NAME,
        TASK_ASSIGNEES,
        TASK_TAGS
    }

    // FXML Fields
    @FXML public HBox toolBarHBox;
    @FXML public JFXTextField searchTextField;
    @FXML public ListView<Column> boardListView;
    @FXML public Label noBoardsLabel;
    @FXML public OButton newBoardOButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private LeftDrawerController leftDrawerController;
    private RightDrawerController rightDrawerController;
    private TitleBarController titleBarController;
    private NewColumnDialogController newColumnDialogController;
    private NewBoardDialogController newBoardDialogController;
    private RepositoryNameDialogController repositoryNameDialogController;
    private BoardSettingsController boardSettingsController;
    private GitHubRepositoryController gitHubRepositoryController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController     = applicationController;
        leftDrawerController           = applicationController.leftDrawerController;
        rightDrawerController          = applicationController.rightDrawerController;
        titleBarController             = applicationController.titleBarController;
        newColumnDialogController      = applicationController.newColumnDialogController;
        newBoardDialogController       = applicationController.newBoardDialogController;
        repositoryNameDialogController = applicationController.repositoryNameDialogController;
        boardSettingsController        = rightDrawerController.boardSettingsController;
        gitHubRepositoryController     = rightDrawerController.gitHubRepositoryController;
    }

    @Override
    public void init() {

        loadFirstBoard();

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                switch (newValue.charAt(0)) {
                    case '@':
                        currentBoard.setTasksPredicate(getPredicate(FilterTarget.TASK_ASSIGNEES, newValue.substring(1)));
                        break;
                    case '#':
                        currentBoard.setTasksPredicate(getPredicate(FilterTarget.TASK_TAGS, newValue.substring(1)));
                        break;
                    default:
                        currentBoard.setTasksPredicate(getPredicate(FilterTarget.TASK_NAME, newValue));
                        break;
                }
            } else {
                currentBoard.setTasksPredicate(task -> true);
            }
        });

    }

    public void loadFirstBoard() {
        if (!applicationController.user.getBoards().isEmpty()) {
            loadBoard(applicationController.user.getBoards().get(0));
            leftDrawerController.userBoardsListView.getSelectionModel().selectFirst();
        } else {
            showBoardView(false);
            boardSettingsController.resetBoardSettings();
            titleBarController.boardNameLabel.setText("Octillect");
        }
    }


    public void loadBoard(Board board) {

        currentBoard = board;

        showBoardView(true);
        titleBarController.boardNameLabel.setText(board.getName());
        boardSettingsController.loadBoardSettings();

        // Populate Board Columns
        boardListView.setItems(board.getFilteredColumns());
        boardListView.setCellFactory(param -> {
            TasksColumn tasksColumn = new TasksColumn();
            tasksColumn.inject(applicationController);
            return tasksColumn;
        });

    }

    @FXML
    public void handleGitHubIconMouseClicked(MouseEvent mouseEvent) {
        if (currentBoard.getRepositoryName() != null) {
            gitHubRepositoryController.loadGitHubRepository();
            rightDrawerController.show(rightDrawerController.gitHubRepository);
            applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
        } else {
            repositoryNameDialogController.repositoryNameDialog.show(applicationController.rootStackPane);
        }
    }

    @FXML
    public void handleCalendarIconMouseClicked(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleAddColumnIconMouseClicked(MouseEvent mouseEvent) {
        newColumnDialogController.newColumnDialog.show(applicationController.rootStackPane);
    }

    @FXML
    public void handleNewBoardButtonAction(ActionEvent actionEvent) {
        newBoardDialogController.newBoardDialog.show(applicationController.rootStackPane);
    }

    private void showBoardView(boolean show) {
        if (show) {
            searchTextField.setText("");
            toolBarHBox.setOpacity(1);
            noBoardsLabel.setOpacity(0);
            toolBarHBox.setDisable(false);
            newBoardOButton.setOpacity(0);
            newBoardOButton.setDisable(true);
        } else {
            boardListView.setItems(null);
            searchTextField.setText("");
            toolBarHBox.setOpacity(0);
            noBoardsLabel.setOpacity(1);
            toolBarHBox.setDisable(true);
            newBoardOButton.setOpacity(1);
            newBoardOButton.setDisable(false);
        }
    }

    private Predicate<Task> getPredicate(FilterTarget target, String query) {
        Predicate<Task> predicate = null;
        if (target == FilterTarget.TASK_NAME) {
            predicate = task -> containsIgnoreCase(task.getName(), query);
        } else if (target == FilterTarget.TASK_ASSIGNEES) {
            predicate = task -> containsIgnoreCase(task.getAssignees().toString(), query);
        } else if (target == FilterTarget.TASK_TAGS) {
            predicate = task -> containsIgnoreCase(task.getTags().toString(), query);
        }
        return predicate;
    }

    private Boolean containsIgnoreCase(String fullString, String subString) {
        return Pattern.compile(Pattern.quote(subString), Pattern.CASE_INSENSITIVE)
                .matcher(fullString)
                .find();
    }

}
