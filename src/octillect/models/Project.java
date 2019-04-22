package octillect.models;

import javafx.collections.ObservableList;

public class Project implements IObservable {

    private String id;
    private String name;
    private String description;
    private String repositoryName;
    private Admin admin;
    private ObservableList<IObserver> contributors;
    private ObservableList<Column> columns;
    private ObservableList<Label> labels;


    public Project() {}

    public Project(String id, String name, String description, String repositoryName,
                   ObservableList<IObserver> contributors, ObservableList<Column> columns,
                   ObservableList<Label> labels) {
        this.id             = id;
        this.name           = name;
        this.description    = description;
        this.repositoryName = repositoryName;
        this.contributors   = contributors;
        this.columns        = columns;
        this.labels         = labels;
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


    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }


    public ObservableList<IObserver> getContributors() {
        return contributors;
    }

    public void setContributors(ObservableList<IObserver> contributors) {
        this.contributors = contributors;
    }


    public ObservableList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ObservableList<Column> columns) {
        this.columns = columns;
    }


    public ObservableList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ObservableList<Label> labels) {
        this.labels = labels;
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    @Override
    public void addObserver(IObserver observer) {
        contributors.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        contributors.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : contributors) {
            observer.updateObserver();
        }
    }

}
