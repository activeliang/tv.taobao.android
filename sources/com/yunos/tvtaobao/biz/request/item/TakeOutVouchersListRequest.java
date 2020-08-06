package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.VouchersList;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutVouchersListRequest extends BaseMtopRequest {
    private final String TAG = "mtop.taobao.waimai.shopvouchers.list";
    private final String VERSION = "1.0";

    public TakeOutVouchersListRequest(String storeId) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.waimai.shopvouchers.list";
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
    public VouchersList resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (VouchersList) JSON.parseObject(obj.toString(), VouchersList.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
