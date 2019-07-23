package net.mrgregorix.variant.commands.core;

import java.lang.annotation.Annotation;
import java.util.List;

import net.mrgregorix.variant.commands.api.annotation.types.CollectionType;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.parser.ArgumentParserImpl;
import net.mrgregorix.variant.commands.core.parser.StringParserImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestParser
{
    @Test
    public void testStringParser()
    {
        final StringParser stringParser = new StringParserImpl("Hello this is test. 'inline string'");

        assertThat("Character read failed", stringParser.readCharacter(), equalTo('H'));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo('e'));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo('l'));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo('l'));
        stringParser.skip();
        assertThat("Skip failed", stringParser.readCharacter(), equalTo(' '));
        stringParser.skip(3);
        assertThat("Skip failed", stringParser.readCharacter(), equalTo('s'));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo(' '));
        stringParser.skipUntil(' ');
        assertThat("skipUntil failed", stringParser.readCharacter(), equalTo(' '));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo('t'));
        stringParser.skipUntil('.');
        assertThat("skipUntil failed", stringParser.readCharacter(), equalTo('.'));
        stringParser.skipUntil('\'');
        assertThat("skipUntil failed", stringParser.readCharacter(), equalTo('\''));
        assertThat("readUntil failed", stringParser.readUntil('\''), equalTo("inline string"));
        assertThat("Character read failed", stringParser.readCharacter(), equalTo('\''));

        assertThrows(IllegalArgumentException.class, stringParser::readCharacter);
    }

    @SuppressWarnings({"unchecked", "MagicNumber"})
    @Test
    public void testCommandParser() throws ParsingException
    {
        final ArgumentParser argumentParser = new ArgumentParserImpl();

        final TypeDefinition[] typeDefinitions = {
            new DummyTypeDefinition("arg0", int.class, new Annotation[0]),
            new DummyTypeDefinition("arg1", float.class, new Annotation[0]),
            new DummyTypeDefinition("arg2", String.class, new Annotation[0]),
            new DummyTypeDefinition("arg3", String.class, new Annotation[0]),
            new DummyTypeDefinition("arg4", List.class, new Annotation[] {this.collectionType(String.class)})
        };

        final DummyTypeDefinition stringsFlag = new DummyTypeDefinition("strings", String.class, new Annotation[0]);
        final DummyTypeDefinition listFlag = new DummyTypeDefinition("list", List.class, new Annotation[] {this.collectionType(float.class)});
        final DummyTypeDefinition testFlag = new DummyTypeDefinition("test", char.class, new Annotation[0], "h");
        final DummyTypeDefinition boolFlag = new DummyTypeDefinition("enableBlabla", boolean.class, new Annotation[0], "false");

        final TypeDefinition[] flagDefinitions = {
            stringsFlag,
            listFlag,
            testFlag,
            boolFlag
        };

        final ParsingResult result = argumentParser.parse(typeDefinitions, flagDefinitions, "-strings hellop\"omello -enableBlabla -list 3.6,15000 152 74.345 \"hello\\\" world\" normal_string_no_quotes \"this\",\"is\",\"list\"");

        assertThat("invalid size", result.getParameters().length, equalTo(5));
        assertThat("invalid parameter type", result.getParameters()[0], instanceOf(int.class));
        assertThat("invalid parameter type", result.getParameters()[1], instanceOf(float.class));
        assertThat("invalid parameter type", result.getParameters()[2], instanceOf(String.class));
        assertThat("invalid parameter type", result.getParameters()[3], instanceOf(String.class));
        assertThat("invalid parameter type", result.getParameters()[4], instanceOf(List.class));

        assertThat("invalid parameter value", result.getParameters()[0], equalTo(152));
        assertThat("invalid parameter value", result.getParameters()[1], equalTo(74.345f));
        assertThat("invalid parameter value", result.getParameters()[2], equalTo("hello\" world"));
        assertThat("invalid parameter value", result.getParameters()[3], equalTo("normal_string_no_quotes"));
        assertThat("invalid parameter value", (List<String>) result.getParameters()[4], contains("this", "is", "list"));

        assertThat("invalid flags size", result.getFlags(), aMapWithSize(4));
        assertThat("no flag found", result.getFlags(), hasKey(stringsFlag));
        assertThat("no flag found", result.getFlags(), hasKey(listFlag));
        assertThat("no flag found", result.getFlags(), hasKey(testFlag));
        assertThat("no flag found", result.getFlags(), hasKey(boolFlag));

        assertThat("invalid flag type", result.getFlags().get(stringsFlag), instanceOf(String.class));
        assertThat("invalid flag type", result.getFlags().get(listFlag), instanceOf(List.class));
        assertThat("invalid flag type", result.getFlags().get(testFlag), instanceOf(char.class));
        assertThat("invalid flag type", result.getFlags().get(boolFlag), instanceOf(boolean.class));

        assertThat("invalid flag value", result.getFlags().get(stringsFlag), equalTo("hellop\"omello"));
        assertThat("invalid flag value", (List<Float>) result.getFlags().get(listFlag), contains(3.6f, 15000f));
        assertThat("invalid flag value", result.getFlags().get(testFlag), equalTo('h'));
        assertThat("invalid flag value", result.getFlags().get(boolFlag), is(true));
    }

    private Annotation collectionType(final Class<?> type)
    {
        return new CollectionType()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return CollectionType.class;
            }

            @Override
            public Class<?> value()
            {
                return type;
            }
        };
    }
}
