package net.mrgregorix.variant.commands.core.manager.valueproviders;

import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Info;
import net.mrgregorix.variant.commands.api.manager.ValueProvider;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

/**
 * A {@link ValueProvider} for {@link CommandInfo}
 */
public class CommandInfoValueProvider implements ValueProvider<Info>
{
    @Override
    public Class<Info> getAnnotationType()
    {
        return Info.class;
    }

    @Override
    public void validate(final Parameter parameter, final Info annotation)
    {
        if (! CommandInfo.class.isAssignableFrom(parameter.getType()))
        {
            throw new IllegalArgumentException(parameter + " must be a subclass of CommandInfo");
        }
    }

    @Override
    public Object provideValue(final CommandSender sender, final CommandInfo info, final Parameter parameter, final ParsingResult parsingResult, final Info annotation)
    {
        return info;
    }
}
