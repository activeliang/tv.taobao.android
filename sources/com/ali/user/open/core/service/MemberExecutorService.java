package com.ali.user.open.core.service;

import android.os.Looper;

public interface MemberExecutorService {
    Looper getDefaultLooper();

    void postHandlerTask(Runnable runnable);

    void postTask(Runnable runnable);

    void postUITask(Runnable runnable);
}
