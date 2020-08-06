package com.yunos.tvtaobao.biz.request.bo;

import org.json.JSONException;
import org.json.JSONObject;

public class GuessLikeGoodsData {
    public static String TYPE_BANNER = "banner";
    public static String TYPE_ITEM = "item";
    public static String TYPE_RECOMMEND = "recommend";
    public static String TYPE_ZTC = SearchedGoods.TYPE_ZTC;
    private DynamicRecommend dynamicRecommend;
    private GuessLikeBannerBean guessLikeBannerBean;
    private GuessLikeGoods guessLikeGoods;
    private KMGoods kmGoods;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public GuessLikeGoods getGuessLikeGoods() {
        return this.guessLikeGoods;
    }

    public void setGuessLikeGoods(GuessLikeGoods guessLikeGoods2) {
        this.guessLikeGoods = guessLikeGoods2;
    }

    public KMGoods getKmGoods() {
        return this.kmGoods;
    }

    public void setKmGoods(KMGoods kmGoods2) {
        this.kmGoods = kmGoods2;
    }

    public DynamicRecommend getDynamicRecommend() {
        return this.dynamicRecommend;
    }

    public void setDynamicRecommend(DynamicRecommend dynamicRecommend2) {
        this.dynamicRecommend = dynamicRecommend2;
    }

    public GuessLikeBannerBean getGuessLikeBannerBean() {
        return this.guessLikeBannerBean;
    }

    public void setGuessLikeBannerBean(GuessLikeBannerBean guessLikeBannerBean2) {
        this.guessLikeBannerBean = guessLikeBannerBean2;
    }

    public static GuessLikeGoodsData resolveGoodsData(JSONObject object) {
        GuessLikeGoodsData guessLikeGoodsData = new GuessLikeGoodsData();
        try {
            String type2 = object.getString("type");
            if (TYPE_ITEM.equals(type2)) {
                guessLikeGoodsData.setGuessLikeGoods(GuessLikeGoods.resolveData(object));
                guessLikeGoodsData.setType(TYPE_ITEM);
            }
            if (TYPE_ZTC.equals(type2)) {
                guessLikeGoodsData.setKmGoods(KMGoods.resolveData(object));
                guessLikeGoodsData.setType(TYPE_ZTC);
            }
            if (TYPE_BANNER.equals(type2)) {
                guessLikeGoodsData.setGuessLikeBannerBean(GuessLikeBannerBean.resolveData(object));
                guessLikeGoodsData.setType(TYPE_BANNER);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guessLikeGoodsData;
    }
}
