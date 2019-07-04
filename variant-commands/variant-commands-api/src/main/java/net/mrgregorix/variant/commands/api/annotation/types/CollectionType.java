package net.mrgregorix.variant.commands.api.annotation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;

/**
 * Specifies the type of elements in a {@link Collection} method parameter.
 * <p>For example:
 * <code>
 * {@literal @}Command(...) public void command({@literal @}Argument {@literal @}CollectionType(String.class) List&lt;String&gt; list) {...}
 * </code>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ForType(Collection.class)
public @interface CollectionType
{
    Class<?> value();
}
