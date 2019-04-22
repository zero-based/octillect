package octillect.models;

import javafx.collections.ObservableList;

public class Column {

    private String id;
    private String name;
    private Project project;
    private ObservableList<Task> tasks;


    public Column() {}

    public Column(String id, String name, Project project, ObservableList<Task> tasks) {
        this.id      = id;
        this.name    = name;
        this.project = project;
        this.tasks   = tasks;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

}
