package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetainMentBo implements Serializable {
    private static final long serialVersionUID = 9098172277300419097L;
    private String algFlag;
    private String auction_tag;
    private String click_url;
    private String ctrScore;
    private String cvrScore;
    private String flag;
    private String matchType;
    private String month_sale;
    private String nid;
    private String pict_url;
    private String rankScore;
    private String rankType;
    private String real_wap_price;
    private String retentionScore;
    private String server;
    private String similarUrl;
    private String tiggerItemInfo;
    private String title;
    private String triggerItemId;
    private String weight;

    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid2) {
        this.nid = nid2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getPrice() {
        return this.real_wap_price;
    }

    public void setPrice(String real_wap_price2) {
        this.real_wap_price = real_wap_price2;
    }

    public String getPictUrl() {
        return this.pict_url;
    }

    public void setPictUrl(String pict_url2) {
        this.pict_url = pict_url2;
    }

    public String getClickUrl() {
        return this.click_url;
    }

    public void setClickUrl(String click_url2) {
        this.click_url = click_url2;
    }

    public String getAuctionTag() {
        return this.auction_tag;
    }

    public void setAuctionTag(String auction_tag2) {
        this.auction_tag = auction_tag2;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag2) {
        this.flag = flag2;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server2) {
        this.server = server2;
    }

    public String getCtrScore() {
        return this.ctrScore;
    }

    public void setCtrScore(String ctrScore2) {
        this.ctrScore = ctrScore2;
    }

    public String getCvrScore() {
        return this.cvrScore;
    }

    public void setCvrScore(String cvrScore2) {
        this.cvrScore = cvrScore2;
    }

    public String getRetentionScore() {
        return this.retentionScore;
    }

    public void setRetentionScore(String retentionScore2) {
        this.retentionScore = retentionScore2;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight2) {
        this.weight = weight2;
    }

    public String getTiggerItemInfo() {
        return this.tiggerItemInfo;
    }

    public void setTiggerItemInfo(String tiggerItemInfo2) {
        this.tiggerItemInfo = tiggerItemInfo2;
    }

    public String getMatchType() {
        return this.matchType;
    }

    public void setMatchType(String matchType2) {
        this.matchType = matchType2;
    }

    public String getTriggerItemId() {
        return this.triggerItemId;
    }

    public void setTriggerItemId(String triggerItemId2) {
        this.triggerItemId = triggerItemId2;
    }

    public String getRankType() {
        return this.rankType;
    }

    public void setRankType(String rankType2) {
        this.rankType = rankType2;
    }

    public String getRankScore() {
        return this.rankScore;
    }

    public void setRankScore(String rankScore2) {
        this.rankScore = rankScore2;
    }

    public String getAlgFlag() {
        return this.algFlag;
    }

    public void setAlgFlag(String algFlag2) {
        this.algFlag = algFlag2;
    }

    public String getMonthSale() {
        return this.month_sale;
    }

    public void setMonthSale(String month_sale2) {
        this.month_sale = month_sale2;
    }

    public String getSimilarUrl() {
        return this.similarUrl;
    }

    public void setSimilarUrl(String similarUrl2) {
        this.similarUrl = similarUrl2;
    }

    public static DetainMentBo[] resolve(String response) throws JSONException {
        JSONArray resultArray;
        if (TextUtils.isEmpty(response) || (resultArray = new JSONObject(response).optJSONArray("result")) == null || resultArray.length() == 0) {
            return null;
        }
        List<DetainMentBo> list = new ArrayList<>();
        list.clear();
        int lenth = resultArray.length();
        for (int i = 0; i < lenth; i++) {
            JSONObject result = resultArray.optJSONObject(i);
            if (result != null) {
                DetainMentBo detainMentBo = new DetainMentBo();
                detainMentBo.setNid(result.optString(BaseConfig.NID_FROM_CART));
                detainMentBo.setTitle(result.optString("title"));
                detainMentBo.setPrice(result.optString("real_wap_price"));
                detainMentBo.setPictUrl(result.optString("pict_url"));
                detainMentBo.setClickUrl(result.optString("click_url"));
                detainMentBo.setAuctionTag(result.optString("auction_tag"));
                detainMentBo.setFlag(result.optString("flag"));
                detainMentBo.setServer(result.optString("server"));
                detainMentBo.setCtrScore(result.optString("ctrScore"));
                detainMentBo.setCvrScore(result.optString("cvrScore"));
                detainMentBo.setRetentionScore(result.optString("retentionScore"));
                detainMentBo.setWeight(result.optString("weight"));
                detainMentBo.setTiggerItemInfo(result.optString("tiggerItemInfo"));
                detainMentBo.setMatchType(result.optString("matchType"));
                detainMentBo.setTriggerItemId(result.optString("triggerItemId"));
                detainMentBo.setRankType(result.optString("rankType"));
                detainMentBo.setRankScore(result.optString("rankScore"));
                detainMentBo.setAlgFlag(result.optString("algFlag"));
                detainMentBo.setMonthSale(result.optString("month_sale"));
                detainMentBo.setSimilarUrl(result.optString("similarUrl"));
                String title2 = detainMentBo.getTitle();
                String picurl = detainMentBo.getPictUrl();
                if (!TextUtils.isEmpty(title2) && !TextUtils.isEmpty(picurl)) {
                    list.add(detainMentBo);
                }
            }
        }
        if (!list.isEmpty()) {
            return (DetainMentBo[]) list.toArray(new DetainMentBo[list.size()]);
        }
        return null;
    }

    public String toString() {
        return "DetainMentBo [nid=" + this.nid + ", title=" + this.title + ", real_wap_price=" + this.real_wap_price + ", pict_url=" + this.pict_url + ", click_url=" + this.click_url + ", auction_tag=" + this.auction_tag + ", flag=" + this.flag + ", server=" + this.server + ", ctrScore=" + this.ctrScore + ", cvrScore=" + this.cvrScore + ", retentionScore=" + this.retentionScore + ", weight=" + this.weight + ", tiggerItemInfo=" + this.tiggerItemInfo + ", matchType=" + this.matchType + ", triggerItemId=" + this.triggerItemId + ", rankType=" + this.rankType + ", rankScore=" + this.rankScore + ", algFlag=" + this.algFlag + ", month_sale=" + this.month_sale + ", similarUrl=" + this.similarUrl + "]";
    }
}
