package octillect.models.builders;

import java.util.function.Consumer;

import javafx.collections.ObservableList;

import octillect.models.*;

public class ProjectBuilder implements Builder<Project, ProjectBuilder> {

    public String id;
    public String name;
    public String description;
    public String repositoryName;
    public User admin;
    public ObservableList<IObserver> contributors;
    public ObservableList<Column> columns;
    public ObservableList<Label> labels;

    @Override
    public ProjectBuilder with(Consumer<ProjectBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Project build() {
        return new Project(id, name, description, repositoryName, admin, contributors, columns, labels);
    }

    public ProjectBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ProjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectBuilder withRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public ProjectBuilder withAdmin(User admin) {
        this.admin = admin;
        return this;
    }

    public ProjectBuilder withContributors(ObservableList<IObserver> contributors) {
        this.contributors = contributors;
        return this;
    }

    public ProjectBuilder withColumns(ObservableList<Column> columns) {
        this.columns = columns;
        return this;
    }

    public ProjectBuilder withLabels(ObservableList<Label> labels) {
        this.labels = labels;
        return this;
    }

}