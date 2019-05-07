package octillect.models.builders;

import java.util.function.Consumer;

import javafx.scene.paint.Color;

import octillect.models.Tag;

public class TagBuilder implements Builder<Tag, TagBuilder> {

    public String id;
    public String name;
    public Color color;

    @Override
    public TagBuilder with(Consumer<TagBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Tag build() {
        return new Tag(id, name, color);
    }

    public TagBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TagBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TagBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

}
