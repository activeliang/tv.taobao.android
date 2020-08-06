package com.taobao.ju.track.spm;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.param.JParamBuilder;
import com.taobao.ju.track.util.IntentUtil;
import com.taobao.ju.track.util.ParamUtil;
import java.util.HashMap;
import java.util.Map;

public class SpmUtil {
    private static final String PARAM_SPM = "spm";
    private static String sPreSpm = null;
    private static boolean sUsePreSpm = false;

    public static void setPreSpm(String preSpm) {
        sPreSpm = preSpm;
    }

    public static void setUsePreSpm() {
        sUsePreSpm = true;
    }

    public static String getPreSpm() {
        if (!sUsePreSpm) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        sUsePreSpm = false;
        return !TextUtils.isEmpty(sPreSpm) ? sPreSpm : Constants.PARAM_OUTER_SPM_NONE;
    }

    public static void clearPreSpm() {
        sPreSpm = null;
    }

    public static boolean isSpmValid(String spm) {
        return spm != null && !spm.equals(Constants.PARAM_OUTER_SPM_NONE) && !spm.equals(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE);
    }

    public static String getSPM(JParamBuilder builder) {
        if (builder != null) {
            String spm = builder.getParamValue(Constants.PARAM_OUTER_SPM_URL);
            if (!TextUtils.isEmpty(spm)) {
                return spm;
            }
        }
        return Constants.PARAM_OUTER_SPM_NONE;
    }

    public static String getSpmFromUrl(String url) {
        if (url == null) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        Uri uri = Uri.parse(url);
        if (!uri.toString().contains("spm") || TextUtils.isEmpty(uri.getQueryParameter("spm"))) {
            return Constants.PARAM_OUTER_SPM_NONE;
        }
        return uri.getQueryParameter("spm");
    }

    public static String addSpm(String url, String spm_url) {
        Uri uri;
        if (url == null || (uri = Uri.parse(url)) == null) {
            return url;
        }
        String oldSPM = uri.getQueryParameter("spm");
        if (oldSPM == null || oldSPM.indexOf(Constants.PARAM_OUTER_SPM_NONE) != -1) {
            return uri.buildUpon().appendQueryParameter("spm", spm_url).build().toString();
        }
        return url;
    }

    public static String addSpm(String url, Intent intent) {
        if (ParamUtil.hasExtra(intent, Constants.PARAM_OUTER_SPM_URL)) {
            return addSpm(url, ParamUtil.getStringExtra(intent, Constants.PARAM_OUTER_SPM_URL));
        }
        String preSpm = getPreSpm();
        if (isSpmValid(preSpm)) {
            return addSpm(url, preSpm);
        }
        return url;
    }

    public static Map addSpm(Map map, JParamBuilder jParamBuilder) {
        if (jParamBuilder != null) {
            if (map == null) {
                map = new HashMap();
            }
            String spm = getSPM(jParamBuilder);
            if (isSpmValid(spm)) {
                map.put(Constants.PARAM_OUTER_SPM_URL, spm);
            } else {
                map.put(Constants.PARAM_OUTER_SPM_URL, getPreSpm());
            }
        }
        return map;
    }

    public static Intent addSpm(Intent intent, JParamBuilder jParamBuilder) {
        if (jParamBuilder != null) {
            String spm = jParamBuilder.getSpm();
            if (isSpmValid(spm)) {
                intent.putExtra(Constants.PARAM_OUTER_SPM_URL, spm);
            } else {
                intent.putExtra(Constants.PARAM_OUTER_SPM_URL, getPreSpm());
            }
        }
        return intent;
    }

    public static Intent addSpm(Intent intent, String spmOrCSVKey) {
        if (intent != null) {
            String data = JTrack.Ctrl.getSpm(IntentUtil.getComponentSimpleClassName(intent), spmOrCSVKey);
            if (isSpmValid(data)) {
                intent.putExtra(Constants.PARAM_OUTER_SPM_URL, data);
            } else if (isSpmValid(spmOrCSVKey)) {
                intent.putExtra(Constants.PARAM_OUTER_SPM_URL, spmOrCSVKey);
            } else {
                intent.putExtra(Constants.PARAM_OUTER_SPM_URL, getPreSpm());
            }
        }
        return intent;
    }

    private static String getABOfSpm(String spm) {
        int firstDot;
        int secondDot;
        if (spm == null || (firstDot = spm.indexOf(46)) == -1 || (secondDot = spm.indexOf(46, firstDot)) == -1) {
            return null;
        }
        spm.substring(0, secondDot);
        return null;
    }
}
