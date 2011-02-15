package net.thecodersbreakfast.annotationinjector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {

    String defaultMessage = "Hello World";
    int defaultAnswer = 42;

    String message() default defaultMessage;

    int answer() default defaultAnswer;

}