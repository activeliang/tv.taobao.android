package com.yunos.tvtaobao.biz.request.blitz;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzMtopRequest extends BaseMtopRequest {
    private static final String TAG = "BlitzMtopRequest";
    private static final long serialVersionUID = -260150970821111706L;
    private boolean isHttpType_Post;
    private boolean isNeedEcode;
    private boolean isNeedLogin;
    private boolean isNeedSession;
    private String mApi;
    private String mApiVersion;
    private Map<String, String> mParamDataMap;

    public BlitzMtopRequest() {
        this.isNeedEcode = false;
        this.isNeedSession = false;
        this.isNeedLogin = false;
        this.isHttpType_Post = false;
        this.mApi = null;
        this.mApiVersion = null;
        this.mParamDataMap = null;
        this.mParamDataMap = new HashMap();
        this.mParamDataMap.clear();
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    public boolean getNeedEcode() {
        return this.isNeedEcode;
    }

    public boolean getNeedSession() {
        return this.isNeedSession;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.mApi;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.mApiVersion;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        for (Map.Entry<String, String> entry : this.mParamDataMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!TextUtils.isEmpty(key)) {
                obj.put(key, value);
            }
        }
        return obj;
    }

    public boolean getBlitzMtopNeedLogin() {
        return this.isNeedLogin;
    }

    public boolean getBlitzMtopPost() {
        return this.isHttpType_Post;
    }

    private void setBlitzMtopApi(String api) {
        this.mApi = api;
    }

    private void setBlitzMtopApiVersion(String version) {
        this.mApiVersion = version;
    }

    private void setBlitzMtopNeedEcode(boolean need) {
        this.isNeedEcode = need;
    }

    private void setBlitzMtopNeedSession(boolean need) {
        this.isNeedSession = need;
    }

    private void setBlitzMtopNeedLogin(boolean need) {
        this.isNeedLogin = need;
    }

    private void setBlitzMtopPost(boolean post) {
        this.isHttpType_Post = post;
    }

    private void setBlitzMtopAppData(String data) {
        ZpLogger.i(TAG, "setAppData --> data = " + data);
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject param_json = new JSONObject(data);
                Iterator it = param_json.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    if (!TextUtils.isEmpty(key)) {
                        Object _obj = param_json.opt(key);
                        if (_obj instanceof String) {
                            String value_str = param_json.optString(key);
                            if (!TextUtils.isEmpty(value_str)) {
                                this.mParamDataMap.put(key, value_str);
                            }
                            ZpLogger.i(TAG, "setAppData --> key = " + key + ";  value_str = " + value_str);
                        } else if (_obj instanceof Boolean) {
                            Boolean value_boolean = Boolean.valueOf(param_json.optBoolean(key));
                            if (value_boolean != null) {
                                if (value_boolean.booleanValue()) {
                                    this.mParamDataMap.put(key, "true");
                                } else {
                                    this.mParamDataMap.put(key, "false");
                                }
                            }
                            ZpLogger.i(TAG, "setAppData --> key = " + key + ";  value_boolean = " + value_boolean);
                        } else if (_obj instanceof Integer) {
                            Integer value_Integer = Integer.valueOf(param_json.optInt(key));
                            if (value_Integer != null) {
                                this.mParamDataMap.put(key, value_Integer.toString());
                            }
                            ZpLogger.i(TAG, "setAppData --> key = " + key + ";  value_Integer = " + value_Integer);
                        } else if (_obj instanceof Double) {
                            Double value_Double = Double.valueOf(param_json.optDouble(key));
                            if (value_Double != null) {
                                this.mParamDataMap.put(key, value_Double.toString());
                            }
                            ZpLogger.i(TAG, "setAppData --> key = " + key + ";  value_Double = " + value_Double);
                        } else if (_obj instanceof Long) {
                            Long value_Long = Long.valueOf(param_json.optLong(key));
                            if (value_Long != null) {
                                this.mParamDataMap.put(key, value_Long.toString());
                            }
                            ZpLogger.i(TAG, "setAppData --> key = " + key + ";  value_Long = " + value_Long);
                        }
                    }
                }
            } catch (JSONException e) {
                ZpLogger.i(TAG, "setAppData --> JSONException e  = " + e.toString());
            }
        }
    }

    public void resolveBlitzRequest(String request) {
        ZpLogger.i(TAG, "resolveBlitzRequest --> request = " + request);
        try {
            JSONObject request_param = new JSONObject(request);
            String api = request_param.optString("api");
            String version = request_param.optString("version");
            String data = request_param.optString("data");
            String need_login = request_param.optString("need_login");
            String use_encode = request_param.optString("need_encode");
            String use_sid = request_param.optString("need_sid");
            String http_type = request_param.optString("http_type");
            ZpLogger.i(TAG, "resolveBlitzRequest --> api = " + api + ";  version = " + version + "; http_type = " + http_type + "; need_login = " + need_login + "; use_encode = " + use_encode + "; use_sid = " + use_sid + "; data = " + data);
            if (!TextUtils.isEmpty(api)) {
                setBlitzMtopApi(api);
            }
            if (!TextUtils.isEmpty(version)) {
                setBlitzMtopApiVersion(version);
            }
            if (!TextUtils.isEmpty(data)) {
                setBlitzMtopAppData(data);
            }
            if (TextUtils.equals(need_login, "true")) {
                setBlitzMtopNeedEcode(true);
                setBlitzMtopNeedSession(true);
                setBlitzMtopNeedLogin(true);
            } else {
                if (TextUtils.equals(use_encode, "true")) {
                    setBlitzMtopNeedEcode(true);
                } else {
                    setBlitzMtopNeedEcode(false);
                }
                if (TextUtils.equals(use_sid, "true")) {
                    setBlitzMtopNeedSession(true);
                } else {
                    setBlitzMtopNeedSession(false);
                }
                setBlitzMtopNeedLogin(false);
            }
            if (TextUtils.isEmpty(http_type)) {
                setBlitzMtopPost(false);
            } else if (TextUtils.equals(http_type, "post")) {
                setBlitzMtopPost(true);
            } else {
                setBlitzMtopPost(false);
            }
        } catch (JSONException e) {
            ZpLogger.i(TAG, "resolveBlitzRequest --> JSONException e  = " + e.toString());
        }
    }
}
