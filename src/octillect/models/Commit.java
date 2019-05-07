package octillect.models;

import java.util.Date;

public class Commit {

    public String url;
    public String subject;
    public String body;
    public String authorName;
    public Date date;


    public Commit(String url, String subject, String body, String authorName, Date date) {
        this.url = url;
        this.subject = subject;
        this.body = body;
        this.authorName = authorName;
        this.date = date;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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

}
