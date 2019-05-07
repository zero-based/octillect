package octillect.database.repositories;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.util.Pair;

import octillect.database.documents.BoardDocument;
import octillect.database.documents.BoardDocument.ContributorMap;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.*;
import octillect.models.builders.BoardBuilder;

public class BoardRepository {

    private static BoardRepository ourInstance = new BoardRepository();

    public static BoardRepository getInstance() {
        return ourInstance;
    }

    private BoardRepository() {
    }

    // Add new board data to database.
    public void add(Board board) {
        BoardDocument document = new BoardDocument();
        document.setId(board.getId());
        document.setName(board.getName());
        document.setDescription(board.getDescription());
        document.setRepositoryName(board.getRepositoryName());

        ArrayList<HashMap<String, String>> contributorIds = new ArrayList<>();
        for (Pair<User, Board.Role> contributor : board.getContributors()) {
            ContributorMap contributorMap = new ContributorMap(contributor.getKey().getId(), contributor.getValue());
            contributorIds.add(contributorMap.getMap());
        }
        document.setContributors(contributorIds);

        if (board.getColumns() != null) {
            ArrayList<String> columnsIds = new ArrayList<>();
            for (Column column : board.getColumns()) {
                columnsIds.add(column.getId());
            }
            document.setColumnsIds(columnsIds);
        }

        if (board.getTags() != null) {
            ArrayList<String> tagsIds = new ArrayList<>();
            for (Tag tag : board.getTags()) {
                tagsIds.add(tag.getId());
            }
            document.setTagsIds(tagsIds);
        }

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.getInstance().BOARDS, document.getId(), document);
    }

    public Board get(String boardId) {

        BoardDocument document = ((DocumentSnapshot) FirestoreAPI.getInstance().selectDocument(FirestoreAPI.getInstance().BOARDS, boardId)).toObject(BoardDocument.class);

        Board board = new BoardBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.description = document.getDescription();
            $.repositoryName = document.getRepositoryName();

            ArrayList<Pair<User, Board.Role>> contributorsIds = new ArrayList<>();
            for (HashMap<String, String> contributor : document.getContributors()) {
                User user = UserRepository.getInstance().getContributor(contributor.get("id"));
                Board.Role role = Board.Role.valueOf(contributor.get("role"));
                Pair<User, Board.Role> pair = new Pair(user, role);
                contributorsIds.add(pair);
            }
            $.contributors = FXCollections.observableArrayList(contributorsIds);

            if (document.getColumnsIds() != null) {
                ArrayList<Column> columns = new ArrayList<>();
                for (String columnId : document.getColumnsIds()) {
                    columns.add(ColumnRepository.getInstance().get(columnId));
                }
                $.columns = FXCollections.observableArrayList(columns);
            }

            if (document.getTagsIds() != null) {
                ArrayList<Tag> tags = new ArrayList<>();
                for (String tagId : document.getTagsIds()) {
                    tags.add(TagRepository.getInstance().get(tagId));
                }
                $.tags = FXCollections.observableArrayList(tags);
            }

        }).build();

        return board;
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

    public void addContributor(String boardId, String email, Board.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.getInstance().encrypt(email), role);
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "contributors", contributor.getMap());
    }

    public void deleteContributor(String boardId, String email, Board.Role role) {
        ContributorMap contributor = new ContributorMap(FirestoreAPI.getInstance().encrypt(email), role);
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "contributors", contributor.getMap());
    }

    public void addTagId(String boardId, String tagId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "tagsIds", tagId);
    }

    public void deleteTagId(String boardId, String tagId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.getInstance().BOARDS, boardId, "tagsIds", tagId);
    }

    public void delete(Board board) {
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().BOARDS, board.getId());

        for (Column column : board.getColumns()) {
            for (Task task : column.getTasks()) {
                FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().TASKS, task.getId());
            }
            FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.getInstance().COLUMNS, column.getId());
        }
        if (board.getTags() != null) {
            for (Tag tag : board.getTags()) {
                TagRepository.getInstance().delete(tag.getId());
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
