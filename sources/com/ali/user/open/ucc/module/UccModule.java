package com.ali.user.open.ucc.module;

import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.ucc.UccService;
import com.ali.user.open.ucc.UccServiceImpl;
import java.util.Map;

public class UccModule {
    public static void init() {
        KernelContext.registerService(new Class[]{UccService.class}, new UccServiceImpl(), (Map<String, String>) null);
    }
}
