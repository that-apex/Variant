package net.mrgregorix.variant.commands.core.parser.result;

import java.util.Map;

import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

/**
 * A simple implementation of {@link ParsingResult}
 */
public class ParsingResultImpl implements ParsingResult
{
    private final String                      fullCommand;
    private final Object[]                    parameters;
    private final Map<TypeDefinition, Object> flags;

    /**
     * Creates a new ParsingResultImpl
     *
     * @param fullCommand the full command string that was used for {@link ArgumentParser}
     * @param parameters  the parsed parameters
     * @param flags       the parsed flags
     */
    public ParsingResultImpl(final String fullCommand, final Object[] parameters, final Map<TypeDefinition, Object> flags)
    {
        this.fullCommand = fullCommand;
        this.parameters = parameters;
        this.flags = flags;
    }

    @Override
    public String getFullCommand()
    {
        return this.fullCommand;
    }

    @Override
    public Object[] getParameters()
    {
        return this.parameters;
    }

    @Override
    public Map<TypeDefinition, Object> getFlags()
    {
        return this.flags;
    }
}
