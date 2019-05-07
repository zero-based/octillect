package octillect.database.repositories;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;

import octillect.database.documents.TaskDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.*;
import octillect.models.builders.TaskBuilder;

public class TaskRepository implements Repository<Task> {

    private static TaskRepository ourInstance = new TaskRepository();

    public static TaskRepository getInstance() {
        return ourInstance;
    }

    private TaskRepository() {
    }


    @Override
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
            for (Contributor assignee : task.getAssignees()) {
                assigneesIds.add(assignee.getId());
            }
            document.setAssigneesIds(assigneesIds);
        }

        if (task.getChildren() != null) {
            ArrayList<String> subTasksIds = new ArrayList<>();
            for (TaskBase subTasks : task.getChildren()) {
                subTasksIds.add(subTasks.getId());
            }
            document.setSubTasksIds(subTasksIds);
        }

        if (task.getTags() != null) {
            ArrayList<String> tagsIds = new ArrayList<>();
            for (Tag tag : task.getTags()) {
                tagsIds.add(tag.getId());
            }
            document.setTagsIds(tagsIds);
        }

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().TASKS, document.getId(), document);
    }

    @Override
    public Task get(String taskId) {

        TaskDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().TASKS, taskId)).toObject(TaskDocument.class);

        Task task = new TaskBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.description = document.getDescription();
            $.isCompleted = document.getIsCompleted();
            $.dueDate = document.getDueDate();
            $.creationDate = document.getCreationDate();
            $.creator = UserRepository.getInstance().getContributor(document.getCreatorId());

            if (document.getAssigneesIds() != null) {
                ArrayList<Contributor> assignees = new ArrayList<>();
                for (String assigneeID : document.getAssigneesIds()) {
                    assignees.add(UserRepository.getInstance().getContributor(assigneeID));
                }
                $.assignees = FXCollections.observableArrayList(assignees);
            }

            if (document.getSubTasksIds() != null) {
                ArrayList<Task> subTasks = new ArrayList<>();
                for (String subTaskId : document.getSubTasksIds()) {
                    subTasks.add(TaskRepository.getInstance().get(subTaskId));
                }
                $.subTasks = FXCollections.observableArrayList(subTasks);
            }

            if (document.getTagsIds() != null) {
                ArrayList<Tag> tags = new ArrayList<>();
                for (String tagId : document.getTagsIds()) {
                    tags.add(TagRepository.getInstance().get(tagId));
                }
                $.tags = FXCollections.observableArrayList(tags);
            }

        }).build();

        return task;
    }

    @Override
    public void delete(Task task) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TASKS, task.getId());
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

    public void updateTagsIds(String taskId, ArrayList<String> tagsIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "tagsIds", tagsIds);
    }

    public void updateAssigneeIds(String taskId, ArrayList<String> assigneeIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().TASKS, taskId, "assigneesIds", assigneeIds);
    }

}
