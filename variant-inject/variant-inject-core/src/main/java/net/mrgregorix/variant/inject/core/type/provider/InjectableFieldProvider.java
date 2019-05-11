package net.mrgregorix.variant.inject.core.type.provider;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.type.InjectableField;
import net.mrgregorix.variant.inject.api.type.provider.InjectableElementProvider;
import net.mrgregorix.variant.inject.core.type.InjectableFieldImpl;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;

public class InjectableFieldProvider implements InjectableElementProvider<InjectableField>
{
    @Override
    public Class<InjectableField> getType()
    {
        return InjectableField.class;
    }

    @Override
    public Collection<InjectableField> provide(final Class<?> clazz)
    {
        return Arrays.stream(clazz.getDeclaredFields())
                     .filter(field -> AnnotationUtils.getAnnotation(field, Inject.class) != null)
                     .map(InjectableFieldImpl::new)
                     .collect(Collectors.toUnmodifiableList());
    }
}
