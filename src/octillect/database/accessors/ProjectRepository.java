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

    private static ProjectRepository ourInstance = new ProjectRepository();

    public static ProjectRepository getInstance() {
        return ourInstance;
    }

    private ProjectRepository() {}

    // Add new project data to database.
    public void add(Project project) {
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

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().PROJECTS, document.getId(), document);
    }

    public Project get(String projectId) {
        Project project = null;
        ProjectDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().PROJECTS, projectId)).toObject(ProjectDocument.class);

        if (document != null) {

            project = new ProjectBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.description = document.getDescription();
                $.repositoryName = document.getRepositoryName();

                if (document.getColumnsIds() != null) {
                    ArrayList<Column> columnsIds = new ArrayList<>();
                    for (String columnId : document.getColumnsIds()) {
                        columnsIds.add(ColumnRepository.getInstance().get(columnId));
                    }
                    $.columns = FXCollections.observableArrayList(columnsIds);
                }

                ArrayList<Pair<User, Project.Role>> contributorsIds = new ArrayList<>();
                for (HashMap<String, String> contributor : document.getContributors()) {
                    User user = UserRepository.getInstance().getContributor(contributor.get("id"));
                    Project.Role role = Project.Role.valueOf(contributor.get("role"));
                    Pair<User, Project.Role> pair = new Pair(user, role);
                    contributorsIds.add(pair);
                }
                $.contributors = FXCollections.observableArrayList(contributorsIds);

                if (document.getLabelsIds() != null) {
                    ArrayList<Label> labels = new ArrayList<>();
                    for (String labelId : document.getLabelsIds()) {
                        labels.add(LabelRepository.getInstance().get(labelId));
                    }
                    $.labels = FXCollections.observableArrayList(labels);
                }

            }).build();

        }
        return project;
    }

    public void addColumn(String projectId, String columnId) {
        FirestoreAPI.getInstance().appendAttribute(FirestoreAPI.getInstance().PROJECTS, projectId, "columnsIds", columnId);
    }

    public void updateColumnsIds(String projectId, ArrayList<String> columnsIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().PROJECTS, projectId, "columnsIds", columnsIds);
    }

    public void updateName(String id, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().PROJECTS, id, "name", name);
    }

    public void updateDescription(String id, String description) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().PROJECTS, id, "description", description);
    }

    public void addContributor(String projectId, String email, Project.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.getInstance().encrypt(email), role);
        FirestoreAPI.getInstance().appendAttribute(FirestoreAPI.getInstance().PROJECTS, projectId, "contributors", contributor.getMap());
    }

    public void deleteContributor(String projectId, String email, Project.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.getInstance().encrypt(email), role);
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().PROJECTS, projectId, "contributors", contributor.getMap());
    }

    public void addLabelId(String projectId, String labelId) {
        FirestoreAPI.getInstance().appendAttribute(FirestoreAPI.getInstance().PROJECTS, projectId, "labelsIds", labelId);
    }

    public void deleteLabelId(String projectId, String labelId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().PROJECTS, projectId, "labelsIds", labelId);
    }

    public void delete(Project project) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().PROJECTS, project.getId());

        for (Column column : project.getColumns()) {
            for (Task task : column.getTasks()) {
                FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TASKS, task.getId());
            }
            FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().COLUMNS, column.getId());
        }
        if (project.getLabels() != null) {
            for (Label label : project.getLabels()) {
                LabelRepository.getInstance().delete(label.getId());
            }
        }
    }

    /**
     * Deletes a column from columnsIds attribute by a given id.
     *
     * @param projectId Project's id where column exists.
     * @param columnId  Column's id to Delete.
     */
    public void deleteColumnId(String projectId, String columnId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().PROJECTS, projectId, "columnsIds", columnId);
    }

}
