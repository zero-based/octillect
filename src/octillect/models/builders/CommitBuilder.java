package octillect.models.builders;

import octillect.models.Commit;
import java.util.Date;
import java.util.function.Consumer;

public class CommitBuilder implements Builder<Commit, CommitBuilder> {

    public String url;
    public String subject;
    public String body;
    public String authorName;
    public Date date;

    @Override
    public CommitBuilder with(Consumer<CommitBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Commit build() {
        return new Commit(url, subject, body, authorName, date);
    }

    public CommitBuilder withURL(String url) {
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

    public CommitBuilder withAuthorName(String committerName) {
        this.authorName = committerName;
        return this;
    }

    public CommitBuilder withDate(Date date) {
        this.date = date;
        return this;
    }
}
