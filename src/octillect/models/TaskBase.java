package octillect.models;

import javafx.collections.ObservableList;

abstract public class TaskBase {

    private String id;
    private String name;
    private String description;
    private ObservableList<TaskBase> children;

    TaskBase() {
    }

    TaskBase(String id, String name, String description, ObservableList<TaskBase> children) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.children = children;
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


    public ObservableList<TaskBase> getChildren() {
        return children;
    }

    public void setChildren(ObservableList<TaskBase> children) {
        this.children = children;
    }

}
