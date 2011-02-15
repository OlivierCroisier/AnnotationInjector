package net.thecodersbreakfast.annotationinjector.demo;

import net.thecodersbreakfast.annotationinjector.InheritedAnnotationsInjector;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InjectorDemo {

    @Test
    public void testInjector() {

        InheritedAnnotationsInjector injector = new InheritedAnnotationsInjector(Pojo.class);

        assertFalse(Pojo.class.isAnnotationPresent(MyAnnotation.class));

        injector.injectInterfaceInheritedAnnotations();
        assertTrue(Pojo.class.isAnnotationPresent(MyAnnotation.class));
        MyAnnotation annotation = Pojo.class.getAnnotation(MyAnnotation.class);
        System.out.println(annotation.message());

        injector.injectPackageInheritedAnnotations();
        assertTrue(Pojo.class.isAnnotationPresent(MyAnnotation.class));
    }

}
