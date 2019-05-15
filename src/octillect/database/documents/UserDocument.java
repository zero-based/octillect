package octillect.database.documents;

import java.util.ArrayList;

public class UserDocument {

    private String id;
    private String name;
    private String email;
    private String password;
    private ArrayList<String> boardsIds = new ArrayList<>();

    public UserDocument() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getBoardsIds() {
        return boardsIds;
    }

    public void setBoardsIds(ArrayList<String> boardsIds) {
        this.boardsIds = boardsIds;
    }
    
}
