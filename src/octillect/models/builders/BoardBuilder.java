package octillect.models.builders;

import java.util.function.Consumer;

import javafx.collections.ObservableList;

import octillect.models.*;

public class BoardBuilder implements Builder<Board, BoardBuilder> {

    public String id;
    public String name;
    public String description;
    public String repositoryName;
    public ObservableList<Contributor> contributors;
    public ObservableList<TaskBase> columns;
    public ObservableList<Tag> tags;

    @Override
    public BoardBuilder with(Consumer<BoardBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Board build() {
        return new Board(id, name, description, repositoryName, contributors, columns, tags);
    }

    public BoardBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BoardBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BoardBuilder withRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public BoardBuilder withContributors(ObservableList<Contributor> contributors) {
        this.contributors = contributors;
        return this;
    }

    public BoardBuilder withColumns(ObservableList<TaskBase> columns) {
        this.columns = columns;
        return this;
    }

    public BoardBuilder withTags(ObservableList<Tag> tags) {
        this.tags = tags;
        return this;
    }

}