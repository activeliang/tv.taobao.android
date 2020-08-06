package com.taobao.ju.track.impl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.CsvFileUtil;
import com.taobao.ju.track.impl.interfaces.ICtrlTrack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CtrlTrackImpl extends TrackImpl implements ICtrlTrack {
    private static final String DEFAULT_FILE_NAME = "ut_ctrl.csv";
    private static final String KEY_PAGE = "_page";

    public CtrlTrackImpl(Context context) {
        super(context, DEFAULT_FILE_NAME);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CtrlTrackImpl(Context context, String csvFileName) {
        super(context, !CsvFileUtil.isCSV(csvFileName) ? DEFAULT_FILE_NAME : csvFileName);
    }

    public String getSpm(String rowKey) {
        return getSpm((String) null, rowKey);
    }

    public String getSpm(Activity activity, String rowKey) {
        return getSpm(activity != null ? activity.getClass().getSimpleName() : null, rowKey);
    }

    public String getSpm(String pageNameOrActivityName, String rowKey) {
        String pageRowKey;
        StringBuffer sb = new StringBuffer();
        Map<String, String> rowParams = getParamMap(rowKey);
        if (rowParams != null) {
            PageTrackImpl pageTrack = JTrack.Page.getTrack();
            if (pageNameOrActivityName == null) {
                pageRowKey = rowParams.get(KEY_PAGE);
            } else {
                pageRowKey = pageNameOrActivityName;
            }
            String spmAB = pageTrack.getSpmAB(pageRowKey);
            if (Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE.equals(spmAB)) {
                spmAB = pageTrack.getSpmAB(getTopActivityName());
            }
            sb.append(spmAB).append(".").append(getParamValue(rowParams, "_spmc", "0")).append(".").append(getParamValue(rowParams, "_spmd", "0"));
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return Constants.PARAM_OUTER_SPM_NONE;
    }

    public Map<String, String> getParamSpm(String rowKey) {
        Map<String, String> result = getParamSpmInternal(rowKey);
        result.put(Constants.PARAM_OUTER_SPM_URL, getSpm(rowKey));
        return result;
    }

    public Map<String, String> getParamSpm(Activity activity, String rowKey) {
        Map<String, String> result = getParamSpmInternal(rowKey);
        result.put(Constants.PARAM_OUTER_SPM_URL, getSpm(activity, rowKey));
        return result;
    }

    public Map<String, String> getParamSpm(String pageNameOrActivityName, String rowKey) {
        Map<String, String> result = getParamSpmInternal(rowKey);
        result.put(Constants.PARAM_OUTER_SPM_URL, getSpm(pageNameOrActivityName, rowKey));
        return result;
    }

    private Map<String, String> getParamSpmInternal(String rowKey) {
        Map<String, String> result = new HashMap<>();
        result.put("autosend", getParamValue(getParamMap(rowKey), "_autosend", (String) null));
        return result;
    }

    private String getTopActivityName() {
        ActivityManager am;
        ComponentName componentInfo;
        if (!(this.mContext == null || (am = (ActivityManager) this.mContext.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) == null)) {
            try {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                if (!(taskInfo == null || taskInfo.get(0) == null || (componentInfo = taskInfo.get(0).topActivity) == null)) {
                    String clazzName = componentInfo.getClassName();
                    return clazzName.substring(Math.max(0, clazzName.lastIndexOf(".") + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
