package net.mrgregorix.variant.commands.api.annotation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ForType(String.class)
public @interface Raw
{
}
