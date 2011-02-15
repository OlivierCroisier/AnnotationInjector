package net.thecodersbreakfast.annotationinjector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Olivier Croisier
 * @version $Id: NonInheritableAnnotation.java 1924 2010-12-14 16:05:55Z OlivierCroisier $
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NonInheritableAnnotation {
}
