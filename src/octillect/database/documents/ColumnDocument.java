package octillect.database.documents;

import java.util.ArrayList;

public class ColumnDocument {

    private String id;
    private String name;
    private ArrayList<String> tasksIds;

    public ColumnDocument() {
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

    public ArrayList<String> getTasksIds() {
        return tasksIds;
    }

    public void setTasksIds(ArrayList<String> tasksIds) {
        this.tasksIds = tasksIds;
    }
}
