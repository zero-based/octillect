package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.controls.TasksColumn;
import octillect.models.Project;

public class ProjectController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public ListView projectListView;
    @FXML public JFXTextField searchTextField;
    @FXML public MaterialDesignIconView gitHubIcon;
    @FXML public MaterialDesignIconView calendarIcon;
    @FXML public MaterialDesignIconView addColumnIcon;

    // Injected Controllers
    private ApplicationController applicationController;
    private TitleBarController titleBarController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        titleBarController         = applicationController.titleBarController;
    }

    public void loadProject(Project project) {

        // Set Project Name in the Title Bar
        titleBarController.projectNameLabel.setText(project.getName());

        // Populate Project Columns
        projectListView.setItems(project.getColumns());
        projectListView.setCellFactory(param -> new TasksColumn());

    }

    public void handleGitHubIconAction(MouseEvent mouseEvent) {
    }

    public void handleCalendarIconAction(MouseEvent mouseEvent) {
    }

    public void handleAddColumnIconAction(MouseEvent mouseEvent) {
    }

}