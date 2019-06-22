package net.mrgregorix.variant.commands.core.parser.defaults;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mrgregorix.variant.commands.api.message.HelpPage;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.message.HelpPageImpl;

public class HelpPageTypeParser extends AbstractDefaultTypeParser<HelpPage, HelpPageTypeParser>
{
    private static final HelpPage     PAGE_ONE      = new HelpPageImpl(1);
    private static final List<String> HELP_COMMANDS = Collections.singletonList("help");
    private static final Pattern      PAGE_MATCHER  = Pattern.compile("\\d+");

    private List<String> helpCommands;

    public HelpPageTypeParser()
    {
        this(HELP_COMMANDS);
    }

    public HelpPageTypeParser(final List<String> helpCommands)
    {
        this.helpCommands = helpCommands;
    }

    public List<String> getHelpCommands()
    {
        return this.helpCommands;
    }

    public void setHelpCommands(final List<String> helpCommands)
    {
        this.helpCommands = helpCommands;
    }

    @Override
    public Class<HelpPage> getBaseType()
    {
        return HelpPage.class;
    }

    @Override
    public HelpPage parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition) throws ParsingException
    {
        final String help = parser.readUntil(' ');

        if (PAGE_MATCHER.matcher(help).matches())
        {
            return new HelpPageImpl(Integer.parseInt(help));
        }

        if (! HELP_COMMANDS.contains(help))
        {
            return null;
        }
        parser.skipAll(' ');

        if (parser.isFinished())
        {
            return PAGE_ONE;
        }

        try
        {
            return new HelpPageImpl(argumentParser.getParserFor(int.class).parseType(argumentParser, parser, typeDefinition));
        }
        catch (final ParsingException e)
        {
            return PAGE_ONE;
        }
    }

    @Override
    public HelpPage parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue)
    {
        return PAGE_ONE;
    }
}
