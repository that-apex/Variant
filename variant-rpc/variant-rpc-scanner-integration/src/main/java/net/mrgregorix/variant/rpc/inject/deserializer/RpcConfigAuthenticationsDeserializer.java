package net.mrgregorix.variant.rpc.inject.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentication;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentications;

/**
 * A {@link StdDeserializer} for {@link RpcConfigAuthentications} type
 */
public class RpcConfigAuthenticationsDeserializer extends StdDeserializer<RpcConfigAuthentications>
{
    private final Map<String, Class<? extends RpcConfigAuthentication>> types = new HashMap<>();

    /**
     * Create a new RpcConfigAuthenticationsDeserializer
     */
    public RpcConfigAuthenticationsDeserializer()
    {
        super((JavaType) null);
    }

    @Override
    public RpcConfigAuthentications deserialize(final JsonParser p, final DeserializationContext context) throws IOException
    {
        final List<RpcConfigAuthentication<?>> authentications = new ArrayList<>();

        while (p.nextToken() == JsonToken.FIELD_NAME)
        {
            final String fieldName = p.readValueAs(String.class);
            final Class<? extends RpcConfigAuthentication> type = this.types.get(fieldName);

            if (type == null)
            {
                throw new IllegalArgumentException("No authentication named " + fieldName + "found");
            }

            authentications.add(p.readValueAs(type));
        }

        return new RpcConfigAuthentications(authentications);
    }

    /**
     * Registers a new type of authenticator that can be deserialized by this deserializer.
     *
     * @param name name of the XML tag that will be used to configure the authenticator
     * @param type type that the XML tree will be deserialized as
     */
    public void registerType(final String name, final Class<? extends RpcConfigAuthentication> type)
    {
        this.types.put(name, type);
    }
}
