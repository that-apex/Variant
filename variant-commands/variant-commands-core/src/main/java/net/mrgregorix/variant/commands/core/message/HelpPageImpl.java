package net.mrgregorix.variant.commands.core.message;

import net.mrgregorix.variant.commands.api.message.HelpPage;

/**
 * The default implementation of the {@link HelpPage}
 */
public class HelpPageImpl implements HelpPage
{
    private final int page;

    /**
     * Creates a new HelpPageImpl
     *
     * @param page number of the page
     */
    public HelpPageImpl(final int page)
    {
        this.page = page;
    }

    @Override
    public int getPage()
    {
        return this.page;
    }

    @Override
    public String toString()
    {
        return "HelpPageImpl{" +
               "page=" + this.page +
               '}';
    }
}
