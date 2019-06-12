package octillect.database.repositories;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;

import octillect.database.documents.BoardDocument;
import octillect.database.documents.BoardDocument.CollaboratorMap;
import octillect.database.documents.UserDocument;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.*;
import octillect.models.builders.BoardBuilder;
import octillect.models.builders.CollaboratorBuilder;

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

        ArrayList<HashMap<String, String>> collaboratorsIds = new ArrayList<>();
        for (Collaborator collaborator : board.getCollaborators()) {
            CollaboratorMap collaboratorMap = new CollaboratorMap(collaborator.getId(), collaborator.getRole());
            collaboratorsIds.add(collaboratorMap.getMap());
        }
        document.setCollaborators(collaboratorsIds);

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

        FirestoreAPI.getInstance().insertDocument(FirestoreAPI.BOARDS, document.getId(), document);
    }

    @Override
    public Board get(String boardId) {

        BoardDocument document = FirestoreAPI.getInstance().selectDocument(FirestoreAPI.BOARDS, boardId).toObject(BoardDocument.class);

        Board board = new BoardBuilder().with($ -> {
            $.id = document.getId();
            $.name = document.getName();
            $.description = document.getDescription();
            $.repositoryName = document.getRepositoryName();

            ArrayList<Collaborator> collaboratorsIds = new ArrayList<>();
            for (HashMap<String, String> collaboratorMap : document.getCollaborators()) {
                Collaborator collaborator = getCollaborator(collaboratorMap.get("id"));
                collaborator.setRole(Collaborator.Role.valueOf(collaboratorMap.get("role")));
                collaboratorsIds.add(collaborator);
            }
            $.collaborators = FXCollections.observableArrayList(collaboratorsIds);

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
        FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.BOARDS, board.getId());

        for (Column column : board.<Column>getChildren()) {
            for (Task task : column.<Task>getChildren()) {
                FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.TASKS, task.getId());
            }
            FirestoreAPI.getInstance().deleteDocument(FirestoreAPI.COLUMNS, column.getId());
        }
        for (Tag tag : board.getTags()) {
            TagRepository.getInstance().delete(tag);
        }
    }

    public void addColumnId(String boardId, String columnId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.BOARDS, boardId, "columnsIds", columnId);
    }

    public void updateColumnsIds(String boardId, ArrayList<String> columnsIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.BOARDS, boardId, "columnsIds", columnsIds);
    }

    public void updateName(String boardId, String name) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.BOARDS, boardId, "name", name);
    }

    public void updateDescription(String boardId, String description) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.BOARDS, boardId, "description", description);
    }

    public void updateRepositoryName(String boardId, String repositoryName) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.BOARDS, boardId, "repositoryName", repositoryName);
    }

    public void addCollaborator(String boardId, Collaborator collaborator) {
        CollaboratorMap collaboratorMap = new CollaboratorMap(collaborator.getId(), collaborator.getRole());
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.BOARDS, boardId, "collaborators", collaboratorMap.getMap());
    }

    public Collaborator getCollaborator(String collaboratorId) {

        Collaborator collaborator = null;
        UserDocument document;
        document = FirestoreAPI.getInstance().selectDocument(FirestoreAPI.USERS, collaboratorId).toObject(UserDocument.class);

        if (document != null) {
            collaborator = new CollaboratorBuilder().with($ -> {
                $.id = document.getId();
                $.name = document.getName();
                $.email = document.getEmail();
                $.image = UserRepository.getInstance().getImage(document.getId());
            }).build();
        }

        return collaborator;
    }

    public void updateCollaboratorsIds(String boardId, ArrayList<HashMap<String, String>> collaboratorsIds) {
        FirestoreAPI.getInstance().updateAttribute(FirestoreAPI.BOARDS, boardId, "collaborators", collaboratorsIds);
    }

    public void deleteCollaborator(Board board, Collaborator collaborator) {
        CollaboratorMap collaboratorMap = new CollaboratorMap(collaborator.getId(), collaborator.getRole());
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.BOARDS, board.getId(), "collaborators", collaboratorMap.getMap());

        for (Column column : board.<Column>getChildren()) {
            for (Task task : column.<Task>getChildren()) {
                for (Collaborator assignee : task.getAssignees()) {
                    if (assignee.getId().equals(collaborator.getId())) {
                        TaskRepository.getInstance().deleteAssigneeId(task.getId(), assignee.getId());
                        break;
                    }
                }
            }
        }
    }

    public void addTagId(String boardId, String tagId) {
        FirestoreAPI.getInstance().appendArrayElement(FirestoreAPI.BOARDS, boardId, "tagsIds", tagId);
    }

    public void deleteTag(Board board, String tagId) {
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.BOARDS, board.getId(), "tagsIds", tagId);

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
        FirestoreAPI.getInstance().deleteArrayElement(FirestoreAPI.BOARDS, boardId, "columnsIds", columnId);
    }

}
