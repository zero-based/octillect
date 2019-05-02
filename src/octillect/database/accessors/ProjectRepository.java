package octillect.database.accessors;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.util.Pair;

import octillect.database.documents.ProjectDocument;
import octillect.database.documents.ProjectDocument.ContributorMap;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Project;
import octillect.models.User;
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
        for (Pair<User, String> contributor : project.getContributors()) {
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

        /* TODO : Add Labels' Ids  */
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

                ArrayList<Pair<User, String>> contributorsIds = new ArrayList<>();
                for (HashMap<String, String> contributor : document.getContributors()) {
                    User user = UserRepository.getContributor(contributor.get("id"));
                    String role = contributor.get("role");
                    Pair<User, String> pair = new Pair(user, role);
                    contributorsIds.add(pair);
                }
                $.contributors = FXCollections.observableArrayList(contributorsIds);

            }).build();

        }
        return project;
    }

    public static void addContributor(String projectId, String email, String role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.encrypt(email), role);
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "contributors", contributor.getMap());
    }

    public static void addColumn(String projectId, String columnId) {
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "columnsIds", columnId);
    }

    public static void updateColumnsIds(String projectId, ArrayList<String> columnsIds) {
        FirestoreAPI.updateAttribute(FirestoreAPI.PROJECTS, projectId, "columnsIds", columnsIds);
    }

}
