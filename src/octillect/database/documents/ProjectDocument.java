package octillect.database.documents;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectDocument {

    public static class ContributorMap {

        HashMap<String, String> map = new HashMap();

        public ContributorMap(String id, String role) {
            map.put("id", id);
            map.put("role", role);
        }

        public HashMap<String, String> getMap() {
            return map;
        }
    }

    String id;
    String name;
    String description;
    String repositoryName;
    ArrayList<HashMap> contributors;
    ArrayList<String> columnsIds;
    ArrayList<String> labelsIds;

    public ProjectDocument() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public ArrayList<HashMap> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<HashMap> contributors) {
        this.contributors = contributors;
    }

    public ArrayList<String> getColumnsIds() {
        return columnsIds;
    }

    public void setColumnsIds(ArrayList<String> columnsIds) {
        this.columnsIds = columnsIds;
    }

    public ArrayList<String> getLabelsIds() {
        return labelsIds;
    }

    public void setLabelsIds(ArrayList<String> labelsId) {
        this.labelsIds = labelsId;
    }
}
