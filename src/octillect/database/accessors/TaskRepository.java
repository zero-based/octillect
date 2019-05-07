package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;

import octillect.database.documents.TaskDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Label;
import octillect.models.Task;
import octillect.models.User;
import octillect.models.builders.TaskBuilder;

public class TaskRepository {

    private static TaskRepository ourInstance = new TaskRepository();

    public static TaskRepository getInstance() {
        return ourInstance;
    }

    private TaskRepository() {}

    // Add new task data to database.
    public void add(Task task) {
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

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().TASKS, document.getId(), document);
    }

    public Task get(String taskId) {
        Task task = null;
        TaskDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().TASKS, taskId)).toObject(TaskDocument.class);

        if (document != null) {

            task = new TaskBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.description = document.getDescription();
                $.isCompleted = document.getIsCompleted();
                $.dueDate = document.getDueDate();
                $.creationDate = document.getCreationDate();
                $.creator = UserRepository.getInstance().getContributor(document.getCreatorId());
            }).build();

            if (document.getAssigneesIds() != null) {
                ArrayList<User> tempAssignees = new ArrayList<>();
                for (String assigneeID : document.getAssigneesIds()) {
                    tempAssignees.add(UserRepository.getInstance().getContributor(assigneeID));
                }
                task.setAssignees(FXCollections.observableArrayList(tempAssignees));
            }

            if (document.getSubTasksIds() != null) {
                ArrayList<Task> tempSubTasks = new ArrayList<>();
                for (String subTaskId : document.getSubTasksIds()) {
                    tempSubTasks.add(TaskRepository.getInstance().get(subTaskId));
                }
                task.setSubTasks(FXCollections.observableArrayList(tempSubTasks));
            }

            if (document.getLabelsIds() != null) {
                ArrayList<Label> tempLabels = new ArrayList<>();
                for (String labelId : document.getLabelsIds()) {
                    tempLabels.add(LabelRepository.getInstance().get(labelId));
                }
                task.setLabels(FXCollections.observableArrayList(tempLabels));
            }
        }

        return task;
    }

    public void updateName(String taskId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "name", name);
    }

    public void updateDescription(String taskId, String description) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "description", description);
    }

    public void updateisCompleted(String taskId, boolean isCompleted) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "isCompleted", isCompleted);
    }

    public void updateDueDate(String taskId, Date dueDate) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "dueDate", dueDate);
    }

    public void updateLabelsIds(String taskId, ArrayList<String> labelIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "labelsIds", labelIds);
    }

    public void updateAssigneeIds(String taskId, ArrayList<String> assigneeIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "assigneesIds", assigneeIds);
    }


    public void delete(String id) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TASKS, id);
    }
}
