package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.meta.ForType;
import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;

/**
 * A {@link ParameterDescription} for providing {@link CommandSender} values
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParameterDescription
@ForType(CommandSender.class)
public @interface Sender
{
}
