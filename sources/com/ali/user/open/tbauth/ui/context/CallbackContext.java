package com.ali.user.open.tbauth.ui.context;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.tbauth.ui.support.ActivityResultHandler;
import java.lang.ref.WeakReference;
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
        ActivityResultHandler activityResultHandler = (ActivityResultHandler) KernelContext.getService(ActivityResultHandler.class);
        if (activityResultHandler == null) {
            SDKLogger.i(TAG, "No ActivityResultHandler handler to support the request code " + requestCode);
            return false;
        } else if (curActivity == null) {
            SDKLogger.e(TAG, "No active activity is set, ignore post " + activityResultHandler);
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
