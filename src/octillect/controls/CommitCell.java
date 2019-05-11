package octillect.controls;

import java.awt.Desktop;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import octillect.models.Commit;

public class CommitCell extends ListCell<Commit> {

    //FXML Fields
    @FXML private GridPane commitCellGridPane;
    @FXML private Circle authorAvatarCircle;
    @FXML private Hyperlink subjectHyperlink;
    @FXML private Label bodyLabel;
    @FXML private Label authorUsernameLabel;
    @FXML private Label dateLabel;

    @Override
    public void updateItem(Commit commitItem, boolean empty) {

        super.updateItem(commitItem, empty);

        if (empty || commitItem == null) {
            setGraphic(null);
            return;
        }


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/cells/CommitCellView.fxml"));
            fxmlLoader.setController(this);
            commitCellGridPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Modifying Values
        String subject = commitItem.getSubject();
        subject = subject.length() <= 50 ? subject : subject.substring(0, 50) + "...";

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy, h:mm a");
        String date = sdf.format(commitItem.getDate());

        authorAvatarCircle.setFill(commitItem.getAuthorAvatar());
        subjectHyperlink.setText(subject);
        bodyLabel.setText(commitItem.getBody());
        authorUsernameLabel.setText(commitItem.getAuthorUsername());
        dateLabel.setText(date);

        subjectHyperlink.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(commitItem.getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        setGraphic(commitCellGridPane);

    }


}
