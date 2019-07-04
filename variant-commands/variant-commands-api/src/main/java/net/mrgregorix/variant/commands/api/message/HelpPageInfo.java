package net.mrgregorix.variant.commands.api.message;

/**
 * Represents information about a single help page.
 */
public interface HelpPageInfo
{
    /**
     * Get the number of this help page. (Note that the help pages start from 1, not 0)
     *
     * @return the number of this help page
     */
    int getCurrent();

    /**
     * Returns the total amount of pages that are available.
     *
     * @return the total amount of pages that are available
     */
    int getMax();

    /**
     * Returns whether or not a page before this page exists.
     *
     * @return whether or not a page before this page exists
     */
    boolean hasPrevious();

    /**
     * Returns whether or not a page after this page exists.
     *
     * @return whether or not a page after this page exists
     */
    boolean hasNext();

    /**
     * Returns the number of a page before this page
     *
     * @return the number of a page before this page
     */
    int getPrevious();

    /**
     * Returns the number of a page after this page
     *
     * @return the number of a page after this page
     */
    int getNext();

    /**
     * Returns a command that was used to display this help page.
     *
     * @return a command that was used to display this help page
     */
    String getHelpCommand();
}
