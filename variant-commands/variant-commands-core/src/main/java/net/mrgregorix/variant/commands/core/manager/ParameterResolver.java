package net.mrgregorix.variant.commands.core.manager;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;

public interface ParameterResolver
{
    Object resolve(RegisteredMethod method, CommandSender sender, ParsingResult result);
}
