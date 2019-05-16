package octillect.database.repositories;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;

import octillect.database.documents.BoardDocument;
import octillect.database.documents.BoardDocument.ContributorMap;
import octillect.database.documents.UserDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.*;
import octillect.models.builders.BoardBuilder;
import octillect.models.builders.ContributorBuilder;

public class BoardRepository implements Repository<Board> {

    private static BoardRepository ourInstance = new BoardRepository();

    public static BoardRepository getInstance() {
        return ourInstance;
    }

    private BoardRepository() {
    }


    @Override
    public void add(Board board) {
        BoardDocument document = new BoardDocument();
        document.setId(board.getId());
        document.setName(board.getName());
        document.setDescription(board.getDescription());
        document.setRepositoryName(board.getRepositoryName());

        ArrayList<HashMap<String, String>> contributorIds = new ArrayList<>();
        for (Contributor contributor : board.getContributors()) {
            ContributorMap contributorMap = new ContributorMap(contributor.getId(), contributor.getRole());
            contributorIds.add(contributorMap.getMap());
        }
        document.setContributors(contributorIds);

        ArrayList<String> columnsIds = new ArrayList<>();
        for (Column column : board.<Column>getChildren()) {
            columnsIds.add(column.getId());
        }
        document.setColumnsIds(columnsIds);

        ArrayList<String> tagsIds = new ArrayList<>();
        for (Tag tag : board.getTags()) {
            tagsIds.add(tag.getId());
        }
        document.setTagsIds(tagsIds);

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().BOARDS, document.getId(), document);
    }

    @Override
    public Board get(String boardId) {

        BoardDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().BOARDS, boardId)).toObject(BoardDocument.class);

        Board board = new BoardBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.description = document.getDescription();
            $.repositoryName = document.getRepositoryName();

            ArrayList<Contributor> contributorsIds = new ArrayList<>();
            for (HashMap<String, String> contributorMap : document.getContributors()) {
                Contributor contributor = getContributor(contributorMap.get("id"));
                contributor.setRole(Contributor.Role.valueOf(contributorMap.get("role")));
                contributorsIds.add(contributor);
            }
            $.contributors = FXCollections.observableArrayList(contributorsIds);

            ArrayList<Column> columns = new ArrayList<>();
            for (String columnId : document.getColumnsIds()) {
                columns.add(ColumnRepository.getInstance().get(columnId));
            }
            $.columns = FXCollections.observableArrayList(columns);

            ArrayList<Tag> tags = new ArrayList<>();
            for (String tagId : document.getTagsIds()) {
                tags.add(TagRepository.getInstance().get(tagId));
            }
            $.tags = FXCollections.observableArrayList(tags);

        }).build();

        return board;
    }

    @Override
    public void delete(Board board) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().BOARDS, board.getId());

        for (Column column : board.<Column>getChildren()) {
            for (Task task : column.<Task>getChildren()) {
                FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TASKS, task.getId());
            }
            FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().COLUMNS, column.getId());
        }
        for (Tag tag : board.getTags()) {
            TagRepository.getInstance().delete(tag);
        }
    }

    public void addColumnId(String boardId, String columnId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "columnsIds", columnId);
    }

    public void updateColumnsIds(String boardId, ArrayList<String> columnsIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardId, "columnsIds", columnsIds);
    }

    public void updateName(String boardId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardId, "name", name);
    }

    public void updateDescription(String boardId, String description) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardId, "description", description);
    }

    public void updateRepositoryName(String boardId, String repositoryName) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardId, "repositoryName", repositoryName);
    }

    public void addContributor(String boardId, Contributor contributor) {
        ContributorMap contributorMap = new ContributorMap(contributor.getId(), contributor.getRole());
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "contributors", contributorMap.getMap());
    }

    public Contributor getContributor(String contributorId) {

        Contributor contributor = null;
        UserDocument document;
        document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().USERS, contributorId)).toObject(UserDocument.class);

        if (document != null) {
            contributor = new ContributorBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.image = UserRepository.getInstance().getImage(document.getId());
            }).build();
        }

        return contributor;
    }

    public void updateContributorsIds(String boardId, ArrayList<HashMap<String,String>> contributorsIds){
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.getInstance().BOARDS, boardId, "contributors", contributorsIds);
    }

    public void deleteContributor(Board board, Contributor contributor) {
        ContributorMap contributorMap = new ContributorMap(contributor.getId(), contributor.getRole());
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().BOARDS, board.getId(), "contributors", contributorMap.getMap());

        for (Column column : board.<Column>getChildren()) {
            for (Task task : column.<Task>getChildren()) {
                for (Contributor assignee : task.getAssignees()) {
                    if (assignee.getId().equals(contributor.getId())) {
                        TaskRepository.getInstance().deleteAssigneeId(task.getId(), assignee.getId());
                        break;
                    }
                }
            }
        }
    }

    public void addTagId(String boardId, String tagId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "tagsIds", tagId);
    }

    public void deleteTag(Board board, String tagId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().BOARDS, board.getId(), "tagsIds", tagId);

        for (Column column : board.<Column>getChildren()) {
            for (Task task : column.<Task>getChildren()) {
                for (Tag tag : task.getTags()) {
                    if (tag.getId().equals(tagId)) {
                        TaskRepository.getInstance().deleteTagId(task.getId(), tagId);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Deletes a column from columnsIds attribute by a given id.
     *
     * @param boardId  Board's id where column exists.
     * @param columnId Column's id to Delete.
     */
    public void deleteColumnId(String boardId, String columnId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "columnsIds", columnId);
    }

}
