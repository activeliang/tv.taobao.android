package com.taobao.ju.track.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.AsyncUtCsvLoader;
import com.taobao.ju.track.impl.interfaces.ITrack;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TrackImpl implements ITrack {
    protected static final String PARAM_INTERNAL_AUTOSEND = "_autosend";
    protected static final String PARAM_INTERNAL_MARK = "_";
    protected static final String PARAM_INTERNAL_PARAM_DYNAMIC_LEFT = "{";
    protected static final String PARAM_INTERNAL_PARAM_DYNAMIC_RIGHT = "}";
    protected static final String PARAM_INTERNAL_PARAM_REFER_LEFT = "[";
    protected static final String PARAM_INTERNAL_PARAM_REFER_RIGHT = "]";
    protected static final String PARAM_INTERNAL_SPMA = "_spma";
    protected static final String PARAM_INTERNAL_SPMB = "_spmb";
    protected static final String PARAM_INTERNAL_SPMC = "_spmc";
    protected static final String PARAM_INTERNAL_SPMD = "_spmd";
    protected static final String PARAM_INTERNAL_SPM_NONE = "0";
    public static final String PARAM_INTERNAL_SPM_SPLIT = ".";
    protected static final String PARAM_OUTER_AUTOSEND = "autosend";
    private static final String TAG = TrackImpl.class.getSimpleName();
    protected Context mContext;
    protected String mFileName;
    protected Map<String, Map<String, String>> mParams = new ConcurrentHashMap();

    public TrackImpl(Context context, String csvName) {
        this.mFileName = csvName;
        this.mContext = context;
        loadParams(context, csvName);
    }

    @TargetApi(3)
    private void loadParams(Context context, String csvName) {
        new AsyncUtCsvLoader(this.mParams).execute(new Object[]{context, csvName});
    }

    public String[] getParamKvs(String rowKey) {
        Map<String, String> paramMap = getParamMap(rowKey);
        if (paramMap == null || paramMap.size() <= 0) {
            return new String[0];
        }
        String[] result = new String[paramMap.size()];
        int index = 0;
        for (String key : paramMap.keySet()) {
            result[index] = key + "=" + paramMap.get(key);
            index++;
        }
        return result;
    }

    public Properties getParamProp(String rowKey) {
        Map<String, String> paramMap = getParamMap(rowKey);
        Properties result = new Properties();
        if (paramMap != null && paramMap.size() > 0) {
            result.putAll(paramMap);
        }
        return result;
    }

    public Map<String, String> getParamMap(String rowKey) {
        if (this.mParams != null && this.mParams.containsKey(rowKey)) {
            return this.mParams.get(rowKey);
        }
        if (this.mParams != null) {
            for (Map.Entry<String, Map<String, String>> entry : this.mParams.entrySet()) {
                if (entry != null) {
                    String key = entry.getKey();
                    Map<String, String> value = entry.getValue();
                    if (rowKey != null && key != null && key.contains("|") && rowKey.matches(key)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    public Map<String, String> getValidParams(String rowKey) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> rowParams = getParamMap(rowKey);
        if (rowParams != null) {
            for (Map.Entry<String, String> entry : rowParams.entrySet()) {
                if (!isInternal(entry.getKey()) && (isStatic(entry.getValue()) || isRefer(entry.getValue()) || isDynamic(entry.getValue()))) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    public String getParamValue(String rowKey, String colKey) {
        return getParamValue(rowKey, colKey, (String) null);
    }

    public String getParamValue(String rowKey, String colKey, String defaultValue) {
        Map<String, String> params = getParamMap(rowKey);
        if (params == null || !params.containsKey(colKey)) {
            return defaultValue;
        }
        return params.get(colKey);
    }

    /* access modifiers changed from: protected */
    public String getParamValue(Map<String, String> params, String key, String defaultValue) {
        if (params == null || !params.containsKey(key)) {
            return defaultValue;
        }
        return params.get(key);
    }

    public Map<String, String> getStatic(String rowKey) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> rowParams = getParamMap(rowKey);
        if (rowParams != null) {
            for (Map.Entry<String, String> entry : rowParams.entrySet()) {
                if (!isInternal(entry.getKey()) && isStatic(entry.getValue())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    public Map<String, String> getRefer(String rowKey) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> rowParams = getParamMap(rowKey);
        if (rowParams != null) {
            for (Map.Entry<String, String> entry : rowParams.entrySet()) {
                if (!isInternal(entry.getKey()) && isRefer(entry.getValue())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    public Map<String, String> getDynamic(String rowKey) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> rowParams = getParamMap(rowKey);
        if (rowParams != null) {
            for (Map.Entry<String, String> entry : rowParams.entrySet()) {
                if (!isInternal(entry.getKey()) && isDynamic(entry.getValue())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    public boolean isValidateToUT(String paramValue) {
        return isStatic(paramValue);
    }

    public boolean hasPoint(String rowKey) {
        return this.mParams != null && this.mParams.containsKey(rowKey);
    }

    public boolean hasParam(String rowKey, String colKey) {
        return !TextUtils.isEmpty(getParamValue(rowKey, colKey));
    }

    public boolean isInternal(String rowKey, String colKey) {
        return isInternal(colKey);
    }

    public boolean isStatic(String rowKey, String colKey) {
        return !isInternal(rowKey, colKey) && isStatic(getParamValue(rowKey, colKey));
    }

    public boolean isRefer(String rowKey, String colKey) {
        return !isInternal(rowKey, colKey) && isRefer(getParamValue(rowKey, colKey));
    }

    public boolean isDynamic(String rowKey, String colKey) {
        return !isInternal(rowKey, colKey) && isDynamic(getParamValue(rowKey, colKey));
    }

    public String getFileName() {
        return this.mFileName;
    }

    public String getSpm(String rowKey) {
        return getSpm(getParamMap(rowKey));
    }

    /* access modifiers changed from: protected */
    public String getSpm(Map<String, String> params) {
        return getSpmAB(params) + "." + getSpmCD(params);
    }

    /* access modifiers changed from: protected */
    public String getSpmAB(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            sb.append(getParamValue(params, PARAM_INTERNAL_SPMA, "0")).append(".").append(getParamValue(params, PARAM_INTERNAL_SPMB, "0"));
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    }

    /* access modifiers changed from: protected */
    public String getSpmCD(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            sb.append(getParamValue(params, PARAM_INTERNAL_SPMC, "0")).append(".").append(getParamValue(params, PARAM_INTERNAL_SPMD, "0"));
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    }

    /* access modifiers changed from: protected */
    public boolean isInternal(String paramKey) {
        if (paramKey == null || !paramKey.startsWith("_")) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isStatic(String paramValue) {
        if (paramValue == null || paramValue.length() <= 0 || isRefer(paramValue) || isDynamic(paramValue)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isRefer(String paramValue) {
        if (paramValue == null || paramValue.indexOf(PARAM_INTERNAL_PARAM_REFER_LEFT) >= paramValue.lastIndexOf(PARAM_INTERNAL_PARAM_REFER_RIGHT)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDynamic(String paramValue) {
        if (paramValue == null || !paramValue.startsWith(PARAM_INTERNAL_PARAM_DYNAMIC_LEFT) || !paramValue.endsWith(PARAM_INTERNAL_PARAM_DYNAMIC_RIGHT)) {
            return false;
        }
        return true;
    }
}
