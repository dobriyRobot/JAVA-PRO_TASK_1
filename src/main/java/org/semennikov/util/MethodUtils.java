package org.semennikov.util;

import org.semennikov.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public final class MethodUtils {

    private MethodUtils() {}

    public static List<Method> getBeforeSuiteMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(BeforeSuite.class))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .toList();
    }

    public static List<Method> getAfterSuiteMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AfterSuite.class))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .toList();
    }

    public static List<Method> getBeforeEachMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .toList();
    }

    public static List<Method> getAfterEachMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AfterEach.class))
                .toList();
    }

    public static String getMethodName(Method method) {
        return method.getAnnotation(Test.class).name().isBlank()
                ? method.getName()
                : method.getAnnotation(Test.class).name();
    }
}
