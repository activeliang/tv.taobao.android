package com.yunos.tv.tvsdk.media.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Qiyi {
    public String albumid;
    public String definition;
    public String id;
    private JSONObject mJson;
    public String name;
    public String starttime;
    public String vid;

    public Qiyi(String qiyi) {
        try {
            JSONObject json = new JSONObject(qiyi);
            this.mJson = json;
            this.albumid = json.optString("albumid");
            this.id = json.optString("id");
            this.name = json.optString("name");
            this.starttime = json.optString(HuasuVideo.TAG_STARTTIME);
            this.definition = json.optString("definition");
            this.vid = json.optString(HuasuVideo.TAG_VID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setStarttime(String pos) {
        try {
            this.mJson.put(HuasuVideo.TAG_STARTTIME, pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDefinition(String def) {
        try {
            this.mJson.put("definition", def);
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
