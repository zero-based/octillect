package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import octillect.models.Commit;

public class CommitCell extends ListCell<Commit> {

    //FXML Fields
    @FXML public GridPane commitCellGridPane;
    @FXML public Hyperlink commitSubject;
    @FXML public Label commitAuthor;
    @FXML public Label commitDate;
    @FXML public Label commitBody;

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

        String commitMessage =  commitItem.getSubject();
        commitMessage = commitMessage.length() >= 50 ? commitMessage.substring(0, 50) + "..." : commitMessage;
        commitSubject.setText(commitMessage);

        commitAuthor.setText(commitItem.getAuthorName());
        commitDate.setText(commitItem.getDate().toString());
        commitBody.setText(commitItem.getBody());

        commitSubject.setOnMouseClicked(event -> {
            //write code here..
        });
        setGraphic(commitCellGridPane);

    }


}
