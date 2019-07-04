package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.CommandListener;

/**
 * Defines a command in a {@link CommandListener}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    /**
     * Name of the command.
     *
     * @return name of the command
     */
    String name();

    /**
     * Description of the command.
     *
     * @return description of the command
     */
    String description();

    /**
     * Usage of the command.
     * <p>If not specified it will be generated</p>
     *
     * @return usage of the command
     */
    String usage() default "";
}
