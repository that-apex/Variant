package net.mrgregorix.variant.commands.api.parser;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import net.mrgregorix.variant.utils.annotation.Nullable;

public interface TypeDefinition
{
    String getName();

    Class<?> getType();

    Annotation[] getDeclaredAnnotations();

    boolean isRequired();

    @Nullable
    String defaultValue();

    @SuppressWarnings("unchecked")
    default <T extends Annotation> T getAnnotation(final Class<T> type)
    {
        return (T) Arrays.stream(this.getDeclaredAnnotations()).filter(type::isInstance).findFirst().orElse(null);
    }
}
