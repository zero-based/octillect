package octillect.database.repositories;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javafx.collections.FXCollections;

import octillect.database.documents.ColumnDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;
import octillect.models.TaskBase;
import octillect.models.builders.ColumnBuilder;

public class ColumnRepository implements Repository<Column> {

    private static ColumnRepository ourInstance = new ColumnRepository();

    public static ColumnRepository getInstance() {
        return ourInstance;
    }

    private ColumnRepository() {
    }


    @Override
    public void add(Column column) {
        ColumnDocument document = new ColumnDocument();
        document.setId(column.getId());
        document.setName(column.getName());

        if (column.getChildren() != null) {
            ArrayList<String> tasksIds = new ArrayList<>();
            for (TaskBase task : column.getChildren()) {
                tasksIds.add(task.getId());
            }
            document.setTasksIds(tasksIds);
        }
        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().COLUMNS, document.getId(), document);
    }

    @Override
    public Column get(String columnId) {

        ColumnDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().COLUMNS, columnId)).toObject(ColumnDocument.class);

        Column column = new ColumnBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();

            if (document.getTasksIds() != null) {
                ArrayList<Task> tasks = new ArrayList<>();
                for (String taskId : document.getTasksIds()) {
                    tasks.add(TaskRepository.getInstance().get(taskId));
                }
                $.tasks = FXCollections.observableArrayList(tasks);
            }
        }).build();

        return column;
    }

    /**
     * Deletes a Column from database and all tasks included within.
     *
     * @param column Column to be deleted.
     */
    @Override
    public void delete(Column column) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().COLUMNS, column.getId());
        for (TaskBase task : column.getChildren()) {
            TaskRepository.getInstance().delete((Task) task);
        }
    }

    public void addTask(String columnId, String taskId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().COLUMNS, columnId, "tasksIds", taskId);
    }

    public void updateName(String columnId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().COLUMNS, columnId, "name", name);
    }

    public void updateTasksIds(String columnId, ArrayList<String> tasksIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().COLUMNS, columnId, "tasksIds", tasksIds);
    }

    /**
     * Deletes a task from tasksIds attribute by a given id.
     *
     * @param columnId Column's id where task exists.
     * @param taskId   Task's id to Delete.
     */
    public void deleteTaskId(String columnId, String taskId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().COLUMNS, columnId, "tasksIds", taskId);
    }

}
