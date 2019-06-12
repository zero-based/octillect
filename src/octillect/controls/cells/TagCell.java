package octillect.controls.cells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.settings.TaskSettingsController;
import octillect.controllers.util.Injectable;
import octillect.database.repositories.TagRepository;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.TaskRepository;
import octillect.models.*;

import org.kordamp.ikonli.javafx.FontIcon;

public class TagCell extends ListCell<Tag> implements Injectable<ApplicationController> {

    // Local Variables
    private Mode mode;

    // FXML Fields
    @FXML private BorderPane rootBorderPane;
    @FXML private HBox tagColorHBox;
    @FXML private Label tagNameLabel;
    @FXML private FontIcon deleteTagIcon;

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
        throw new UnsupportedOperationException("TagCell cannot be initialized");
    }

    public TagCell(Mode mode) {
        this.mode = mode;
    }

    public void updateItem(Tag tagItem, boolean empty) {

        super.updateItem(tagItem, empty);

        if (empty || tagItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/TagCell.fxml"));
            fxmlLoader.setController(this);
            rootBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tagColorHBox.setStyle("-fx-background-color: " + tagItem.getColorHex() + "; -fx-background-radius: 4px;");
        tagNameLabel.setText(tagItem.getName());
        tagNameLabel.setStyle(determineTextFillStyle((tagItem.getColor())));


        if (mode == Mode.VIEW_ONLY) {
            setGraphic(tagColorHBox);
            return;
        } else if (mode == Mode.BOARD) {

            if (boardController.currentBoard.getUserRole(applicationController.user.getId())
                    .equals(Collaborator.Role.viewer)) {
                deleteTagIcon.setDisable(true);
                deleteTagIcon.setOpacity(0);
            }

            deleteTagIcon.setOnMouseClicked(event -> {
                /* TODO: Add Confirmation Here. */
                BoardRepository.getInstance().deleteTag(boardController.currentBoard, getItem().getId());
                TagRepository.getInstance().delete(getItem());

                for (Column column : boardController.currentBoard.<Column>getChildren()) {
                    for (Task task : column.<Task>getChildren()) {
                        for (Tag tag : task.getTags()) {
                            if (tag.getId().equals(getItem().getId())) {
                                task.getTags().remove(tag);
                                break;
                            }
                        }

                    }
                }

                boardController.currentBoard.getTags().remove(getItem());
                boardController.loadBoard(boardController.currentBoard);
            });

        } else if (mode == Mode.TASK) {
            deleteTagIcon.setOnMouseClicked(event -> {
                TaskRepository.getInstance().deleteTagId(taskSettingsController.currentTask.getId(), tagItem.getId());
                taskSettingsController.currentTask.getTags().remove(tagItem);
                taskSettingsController.loadTags();
                boardController.boardListView.refresh();
            });
        }

        setGraphic(rootBorderPane);

    }

    static String determineTextFillStyle(Color tagColor) {

        // Counting the perceptive luminance
        double luminance = 0.299 * tagColor.getRed()
                + 0.587 * tagColor.getGreen()
                + 0.114 * tagColor.getBlue();

        return luminance > 0.5 ? "-fx-text-fill: black;" : "-fx-text-fill: white;";
    }

}
