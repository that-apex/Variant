package net.mrgregorix.variant.commands.core.parser.defaults;

import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

public abstract class AbstractDefaultTypeParser <T, P extends AbstractDefaultTypeParser<T, P>> extends AbstractModifiablePrioritizable<P> implements TypeParser<T, P>
{
    protected AbstractDefaultTypeParser()
    {
        this.setPriority(PriorityConstants.HIGHEST);
    }
}
