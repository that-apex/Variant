package net.mrgregorix.variant.commands.core.parser.defaults;

import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

/**
 * A build-in {@link TypeParser} with the highest priority.
 *
 * @param <T>
 * @param <P>
 */
public abstract class AbstractDefaultTypeParser <T, P extends AbstractDefaultTypeParser<T, P>> extends AbstractModifiablePrioritizable<P> implements TypeParser<T, P>
{
    /**
     * Creates a new AbstractDefaultTypeParser with the highest priority
     */
    protected AbstractDefaultTypeParser()
    {
        this.setPriority(PriorityConstants.HIGHEST);
    }
}
