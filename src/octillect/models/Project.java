package octillect.models;

import java.util.List;

public class Project implements IObservable {

    private String id;
    private String name;
    private String description;
    private List<IObserver> contributors;
    private Column[] columns;
    private User admin;
    private Label[] labels;
    private String nameOfRepo;

    public Project() {}

    public Project(String id, String name, String description, List<IObserver> contributors, Column[] columns, Label [] labels, String nameOfRepo){
        this.id = id;
        this.name = name;
        this.description = description;
        this.contributors = contributors;
        this.columns = columns;
        this.labels = labels;
        this.nameOfRepo = nameOfRepo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public Column[] getColumns() { return columns;}
    public void setColumns(Column[] columns) { this.columns = columns; }

    public List<IObserver> getContributors() { return contributors;}
    public void setContributors(List<IObserver> contributors) { this.contributors = contributors; }

    public Label[] getLabels() { return labels; }
    public void setLabels(Label[] labels) { this.labels = labels; }

    public String getNameOfRepo() { return nameOfRepo; }
    public void setNameOfRepo(String nameOfRepo) { this.nameOfRepo = nameOfRepo; }

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
        for (IObserver observer : contributors) { observer.updateObserver(); }
    }

}
