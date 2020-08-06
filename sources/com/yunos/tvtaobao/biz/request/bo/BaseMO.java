package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseMO implements Serializable {
    private static final long serialVersionUID = -7950946206282680445L;

    public static JSONObject getDataElement(String json) {
        if (json == null) {
            return null;
        }
        try {
            return new JSONObject(json).getJSONObject("data");
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONArray getDataResultElement(String json) {
        JSONObject data = getDataElement(json);
        if (data == null) {
            return null;
        }
        try {
            return data.getJSONArray("result");
        } catch (JSONException e) {
            return null;
        }
    }
}
