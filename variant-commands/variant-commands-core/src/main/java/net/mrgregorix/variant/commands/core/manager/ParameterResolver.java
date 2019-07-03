package net.mrgregorix.variant.commands.core.manager;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

/**
 * Resolver for parameter values in {@link Command} annotated methods
 */
public interface ParameterResolver
{
    /**
     * Resolves a value for the given parameter
     *
     * @param method method that the value is resolved for
     * @param sender sender that called this command
     * @param result result of parsing the command's arguments
     *
     * @return the resolved value
     */
    Object resolve(RegisteredMethod method, CommandSender sender, ParsingResult result);
}
