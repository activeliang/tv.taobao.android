package com.taobao.ju.track.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.CsvFileUtil;
import com.taobao.ju.track.impl.interfaces.IPageTrack;
import com.taobao.ju.track.interfaces.IJPageTrackProvider;
import com.taobao.ju.track.spm.SpmUtil;
import com.taobao.ju.track.util.BundleUtil;
import com.taobao.ju.track.util.JsonUtil;
import com.taobao.ju.track.util.ParamUtil;
import com.taobao.ju.track.util.UriUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PageTrackImpl extends TrackImpl implements IPageTrack {
    private static final String DEFAULT_FILE_NAME = "ut_page.csv";
    private static final String EMPTY_PAGE_NAME = "NullActivity";
    private static final String PARAM_INTERNAL_ARGS = "_args";

    public PageTrackImpl(Context context) {
        super(context, DEFAULT_FILE_NAME);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PageTrackImpl(Context context, String csvFileName) {
        super(context, !CsvFileUtil.isCSV(csvFileName) ? DEFAULT_FILE_NAME : csvFileName);
    }

    public String getPageName(String activityName) {
        String pageName = null;
        if (activityName != null) {
            pageName = getParamValue(activityName, Constants.CSV_PARAM_INTERNAL_KEY, activityName);
        }
        return pageName != null ? pageName : EMPTY_PAGE_NAME;
    }

    public String getPageName(Activity activity) {
        String rowNameOrDefaultValue;
        String pageName = null;
        if (activity instanceof IJPageTrackProvider) {
            pageName = ((IJPageTrackProvider) activity).getPageName();
        }
        if (activity != null) {
            String activityName = activity.getClass().getSimpleName();
            if (pageName != null) {
                rowNameOrDefaultValue = pageName;
            } else {
                rowNameOrDefaultValue = activityName;
            }
            pageName = getParamValue(rowNameOrDefaultValue, Constants.CSV_PARAM_INTERNAL_KEY, rowNameOrDefaultValue);
        }
        return pageName != null ? pageName : EMPTY_PAGE_NAME;
    }

    public String getSpm(String pageNameOrActivityName) {
        Map<String, String> rowParams = getParamMap(pageNameOrActivityName);
        if (rowParams == null || rowParams.size() == 0) {
            rowParams = getParamMap(getPageName(pageNameOrActivityName));
        }
        return super.getSpm(rowParams);
    }

    public String getSpm(Activity activity) {
        return getSpm(getPageName(activity));
    }

    public Properties getSpmAsProp(Activity activity) {
        Properties properties = new Properties();
        if (activity != null) {
            String activityName = getPageName(activity);
            properties.put(Constants.PARAM_OUTER_SPM_URL, SpmUtil.getPreSpm());
            Intent intent = activity.getIntent();
            if (ParamUtil.hasExtra(intent, Constants.PARAM_OUTER_SPM_URL)) {
                properties.put(Constants.PARAM_OUTER_SPM_URL, ParamUtil.getStringExtra(intent, Constants.PARAM_OUTER_SPM_URL));
            }
            properties.put("spm-cnt", getSpm(activityName));
        }
        return properties;
    }

    public HashMap<String, String> getSpmAsMap(Activity activity) {
        HashMap<String, String> props = new HashMap<>();
        if (activity != null) {
            String activityName = getPageName(activity);
            props.put(Constants.PARAM_OUTER_SPM_URL, SpmUtil.getPreSpm());
            Intent intent = activity.getIntent();
            if (ParamUtil.hasExtra(intent, Constants.PARAM_OUTER_SPM_URL)) {
                props.put(Constants.PARAM_OUTER_SPM_URL, ParamUtil.getStringExtra(intent, Constants.PARAM_OUTER_SPM_URL));
            }
            props.put("spm-cnt", getSpm(activityName));
        }
        return props;
    }

    public String getSpmAB(String pageNameOrActivityName) {
        Map<String, String> rowParams = getParamMap(pageNameOrActivityName);
        if (rowParams == null || rowParams.size() == 0) {
            rowParams = getParamMap(getPageName(pageNameOrActivityName));
        }
        return super.getSpmAB(rowParams);
    }

    public String getSpmAB(Activity activity) {
        return getSpmAB(getPageName(activity));
    }

    public String getArgs(String pageNameOrActivityName) {
        String paramValue = getParamValue(pageNameOrActivityName, PARAM_INTERNAL_ARGS);
        if (paramValue == null) {
            return getParamValue(getPageName(pageNameOrActivityName), PARAM_INTERNAL_ARGS);
        }
        return paramValue;
    }

    public String getArgs(Activity activity) {
        if (activity != null) {
            return getArgs(activity.getClass().getSimpleName());
        }
        return null;
    }

    public Map<String, String> getArgsMap(String pageNameOrActivityName, Bundle bundle) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(pageNameOrActivityName)), bundle);
    }

    public Map<String, String> getArgsMap(Activity activity, Bundle bundle) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(activity)), bundle);
    }

    public Map<String, String> getArgsMap(String pageNameOrActivityName, Uri uri) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(pageNameOrActivityName)), uri);
    }

    public Map<String, String> getArgsMap(Activity activity, Uri uri) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(activity)), uri);
    }

    private Map<String, String> doBundleValueMapping(Map<String, String> mapOfArgs, Bundle bundle) {
        if (mapOfArgs != null && mapOfArgs.size() > 0) {
            Iterator<Map.Entry<String, String>> it = mapOfArgs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(value) && value.startsWith("${") && value.endsWith("}") && value.length() > 2) {
                    String bundleValue = BundleUtil.getString(bundle, value.substring(2, value.length() - 1), "");
                    if (!TextUtils.isEmpty(bundleValue)) {
                        mapOfArgs.put(key, bundleValue);
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return mapOfArgs;
    }

    private Map<String, String> doBundleValueMapping(Map<String, String> mapOfArgs, Uri uri) {
        if (mapOfArgs != null && mapOfArgs.size() > 0) {
            Iterator<Map.Entry<String, String>> it = mapOfArgs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(value) && value.startsWith("${") && value.endsWith("}") && value.length() > 2) {
                    String bundleValue = UriUtil.getString(uri, value.substring(2, value.length() - 1), "");
                    if (!TextUtils.isEmpty(bundleValue)) {
                        mapOfArgs.put(key, bundleValue);
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return mapOfArgs;
    }

    @Deprecated
    private String getDefaultPageName(Object pageObject) {
        if (pageObject == null) {
            return EMPTY_PAGE_NAME;
        }
        String pageName = pageObject instanceof String ? String.valueOf(pageObject) : pageObject.getClass().getSimpleName();
        if (pageName == null || !pageName.toLowerCase().endsWith(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) {
            return pageName;
        }
        return pageName.substring(0, pageName.length() - 8);
    }
}
