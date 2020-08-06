package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CouponList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class GetCouponListRequest extends BaseMtopRequest {
    private static final String API = "mtop.msp.coupon.getMyCouponListByType";
    private static final long serialVersionUID = 6403615798555209809L;
    private int page;
    private String sid;
    private int size;
    private String tag;

    public GetCouponListRequest(int page2, int size2, String tag2) {
        this.sid = "";
        this.page = 1;
        this.size = 20;
        this.tag = "";
        this.sid = User.getSessionId();
        this.page = page2;
        this.size = size2;
        this.tag = tag2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.sid)) {
            obj.put("sid", this.sid);
        }
        if (!TextUtils.isEmpty(this.tag)) {
            obj.put("tag", this.tag);
        }
        obj.put("page", String.valueOf(this.page));
        obj.put("size", String.valueOf(this.size));
        obj.put("couponType", "0");
        obj.put("stateType", "0");
        obj.put("orderType", "0");
        return obj;
    }

    /* access modifiers changed from: protected */
    public CouponList resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (CouponList) JSON.parseObject(obj.toString(), CouponList.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
