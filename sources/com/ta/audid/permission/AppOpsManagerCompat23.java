package com.ta.audid.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;

@TargetApi(23)
class AppOpsManagerCompat23 {
    AppOpsManagerCompat23() {
    }

    public static String permissionToOp(String permission) {
        return AppOpsManager.permissionToOp(permission);
    }

    public static int noteOp(Context context, String op, int uid, String packageName) {
        return ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOp(op, uid, packageName);
    }

    public static int noteProxyOp(Context context, String op, String proxiedPackageName) {
        return ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteProxyOp(op, proxiedPackageName);
    }
}
