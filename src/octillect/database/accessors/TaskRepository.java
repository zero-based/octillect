package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javafx.collections.FXCollections;

import octillect.database.documents.TaskDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Label;
import octillect.models.Task;
import octillect.models.User;
import octillect.models.builders.TaskBuilder;

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

        if (task.getLabels() != null) {
            ArrayList<String> labelsIds = new ArrayList<>();
            for (Label label : task.getLabels()) {
                labelsIds.add(label.getId());
            }
            document.setLabelsIds(labelsIds);
        }

        FirestoreAPI.insertDocument(FirestoreAPI.TASKS, document.getId(), document);
    }

    public static Task get(String taskId) {
        Task task = null;
        TaskDocument document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.TASKS, taskId)).toObject(TaskDocument.class);

        if (document != null) {

            task = new TaskBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.description = document.getDescription();
                $.isCompleted = document.getIsCompleted();
                $.dueDate = document.getDueDate();
                $.creationDate = document.getCreationDate();
                $.creator = UserRepository.getContributor(document.getCreatorId());
            }).build();

            if (document.getAssigneesIds() != null) {
                ArrayList<User> tempAssignees = new ArrayList<>();
                for (String assigneeID : document.getAssigneesIds()) {
                    tempAssignees.add(UserRepository.getContributor(assigneeID));
                }
                task.setAssignees(FXCollections.observableArrayList(tempAssignees));
            }

            if (document.getSubTasksIds() != null) {
                ArrayList<Task> tempSubTasks = new ArrayList<>();
                for (String subTaskId : document.getSubTasksIds()) {
                    tempSubTasks.add(TaskRepository.get(subTaskId));
                }
                task.setSubTasks(FXCollections.observableArrayList(tempSubTasks));
            }

            if (document.getLabelsIds() != null) {
                ArrayList<Label> tempLabels = new ArrayList<>();
                for (String labelId : document.getLabelsIds()) {
                    tempLabels.add(LabelRepository.get(labelId));
                }
                task.setLabels(FXCollections.observableArrayList(tempLabels));
            }
        }

        return task;
    }

    public static void delete(String id) {
        FirestoreAPI.deleteDocument(FirestoreAPI.TASKS, id);
    }
}
