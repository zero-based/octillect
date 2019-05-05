package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import octillect.models.Label;
import org.kordamp.ikonli.javafx.FontIcon;

public class LabelCell extends ListCell<Label> {

    //FXML Fields
    @FXML public BorderPane labelCellBorderPane;
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

        String hexCode = String.format( "#%02X%02X%02X",
                (int) labelItem.getColor().getRed() * 255,
                (int) labelItem.getColor().getGreen() * 255,
                (int) labelItem.getColor().getBlue() * 255);

        labelNameLabel.setText(labelItem.getName());
        labelNameLabel.setStyle(labelNameLabel.getStyle() + " -fx-background-color: " + hexCode + ";");

        deleteLabelIcon.setOnMouseClicked(event -> {
        });

        setGraphic(labelCellBorderPane);

    }

}
