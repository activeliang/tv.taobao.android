package com.tvtaobao.voicesdk.request;

import android.text.TextUtils;
import com.tvtaobao.voicesdk.bean.SearchObject;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceSearch extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.speech.item.search";
    private final String VERSION = "1.0";

    public VoiceSearch(SearchObject searchConfig, String tvOptions) {
        addParams("q", searchConfig.keyword);
        addParams("s", searchConfig.startIndex + "");
        addParams("n", String.valueOf(searchConfig.endIndex - searchConfig.startIndex));
        addParams("v", "2.0");
        String exquery = "{\"needJinNangs\":" + searchConfig.needJinnang + ", \"needFeeds\":" + searchConfig.needFeeds + ", \"tvOptions\":" + tvOptions + "}";
        LogPrint.e("TVTao_VoiceSearch", "VoiceSearch q : " + searchConfig.keyword + ", s : " + searchConfig.startIndex + " ,n : " + (searchConfig.endIndex - searchConfig.startIndex) + ", exquery : " + exquery);
        addParams("exquery", exquery);
        addParams("appkey", Config.getChannel());
        if (!TextUtils.isEmpty(searchConfig.priceScope)) {
            addParams("price", searchConfig.priceScope);
        }
        if (!TextUtils.isEmpty(searchConfig.sorting)) {
            addParams("sort", searchConfig.sorting);
        }
        addParams("appkey", Config.getChannel());
        try {
            JSONObject extParams = new JSONObject();
            extParams.put("umToken", Config.getUmtoken(CoreApplication.getApplication()));
            extParams.put("wua", Config.getWua(CoreApplication.getApplication()));
            extParams.put("isSimulator", Config.isSimulator(CoreApplication.getApplication()));
            extParams.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
            addParams("extParams", extParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.item.search";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
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
}
