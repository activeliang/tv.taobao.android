package com.tvtaobao.voicesdk.bean;

import com.tvtaobao.voicesdk.utils.JSONUtil;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchObject implements Serializable {
    public int endIndex = 30;
    public String keyword;
    public boolean needFeeds = false;
    public boolean needJinnang = true;
    public String priceScope;
    public boolean showUI = false;
    public String sorting;
    public int startIndex = 0;

    public static SearchObject resolverData(String data) {
        SearchObject search = new SearchObject();
        try {
            JSONObject object = new JSONObject(data);
            search.showUI = JSONUtil.getBoolean(object, "showUI");
            search.keyword = JSONUtil.getString(object, "keyword");
            search.startIndex = JSONUtil.getInt(object, "startIndex");
            search.endIndex = JSONUtil.getInt(object, "endIndex");
            search.needJinnang = JSONUtil.getBoolean(object, "needJinnang");
            search.needFeeds = JSONUtil.getBoolean(object, "needFeeds");
            search.priceScope = JSONUtil.getString(object, "priceScope");
            search.sorting = JSONUtil.getString(object, "sorting");
            JSONObject jSONObject = object;
        } catch (JSONException e) {
        }
        return search;
    }

    public void clearSift() {
        this.priceScope = null;
        this.sorting = null;
    }

    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "seach");
            object.put("showUI", this.showUI);
            object.put("keyword", this.keyword);
            object.put("startIndex", this.startIndex);
            object.put("endIndex", this.endIndex);
            object.put("needJinnang", this.needJinnang);
            object.put("priceScope", this.priceScope);
            object.put("sorting", this.sorting);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
