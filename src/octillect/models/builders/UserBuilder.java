package octillect.models.builders;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import octillect.models.Board;
import octillect.models.User;

import java.util.function.Consumer;

public class UserBuilder implements Builder<User, UserBuilder> {

    public String id;
    public String name;
    public String email;
    public String password;
    public Image image;
    public ObservableList<Board> boards;

    @Override
    public UserBuilder with(Consumer<UserBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public User build() {
        return new User(id, name, email, password, image, boards);
    }

    public UserBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withImage(Image image) {
        this.image = image;
        return this;
    }

    public UserBuilder withBoards(ObservableList<Board> boards) {
        this.boards = boards;
        return this;
    }

}
