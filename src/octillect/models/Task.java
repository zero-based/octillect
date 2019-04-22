package octillect.models;

import java.util.Date;

public class Task {

    private String id;
    private String name;
    private String description;
    private boolean isCompleted;
    private Date dueDate;
    private Date creationDate;
    private Project project;
    private Column column;
    private User creator;
    private User[] assignees;
    private Task[] subTasks;


    public Task() {}

    public Task(String id, String name, String description, boolean isCompleted, Date dueDate, Date creationDate,
                Project project, Column column, User creator, User[] assignees, Task[] subTasks) {
        this.id           = id;
        this.name         = name;
        this.description  = description;
        this.isCompleted  = isCompleted;
        this.dueDate      = dueDate;
        this.creationDate = creationDate;
        this.project      = project;
        this.column       = column;
        this.creator      = creator;
        this.assignees    = assignees;
        this.subTasks     = subTasks;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public User[] getAssignees() {
        return assignees;
    }

    public void setAssignees(User[] assignees) {
        this.assignees = assignees;
    }


    public Task[] getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Task[] subTasks) {
        this.subTasks = subTasks;
    }

}
