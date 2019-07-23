package net.mrgregorix.variant.commands.core.parser.defaults;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import net.mrgregorix.variant.commands.api.message.HelpPage;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.message.HelpPageImpl;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A {@link TypeParser} for {@link HelpPage} object
 */
public class HelpPageTypeParser extends AbstractDefaultTypeParser<HelpPage, HelpPageTypeParser>
{
    private static final HelpPage     PAGE_ONE      = new HelpPageImpl(1);
    private static final List<String> HELP_COMMANDS = Collections.singletonList("help");
    private static final Pattern      PAGE_MATCHER  = Pattern.compile("\\d+");

    private List<String> helpCommands;

    /**
     * Create a new HelpPageTypeParser with the default help command
     */
    public HelpPageTypeParser()
    {
        this(HELP_COMMANDS);
    }

    /**
     * Create a new HelpPageTypeParser with the provided help commands
     *
     * @param helpCommands help commands to use
     */
    public HelpPageTypeParser(final List<String> helpCommands)
    {
        this.helpCommands = helpCommands;
    }

    /**
     * Returns a list containing all help commands.
     *
     * @return a list containing all help commands
     */
    public List<String> getHelpCommands()
    {
        return this.helpCommands;
    }

    /**
     * Sets the list containing all help commands.
     *
     * @param helpCommands list containing all help commands
     */
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
    @Nullable
    public HelpPage parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition)
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
            final Integer page = argumentParser.getParserFor(int.class).parseType(argumentParser, parser, typeDefinition);
            return page == null ? PAGE_ONE : new HelpPageImpl(page);
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
