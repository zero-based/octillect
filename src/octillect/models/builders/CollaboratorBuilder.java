package octillect.models.builders;

import java.util.function.Consumer;

import javafx.scene.image.Image;

import octillect.models.Collaborator;

public class CollaboratorBuilder implements Builder<Collaborator, CollaboratorBuilder> {

    public String id;
    public String name;
    public String email;
    public Image image;
    public Collaborator.Role role;

    @Override
    public CollaboratorBuilder with(Consumer<CollaboratorBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Collaborator build() {
        return new Collaborator(id, name, email, image, role);
    }

    public CollaboratorBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CollaboratorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CollaboratorBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CollaboratorBuilder withImage(Image image) {
        this.image = image;
        return this;
    }

    public CollaboratorBuilder withRole(Collaborator.Role role) {
        this.role = role;
        return this;
    }

}
