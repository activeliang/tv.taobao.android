package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.MyTaoBaoModule;
import java.util.Map;
import org.json.JSONObject;

public class GetNewCollectsNumRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.mclaren.index.data.get";
    private static final long serialVersionUID = 1132143426750772778L;

    public GetNewCollectsNumRequest(int waitReceiveOrderCnt) {
        addParams("mytbVersion", AppInfo.getAppVersionName());
        addParams("moduleConfigVersion", "-1");
        addParams("dataConfigVersion", "-1");
        addParams("waitReceiveOrderCnt", waitReceiveOrderCnt + "");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public MyTaoBaoModule resolveResponse(JSONObject obj) throws Exception {
        if (obj != null && obj.has("data")) {
            return (MyTaoBaoModule) JSON.parseObject(obj.getJSONObject("data").toString(), MyTaoBaoModule.class);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "4.1";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
