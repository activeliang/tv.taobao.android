package com.ali.user.open.oauth.module;

import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.oauth.OauthService;
import com.ali.user.open.oauth.OauthServiceImpl;
import java.util.Map;

public class OauthModule {
    public static void init() {
        KernelContext.registerService(new Class[]{OauthService.class}, new OauthServiceImpl(), (Map<String, String>) null);
    }
}
