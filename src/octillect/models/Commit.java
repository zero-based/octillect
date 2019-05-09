package octillect.models;

import java.net.URI;
import java.util.Date;

import javafx.scene.image.Image;

public class Commit {

    private URI url;
    private String subject;
    private String body;
    private String authorUsername;
    private Image authorAvatar;
    private Date date;

    public Commit(URI url, String subject, String body, String authorUsername, Image authorAvatar, Date date) {
        this.url = url;
        this.subject = subject;
        this.body = body;
        this.authorUsername = authorUsername;
        this.authorAvatar = authorAvatar;
        this.date = date;
    }


    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }


    public Image getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(Image authorAvatar) {
        this.authorAvatar = authorAvatar;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
