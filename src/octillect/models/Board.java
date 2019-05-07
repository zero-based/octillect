package octillect.models;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import octillect.database.firebase.FirestoreAPI;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.ContributorBuilder;
import octillect.models.builders.TaskBuilder;

public class Board extends TaskBase implements IObservable<Contributor> {

    public enum Role {
        owner,
        admin,
        viewer
    }

    private String repositoryName;
    private ObservableList<Contributor> contributors;
    private ObservableList<Tag> tags;

    public Board() {
    }

    public Board(String id, String name, String description, String repositoryName,
                 ObservableList<Contributor> contributors, ObservableList<TaskBase> columns,
                 ObservableList<Tag> tags) {
        super(id, name, description, columns);
        this.repositoryName = repositoryName;
        this.contributors = contributors;
        this.tags = tags;
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


    public Role getUserRole(String userId) {
        for (Contributor contributor : contributors) {
            if (contributor.getId().equals(userId)) {
                return contributor.getRole();
            }
        }
        return null;
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
            ((User) observer).updateObserver();
        }
    }


    public static final class WelcomeBoard extends Board {

        public WelcomeBoard(User user) {

            Contributor owner = new ContributorBuilder().with($ -> {
                $.id = user.getId();
                $.name = user.getName();
                $.email = user.getEmail();
                $.image = user.getImage();
                $.role = Role.owner;
            }).build();

            setId(FirestoreAPI.getInstance().encryptWithDateTime("Welcome Board" + user.getId()));
            setName("Welcome Board");
            setDescription("Welcome to Octillect");
            setContributors(FXCollections.observableArrayList(owner));

            Column essentialsColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Octillect Essentials" + user.getId()))
                    .withName("Octillect Essentials")
                    .build();

            Column featuresColumn = new ColumnBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Octillect Features" + user.getId()))
                    .withName("Octillect Features")
                    .build();

            setChildren(FXCollections.observableArrayList(essentialsColumn, featuresColumn));


            Task task_1 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Boards" + user.getId()))
                    .withName("Boards")
                    .withIsCompleted(false)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            Task task_2 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Columns" + user.getId()))
                    .withName("Columns")
                    .withDescription("Columns group cards into categories, like 'Todo', 'In Progress', and 'Done'.")
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            Task task_3 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Cards" + user.getId()))
                    .withName("Cards")
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            essentialsColumn.setChildren(FXCollections.observableArrayList(task_1, task_2, task_3));


            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, Calendar.APRIL, 5);

            Task task_4 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Done CheckBox" + user.getId()))
                    .withName("Done CheckBox")
                    .withDescription("When a task is done, a CheckBox appears on the Task's Cell.")
                    .withIsCompleted(true)
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            Task task_5 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Over Due Date" + user.getId()))
                    .withName("Over Due Date")
                    .withDescription("If Task's due date is past, the date's label turns to be red.")
                    .withDueDate(calendar.getTime())
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            calendar.set(2019, Calendar.MAY, 11);

            Task task_6 = new TaskBuilder()
                    .withId(FirestoreAPI.getInstance().encryptWithDateTime("Assignees" + user.getId()))
                    .withName("Assignees")
                    .withDescription("You can add Assignees to any Task and they will appear on the Task's Cell.")
                    .withAssignees(FXCollections.observableArrayList(owner))
                    .withDueDate(calendar.getTime())
                    .withCreationDate(Calendar.getInstance().getTime())
                    .withCreator(owner)
                    .build();

            featuresColumn.setChildren(FXCollections.observableArrayList(task_4, task_5, task_6));

        }
    }

}
