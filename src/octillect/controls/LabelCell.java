package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.models.Label;

import org.kordamp.ikonli.javafx.FontIcon;

public class LabelCell extends ListCell<Label> implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML private BorderPane labelCellBorderPane;
    @FXML private HBox labelColorHBox;
    @FXML private javafx.scene.control.Label labelNameLabel;
    @FXML private FontIcon deleteLabelIcon;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("LabelCell cannot be initialized");
    }


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
        labelNameLabel.setStyle(determineTextFillStyle((labelItem.getColor())));

        deleteLabelIcon.setOnMouseClicked(event -> {
        });

        setGraphic(labelCellBorderPane);

    }

    private String determineTextFillStyle(Color labelColor){

        // Counting the perceptive luminance
        double luminance = 0.299 * labelColor.getRed()
                + 0.587 * labelColor.getGreen()
                + 0.114 * labelColor.getBlue();

        return luminance > 0.5 ? "-fx-text-fill: black;" : "-fx-text-fill: white;";
    }

}
