package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.api.proxy.ProxyProvider;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * A proxy provider that uses the ASM library to create a proxy class.
 */
public class AsmProxyProvider implements ProxyProvider
{
    private static final String ASM_PROXY_HELPER_BEFORE_INVOKE;
    private static final String ASM_PROXY_HELPER_AFTER_INVOKE;

    static
    {
        try
        {
            ASM_PROXY_HELPER_BEFORE_INVOKE =
                Type.getMethodDescriptor(AsmProxyHelperInternal.class.getDeclaredMethod("beforeInvoke", Object.class, Method.class, Object[].class, ProxyInvocationHandler[].class));

            ASM_PROXY_HELPER_AFTER_INVOKE =
                Type.getMethodDescriptor(AsmProxyHelperInternal.class.getDeclaredMethod("afterInvoke", Object.class, Object.class, Method.class, Object[].class, ProxyInvocationHandler[].class));
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    @Override
    public <T> Class<? extends T> createProxy(final ClassLoader classLoader, final Class<T> type, final String proxyClassname, final Map<Method, Collection<ProxyInvocationHandler>> invocationHandlers)
    {
        final AsmClassLoadingHelper loadingHelper = new AsmClassLoadingHelper(classLoader);
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        cw.visit(
            Opcodes.V1_8, // Java version
            Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER | Opcodes.ACC_SYNTHETIC, // access specifier
            AsmGenerationUtils.javaNameToAsmName(proxyClassname), // name
            null, // signature, null = no generics
            AsmGenerationUtils.javaNameToAsmName(type.getName()), // superclass
            new String[] {AsmGenerationUtils.javaNameToAsmName(Proxy.class.getName())} // interfaces
        );

        //
        // Generate constructors
        //
        for (final Constructor<?> constructor : type.getDeclaredConstructors())
        {
            final String descriptor = Type.getType(constructor).getDescriptor();

            final MethodVisitor mv = cw.visitMethod(
                AsmGenerationUtils.getAccess(constructor),
                "<init>",
                descriptor,
                null,
                AsmGenerationUtils.getExceptions(constructor)
            );

            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);

            int paramNum = 1;
            for (final Parameter parameter : constructor.getParameters())
            {
                mv.visitVarInsn(AsmGenerationUtils.getLoadOpcode(parameter.getType()), paramNum++);
            }

            mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                AsmGenerationUtils.javaNameToAsmName(constructor.getDeclaringClass().getName()), // owner
                "<init>",
                descriptor,
                false
            );

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                AsmGenerationUtils.javaNameToAsmName(proxyClassname), // owner
                "$_VariantInit",
                "()V",
                false
            );

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        int localId = 0;
        final Map<Integer, Integer> localToGlobalMap = new HashMap<>();

        for (final Map.Entry<Method, Collection<ProxyInvocationHandler>> methodListEntry : invocationHandlers.entrySet())
        {
            int id = AsmProxyHelperInternal.getId();

            final Method method = methodListEntry.getKey();
            final String descriptor = Type.getType(method).getDescriptor();
            final MethodVisitor mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC,
                method.getName(),
                descriptor,
                null,
                AsmGenerationUtils.getExceptions(method)
            );

            mv.visitCode();
            final Label codeStart = new Label();
            final Label codeEnd = new Label();
            mv.visitLabel(codeStart);

            int localStartNum = 1;
            for (final Parameter parameter : method.getParameters())
            {
                localStartNum++;
                if (parameter.getType() == long.class || parameter.getType() == double.class)
                {
                    localStartNum++;
                }
            }

            final int localParametersArray = localStartNum;

            // pack parameters to array
            AsmGenerationUtils.packParametersToArray(mv, method.getParameters());
            mv.visitVarInsn(Opcodes.ASTORE, localParametersArray);

            // param 0, this instance
            mv.visitVarInsn(Opcodes.ALOAD, 0);

            // param 1, reflect method
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantReflectMethod_" + localId, Type.getDescriptor(Method.class));

            // param 2, array of parameters
            mv.visitVarInsn(Opcodes.ALOAD, localParametersArray);

            // param 3, handler array
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantHandlerArray_" + localId, Type.getDescriptor(ProxyInvocationHandler[].class));

            // call beforeInvoke
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                AsmGenerationUtils.javaNameToAsmName(AsmProxyHelperInternal.class.getName()),
                "beforeInvoke",
                ASM_PROXY_HELPER_BEFORE_INVOKE,
                false
            );
            mv.visitInsn(Opcodes.DUP);

            // check if shouldProceed
            final Label continueInvoke = new Label();
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                AsmGenerationUtils.javaNameToAsmName(BeforeInvocationResult.class.getName()),
                "shouldProceed",
                "()Z",
                false
            );
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitJumpInsn(Opcodes.IF_ICMPNE, continueInvoke);

            // return value from BeforeInvocationResult
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                AsmGenerationUtils.javaNameToAsmName(BeforeInvocationResult.class.getName()),
                "getReturnValue",
                "()Ljava/lang/Object;",
                false
            );

            AsmGenerationUtils.returnFromMethod(mv, method.getReturnType());

            // continue invocation
            mv.visitLabel(continueInvoke);
            mv.visitInsn(Opcodes.POP); // pop the additional BeforeInvocationResult instance

            // call the super method if possible, gets first parameter for afterInvoke call
            if (Modifier.isAbstract(method.getModifiers()))
            {
                mv.visitInsn(Opcodes.ACONST_NULL);
            }
            else
            {
                // load this onto the stack
                mv.visitVarInsn(Opcodes.ALOAD, 0);

                final Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++)
                {
                    // get parameter
                    mv.visitVarInsn(Opcodes.ALOAD, localParametersArray);
                    AsmGenerationUtils.pushIntOntoStack(mv, i);
                    mv.visitInsn(Opcodes.AALOAD);
                    AsmGenerationUtils.castObject(mv, parameters[i].getType());
                }

                mv.visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    AsmGenerationUtils.javaNameToAsmName(type.getName()),
                    method.getName(),
                    descriptor,
                    false
                );

                if (method.getReturnType().isPrimitive())
                {
                    AsmGenerationUtils.boxPrimitive(mv, method.getReturnType());
                }
            }

            // param 1, this instance
            mv.visitVarInsn(Opcodes.ALOAD, 0);

            // param 2, reflect method
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantReflectMethod_" + localId, Type.getDescriptor(Method.class));

            // param 3, array of parameters
            mv.visitVarInsn(Opcodes.ALOAD, localParametersArray);

            // param 4, handler array
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantHandlerArray_" + localId, Type.getDescriptor(ProxyInvocationHandler[].class));

            // call afterInvoke
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                AsmGenerationUtils.javaNameToAsmName(AsmProxyHelperInternal.class.getName()),
                "afterInvoke",
                ASM_PROXY_HELPER_AFTER_INVOKE,
                false
            );

            AsmGenerationUtils.returnFromMethod(mv, method.getReturnType());
            mv.visitLabel(codeEnd);

            mv.visitMaxs(0, 0);
            mv.visitLocalVariable("$_VariantParameters", Type.getDescriptor(Object[].class), null, codeStart, codeEnd, localParametersArray);
            mv.visitEnd();

            cw.visitField(Opcodes.ACC_PRIVATE, "$_VariantReflectMethod_" + localId, Type.getDescriptor(Method.class), null, null);
            cw.visitField(Opcodes.ACC_PRIVATE, "$_VariantHandlerArray_" + localId, Type.getDescriptor(ProxyInvocationHandler[].class), null, null);


            final ProxyInvocationHandler[] array = new ProxyInvocationHandler[methodListEntry.getValue().size()];
            methodListEntry.getValue().toArray(array);
            AsmProxyHelperInternal.register(id, method, array);

            localToGlobalMap.put(localId, id);
            localId++;
        }

        //
        // Generate init methods
        //
        cw.visitField(Opcodes.ACC_PRIVATE, "$_VariantInitialized", "Z", null, false);

        final MethodVisitor mv = cw.visitMethod(
            Opcodes.ACC_PRIVATE,
            "$_VariantInit",
            "()V",
            null,
            new String[0]
        );

        mv.visitCode();
        final Label initialize = new Label();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantInitialized", "Z");
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.IF_ICMPEQ, initialize);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(initialize);

        for (final Map.Entry<Integer, Integer> entry : localToGlobalMap.entrySet())
        {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            AsmGenerationUtils.pushIntOntoStack(mv, entry.getValue());
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                AsmGenerationUtils.javaNameToAsmName(AsmProxyHelperInternal.class.getName()),
                "getMethod",
                Type.getMethodDescriptor(Type.getType(Method.class), Type.getType(int.class)),
                false
            );
            mv.visitFieldInsn(Opcodes.PUTFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantReflectMethod_" + entry.getKey(), Type.getDescriptor(Method.class));

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            AsmGenerationUtils.pushIntOntoStack(mv, entry.getValue());
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                AsmGenerationUtils.javaNameToAsmName(AsmProxyHelperInternal.class.getName()),
                "getInvocationHandlers",
                Type.getMethodDescriptor(Type.getType(ProxyInvocationHandler[].class), Type.getType(int.class)),
                false
            );
            mv.visitFieldInsn(Opcodes.PUTFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantHandlerArray_" + entry.getKey(), Type.getDescriptor(ProxyInvocationHandler[].class));
        }

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, AsmGenerationUtils.javaNameToAsmName(proxyClassname), "$_VariantInitialized", "Z");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        //
        // Finalize
        //
        cw.visitEnd();
        return (Class<? extends T>) loadingHelper.defineClass(proxyClassname, cw.toByteArray());
    }
}
