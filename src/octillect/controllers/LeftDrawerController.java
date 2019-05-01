package octillect.controllers;

import com.jfoenix.controls.JFXDrawer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.models.Project;

import org.kordamp.ikonli.javafx.FontIcon;

public class LeftDrawerController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDrawer leftDrawer;
    @FXML public FontIcon addNewProjectIcon;
    @FXML public ListView<Project> userProjectsListView;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;
    private NewProjectDialogController newProjectDialogController;

    public ObservableList<Project> userProjects = FXCollections.observableArrayList();

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController = applicationController.projectController;
        newProjectDialogController = applicationController.newProjectDialogController;
    }

    @FXML
    public void initialize() {
        /* TODO: Load signed in user projects here */
        userProjectsListView.setItems(userProjects);
        userProjectsListView.setCellFactory(param -> new ListCell<Project>() {
            {
                prefWidthProperty().bind(userProjectsListView.widthProperty().subtract(16));
                setMaxWidth(Control.USE_PREF_SIZE);
            }

            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @FXML
    public void handleAddNewProjectIconMouseClicked(MouseEvent mouseEvent) {
        newProjectDialogController.newProjectDialog.show(applicationController.rootStackPane);
    }

    @FXML
    public void handleUserProjectsListViewMouseClicked(MouseEvent mouseEvent) {
        projectController.loadProject(userProjectsListView.getSelectionModel().getSelectedItem());
    }

}
