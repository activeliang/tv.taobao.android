package com.taobao.atlas.dexmerge.dx.util;

import com.taobao.atlas.dex.util.ExceptionWithContext;

public class MutabilityException extends ExceptionWithContext {
    public MutabilityException(String message) {
        super(message);
    }

    public MutabilityException(Throwable cause) {
        super(cause);
    }

    public MutabilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
