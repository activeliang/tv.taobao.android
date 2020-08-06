package com.taobao.ju.track.server;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JTrackParams extends HashMap<String, String> {
    private static final String KEY_SPLIT = ",";
    public static final String TRACK_PARAMS = "trackParams";
    public static final String TRACK_PARAMS_CLICK = "_click";
    public static final String TRACK_PARAMS_DETAIL_EXPOSE = "_detailExpose";
    public static final String TRACK_PARAMS_DOUBLE_CLICK = "_doubleClick";
    public static final String TRACK_PARAMS_EXPOSE = "_expose";
    public static final String TRACK_PARAMS_ID = "_trackId";
    public static final String TRACK_PARAMS_NAME = "_trackName";

    public static JTrackParams addAll(JTrackParams base, JTrackParams extra) {
        if (base == null) {
            return extra;
        }
        if (extra != null) {
            base.putAll(extra);
        }
        return base;
    }

    public static JTrackParams create(JSONObject data) {
        if (data == null || !data.containsKey(TRACK_PARAMS)) {
            return null;
        }
        return create(data.getString(TRACK_PARAMS));
    }

    public static JTrackParams create(String json) {
        try {
            if (!TextUtils.isEmpty(json)) {
                return (JTrackParams) JSON.parseObject(json, JTrackParams.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JTrackParams create(Map<String, String> map) {
        if (map != null) {
            try {
                JTrackParams result = new JTrackParams();
                result.putAll(map);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JTrackParams createFromObjMap(Map<String, Object> map) {
        if (map != null) {
            try {
                JTrackParams result = new JTrackParams();
                for (String key : map.keySet()) {
                    result.put(key, String.valueOf(map.get(key)));
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setFields(String key, String fields) {
        put(key, fields);
    }

    public String getAllKeys() {
        StringBuffer sb = new StringBuffer();
        Set<String> keys = keySet();
        if (keys != null) {
            for (String key : keys) {
                if (key != null && !key.startsWith("_")) {
                    sb.append(key).append(",");
                }
            }
        }
        if (sb.length() > 1) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static Map<String, String> getClickParams(JTrackParams params) {
        Map<String, String> result = getParams(params, TRACK_PARAMS_CLICK);
        String trackId = getTrackId(params);
        String trackName = getTrackName(params);
        if (!(trackId == null && trackName == null)) {
            if (result == null) {
                result = new HashMap<>();
            }
            result.put(TRACK_PARAMS_ID, trackId);
            result.put(TRACK_PARAMS_NAME, trackName);
        }
        return result;
    }

    public static Map<String, String> getDoubleClickParams(JTrackParams params) {
        Map<String, String> result = getParams(params, TRACK_PARAMS_DOUBLE_CLICK);
        String trackId = getTrackId(params);
        String trackName = getTrackName(params);
        if (!(trackId == null && trackName == null)) {
            if (result == null) {
                result = new HashMap<>();
            }
            result.put(TRACK_PARAMS_ID, trackId);
            result.put(TRACK_PARAMS_NAME, trackName);
        }
        return result;
    }

    public static Map<String, String> getExposeParams(JTrackParams params) {
        return getParams(params, TRACK_PARAMS_EXPOSE);
    }

    public static Map<String, String> getDetailExposeParams(JTrackParams params) {
        return getParams(params, TRACK_PARAMS_DETAIL_EXPOSE);
    }

    public static Serializable getSerializableDetailExposeParams(JTrackParams params) {
        return (Serializable) getDetailExposeParams(params);
    }

    public static String getTrackId(JTrackParams params) {
        return getParam(params, TRACK_PARAMS_ID);
    }

    public static String getTrackName(JTrackParams params) {
        return getParam(params, TRACK_PARAMS_NAME);
    }

    private static Map<String, String> getParams(JTrackParams params, String keyOfList) {
        if (params != null && params.containsKey(keyOfList)) {
            String keyStr = (String) params.get(keyOfList);
            if (!TextUtils.isEmpty(keyStr)) {
                return getParams(params, keyStr.split(","));
            }
        }
        return null;
    }

    private static Map<String, String> getParams(JTrackParams params, String[] keys) {
        if (params == null || keys == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            String key2 = key.trim();
            if (params.containsKey(key2)) {
                result.put(key2, params.get(key2));
            }
        }
        return result;
    }

    private static String getParam(JTrackParams params, String key) {
        if (params == null || !params.containsKey(key)) {
            return null;
        }
        return (String) params.get(key);
    }
}
