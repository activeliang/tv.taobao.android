package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.analytics.core.Constants;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeFieldsVO;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsBean;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetRealTimeRecommondRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.wireless.chanel.realTimeRecommond";

    public GetRealTimeRecommondRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appid", "1640");
            jsonObject.put("albumId", "1");
            jsonObject.put("enabled", "true");
            addParams(CommonData.PARAM, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addParams("albumId", "PAY_SUCCESS");
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public GuessLikeGoodsBean resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.i("GetRealTime", obj.toString());
        GuessLikeGoodsBean guessLikeGoodsBean = new GuessLikeGoodsBean();
        GuessLikeGoodsBean.ResultVO resultVO = new GuessLikeGoodsBean.ResultVO();
        ArrayList arrayList = new ArrayList();
        JSONObject modelJsonObject = obj.getJSONObject("model");
        String currentPage = modelJsonObject.getString("currentPage");
        String currentTime = modelJsonObject.getString("currentTime");
        String empty = modelJsonObject.getString("empty");
        JSONArray itemListJsonArray = modelJsonObject.getJSONObject("result").getJSONArray("recommedResult").getJSONObject(0).getJSONArray("itemList");
        for (int i = 0; i < itemListJsonArray.length(); i++) {
            GuessLikeGoodsBean.ResultVO.RecommendVO recommendVO = new GuessLikeGoodsBean.ResultVO.RecommendVO();
            GuessLikeFieldsVO guessLikeFieldsVO = new GuessLikeFieldsVO();
            GuessLikeFieldsVO.BottomTipVO bottomTipVO = new GuessLikeFieldsVO.BottomTipVO();
            GuessLikeFieldsVO.MasterPicVO masterPicVO = new GuessLikeFieldsVO.MasterPicVO();
            JSONObject itemJsonObject = itemListJsonArray.getJSONObject(i);
            if (itemJsonObject.getString("type").equals(Constants.LogTransferLevel.HIGH)) {
                recommendVO.setType("item");
                GuessLikeFieldsVO.TitleVO titleTextVo = new GuessLikeFieldsVO.TitleVO();
                GuessLikeFieldsVO.TitleVO.ContextVO contextVO = new GuessLikeFieldsVO.TitleVO.ContextVO();
                if (itemJsonObject.has("title")) {
                    contextVO.setContent(itemJsonObject.getString("title"));
                    titleTextVo.setContext(contextVO);
                    guessLikeFieldsVO.setTitle(titleTextVo);
                    String recExc = itemJsonObject.getJSONObject("extMap").getString("recExc");
                    GuessLikeFieldsVO.BottomTipVO.TextVO bottomTextVo = new GuessLikeFieldsVO.BottomTipVO.TextVO();
                    bottomTextVo.setContent(recExc);
                    bottomTipVO.setText(bottomTextVo);
                    masterPicVO.setPicUrl(itemJsonObject.getString(TuwenConstants.PARAMS.PIC_URL));
                    guessLikeFieldsVO.setBottomTip(bottomTipVO);
                    guessLikeFieldsVO.setMasterPic(masterPicVO);
                    GuessLikeFieldsVO.PriceVO priceVO = new GuessLikeFieldsVO.PriceVO();
                    priceVO.setYuan(itemJsonObject.getString("marketPrice"));
                    priceVO.setSymbol("¥");
                    guessLikeFieldsVO.setPrice(priceVO);
                    guessLikeFieldsVO.setItemId(itemJsonObject.getString("itemId"));
                    recommendVO.setFields(guessLikeFieldsVO);
                    arrayList.add(recommendVO);
                }
            }
        }
        resultVO.setRecommedResult(arrayList);
        guessLikeGoodsBean.setCurrentPage(currentPage);
        guessLikeGoodsBean.setCurrentTime(currentTime);
        guessLikeGoodsBean.setEmpty(empty);
        guessLikeGoodsBean.setResult(resultVO);
        return guessLikeGoodsBean;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
