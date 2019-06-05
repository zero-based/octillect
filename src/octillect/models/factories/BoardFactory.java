package octillect.models.factories;

import javafx.collections.FXCollections;

import octillect.database.firebase.FirestoreAPI;
import octillect.models.Board;
import octillect.models.Contributor;
import octillect.models.User;
import octillect.models.builders.ContributorBuilder;

public class BoardFactory {

    public static Board createBoard(BoardTemplate template, User user, String name, String description) {

        Contributor owner = new ContributorBuilder().with($ -> {
            $.id    = user.getId();
            $.name  = user.getName();
            $.email = user.getEmail();
            $.image = user.getImage();
            $.role  = Contributor.Role.owner;
        }).build();

        Board board = new Board();

        if (template == BoardTemplate.WELCOME) {
            board = new WelcomeBoard(owner);
        } else if (template == BoardTemplate.DEFAULT) {
            board = new DefaultBoard(owner);
        }

        board.setId(FirestoreAPI.getInstance().encryptWithDateTime(name + owner.getId()));
        board.setName(name);
        board.setDescription(description);
        board.setContributors(FXCollections.observableArrayList(owner));

        return board;

    }

}
