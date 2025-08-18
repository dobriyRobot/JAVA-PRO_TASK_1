package org.semennikov.util;

import org.semennikov.exception.TestAssertionError;

import java.util.Collection;
import java.util.Objects;

public class AssertHelper {

    private final Object actual;

    public AssertHelper(Object actual) {
        this.actual = actual;
    }

    public void isEqualTo(Object expected) {
        if (!Objects.equals(actual, expected)) {
            throw new TestAssertionError("Expected " + expected + " but was " + actual);
        }
    }

    public void hasSize(int size) {
        if (actual instanceof Collection && ((Collection<?>) actual).size() != size) {
            throw new TestAssertionError("Expected size " + size + " but was " +
                    ((Collection<?>) actual).size());
        }
    }
}
