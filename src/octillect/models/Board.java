package octillect.models;

import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Board extends TaskBase implements IObservable<Collaborator> {

    private String repositoryName;
    private ObservableList<Collaborator> collaborators = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();
    private FilteredList<Column> filteredColumns;


    public Board() {
    }

    public Board(String id, String name, String description, String repositoryName,
                 ObservableList<Collaborator> collaborators, ObservableList<Column> columns,
                 ObservableList<Tag> tags) {
        super(id, name, description, columns);
        filteredColumns     = new FilteredList<>(columns);
        this.repositoryName = repositoryName;
        this.collaborators = collaborators;
        this.tags           = tags;
    }


    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public ObservableList<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ObservableList<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }


    public ObservableList<Tag> getTags() {
        return tags;
    }

    public void setTags(ObservableList<Tag> tags) {
        this.tags = tags;
    }


    public Collaborator.Role getUserRole(String userId) {
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getId().equals(userId)) {
                return collaborator.getRole();
            }
        }
        return null;
    }


    public FilteredList<Column> getFilteredColumns() {
        return filteredColumns;
    }

    public void setFilteredColumns(FilteredList<Column> filteredColumns) {
        this.filteredColumns = filteredColumns;
    }


    public void setTasksPredicate(Predicate<Task> predicate) {
        getFilteredColumns().forEach(column -> column
                        .getFilteredTasks()
                        .setPredicate(predicate));
    }


    @Override
    public void addObserver(Collaborator observer) {
        collaborators.add(observer);
    }

    @Override
    public void removeObserver(Collaborator observer) {
        collaborators.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (UserBase observer : collaborators) {
            ((Collaborator) observer).updateObserver();
        }
    }

}
