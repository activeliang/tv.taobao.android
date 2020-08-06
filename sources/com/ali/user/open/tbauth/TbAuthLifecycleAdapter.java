package com.ali.user.open.tbauth;

import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.tbauth.handler.TbAuthActivityResultHandler;
import com.ali.user.open.tbauth.ui.support.ActivityResultHandler;
import java.util.Map;

public class TbAuthLifecycleAdapter {
    public static final String TAG = "Member.TbAuthLifecycleAdapter";

    public static void init() {
        SDKLogger.d(TAG, "LoginLifecycle init ");
        Class[] clsArr = {ActivityResultHandler.class};
        KernelContext.registerService(clsArr, new TbAuthActivityResultHandler(), (Map<String, String>) null);
        Class[] clsArr2 = {TbAuthService.class};
        KernelContext.registerService(clsArr2, new TbAuthServiceImpl(), (Map<String, String>) null);
    }
}
