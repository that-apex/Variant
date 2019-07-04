package net.mrgregorix.variant.commands.api.parser;

/**
 * Parser for parsing strings character by character.
 */
public interface StringParser
{
    /**
     * Returns the full string that was passed to this parser
     *
     * @return the full string that was passed to this parser
     */
    String getFullString();

    /**
     * Returns the current character index that the parser is at, starting from 0
     *
     * @return the current character index that the parser is at, starting from 0
     */
    int getCurrentPosition();

    /**
     * Returns the total amount of characters that the parsed string has.
     *
     * @return the total amount of characters that the parsed string has
     */
    int getLength();

    /**
     * Sets the current character index that the parser is at, starting from 0
     *
     * @param position character index
     */
    void setCurrentPosition(int position);

    /**
     * Returns whether or not the parser has finished parsing.
     *
     * @return whether or not the parsing was finished.
     */
    boolean isFinished();

    /**
     * Returns the next character without modifying the character index
     *
     * @return the next character
     */
    char peekCharacter();

    /**
     * Reads a character and returns it, increments the character index by 1.
     *
     * @return the read character
     */
    char readCharacter();

    /**
     * Returns the next N characters without modifying the character index
     *
     * @param n amount of character to read
     *
     * @return the next characters
     */
    String peekCharacters(int n);

    /**
     * Returns the next characters from current index (inclusive) to the given I index (exclusive)
     *
     * @param i final index, exclusive
     *
     * @return the next characters
     */
    String peekCharactersTo(int i);

    /**
     * Reads and returns the next N characters, sets the current index at the first character after the read string
     *
     * @param n amount of character to read
     *
     * @return the next characters
     */
    String readCharacters(int n);

    /**
     * Reads and returns the next characters from current index (inclusive) to the given I index (exclusive) and sets the current index to I
     *
     * @param i final index, exclusive
     *
     * @return the next characters
     */
    String readCharactersTo(int i);

    /**
     * Increments the current index by 1
     */
    void skip();

    /**
     * Increments the current index by N
     *
     * @param n amount to increment the current index by
     */
    void skip(int n);

    /**
     * Increments the current index until it finds the given character or the parser has finished
     *
     * @param c character to find
     */
    void skipUntil(char c);

    /**
     * Increments the current as long as the current character is the provided character
     *
     * @param c character to skip
     */
    void skipAll(char c);

    /**
     * Searches for the given character after the current index (inclusive) and returns its index.
     *
     * @param c character to search for
     *
     * @return index of the character or {@code -1} if no character is found
     */
    int findFirst(char c);

    /**
     * Reads all character until it finds the given character or the parser finishes and sets the current index after the found string
     *
     * @param c character to search for
     *
     * @return all characters until the character
     */
    String readUntil(char c);
}
