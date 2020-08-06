package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TypeWordsRequestMtop extends BaseMtopRequest {
    private static final String TAG = TypeWordsRequestMtop.class.getSimpleName();

    public TypeWordsRequestMtop(String orgin) {
        addParams("sentence", orgin);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        obj.toString();
        ZpLogger.e(TAG, "语音 MTOPS-----yuan" + obj.toString());
        return obj.toString();
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.segmentationservice.tagging";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
