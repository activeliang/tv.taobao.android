package com.yunos.tv.blitz.video.data;

import android.util.SparseArray;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MtopReponse {
    private String api;
    private Map<String, JSONObject> data;
    private SparseArray<String> ret;
    private String v;

    public static MtopReponse fromJson(String reponse) {
        JSONObject data2;
        MtopReponse item = null;
        if (reponse == null) {
            return null;
        }
        try {
            JSONObject obj = new JSONObject(reponse);
            MtopReponse item2 = new MtopReponse();
            try {
                item2.api = obj.optString("api");
                item2.v = obj.optString("v");
                JSONArray array = obj.optJSONArray("ret");
                if (array != null && array.length() > 0) {
                    SparseArray<String> rets = new SparseArray<>();
                    for (int i = 0; i < array.length(); i++) {
                        rets.put(i, array.optString(i));
                    }
                }
                if (obj.has("data") && (data2 = obj.optJSONObject("data")) != null) {
                    item2.data = new HashMap();
                    item2.data.put("result", data2.optJSONObject("result"));
                }
                item = item2;
            } catch (JSONException e) {
                item = item2;
            }
        } catch (JSONException e2) {
        }
        return item;
    }

    public JSONObject getResult() {
        if (this.data != null) {
            return this.data.get("result");
        }
        return null;
    }

    public boolean isSuccess() {
        if (this.ret == null || this.ret.size() <= 0 || !this.ret.get(0).startsWith("SUCCESS::")) {
            return false;
        }
        return true;
    }

    public String getApi() {
        return this.api;
    }

    public String getV() {
        return this.v;
    }

    public SparseArray<String> getRet() {
        return this.ret;
    }

    public Map<String, JSONObject> getData() {
        return this.data;
    }

    public void setApi(String api2) {
        this.api = api2;
    }

    public void setV(String v2) {
        this.v = v2;
    }

    public void setRet(SparseArray<String> ret2) {
        this.ret = ret2;
    }

    public void setData(Map<String, JSONObject> data2) {
        this.data = data2;
    }
}
