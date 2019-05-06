package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.controls.TasksColumn;
import octillect.models.Project;

import org.kordamp.ikonli.javafx.FontIcon;

public class ProjectController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public ListView projectListView;
    @FXML public JFXTextField searchTextField;
    @FXML public FontIcon gitHubIcon;
    @FXML public FontIcon calendarIcon;
    @FXML public FontIcon addColumnIcon;

    public Project currentProject;

    // Injected Controllers
    private ApplicationController applicationController;
    private TitleBarController titleBarController;
    private NewColumnDialogController newColumnDialogController;
    private RightDrawerController rightDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        titleBarController         = applicationController.titleBarController;
        newColumnDialogController  = applicationController.newColumnDialogController;
        rightDrawerController      = applicationController.rightDrawerController;
    }

    @Override
    public void init() {
        loadProject(applicationController.user.getProjects().get(0));
    }

    public void loadProject(Project project) {

        // Set Project Name in the Title Bar
        titleBarController.projectNameLabel.setText(project.getName());

        // Populate Project Columns
        projectListView.setItems(project.getColumns());
        projectListView.setCellFactory(param -> {
            TasksColumn tasksColumn = new TasksColumn();
            tasksColumn.inject(applicationController);
            return tasksColumn;
        });

        currentProject = project;
    }

    @FXML
    public void handleGitHubIconMouseClicked(MouseEvent mouseEvent) {
        rightDrawerController.show(rightDrawerController.gitHubRepository);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    @FXML
    public void handleCalendarIconMouseClicked(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleAddColumnIconMouseClicked(MouseEvent mouseEvent) {
        newColumnDialogController.newColumnDialog.show(applicationController.rootStackPane);
    }

}