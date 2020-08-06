package com.ali.user.sso.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.taobao.windvane.config.WVConfigManager;
import java.util.ArrayList;

public final class PermissionUtils {
    public static final int PERMISSION_REQUEST_CODE = 16;
    public static final int PERMISSION_SETTING_REQ_CODE = 4096;
    private static final String TAG = "PermissionUtils";

    @TargetApi(23)
    public static boolean checkPermission(Object cxt, String permission, int requestCode) {
        if (checkSelfPermissionWrapper(cxt, permission)) {
            return true;
        }
        if (!shouldShowRequestPermissionRationaleWrapper(cxt, permission)) {
            requestPermissionsWrapper(cxt, new String[]{permission}, requestCode);
            return false;
        }
        openSettingActivity(cxt);
        return false;
    }

    @TargetApi(23)
    private static void requestPermissionsWrapper(Object cxt, String[] permission, int requestCode) {
        if (cxt instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) cxt, permission, requestCode);
        } else if (cxt instanceof Fragment) {
            ((Fragment) cxt).requestPermissions(permission, requestCode);
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationaleWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) cxt, permission);
        }
        if (cxt instanceof Fragment) {
            return ((Fragment) cxt).shouldShowRequestPermissionRationale(permission);
        }
        throw new RuntimeException("cxt is net a activity or fragment");
    }

    @TargetApi(23)
    private static boolean checkSelfPermissionWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            if (ActivityCompat.checkSelfPermission((Activity) cxt, permission) == 0) {
                return true;
            }
            return false;
        } else if (!(cxt instanceof Fragment)) {
            throw new RuntimeException("cxt is net a activity or fragment");
        } else if (((Fragment) cxt).getActivity().checkSelfPermission(permission) != 0) {
            return false;
        } else {
            return true;
        }
    }

    private static String[] checkSelfPermissionArray(Object cxt, String[] permission) {
        ArrayList<String> permiList = new ArrayList<>();
        for (String p : permission) {
            if (!checkSelfPermissionWrapper(cxt, p)) {
                permiList.add(p);
            }
        }
        return (String[]) permiList.toArray(new String[permiList.size()]);
    }

    @TargetApi(23)
    public static boolean checkPermissionArray(Object cxt, String[] permission, int requestCode) {
        String[] permissionNo = checkSelfPermissionArray(cxt, permission);
        if (permissionNo.length <= 0) {
            return true;
        }
        requestPermissionsWrapper(cxt, permissionNo, requestCode);
        return false;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    private static void openSettingActivity(Object context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        if (context instanceof Activity) {
            intent.setData(Uri.fromParts(WVConfigManager.CONFIGNAME_PACKAGE, ((Activity) context).getPackageName(), (String) null));
            ((Activity) context).startActivity(intent);
        } else if (context instanceof Fragment) {
            intent.setData(Uri.fromParts(WVConfigManager.CONFIGNAME_PACKAGE, ((Fragment) context).getActivity().getPackageName(), (String) null));
            ((Fragment) context).getActivity().startActivity(intent);
        }
    }
}
