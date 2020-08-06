package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GuessLikeRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.guessBannerApi.queryGuessBanner";
    private final String VERSION = "1.0";

    public GuessLikeRequest(String business, String activityCode, int position) {
        addParams("kmBus", business);
        try {
            JSONObject job = new JSONObject();
            job.put("appkey", Config.getChannel());
            job.put("umtoken", Config.getUmtoken(CoreApplication.getApplication()));
            job.put(TvTaoSharedPerference.NICK, User.getNick());
            job.put("business", business);
            job.put("platform", "apk");
            addParams("extParams", job.toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("activityCode", activityCode);
            jsonObject.put("position", position);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            addParams("activityCodePositionArr", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public List<GuessLikeGoodsData> resolveResponse(JSONObject obj) throws Exception {
        JSONArray result = obj.getJSONArray("result");
        int length = result.length();
        List<GuessLikeGoodsData> dataList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            dataList.add(GuessLikeGoodsData.resolveGoodsData(result.getJSONObject(i)));
        }
        return dataList;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.guessBannerApi.queryGuessBanner";
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
