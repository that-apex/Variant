package net.mrgregorix.variant.commands.core.manager.valueproviders;

import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Sender;
import net.mrgregorix.variant.commands.api.manager.ValueProvider;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

/**
 * A {@link ValueProvider} for {@link CommandSender}
 */
public class SenderValueProvider implements ValueProvider<Sender>
{
    @Override
    public Class<Sender> getAnnotationType()
    {
        return Sender.class;
    }

    @Override
    public void validate(final Parameter parameter, final Sender annotation)
    {
        if (! CommandSender.class.isAssignableFrom(parameter.getType()))
        {
            throw new IllegalArgumentException(parameter + " must be a subclass of CommandSender");
        }
    }

    @Override
    public Object provideValue(final CommandSender sender, final CommandInfo info, final Parameter parameter, final ParsingResult parsingResult, final Sender annotation)
    {
        return sender;
    }
}
