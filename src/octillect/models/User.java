package octillect.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class User extends UserBase {

    private String password;
    private ObservableList<Board> boards = FXCollections.observableArrayList();


    public User(String id, String name, String email, String password,
                Image image, ObservableList<Board> boards) {
        super(id, name, email, image);
        this.password = password;
        this.boards = boards;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public ObservableList<Board> getBoards() {
        return boards;
    }

    public void setBoards(ObservableList<Board> boards) {
        this.boards = boards;
    }

}
