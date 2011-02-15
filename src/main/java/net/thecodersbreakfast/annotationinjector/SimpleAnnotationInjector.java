package net.thecodersbreakfast.annotationinjector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleAnnotationInjector {

    public static boolean injectClass(Class<?> targetClass, Annotation annotation) {

        // Initialisation des maps d'annotations
        targetClass.getAnnotations();

        try {
            // Récupération de la map interne des annotations
            Field annotationFieldRef = Class.class.getDeclaredField("annotations");
            annotationFieldRef.setAccessible(true);

            // Modification de la map des annotations
            @SuppressWarnings("unchecked")
            Map<Class, Annotation> annotationMap = (Map<Class, Annotation>) annotationFieldRef.get(targetClass);
            if (annotationMap == null || annotationMap.isEmpty()) {
                annotationMap = new HashMap<Class, Annotation>();
            }
            annotationMap.put(annotation.annotationType(), annotation);
            annotationFieldRef.set(targetClass, annotationMap);

            return true;
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        return false;
    }

    public static boolean injectField(Field field, Annotation annotation) {

        try {

            // Récupération de la map interne des annotations 
            Field annotationFieldRef = Field.class.getDeclaredField("declaredAnnotations");
            annotationFieldRef.setAccessible(true);

            // Modification de la map des annotations
            @SuppressWarnings("unchecked")
            Map<Class, Annotation> annotationMap = (Map<Class, Annotation>) annotationFieldRef.get(field);
            if (annotationMap == null || annotationMap.isEmpty()) {
                annotationMap = new HashMap<Class, Annotation>();
            }
            annotationMap.put(annotation.annotationType(), annotation);
            annotationFieldRef.set(field, annotationMap);

            return true;

        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        return false;

    }

}
