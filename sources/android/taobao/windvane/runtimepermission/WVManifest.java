package android.taobao.windvane.runtimepermission;

import android.content.Context;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

class WVManifest {
    WVManifest() {
    }

    static final class Permission {
        public static final HashMap<String, String> sPermissionMapping = new HashMap<>();

        static {
            sPermissionMapping.put("android.permission.READ_CONTACTS", "OP_READ_CONTACTS");
            sPermissionMapping.put("android.permission.CAMERA", "OP_CAMERA");
            sPermissionMapping.put("android.permission.WRITE_SETTINGS", "OP_WRITE_SETTINGS");
            sPermissionMapping.put("android.permission.ACCESS_COARSE_LOCATION", "OP_COARSE_LOCATION");
            sPermissionMapping.put("android.permission.ACCESS_FINE_LOCATION", "OP_FINE_LOCATION");
            sPermissionMapping.put("android.permission.VIBRATE", "OP_VIBRATE");
            sPermissionMapping.put("android.permission.SYSTEM_ALERT_WINDOW", "OP_SYSTEM_ALERT_WINDOW");
            sPermissionMapping.put("android.permission.RECORD_AUDIO", "OP_RECORD_AUDIO");
            sPermissionMapping.put("android.permission.WAKE_LOCK", "OP_WAKE_LOCK");
        }

        Permission() {
        }

        public static boolean isPermissionGranted(Context context, String[] permissions) {
            Object object = context.getSystemService("appops");
            try {
                Method e = object.getClass().getDeclaredMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class});
                e.setAccessible(true);
                for (String permission : permissions) {
                    String internalPermission = sPermissionMapping.get(permission);
                    if (internalPermission != null) {
                        Field field = object.getClass().getDeclaredField(internalPermission);
                        field.setAccessible(true);
                        if (((Integer) e.invoke(object, new Object[]{Integer.valueOf(((Integer) field.get(object)).intValue()), Integer.valueOf(context.getApplicationInfo().uid), context.getPackageName()})).intValue() != 0) {
                            return false;
                        }
                    }
                }
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }
}
