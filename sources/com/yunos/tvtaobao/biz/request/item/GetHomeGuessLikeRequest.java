package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.focus_impl.FocusRoot;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeFieldsVO;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsBean;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetHomeGuessLikeRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.wireless.guess.get";

    public GetHomeGuessLikeRequest(String channel) {
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
        GuessLikeGoodsBean guessLikeGoodsBean = new GuessLikeGoodsBean();
        GuessLikeGoodsBean.ResultVO resultVO = new GuessLikeGoodsBean.ResultVO();
        List<GuessLikeGoodsBean.ResultVO.RecommendVO> list = new ArrayList<>();
        try {
            JSONObject datas = obj.getJSONObject("data");
            JSONObject hierarchy = obj.getJSONObject("hierarchy");
            JSONArray hierarchyKey = hierarchy.getJSONObject("structure").getJSONArray(hierarchy.getString(FocusRoot.TAG_FLAG_FOR_ROOT));
            for (int i = 0; i < hierarchyKey.length(); i++) {
                JSONObject data = datas.getJSONObject(hierarchyKey.getString(i));
                GuessLikeGoodsBean.ResultVO.RecommendVO recommendVO = new GuessLikeGoodsBean.ResultVO.RecommendVO();
                JSONObject fields = data.getJSONObject("fields");
                JSONObject trackParamShow = fields.optJSONObject("trackParamShow");
                if (trackParamShow != null && trackParamShow.has("itemId")) {
                    String itemId = trackParamShow.getString("itemId");
                    GuessLikeFieldsVO guessLikeFieldsVO = (GuessLikeFieldsVO) JSON.parseObject(fields.toString(), GuessLikeFieldsVO.class);
                    guessLikeFieldsVO.setItemId(itemId);
                    recommendVO.setFields(guessLikeFieldsVO);
                    recommendVO.setBizName(data.getString("bizName"));
                    recommendVO.setType(data.getString("type"));
                    list.add(recommendVO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ZpLogger.v("GetGuessLikeRequest", "homePage GuessLikeData error " + e.toString());
        }
        resultVO.setRecommedResult(list);
        guessLikeGoodsBean.setResult(resultVO);
        return guessLikeGoodsBean;
    }
}
