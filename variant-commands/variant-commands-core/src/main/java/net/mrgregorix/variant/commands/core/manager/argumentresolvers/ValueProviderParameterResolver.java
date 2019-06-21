package net.mrgregorix.variant.commands.core.manager.argumentresolvers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.manager.ValueProvider;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.core.manager.ParameterResolver;
import net.mrgregorix.variant.commands.core.manager.RegisteredMethod;

public class ValueProviderParameterResolver implements ParameterResolver
{
    private final ValueProvider<Annotation> provider;
    private final Parameter parameter;
    private final Annotation annotation;

    public ValueProviderParameterResolver(final ValueProvider<Annotation> provider, final Parameter parameter, final Annotation annotation)
    {
        this.provider = provider;
        this.parameter = parameter;
        this.annotation = annotation;
    }

    @Override
    public Object resolve(final RegisteredMethod method, final CommandSender sender, final ParsingResult result)
    {
        return this.provider.provideValue(sender, method.getCommandInfo(), this.parameter, result, this.annotation);
    }
}
