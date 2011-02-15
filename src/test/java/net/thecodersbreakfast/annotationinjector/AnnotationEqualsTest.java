package net.thecodersbreakfast.annotationinjector;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Olivier Croisier
 * @version $Id: AnnotationEqualsTest.java 1921 2010-12-13 10:07:01Z OlivierCroisier $
 */
public class AnnotationEqualsTest {

    static AnnotationA a1, a2;
    static AnnotationB b1, b2, b3;

    @BeforeClass
    public static void setup() {

        a1 = new AnnotationA() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationA.class;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof AnnotationA;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };

        a2 = new AnnotationA() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationA.class;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof AnnotationA;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };

        b1 = new AnnotationB() {
            @Override
            public String value() {
                return "foo";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationB.class;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof AnnotationB && ((AnnotationB) obj).value().equals(value());
            }

            @Override
            public int hashCode() {
                return 127 * ("value".hashCode() ^ value().hashCode());
            }
        };

        b2 = new AnnotationB() {
            @Override
            public String value() {
                return "foo";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationB.class;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof AnnotationB && ((AnnotationB) obj).value().equals(value());
            }

            @Override
            public int hashCode() {
                return 127 * ("value".hashCode() ^ value().hashCode());
            }
        };

        b3 = new AnnotationB() {
            @Override
            public String value() {
                return "bar";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotationB.class;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof AnnotationB && ((AnnotationB) obj).value().equals(value());
            }

            @Override
            public int hashCode() {
                return 127 * ("value".hashCode() ^ value().hashCode());
            }
        };

    }

    @Test
    public void testAnnotationsWithoutParametersAreEqual() {
        assertEquals(a1, a2);
    }

    @Test
    public void testAnnotationsWithEqualParametersAreEqual() {
        assertEquals(b1, b2);
    }

    @Test
    public void testAnnotationsWithDifferentParametersAreEqual() {
        assertFalse(b1.equals(b3));
    }

}
