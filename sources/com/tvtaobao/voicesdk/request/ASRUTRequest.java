package com.tvtaobao.voicesdk.request;

import android.os.Build;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.Location;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ASRUTRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.speech.nlp.log";
    private final String VERSION = "1.0";
    String androidVersion = null;
    String appKey = null;
    String appPackage = null;
    private String lat = null;
    private Location location;
    private String lon = null;
    String model = null;
    String osVersion = null;
    String uuid = null;
    String versionCode = null;
    String versionName = null;

    public ASRUTRequest(String asr, String referrer) {
        JSONObject systemJson = new JSONObject();
        JSONObject sceneJson = new JSONObject();
        if (this.lat == null && this.lon == null) {
            String loc = SharePreferences.getString("location");
            if (loc != null) {
                this.location = (Location) JSON.parseObject(loc, Location.class);
            }
            if (this.location != null) {
                LogPrint.e(this.TAG, "NlpNewRequest userNick : " + this.location.userName + ", user : " + User.getNick());
                String userNick = this.location.userName;
                if (userNick != null && userNick.equals(User.getNick())) {
                    this.lat = this.location.x;
                    this.lon = this.location.y;
                }
            }
        }
        this.uuid = CloudUUIDWrapper.getCloudUUID();
        this.osVersion = SystemConfig.getSystemVersion();
        this.model = Build.MODEL;
        this.androidVersion = Build.VERSION.RELEASE;
        this.appPackage = AppInfo.getPackageName();
        this.appKey = Config.getChannel();
        this.versionCode = AppInfo.getAppVersionNum() + "";
        this.versionName = AppInfo.getAppVersionName();
        try {
            systemJson.put("uuid", this.uuid);
            systemJson.put("osVersion", this.osVersion);
            systemJson.put("model", this.model);
            systemJson.put("androidVersion", this.androidVersion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addParams("systemInfo", systemJson.toString());
        try {
            sceneJson.put("referrer", TextUtils.isEmpty(referrer) ? SDKInitConfig.getCurrentPage() : referrer);
            sceneJson.put("appPackage", this.appPackage);
            sceneJson.put("appKey", this.appKey);
            sceneJson.put("versionCode", this.versionCode);
            sceneJson.put("versionName", this.versionName);
            sceneJson.put("lat", this.lat);
            sceneJson.put("lon", this.lon);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        addParams("sceneInfo", sceneJson.toString());
        addParams(CommonData.TYPE_ASR, asr);
        LogPrint.e(this.TAG, "requestparams: system" + systemJson.toString() + "----scene---" + sceneJson.toString() + "-----asr---" + asr);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
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
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
