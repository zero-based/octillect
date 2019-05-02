package octillect.models;

import java.util.Date;

public class Commit {

    private String authorName;
    private Date date;
    private String message;
    private String url;
    private String body;

    public Commit(String committerName, Date date, String message, String url, String body) {
        this.authorName = authorName;
        this.date = date;
        this.message = message;
        this.url = url;
        this.body = body;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
