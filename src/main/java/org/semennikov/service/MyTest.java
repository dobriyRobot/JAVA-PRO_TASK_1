package org.semennikov.service;

import org.semennikov.annotation.*;
import org.semennikov.util.AssertHelper;

import java.util.ArrayList;
import java.util.List;

public class MyTest {

    // ==================== УСПЕШНЫЕ ТЕСТЫ ====================
    @Test(priority = 1, name = "Тест для проверки сложения")
    public void additionTest() {
        assertThat(2 + 2).isEqualTo(4);
    }
    @Test(priority = 2)
    public void stringConcatenationTest() {
        String result = "Hello" + " " + "World";
        assertThat(result).isEqualTo("Hello World");
    }
    @Test
    public void listOperationsTest() {
        List<String> list = new ArrayList<>();
        list.add("item1");
        assertThat(list).hasSize(1);
    }
    // ==================== ПРОВАЛЕННЫЕ ТЕСТЫ ====================
    @Test(priority = 3, name = "Тест не успешного сравнения")
    public void failingTest() {
        assertThat(1).isEqualTo(2);
    }
    @Test(priority = 7)
    public void arrayComparisonTest() {
        int[] actual = {1, 2, 3};
        int[] expected = {1, 2, 4};
        assertThat(actual).isEqualTo(expected);
    }
    // ==================== ТЕСТЫ С ОШИБКАМИ ====================
    @Test
    public void exceptionThrowingTest() {
        throw new RuntimeException("Неизвестная ошибка");
    }
    @Test(name = "Тест деления на ноль")
    public void divisionByZeroTest() {
        int result = 10 / 0;
    }
    // ==================== ПРОПУЩЕННЫЕ ТЕСТЫ ====================
    @Test
    @Disabled
    public void skippedTest1() {
        System.out.println("This test should be skipped");
    }
    @Disabled
    @Test(name = "Disabled feature test")
    public void skippedTestWithReason() {
        // Test for unimplemented feature
    }
    // ==================== МЕТОДЫ ДЛЯ НАСТРОЙКИ ТЕСТОВ ====================
    @BeforeSuite
    public static void globalSetup() {
        System.out.println("Global setup before all tests");
    }
    @AfterSuite
    public static void globalTeardown() {
        System.out.println("Global teardown after all tests");
    }
    @BeforeEach
    public void testSetup() {
        System.out.println("Setup before each test");
    }
    @AfterEach
    public void testCleanup() {
        System.out.println("Cleanup after each test");
    }

    private AssertHelper assertThat(Object actual) {
        return new AssertHelper(actual);
    }
}
