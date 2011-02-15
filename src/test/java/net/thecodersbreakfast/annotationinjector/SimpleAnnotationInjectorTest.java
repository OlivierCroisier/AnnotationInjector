package net.thecodersbreakfast.annotationinjector;

import org.junit.BeforeClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class SimpleAnnotationInjectorTest {

    private static MyAnnotation myAnnotation;

    @BeforeClass
    public static void init() {

        myAnnotation = new MyAnnotation() {
            @Override
            public String message() {
                return defaultMessage;
            }

            @Override
            public int answer() {
                return defaultAnswer;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        Class<? extends Annotation> annotClass = myAnnotation.annotationType();
    }

    @org.junit.Test
    public void testClassInjection() {

        // Injection dans la classe TestPojo
        SimpleAnnotationInjector.injectClass(TestPojo.class, myAnnotation);

        // SimpleAnnotationInjectorTest
        MyAnnotation a = TestPojo.class.getAnnotation(MyAnnotation.class);
        assertNotNull("Annotation is missing", a);
        System.out.println("Annotation value is : " + a.message());

    }

    @org.junit.Test
    public void testFieldInjection() throws Exception {

        Field f1 = TestPojo.class.getDeclaredField("foo");
        SimpleAnnotationInjector.injectField(f1, myAnnotation);
        assertTrue(f1.isAnnotationPresent(MyAnnotation.class));

        Field f2 = TestPojo.class.getDeclaredField("foo");
        System.out.println("f1 == f2 ? " + (f1 == f2));
        assertFalse(f2.isAnnotationPresent(MyAnnotation.class));

    }

}
