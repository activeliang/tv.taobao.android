package io.reactivex.exceptions;

import io.reactivex.annotations.Beta;
import io.reactivex.annotations.NonNull;

@Beta
public final class OnErrorNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -6298857009889503852L;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnErrorNotImplementedException(String message, @NonNull Throwable e) {
        super(message, e == null ? new NullPointerException() : e);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OnErrorNotImplementedException(@NonNull Throwable e) {
        super(e != null ? e.getMessage() : null, e == null ? new NullPointerException() : e);
    }
}
