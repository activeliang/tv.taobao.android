package com.yunos.tvtaobao.biz.request.item;

import anet.channel.strategy.dispatch.DispatchConstants;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateBagRequest extends BaseMtopRequest {
    private static final String API = "mtop.trade.updateBag";
    private static final long serialVersionUID = 4725190889190641767L;
    private String mCartFrom;
    private String mParams;

    public UpdateBagRequest(String params, String cartFrom) {
        this.mParams = params;
        this.mCartFrom = cartFrom;
        addParams("p", this.mParams);
        addParams("extStatus", "0");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("version", "1.1.1");
            jsonObject.put("mergeCombo", true);
            jsonObject.put("globalSell", "1");
            JSONObject tvtaoExtra = new JSONObject();
            tvtaoExtra.put("appKey", Config.getChannel());
            String tvOptions = TvOptionsConfig.getTvOptions();
            String tvOptionsResult = tvOptions.substring(0, tvOptions.length() - 1);
            ZpLogger.e("tvOptions = ", tvOptionsResult + "1");
            tvtaoExtra.put("tvOptions", tvOptionsResult + "1");
            jsonObject.put("tvtaoExtra", tvtaoExtra.toString());
            addParams(ApiUnitHelper.EX_QUERY_KEY, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject feature = new JSONObject();
        try {
            feature.put("gzip", true);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        addParams("feature", feature.toString());
        addParams("cartFrom", this.mCartFrom);
    }

    public String getTTid() {
        return Config.getChannel() + "@taobao_android_7.10.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return DispatchConstants.VER_CODE;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
