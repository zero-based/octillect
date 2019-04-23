package octillect.controllers;

import com.jfoenix.controls.JFXDrawersStack;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;

public class ApplicationController {

    @FXML private JFXDrawersStack drawersStack;
    @FXML private TitleBarController titleBarController;
    @FXML private LeftDrawerController leftDrawerController;
    @FXML private RightDrawerController rightDrawerController;
    @FXML private ProjectController projectController;

    @FXML
    public void initialize() {
        titleBarController.setDrawersStack(drawersStack);
        titleBarController.setLeftDrawer(leftDrawerController.getLeftDrawer());
        titleBarController.setRightDrawer(rightDrawerController.getRightDrawer());
    }

    /* TODO: Remove this event handler */
    public void loadFirstProject(ActionEvent event) {

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
        firstProject.setColumns(FXCollections.observableArrayList(column1, column2));
        projectController.loadProject(firstProject);

    }

    /* TODO: Remove this event handler */
    public void loadSecondProject(ActionEvent event) {

        Task task1 = new Task("211", "X1", null, false, null,
                null, null, null, null, null, null);

        Task task2 = new Task("212", "X2", null, false, null,
                null, null, null, null, null, null);

        Task task3 = new Task("221", "X3", null, false, null,
                null, null, null, null, null, null);

        Task task4 = new Task("222", "X4", null, false, null,
                null, null, null, null, null, null);

        Column column1 = new Column("21", "Front-end", null,
                FXCollections.observableArrayList(task1, task2));

        Column column2 = new Column("22", "Back-end", null,
                FXCollections.observableArrayList(task3, task4));

        Project secondProject = new Project();
        secondProject.setColumns(FXCollections.observableArrayList(column1, column2));
        projectController.loadProject(secondProject);

    }

}
