package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.RunMode;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetItemDetailV5Request extends BaseMtopRequest {
    private static final long serialVersionUID = -2299233377170054112L;
    private String TAG = "GetItemDetailV5Request";
    private String extParams;

    public GetItemDetailV5Request(String itemId, String extParams2) {
        this.extParams = extParams2;
        if (!TextUtils.isEmpty(itemId)) {
            addParams("id", itemId);
        }
        addParams("ttid", Config.getTTid());
    }

    /* access modifiers changed from: protected */
    public TBDetailResultVO resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.w(this.TAG, obj.toString());
        if (obj == null) {
            return null;
        }
        return (TBDetailResultVO) JSON.parseObject(obj.toString(), TBDetailResultVO.class);
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        if (Config.getRunMode() == RunMode.DAILY) {
            return "http://item.daily.taobao.net/modulet/v5/wdetailEsi.do";
        }
        return "https://hws.alicdn.com/cache/wdetail/5.0/";
    }

    /* access modifiers changed from: protected */
    public String getHttpParams() {
        String params = super.getHttpParams();
        if (!TextUtils.isEmpty(this.extParams)) {
            params = params + "&" + this.extParams;
        }
        ZpLogger.v(this.TAG, this.TAG + ".getHttpParams.params = " + params);
        return params;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return null;
    }
}
