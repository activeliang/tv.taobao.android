package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.VouchersMO;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutVouchersRequest extends BaseMtopRequest {
    private final String TAG = "mtop.taobao.waimai.myvouchers.put";
    private final String VERSION = "1.0";

    public TakeOutVouchersRequest(String storeId) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.waimai.myvouchers.put";
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
    public VouchersMO resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (VouchersMO) JSON.parseObject(obj.toString(), VouchersMO.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
