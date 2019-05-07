package octillect.database.documents;

import java.util.ArrayList;
import java.util.Date;

public class TaskDocument {

    private String id;
    private String name;
    private String description;
    private boolean isCompleted;
    private Date dueDate;
    private Date creationDate;
    private String creatorId;
    private ArrayList<String> assigneesIds;
    private ArrayList<String> subTasksIds;
    private ArrayList<String> tagsIds;

    public TaskDocument() {
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<String> getAssigneesIds() {
        return assigneesIds;
    }

    public void setAssigneesIds(ArrayList<String> assigneesIds) {
        this.assigneesIds = assigneesIds;
    }

    public ArrayList<String> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<String> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public ArrayList<String> getTagsIds() {
        return tagsIds;
    }

    public void setTagsIds(ArrayList<String> tagsIds) {
        this.tagsIds = tagsIds;
    }

}
