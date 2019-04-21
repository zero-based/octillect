package octillect.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;

public class ApplicationController {

    @FXML private ProjectController projectController;

    /* TODO: Remove this event handler */
    public void loadFirstProject(ActionEvent event) {

        Project firstProject = new Project();
        firstProject.setColumns(new Column[]{
                new Column("11", "To-do", firstProject, new Task[]{
                        new Task("111", "T1", null, false, null,
                                null, firstProject, null, null, null, null),
                        new Task("112", "T2", null, false, null,
                                null, firstProject, null, null, null, null),
                }),
                new Column("12", "Done", firstProject, new Task[]{
                        new Task("121", "T3", null, false, null,
                                null, firstProject, null, null, null, null),
                        new Task("122", "T4", null, false, null,
                                null, firstProject, null, null, null, null)})
        });
        projectController.loadProject(firstProject);

    }

    /* TODO: Remove this event handler */
    public void loadSecondProject(ActionEvent event) {

        Project secondProject = new Project();
        secondProject.setColumns(new Column[]{
                new Column("21", "Front-end", null, new Task[]{
                        new Task("211", "X1", null, false, null,
                                null, null, null, null, null, null),
                        new Task("212", "X2", null, false, null,
                                null, null, null, null, null, null),
                }),
                new Column("22", "Back-end", null, new Task[]{
                        new Task("221", "X3", null, false, null,
                                null, null, null, null, null, null),
                        new Task("222", "X4", null, false, null,
                                null, null, null, null, null, null)})
        });
        projectController.loadProject(secondProject);

    }

}
