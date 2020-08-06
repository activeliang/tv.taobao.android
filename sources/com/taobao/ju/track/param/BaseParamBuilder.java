package com.taobao.ju.track.param;

import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.impl.TrackImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseParamBuilder {
    public static final String DIVIDER = "_";
    private static final Pattern DYNAMIC_REGEX_PATTERN = Pattern.compile(DYNAMIC_REPLACE_REGEX);
    private static final String DYNAMIC_REPLACE_REGEX = "\\[(\\w)+\\]";
    protected String mCSVName = null;
    protected String mCSVRowName = null;
    protected Map<String, String> mParams;

    public abstract BaseParamBuilder add(String str, Object obj);

    public abstract BaseParamBuilder add(Map<String, String> map);

    public abstract BaseParamBuilder forceAdd(String str, Object obj);

    protected static <T extends BaseParamBuilder> T makeInternal(TrackImpl track, T paramBuilder, Object rowKey) {
        paramBuilder.mCSVRowName = String.valueOf(rowKey);
        paramBuilder.mParams = new HashMap();
        paramBuilder.mParams.putAll(track.getValidParams(paramBuilder.mCSVRowName));
        paramBuilder.mCSVName = track.getFileName();
        return paramBuilder;
    }

    public Map<String, String> getParams() {
        return getParams(false);
    }

    public Map<String, String> getParams(boolean showEmptyValue) {
        Map<String, String> result = new HashMap<>();
        if (this.mParams != null && this.mParams.size() > 0) {
            for (String key : this.mParams.keySet()) {
                if (!TextUtils.isEmpty(key)) {
                    String paramValue = getParamValue(key);
                    if (showEmptyValue || isParamValueValidate(paramValue)) {
                        result.put(key, paramValue);
                    }
                }
            }
        }
        return result;
    }

    public String[] getKvs() {
        return getKvs(false);
    }

    private String[] getKvs(boolean showEmptyValue) {
        if (this.mParams != null && this.mParams.size() > 0) {
            List<String> kvs = new ArrayList<>();
            for (String key : this.mParams.keySet()) {
                if (!TextUtils.isEmpty(key)) {
                    String paramValue = getParamValue(key);
                    if (showEmptyValue || isParamValueValidate(paramValue)) {
                        kvs.add(key + "=" + paramValue);
                    }
                }
            }
            if (kvs.size() > 0) {
                String[] result = new String[kvs.size()];
                kvs.toArray(result);
                return result;
            }
        }
        return null;
    }

    private boolean isParamValueValidate(String paramValue) {
        boolean isNotNull;
        if (TextUtils.isEmpty(paramValue) || Constant.NULL.equals(paramValue)) {
            isNotNull = false;
        } else {
            isNotNull = true;
        }
        if (this.mCSVName != null) {
            return isNotNull && JTrack.Ctrl.isValidateToUt(paramValue);
        }
        return isNotNull;
    }

    public String getParamValue(String key) {
        return getParamValue(key, Constant.NULL);
    }

    public String getParamValue(String key, String defaultValue) {
        if (!TextUtils.isEmpty(key) && this.mParams != null && this.mParams.size() > 0) {
            String value = this.mParams.get(key);
            if (!TextUtils.isEmpty(value)) {
                Matcher matcher = DYNAMIC_REGEX_PATTERN.matcher(value);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String newKey = matcher.group();
                    matcher.appendReplacement(sb, getParamValue(newKey.substring(1, newKey.length() - 1)));
                }
                if (sb.length() > 0) {
                    return sb.toString();
                }
                return value;
            }
        }
        return defaultValue;
    }

    public String getParamsValueStr() {
        if (this.mParams == null || this.mParams.size() <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        boolean isFirst = false;
        for (String key : this.mParams.keySet()) {
            String paramValue = getParamValue(key);
            if (isParamValueValidate(paramValue)) {
                sb.append(paramValue);
                if (isFirst) {
                    sb.append("_");
                } else {
                    isFirst = true;
                }
            }
        }
        return sb.toString();
    }

    public Map<String, String> getParamsByKeys(String... keys) {
        Map<String, String> result = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                result.put(key, getParamValue(key));
            }
        }
        return result;
    }

    public String toString() {
        String[] kvs = getKvs(false);
        if (kvs != null) {
            return Arrays.asList(kvs).toString();
        }
        return super.toString();
    }

    public String toSortString() {
        String[] kvs = getKvs(false);
        if (kvs == null) {
            return super.toString();
        }
        List<String> tmp = Arrays.asList(kvs);
        Collections.sort(tmp);
        ArrayList<String> result = new ArrayList<>(tmp);
        result.add("_key=" + getCtrlName());
        return result.toString();
    }

    public String getCtrlName() {
        return this.mCSVRowName != null ? this.mCSVRowName : Constant.NULL;
    }
}
