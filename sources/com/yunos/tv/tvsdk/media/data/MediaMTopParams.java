package com.yunos.tv.tvsdk.media.data;

import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public class MediaMTopParams {
    public static final boolean DEBUG = true;
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String HTTP_PARAMS_ENCODING = "UTF-8";
    private static final String SIGN_SPLIT_CHAR = "&";
    public static final String TAG = "MediaMTopParams";
    private String mApiName;
    private String mApiVersion;
    private String mAppKey;
    private String mAppdata;
    private String mDeviceId;
    private String mIMEI;
    private String mIMSI;
    private String mSecret;
    private long mServerTime;
    private String mTTID;
    private String mUrl;

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String mUrl2) {
        this.mUrl = mUrl2;
    }

    public String getAppdata() {
        return this.mAppdata;
    }

    public void setAppdata(String mAppdata2) {
        this.mAppdata = mAppdata2;
    }

    public long getServerTime() {
        return this.mServerTime;
    }

    public void setServerTime(long mServerTime2) {
        this.mServerTime = mServerTime2;
    }

    public String getIMEI() {
        return this.mIMEI;
    }

    public void setIMEI(String mIMEI2) {
        this.mIMEI = mIMEI2;
    }

    public String getIMSI() {
        return this.mIMSI;
    }

    public void setIMSI(String mIMSI2) {
        this.mIMSI = mIMSI2;
    }

    public String getTTID() {
        return this.mTTID;
    }

    public void setTTID(String ttid) {
        this.mTTID = ttid;
    }

    public String getApiVersion() {
        return this.mApiVersion;
    }

    public void setApiVersion(String mApiVersion2) {
        this.mApiVersion = mApiVersion2;
    }

    public String getApiName() {
        return this.mApiName;
    }

    public void setApiName(String mApiName2) {
        this.mApiName = mApiName2;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public void setAppKey(String mAppKey2) {
        this.mAppKey = mAppKey2;
    }

    public String getSecret() {
        return this.mSecret;
    }

    public void setSecret(String mSecret2) {
        this.mSecret = mSecret2;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public void setDeviceId(String mDeviceId2) {
        this.mDeviceId = mDeviceId2;
    }

    public MediaMTopParams() {
    }

    public MediaMTopParams(String url, String apiVersion, String apiName, String appKey, String secret, String appData, String ttid, String imei, String imsi, String deviceId) {
        setUrl(url);
        setApiVersion(apiVersion);
        setApiName(apiName);
        setAppKey(appKey);
        setSecret(secret);
        setAppdata(appData);
        setTTID(ttid);
        setIMEI(imei);
        setIMSI(imsi);
        setDeviceId(deviceId);
    }

    public String getHttpParams() throws Exception {
        return getHttpParams(this.mApiName, this.mApiVersion, this.mDeviceId, this.mAppKey, this.mSecret, this.mAppdata, this.mServerTime);
    }

    public String getHttpParams(String api, String key, String secret, String appData, long serverTime) throws Exception {
        return getHttpParams(api, this.mApiVersion, (String) null, key, secret, appData, serverTime);
    }

    public String getHttpParams(String api, String version, String deviceId, String key, String secret, String appData, long serverTime) throws Exception {
        if (serverTime <= 0) {
            serverTime = System.currentTimeMillis() / 1000;
            Log.e(TAG, " -- sign,severTime error datetime=" + serverTime);
        }
        StringBuilder sb = new StringBuilder();
        if (checkParamValidate("url", this.mUrl)) {
            sb.append(this.mUrl);
        }
        Map<String, String> params = initParams(api, key, deviceId, appData, version, serverTime);
        int len = params.size();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            if (0 < len - 1) {
                sb.append("&");
            }
        }
        sb.append("sign").append("=").append(sign(api, key, secret, appData, version, serverTime));
        return sb.toString();
    }

    private Map<String, String> initParams(String api, String key, String deviceId, String appData, String version, long serverTime) throws NumberFormatException, Exception {
        Map<String, String> params = new HashMap<>();
        String paramValue = deviceId;
        if (!TextUtils.isEmpty(paramValue)) {
            params.put("deviceId", paramValue);
        }
        String paramValue2 = this.mTTID;
        if (checkParamValidate("ttid", paramValue2)) {
            params.put("ttid", paramValue2);
        }
        String paramValue3 = version;
        if (checkParamValidate("v", paramValue3)) {
            params.put("v", paramValue3);
        }
        String paramValue4 = this.mIMEI;
        if (checkParamValidate("imei", paramValue4)) {
            params.put("imei", paramValue4);
        }
        String paramValue5 = this.mIMSI;
        if (checkParamValidate("imsi", paramValue5)) {
            params.put("imsi", paramValue5);
        }
        String paramValue6 = key;
        if (checkParamValidate("appKey", paramValue6)) {
            params.put("appKey", paramValue6);
        }
        String paramValue7 = api;
        if (checkParamValidate("api", paramValue7)) {
            params.put("api", paramValue7);
        }
        String paramValue8 = String.valueOf(serverTime);
        if (checkParamValidate("t", paramValue8)) {
            params.put("t", paramValue8);
        }
        String data = encodeUrl(appData);
        String paramValue9 = data;
        if (checkParamValidate("data", paramValue9)) {
            params.put("data", paramValue9);
        }
        Log.d(TAG, " -- initParams, data:" + data);
        return params;
    }

    private boolean checkParamValidate(String paramName, String paramValue) throws Exception {
        if (!TextUtils.isEmpty(paramValue)) {
            return true;
        }
        throw new NullPointerException("MediaMTopParams " + paramName + " is null");
    }

    private String sign(String api, String key, String secret, String appData, String version, long serverTime) throws JSONException, Exception {
        StringBuilder sb = new StringBuilder();
        if (checkParamValidate("secret", secret)) {
            sb.append(secret + "&");
        }
        String paramValue = encode(key);
        if (checkParamValidate("appkey", paramValue)) {
            sb.append(paramValue).append("&");
        }
        String paramValue2 = api;
        if (checkParamValidate("api", paramValue2)) {
            sb.append(paramValue2).append("&");
        }
        String paramValue3 = version;
        if (checkParamValidate("version", paramValue3)) {
            sb.append(paramValue3).append("&");
        }
        String paramValue4 = this.mIMEI;
        if (checkParamValidate("mIMEI", paramValue4)) {
            sb.append(paramValue4).append("&");
        }
        String paramValue5 = this.mIMSI;
        if (checkParamValidate("mIMSI", paramValue5)) {
            sb.append(paramValue5).append("&");
        }
        String paramValue6 = encode(appData);
        if (checkParamValidate("appData", paramValue6)) {
            sb.append(paramValue6).append("&");
        }
        sb.append(serverTime);
        Log.i(TAG, (System.currentTimeMillis() / 1000) + " -- sign, serverTime=" + serverTime);
        return encode(sb.toString());
    }

    private String encodeUrl(String str) {
        String str2;
        if (str == null) {
            return null;
        }
        try {
            str2 = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            str2 = null;
        }
        return str2;
    }

    private String decodeUrl(String str) {
        String str2;
        if (str == null) {
            return null;
        }
        try {
            str2 = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            str2 = null;
        }
        return str2;
    }

    private String encode(String source) {
        if (source == null) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        if (bytes != null) {
            return String.valueOf(encode2Hex(encode2MD5(bytes)));
        }
        return null;
    }

    private char[] encode2Hex(byte[] data) {
        int len = data.length;
        char[] out = new char[(len << 1)];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int j2 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = DIGITS[data[i] & 15];
        }
        return out;
    }

    private byte[] encode2MD5(byte[] source) {
        byte[] result = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            return md.digest();
        } catch (Exception e) {
            return result;
        }
    }
}
