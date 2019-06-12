package octillect.models.builders;

import java.util.Date;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import octillect.models.*;

public class TaskBuilder implements Builder<Task, TaskBuilder> {

    public String id;
    public String name;
    public String description;
    public boolean isCompleted;
    public Date dueDate;
    public Date creationDate;
    public Collaborator creator;
    public ObservableList<Collaborator> assignees = FXCollections.observableArrayList();
    public ObservableList<Task> subTasks = FXCollections.observableArrayList();
    public ObservableList<Tag> tags = FXCollections.observableArrayList();

    @Override
    public TaskBuilder with(Consumer<TaskBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Task build() {
        return new Task(id, name, description, isCompleted, dueDate, creationDate, creator, assignees, subTasks, tags);
    }

    public TaskBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder withIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public TaskBuilder withDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskBuilder withCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public TaskBuilder withCreator(Collaborator creator) {
        this.creator = creator;
        return this;
    }

    public TaskBuilder withAssignees(ObservableList<Collaborator> assignees) {
        this.assignees = assignees;
        return this;
    }

    public TaskBuilder withSubTasks(ObservableList<Task> subTasks) {
        this.subTasks = subTasks;
        return this;
    }

    public TaskBuilder withTags(ObservableList<Tag> tags) {
        this.tags = tags;
        return this;
    }

}
