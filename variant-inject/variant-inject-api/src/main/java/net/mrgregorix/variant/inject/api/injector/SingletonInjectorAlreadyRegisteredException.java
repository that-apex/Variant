package net.mrgregorix.variant.inject.api.injector;

/**
 * Exception thrown when a {@link CustomInjector} marked as singleton registerer is being registered but previously an injector with the same type was registered.
 */
public class SingletonInjectorAlreadyRegisteredException extends Exception
{
    private final Class<?> type;

    /**
     * Construct a new exception.
     *
     * @param type type of an injector that failed to register
     */
    public SingletonInjectorAlreadyRegisteredException(final Class<?> type)
    {
        super("Failed to register injector " + type.getName() + " because an injector with such type already exists");
        this.type = type;
    }

    /**
     * @return type of an injector that failed to register
     */
    public Class<?> getType()
    {
        return this.type;
    }
}
