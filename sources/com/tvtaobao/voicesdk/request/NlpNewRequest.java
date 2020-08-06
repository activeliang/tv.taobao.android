package com.tvtaobao.voicesdk.request;

import android.text.TextUtils;
import com.ali.user.open.core.Site;
import com.alibaba.fastjson.JSON;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.Location;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.location.LocationAssist;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class NlpNewRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.speech.nlp.ask";
    private final String VERSION = "2.0";
    private String androidVersion = null;
    private String appKey = null;
    private String appPackage = null;
    private String lat = null;
    private Location location;
    private String lon = null;
    private String model = null;
    private String osVersion = null;
    private String uuid = null;
    private String versionCode = null;
    private String versionName = null;

    /* JADX WARNING: Removed duplicated region for block: B:21:0x01d3  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x01e2 A[Catch:{ JSONException -> 0x032b }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0280 A[Catch:{ JSONException -> 0x033e }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0297 A[Catch:{ JSONException -> 0x033e }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0331 A[SYNTHETIC, Splitter:B:54:0x0331] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0339 A[Catch:{ JSONException -> 0x033e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NlpNewRequest(java.lang.String r15) {
        /*
            r14 = this;
            r12 = 0
            r14.<init>()
            java.lang.String r11 = "mtop.taobao.tvtao.speech.nlp.ask"
            r14.API = r11
            java.lang.String r11 = "2.0"
            r14.VERSION = r11
            r14.uuid = r12
            r14.osVersion = r12
            r14.model = r12
            r14.androidVersion = r12
            r14.appPackage = r12
            r14.appKey = r12
            r14.versionCode = r12
            r14.versionName = r12
            r14.lat = r12
            r14.lon = r12
            org.json.JSONObject r9 = new org.json.JSONObject
            r9.<init>()
            org.json.JSONObject r5 = new org.json.JSONObject
            r5.<init>()
            r14.initLocation()
            java.lang.String r11 = com.yunos.CloudUUIDWrapper.getCloudUUID()
            r14.uuid = r11
            java.lang.String r11 = r14.uuid
            boolean r11 = android.text.TextUtils.isEmpty(r11)
            if (r11 == 0) goto L_0x0042
            java.lang.String r11 = "false"
            r14.uuid = r11
        L_0x0042:
            java.lang.String r11 = com.yunos.tv.core.config.SystemConfig.getSystemVersion()
            r14.osVersion = r11
            java.lang.String r11 = android.os.Build.MODEL
            r14.model = r11
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            int r12 = android.os.Build.VERSION.SDK_INT
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = ""
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r14.androidVersion = r11
            java.lang.String r11 = com.yunos.tv.core.config.AppInfo.getPackageName()
            r14.appPackage = r11
            java.lang.String r11 = com.yunos.tv.core.config.Config.getChannel()
            r14.appKey = r11
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            int r12 = com.yunos.tv.core.config.AppInfo.getAppVersionNum()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = ""
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r14.versionCode = r11
            java.lang.String r11 = com.yunos.tv.core.config.AppInfo.getAppVersionName()
            r14.versionName = r11
            java.lang.String r11 = "uuid"
            java.lang.String r12 = r14.uuid     // Catch:{ JSONException -> 0x02e1 }
            r9.put(r11, r12)     // Catch:{ JSONException -> 0x02e1 }
            java.lang.String r11 = "osVersion"
            java.lang.String r12 = r14.osVersion     // Catch:{ JSONException -> 0x02e1 }
            r9.put(r11, r12)     // Catch:{ JSONException -> 0x02e1 }
            java.lang.String r11 = "model"
            java.lang.String r12 = r14.model     // Catch:{ JSONException -> 0x02e1 }
            r9.put(r11, r12)     // Catch:{ JSONException -> 0x02e1 }
            java.lang.String r11 = "androidVersion"
            java.lang.String r12 = r14.androidVersion     // Catch:{ JSONException -> 0x02e1 }
            r9.put(r11, r12)     // Catch:{ JSONException -> 0x02e1 }
        L_0x00b0:
            java.lang.String r11 = "systemInfo"
            java.lang.String r12 = r9.toString()
            r14.addParams(r11, r12)
            java.lang.String r11 = "appPackage"
            java.lang.String r12 = r14.appPackage     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "appKey"
            java.lang.String r12 = r14.appKey     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "versionCode"
            java.lang.String r12 = r14.versionCode     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "versionName"
            java.lang.String r12 = r14.versionName     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "businessScene"
            java.lang.String r12 = "APK内语音外卖主干流程"
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "referrer"
            java.lang.String r12 = com.tvtaobao.voicesdk.base.SDKInitConfig.getCurrentPage()     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            com.yunos.tv.core.CoreApplication r11 = com.yunos.tv.core.CoreApplication.getApplication()     // Catch:{ JSONException -> 0x030b }
            boolean r11 = com.tvtaobao.voicesdk.utils.ActivityUtil.isRunningForeground(r11)     // Catch:{ JSONException -> 0x030b }
            if (r11 == 0) goto L_0x0311
            android.app.Dialog r11 = com.tvtaobao.voicesdk.utils.ActivityUtil.getVoiceDialog()     // Catch:{ JSONException -> 0x030b }
            if (r11 == 0) goto L_0x02e7
            android.app.Dialog r11 = com.tvtaobao.voicesdk.utils.ActivityUtil.getVoiceDialog()     // Catch:{ JSONException -> 0x030b }
            java.lang.Class r11 = r11.getClass()     // Catch:{ JSONException -> 0x030b }
            if (r11 == 0) goto L_0x02e7
            java.lang.String r11 = "className"
            android.app.Dialog r12 = com.tvtaobao.voicesdk.utils.ActivityUtil.getVoiceDialog()     // Catch:{ JSONException -> 0x030b }
            java.lang.Class r12 = r12.getClass()     // Catch:{ JSONException -> 0x030b }
            java.lang.String r12 = r12.getName()     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
        L_0x0119:
            java.lang.String r11 = "from"
            java.lang.String r12 = "voice_application"
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
        L_0x0122:
            java.lang.String r11 = "sceneInfo"
            java.lang.String r12 = r5.toString()
            r14.addParams(r11, r12)
            java.lang.String r11 = "asr"
            r14.addParams(r11, r15)
            java.lang.String r11 = r14.TAG
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "requestparams: system---"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r9.toString()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = "\n"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = "scene---"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r5.toString()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = "\n"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = "asr---"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r15)
            java.lang.String r12 = r12.toString()
            com.tvtaobao.voicesdk.utils.LogPrint.i(r11, r12)
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0325 }
            r2.<init>()     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "umToken"
            com.yunos.tv.core.CoreApplication r12 = com.yunos.tv.core.CoreApplication.getApplication()     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r12 = com.yunos.tv.core.config.Config.getUmtoken(r12)     // Catch:{ JSONException -> 0x0325 }
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "wua"
            com.yunos.tv.core.CoreApplication r12 = com.yunos.tv.core.CoreApplication.getApplication()     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r12 = com.yunos.tv.core.config.Config.getWua(r12)     // Catch:{ JSONException -> 0x0325 }
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "isSimulator"
            com.yunos.tv.core.CoreApplication r12 = com.yunos.tv.core.CoreApplication.getApplication()     // Catch:{ JSONException -> 0x0325 }
            boolean r12 = com.yunos.tv.core.config.Config.isSimulator(r12)     // Catch:{ JSONException -> 0x0325 }
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "userAgent"
            com.yunos.tv.core.CoreApplication r12 = com.yunos.tv.core.CoreApplication.getApplication()     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r12 = com.yunos.tv.core.config.Config.getAndroidSystem(r12)     // Catch:{ JSONException -> 0x0325 }
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "versionName"
            java.lang.String r12 = r14.versionName     // Catch:{ JSONException -> 0x0325 }
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "platform"
            java.lang.String r12 = "apk"
            r2.put(r11, r12)     // Catch:{ JSONException -> 0x0325 }
            java.lang.String r11 = "extParams"
            java.lang.String r12 = r2.toString()     // Catch:{ JSONException -> 0x0325 }
            r14.addParams(r11, r12)     // Catch:{ JSONException -> 0x0325 }
        L_0x01cf:
            com.tvtaobao.voicesdk.bean.SearchObject r11 = com.tvtaobao.voicesdk.bean.ConfigVO.searchConfig
            if (r11 != 0) goto L_0x01da
            com.tvtaobao.voicesdk.bean.SearchObject r8 = new com.tvtaobao.voicesdk.bean.SearchObject
            r8.<init>()
            com.tvtaobao.voicesdk.bean.ConfigVO.searchConfig = r8
        L_0x01da:
            com.tvtaobao.voicesdk.bean.SearchObject r6 = com.tvtaobao.voicesdk.bean.ConfigVO.searchConfig     // Catch:{ JSONException -> 0x032b }
            boolean r11 = com.tvtaobao.voicesdk.base.SDKInitConfig.needShowUI()     // Catch:{ JSONException -> 0x032b }
            if (r11 == 0) goto L_0x01e9
            r11 = 0
            r6.startIndex = r11     // Catch:{ JSONException -> 0x032b }
            r11 = 17
            r6.endIndex = r11     // Catch:{ JSONException -> 0x032b }
        L_0x01e9:
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ JSONException -> 0x032b }
            r7.<init>()     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x032b }
            r11.<init>()     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = "{\"needJinNangs\":"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            boolean r12 = r6.needJinnang     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = ", \"needFeeds\":"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            boolean r12 = r6.needFeeds     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = ", \"tvOptions\":"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = com.yunos.tv.core.config.TvOptionsConfig.getTvOptions()     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = "}"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r1 = r11.toString()     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = "s"
            int r12 = r6.startIndex     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = java.lang.String.valueOf(r12)     // Catch:{ JSONException -> 0x032b }
            r7.put(r11, r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = "n"
            int r12 = r6.endIndex     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = java.lang.String.valueOf(r12)     // Catch:{ JSONException -> 0x032b }
            r7.put(r11, r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = "extendVoiceSearchQuery"
            r7.put(r11, r1)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = "appkey"
            java.lang.String r12 = com.yunos.tv.core.config.Config.getChannel()     // Catch:{ JSONException -> 0x032b }
            r7.put(r11, r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = "searchInfo"
            java.lang.String r12 = r7.toString()     // Catch:{ JSONException -> 0x032b }
            r14.addParams(r11, r12)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r11 = r14.TAG     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x032b }
            r12.<init>()     // Catch:{ JSONException -> 0x032b }
            java.lang.String r13 = "requestparams: searchInfo---"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r13 = r7.toString()     // Catch:{ JSONException -> 0x032b }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ JSONException -> 0x032b }
            java.lang.String r12 = r12.toString()     // Catch:{ JSONException -> 0x032b }
            com.tvtaobao.voicesdk.utils.LogPrint.i(r11, r12)     // Catch:{ JSONException -> 0x032b }
        L_0x0276:
            com.tvtaobao.voicesdk.bean.SearchObject r6 = com.tvtaobao.voicesdk.bean.ConfigVO.searchConfig     // Catch:{ JSONException -> 0x033e }
            r3 = 1
            r4 = 0
            boolean r11 = com.tvtaobao.voicesdk.base.SDKInitConfig.needShowUI()     // Catch:{ JSONException -> 0x033e }
            if (r11 == 0) goto L_0x0331
            r4 = 11
        L_0x0282:
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ JSONException -> 0x033e }
            r10.<init>()     // Catch:{ JSONException -> 0x033e }
            java.lang.String r11 = r14.lon     // Catch:{ JSONException -> 0x033e }
            boolean r11 = android.text.TextUtils.isEmpty(r11)     // Catch:{ JSONException -> 0x033e }
            if (r11 != 0) goto L_0x0339
            java.lang.String r11 = r14.lat     // Catch:{ JSONException -> 0x033e }
            boolean r11 = android.text.TextUtils.isEmpty(r11)     // Catch:{ JSONException -> 0x033e }
            if (r11 != 0) goto L_0x0339
            java.lang.String r11 = "longitude"
            java.lang.String r12 = r14.lon     // Catch:{ JSONException -> 0x033e }
            r10.put(r11, r12)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r11 = "latitude"
            java.lang.String r12 = r14.lat     // Catch:{ JSONException -> 0x033e }
            r10.put(r11, r12)     // Catch:{ JSONException -> 0x033e }
        L_0x02a7:
            java.lang.String r11 = "offset"
            int r12 = r6.startIndex     // Catch:{ JSONException -> 0x033e }
            r10.put(r11, r12)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r11 = "limit"
            java.lang.String r12 = java.lang.String.valueOf(r4)     // Catch:{ JSONException -> 0x033e }
            r10.put(r11, r12)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r11 = "takeoutInfo"
            java.lang.String r12 = r10.toString()     // Catch:{ JSONException -> 0x033e }
            r14.addParams(r11, r12)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r11 = r14.TAG     // Catch:{ JSONException -> 0x033e }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x033e }
            r12.<init>()     // Catch:{ JSONException -> 0x033e }
            java.lang.String r13 = "requestparams: takeoutInfo---"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r13 = r10.toString()     // Catch:{ JSONException -> 0x033e }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ JSONException -> 0x033e }
            java.lang.String r12 = r12.toString()     // Catch:{ JSONException -> 0x033e }
            com.tvtaobao.voicesdk.utils.LogPrint.i(r11, r12)     // Catch:{ JSONException -> 0x033e }
        L_0x02e0:
            return
        L_0x02e1:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00b0
        L_0x02e7:
            android.content.Context r11 = com.tvtaobao.voicesdk.utils.ActivityUtil.getTopActivity()     // Catch:{ JSONException -> 0x030b }
            if (r11 == 0) goto L_0x0119
            android.content.Context r11 = com.tvtaobao.voicesdk.utils.ActivityUtil.getTopActivity()     // Catch:{ JSONException -> 0x030b }
            java.lang.Class r11 = r11.getClass()     // Catch:{ JSONException -> 0x030b }
            if (r11 == 0) goto L_0x0119
            java.lang.String r11 = "className"
            android.content.Context r12 = com.tvtaobao.voicesdk.utils.ActivityUtil.getTopActivity()     // Catch:{ JSONException -> 0x030b }
            java.lang.Class r12 = r12.getClass()     // Catch:{ JSONException -> 0x030b }
            java.lang.String r12 = r12.getName()     // Catch:{ JSONException -> 0x030b }
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            goto L_0x0119
        L_0x030b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0122
        L_0x0311:
            java.lang.String r11 = "className"
            java.lang.String r12 = "all"
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            java.lang.String r11 = "from"
            java.lang.String r12 = "voice_system"
            r5.put(r11, r12)     // Catch:{ JSONException -> 0x030b }
            goto L_0x0122
        L_0x0325:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x01cf
        L_0x032b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0276
        L_0x0331:
            int r11 = r6.endIndex     // Catch:{ JSONException -> 0x033e }
            int r12 = r6.startIndex     // Catch:{ JSONException -> 0x033e }
            int r4 = r11 - r12
            goto L_0x0282
        L_0x0339:
            r14.getCurrentAddress(r10)     // Catch:{ JSONException -> 0x033e }
            goto L_0x02a7
        L_0x033e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x02e0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvtaobao.voicesdk.request.NlpNewRequest.<init>(java.lang.String):void");
    }

    private void initLocation() {
        String loc = SharePreferences.getString("location");
        if (!TextUtils.isEmpty(loc)) {
            this.location = (Location) JSON.parseObject(loc, Location.class);
            if (this.location != null) {
                LogPrint.e(this.TAG, "NlpNewRequest userNick : " + this.location.userName + ", user : " + User.getNick());
                String userNick = this.location.userName;
                if (userNick != null && userNick.equals(User.getNick())) {
                    this.lat = this.location.x;
                    this.lon = this.location.y;
                }
            }
        }
        if (TextUtils.isEmpty(this.lat) || TextUtils.isEmpty(this.lon)) {
            if (!TextUtils.isEmpty(SDKInitConfig.getLocation())) {
                this.location = (Location) JSON.parseObject(loc, Location.class);
            }
            if (this.location != null) {
                this.lat = this.location.x;
                this.lon = this.location.y;
            }
        }
    }

    /* access modifiers changed from: protected */
    public DomainResultVo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        try {
            DomainResultVo domainResultVo = (DomainResultVo) JSON.parseObject(obj.toString(), DomainResultVo.class);
            String resultVOType = domainResultVo.getResultVOType();
            if (TextUtils.isEmpty(resultVOType)) {
                return domainResultVo;
            }
            LogPrint.i(this.TAG, this.TAG + ".resultVOType=" + resultVOType);
            domainResultVo.setResultVO(JSON.parseObject(obj.getJSONObject("resultVO").toString(), Class.forName(domainResultVo.getResultVOType())));
            return domainResultVo;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.nlp.ask";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
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
