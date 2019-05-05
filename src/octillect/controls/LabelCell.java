package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import octillect.models.Label;

import org.kordamp.ikonli.javafx.FontIcon;

public class LabelCell extends ListCell<Label> {

    //FXML Fields
    @FXML public BorderPane labelCellBorderPane;
    @FXML public HBox labelColorHBox;
    @FXML public javafx.scene.control.Label labelNameLabel;
    @FXML public FontIcon deleteLabelIcon;

    public void updateItem(Label labelItem, boolean empty) {

        super.updateItem(labelItem, empty);

        if (empty || labelItem == null) {
            setGraphic(null);
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/views/LabelCell.fxml"));
            fxmlLoader.setController(this);
            labelCellBorderPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        labelColorHBox.setStyle("-fx-background-color: " + labelItem.getColorHex() + "; -fx-background-radius: 4px;");
        labelNameLabel.setText(labelItem.getName());

        deleteLabelIcon.setOnMouseClicked(event -> {
        });

        setGraphic(labelCellBorderPane);

    }

}
