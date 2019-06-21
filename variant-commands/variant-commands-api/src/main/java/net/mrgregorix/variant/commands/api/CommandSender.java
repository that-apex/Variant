package net.mrgregorix.variant.commands.api;

public interface CommandSender
{
    String getName();

    void sendMessage(String message);
}
