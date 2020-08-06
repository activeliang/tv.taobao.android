package com.yunos.tvtaobao.biz.request.bo;

import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Tag implements Serializable {
    private static final String TAG = Tag.class.getSimpleName();
    private static final long serialVersionUID = -6736952240795493531L;
    private Map<String, String> params;
    private boolean supportBuy = true;
    private List<Integer> unSupportList = Arrays.asList(new Integer[]{5, 6, 9, 10, 18, 19, 29, 21, 23, 25, 26});
    private Long value;

    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value2) {
        this.value = value2;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params2) {
        this.params = params2;
    }

    public static Tag resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Tag tag = new Tag();
        if (obj.isNull("value")) {
            return tag;
        }
        tag.setValue(Long.valueOf(obj.getLong("value")));
        tag.setSupportBuy(tag.resolveSupportBuy(Long.valueOf(obj.getLong("value"))));
        return tag;
    }

    private boolean resolveSupportBuy(Long value2) {
        if (value2 != null && value2.longValue() > 0) {
            for (int i = 0; i < this.unSupportList.size(); i++) {
                ZpLogger.i(TAG, "mItem.getTag().value:" + value2 + ",unSupportList.get(i):" + this.unSupportList.get(i) + ",1L<<get(i):" + (1 << this.unSupportList.get(i).intValue()));
                if ((value2.longValue() & (1 << this.unSupportList.get(i).intValue())) > 0) {
                    ZpLogger.i(TAG, "mItem.getTag().supportBuy:false");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSupportBuy() {
        return this.supportBuy;
    }

    public void setSupportBuy(boolean supportBuy2) {
        this.supportBuy = supportBuy2;
    }
}
