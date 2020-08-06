package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsBean;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GetGuessLikeRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.wireless.guess.get";

    public GetGuessLikeRequest(String channel) {
        String string = SharePreferences.getString("location");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currencyCode", "CNY");
            jsonObject.put("countryNumCode", "156");
            jsonObject.put("countryId", "CN");
            jsonObject.put("actualLanguageCode", "zh-CN");
            addParams("edition", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addParams("channel", channel);
        addParams("pageNum", "0");
        addParams(TvTaoSharedPerference.NICK, User.getNick());
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
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
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public GuessLikeGoodsBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("GuessLikeGoodsBean = ", obj.toString());
        return (GuessLikeGoodsBean) JSON.parseObject(obj.toString(), GuessLikeGoodsBean.class);
    }
}
