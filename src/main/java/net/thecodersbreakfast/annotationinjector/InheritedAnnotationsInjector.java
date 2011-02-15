package net.thecodersbreakfast.annotationinjector;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.util.*;

public class InheritedAnnotationsInjector extends AnnotationInjector {

    public InheritedAnnotationsInjector(Class<?> targetClass) {
        super(targetClass);
    }

    public void injectPackageInheritedAnnotations() throws AnnotationInjectionException {
        Set<Annotation> inheritableAnnotations = findInheritableAnnotationsOnPackage();
        injectAnnotations(inheritableAnnotations);
    }

    private Set<Annotation> findInheritableAnnotationsOnPackage() {
        Package pkg = targetClass.getPackage();
        if (pkg != null) {
            Annotation[] declaredAnnotations = pkg.getDeclaredAnnotations();
            return new HashSet<Annotation>(Arrays.asList(declaredAnnotations));
        }
        return Collections.emptySet();
    }

    public void injectInterfaceInheritedAnnotations() throws AnnotationInjectionException {
        Map<Class<?>, Set<Annotation>> inheritableAnnotations = findInheritableAnnotationsOnInterfaces();
        detectConflictingAnnotationInstances(inheritableAnnotations);
        Set<Annotation> mergedAnnotations = mergeAnnotations(inheritableAnnotations.values());
        injectAnnotations(mergedAnnotations);
    }

    private Map<Class<?>, Set<Annotation>> findInheritableAnnotationsOnInterfaces() {
        Map<Class<?>, Set<Annotation>> allInheritableAnnotations = new HashMap<Class<?>, Set<Annotation>>();
        for (Class<?> anInterface : targetClass.getInterfaces()) {
            Set<Annotation> inheritableAnnotations = findInheritableAnnotationsOnInterface(anInterface);
            if (inheritableAnnotations != null) {
                allInheritableAnnotations.put(anInterface, inheritableAnnotations);
            }
        }
        return allInheritableAnnotations;
    }

    private Set<Annotation> findInheritableAnnotationsOnInterface(Class<?> anInterface) {
        Set<Annotation> inheritableAnnotations = new HashSet<Annotation>();
        for (Annotation annotation : anInterface.getAnnotations()) {
            if (isAnnotationInheritable(annotation.annotationType())) {
                inheritableAnnotations.add(annotation);
            }
        }
        return inheritableAnnotations;
    }

    private boolean isAnnotationInheritable(Class<? extends Annotation> annotationClass) {
        return annotationClass.getAnnotation(Inherited.class) != null;
    }

    private void detectConflictingAnnotationInstances(Map<Class<?>, Set<Annotation>> annotationsBySourceInterface) {
        Map<Class<? extends Annotation>, AnnotationUsage> annotationUsages = new HashMap<Class<? extends Annotation>, AnnotationUsage>();
        for (Class<?> anInterface : annotationsBySourceInterface.keySet()) {
            Set<Annotation> annotations = annotationsBySourceInterface.get(anInterface);
            for (Annotation annotation : annotations) {
                AnnotationUsage usage = new AnnotationUsage(annotation, anInterface);
                AnnotationUsage potentiallyConflictingUsage = annotationUsages.put(annotation.annotationType(), usage);
                if (potentiallyConflictingUsage != null && !potentiallyConflictingUsage.equals(usage)) {
                    throw new AnnotationInjectionException(String.format(
                            "Annotation conflict : annotation %s is used on interfaces %s and %s with different parameters.",
                            annotation.annotationType().getCanonicalName(),
                            usage.getAnnotatedInterface().getCanonicalName(),
                            potentiallyConflictingUsage.getAnnotatedInterface().getCanonicalName()));
                }
            }
        }
    }

    private Set<Annotation> mergeAnnotations(Collection<Set<Annotation>> annotations) {
        Set<Annotation> mergedAnnotations = new HashSet<Annotation>();
        for (Set<Annotation> annotationSet : annotations) {
            mergedAnnotations.addAll(annotationSet);
        }
        return mergedAnnotations;
    }

    private static class AnnotationUsage {
        private Annotation annotation;
        private Class<?> annotatedInterface;

        private AnnotationUsage(Annotation annotation, Class<?> annotatedInterface) {
            if (annotation == null || annotatedInterface == null) {
                throw new NullPointerException("Annotation and using type must not be null.");
            }
            this.annotation = annotation;
            this.annotatedInterface = annotatedInterface;
        }

        public Class<?> getAnnotatedInterface() {
            return annotatedInterface;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            AnnotationUsage that = (AnnotationUsage) o;

            return annotation.equals(that.annotation);

        }

        @Override
        public int hashCode() {
            return annotation.hashCode();
        }
    }

}
