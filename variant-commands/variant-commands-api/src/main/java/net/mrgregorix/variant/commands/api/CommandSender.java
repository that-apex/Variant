package net.mrgregorix.variant.commands.api;

/**
 * A named entity capable of executing commands and receiving messages.
 */
public interface CommandSender
{
    /**
     * Returns the name of this sender
     *
     * @return name of this sender
     */
    String getName();

    /**
     * Sends a message to this sender
     *
     * @param message message
     */
    void sendMessage(String message);
}
