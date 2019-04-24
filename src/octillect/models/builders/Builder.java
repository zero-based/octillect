package octillect.models.builders;

import java.util.function.Consumer;

/**
 * Add the functionality to use advanced builder design pattern
 * by passing parameters through lambda expression from the
 * {@code with()} method and building the Model class object using
 * {@code build()} method.<br>
 *
 * The Builder class can be used as follows:
 * <pre>
 *     Class class = new BuilderClass().with($ -> {
 *         $.field_1 = value;
 *         $.field_2 = value;
 *     }).build();
 * </pre>
 *
 * @param <Class> The original class that needs a builder.
 * @param <BuilderClass> the Builder Class itself.
 */
interface Builder<Class, BuilderClass> {
    BuilderClass with(Consumer<BuilderClass> builderFunction);
    Class build();
}
