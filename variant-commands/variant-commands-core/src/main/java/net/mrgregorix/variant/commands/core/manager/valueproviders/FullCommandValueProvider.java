package net.mrgregorix.variant.commands.core.manager.valueproviders;

import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.FullCommand;
import net.mrgregorix.variant.commands.api.manager.ValueProvider;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

/**
 * A {@link ValueProvider} for {@link FullCommand}
 */
public class FullCommandValueProvider implements ValueProvider<FullCommand>
{
    @Override
    public Class<FullCommand> getAnnotationType()
    {
        return FullCommand.class;
    }

    @Override
    public void validate(final Parameter parameter, final FullCommand annotation)
    {
        if (parameter.getType() != String.class)
        {
            throw new IllegalArgumentException(parameter + " must be a String");
        }
    }

    @Override
    public Object provideValue(final CommandSender sender, final CommandInfo info, final Parameter parameter, final ParsingResult parsingResult, final FullCommand annotation)
    {
        return parsingResult.getFullCommand();
    }
}
