package com.ta.audid.permission;

import android.content.Context;
import android.os.Build;

final class AppOpsManagerCompat {
    private static final AppOpsManagerImpl IMPL;
    public static final int MODE_IGNORED = 1;

    private static class AppOpsManagerImpl {
        private AppOpsManagerImpl() {
        }

        public String permissionToOp(String permission) {
            return null;
        }

        public int noteOp(Context context, String op, int uid, String packageName) {
            return 1;
        }

        public int noteProxyOp(Context context, String op, String proxiedPackageName) {
            return 1;
        }
    }

    private static class AppOpsManager23 extends AppOpsManagerImpl {
        private AppOpsManager23() {
            super();
        }

        public String permissionToOp(String permission) {
            return AppOpsManagerCompat23.permissionToOp(permission);
        }

        public int noteOp(Context context, String op, int uid, String packageName) {
            return AppOpsManagerCompat23.noteOp(context, op, uid, packageName);
        }

        public int noteProxyOp(Context context, String op, String proxiedPackageName) {
            return AppOpsManagerCompat23.noteProxyOp(context, op, proxiedPackageName);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 23) {
            IMPL = new AppOpsManager23();
        } else {
            IMPL = new AppOpsManagerImpl();
        }
    }

    private AppOpsManagerCompat() {
    }

    public static String permissionToOp(String permission) {
        return IMPL.permissionToOp(permission);
    }

    public static int noteOp(Context context, String op, int uid, String packageName) {
        return IMPL.noteOp(context, op, uid, packageName);
    }

    public static int noteProxyOp(Context context, String op, String proxiedPackageName) {
        return IMPL.noteProxyOp(context, op, proxiedPackageName);
    }
}
