package com.devabit.takestock.util;

/**
 * Created by Victor Artemyev on 22/04/2016.
 */
public final class Preconditions {

    private Preconditions() {
        throw new AssertionError();
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
