package octillect.database.documents;

import java.util.ArrayList;

public class UserDocument {

    String id;
    String name;
    String email;
    String password;
    ArrayList<String> projectsIds;

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

    public ArrayList<String> getProjectsIds() {
        return projectsIds;
    }

    public void setProjectsIds(ArrayList<String> projectsIds) {
        this.projectsIds = projectsIds;
    }
    
}
