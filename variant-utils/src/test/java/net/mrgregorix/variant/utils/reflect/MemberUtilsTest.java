package net.mrgregorix.variant.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MemberUtilsTest
{
    @Test
    public void testGetAllFields() throws ReflectiveOperationException
    {
        final Set<Field> expected = new HashSet<>(Arrays.asList(
            BaseClass.class.getDeclaredField("baseField"),
            BaseClass.class.getDeclaredField("baseField2"),
            TestedClass.class.getDeclaredField("myField")
        ));

        final Set<Field> actual = MemberUtils.getAllFields(TestedClass.class);

        assertThat("not all fields were found", expected, everyItem(is(in(actual))));
    }

    @Test
    public void testGetAllMethods() throws ReflectiveOperationException
    {
        final Set<Method> expected = new HashSet<>(Arrays.asList(
            BaseClass.class.getDeclaredMethod("baseMethod"),
            TestedClass.class.getDeclaredMethod("testMethod"),
            BaseInterface.class.getDeclaredMethod("method1"),
            OtherInterface.class.getDeclaredMethod("method2"),
            OtherInterface.class.getDeclaredMethod("method3")
        ));

        final Set<Method> actual = MemberUtils.getAllMethods(TestedClass.class);

        assertThat("not all methods were found", expected, everyItem(is(in(actual))));
    }
}
