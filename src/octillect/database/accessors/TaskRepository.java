package octillect.database.accessors;

import java.util.ArrayList;

import octillect.database.documents.TaskDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Task;
import octillect.models.User;

public class TaskRepository {

    // Add new task data to database.
    public static void add(Task task) {
        TaskDocument document = new TaskDocument();
        document.setId(task.getId());
        document.setName(task.getName());
        document.setDescription(task.getDescription());
        document.setIsCompleted(task.getIsCompleted());
        document.setDueDate(task.getDueDate());
        document.setCreationDate(task.getCreationDate());
        document.setCreatorId(task.getCreator().getId());

        if (task.getAssignees() != null) {
            ArrayList<String> assigneesIds = new ArrayList<>();
            for (User assignee : task.getAssignees()) {
                assigneesIds.add(assignee.getId());
            }
            document.setAssigneesIds(assigneesIds);
        }
        if (task.getSubTasks() != null) {
            ArrayList<String> subTasksIds = new ArrayList<>();
            for (Task subTasks : task.getSubTasks()) {
                subTasksIds.add(subTasks.getId());
            }
            document.setSubTasksIds(subTasksIds);
        }
        FirestoreAPI.insertDocument(FirestoreAPI.TASKS, document.getId(), document);
    }
}
