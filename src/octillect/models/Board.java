package octillect.models;

import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Board extends TaskBase implements IObservable<Contributor> {

    private String repositoryName;
    private ObservableList<Contributor> contributors = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();
    private FilteredList<Column> filteredColumns;


    public Board() {
    }

    public Board(String id, String name, String description, String repositoryName,
                 ObservableList<Contributor> contributors, ObservableList<Column> columns,
                 ObservableList<Tag> tags) {
        super(id, name, description, columns);
        filteredColumns     = new FilteredList<>(columns);
        this.repositoryName = repositoryName;
        this.contributors   = contributors;
        this.tags           = tags;
    }


    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public ObservableList<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(ObservableList<Contributor> contributors) {
        this.contributors = contributors;
    }


    public ObservableList<Tag> getTags() {
        return tags;
    }

    public void setTags(ObservableList<Tag> tags) {
        this.tags = tags;
    }


    public Contributor.Role getUserRole(String userId) {
        for (Contributor contributor : contributors) {
            if (contributor.getId().equals(userId)) {
                return contributor.getRole();
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
    public void addObserver(Contributor observer) {
        contributors.add(observer);
    }

    @Override
    public void removeObserver(Contributor observer) {
        contributors.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (UserBase observer : contributors) {
            ((Contributor) observer).updateObserver();
        }
    }

}
