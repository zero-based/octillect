package octillect.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;

public class LeftDrawerController {

    @FXML private JFXDrawer leftDrawer;
    @FXML private JFXButton addNewProjectButton;
    @FXML private ListView<Project> userProjectsListView;

    private ProjectController projectController;

    @FXML
    public void initialize() {

        /* TODO: Remove this temporary projects */
        Task task1 = new Task("111", "T1", null, false, null,
                null, null, null, null, null, null);

        Task task2 = new Task("112", "T2", null, false, null,
                null, null, null, null, null, null);

        Task task3 = new Task("121", "T3", null, false, null,
                null, null, null, null, null, null);

        Task task4 = new Task("122", "T4", null, false, null,
                null, null, null, null, null, null);

        Column column1 = new Column("11", "To-do", null,
                FXCollections.observableArrayList(task1, task2));

        Column column2 = new Column("12", "Done", null,
                FXCollections.observableArrayList(task3, task4));

        Project firstProject = new Project();
        firstProject.setName("Project 1");
        firstProject.setColumns(FXCollections.observableArrayList(column1, column2));

        task1 = new Task("211", "X1", null, false, null,
                null, null, null, null, null, null);

        task2 = new Task("212", "X2", null, false, null,
                null, null, null, null, null, null);

        task3 = new Task("221", "X3", null, false, null,
                null, null, null, null, null, null);

        task4 = new Task("222", "X4", null, false, null,
                null, null, null, null, null, null);

        column1 = new Column("21", "Front-end", null,
                FXCollections.observableArrayList(task1, task2));

        column2 = new Column("22", "Back-end", null,
                FXCollections.observableArrayList(task3, task4));

        Project secondProject = new Project();
        secondProject.setName("Project 2 ");
        secondProject.setColumns(FXCollections.observableArrayList(column1, column2));

        /* TODO: Load signed in user projects here */
        ObservableList<Project> userProjects = FXCollections.observableArrayList(firstProject, secondProject);

        userProjectsListView.setItems(userProjects);
        userProjectsListView.setCellFactory(param -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    public void handleAddNewProjectButtonAction(ActionEvent actionEvent) {
        /* TODO: Add new project code here */
    }

    public void handleUserProjectsListViewMouseClicked(MouseEvent mouseEvent) {
        projectController.loadProject(userProjectsListView.getSelectionModel().getSelectedItem());
    }

    public JFXDrawer getLeftDrawer() {
        return leftDrawer;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }


}
