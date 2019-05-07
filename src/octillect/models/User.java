package octillect.models;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class User extends UserBase implements IObserver {

    private String password;
    private ObservableList<Board> boards;


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


    @Override
    public void updateObserver() {
        /* UPDATE USER */
    }

}
