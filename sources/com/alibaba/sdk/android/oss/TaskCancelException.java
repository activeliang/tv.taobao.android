package com.alibaba.sdk.android.oss;

public class TaskCancelException extends Exception {
    public TaskCancelException() {
    }

    public TaskCancelException(String message) {
        super("[ErrorMessage]: " + message);
    }

    public TaskCancelException(Throwable cause) {
        super(cause);
    }
}
