package net.mrgregorix.variant.commands.core.manager.argumentresolvers;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.core.manager.ParameterResolver;
import net.mrgregorix.variant.commands.core.manager.RegisteredMethod;

/**
 * A {@link ParameterResolver} for command flags.
 */
public class FlagParameterResolver implements ParameterResolver
{
    private final TypeDefinition flagDefinition;

    /**
     * Creates a new FlagParameterResolver
     *
     * @param flagDefinition definition of the flag that this resolver resolves
     */
    public FlagParameterResolver(final TypeDefinition flagDefinition)
    {
        this.flagDefinition = flagDefinition;
    }

    @Override
    public Object resolve(final RegisteredMethod method, final CommandSender sender, final ParsingResult result)
    {
        return result.getFlags().get(this.flagDefinition);
    }
}
