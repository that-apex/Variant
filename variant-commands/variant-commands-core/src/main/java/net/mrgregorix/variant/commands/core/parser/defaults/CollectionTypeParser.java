package net.mrgregorix.variant.commands.core.parser.defaults;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.google.common.base.Supplier;
import net.mrgregorix.variant.commands.api.annotation.types.CollectionType;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.parser.StringParserImpl;

/**
 * A {@link TypeParser} for basic {@link Collection} implementations
 *
 * @see Set
 * @see List
 * @see Queue
 */
public class CollectionTypeParser extends AbstractDefaultTypeParser<Collection, CollectionTypeParser>
{
    private static final Map<Class<? extends Collection>, Supplier<Collection<?>>> SUPPORTED_TYPES = new HashMap<>();

    static
    {
        SUPPORTED_TYPES.put(List.class, ArrayList::new);
        SUPPORTED_TYPES.put(Set.class, HashSet::new);
        SUPPORTED_TYPES.put(Queue.class, LinkedList::new);
        SUPPORTED_TYPES.put(Collection.class, ArrayList::new);
    }

    @Override
    public Class<Collection> getBaseType()
    {
        return Collection.class;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean matches(final Class<?> type)
    {
        return SUPPORTED_TYPES.containsKey(type);
    }

    private Class<?> collectionType(final TypeDefinition definition)
    {
        return Optional.ofNullable(definition.getAnnotation(CollectionType.class)).orElseThrow(() -> new IllegalArgumentException("No @CollectionType for " + definition)).value();
    }

    @Override
    public Collection parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition) throws ParsingException
    {
        return this.parse(argumentParser, parser, typeDefinition);
    }

    @Override
    public Collection parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue) throws ParsingException
    {
        return this.parse(argumentParser, new StringParserImpl(defaultValue.defaultValue()), defaultValue);
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    private Collection parse(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition def) throws ParsingException
    {
        final Class<?> collectionType = this.collectionType(def);
        final TypeDefinition subDefinition = new CollectionTypeDefinition(collectionType, def);
        final TypeParser<?, ?> typeParser = argumentParser.getParserFor(collectionType);
        final Collection collection = SUPPORTED_TYPES.get(def.getType()).get();

        while (true)
        {
            collection.add(typeParser.parseType(argumentParser, parser, subDefinition));

            if (parser.isFinished())
            {
                break;
            }

            if (parser.peekCharacter() == ',')
            {
                parser.readCharacter();
            }
            else
            {
                break;
            }
        }

        return collection;
    }

    private static final class CollectionTypeDefinition implements TypeDefinition
    {
        private final Class<?>       type;
        private final TypeDefinition parent;

        private CollectionTypeDefinition(final Class<?> type, final TypeDefinition parent)
        {
            this.type = type;
            this.parent = parent;
        }

        @Override
        public String getName()
        {
            return this.parent.getName() + "-element";
        }

        @Override
        public Class<?> getType()
        {
            return this.type;
        }

        @Override
        public Annotation[] getDeclaredAnnotations()
        {
            return this.parent.getDeclaredAnnotations();
        }

        @Override
        public <T extends Annotation> T getAnnotation(final Class<T> type)
        {
            return this.parent.getAnnotation(type);
        }

        @Override
        public boolean isRequired()
        {
            return true;
        }

        @Override
        public String defaultValue()
        {
            return "";
        }
    }
}
