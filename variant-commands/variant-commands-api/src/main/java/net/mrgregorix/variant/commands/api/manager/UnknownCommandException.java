package net.mrgregorix.variant.commands.api.manager;

public class UnknownCommandException extends RuntimeException
{
    private final String command;

    public UnknownCommandException(final String command)
    {
        super("Couldn't find command matching " + command);
        this.command = command;
    }

    public String getCommand()
    {
        return this.command;
    }
}
