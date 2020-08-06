package com.taobao.taobaoavsdk.cache.library;

final class Preconditions {
    Preconditions() {
    }

    static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }

    static void checkAllNotNull(Object... references) {
        for (Object reference : references) {
            if (reference == null) {
                throw new NullPointerException();
            }
        }
    }

    static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(errorMessage);
    }

    static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
