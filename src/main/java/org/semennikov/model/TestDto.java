package org.semennikov.model;

public class TestDto {
    private final TestResult result;
    private final String name;
    private final Throwable exception;

    public TestDto(TestResult result, String name, Throwable exception) {
        this.result = result;
        this.name = name;
        this.exception = exception;
    }

    public TestResult getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public Throwable getException() {
        return exception;
    }
}
