package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GetFootMarkResponse;
import java.util.Map;
import org.json.JSONObject;

public class GetFootMarkRequest extends BaseMtopRequest {
    long endTime;
    int pageSize = 20;

    public GetFootMarkRequest(long endTime2) {
        this.endTime = endTime2;
    }

    /* access modifiers changed from: protected */
    public GetFootMarkResponse resolveResponse(JSONObject jsonObject) throws Exception {
        try {
            return (GetFootMarkResponse) JSON.parseObject(jsonObject.toString(), GetFootMarkResponse.class);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.cmin.mypath";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("pageSize", "" + this.pageSize);
        addParams("endTime", "" + this.endTime);
        return null;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime2) {
        this.endTime = endTime2;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize2) {
        this.pageSize = pageSize2;
    }
}
