package octillect.models.factories;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

import octillect.database.firebase.FirestoreAPI;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Contributor;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.TaskBuilder;

final class WelcomeBoard extends Board {

    WelcomeBoard(Contributor owner) {

        // Columns

        Column essentialsColumn = new ColumnBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Octillect Essentials" + owner.getId()))
                .withName("Octillect Essentials")
                .build();

        Column featuresColumn = new ColumnBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Octillect Features" + owner.getId()))
                .withName("Octillect Features")
                .build();

        setChildren(FXCollections.observableArrayList(essentialsColumn, featuresColumn));
        setFilteredColumns(new FilteredList<>(getChildren()));


        // Tasks

        Task task_1 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Boards" + owner.getId()))
                .withName("Boards")
                .withIsCompleted(false)
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        Task task_2 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Columns" + owner.getId()))
                .withName("Columns")
                .withDescription("Columns group cards into categories, like 'Todo', 'In Progress', and 'Done'.")
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        Task task_3 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Cards" + owner.getId()))
                .withName("Cards")
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        essentialsColumn.setChildren(FXCollections.observableArrayList(task_1, task_2, task_3));
        essentialsColumn.setFilteredTasks(new FilteredList<>(essentialsColumn.getChildren()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.APRIL, 5);

        Task task_4 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Done CheckBox" + owner.getId()))
                .withName("Done CheckBox")
                .withDescription("When a task is done, a CheckBox appears on the Task's Cell.")
                .withIsCompleted(true)
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        Task task_5 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Over Due Date" + owner.getId()))
                .withName("Over Due Date")
                .withDescription("If Task's due date is past, the date's label turns to be red.")
                .withDueDate(calendar.getTime())
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        calendar.set(2019, Calendar.MAY, 11);

        Task task_6 = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Assignees" + owner.getId()))
                .withName("Assignees")
                .withDescription("You can add Assignees to any Task and they will appear on the Task's Cell.")
                .withAssignees(FXCollections.observableArrayList(owner))
                .withDueDate(calendar.getTime())
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        featuresColumn.setChildren(FXCollections.observableArrayList(task_4, task_5, task_6));
        featuresColumn.setFilteredTasks(new FilteredList<>(featuresColumn.getChildren()));

    }

}
