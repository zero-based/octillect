package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.util.Pair;

import octillect.database.documents.ProjectDocument;
import octillect.database.documents.ProjectDocument.ContributorMap;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.*;
import octillect.models.builders.ProjectBuilder;

public class ProjectRepository {

    // Add new project data to database.
    public static void add(Project project) {
        ProjectDocument document = new ProjectDocument();
        document.setId(project.getId());
        document.setName(project.getName());
        document.setDescription(project.getDescription());
        document.setRepositoryName(project.getRepositoryName());

        ArrayList<HashMap<String, String>> contributorIds = new ArrayList<>();
        for (Pair<User, Project.Role> contributor : project.getContributors()) {
            ContributorMap contributorMap = new ContributorMap(contributor.getKey().getId(), contributor.getValue());
            contributorIds.add(contributorMap.getMap());
        }
        document.setContributors(contributorIds);

        if (project.getColumns() != null) {
            ArrayList<String> columnsIds = new ArrayList<>();
            for (Column column : project.getColumns()) {
                columnsIds.add(column.getId());
            }
            document.setColumnsIds(columnsIds);
        }

        if (project.getLabels() != null) {
            ArrayList<String> labelsIds = new ArrayList<>();
            for (Label label : project.getLabels()) {
                labelsIds.add(label.getId());
            }
            document.setLabelsIds(labelsIds);
        }

        FirestoreAPI.insertDocument(FirestoreAPI.PROJECTS, document.getId(), document);
    }

    public static Project get(String projectId) {
        Project project = null;
        ProjectDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.selectDocument(FirestoreAPI.PROJECTS, projectId)).toObject(ProjectDocument.class);

        if (document != null) {

            project = new ProjectBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.description = document.getDescription();
                $.repositoryName = document.getRepositoryName();

                if (document.getColumnsIds() != null) {
                    ArrayList<Column> columnsIds = new ArrayList<>();
                    for (String columnId : document.getColumnsIds()) {
                        columnsIds.add(ColumnRepository.get(columnId));
                    }
                    $.columns = FXCollections.observableArrayList(columnsIds);
                }

                ArrayList<Pair<User, Project.Role>> contributorsIds = new ArrayList<>();
                for (HashMap<String, String> contributor : document.getContributors()) {
                    User user = UserRepository.getContributor(contributor.get("id"));
                    Project.Role role = Project.Role.valueOf(contributor.get("role"));
                    Pair<User, Project.Role> pair = new Pair(user, role);
                    contributorsIds.add(pair);
                }
                $.contributors = FXCollections.observableArrayList(contributorsIds);

                if (document.getLabelsIds() != null) {
                    ArrayList<Label> labels = new ArrayList<>();
                    for (String labelId : document.getLabelsIds()) {
                        labels.add(LabelRepository.get(labelId));
                    }
                    $.labels = FXCollections.observableArrayList(labels);
                }

            }).build();

        }
        return project;
    }

    public static void addColumn(String projectId, String columnId) {
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "columnsIds", columnId);
    }

    public static void updateColumnsIds(String projectId, ArrayList<String> columnsIds) {
        FirestoreAPI.updateAttribute(FirestoreAPI.PROJECTS, projectId, "columnsIds", columnsIds);
    }

    public static void updateName(String id, String name) {
        FirestoreAPI.updateAttribute(FirestoreAPI.PROJECTS, id, "name", name);
    }

    public static void updateDescription(String id, String description) {
        FirestoreAPI.updateAttribute(FirestoreAPI.PROJECTS, id, "description", description);
    }

    public static void addContributor(String projectId, String email, Project.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.encrypt(email), role);
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "contributors", contributor.getMap());
    }

    public static void deleteContributor(String projectId, String email, Project.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.encrypt(email), role);
        FirestoreAPI.deleteArrayElement(FirestoreAPI.PROJECTS, projectId, "contributors", contributor.getMap());
    }

    public static void addLabelId(String projectId, String labelId) {
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "labelsIds", labelId);
    }

    public static void deleteLabelId(String projectId, String labelId) {
        FirestoreAPI.deleteArrayElement(FirestoreAPI.PROJECTS, projectId, "labelsIds", labelId);
    }

    public static void delete(Project project) {
        FirestoreAPI.deleteDocument(FirestoreAPI.PROJECTS, project.getId());

        for (Column column : project.getColumns()) {
            for (Task task : column.getTasks()) {
                FirestoreAPI.deleteDocument(FirestoreAPI.TASKS, task.getId());
            }
            FirestoreAPI.deleteDocument(FirestoreAPI.COLUMNS, column.getId());
        }
        if (project.getLabels() != null) {
            for (Label label : project.getLabels()) {
                LabelRepository.delete(label.getId());
            }
        }
    }

    /**
     * Deletes a column from columnsIds attribute by a given id.
     *
     * @param projectId Project's id where column exists.
     * @param columnId  Column's id to Delete.
     */
    public static void deleteColumnId(String projectId, String columnId) {
        FirestoreAPI.deleteArrayElement(FirestoreAPI.PROJECTS, projectId, "columnsIds", columnId);
    }

}
