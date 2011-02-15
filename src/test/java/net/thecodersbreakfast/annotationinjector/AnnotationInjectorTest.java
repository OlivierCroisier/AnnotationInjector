package net.thecodersbreakfast.annotationinjector;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AnnotationInjectorTest {

    private AnnotationA annotA;
    private AnnotationB annotB1;
    private AnnotationB annotB2;

    @Before
    public void setup() {

        annotA = new AnnotationA() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationA.class;
            }

            @Override
            public boolean equals(Object obj) {
                AnnotationA other = (AnnotationA) obj;
                return this.annotationType().equals(other.annotationType());
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };

        annotB1 = new AnnotationB() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationB.class;
            }

            @Override
            public String value() {
                return "foo";
            }

            @Override
            public boolean equals(Object obj) {
                AnnotationB other = (AnnotationB) obj;
                return this.annotationType().equals(other.annotationType()) && this.value().equals(other.value());
            }

            @Override
            public int hashCode() {
                return 127 * ("value".hashCode() ^ value().hashCode());
            }
        };

        annotB2 = new AnnotationB() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationB.class;
            }

            @Override
            public String value() {
                return "bar";
            }

            @Override
            public boolean equals(Object obj) {
                AnnotationB other = (AnnotationB) obj;
                return this.annotationType().equals(other.annotationType()) && this.value().equals(other.value());
            }

            @Override
            public int hashCode() {
                return 127 * ("value".hashCode() ^ value().hashCode());
            }
        };

    }

    @Test(expected = NullPointerException.class)
    public void nullTargetClassShouldFail() {
        new AnnotationInjector(null);
    }

    @Test(expected = NullPointerException.class)
    public void injectingNullAnnotationShouldFail() {

        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        injector.injectAnnotation(null);
    }

    @Test
    public void injectingNonExistingAnnotationShouldSucceed() {
        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        AnnotationA annot = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNull(annot);

        injector.injectAnnotation(annotA);

        annot = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annot);
        assertEquals(annotA, annot);
    }

    @Test
    public void injectingDifferentAnnotationShouldSucceed() {

        @AnnotationA
        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        injector.injectAnnotation(annotB1);

        AnnotationA annot1;
        annot1 = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annot1);
        AnnotationB annot2;
        annot2 = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annot2);
    }

    @Test
    public void injectingSameCompatibleAnnotationShouldSucceed() {

        @AnnotationA
        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        injector.injectAnnotation(annotA);

        AnnotationA annot = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annot);
    }

    @Test(expected = AnnotationInjectionException.class)
    public void injectingSameIncompatibleAnnotationShouldFail() {

        @AnnotationB("foo")
        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        injector.injectAnnotation(annotB2);
    }

    @Test
    public void injectingManyCompatibleAnnotationsShouldSucceed() {

        class TestPojo {
        }
        AnnotationInjector injector = new AnnotationInjector(TestPojo.class);

        Set<Annotation> annotations = new HashSet<Annotation>();
        annotations.add(annotA);
        annotations.add(annotB1);

        injector.injectAnnotations(annotations);

        AnnotationA annot1 = TestPojo.class.getAnnotation(AnnotationA.class);
        assertNotNull(annot1);
        AnnotationB annot2 = TestPojo.class.getAnnotation(AnnotationB.class);
        assertNotNull(annot2);
    }

}
