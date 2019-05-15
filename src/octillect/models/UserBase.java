package octillect.models;

import javafx.scene.image.Image;

abstract public class UserBase {

    private String id;
    private String name;
    private String email;
    private Image image;


    UserBase(String id, String name, String email, Image image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
    }


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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
