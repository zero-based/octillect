package octillect.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import octillect.controls.TasksColumn;
import octillect.models.Column;
import octillect.models.Project;

public class ProjectController {

    @FXML public ListView projectListView;

    public void loadProject(Project project) {
        ObservableList<Column> columnsObservableList = FXCollections.observableArrayList();
        columnsObservableList.setAll(project.getColumns());
        projectListView.setItems(columnsObservableList);
        projectListView.setCellFactory(param -> new TasksColumn());
    }

    /* TODO: Add functions (sort, filter, update, ...) to control ProjectView */

}