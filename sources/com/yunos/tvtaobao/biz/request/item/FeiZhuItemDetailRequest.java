package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.google.gson.Gson;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.FeiZhuBean;
import com.yunos.tvtaobao.biz.request.bo.NewFeiZhuBean;
import com.zhiping.dev.android.logger.ZpLogger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeiZhuItemDetailRequest extends BaseMtopRequest {
    private static final String API = "mtop.trip.traveldetailskip.detail.get";
    private static final String apiversion = "5.0";

    public FeiZhuItemDetailRequest(String itemid) {
        if (!TextUtils.isEmpty(itemid)) {
            addParams("itemId", itemid);
        }
    }

    /* access modifiers changed from: protected */
    public FeiZhuBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("feizhu", obj.toString());
        NewFeiZhuBean newFeiZhuBean = (NewFeiZhuBean) new Gson().fromJson(obj.toString(), NewFeiZhuBean.class);
        ZpLogger.e("newFeiZhuBean1", newFeiZhuBean.toString());
        FeiZhuBean feiZhuBean = new FeiZhuBean();
        feiZhuBean.setNewFeiZhuBean(newFeiZhuBean);
        resolveFeiZhuService(obj, feiZhuBean);
        resolveFeiZhuSoldNum(obj, feiZhuBean);
        resolveFeiZhuText(obj, feiZhuBean);
        resolveFeiZhuRightDesc(obj, feiZhuBean);
        resolveFeiZhuMileage(obj, feiZhuBean);
        if (obj.has("coupon")) {
            feiZhuBean.setHasCoupon(true);
        } else {
            feiZhuBean.setHasCoupon(false);
        }
        if (!(!TextUtils.isEmpty(feiZhuBean.getNewPrice()) || newFeiZhuBean.getJhs() == null || newFeiZhuBean.getJhs().getData() == null || newFeiZhuBean.getJhs().getData().getStarted() == null || newFeiZhuBean.getJhs().getData().getStarted().getPrice() == null)) {
            String price = newFeiZhuBean.getJhs().getData().getStarted().getPrice();
            ZpLogger.e("feizhu price", price);
            if (price != null) {
                String priceResult = getPrice(price);
                feiZhuBean.setNewPrice(priceResult);
                ZpLogger.e("feizhu price4", priceResult);
            }
        }
        if (!(!TextUtils.isEmpty(feiZhuBean.getOldPrice()) || newFeiZhuBean.getJhs() == null || newFeiZhuBean.getJhs().getData() == null || newFeiZhuBean.getJhs().getData().getStarted() == null || newFeiZhuBean.getJhs().getData().getStarted().getOriginalPrice() == null)) {
            String price2 = newFeiZhuBean.getJhs().getData().getStarted().getOriginalPrice();
            ZpLogger.e("feizhu price", price2);
            if (price2 != null) {
                String priceResult2 = getPrice(price2);
                feiZhuBean.setOldPrice(priceResult2);
                ZpLogger.e("feizhu price4", priceResult2);
            }
        }
        ZpLogger.e("feizhu.tostring", feiZhuBean.toString());
        return feiZhuBean;
    }

    private void resolveFeiZhuText(JSONObject obj, FeiZhuBean feiZhuBean) throws JSONException {
        JSONObject buyBanner;
        if (obj.has("buyBanner") && obj.getString("buyBanner") != null && (buyBanner = obj.getJSONObject("buyBanner")) != null) {
            JSONObject data = buyBanner.getJSONObject("data");
            String buyButtonDesc = data.getString("buyButtonDesc");
            String carDesc = data.getString("carDesc");
            if (!buyButtonDesc.equals("")) {
                feiZhuBean.setBuyText(buyButtonDesc);
            }
            if (!carDesc.equals("")) {
                feiZhuBean.setCartText(carDesc);
            }
        }
    }

    private void resolveFeiZhuRightDesc(JSONObject obj, FeiZhuBean feiZhuBean) throws JSONException {
        JSONObject banner;
        if (obj.has("banner") && (banner = obj.getJSONObject("banner")) != null) {
            JSONObject data = banner.getJSONObject("data");
            if (data.has("rightDesc")) {
                String rightDesc = data.getString("rightDesc");
                if (!rightDesc.equals("")) {
                    feiZhuBean.setRightDesc(rightDesc);
                }
            }
        }
    }

    private void resolveFeiZhuMileage(JSONObject obj, FeiZhuBean feiZhuBean) throws JSONException {
        JSONObject mileage;
        if (obj.has("mileage") && (mileage = obj.getJSONObject("mileage")) != null) {
            JSONObject data = mileage.getJSONObject("data");
            if (data.has("flayerTitle")) {
                String flayerTitle = data.getString("flayerTitle");
                if (!flayerTitle.equals("")) {
                    feiZhuBean.setFlayerTitle(flayerTitle);
                }
            }
            if (data.has("title")) {
                String title = data.getString("title");
                if (!title.equals("")) {
                    feiZhuBean.setMileageTitle(title);
                }
            }
        }
    }

    private void resolveFeiZhuSoldNum(JSONObject obj, FeiZhuBean feiZhuBean) throws JSONException {
        if (obj.has("price") && obj.getString("price") != null) {
            JSONObject data = obj.getJSONObject("price").getJSONObject("data");
            JSONObject jsonObject = data.getJSONObject("aidedPrice");
            if (!jsonObject.has("price") || jsonObject.getString("price") == null) {
                feiZhuBean.setOldPrice("");
            } else {
                String price1 = jsonObject.getString("price");
                ZpLogger.e("feizhu price1", price1);
                if (price1 != null) {
                    String price2 = getPrice(price1);
                    feiZhuBean.setOldPrice(price2);
                    ZpLogger.e("feizhu price2", price2);
                }
            }
            if (!data.has("mainPrice") || data.getJSONObject("mainPrice") == null) {
                feiZhuBean.setNewPrice("");
            } else {
                JSONObject mainPrice = data.getJSONObject("mainPrice");
                if (mainPrice.has("price")) {
                    String price3 = mainPrice.getString("price");
                    ZpLogger.e("feizhu price3", price3);
                    if (price3 != null) {
                        String price4 = getPrice(price3);
                        feiZhuBean.setNewPrice(price4);
                        ZpLogger.e("feizhu price4", price4);
                    }
                } else {
                    feiZhuBean.setNewPrice("");
                }
            }
            JSONArray jsonArray = jsonObject.getJSONArray(SampleConfigConstant.ACCURATE);
            if (jsonArray.length() != 0) {
                int i = 0;
                while (i < jsonArray.length()) {
                    try {
                        String content = ((JSONObject) jsonArray.get(i)).optString("content");
                        if (content.contains("月售")) {
                            feiZhuBean.setSoldCount(getSoldCount(content));
                            return;
                        }
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (TextUtils.isEmpty((CharSequence) null) && obj.has("sold") && obj.get("sold") != null) {
                JSONObject soldObject = obj.getJSONObject("sold");
                if (soldObject.has("data") && soldObject.get("data") != null) {
                    JSONObject soldCountObject = soldObject.getJSONObject("data");
                    if (soldCountObject.has("soldCount") && soldCountObject.get("soldCount") != null) {
                        feiZhuBean.setSoldCount(soldCountObject.getString("soldCount"));
                    }
                }
            }
        }
    }

    private String getPrice(String s) {
        if (s.contains("-")) {
            String[] split = s.split("-");
            String substring = split[0].substring(0, split[0].length() - 2);
            return substring + "-" + split[1].substring(0, split[1].length() - 2);
        } else if (s.length() <= 2 || !s.endsWith("00")) {
            return new DecimalFormat("0.00").format((double) (((float) Integer.parseInt(s)) / 100.0f));
        } else {
            return s.substring(0, s.length() - 2);
        }
    }

    private String getSoldCount(String s) {
        String[] sp = s.split("月售")[1].split("笔");
        String s1 = "";
        if (!sp[0].contains(",")) {
            return sp[0];
        }
        String[] split1 = sp[0].split(",");
        for (int i = 0; i < split1.length; i++) {
            s1 = s1 + split1[i];
        }
        return s1;
    }

    private void resolveFeiZhuService(JSONObject jsonObject, FeiZhuBean feiZhuBean) throws JSONException {
        if (jsonObject.has("services") && jsonObject.getString("services") != null) {
            JSONArray jsonArray = jsonObject.getJSONObject("services").getJSONObject("data").getJSONArray("cells");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    list.add(((JSONObject) jsonArray.get(i)).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            feiZhuBean.setService(list);
        }
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return apiversion;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
