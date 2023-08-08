package cloud.grabsky.commands.condition;

import cloud.grabsky.commands.RootCommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Condition extends Predicate<RootCommandContext>, Consumer<RootCommandContext> {

    @Override
    default void accept(final @NotNull RootCommandContext context) {
        // Do nothing.
    }

}
