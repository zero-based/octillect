package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javafx.collections.FXCollections;

import octillect.database.documents.ColumnDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;
import octillect.models.builders.ColumnBuilder;

public class ColumnRepository {

    // Add new column data to database.
    public static void add(Column column) {
        ColumnDocument document = new ColumnDocument();
        document.setId(column.getId());
        document.setName(column.getName());

        if (column.getTasks() != null) {
            ArrayList<String> tasksIds = new ArrayList<>();
            for (Task task : column.getTasks()) {
                tasksIds.add(task.getId());
            }
            document.setTasksIds(tasksIds);
        }
        FirestoreAPI.insertDocument(FirestoreAPI.COLUMNS, document.getId(), document);
    }

    public static Column get(String columnId) {
        Column column = null;
        ColumnDocument document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.COLUMNS, columnId)).toObject(ColumnDocument.class);

        if (document != null) {

            column = new ColumnBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();

                if (document.getTasksIds() != null) {
                    ArrayList<Task> tasksIds = new ArrayList<>();
                    for (String taskId : document.getTasksIds()) {
                        tasksIds.add(TaskRepository.get(taskId));
                    }
                    $.tasks = FXCollections.observableArrayList(tasksIds);

                }
            }).build();

        }
        return column;
    }

    public static void addTask(String columnId, String taskId) {
        FirestoreAPI.appendAttribute(FirestoreAPI.COLUMNS, columnId, "tasksIds", taskId);
    }

    public static void updateTasksIds(String columnId, ArrayList<String> tasksIds) {
        FirestoreAPI.updateAttribute(FirestoreAPI.COLUMNS, columnId, "tasksIds", tasksIds);
    }

    /**
     * Deletes a task from tasksIds attribute by a given id.
     *
     * @param columnId Column's id where task exists.
     * @param taskId   Task's id to Delete.
     */
    public static void deleteTaskId(String columnId, String taskId) {
        FirestoreAPI.deleteArrayElement(FirestoreAPI.COLUMNS, columnId, "tasksIds", taskId);
    }

    /**
     * Deletes a Column from database and all tasks included within.
     *
     * @param column    Column to be deleted.
     */
    public static void delete(Column column) {
        FirestoreAPI.deleteDocument(FirestoreAPI.COLUMNS, column.getId());

        for (Task task : column.getTasks()) {
            FirestoreAPI.deleteDocument(FirestoreAPI.TASKS, task.getId());
        }
    }

}
