package octillect.models;

import javafx.collections.ObservableList;

import java.util.Date;

public class Task {

    private String id;
    private String name;
    private String description;
    private boolean isCompleted;
    private Date dueDate;
    private Date creationDate;
    private User creator;
    private ObservableList<User> assignees;
    private ObservableList<Task> subTasks;
    private ObservableList<Label> labels;

    public Task() {}

    public Task(String id, String name, String description, boolean isCompleted,
                Date dueDate, Date creationDate, User creator,
                ObservableList<User> assignees, ObservableList<Task> subTasks, ObservableList<Label> labels ) {
        this.id           = id;
        this.name         = name;
        this.description  = description;
        this.isCompleted  = isCompleted;
        this.dueDate      = dueDate;
        this.creationDate = creationDate;
        this.creator      = creator;
        this.assignees    = assignees;
        this.subTasks     = subTasks;
        this.labels       = labels;
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


    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
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


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public ObservableList<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(ObservableList<User> assignees) {
        this.assignees = assignees;
    }


    public ObservableList<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ObservableList<Task> subTasks) {
        this.subTasks = subTasks;
    }


    public ObservableList<Label> getLabels() { return labels; }

    public void setLabels(ObservableList<Label> labels) { this.labels = labels; }

}
