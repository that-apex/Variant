package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;
import net.mrgregorix.variant.utils.reflect.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * A {@link TypeSerializer} implementation for all Objects that do not have their own TypeSerializer
 */
public abstract class AbstractObjectSerializer extends AbstractModifiablePrioritizable<TypeSerializer<Object>> implements TypeSerializer<Object>
{
    /**
     * Modifiers that should be ignored when looking for object's field
     */
    public static final int IGNORED_MODIFIERS = Modifier.STATIC | Modifier.TRANSIENT;

    private final DataSerializer defaultSerializer;

    /**
     * Creates a new AbstractObjectSerializer
     *
     * @param defaultSerializer serializer to use
     */
    protected AbstractObjectSerializer(final DataSerializer defaultSerializer)
    {
        this.defaultSerializer = defaultSerializer;
        this.setPriority(PriorityConstants.LOWEST);
    }

    @Override
    public Class<?> getMainType()
    {
        return Object.class;
    }

    @Override
    public String getIdentifier()
    {
        return "" + (char) 10;
    }

    /**
     * Serializers all fields of the given object.
     *
     * @param data        data stream where the serialized data will be written.
     * @param object      object to serialize.
     * @param serializeAs type that this should be serialized as.
     *
     * @throws IOException rethrown from IO operations on the data stream.
     */
    protected void serializeFields(final DataOutputStream data, final Object object, final Class<?> serializeAs) throws IOException
    {
        for (final Field declaredField : serializeAs.getDeclaredFields())
        {
            if ((declaredField.getModifiers() & IGNORED_MODIFIERS) != 0)
            {
                continue;
            }

            final Method accessMethod = this.findAccessMethod(declaredField, method -> declaredField.getType().isAssignableFrom(method.getReturnType()), "get", "is");

            try
            {
                final Object part;
                if (accessMethod != null)
                {
                    part = accessMethod.invoke(object);
                }
                else
                {
                    declaredField.setAccessible(true);
                    part = declaredField.get(object);
                }
                this.defaultSerializer.serialize(data, part);
            }
            catch (final IllegalAccessException e)
            {
                throw new AssertionError("This should never happen", e);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Instantiates a given type using an empty default constructor, or {@link Unsafe} if its available.
     *
     * @param type type to be instantiated
     * @param <T>  type to be instantiated
     *
     * @return a newly created instance.
     */
    @SuppressWarnings("unchecked")
    protected <T> T instantiateType(final Class<T> type)
    {
        T object;

        try
        {
            try
            {
                final Constructor<T> declaredConstructor = type.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                object = declaredConstructor.newInstance();
            }
            catch (final NoSuchMethodException e)
            {
                if (! UnsafeUtils.isAvailable())
                {
                    throw new IllegalArgumentException("Class " + type + " has no empty constructors.");
                }

                object = (T) UnsafeUtils.getUnsafe().allocateInstance(type);
            }
            catch (final IllegalAccessException e)
            {
                throw new AssertionError("This should never happen", e);
            }
        }
        catch (final InvocationTargetException | InstantiationException e)
        {
            throw new RuntimeException("Exception while calling constructor of " + type, e);
        }

        return object;
    }

    /**
     * Deserializes all fields of the given object.
     *
     * @param data          data stream where the serialized data will be read from.
     * @param object        object to set the fields on.
     * @param deserializeAs type that this should be deserialized as.
     *
     * @throws IOException rethrown from IO operations on the data stream.
     */
    protected void deserializeFields(final DataInputStream data, final Object object, final Class<?> deserializeAs) throws IOException
    {
        for (final Field declaredField : deserializeAs.getDeclaredFields())
        {
            if ((declaredField.getModifiers() & IGNORED_MODIFIERS) != 0)
            {
                continue;
            }

            final Method accessMethod = this.findAccessMethod(declaredField, method -> method.getParameterCount() == 1 && declaredField.getType().isAssignableFrom(method.getParameterTypes()[0]), "set");
            final Object argument = this.defaultSerializer.deserialize(data, declaredField.getType().isArray() ? declaredField.getType() : null);

            try
            {
                if (accessMethod != null)
                {
                    accessMethod.invoke(object, argument);
                }
                else
                {
                    declaredField.setAccessible(true);
                    declaredField.set(object, argument);
                }
            }
            catch (final IllegalAccessException | IllegalArgumentException e)
            {
                throw new AssertionError("This should never happen", e);
            }
            catch (final InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Finds a method matching the rule &lt;prefix&gt;FieldNameWithUppercase and the given predicate
     *
     * @param field     field to get the name from
     * @param predicate predicate to check the method against
     * @param prefixes  prefixes that will be looked for
     *
     * @return the found method or {@code null} if none found
     */
    @Nullable
    private Method findAccessMethod(final Field field, final Predicate<Method> predicate, final String... prefixes)
    {
        final String nameUpper = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);

        for (final String prefix : prefixes)
        {
            final String fullName = prefix + nameUpper;

            for (final Method method : field.getDeclaringClass().getMethods())
            {
                if (! method.getName().equals(fullName) || ! predicate.test(method))
                {
                    continue;
                }

                method.setAccessible(true);
                return method;
            }
        }

        return null;
    }

    @Override
    public void deserialize(final Object object, final DataInputStream data, Class<?> deserializeAs) throws IOException
    {
        if (deserializeAs == null)
        {
            deserializeAs = object.getClass();
        }

        if (deserializeAs.getSuperclass() != Object.class)
        {
            this.getDefaultSerializer().deserialize(data, object, deserializeAs.getSuperclass());
        }

        this.deserializeFields(data, object, deserializeAs);
    }

    /**
     * Gets the {@link DataSerializer} owning this object.
     *
     * @return the {@link DataSerializer} owning this object
     */
    public DataSerializer getDefaultSerializer()
    {
        return this.defaultSerializer;
    }
}
