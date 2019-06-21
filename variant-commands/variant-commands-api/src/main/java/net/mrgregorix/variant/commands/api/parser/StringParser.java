package net.mrgregorix.variant.commands.api.parser;

public interface StringParser
{
    String getFullCommand();

    int getCurrentPosition();

    int getLength();

    void setCurrentPosition(int position);

    boolean isFinished();

    char peekCharacter();

    char readCharacter();

    String peekCharacters(int n);

    String peekCharactersTo(int i);

    String readCharacters(int n);

    String readCharactersTo(int i);

    void skip();

    void skip(int n);

    void skipUntil(char c);

    void skipAll(char c);

    int findFirst(char c);

    String readUntil(char c);
}
