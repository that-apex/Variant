package net.mrgregorix.variant.utils.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Utilities for finding annotations
 */
public final class AnnotationUtils
{
    private static final Collection<Class<? extends Annotation>> IGNORED_ANNOTATIONS = Arrays.asList(Target.class, Retention.class, Documented.class);

    /**
     * Recursively scans for all annotations on this element.
     *
     * @param annotatedElement element to be scanned
     *
     * @return collection of all found annotations
     */
    public static Collection<Annotation> getAllAnnotations(final AnnotatedElement annotatedElement)
    {
        return getAnnotationsMatching(annotatedElement, annotation -> true);
    }

    /**
     * Recursively scans for all annotations on this element that matches the given matcher.
     *
     * @param annotatedElement element to be scanned
     * @param matcher          matcher to the annotations against
     *
     * @return collection of all found annotations
     */
    public static Collection<Annotation> getAnnotationsMatching(final AnnotatedElement annotatedElement, final Predicate<? super Annotation> matcher)
    {
        return findAnnotationsInternal(annotatedElement, matcher, false);
    }

    /**
     * Recursively scans for the first annotation on this element that matches the given matcher.
     *
     * @param annotatedElement element to be scanned
     * @param matcher          matcher to the annotations against
     *
     * @return the found annotation or null if none found
     */
    @Nullable
    public static Annotation getFirstAnnotationsMatching(final AnnotatedElement annotatedElement, final Predicate<? super Annotation> matcher)
    {
        final Collection<Annotation> result = findAnnotationsInternal(annotatedElement, matcher, true);
        return result.isEmpty() ? null : result.iterator().next();
    }

    /**
     * Recursively scans for the first annotation on this element that has the given type.
     *
     * @param annotatedElement element to be scanned
     * @param type             type of an annotation
     *
     * @return the found annotation or null if none found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final AnnotatedElement annotatedElement, final Class<T> type)
    {
        return (T) getFirstAnnotationsMatching(annotatedElement, type::isInstance);
    }

    /**
     * Recursively scans for all annotations oon this element that has the given type.
     *
     * @param annotatedElement element to be scanned
     * @param type             type of an annotation
     *
     * @return collection of all found annotations
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Collection<T> getAllAnnotationsWithType(final AnnotatedElement annotatedElement, final Class<T> type)
    {
        return (Collection<T>) getAnnotationsMatching(annotatedElement, type::isInstance);
    }


    private static Collection<Annotation> findAnnotationsInternal(final AnnotatedElement annotatedElement, final Predicate<? super Annotation> matcher, final boolean first)
    {
        final Set<Annotation> result = first ? new HashSet<>(1) : new HashSet<>();
        final Set<Annotation> checked = new HashSet<>();
        findAnnotationsRecurse(annotatedElement, matcher, result, checked, first);
        return result;
    }

    private static boolean findAnnotationsRecurse(final AnnotatedElement annotatedElement, final Predicate<? super Annotation> matcher, final Set<? super Annotation> result, final Set<? super Annotation> checked,
                                                  final boolean first)
    {
        for (final Annotation declaredAnnotation : annotatedElement.getDeclaredAnnotations())
        {
            if (IGNORED_ANNOTATIONS.contains(declaredAnnotation.annotationType()) || checked.contains(declaredAnnotation))
            {
                continue;
            }
            checked.add(declaredAnnotation);

            if (matcher.test(declaredAnnotation))
            {
                result.add(declaredAnnotation);

                if (first)
                {
                    return true;
                }
            }

            if (findAnnotationsRecurse(declaredAnnotation.annotationType(), matcher, result, checked, first))
            {
                return true;
            }
        }

        return false;
    }

    private AnnotationUtils()
    {
    }
}
