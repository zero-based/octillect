package octillect.models;

import javafx.scene.image.Image;

public class Admin extends User {

    boolean canEdit;

    public Admin(String id, String name, String email, String password, Image image) {
        super(id, name, email, password, image);
        this.canEdit = true;
    }

}
