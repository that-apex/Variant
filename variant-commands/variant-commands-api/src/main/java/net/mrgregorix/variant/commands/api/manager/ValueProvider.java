package net.mrgregorix.variant.commands.api.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

public interface ValueProvider <T extends Annotation>
{
    Class<T> getAnnotationType();

    void validate(Parameter parameter, T annotation);

    Object provideValue(CommandSender sender, CommandInfo info, Parameter parameter, ParsingResult parsingResult, T annotation);
}
