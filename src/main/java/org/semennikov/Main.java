package org.semennikov;

import org.semennikov.model.TestDto;
import org.semennikov.model.TestResult;
import org.semennikov.service.MyTest;
import org.semennikov.service.TestRunner;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<TestResult, List<TestDto>> results = TestRunner.runTests(MyTest.class);

        for (TestResult result : TestResult.values()) {
            List<TestDto> testsInGroup = results.get(result);
            if (!testsInGroup.isEmpty()) {
                System.out.println("\n" + result.getColoredMessage(
                        result.getDisplayName() + " (" + testsInGroup.size() + "):"));

                for (TestDto test : testsInGroup) {
                    System.out.println("  - " + test.getName());
                    if (test.getException() != null) {
                        System.out.println("    " + result.getColorCode() +
                                "Reason: " + test.getException().getMessage() + "\u001B[0m");
                    }
                }
            }
        }
    }
}
