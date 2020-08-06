package com.alibaba.appmonitor.delegate;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.ReflectUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SdkMeta {
    private static final String TAG = "SdkMeta";
    private static final Map<String, String> meta = new ConcurrentHashMap();

    public static Map<String, String> getSDKMetaData() {
        Context context = Variables.getInstance().getContext();
        if (context != null) {
            if (!meta.containsKey("pt")) {
                String pt = getString(context, "package_type");
                if (!TextUtils.isEmpty(pt)) {
                    meta.put("pt", pt);
                } else {
                    meta.put("pt", "");
                }
            }
            if (!meta.containsKey("pid")) {
                String pid = getString(context, "project_id");
                if (!TextUtils.isEmpty(pid)) {
                    meta.put("pid", pid);
                } else {
                    meta.put("pid", "");
                }
            }
            if (!meta.containsKey("bid")) {
                String bid = getString(context, "build_id");
                if (!TextUtils.isEmpty(bid)) {
                    meta.put("bid", bid);
                } else {
                    meta.put("bid", "");
                }
            }
            if (!meta.containsKey("bv")) {
                String bv = getString(context, "base_version");
                if (!TextUtils.isEmpty(bv)) {
                    meta.put("bv", bv);
                } else {
                    meta.put("bv", "");
                }
            }
        }
        String hv = getHotPatchVersion();
        if (!TextUtils.isEmpty(hv)) {
            meta.put("hv", hv);
        } else {
            meta.put("hv", "");
        }
        if (!meta.containsKey("sdk-version")) {
            meta.put("sdk-version", UTBuildInfo.getInstance().getFullSDKVersion());
        }
        return meta;
    }

    private static String getHotPatchVersion() {
        Object retValue;
        try {
            Object instance = ReflectUtils.invokeStaticMethod("com.taobao.updatecenter.hotpatch.HotPatchManager", "getInstance");
            if (instance == null || (retValue = ReflectUtils.invokeMethod(instance, "getPatchSuccessedVersion")) == null) {
                return null;
            }
            return retValue + "";
        } catch (Throwable th) {
            return null;
        }
    }

    static {
        meta.put("sdk-version", UTBuildInfo.getInstance().getFullSDKVersion());
    }

    public static String getString(Context context, String paramString) {
        if (context == null) {
            return null;
        }
        int id = 0;
        try {
            id = context.getResources().getIdentifier(paramString, "string", context.getPackageName());
        } catch (Throwable e) {
            Logger.w(TAG, "getString Id error", e);
        }
        if (id != 0) {
            return context.getString(id);
        }
        return null;
    }
}
