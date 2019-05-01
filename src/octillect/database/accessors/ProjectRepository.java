package octillect.database.accessors;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;

import octillect.database.documents.ProjectDocument;
import octillect.database.documents.ProjectDocument.ContributorMap;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Project;
import octillect.models.User;

public class ProjectRepository {

    // Add new project data to database.
    public static void add(Project project) {
        ProjectDocument document = new ProjectDocument();
        document.setId(project.getId());
        document.setName(project.getName());
        document.setDescription(project.getDescription());
        document.setRepositoryName(project.getRepositoryName());

        ArrayList<HashMap> contributorIds = new ArrayList<>();
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

    public static void addContributor(String projectId, String email, String role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.encrypt(email), role);
        FirestoreAPI.appendAttribute(FirestoreAPI.PROJECTS, projectId, "contributors", contributor.getMap());
    }


}
