package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemRates implements Serializable {
    private static final long serialVersionUID = -8097860001794189899L;
    private boolean annoy;
    private String feedback;
    private String feedbackDate;
    private AppendedFeed mAppendedFeed;
    private ArrayList<String> mPicUrlList;
    private Map<String, String> mSkuMap;
    private int rateType;
    private String userNick;
    private String userStar;
    private String userStarPic;

    public ArrayList<String> getPicUrlList() {
        return this.mPicUrlList;
    }

    public void setPicUrlList(ArrayList<String> mPicUrlList2) {
        this.mPicUrlList = mPicUrlList2;
    }

    public AppendedFeed getAppendedFeed() {
        return this.mAppendedFeed;
    }

    public void setAppendedFeed(AppendedFeed mAppendedFeed2) {
        this.mAppendedFeed = mAppendedFeed2;
    }

    public Map<String, String> getSkuMap() {
        return this.mSkuMap;
    }

    public void setSkuMap(Map<String, String> mSkuMap2) {
        this.mSkuMap = mSkuMap2;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String userNick2) {
        this.userNick = userNick2;
    }

    public String getUserStar() {
        return this.userStar;
    }

    public void setUserStar(String userStar2) {
        this.userStar = userStar2;
    }

    public String getUserStarPic() {
        return this.userStarPic;
    }

    public void setUserStarPic(String userStarPic2) {
        this.userStarPic = userStarPic2;
    }

    public int getRateType() {
        return this.rateType;
    }

    public void setRateType(int rateType2) {
        this.rateType = rateType2;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback2) {
        this.feedback = feedback2;
    }

    public String getFeedbackDate() {
        return this.feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate2) {
        this.feedbackDate = feedbackDate2;
    }

    public boolean getAnnoy() {
        return this.annoy;
    }

    public void setAnnoy(boolean annoy2) {
        this.annoy = annoy2;
    }

    public static ItemRates resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        ItemRates itemRates = new ItemRates();
        if (!obj.isNull("userStar")) {
            itemRates.setUserStar(obj.getString("userStar"));
        }
        if (!obj.isNull("userStarPic")) {
            itemRates.setUserStarPic(obj.getString("userStarPic"));
        }
        if (!obj.isNull("annoy")) {
            String annoy2 = obj.optString("annoy");
            if ("true".equals(annoy2) || "1".equals(annoy2)) {
                itemRates.setAnnoy(true);
            } else {
                itemRates.setAnnoy(false);
            }
        }
        if (!obj.isNull("userNick")) {
            String userNick2 = obj.getString("userNick");
            if (itemRates.getAnnoy()) {
                char[] ch = userNick2.toCharArray();
                userNick2 = ch[0] + "**" + ch[ch.length - 1];
            }
            itemRates.setUserNick(userNick2);
        }
        if (!obj.isNull("rateType")) {
            itemRates.setRateType(obj.getInt("rateType"));
        }
        if (!obj.isNull("feedback")) {
            itemRates.setFeedback(obj.getString("feedback"));
        }
        if (!obj.isNull("feedbackDate")) {
            itemRates.setFeedbackDate(obj.getString("feedbackDate"));
        }
        if (!obj.isNull("skuMap")) {
            Map<String, String> skuMap = new HashMap<>();
            JSONObject ob = obj.getJSONObject("skuMap");
            if (obj != null) {
                Iterator<String> it = ob.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    if (!TextUtils.isEmpty(key)) {
                        skuMap.put(key, ob.getString(key));
                    }
                }
                itemRates.setSkuMap(skuMap);
            }
        }
        if (!obj.isNull("feedPicPathList")) {
            JSONArray picArray = obj.getJSONArray("feedPicPathList");
            ArrayList<String> picList = new ArrayList<>();
            if (picArray != null) {
                for (int i = 0; i < picArray.length(); i++) {
                    String pic_url = picArray.getString(i);
                    if (!TextUtils.isEmpty(pic_url)) {
                        picList.add(pic_url);
                    }
                }
                itemRates.setPicUrlList(picList);
            }
        }
        if (obj.isNull("appendedFeed")) {
            return itemRates;
        }
        itemRates.setAppendedFeed(AppendedFeed.resolveFromMTOP(obj.getJSONObject("appendedFeed")));
        return itemRates;
    }
}
