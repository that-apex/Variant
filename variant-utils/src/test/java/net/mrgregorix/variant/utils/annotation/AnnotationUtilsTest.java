package net.mrgregorix.variant.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AnnotationUtilsTest
{
    @SuppressWarnings("unchecked")
    @Test
    public void testAnnotationUtils()
    {
        assertThat("invalid annotations found", AnnotationUtils.getAllAnnotations(TestClass.class), contains(instanceOf(Base.class)));
        assertThat("invalid annotations found", AnnotationUtils.getAllAnnotations(TestClass2.class), containsInAnyOrder(instanceOf(Base.class), instanceOf(Sub.class)));

        assertThat("invalid annotations found", AnnotationUtils.getAnnotation(TestClass2.class, Base.class), is(instanceOf(Base.class)));
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface Base
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Base
    private @interface Sub
    {
    }

    @Base
    class TestClass
    {
    }

    @Sub
    class TestClass2
    {
    }
}
