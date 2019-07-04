package net.mrgregorix.variant.commands.core.manager.argumentresolvers;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.core.manager.ParameterResolver;
import net.mrgregorix.variant.commands.core.manager.RegisteredMethod;

/**
 * A {@link ParameterResolver} for command arguments.
 */
public class ArgumentParameterResolver implements ParameterResolver
{
    private final int argumentNumber;

    /**
     * Creates a new ArgumentParameterResolver
     *
     * @param argumentNumber index of the argument that this resolver resolves
     */
    public ArgumentParameterResolver(final int argumentNumber)
    {
        this.argumentNumber = argumentNumber;
    }

    @Override
    public Object resolve(final RegisteredMethod method, final CommandSender sender, final ParsingResult result)
    {
        return result.getParameters()[this.argumentNumber];
    }
}
