package com.ali.auth.third.core.util;

import android.content.Context;
import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.trace.SDKLogger;

public class ResourceUtils {
    public static final String TAG = "ResourceUtils";

    public static String getString(Context context, String name) {
        if (KernelContext.resources != null) {
            return KernelContext.resources.getString(getIdentifier(context, "string", name));
        }
        return context.getResources().getString(getIdentifier(context, "string", name));
    }

    public static int getRLayout(Context context, String name) {
        return getIdentifier(context, "layout", name);
    }

    public static int getRDrawable(Context context, String name) {
        return getIdentifier(context, "drawable", name);
    }

    public static String getString(String name) {
        return getString(KernelContext.getApplicationContext(), name);
    }

    public static int getRLayout(String name) {
        return getIdentifier(KernelContext.getApplicationContext(), "layout", name);
    }

    public static int getRId(Context context, String name) {
        return getIdentifier(context, "id", name);
    }

    public static float getDimen(Context context, String name) {
        if (KernelContext.resources != null) {
            return KernelContext.resources.getDimension(getIdentifier(context, "dimen", name));
        }
        return context.getResources().getDimension(getIdentifier(context, "dimen", name));
    }

    public static int getIdentifier(String defType, String name) {
        return getIdentifier(KernelContext.getApplicationContext(), defType, name);
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
