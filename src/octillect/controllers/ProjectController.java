package octillect.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import octillect.controls.TasksColumn;
import octillect.models.Project;

public class ProjectController {

    @FXML public ListView projectListView;

    public void loadProject(Project project) {
        projectListView.setItems(project.getColumns());
        projectListView.setCellFactory(param -> new TasksColumn());
    }

    /* TODO: Add functions (sort, filter, update, ...) to control ProjectView */

}