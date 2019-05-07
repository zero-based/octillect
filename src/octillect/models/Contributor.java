package octillect.models;

import javafx.scene.image.Image;

public class Contributor extends UserBase {

    private Board.Role role;

    public Contributor(String id, String name, String email, Image image, Board.Role role) {
        super(id, name, email, image);
        this.role = role;
    }

    public Board.Role getRole() {
        return role;
    }

    public void setRole(Board.Role role) {
        this.role = role;
    }

}
