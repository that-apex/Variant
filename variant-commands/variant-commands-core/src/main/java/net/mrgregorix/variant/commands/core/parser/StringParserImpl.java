package net.mrgregorix.variant.commands.core.parser;

import com.google.common.base.Preconditions;
import net.mrgregorix.variant.commands.api.parser.StringParser;

public class StringParserImpl implements StringParser
{
    private final String string;
    private       int    currentPosition;

    public StringParserImpl(final String string)
    {
        this.string = string;
    }

    @Override
    public String getFullCommand()
    {
        return this.string;
    }

    @Override
    public int getCurrentPosition()
    {
        return this.currentPosition;
    }

    @Override
    public int getLength()
    {
        return this.string.length();
    }

    @Override
    public void setCurrentPosition(final int position)
    {
        this.currentPosition = position;
    }

    @Override
    public boolean isFinished()
    {
        return this.currentPosition == this.string.length();
    }

    @Override
    public char peekCharacter()
    {
        return this.string.charAt(this.index(this.currentPosition));
    }

    @Override
    public char readCharacter()
    {
        return this.string.charAt(this.index(this.currentPosition++));
    }

    @Override
    public String peekCharacters(final int n)
    {
        Preconditions.checkArgument(n >= 0, "n cannot be negative");
        if (n == 0)
        {
            return "";
        }

        final int startIndex = this.index(this.currentPosition);
        final int endIndex = this.index(this.currentPosition + n - 1);
        return this.string.substring(startIndex, endIndex);
    }

    @Override
    public String peekCharactersTo(final int n)
    {
        Preconditions.checkArgument(n >= 0, "n cannot be negative");
        this.index(n - 1);

        return this.string.substring(this.currentPosition, n);
    }

    @Override
    public String readCharacters(final int n)
    {
        final String string = this.peekCharacters(n);
        this.currentPosition = this.index(this.currentPosition + string.length());
        return string;
    }

    @Override
    public String readCharactersTo(final int i)
    {
        final String string = this.peekCharactersTo(i);
        this.currentPosition = this.currentPosition + string.length();
        return string;
    }

    @Override
    public void skip()
    {
        this.skip(1);
    }

    @Override
    public void skip(final int n)
    {
        this.currentPosition = this.index(this.currentPosition + n);
    }

    @Override
    public void skipUntil(final char c)
    {
        while (this.currentPosition < this.string.length() && this.string.charAt(this.currentPosition) != c)
        {
            this.currentPosition++;
        }
    }

    @Override
    public void skipAll(final char c)
    {
        while (this.currentPosition < this.string.length() && this.string.charAt(this.currentPosition) == c)
        {
            this.currentPosition++;
        }
    }

    @Override
    public int findFirst(final char c)
    {
        int i = this.currentPosition;

        while (i < this.string.length())
        {
            if (this.string.charAt(i) == c)
            {
                return i;
            }

            i++;
        }

        return - 1;
    }

    @Override
    public String readUntil(final char c)
    {
        final int index = this.findFirst(c);

        if (index == - 1)
        {
            return this.readCharactersTo(this.string.length());
        }

        return this.readCharactersTo(index);
    }

    private int index(int i)
    {
        if (i < 0 || i >= this.string.length())
        {
            throw new IllegalArgumentException("index " + i + " out of range: " + this.string.length());
        }

        return i;
    }
}
