package octillect.models.builders;

import java.util.function.Consumer;

import javafx.scene.paint.Color;

import octillect.models.Label;

public class LabelBuilder implements Builder<Label, LabelBuilder> {

    public String name;
    public Color color;

    @Override
    public LabelBuilder with(Consumer<LabelBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public Label build() {
        return new Label(name, color);
    }

    public LabelBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LabelBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

}
