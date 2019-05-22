package octillect.database.repositories;

import java.util.ArrayList;

import javafx.collections.FXCollections;

import octillect.database.documents.ColumnDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;
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

        ArrayList<String> tasksIds = new ArrayList<>();
        for (Task task : column.<Task>getChildren()) {
            tasksIds.add(task.getId());
        }
        document.setTasksIds(tasksIds);

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.COLUMNS, document.getId(), document);
    }

    @Override
    public Column get(String columnId) {

        ColumnDocument document = FirestoreAPI.getInstance().selectDocument(FirestoreAPI.COLUMNS, columnId).toObject(ColumnDocument.class);

        Column column = new ColumnBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();

            ArrayList<Task> tasks = new ArrayList<>();
            for (String taskId : document.getTasksIds()) {
                tasks.add(TaskRepository.getInstance().get(taskId));
            }
            $.tasks = FXCollections.observableArrayList(tasks);

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
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.COLUMNS, column.getId());
        for (Task task : column.<Task>getChildren()) {
            TaskRepository.getInstance().delete(task);
        }
    }

    public void addTask(String columnId, String taskId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.COLUMNS, columnId, "tasksIds", taskId);
    }

    public void updateName(String columnId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.COLUMNS, columnId, "name", name);
    }

    public void updateTasksIds(String columnId, ArrayList<String> tasksIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.COLUMNS, columnId, "tasksIds", tasksIds);
    }

    /**
     * Deletes a task from tasksIds attribute by a given id.
     *
     * @param columnId Column's id where task exists.
     * @param taskId   Task's id to Delete.
     */
    public void deleteTaskId(String columnId, String taskId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.COLUMNS, columnId, "tasksIds", taskId);
    }

}
