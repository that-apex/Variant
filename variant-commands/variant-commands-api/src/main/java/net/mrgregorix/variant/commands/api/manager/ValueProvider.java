package net.mrgregorix.variant.commands.api.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

/**
 * Provider for {@link ParameterDescription} annotation valuesl
 *
 * @param <T> an annotation annotated with {@link ParameterDescription}
 */
public interface ValueProvider <T extends Annotation>
{
    /**
     * An annotation that this value provider requires for parameters that it will handle.
     * <p>Should always just be {@code T.class} where T is the type parameter T</p>
     *
     * @return annotation that this value provider requires for parameters that it will handle.
     */
    Class<T> getAnnotationType();

    /**
     * Validates that the given parameter meets all {@link ValueProvider}'s requirements
     *
     * @param parameter  parameter to validate
     * @param annotation annotation T declared on this parameter
     */
    void validate(Parameter parameter, T annotation);

    /**
     * Provides a value for a method parameter
     *
     * @param sender        sender that executed this command
     * @param info          information about the executed command
     * @param parameter     the parameter that the value is requested for
     * @param parsingResult result of parsing the command arguments
     * @param annotation    the annotation T declared of this parameter
     *
     * @return the provided value
     */
    Object provideValue(CommandSender sender, CommandInfo info, Parameter parameter, ParsingResult parsingResult, T annotation);
}
