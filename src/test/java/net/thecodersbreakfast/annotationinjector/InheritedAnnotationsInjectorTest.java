package net.thecodersbreakfast.annotationinjector;

import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

public class InheritedAnnotationsInjectorTest {

    @Test
    public void pojoNoAnnotationNoInterface() {
        class TestPojo {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();
    }

    // ================================================================================
    // POJO without local annotations
    // ================================================================================

    /**
     * One interface
     * Also, tests that non-inheritable annotations are not inherited.
     */
    @Test
    public void pojoWithInterfaceA1() {
        class TestPojo implements InterfaceA1 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();

        AnnotationA annotationA = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annotationA);
        NonInheritableAnnotation nonInheritableAnnotation = TestPojo.class.getAnnotation(NonInheritableAnnotation.class);
        assertNull(nonInheritableAnnotation);
    }

    /**
     * Two interfaces with different annotations
     */
    @Test
    public void pojoWithCompatibleInterfacesA1B1() {

        class TestPojo implements InterfaceA1, InterfaceB1 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();

        Annotation[] annotations = TestPojo.class.getAnnotations();
        assertEquals(2, annotations.length);
        AnnotationA annotationA = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annotationA);
        AnnotationB annotationB = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annotationB);
    }

    /**
     * Two interfaces with same annotation, equal parameters
     */
    @Test
    public void pojoWithCompatibleInterfacesA1A2() {

        class TestPojo implements InterfaceA1, InterfaceA2 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();

        AnnotationA annotationA = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annotationA);
        Annotation[] annotations = TestPojo.class.getAnnotations();
        assertEquals(1, annotations.length);
    }

    /**
     * Two interfaces with same annotation, different parameters
     */
    @Test(expected = AnnotationInjectionException.class)
    public void pojoWithIncompatibleInterfacesB1B2() {

        class TestPojo implements InterfaceB1, InterfaceB2 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();
    }

    // ================================================================================
    // POJO with local annotations
    // ================================================================================

    /**
     * Pojo with an annotation and an interface with a different annotation
     */
    @Test
    public void pojoWithAnnotationA1AndCompatibleAnnotationB1() {

        @AnnotationB("foo")
        class TestPojo implements InterfaceA1 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();

        AnnotationA annotationA = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annotationA);
        AnnotationB annotationB = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annotationB);
    }

    /**
     * Pojo with an annotation and an interface with the same annotation, same parameters
     */
    @Test
    public void pojoWithAnnotationA1AndCompatibleInterfaceA1() {

        @AnnotationA
        class TestPojo implements InterfaceA1 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();

        AnnotationA annotationA = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annotationA);
    }

    /**
     * Pojo with an annotation and an interface with the same annotation, different parameters
     */
    @Test(expected = AnnotationInjectionException.class)
    public void pojoWithAnnotationB1AndIncompatibleInterfaceB2() {

        @AnnotationB("foo")
        class TestPojo implements InterfaceB2 {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectInterfaceInheritedAnnotations();
    }

    // ================================================================================
    // POJO with package annotations
    // ================================================================================

    @Test
    public void pojoWithNoAnnotationAndPackageAnnotation() {
        class TestPojo {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectPackageInheritedAnnotations();

        AnnotationB annotationB = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annotationB);
    }

    @Test
    public void pojoWithAnnotationAndCompatiblePackageAnnotation() {

        @AnnotationA
        class TestPojo {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectPackageInheritedAnnotations();

        AnnotationB annotationB = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annotationB);
    }

    @Test(expected = AnnotationInjectionException.class)
    public void pojoWithAnnotationAndIncompatiblePackageAnnotation() {

        @AnnotationB("foo")
        class TestPojo {
        }

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(TestPojo.class);
        injector.injectPackageInheritedAnnotations();
    }

}
