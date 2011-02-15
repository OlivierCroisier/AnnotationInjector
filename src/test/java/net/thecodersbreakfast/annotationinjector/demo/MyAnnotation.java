package net.thecodersbreakfast.annotationinjector.demo;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyAnnotation {
    String message() default "Hello World";
}
