package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsSearchZtcResult implements Serializable {
    private static final String TAG = GoodsSearchZtcResult.class.getSimpleName();
    private String current_page;
    private SearchedGoods[] goodlist;
    private String page_size;

    public void setCurrentPage(String current_page2) {
        this.current_page = current_page2;
    }

    public void setPageSize(String page_size2) {
        this.page_size = page_size2;
    }

    public void setGoodList(SearchedGoods[] goodlist2) {
        this.goodlist = goodlist2;
    }

    public String getCurrentPage() {
        return this.current_page;
    }

    public String getPageSize() {
        return this.page_size;
    }

    public SearchedGoods[] getGoodList() {
        return this.goodlist;
    }

    public String toString() {
        return "{ current_page = " + this.current_page + ", page_size = " + this.page_size + ", goodlist = " + this.goodlist + " , size = " + this.goodlist.length + " }";
    }

    public static GoodsSearchZtcResult resolveFromJson(String response) throws JSONException {
        GoodsSearchZtcResult goodsSearchResult = null;
        if (!TextUtils.isEmpty(response)) {
            String response2 = response.replaceAll("\\\\", "");
            if (response2.length() != 0) {
                JSONObject obj = new JSONObject(response2);
                JSONObject objPage = null;
                if (!obj.isNull("page")) {
                    objPage = obj.getJSONObject("page");
                }
                if (objPage != null) {
                    goodsSearchResult = new GoodsSearchZtcResult();
                    if (!objPage.isNull("current_page")) {
                        goodsSearchResult.setCurrentPage(objPage.getString("current_page"));
                    }
                    if (!objPage.isNull("page_size")) {
                        goodsSearchResult.setPageSize(objPage.getString("page_size"));
                    }
                    if (!obj.isNull("searchItems")) {
                        JSONArray array = obj.getJSONArray("searchItems");
                        GoodsWithZtc[] goodsList = new GoodsWithZtc[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            goodsList[i] = GoodsWithZtc.resolveFromJson(array.getJSONObject(i));
                        }
                        goodsSearchResult.setGoodList(goodsList);
                    }
                    ZpLogger.v(TAG, "GoodsSearchResult.resolveFromJson.goodsSearchResult = " + goodsSearchResult);
                }
            }
        }
        return goodsSearchResult;
    }

    public static GoodsSearchZtcResult resolveFromJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            ZpLogger.e(TAG, "GoodsSearchResult.resolveFromJson.obj == null ");
            return null;
        }
        GoodsSearchZtcResult goodsSearchResult = new GoodsSearchZtcResult();
        if (!obj.isNull("searchItems")) {
            JSONArray array = obj.getJSONArray("searchItems");
            SearchedGoods[] goodsList = new SearchedGoods[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (!"item".equals(jsonObject.getString("type"))) {
                    goodsList[i] = GoodsWithZtc.resolveFromJson(jsonObject);
                } else {
                    goodsList[i] = GoodsTmail.resolveFromJson(jsonObject);
                }
            }
            goodsSearchResult.setGoodList(goodsList);
        }
        ZpLogger.v(TAG, "GoodsSearchZtcResult.resolveFromJson.GoodsSearchZtcResult = " + goodsSearchResult);
        return goodsSearchResult;
    }
}
