package net.mrgregorix.variant.commands.core.manager;

import java.lang.reflect.Method;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.core.CommandInfoImpl;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a method that was registered as a command method and its data.
 */
public class RegisteredMethod
{
    private final String              prefix;
    private final Command             command;
    private final CommandInfoImpl     commandInfo;
    private final Method              method;
    private final CommandListener     listener;
    @Nullable
    private final RegisteredMethod    parentMethod;
    private final TypeDefinition[]    argumentDefinitions;
    private final TypeDefinition[]    flagDefinitions;
    private final ParameterResolver[] parameterResolvers;
    private final int                 level;

    /**
     * Creates a new RegisteredMethod
     *
     * @param prefix              full prefix of this command
     * @param command             {@link Command} annotation
     * @param commandInfo         {@link CommandInfo} about this command
     * @param method              the actual reflect method
     * @param listener            listener instance that owns this method
     * @param parentMethod        parent of this method if this method is a subcommand
     * @param argumentDefinitions definitions of this command's arguments
     * @param flagDefinitions     definitions of this command's flags
     * @param parameterResolvers  {@link ParameterResolver}s for this command
     * @param level               level of nesting of this command
     */
    public RegisteredMethod(final String prefix, final Command command, final CommandInfoImpl commandInfo, final Method method, final CommandListener listener, @Nullable final RegisteredMethod parentMethod,
                            final TypeDefinition[] argumentDefinitions, final TypeDefinition[] flagDefinitions, final ParameterResolver[] parameterResolvers, final int level)
    {
        this.prefix = prefix;
        this.command = command;
        this.commandInfo = commandInfo;
        this.method = method;
        this.listener = listener;
        this.parentMethod = parentMethod;
        this.argumentDefinitions = argumentDefinitions;
        this.flagDefinitions = flagDefinitions;
        this.parameterResolvers = parameterResolvers;
        this.level = level;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public Command getCommand()
    {
        return this.command;
    }

    public CommandInfoImpl getCommandInfo()
    {
        return this.commandInfo;
    }

    public Method getMethod()
    {
        return this.method;
    }

    public CommandListener getListener()
    {
        return this.listener;
    }

    @Nullable
    public RegisteredMethod getParentMethod()
    {
        return this.parentMethod;
    }

    public TypeDefinition[] getArgumentDefinitions()
    {
        return this.argumentDefinitions;
    }

    public TypeDefinition[] getFlagDefinitions()
    {
        return this.flagDefinitions;
    }

    public ParameterResolver[] getParameterResolvers()
    {
        return this.parameterResolvers;
    }

    public int getLevel()
    {
        return this.level;
    }
}
