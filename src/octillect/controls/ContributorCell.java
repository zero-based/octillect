package octillect.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.models.Project;
import octillect.models.User;

import org.kordamp.ikonli.javafx.FontIcon;

public class ContributorCell extends ListCell<Pair<User, Project.Role>> implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML private BorderPane contributorCellBorderPane;
    @FXML private Circle contributorImage;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private FontIcon closeIcon;

    //Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("ContributorCell cannot be initialized");
    }

    @Override
    public void updateItem(Pair<User, Project.Role> contributorItem, boolean empty) {

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

        if (contributorItem.getKey().getImage() != null) {
            contributorImage.setFill(new ImagePattern(contributorItem.getKey().getImage()));
        }
        usernameLabel.setText(contributorItem.getKey().getName());
        emailLabel.setText(contributorItem.getKey().getEmail());
        roleLabel.setText(contributorItem.getValue().toString());

        closeIcon.setOnMouseClicked(event -> {
        });

        setGraphic(contributorCellBorderPane);
    }
}
