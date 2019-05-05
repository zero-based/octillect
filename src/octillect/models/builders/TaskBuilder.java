package octillect.models.builders;

import java.util.Date;
import java.util.function.Consumer;

import javafx.collections.ObservableList;

import octillect.models.Label;
import octillect.models.Task;
import octillect.models.User;

public class TaskBuilder implements Builder<Task, TaskBuilder> {

    public String id;
    public String name;
    public String description;
    public boolean isCompleted;
    public Date dueDate;
    public Date creationDate;
    public User creator;
    public ObservableList<User> assignees;
    public ObservableList<Task> subTasks;
    public ObservableList<Label> labels;

    @Override
    public TaskBuilder with(Consumer<TaskBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Task build() {
        return new Task(id, name, description, isCompleted, dueDate, creationDate, creator, assignees, subTasks, labels);
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

    public TaskBuilder withCreator(User creator) {
        this.creator = creator;
        return this;
    }

    public TaskBuilder withAssignees(ObservableList<User> assignees) {
        this.assignees = assignees;
        return this;
    }

    public TaskBuilder withSubTasks(ObservableList<Task> subTasks) {
        this.subTasks = subTasks;
        return this;
    }

    public TaskBuilder withLabels(ObservableList<Label> labels) {
        this.labels = labels;
        return this;
    }

}
