package net.mrgregorix.variant.commands.api.parser;

import java.util.Map;

/**
 * Result of a {@link ArgumentParser#parse(TypeDefinition[], TypeDefinition[], String)}
 */
public interface ParsingResult
{
    /**
     * Returns the full string that was passed to the parser.
     *
     * @return the full string that was passed to the parser
     */
    String getFullCommand();

    /**
     * Returns an array of all the parameters in the same order as the provided parameters definitions.
     *
     * @return an array of all the parameters in the same order as the provided parameters definitions
     */
    Object[] getParameters();

    /**
     * Returns the values for ALL the flags whose definitions were provided.
     * <p>If the user did not specify any value for the flag, the default value is put to this map.</p>
     *
     * @return the values for ALL the flags whose definitions were provided
     */
    Map<TypeDefinition, Object> getFlags();
}
