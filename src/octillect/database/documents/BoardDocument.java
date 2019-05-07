package octillect.database.documents;

import java.util.ArrayList;
import java.util.HashMap;

import octillect.models.Board;

public class BoardDocument {

    public static class ContributorMap {

        HashMap<String, String> map = new HashMap();

        public ContributorMap(String id, Board.Role role) {
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
    private ArrayList<HashMap<String, String>> contributors;
    private ArrayList<String> columnsIds;
    private ArrayList<String> tagsIds;

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

    public ArrayList<HashMap<String, String>> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<HashMap<String, String>> contributors) {
        this.contributors = contributors;
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
