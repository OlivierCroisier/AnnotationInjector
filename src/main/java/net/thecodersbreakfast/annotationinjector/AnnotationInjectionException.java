package net.thecodersbreakfast.annotationinjector;

/**
 * @author Olivier Croisier
 * @version $Id: AnnotationInjectionException.java 1921 2010-12-13 10:07:01Z OlivierCroisier $
 */
public class AnnotationInjectionException extends RuntimeException {

    public AnnotationInjectionException() {
    }

    public AnnotationInjectionException(String message) {
        super(message);
    }

    public AnnotationInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationInjectionException(Throwable cause) {
        super(cause);
    }
}
