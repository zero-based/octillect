package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML public Label noProjectsLabel;

    public Project currentProject;

    // Injected Controllers
    private ApplicationController applicationController;
    private TitleBarController titleBarController;
    private NewColumnDialogController newColumnDialogController;
    private RightDrawerController rightDrawerController;
    private LeftDrawerController leftDrawerController;
    private ProjectSettingsController projectSettingsController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        titleBarController         = applicationController.titleBarController;
        newColumnDialogController  = applicationController.newColumnDialogController;
        rightDrawerController      = applicationController.rightDrawerController;
        leftDrawerController       = applicationController.leftDrawerController;
        projectSettingsController  = rightDrawerController.projectSettingsController;
    }

    @Override
    public void init() {
        if (!applicationController.user.getProjects().isEmpty()) {
            loadProject(applicationController.user.getProjects().get(0));
            leftDrawerController.userProjectsListView.getSelectionModel().selectFirst();
        } else {
            showToolbar(false);
            titleBarController.projectNameLabel.setText("Octillect");
        }
    }

    public void loadProject(Project project) {

        currentProject = project;

        showToolbar(true);
        titleBarController.projectNameLabel.setText(project.getName());
        projectSettingsController.loadProjectSettings();

        // Populate Project Columns
        projectListView.setItems(project.getColumns());
        projectListView.setCellFactory(param -> {
            TasksColumn tasksColumn = new TasksColumn();
            tasksColumn.inject(applicationController);
            return tasksColumn;
        });

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

    private void showToolbar(boolean show) {
        if (show) {
            searchTextField.setOpacity(1);
            searchTextField.setDisable(false);
            gitHubIcon.setOpacity(1);
            gitHubIcon.setDisable(false);
            calendarIcon.setOpacity(1);
            calendarIcon.setDisable(false);
            addColumnIcon.setOpacity(1);
            addColumnIcon.setDisable(false);
            noProjectsLabel.setOpacity(0);
        } else {
            searchTextField.setOpacity(0);
            searchTextField.setDisable(true);
            gitHubIcon.setOpacity(0);
            gitHubIcon.setDisable(true);
            calendarIcon.setOpacity(0);
            calendarIcon.setDisable(true);
            addColumnIcon.setOpacity(0);
            addColumnIcon.setDisable(true);
            noProjectsLabel.setOpacity(1);
        }
    }

}