package com.ali.auth.third.offline.context;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.support.ActivityResultHandler;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class CallbackContext {
    private static final String TAG = CallbackContext.class.getSimpleName();
    public static volatile WeakReference<Activity> activity;
    public static volatile Object loginCallback;
    public static LoginCallback mGlobalLoginCallback;

    public static void setActivity(Activity activity2) {
        activity = new WeakReference<>(activity2);
    }

    public static boolean onActivityResult(Activity curActivity, int requestCode, int resultCode, Intent data) {
        SDKLogger.d(TAG, "onActivityResult requestCode=" + requestCode + " resultCode = " + resultCode + " authCode = " + (data == null ? "" : data.getStringExtra("result")));
        Map<String, String> activityResultServiceFilters = new HashMap<>();
        activityResultServiceFilters.put(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(requestCode));
        ActivityResultHandler activityResultHandler = (ActivityResultHandler) KernelContext.getService(ActivityResultHandler.class, activityResultServiceFilters);
        if (activityResultHandler == null) {
            SDKLogger.i(TAG, "No ActivityResultHandler handler to support the request code " + requestCode);
            return false;
        } else if (curActivity == null) {
            SDKLogger.e(TAG, "No active activity is set, ignore invoke " + activityResultHandler);
            return false;
        } else {
            activityResultHandler.onActivityResult(1, requestCode, resultCode, data, curActivity, (Map<Class<?>, Object>) null, (WebView) null);
            return true;
        }
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return onActivityResult(activity != null ? (Activity) activity.get() : null, requestCode, resultCode, data);
    }
}
