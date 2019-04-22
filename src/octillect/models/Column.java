package octillect.models;

public class Column {

    private String id;
    private String name;
    private Project project;
    private Task[] tasks;

    public Column() {}

    public Column(String id, String name, Project project, Task[] tasks) {
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


    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }
}
