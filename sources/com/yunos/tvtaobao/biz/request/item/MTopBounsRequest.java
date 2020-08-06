package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;

public class MTopBounsRequest extends BaseHttpRequest {
    public String resolveResult(String result) throws Exception {
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "https://fragment.tmall.com/yunos/tianmaojiugongge?spm=0.0.0.0.5mxi3j";
    }
}
