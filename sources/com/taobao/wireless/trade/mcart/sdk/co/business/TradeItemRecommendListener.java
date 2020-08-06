package com.taobao.wireless.trade.mcart.sdk.co.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.mtop.MtopTradeItemRecommendResponse;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;

public abstract class TradeItemRecommendListener extends AbstractCartRemoteBaseListener {
    public abstract void onSuccessExt(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj, ItemRecommendResult itemRecommendResult);

    public TradeItemRecommendListener(CartFrom cartFrom) {
        super(cartFrom);
    }

    public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object context) {
        super.onSuccess(requestType, response, pojo, context);
        onSuccessExt(requestType, response, pojo, context, dataProcess(pojo, response));
    }

    private ItemRecommendResult dataProcess(BaseOutDo pojo, MtopResponse response) {
        JSONObject data;
        ItemRecommendResult itemRecommendResult = new ItemRecommendResult();
        if (!(pojo == null || !(pojo instanceof MtopTradeItemRecommendResponse) || (data = ((MtopTradeItemRecommendResponse) pojo).getData()) == null)) {
            ArrayList<RecommendItemDetail> cartRecommendItemDetails = new ArrayList<>();
            JSONArray itemDetailArray = data.getJSONArray("result");
            if (itemDetailArray != null) {
                Iterator<Object> it = itemDetailArray.iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (obj != null) {
                        RecommendItemDetail recommendItemDetail = new RecommendItemDetail();
                        JSONObject itemDetailJObject = (JSONObject) obj;
                        recommendItemDetail.setItemId(itemDetailJObject.getString("itemId"));
                        recommendItemDetail.setItemName(itemDetailJObject.getString("itemName"));
                        recommendItemDetail.setPrice(itemDetailJObject.getString("price"));
                        recommendItemDetail.setPromotionPrice(itemDetailJObject.getString("promotionPrice"));
                        recommendItemDetail.setCategoryId(itemDetailJObject.getString("categoryId"));
                        recommendItemDetail.setUrl(itemDetailJObject.getString("url"));
                        recommendItemDetail.setPic(itemDetailJObject.getString("pic"));
                        recommendItemDetail.setCommentTimes(itemDetailJObject.getString("commentTimes"));
                        recommendItemDetail.setSellCount(itemDetailJObject.getString("sellCount"));
                        recommendItemDetail.setMonthSellCount(itemDetailJObject.getString("monthSellCount"));
                        recommendItemDetail.setSellerId(itemDetailJObject.getString("sellerId"));
                        recommendItemDetail.setRate(itemDetailJObject.getString("rate"));
                        JSONObject extMapjo = itemDetailJObject.getJSONObject("extMap");
                        HashMap<String, String> extMap = new HashMap<>();
                        if (extMapjo != null) {
                            extMap.put("cartNum", extMapjo.getString("cartNum"));
                            extMap.put("matchType", extMapjo.getString("matchType"));
                            extMap.put("score", extMapjo.getString("score"));
                            extMap.put("recommendExplain", extMapjo.getString("recExc"));
                            extMap.put("sku", extMapjo.getString("sku"));
                            extMap.put("triggerItem", extMapjo.getString("triggerItem"));
                            extMap.put("triggerItemPic", extMapjo.getString("triggerItemPic"));
                            extMap.put(BaseConfig.INTENT_KEY_SCM, extMapjo.getString(BaseConfig.INTENT_KEY_SCM));
                        }
                        recommendItemDetail.setExtMap(extMap);
                        cartRecommendItemDetails.add(recommendItemDetail);
                    }
                }
            }
            itemRecommendResult.setCartRecommendItemDetails(cartRecommendItemDetails);
            itemRecommendResult.setScm(data.getString(BaseConfig.INTENT_KEY_SCM));
        }
        return itemRecommendResult;
    }
}
