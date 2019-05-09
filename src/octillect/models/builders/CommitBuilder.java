package octillect.models.builders;

import java.net.URI;
import java.util.Date;
import java.util.function.Consumer;

import javafx.scene.image.Image;

import octillect.models.Commit;

public class CommitBuilder implements Builder<Commit, CommitBuilder> {

    public URI url;
    public String subject;
    public String body;
    public String authorUsername;
    public Image authorAvatar;
    public Date date;

    @Override
    public CommitBuilder with(Consumer<CommitBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Commit build() {
        return new Commit(url, subject, body, authorUsername, authorAvatar, date);
    }


    public CommitBuilder withURL(URI url) {
        this.url = url;
        return this;
    }

    public CommitBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public CommitBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public CommitBuilder withAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
        return this;
    }

    public CommitBuilder withAuthorAvatar(Image authorAvatar) {
        this.authorAvatar = authorAvatar;
        return this;
    }

    public CommitBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

}
