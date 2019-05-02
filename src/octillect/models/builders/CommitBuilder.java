package octillect.models.builders;

import octillect.models.Commit;
import java.util.Date;
import java.util.function.Consumer;

public class CommitBuilder implements Builder<Commit, CommitBuilder> {

    public String committerName;
    public Date date;
    public String message;
    public String url;
    public String body;

    @Override
    public CommitBuilder with(Consumer<CommitBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Commit build() {
        return new Commit(committerName, date, message, url, body);
    }

    public CommitBuilder withCommitterName(String committerName) {
        this.committerName = committerName;
        return this;
    }

    public CommitBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public CommitBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public CommitBuilder withURL(String url) {
        this.url = url;
        return this;
    }

    public CommitBuilder withBody(String body) {
        this.body = body;
        return this;
    }
}
