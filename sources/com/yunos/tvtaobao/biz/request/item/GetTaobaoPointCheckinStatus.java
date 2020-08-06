package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TaobaoPointCheckinStatusBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetTaobaoPointCheckinStatus extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tvtaopointservice.checkinStatus";
    private static final long serialVersionUID = 6403615798555209809L;

    public GetTaobaoPointCheckinStatus() {
        addParams("userId", User.getUserId());
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public TaobaoPointCheckinStatusBo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return (TaobaoPointCheckinStatusBo) JSON.parseObject(obj.toString(), TaobaoPointCheckinStatusBo.class);
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
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
