package net.mrgregorix.variant.commands.api.manager;

/**
 * Thrown when a requested command is not found.
 */
public class UnknownCommandException extends RuntimeException
{
    private final String command;

    /**
     * Creates a new UnknownCommandException
     *
     * @param command the command that was requested
     */
    public UnknownCommandException(final String command)
    {
        super("Couldn't find command matching " + command);
        this.command = command;
    }

    /**
     * Returns the command that was requested
     *
     * @return the command that was requested
     */
    public String getCommand()
    {
        return this.command;
    }
}
