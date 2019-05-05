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

        // Counting the perceptive luminance
        double luminance = 0.299 * labelItem.getColor().getRed()
                + 0.587 * labelItem.getColor().getGreen()
                + 0.114 * labelItem.getColor().getBlue();

        String textFillStyle = luminance > 0.5 ? "-fx-text-fill: black;" : "-fx-text-fill: white;";
        labelNameLabel.setStyle(textFillStyle);

        deleteLabelIcon.setOnMouseClicked(event -> {
        });

        setGraphic(labelCellBorderPane);

    }

}
