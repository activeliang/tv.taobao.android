package com.yunos.tv.tvsdk.media.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Sohu {
    public String catecode;
    public String cid;
    public int definition;
    private JSONObject mJson = null;
    public String position;
    public String sid;
    public String vid;

    public Sohu(String sohu) {
        try {
            JSONObject json = new JSONObject(sohu);
            this.mJson = json;
            this.catecode = json.optString("catecode", "0");
            this.cid = json.optString("cid", "0");
            this.definition = json.optInt("definition", 0);
            this.position = json.optString("position", "0");
            this.sid = json.optString("sid", "0");
            this.vid = json.optString(HuasuVideo.TAG_VID, "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDefinition(int definition2) {
        try {
            this.mJson.put("definition", definition2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(int pos) {
        try {
            this.mJson.put("position", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        if (this.mJson != null) {
            return this.mJson.toString();
        }
        return "";
    }
}
