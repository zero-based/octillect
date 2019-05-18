package octillect.models;

import javafx.scene.image.Image;

public class Contributor extends UserBase implements IObserver {

    public enum Role {
        owner,
        admin,
        viewer
    }

    private Role role;

    public Contributor(String id, String name, String email, Image image, Role role) {
        super(id, name, email, image);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void updateObserver() {
        throw new UnsupportedOperationException("Not Implemented Yet!");
    }

    @Override
    public String toString() {
        return getName();
    }

}
