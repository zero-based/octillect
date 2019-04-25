package octillect.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import octillect.controls.TasksColumn;
import octillect.models.Project;

public class ProjectController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public ListView projectListView;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public void loadProject(Project project) {
        projectListView.setItems(project.getColumns());
        projectListView.setCellFactory(param -> new TasksColumn());
    }

    /* TODO: Add functions (sort, filter, update, ...) to control ProjectView */

}