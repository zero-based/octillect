package octillect.models;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import octillect.database.firebase.FirestoreAPI;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.TaskBuilder;

public class Project implements IObservable<Pair<User, Project.Role>> {

    public enum Role {
        owner,
        admin,
        viewer
    }

    private String id;
    private String name;
    private String description;
    private String repositoryName;
    private ObservableList<Pair<User, Role>> contributors;
    private ObservableList<Column> columns;
    private ObservableList<Label> labels;


    public Project() {}

    public Project(String id, String name, String description, String repositoryName,
                   ObservableList<Pair<User, Role>> contributors, ObservableList<Column> columns,
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

    public ObservableList<Pair<User, Role>> getContributors() {
        return contributors;
    }

    public void setContributors(ObservableList<Pair<User, Role>> contributors) {
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

    public Role getUserRole(String userId) {
        for (Pair<User, Role> contributor : contributors) {
            if (contributor.getKey().getId().equals(userId)) {
                return contributor.getValue();
            }
        }
        return null;
    }

    @Override
    public void addObserver(Pair<User, Project.Role> observer) {
        contributors.add(observer);
    }

    @Override
    public void removeObserver(Pair<User, Project.Role> observer) {
        contributors.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Pair<User, Project.Role>  observer : contributors) {
            observer.getKey().updateObserver();
        }
    }


    public static final class WelcomeProject extends Project {

        public WelcomeProject(User user) {

            setId(FirestoreAPI.encryptWithDateTime("Welcome Project" + user.getId()));
            setName("Welcome Project");
            setDescription("Welcome to Octillect");
            setContributors(FXCollections.observableArrayList(new Pair<>(user, Role.owner)));

            Column essentialsColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Octillect Essentials" + user.getId()))
                    .withName("Octillect Essentials")
                    .build();

            Column featuresColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Octillect Features" + user.getId()))
                    .withName("Octillect Features")
                    .build();

            setColumns(FXCollections.observableArrayList(essentialsColumn, featuresColumn));


            Task task_1 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Projects" + user.getId()))
                    .withName("Projects")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            Task task_2 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Columns" + user.getId()))
                    .withName("Columns")
                    .withDescription("Columns group cards into categories, like 'Todo', 'In Progress', and 'Done'.")
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            Task task_3 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Cards" + user.getId()))
                    .withName("Cards")
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            essentialsColumn.setTasks(FXCollections.observableArrayList(task_1, task_2, task_3));


            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, Calendar.APRIL, 5);

            Task task_4 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Done CheckBox" + user.getId()))
                    .withName("Done CheckBox")
                    .withDescription("When a task is done, a CheckBox appears on the Task's Cell.")
                    .withIsCompleted(true)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            Task task_5 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Over Due Date" + user.getId()))
                    .withName("Over Due Date")
                    .withDescription("If Task's due date is past, the date's label turns to be red.")
                    .withDueDate(calendar.getTime())
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            calendar.set(2019, Calendar.MAY, 11);

            Task task_6 = new TaskBuilder()
                    .withId(FirestoreAPI.encryptWithDateTime("Assignees" + user.getId()))
                    .withName("Assignees")
                    .withDescription("You can add Assignees to any Task and they will appear on the Task's Cell.")
                    .withAssignees(FXCollections.observableArrayList(user))
                    .withDueDate(calendar.getTime())
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(user)
                    .build();

            featuresColumn.setTasks(FXCollections.observableArrayList(task_4, task_5, task_6));

        }
    }

}
