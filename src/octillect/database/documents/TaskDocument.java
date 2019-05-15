package octillect.database.documents;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TaskDocument {

    public static class SubTaskMap {

        HashMap<String, String> map = new HashMap();

        public SubTaskMap(String id, String name , boolean isCompleted) {
            map.put("id", id);
            map.put("name", name);
            map.put("isCompleted", String.valueOf(isCompleted));
        }

        public HashMap<String, String> getMap() {
            return map;
        }
    }

    private String id;
    private String name;
    private String description;
    private boolean isCompleted;
    private Date dueDate;
    private Date creationDate;
    private String creatorId;
    private ArrayList<String> assigneesIds = new ArrayList<>();
    private ArrayList<HashMap<String, String>> subTasks = new ArrayList<>();
    private ArrayList<String> tagsIds = new ArrayList<>();

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

    public ArrayList<HashMap<String, String>> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<HashMap<String, String>> subTasks) {
        this.subTasks = subTasks;
    }

    public ArrayList<String> getTagsIds() {
        return tagsIds;
    }

    public void setTagsIds(ArrayList<String> tagsIds) {
        this.tagsIds = tagsIds;
    }

}
