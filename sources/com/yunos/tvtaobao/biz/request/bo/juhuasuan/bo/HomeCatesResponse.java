package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeCatesResponse extends BaseMO {
    private static final long serialVersionUID = -7587642820388329581L;
    private ArrayList<HomeCatesBo> cates;
    private HomeBackgroundBo homeBackground;
    private ArrayList<HomeItemsBo> items;

    public static HomeCatesResponse resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        HomeCatesResponse item = new HomeCatesResponse();
        item.setHomeBackground(HomeBackgroundBo.resolveFromMTOP(obj.optJSONObject("homeBackground")));
        if (obj.has("items")) {
            ArrayList<HomeItemsBo> list = new ArrayList<>();
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                list.add(HomeItemsBo.resolveFromMTOP(array.getJSONObject(i)));
            }
            item.setItems(list);
        }
        if (!obj.has("cates")) {
            return item;
        }
        ArrayList<HomeCatesBo> list2 = new ArrayList<>();
        JSONArray array2 = obj.getJSONArray("cates");
        for (int i2 = 0; i2 < array2.length(); i2++) {
            list2.add(HomeCatesBo.resolveFromMTOP(array2.getJSONObject(i2)));
        }
        item.setCates(list2);
        return item;
    }

    public String toString() {
        return "HomeCatesResponse [homeBackground=" + this.homeBackground + ", items=" + this.items + ", cates=" + this.cates + "]";
    }

    public HomeBackgroundBo getHomeBackground() {
        return this.homeBackground;
    }

    public void setHomeBackground(HomeBackgroundBo homeBackground2) {
        this.homeBackground = homeBackground2;
    }

    public ArrayList<HomeItemsBo> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<HomeItemsBo> items2) {
        this.items = items2;
    }

    public ArrayList<HomeCatesBo> getCates() {
        return this.cates;
    }

    public void setCates(ArrayList<HomeCatesBo> cates2) {
        this.cates = cates2;
    }
}
