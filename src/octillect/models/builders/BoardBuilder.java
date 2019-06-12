package octillect.models.builders;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import octillect.models.*;

public class BoardBuilder implements Builder<Board, BoardBuilder> {

    public String id;
    public String name;
    public String description;
    public String repositoryName;
    public ObservableList<Collaborator> collaborators = FXCollections.observableArrayList();
    public ObservableList<Column> columns = FXCollections.observableArrayList();
    public ObservableList<Tag> tags = FXCollections.observableArrayList();

    @Override
    public BoardBuilder with(Consumer<BoardBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Board build() {
        return new Board(id, name, description, repositoryName, collaborators, columns, tags);
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

    public BoardBuilder withCollaborators(ObservableList<Collaborator> collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    public BoardBuilder withColumns(ObservableList<Column> columns) {
        this.columns = columns;
        return this;
    }

    public BoardBuilder withTags(ObservableList<Tag> tags) {
        this.tags = tags;
        return this;
    }

}