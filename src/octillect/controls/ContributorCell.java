package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import octillect.models.User;

import org.kordamp.ikonli.javafx.FontIcon;

public class ContributorCell extends ListCell<Pair<User, String>> {

    //FXML Fields
    @FXML public BorderPane contributorCellBorderPane;
    @FXML public Label usernameLabel;
    @FXML public Label emailLabel;
    @FXML public Label roleLabel;
    @FXML public FontIcon closeIcon;

    @Override
    public void updateItem(Pair<User, String> contributorItem, boolean empty) {

        super.updateItem(contributorItem, empty);

        if (empty || contributorItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/ContributorCellView.fxml"));
            fxmlLoader.setController(this);
            contributorCellBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        usernameLabel.setText(contributorItem.getKey().getName());
        emailLabel.setText(contributorItem.getKey().getEmail());
        roleLabel.setText(contributorItem.getValue());

        closeIcon.setOnMouseClicked(event -> {
        });

        setGraphic(contributorCellBorderPane);
    }
}
