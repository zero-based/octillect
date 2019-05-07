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
import octillect.controllers.LeftDrawerController;
import octillect.controllers.ProjectController;
import octillect.database.accessors.ProjectRepository;
import octillect.database.accessors.UserRepository;
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
    private ProjectController projectController;
    private LeftDrawerController leftDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
        leftDrawerController       = applicationController.leftDrawerController;
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

        if (projectController.currentProject.getUserRole(applicationController.user.getId())
                .equals(Project.Role.viewer)) {
            closeIcon.setDisable(true);
            closeIcon.setOpacity(0);
        }

        closeIcon.setOnMouseClicked(event -> {
            /* TODO: Add Confirmation Here. */
            ProjectRepository.deleteContributor(projectController.currentProject.getId(),
                    getItem().getKey().getEmail(), getItem().getValue());
            UserRepository.deleteProjectId(getItem().getKey().getId(), projectController.currentProject.getId());

            if (projectController.currentProject.getContributors().size() == 1) {
                // Delete whole project in case no contributors left
                ProjectRepository.delete(projectController.currentProject);
            }

            if (getItem().getKey().getId().equals(applicationController.user.getId())) {
                applicationController.user.getProjects().remove(projectController.currentProject);
                projectController.init();
                leftDrawerController.init();
            } else {
                int index = applicationController.user.getProjects().indexOf(projectController.currentProject);
                applicationController.user.getProjects().get(index).getContributors().remove(getItem());
                projectController.currentProject.getContributors().remove(getItem());
                getListView().getItems().remove(getItem());
            }

        });

        setGraphic(contributorCellBorderPane);
    }

}
