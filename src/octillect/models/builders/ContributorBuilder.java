package octillect.models.builders;

import java.util.function.Consumer;

import javafx.scene.image.Image;

import octillect.models.Board;
import octillect.models.Contributor;

public class ContributorBuilder implements Builder<Contributor, ContributorBuilder> {

    public String id;
    public String name;
    public String email;
    public Image image;
    public Board.Role role;

    @Override
    public ContributorBuilder with(Consumer<ContributorBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Contributor build() {
        return new Contributor(id, name, email, image, role);
    }

    public ContributorBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ContributorBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ContributorBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ContributorBuilder withImage(Image image) {
        this.image = image;
        return this;
    }

    public ContributorBuilder withRole(Board.Role role) {
        this.role = role;
        return this;
    }

}
