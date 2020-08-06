package com.yunos.tv.tvsdk.media.data;

import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MTopTaoTvInfo extends MTopInfoBase {
    private static final int MINUTE = 60000;
    private static final int SECOND = 1000;
    private static final String TAG_API = "api";
    private static final String TAG_DASHCONTENT = "dashContent";
    private static final String TAG_DATA = "data";
    private static final String TAG_DNSADDRESS = "dnsAddress";
    private static final String TAG_DRMTOKEN = "drmToken";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_ERRCODE = "errCode";
    private static final String TAG_ERRMSG = "errMsg";
    private static final String TAG_FREE = "free";
    private static final String TAG_HLSCONTENT = "hlsContent";
    private static final String TAG_HTTPDNS = "httpDns";
    private static final String TAG_LIVE = "live";
    private static final String TAG_METHOD = "method";
    private static final String TAG_ORDER_STATUS = "orderStatus";
    private static final String TAG_RESULT = "result";
    private static final String TAG_RET = "ret";
    private static final String TAG_SOURCEINFO = "sourceInfo";
    private static final String TAG_TOKENVALID = "tokenValid";
    private static final String TAG_TRIAL = "trial";
    private static final String TAG_TVHOST = "tvHost";
    private static final String TAG_V = "v";
    private static final String TAG_V1080TV = "v1080tv";
    private static final String TAG_V480 = "v480";
    private static final String TAG_V4K = "v2160tv";
    private static final String TAG_V720 = "v720";
    private static final String TAG_V720TV = "v720tv";
    private static final long serialVersionUID = 3654348195581783362L;
    @SerializedName("api")
    private String mApi;
    @SerializedName("data")
    private Data mData;
    @SerializedName("ret")
    private ArrayList<String> mRet = new ArrayList<>();
    @SerializedName("v")
    private String mVersion;

    public static class Data {
        @SerializedName("errCode")
        public String errorCode;
        @SerializedName("errMsg")
        public String errorMsg;
        @SerializedName("result")
        TaoTvInfo result;

        /* access modifiers changed from: private */
        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has(MTopTaoTvInfo.TAG_ERRCODE)) {
                    this.errorCode = jsonObj.optString(MTopTaoTvInfo.TAG_ERRCODE);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_ERRMSG)) {
                    this.errorMsg = jsonObj.optString(MTopTaoTvInfo.TAG_ERRMSG);
                }
                if (jsonObj.has("result")) {
                    this.result = new TaoTvInfo();
                    this.result.pareseFromJson(jsonObj.optString("result"));
                }
            }
        }
    }

    public static class TaoTvInfo {
        @SerializedName("dashContent")
        public String dashContent = "";
        @SerializedName("drmToken")
        public String drmToken = "";
        @SerializedName("duration")
        public int duration = 600000;
        @SerializedName("errCode")
        public String errCode = "";
        @SerializedName("free")
        public boolean free;
        @SerializedName("hlsContent")
        public String hlsContent = "";
        @SerializedName("httpDns")
        public HttpDns httpDns;
        @SerializedName("live")
        public boolean live;
        @SerializedName("orderStatus")
        public String orderStatus = "";
        @SerializedName("sourceInfo")
        public SourceInfo sourceInfo;
        @SerializedName("tokenValid")
        public boolean tokenValid;
        @SerializedName("trial")
        public boolean trial;

        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has(MTopTaoTvInfo.TAG_TOKENVALID)) {
                    this.tokenValid = jsonObj.optBoolean(MTopTaoTvInfo.TAG_TOKENVALID);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_TRIAL)) {
                    this.trial = jsonObj.optBoolean(MTopTaoTvInfo.TAG_TRIAL);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_FREE)) {
                    this.free = jsonObj.optBoolean(MTopTaoTvInfo.TAG_FREE);
                }
                if (jsonObj.has("live")) {
                    this.live = jsonObj.optBoolean("live");
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_HLSCONTENT)) {
                    this.hlsContent = jsonObj.optString(MTopTaoTvInfo.TAG_HLSCONTENT);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_ERRCODE)) {
                    this.errCode = jsonObj.optString(MTopTaoTvInfo.TAG_ERRCODE);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_DRMTOKEN)) {
                    this.drmToken = jsonObj.optString(MTopTaoTvInfo.TAG_DRMTOKEN);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_DASHCONTENT)) {
                    this.dashContent = jsonObj.optString(MTopTaoTvInfo.TAG_DASHCONTENT);
                }
                if (jsonObj.has("duration")) {
                    this.duration = jsonObj.optInt("duration", 600000);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_SOURCEINFO)) {
                    this.sourceInfo = new SourceInfo();
                    this.sourceInfo.pareseFromJson(jsonObj.optString(MTopTaoTvInfo.TAG_SOURCEINFO));
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_HTTPDNS)) {
                    this.httpDns = new HttpDns();
                    this.httpDns.pareseFromJson(jsonObj.optString(MTopTaoTvInfo.TAG_HTTPDNS));
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_ORDER_STATUS)) {
                    this.orderStatus = jsonObj.optString(MTopTaoTvInfo.TAG_ORDER_STATUS);
                }
            }
        }
    }

    public static class SourceInfo {
        @SerializedName("v1080tv")
        public String v1080 = "";
        @SerializedName("v480")
        public String v320 = "";
        @SerializedName("v720")
        public String v480 = "";
        @SerializedName("v2160tv")
        public String v4k = "";
        @SerializedName("v720tv")
        public String v720 = "";

        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has(MTopTaoTvInfo.TAG_V4K)) {
                    this.v4k = jsonObj.optString(MTopTaoTvInfo.TAG_V4K);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_V1080TV)) {
                    this.v1080 = jsonObj.optString(MTopTaoTvInfo.TAG_V1080TV);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_V720TV)) {
                    this.v720 = jsonObj.optString(MTopTaoTvInfo.TAG_V720TV);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_V720)) {
                    this.v480 = jsonObj.optString(MTopTaoTvInfo.TAG_V720);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_V480)) {
                    this.v320 = jsonObj.optString(MTopTaoTvInfo.TAG_V480);
                }
            }
        }
    }

    public static class HttpDns {
        @SerializedName("dnsAddress")
        public String dnsAddress = "";
        @SerializedName("method")
        public String method = "";
        @SerializedName("tvHost")
        public String tvHost = "";

        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has("method")) {
                    this.method = jsonObj.optString("method");
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_TVHOST)) {
                    this.tvHost = jsonObj.optString(MTopTaoTvInfo.TAG_TVHOST);
                }
                if (jsonObj.has(MTopTaoTvInfo.TAG_DNSADDRESS)) {
                    this.dnsAddress = jsonObj.optString(MTopTaoTvInfo.TAG_DNSADDRESS);
                }
            }
        }
    }

    public SourceInfo getSouceInfo() {
        if (this.mData == null || this.mData.result == null) {
            return null;
        }
        return this.mData.result.sourceInfo;
    }

    public HttpDns getHttpDns() {
        if (this.mData == null || this.mData.result == null) {
            return null;
        }
        return this.mData.result.httpDns;
    }

    public boolean isLive() {
        if (this.mData == null || this.mData.result == null) {
            return false;
        }
        return this.mData.result.live;
    }

    public boolean isTrial() {
        if (this.mData == null || this.mData.result == null) {
            return true;
        }
        return this.mData.result.trial;
    }

    public boolean tokenValid() {
        if (this.mData == null || this.mData.result == null) {
            return false;
        }
        return this.mData.result.tokenValid;
    }

    public boolean isDataEmpty() {
        if (this.mData == null || this.mData.result == null) {
            return true;
        }
        return false;
    }

    public String getOrderStatus() {
        if (this.mData == null || this.mData.result == null) {
            return null;
        }
        return this.mData.result.orderStatus;
    }

    public JSONObject convertToJSObject() {
        try {
            return new JSONObject(new GsonBuilder().create().toJson((Object) this));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseFromJson(String json) throws JSONException {
        if (!TextUtils.isEmpty(json)) {
            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.has("api")) {
                this.mApi = jsonObj.optString("api");
            }
            if (jsonObj.has("v")) {
                this.mVersion = jsonObj.optString("v");
            }
            if (jsonObj.has("ret")) {
                ArrayList<String> list = new ArrayList<>();
                JSONArray array = jsonObj.optJSONArray("ret");
                int len = array == null ? 0 : array.length();
                for (int i = 0; i < len; i++) {
                    String value = array.optString(i);
                    if (value == null) {
                        value = "";
                    }
                    list.add(value);
                }
                if (this.mRet == null) {
                    this.mRet = new ArrayList<>();
                }
                this.mRet.addAll(list);
            }
            if (jsonObj.has("data")) {
                this.mData = new Data();
                this.mData.pareseFromJson(jsonObj.optString("data"));
            }
        }
    }

    public TaoTvInfo getDataResult() {
        if (this.mData == null) {
            return null;
        }
        return this.mData.result;
    }
}
