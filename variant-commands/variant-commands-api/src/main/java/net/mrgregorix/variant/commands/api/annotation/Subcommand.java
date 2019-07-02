package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.CommandListener;

/**
 * States that this {@link Command} method is a subcommand of another command method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand
{
    /**
     * Name of the main method. This command must be declared after the main method and in the same {@link CommandListener} as the main method.
     *
     * @return name of the main method
     */
    String of();
}
