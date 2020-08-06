package com.yunos.tvtaobao.biz.request.bo;

import com.alibaba.appmonitor.sample.SampleConfigConstant;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Prop implements Serializable {
    private static final long serialVersionUID = 137490692503179586L;
    private Long propId;
    private String propName;
    private PropValue[] values;

    public Long getPropId() {
        return this.propId;
    }

    public PropValue getValue(Long vid) {
        for (PropValue v : this.values) {
            if (((long) v.getValueId().intValue()) == vid.longValue()) {
                return v;
            }
        }
        return null;
    }

    public void setPropId(Long propId2) {
        this.propId = propId2;
    }

    public String getPropName() {
        return this.propName;
    }

    public void setPropName(String propName2) {
        this.propName = propName2;
    }

    public PropValue[] getValues() {
        return this.values;
    }

    public void setValues(PropValue[] values2) {
        this.values = values2;
    }

    public static Prop resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Prop p = new Prop();
        if (!obj.isNull("propId")) {
            p.setPropId(Long.valueOf(obj.getLong("propId")));
        }
        if (!obj.isNull("propName")) {
            p.setPropName(obj.getString("propName"));
        }
        if (obj.isNull(SampleConfigConstant.VALUES)) {
            return p;
        }
        JSONArray array = obj.getJSONArray(SampleConfigConstant.VALUES);
        PropValue[] temp = new PropValue[array.length()];
        for (int i = 0; i < array.length(); i++) {
            temp[i] = PropValue.resolveFromMTOP(array.getJSONObject(i));
        }
        p.setValues(temp);
        return p;
    }
}
