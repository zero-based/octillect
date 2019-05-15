package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controllers.BoardController;
import octillect.database.repositories.TagRepository;
import octillect.database.repositories.BoardRepository;
import octillect.models.Contributor;
import octillect.models.Tag;

import org.kordamp.ikonli.javafx.FontIcon;

public class TagCell extends ListCell<Tag> implements Injectable<ApplicationController> {

    // Local Variables
    private Mode mode;

    // FXML Fields
    @FXML private BorderPane tagCellBorderPane;
    @FXML private HBox tagColorHBox;
    @FXML private Label tagNameLabel;
    @FXML private FontIcon deleteTagIcon;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
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
            tagCellBorderPane = fxmlLoader.load();
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
                    .equals(Contributor.Role.viewer)) {
                deleteTagIcon.setDisable(true);
                deleteTagIcon.setOpacity(0);
            }

            deleteTagIcon.setOnMouseClicked(event -> {
                /* TODO: Add Confirmation Here. */
                BoardRepository.getInstance().deleteTagId(boardController.currentBoard.getId(), getItem().getId());
                TagRepository.getInstance().delete(getItem());
                boardController.currentBoard.getTags().remove(getItem());
            });

        } else if (mode == Mode.TASK){
            deleteTagIcon.setOnMouseClicked(event -> {
                /* TODO: Handle removing tag from task here. */
            });
        }

        setGraphic(tagCellBorderPane);

    }

    static String determineTextFillStyle(Color tagColor) {

        // Counting the perceptive luminance
        double luminance = 0.299 * tagColor.getRed()
                + 0.587 * tagColor.getGreen()
                + 0.114 * tagColor.getBlue();

        return luminance > 0.5 ? "-fx-text-fill: black;" : "-fx-text-fill: white;";
    }

}
