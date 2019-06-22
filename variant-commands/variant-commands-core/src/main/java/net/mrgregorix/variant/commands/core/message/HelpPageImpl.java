package net.mrgregorix.variant.commands.core.message;

import net.mrgregorix.variant.commands.api.message.HelpPage;

public class HelpPageImpl implements HelpPage
{
    private final int page;

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
