package com.ut.mini.exposure;

import android.text.TextUtils;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

public class ExposureUtils {
    protected static final String UT_EXPROSURE_ARGS = "UT_EXPROSURE_ARGS";
    protected static final String UT_EXPROSURE_BLOCK = "UT_EXPROSURE_BLOCK";
    protected static final String UT_EXPROSURE_VIEWID = "UT_EXPROSURE_VIEWID";
    public static final int ut_exprosure_common_info_tag = -17003;
    public static final int ut_exprosure_ignore_tag = -17004;
    public static final int ut_exprosure_tag = -17001;
    public static final int ut_exprosure_tag_for_weex = -17002;

    public static void setExposure(View view, String block, String viewId, Map<String, String> args) {
        if (view == null) {
            ExpLogger.w((String) null, "error,view is null");
        } else if (TextUtils.isEmpty(block)) {
            ExpLogger.w((String) null, "error,block is empty");
        } else if (TextUtils.isEmpty(viewId)) {
            ExpLogger.w((String) null, "error,viewId is empty");
        } else {
            Map<String, Object> exprosureData = new HashMap<>();
            exprosureData.put(UT_EXPROSURE_BLOCK, block);
            exprosureData.put(UT_EXPROSURE_VIEWID, viewId);
            if (args != null) {
                exprosureData.put(UT_EXPROSURE_ARGS, args);
            }
            view.setTag(ut_exprosure_tag, exprosureData);
        }
    }

    protected static void clearExposureForWeex(View view) {
        if (view == null) {
            ExpLogger.w((String) null, "error,view is null");
            return;
        }
        view.setTag(ut_exprosure_tag_for_weex, (Object) null);
    }

    public static void setExposureForWeex(View view) {
        if (view == null) {
            ExpLogger.w((String) null, "error,view is null");
            return;
        }
        view.setTag(ut_exprosure_tag_for_weex, "auto");
    }

    public static boolean isExposureViewForWeex(View view) {
        if (view == null || view.getTag(ut_exprosure_tag_for_weex) == null) {
            return false;
        }
        return true;
    }

    public static boolean isExposureView(View view) {
        if (view == null || view.getTag(ut_exprosure_tag) == null) {
            return false;
        }
        return true;
    }

    public static boolean isIngoneExposureView(View view) {
        if (view == null || view.getTag(ut_exprosure_ignore_tag) == null) {
            return false;
        }
        return true;
    }

    public static void setIgnoreTagForExposureView(View view) {
        if (view != null) {
            view.setTag(ut_exprosure_ignore_tag, "user");
        }
    }

    public static void clearIgnoreTagForExposureView(View view) {
        if (view != null) {
            view.setTag(ut_exprosure_ignore_tag, (Object) null);
        }
    }
}
