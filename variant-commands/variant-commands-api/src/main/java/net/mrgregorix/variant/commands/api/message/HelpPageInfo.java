package net.mrgregorix.variant.commands.api.message;

public interface HelpPageInfo
{
    int getCurrent();

    int getMax();

    boolean hasPrevious();

    boolean hasNext();

    int getPrevious();

    int getNext();

    String getHelpCommand();
}
