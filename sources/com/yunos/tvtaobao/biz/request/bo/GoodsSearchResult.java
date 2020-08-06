package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoodsSearchResult implements Serializable {
    private static final long serialVersionUID = 9094487275465471716L;
    private String current_page;
    private GoodsTmail[] goodlist;
    private String page_size;
    private String total_num;
    private String total_page;

    public void setCurrentPage(String current_page2) {
        this.current_page = current_page2;
    }

    public void setPageSize(String page_size2) {
        this.page_size = page_size2;
    }

    public void setTotalNum(String total_num2) {
        this.total_num = total_num2;
    }

    public void setTotalPage(String total_page2) {
        this.total_page = total_page2;
    }

    public void setGoodList(GoodsTmail[] goodlist2) {
        this.goodlist = goodlist2;
    }

    public String getCurrentPage() {
        return this.current_page;
    }

    public String getPageSize() {
        return this.page_size;
    }

    public String getTotalNum() {
        return this.total_num;
    }

    public GoodsTmail[] getGoodList() {
        return this.goodlist;
    }

    public String getTotalPage() {
        return this.total_page;
    }

    public String toString() {
        return "{ current_page = " + this.current_page + ", page_size = " + this.page_size + ", total_num = " + this.total_num + ", total_page = " + this.total_page + ", goodlist = " + this.goodlist + " }";
    }

    public static GoodsSearchResult resolveFromJson(String response) throws JSONException {
        GoodsSearchResult goodsSearchResult = null;
        if (!TextUtils.isEmpty(response)) {
            String response2 = response.replaceAll("\\\\", "");
            if (response2.length() != 0) {
                JSONObject obj = new JSONObject(response2);
                JSONObject objPage = null;
                if (!obj.isNull("page")) {
                    objPage = obj.getJSONObject("page");
                }
                if (objPage != null) {
                    goodsSearchResult = new GoodsSearchResult();
                    if (!objPage.isNull("current_page")) {
                        goodsSearchResult.setCurrentPage(objPage.getString("current_page"));
                    }
                    if (!objPage.isNull("page_size")) {
                        goodsSearchResult.setPageSize(objPage.getString("page_size"));
                    }
                    if (!objPage.isNull("total_num")) {
                        goodsSearchResult.setTotalNum(objPage.getString("total_num"));
                    }
                    if (!objPage.isNull("total_page")) {
                        goodsSearchResult.setTotalPage(objPage.getString("total_page"));
                    }
                    if (!obj.isNull("item")) {
                        JSONArray array = obj.getJSONArray("item");
                        GoodsTmail[] goodsList = new GoodsTmail[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            goodsList[i] = GoodsTmail.resolveFromJson(array.getJSONObject(i));
                        }
                        goodsSearchResult.setGoodList(goodsList);
                    }
                    ZpLogger.v("GoodsSearchResult", "GoodsSearchResult.resolveFromJson.goodsSearchResult = " + goodsSearchResult);
                }
            }
        }
        return goodsSearchResult;
    }

    public static GoodsSearchResult resolveFromJson(JSONObject obj) throws JSONException {
        GoodsSearchResult goodsSearchResult = null;
        if (obj == null) {
            ZpLogger.e("GoodsSearchResult", "GoodsSearchResult.resolveFromJson.obj == null ");
        } else {
            JSONObject objPage = null;
            if (!obj.isNull("page")) {
                objPage = obj.getJSONObject("page");
            }
            if (objPage == null) {
                ZpLogger.e("GoodsSearchResult", "GoodsSearchResult.resolveFromJson.objPage == null ");
            } else {
                goodsSearchResult = new GoodsSearchResult();
                if (!objPage.isNull("currentPage")) {
                    goodsSearchResult.setCurrentPage(objPage.getString("currentPage"));
                }
                if (!objPage.isNull("pageSize")) {
                    goodsSearchResult.setPageSize(objPage.getString("pageSize"));
                }
                if (!objPage.isNull("totalNum")) {
                    goodsSearchResult.setTotalNum(objPage.getString("totalNum"));
                }
                if (!objPage.isNull("totalPage")) {
                    goodsSearchResult.setTotalPage(objPage.getString("totalPage"));
                }
                if (!obj.isNull("productList")) {
                    JSONArray array = obj.getJSONArray("productList");
                    GoodsTmail[] goodsList = new GoodsTmail[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        goodsList[i] = GoodsTmail.resolveFromJson(array.getJSONObject(i));
                    }
                    goodsSearchResult.setGoodList(goodsList);
                }
                ZpLogger.v("GoodsSearchResult", "GoodsSearchResult.resolveFromJson.goodsSearchResult = " + goodsSearchResult);
            }
        }
        return goodsSearchResult;
    }
}
