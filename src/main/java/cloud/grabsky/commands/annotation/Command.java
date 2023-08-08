package cloud.grabsky.commands.annotation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    @NotNull String name();

    @Nullable String[] aliases() default "";

    @Nullable String permission() default "";

    @Nullable String usage() default "";

    @Nullable String description() default "";

}
