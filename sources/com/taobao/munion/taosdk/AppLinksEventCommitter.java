package com.taobao.munion.taosdk;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.utils.EnvironmentUtils;
import com.taobao.alimama.utils.KeySteps;
import com.taobao.alimama.utils.UTAppBackgroundTimeoutDetector;
import com.taobao.alimama.utils.UserTrackLogs;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import com.taobao.muniontaobaosdk.util.TaoLog;
import com.taobao.orange.OrangeConfig;
import com.taobao.utils.Global;
import com.ut.mini.UTAnalytics;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class AppLinksEventCommitter implements MunionEventCommitter {
    private static final String a = "clickid";

    /* renamed from: a  reason: collision with other field name */
    private boolean f10a;
    private String b = "";

    /* renamed from: b  reason: collision with other field name */
    private boolean f11b;
    private String c = "";

    public AppLinksEventCommitter(boolean z) {
        this.f11b = z;
    }

    private void a(Uri uri) {
        if (uri != null && uri.isHierarchical()) {
            this.c = uri.getQueryParameter("ali_trackid");
            this.c = this.c == null ? "" : this.c;
            this.f10a = this.c.startsWith("2:mm");
            this.b = uri.toString();
        }
    }

    public Uri commitEvent(String str, Uri uri) {
        if (uri == null) {
            return null;
        }
        a(uri);
        return (!this.f10a || TextUtils.equals("on", OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, "external_cps_flow_intercept_switch", "on"))) ? MunionUrlBuilder.appendClickidToTargetUrl(uri, commitEvent(uri.toString())) : uri;
    }

    public String commitEvent(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            TaoLog.Loge("Munion", "广告请求参数或者点击URL为空");
            return "";
        }
        try {
            str2 = (this.f10a ? Constants.ClickIdPrefix.APP_LINK_CPS : Constants.ClickIdPrefix.APP_LINK_CPC) + SdkUtil.createClickID(Global.getApplication());
            HashMap hashMap = new HashMap();
            hashMap.put("alitrackid", this.c);
            hashMap.put("uptime", String.valueOf(SystemClock.uptimeMillis() - Global.StartupTime));
            UserTrackLogs.trackClick(Constants.UtEventId.CLICK_BEFORE, SdkUtil.buildUTKvs(hashMap), str2);
            KeySteps.mark("app_link_click_before", SdkUtil.buildUTKvs(hashMap), "clickid=" + str2);
            if (this.f11b) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("clickid", str2);
                UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(JSON.toJSONString(hashMap2));
                if (EnvironmentUtils.isInTaobao() && "on".equals(OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, "set_ut_tpk_param_switch", "on")) && UTAppBackgroundTimeoutDetector.getInstance().shouldRewriteTpkCache()) {
                    UTAnalytics.getInstance().getDefaultTracker().addTPKCache("adTraceOprId", str2);
                    UTAppBackgroundTimeoutDetector.getInstance().setShouldRewriteTpkCache(false);
                }
            }
            this.b = URLEncoder.encode(this.b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            TaoLog.Loge("Munion", e.getMessage());
        } catch (Throwable th) {
            TaoLog.Loge("Munion", th.getMessage());
            return "";
        }
        HashMap hashMap3 = new HashMap();
        hashMap3.put("redirecturl", this.b);
        UserTrackLogs.trackClick(Constants.UtEventId.CLICK_RETURN, SdkUtil.buildUTKvs(hashMap3), str2);
        KeySteps.mark("app_link_click_after", SdkUtil.buildUTKvs(hashMap3), "clickid=", str2);
        return str2;
    }

    public String commitEvent(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return str2;
        }
        a(Uri.parse(str2));
        return (!this.f10a || TextUtils.equals("on", OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, "external_cps_flow_intercept_switch", "on"))) ? MunionUrlBuilder.appendClickidToTargetUrl(str2, commitEvent(str2)) : str2;
    }
}
