package octillect.models.factories;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

import octillect.database.firebase.FirestoreAPI;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Contributor;
import octillect.models.Tag;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;
import octillect.models.builders.TagBuilder;
import octillect.models.builders.TaskBuilder;
import octillect.styles.Palette;

final class DefaultBoard extends Board {

    DefaultBoard(Contributor owner) {

        // Columns

        Column todoColumn = new ColumnBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("To-do" + owner.getId()))
                .withName("To-do")
                .build();

        Column inProgressColumn = new ColumnBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("In Progress" + owner.getId()))
                .withName("In Progress")
                .build();

        Column doneColumn = new ColumnBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Done" + owner.getId()))
                .withName("Done")
                .build();

        setChildren(FXCollections.observableArrayList(todoColumn, inProgressColumn, doneColumn));
        setFilteredColumns(new FilteredList<>(getChildren()));


        // Tasks

        Task todoTask = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("To-do Task" + owner.getId()))
                .withName("To-do Task")
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        todoColumn.setChildren(FXCollections.observableArrayList(todoTask));
        todoColumn.setFilteredTasks(new FilteredList<>(todoColumn.getChildren()));

        Task inProgressTask = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("In Progress Task" + owner.getId()))
                .withName("In Progress Task")
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        inProgressColumn.setChildren(FXCollections.observableArrayList(inProgressTask));
        inProgressColumn.setFilteredTasks(new FilteredList<>(inProgressColumn.getChildren()));

        Task doneTask = new TaskBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("Done Task" + owner.getId()))
                .withName("Done Task")
                .withCreationDate(Calendar.getInstance().getTime())
                .withCreator(owner)
                .build();

        doneColumn.setChildren(FXCollections.observableArrayList(doneTask));
        doneColumn.setFilteredTasks(new FilteredList<>(doneColumn.getChildren()));


        // Tags

        Tag bugTag = new TagBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("bug" + owner.getId()))
                .withName("bug")
                .withColor(Palette.DANGER)
                .build();

        Tag featureTag = new TagBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("feature" + owner.getId()))
                .withName("feature")
                .withColor(Palette.INFO)
                .build();

        Tag improvementTag = new TagBuilder()
                .withId(FirestoreAPI.getInstance().encryptWithDateTime("improvement" + owner.getId()))
                .withName("improvement")
                .withColor(Palette.PRIMARY_DARK)
                .build();

        setTags(FXCollections.observableArrayList(bugTag, featureTag, improvementTag));

    }

}
