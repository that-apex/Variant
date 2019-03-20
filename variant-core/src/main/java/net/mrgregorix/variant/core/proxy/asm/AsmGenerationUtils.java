package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Utilities for generating ASM code.
 */
public class AsmGenerationUtils
{
    private static final BiMap<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPES = HashBiMap.create(9);

    static
    {
        PRIMITIVE_WRAPPER_TYPES.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPER_TYPES.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPER_TYPES.put(char.class, Character.class);
        PRIMITIVE_WRAPPER_TYPES.put(double.class, Double.class);
        PRIMITIVE_WRAPPER_TYPES.put(float.class, Float.class);
        PRIMITIVE_WRAPPER_TYPES.put(int.class, Integer.class);
        PRIMITIVE_WRAPPER_TYPES.put(long.class, Long.class);
        PRIMITIVE_WRAPPER_TYPES.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_TYPES.put(void.class, Void.class);
    }

    /**
     * Change a java class name to a bytecode name
     *
     * @param name java class name
     *
     * @return bytecode name
     */
    public static String javaNameToAsmName(final String name)
    {
        return name.replace('.', '/');
    }

    /**
     * Get an access {@link Opcodes} value for the given member.
     *
     * @param member member
     *
     * @return access value
     */
    public static int getAccess(final Member member)
    {
        if (Modifier.isPublic(member.getModifiers()))
        {
            return Opcodes.ACC_PUBLIC;
        }
        else if (Modifier.isProtected(member.getModifiers()))
        {
            return Opcodes.ACC_PROTECTED;
        }
        else if (Modifier.isPrivate(member.getModifiers()))
        {
            return Opcodes.ACC_PRIVATE;
        }

        return 0;
    }

    /**
     * Gets an exceptions definition of a {@link Executable}
     *
     * @param member any executable member
     *
     * @return array of exception names
     */
    public static String[] getExceptions(final Executable member)
    {
        final Class<?>[] exceptionTypes = member.getExceptionTypes();
        final String[] exceptions = new String[exceptionTypes.length];

        for (int i = 0; i < exceptionTypes.length; i++)
        {
            exceptions[i] = javaNameToAsmName(exceptionTypes[i].getName());
        }

        return exceptions;
    }

    /**
     * Gets an opcode used to load the given type from arguments.
     *
     * @param type type to be loaded
     *
     * @return opcode to load the type
     */
    public static int getLoadOpcode(final Class<?> type)
    {
        if (type == int.class || type == short.class || type == byte.class || type == char.class)
        {
            return Opcodes.ILOAD;
        }
        else if (type == long.class)
        {
            return Opcodes.LLOAD;
        }
        else if (type == float.class)
        {
            return Opcodes.FLOAD;
        }
        else if (type == double.class)
        {
            return Opcodes.DLOAD;
        }
        else
        {
            return Opcodes.ALOAD;
        }
    }

    /**
     * Gets an opcode used to return the given type.
     *
     * @param type type to be returned
     *
     * @return opcode to return the type
     */
    public static int getReturnOpcode(final Class<?> type)
    {
        if (type == int.class || type == short.class || type == byte.class || type == char.class)
        {
            return Opcodes.IRETURN;
        }
        else if (type == long.class)
        {
            return Opcodes.LRETURN;
        }
        else if (type == float.class)
        {
            return Opcodes.FRETURN;
        }
        else if (type == double.class)
        {
            return Opcodes.DRETURN;
        }
        else if (type == void.class)
        {
            return Opcodes.RETURN;
        }
        else
        {
            return Opcodes.ARETURN;
        }
    }


    /**
     * Pushes an integer onto the stack.
     *
     * @param mv      method visitor to be used
     * @param integer integer to be pushed
     */
    public static void pushIntOntoStack(final MethodVisitor mv, final int integer)
    {
        switch (integer)
        {
            case - 1:
                mv.visitInsn(Opcodes.ICONST_M1);
                return;
            case 0:
                mv.visitInsn(Opcodes.ICONST_0);
                return;
            case 1:
                mv.visitInsn(Opcodes.ICONST_1);
                return;
            case 2:
                mv.visitInsn(Opcodes.ICONST_2);
                return;
            case 3:
                mv.visitInsn(Opcodes.ICONST_3);
                return;
            case 4:
                mv.visitInsn(Opcodes.ICONST_4);
                return;
            case 5:
                mv.visitInsn(Opcodes.ICONST_5);
                return;
        }

        if (integer >= Byte.MIN_VALUE && integer <= Byte.MAX_VALUE)
        {
            mv.visitIntInsn(Opcodes.BIPUSH, integer);
        }
        else if (integer >= Short.MIN_VALUE && integer <= Short.MAX_VALUE)
        {
            mv.visitIntInsn(Opcodes.SIPUSH, integer);
        }
        else
        {
            mv.visitLdcInsn(integer);
        }
    }

    /**
     * Casts an object from the stack to the given type
     *
     * @param mv   method visitor to be used
     * @param type type the value should be casted to
     */
    public static void castObject(final MethodVisitor mv, final Class<?> type)
    {
        if (! type.isPrimitive())
        {
            mv.visitTypeInsn(Opcodes.CHECKCAST, javaNameToAsmName(type.getName()));
            return;
        }

        final String wrapperType = javaNameToAsmName(PRIMITIVE_WRAPPER_TYPES.get(type).getName());
        mv.visitTypeInsn(Opcodes.CHECKCAST, wrapperType);
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            wrapperType,
            type.getName() + "Value",
            "()" + Type.getDescriptor(type),
            false
        );
    }

    /**
     * Box a primitive type by creating a wrapper type of it.
     */
    public static void boxPrimitive(final MethodVisitor mv, final Class<?> type)
    {
        Preconditions.checkArgument(type.isPrimitive(), "type is not primitive");

        if (type == void.class)
        {
            mv.visitInsn(Opcodes.ACONST_NULL);
            return;
        }

        final String wrapperType = javaNameToAsmName(PRIMITIVE_WRAPPER_TYPES.get(type).getName());
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            wrapperType,
            "valueOf",
            "(" + Type.getDescriptor(type) + ")L" + wrapperType + ";",
            false
        );
    }

    /**
     * Pack all method's parameters into an {@code Object[]} array
     *
     * @param mv         method visitor to be used
     * @param parameters parameters of the method
     */
    public static void packParametersToArray(final MethodVisitor mv, final Parameter[] parameters)
    {
        AsmGenerationUtils.pushIntOntoStack(mv, parameters.length);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, AsmGenerationUtils.javaNameToAsmName(Object.class.getName()));
        int stackPosition = 1;

        for (int i = 0; i < parameters.length; i++)
        {
            mv.visitInsn(Opcodes.DUP);

            AsmGenerationUtils.pushIntOntoStack(mv, i);

            final Class<?> parameterType = parameters[i].getType();
            mv.visitVarInsn(AsmGenerationUtils.getLoadOpcode(parameterType), stackPosition);

            if (parameterType.isPrimitive())
            {
                AsmGenerationUtils.boxPrimitive(mv, parameterType);
            }

            stackPosition++;

            if (parameterType == long.class || parameterType == double.class)
            {
                stackPosition++;
            }

            mv.visitInsn(Opcodes.AASTORE);
        }
    }

    /**
     * Return a value from a method
     *
     * @param mv         method visitor to be used
     * @param returnType return type of the method
     */
    public static void returnFromMethod(final MethodVisitor mv, final Class<?> returnType)
    {
        if (returnType == void.class)
        {
            mv.visitInsn(Opcodes.POP);
            mv.visitInsn(Opcodes.RETURN);
        }
        else
        {
            AsmGenerationUtils.castObject(mv, returnType);
            mv.visitInsn(AsmGenerationUtils.getReturnOpcode(returnType));
        }
    }
}
