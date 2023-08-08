package cloud.grabsky.commands.util;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.lang.reflect.Field;

@Internal
public final class Reflections {

    public static <T> void setInstanceField(final Object instance, final Field field, final T value) throws IllegalAccessException {
        // Making field accessible.
        field.setAccessible(true);
        // Setting the value.
        field.set(instance, value);
    }

}
