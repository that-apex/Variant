package net.mrgregorix.variant.commands.api.parser;

import java.util.Map;

public interface ParsingResult
{
    String getFullCommand();

    Object[] getParameters();

    Map<TypeDefinition, Object> getFlags();
}
