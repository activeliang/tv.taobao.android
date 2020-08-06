package com.yunos.tvtaobao.biz.request.core;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class MtopResponse {
    private String api;
    private JSONObject data;
    private List<String> ret = new ArrayList();
    private String v;

    public String getApi() {
        return this.api;
    }

    public String getV() {
        return this.v;
    }

    public JSONObject getData() {
        return this.data;
    }

    public List<String> getRet() {
        return this.ret;
    }

    public boolean isTopSuccess() {
        return this.ret.contains("SUCCESS");
    }

    public boolean containsCode(String code) {
        return this.ret.contains(code);
    }

    public MtopResponse edcode(String jsonSrc) throws JSONException {
        JSONObject json = new JSONObject(jsonSrc);
        if (json.has("api")) {
            this.api = json.getString("api");
        }
        if (json.has("v")) {
            this.v = json.getString("v");
        }
        if (json.has("data")) {
            this.data = json.optJSONObject("data");
        }
        if (json.has("ret")) {
            String retStr = json.getString("ret");
            this.ret.addAll(StringToList(retStr.substring(1, retStr.length() - 2)));
        }
        return this;
    }

    public String toString() {
        return "TopApiResponse [api=" + this.api + ", v=" + this.v + ", data=" + this.data + ", ret=" + this.ret + "]";
    }

    public static List<String> StringToList(String listText) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(listText)) {
            list.add("ERROR");
        } else {
            for (String str : listText.split(",|::|\"")) {
                if (!TextUtils.isEmpty(str)) {
                    list.add(str);
                }
            }
        }
        return list;
    }
}
