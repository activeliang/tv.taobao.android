package com.yunos.tv.tvsdk.media.data;

import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MTopQiyiInfo extends MTopInfoBase {
    private static final String TAG_API = "api";
    private static final String TAG_DATA = "data";
    private static final String TAG_ERRCODE = "errCode";
    private static final String TAG_ERRMSG = "errMsg";
    private static final String TAG_ORDER_STATUS = "orderStatus";
    private static final String TAG_RESULT = "result";
    private static final String TAG_RET = "ret";
    private static final String TAG_TOKENVALID = "tokenValid";
    private static final String TAG_TRIAL = "trial";
    private static final String TAG_USERID = "userId";
    private static final String TAG_V = "v";
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
        public QiyiInfo result;

        /* access modifiers changed from: private */
        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has(MTopQiyiInfo.TAG_ERRCODE)) {
                    this.errorCode = jsonObj.optString(MTopQiyiInfo.TAG_ERRCODE);
                }
                if (jsonObj.has(MTopQiyiInfo.TAG_ERRMSG)) {
                    this.errorMsg = jsonObj.optString(MTopQiyiInfo.TAG_ERRMSG);
                }
                if (jsonObj.has("result")) {
                    this.result = new QiyiInfo();
                    this.result.pareseFromJson(jsonObj.optString("result"));
                }
            }
        }
    }

    public static class QiyiInfo {
        @SerializedName("orderStatus")
        public String orderStatus = "";
        @SerializedName("tokenValid")
        public boolean tokenValid;
        @SerializedName("trial")
        public boolean trial;
        @SerializedName("userId")
        public String userId = "";

        /* access modifiers changed from: private */
        public void pareseFromJson(String json) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has(MTopQiyiInfo.TAG_TOKENVALID)) {
                    this.tokenValid = jsonObj.optBoolean(MTopQiyiInfo.TAG_TOKENVALID);
                }
                if (jsonObj.has(MTopQiyiInfo.TAG_TRIAL)) {
                    this.trial = jsonObj.optBoolean(MTopQiyiInfo.TAG_TRIAL);
                }
                if (jsonObj.has("userId")) {
                    this.userId = jsonObj.optString("userId");
                }
                if (jsonObj.has(MTopQiyiInfo.TAG_ORDER_STATUS)) {
                    this.orderStatus = jsonObj.optString(MTopQiyiInfo.TAG_ORDER_STATUS);
                }
            }
        }
    }

    public String getUserId() {
        if (this.mData == null || this.mData.result == null) {
            return "";
        }
        return this.mData.result.userId;
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

    public QiyiInfo getDataResult() {
        if (this.mData == null) {
            return null;
        }
        return this.mData.result;
    }
}
