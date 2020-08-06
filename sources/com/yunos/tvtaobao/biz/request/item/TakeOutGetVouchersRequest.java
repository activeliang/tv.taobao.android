package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeVouchers;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutGetVouchersRequest extends BaseMtopRequest {
    private final String TAG = "mtop.taobao.waimai.shopvouchers.take";
    private final String VERSION = "1.0";

    public TakeOutGetVouchersRequest(String storeId, String activityId, String exchangeType, String storeIdType) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
        if (!TextUtils.isEmpty(activityId)) {
            addParams(BaseConfig.ACTIVITY_ID, activityId);
        }
        if (!TextUtils.isEmpty(exchangeType)) {
            addParams("exchangeType", exchangeType);
        }
        if (!TextUtils.isEmpty(storeIdType)) {
            addParams("storeIdType", storeIdType);
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.waimai.shopvouchers.take";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public TakeVouchers resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (TakeVouchers) JSON.parseObject(obj.toString(), TakeVouchers.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
