package net.mrgregorix.variant.scanner.core.scenario.customannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.inject.api.annotation.Inject;

@Inject(simpleInjection = true)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface CustomSimpleInject
{
}
