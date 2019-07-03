package net.mrgregorix.variant.commands.api.parser;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Definition of a parsable type
 *
 * @see ArgumentParser
 */
public interface TypeDefinition
{
    /**
     * Returns name of the definition
     * <p>must be unique in the context of a single command if the definition is a flag definition/p>
     *
     * @return name fo the definition
     */
    String getName();

    /**
     * Returns the type of this definition.
     *
     * @return type of this definition
     */
    Class<?> getType();

    /**
     * Returns all annotations that are declared on this definition
     *
     * @return all annotations that are declared on this definition
     */
    Annotation[] getDeclaredAnnotations();

    /**
     * Returns whether or a value must be provided for this definition
     *
     * @return whether or not the definition is required
     */
    boolean isRequired();

    /**
     * Returns the default value for the definition, may be {@code null} if the definition has no default value
     *
     * @return the default value for the definition, or {@code null} if none
     */
    @Nullable
    String defaultValue();

    /**
     * Gets the annotation of the given type from this definition
     *
     * @param type type of the annotation
     * @param <T>  type of the annotation
     *
     * @return the found annotation, or {@code null} if none
     */
    @SuppressWarnings("unchecked")
    @Nullable
    default <T extends Annotation> T getAnnotation(final Class<T> type)
    {
        return (T) Arrays.stream(this.getDeclaredAnnotations()).filter(type::isInstance).findFirst().orElse(null);
    }
}
