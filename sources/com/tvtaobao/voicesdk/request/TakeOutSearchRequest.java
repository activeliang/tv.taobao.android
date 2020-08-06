package com.tvtaobao.voicesdk.request;

import android.text.TextUtils;
import com.ali.user.open.core.Site;
import com.alibaba.fastjson.JSON;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.tvtaobao.voicesdk.bean.Location;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.location.LocationAssist;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutSearchRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.takeout.queryKeywordShopListNew";
    private final String VERSION = "1.0";
    private String lat = null;
    private Location location;
    private String lon = null;

    public TakeOutSearchRequest(String keywords, String offset, int pageSize, String locaStr, String orderType) {
        JSONObject object = new JSONObject();
        try {
            if (!TextUtils.isEmpty(locaStr)) {
                this.location = (Location) JSON.parseObject(locaStr, Location.class);
                this.lat = this.location.x;
                this.lon = this.location.y;
            }
            if (TextUtils.isEmpty(this.lat) && TextUtils.isEmpty(this.lon)) {
                String loc = SharePreferences.getString("location");
                if (loc != null) {
                    this.location = (Location) JSON.parseObject(loc, Location.class);
                }
                if (this.location != null) {
                    this.lat = this.location.x;
                    this.lon = this.location.y;
                }
            }
            if (TextUtils.isEmpty(this.lon) || TextUtils.isEmpty(this.lat)) {
                getCurrentAddress(object);
            } else {
                object.put(ClientTraceData.b.f54c, this.lon);
                object.put(ClientTraceData.b.d, this.lat);
            }
            object.put("keyword", keywords);
            object.put("offset", offset);
            object.put("limit", String.valueOf(pageSize));
            object.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
            if (!TextUtils.isEmpty(orderType)) {
                object.put("orderType", orderType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addParams("jsonStr", object.toString());
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.takeout.queryKeywordShopListNew";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    public void getCurrentAddress(JSONObject takeoutInfo) {
        String localStr = SharePreferences.getString("elemeLoc");
        try {
            if (!LoginHelperImpl.getJuLoginHelper().isLogin() || TextUtils.isEmpty(localStr)) {
                takeoutInfo.put(ClientTraceData.b.d, LocationAssist.getInstance().getLatitude());
                takeoutInfo.put(ClientTraceData.b.f54c, LocationAssist.getInstance().getLongitude());
                return;
            }
            JSONObject saveData = new JSONObject(localStr);
            if (Site.ELEME.equals(saveData.optString("type"))) {
                if (LoginHelperImpl.getJuLoginHelper().getUserId().equals(saveData.optString("tbUserId"))) {
                    this.lat = saveData.optString("lat");
                    this.lon = saveData.optString("lng");
                    takeoutInfo.put(ClientTraceData.b.d, this.lat);
                    takeoutInfo.put(ClientTraceData.b.f54c, this.lon);
                    return;
                }
                takeoutInfo.put(ClientTraceData.b.d, LocationAssist.getInstance().getLatitude());
                takeoutInfo.put(ClientTraceData.b.f54c, LocationAssist.getInstance().getLongitude());
                return;
            }
            takeoutInfo.put(ClientTraceData.b.d, LocationAssist.getInstance().getLatitude());
            takeoutInfo.put(ClientTraceData.b.f54c, LocationAssist.getInstance().getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
