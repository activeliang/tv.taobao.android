package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6;
import java.util.Map;
import org.json.JSONObject;

public class TBDetailV6HttpDetailDialogRequest extends BaseMtopRequest {
    private String data = null;
    private String host = "https://acs.m.taobao.com/gw/mtop.taobao.detail.getdetail/6.0/?data=";
    private String itemId = "";
    private String tag = "%7B%22itemNumId%22%3A%22";
    private String tag2 = "%22%2C%22detail_v%22%3A%223.1.0%22%7D";

    public TBDetailV6HttpDetailDialogRequest(String itemId2, String areaId) {
        addParams("itemNumId", itemId2);
        if (!TextUtils.isEmpty(areaId)) {
            addParams(BaseConfig.INTENT_KEY_AREAID, areaId);
        }
        addParams("detail_v", "3.1.0");
        this.itemId = itemId2;
    }

    public String getCustomDomain() {
        return "acs.m.taobao.com";
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.detail.getdetail";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "6.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public TBDetailResultV6 resolveResponse(JSONObject obj) throws Exception {
        return (TBDetailResultV6) JSON.parseObject(obj.toString(), TBDetailResultV6.class);
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    public String getTTid() {
        return "142857@taobao_iphone_7.10.3";
    }
}
