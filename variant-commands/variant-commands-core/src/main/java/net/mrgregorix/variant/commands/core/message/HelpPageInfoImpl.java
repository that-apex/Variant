package net.mrgregorix.variant.commands.core.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.message.HelpPageInfo;

/**
 * Default implementation of the {@link HelpPageInfo}
 */
public class HelpPageInfoImpl implements HelpPageInfo
{
    private final Collection<CommandInfo> commands;
    private final int                     currentPage;
    private final int                     pageSize;
    private final String                  helpCommand;

    /**
     * Creates a new HelpPageInfoImpl
     *
     * @param commands    all the commands in the help
     * @param currentPage the current page number
     * @param pageSize    size of a single page
     * @param helpCommand the command that is used to view the help page
     */
    public HelpPageInfoImpl(final Collection<? extends CommandInfo> commands, final int currentPage, final int pageSize, final String helpCommand)
    {
        this.commands = new TreeSet<>(Comparator.comparing(CommandInfo::getFullPrefix));
        this.commands.addAll(commands);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.helpCommand = helpCommand;
    }

    @Override
    public int getCurrent()
    {
        return this.currentPage;
    }

    @Override
    public int getMax()
    {
        final int fullPages = this.commands.size() / this.pageSize;

        if (this.commands.size() % this.pageSize == 0)
        {
            return fullPages;
        }

        return fullPages + 1;
    }

    @Override
    public boolean hasPrevious()
    {
        return this.currentPage > 1;
    }

    @Override
    public boolean hasNext()
    {
        return this.currentPage < this.getMax();
    }

    @Override
    public int getPrevious()
    {
        return this.currentPage - 1;
    }

    @Override
    public int getNext()
    {
        return this.currentPage + 1;
    }

    @Override
    public String getHelpCommand()
    {
        return this.helpCommand;
    }

    /**
     * Returns the collection of the {@link CommandInfo}s that are on this help page.
     *
     * @return the collection of the {@link CommandInfo}s that are on this help page
     */
    public Collection<? extends CommandInfo> getInfos()
    {
        final List<CommandInfo> collection = new ArrayList<>();
        final int startIndex = (this.currentPage - 1) * this.pageSize;
        final int endIndex = this.currentPage * this.pageSize - 1;

        final Iterator<? extends CommandInfo> iterator = this.commands.iterator();

        int i = 0;
        while (iterator.hasNext())
        {
            int currentI = i++;
            final CommandInfo info = iterator.next();

            if (currentI < startIndex || currentI > endIndex)
            {
                continue;
            }

            collection.add(info);
        }

        return collection;
    }
}
