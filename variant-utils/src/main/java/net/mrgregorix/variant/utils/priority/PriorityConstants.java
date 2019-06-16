package net.mrgregorix.variant.utils.priority;

/**
 * Contains a numeric values for common {@link Prioritizable} priorities.
 * <p>
 * Only these exact values are used by are the official Variant modules
 */
public class PriorityConstants
{
    public static final int LOWEST         = - 1000;
    public static final int NEARLY_LOWEST  = - 999;
    public static final int LOW            = - 500;
    public static final int NORMAL         = 0;
    public static final int HIGH           = 500;
    public static final int NEARLY_HIGHEST = 999;
    public static final int HIGHEST        = 1000;
}
