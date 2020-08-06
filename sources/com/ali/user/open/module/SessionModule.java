package com.ali.user.open.module;

import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.service.impl.SessionManager;
import java.util.Map;

public class SessionModule {
    public static void init() {
        KernelContext.registerService(new Class[]{SessionService.class}, SessionManager.INSTANCE, (Map<String, String>) null);
    }
}
