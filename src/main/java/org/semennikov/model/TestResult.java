package org.semennikov.model;

public enum TestResult {
    SUCCESS("\u001B[32m", "SUCCESS"),
    FAILED("\u001B[31m", "FAILED"),
    ERROR("\u001B[33m", "ERROR"),
    SKIPPED("\u001B[36m", "SKIPPED");

    private final String colorCode;
    private final String displayName;

    TestResult(String colorCode, String displayName) {
        this.colorCode = colorCode;
        this.displayName = displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColoredMessage(String message) {
        return colorCode + message + "\u001B[0m";
    }
}
