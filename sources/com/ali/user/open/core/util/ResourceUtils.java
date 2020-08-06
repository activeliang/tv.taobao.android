package com.ali.user.open.core.util;

import android.content.Context;
import android.text.TextUtils;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;

public class ResourceUtils {
    public static final String TAG = "ResourceUtils";

    public static String getString(Context context, String name) {
        if (KernelContext.resources != null) {
            return KernelContext.resources.getString(getIdentifier(context, "string", name));
        }
        return context.getResources().getString(getIdentifier(context, "string", name));
    }

    public static String getString(String name) {
        return getString(KernelContext.getApplicationContext(), name);
    }

    public static int getIdentifier(Context context, String defType, String name) {
        String packageName;
        if (!TextUtils.isEmpty(KernelContext.packageName)) {
            packageName = KernelContext.packageName;
        } else {
            packageName = context.getPackageName();
        }
        SDKLogger.i("resources", "resources = " + KernelContext.resources + " packageName = " + KernelContext.packageName);
        if (KernelContext.resources != null) {
            return KernelContext.resources.getIdentifier(name, defType, packageName);
        }
        return context.getResources().getIdentifier(name, defType, packageName);
    }
}
