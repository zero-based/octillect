package octillect.database.documents;

import java.util.ArrayList;
import java.util.HashMap;

import octillect.models.Collaborator;

public class BoardDocument {

    public static class CollaboratorMap {

        HashMap<String, String> map = new HashMap();

        public CollaboratorMap(String id, Collaborator.Role role) {
            map.put("id", id);
            map.put("role", role.toString());
        }

        public HashMap<String, String> getMap() {
            return map;
        }
    }

    private String id;
    private String name;
    private String description;
    private String repositoryName;
    private ArrayList<HashMap<String, String>> collaborators = new ArrayList<>();
    private ArrayList<String> columnsIds = new ArrayList<>();
    private ArrayList<String> tagsIds = new ArrayList<>();

    public BoardDocument() {}

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

    public ArrayList<HashMap<String, String>> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<HashMap<String, String>> collaborators) {
        this.collaborators = collaborators;
    }

    public ArrayList<String> getColumnsIds() {
        return columnsIds;
    }

    public void setColumnsIds(ArrayList<String> columnsIds) {
        this.columnsIds = columnsIds;
    }

    public ArrayList<String> getTagsIds() {
        return tagsIds;
    }

    public void setTagsIds(ArrayList<String> tagsId) {
        this.tagsIds = tagsId;
    }
}
