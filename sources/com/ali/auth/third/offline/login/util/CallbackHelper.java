package com.ali.auth.third.offline.login.util;

import com.ali.auth.third.core.callback.FailureCallback;
import com.ali.auth.third.core.context.KernelContext;

public class CallbackHelper {
    public static void onFailure(final int code, final String message, final FailureCallback callback) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                if (callback != null) {
                    callback.onFailure(code, message);
                }
            }
        });
    }
}
