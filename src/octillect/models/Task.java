package octillect.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class Task extends TaskBase {

    private boolean isCompleted;
    private Date dueDate;
    private Date creationDate;
    private Collaborator creator;
    private ObservableList<Collaborator> assignees = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();


    public Task(String id, String name, String description, boolean isCompleted,
                Date dueDate, Date creationDate, Collaborator creator, ObservableList<Collaborator> assignees,
                ObservableList<Task> subTasks, ObservableList<Tag> tags) {
        super(id, name, description, subTasks);
        this.isCompleted  = isCompleted;
        this.dueDate      = dueDate;
        this.creationDate = creationDate;
        this.creator      = creator;
        this.assignees    = assignees;
        this.tags         = tags;
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


    public Collaborator getCreator() {
        return creator;
    }

    public void setCreator(Collaborator creator) {
        this.creator = creator;
    }


    public ObservableList<Collaborator> getAssignees() {
        return assignees;
    }

    public void setAssignees(ObservableList<Collaborator> assignees) {
        this.assignees = assignees;
    }


    public ObservableList<Tag> getTags() { return tags; }

    public void setTags(ObservableList<Tag> tags) { this.tags = tags; }

}
