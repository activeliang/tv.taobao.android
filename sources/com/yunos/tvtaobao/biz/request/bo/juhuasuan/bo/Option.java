package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import com.yunos.tvtaobao.biz.request.core.JsonResolver;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Option extends BaseMO {
    private static final long serialVersionUID = 3250136711541187306L;
    private String bkgImgUrl;
    private ArrayList<Option> children;
    private String count;
    private String displayName;
    private OptionExtend extend;
    private String optStr;
    private Map<String, String> params;
    private String type;
    private String value;

    public static Option resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Option item = new Option();
        item.setType(obj.optString("type"));
        item.setValue(obj.optString("value"));
        item.setDisplayName(obj.optString("displayName"));
        item.setCount(obj.optString("count"));
        item.setOptStr(obj.optString("optStr"));
        if (obj.has("params")) {
            item.setParams(JsonResolver.jsonobjToMap(obj.getJSONObject("params")));
        }
        item.setExtend(OptionExtend.resolveFromJson(obj.optJSONObject("extend")));
        item.setBkgImgUrl(obj.optString("bkgImgUrl"));
        return item;
    }

    public String toString() {
        return "Option [type=" + this.type + ", value=" + this.value + ", displayName=" + this.displayName + ", count=" + this.count + ", optStr=" + this.optStr + ", params=" + this.params + ", extend=" + this.extend + ", bkgImgUrl=" + this.bkgImgUrl + ", children=" + this.children + "]";
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName2) {
        this.displayName = displayName2;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count2) {
        this.count = count2;
    }

    public String getOptStr() {
        return this.optStr;
    }

    public void setOptStr(String optStr2) {
        this.optStr = optStr2;
    }

    public OptionExtend getExtend() {
        return this.extend;
    }

    public void setExtend(OptionExtend extend2) {
        this.extend = extend2;
    }

    public String getBkgImgUrl() {
        return this.bkgImgUrl;
    }

    public void setBkgImgUrl(String bkgImgUrl2) {
        this.bkgImgUrl = bkgImgUrl2;
    }

    public ArrayList<Option> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Option> children2) {
        this.children = children2;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params2) {
        this.params = params2;
    }
}
