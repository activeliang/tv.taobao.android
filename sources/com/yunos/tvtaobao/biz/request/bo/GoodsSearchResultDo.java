package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsSearchResultDo implements Serializable {
    private static final String TAG = GoodsSearchResultDo.class.getSimpleName();
    private String current_page;
    private SearchedGoods[] goodlist;
    private boolean hasNextPage;
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

    public static GoodsSearchResultDo resolveFromJson(String response) throws JSONException {
        GoodsSearchResultDo goodsSearchResult = null;
        if (!TextUtils.isEmpty(response)) {
            String response2 = response.replaceAll("\\\\", "");
            if (response2.length() != 0) {
                JSONObject obj = new JSONObject(response2);
                JSONObject objPage = null;
                if (!obj.isNull("page")) {
                    objPage = obj.getJSONObject("page");
                }
                if (objPage != null) {
                    goodsSearchResult = new GoodsSearchResultDo();
                    if (!objPage.isNull("current_page")) {
                        goodsSearchResult.setCurrentPage(objPage.getString("current_page"));
                    }
                    if (!objPage.isNull("page_size")) {
                        goodsSearchResult.setPageSize(objPage.getString("page_size"));
                    }
                    if (!objPage.isNull("hasNextPage")) {
                        goodsSearchResult.setHasNextPage("true".equals(objPage.getString("hasNextPage")));
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

    public static GoodsSearchResultDo resolveFromJson(JSONObject obj) throws JSONException {
        RebateBo rebateBo;
        if (obj == null) {
            ZpLogger.e(TAG, "GoodsSearchResult.resolveFromJson.obj == null ");
            return null;
        }
        GoodsSearchResultDo goodsSearchResult = new GoodsSearchResultDo();
        if (!obj.isNull("hasNextPage")) {
            goodsSearchResult.setHasNextPage("true".equals(obj.getString("hasNextPage")));
        }
        if (!obj.isNull("searchAndTagItems")) {
            JSONArray array = obj.getJSONArray("searchAndTagItems");
            SearchedGoods[] goodsList = new SearchedGoods[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (!object.isNull("tvTaoSearchItemDO")) {
                    JSONObject jsonObject = object.getJSONObject("tvTaoSearchItemDO");
                    if (!"item".equals(jsonObject.getString("type"))) {
                        goodsList[i] = GoodsWithZtc.resolveFromJson(jsonObject);
                    } else {
                        goodsList[i] = GoodsTmail.resolveFromJson(jsonObject);
                    }
                }
                if (!object.isNull("itemTagDO") && (rebateBo = (RebateBo) JSON.parseObject(object.getJSONObject("itemTagDO").toString(), RebateBo.class)) != null) {
                    goodsList[i].setRebateBo(rebateBo);
                }
            }
            goodsSearchResult.setGoodList(goodsList);
        }
        ZpLogger.v(TAG, "GoodsSearchZtcResult.resolveFromJson.GoodsSearchZtcResult = " + goodsSearchResult);
        return goodsSearchResult;
    }

    public boolean hasNextPage() {
        return this.hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage2) {
        this.hasNextPage = hasNextPage2;
    }
}
