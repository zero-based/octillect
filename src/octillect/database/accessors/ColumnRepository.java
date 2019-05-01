package octillect.database.accessors;

import java.util.ArrayList;

import octillect.database.documents.ColumnDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Task;

public class ColumnRepository {

    // Add new column data to database.
    public static void add(Column column) {
        ColumnDocument document = new ColumnDocument();
        document.setId(column.getId());
        document.setName(column.getName());

        if (column.getTasks() != null) {
            ArrayList<String> tasksIds= new ArrayList<>();
            for (Task task : column.getTasks()) {
                tasksIds.add(task.getId());
            }
            document.setTasksIds(tasksIds);
        }
        FirestoreAPI.insertDocument(FirestoreAPI.COLUMNS, document.getId(), document);
    }
}
