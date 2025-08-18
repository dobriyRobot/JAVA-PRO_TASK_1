package org.semennikov.service;

import org.semennikov.annotation.*;
import org.semennikov.exception.BadTestClassError;
import org.semennikov.exception.TestAssertionError;
import org.semennikov.model.TestDto;
import org.semennikov.model.TestResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.semennikov.util.MethodUtils.*;

public class TestRunner {

    public static Map<TestResult, List<TestDto>> runTests(Class<?> clz) {
        Map<TestResult, List<TestDto>> resultMap = Arrays.stream(TestResult.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        result -> new ArrayList<>(),
                        (a, b) -> a,
                        () -> new EnumMap<>(TestResult.class)));


        try {
            checkTestClass(clz);
            Object obj = clz.getDeclaredConstructor().newInstance();
            List<Method> sortedTestMethods = Arrays.stream(clz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Test.class))
                    .sorted((m1, m2) -> {
                        Test a1 = m1.getAnnotation(Test.class);
                        Test a2 = m2.getAnnotation(Test.class);
                        int priorityCompare = Integer.compare(a1.priority(), a2.priority());
                        return priorityCompare != 0 ? priorityCompare : m1.getName().compareTo(m2.getName());
                    })
                    .toList();

            // BeforeSuite
            for (Method beforeSuiteMethod : getBeforeSuiteMethods(clz.getDeclaredMethods())) {
                beforeSuiteMethod.invoke(obj);
            }

            for (Method method : sortedTestMethods) {
                String methodName = getMethodName(method);
                if (method.isAnnotationPresent(Test.class)) {
                    if (method.isAnnotationPresent(Disabled.class)) {
                        resultMap.get(TestResult.SKIPPED)
                                .add(new TestDto(TestResult.SKIPPED, methodName, null));
                        continue;
                    }
                    TestDto result = executeMethod(method, obj, clz);
                    resultMap.get(result.getResult()).add(result);
                }
            }

            // AfterSuite
            for (Method afterSuiteMethod : getAfterSuiteMethods(clz.getDeclaredMethods())) {
                afterSuiteMethod.invoke(obj);
            }
        } catch (Exception e) {
            throw new BadTestClassError("Ошибка запуска тестов: " + e.getMessage());
        }

        return resultMap;
    }

    private static TestDto executeMethod(Method method, Object instance, Class<?> clz) {
        String methodName = getMethodName(method);
        try {
            // BeforeEach
            for (Method beforeEachMethod : getBeforeEachMethods(clz.getDeclaredMethods())) {
                beforeEachMethod.invoke(instance);
            }

            method.invoke(instance);

            // AfterEach
            for (Method afterEachMethod : getAfterEachMethods(clz.getDeclaredMethods())) {
                afterEachMethod.invoke(instance);
            }

            return new TestDto(TestResult.SUCCESS, methodName, null);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            TestResult result = (cause instanceof TestAssertionError)
                    ? TestResult.FAILED
                    : TestResult.ERROR;
            return new TestDto(result, methodName, cause);
        } catch (Exception e) {
            return new TestDto(TestResult.ERROR, methodName, e);
        }
    }

    private static void checkTestClass(Class<?> clz) {
        try {
            clz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new BadTestClassError("Конструктор должен быть без аргументов");
        }

        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class) || method.isAnnotationPresent(AfterSuite.class)) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@BeforeSuite и @AfterSuite должны аннотировать статические методы");
                }
            }

            if ((method.isAnnotationPresent(BeforeEach.class) || method.isAnnotationPresent(AfterEach.class)
                    || method.isAnnotationPresent(Test.class)) && Modifier.isStatic(method.getModifiers())) {
                throw new BadTestClassError("@BeforeEach, @AfterEach and @Test должны аннотировать не статичные метожы");
            }
        }
    }
}
