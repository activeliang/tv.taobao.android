package com.taobao.munion.taosdk;

import android.app.Application;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.fastjson.JSON;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.alimama.cpm.CpmAdHelper;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.net.pojo.request.SendCpmInfoRequest;
import com.taobao.alimama.net.pojo.response.P4pCpmInfoResponse;
import com.taobao.alimama.utils.EnvironmentUtils;
import com.taobao.alimama.utils.KeySteps;
import com.taobao.alimama.utils.UTAppBackgroundTimeoutDetector;
import com.taobao.alimama.utils.UserTrackLogs;
import com.taobao.business.mtop.MunionRemoteBusiness;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import com.taobao.muniontaobaosdk.util.TaoLog;
import com.taobao.orange.OrangeConfig;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.utils.Global;
import com.ut.mini.UTAnalytics;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class CpmEventCommitter extends MunionRemoteBusiness implements MunionEventCommitter {
    private static final int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static final String f16a = "clickid";

    /* renamed from: a  reason: collision with other field name */
    private boolean f17a;
    private String b;
    private String c;

    class a implements IRemoteBaseListener {

        /* renamed from: a  reason: collision with other field name */
        private String f18a;
        private String b;

        a(String str, String str2) {
            this.f18a = str;
            this.b = str2;
        }

        public void onError(int i, MtopResponse mtopResponse, Object obj) {
            if (mtopResponse != null) {
                byte[] bytedata = mtopResponse.getBytedata();
                TaoLog.Logd("Munion", new StringBuilder().append("Cpm 请求失败：Response Code: ").append(mtopResponse.getResponseCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret code: ").append(mtopResponse.getRetCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret msg: ").append(bytedata).toString() != null ? bytedata.toString() : Constant.NULL);
                return;
            }
            TaoLog.Logd("Munion", "Cpm 请求失败");
        }

        public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
            String str;
            Object data = ((P4pCpmInfoResponse) baseOutDo).getData();
            TaoLog.Logd("Munion", "Cpm 请求成功！ result is :" + data.toString());
            String obj2 = data.toString();
            String str2 = "";
            try {
                JSONObject jSONObject = new JSONObject(obj2);
                if (!(baseOutDo == null || jSONObject.get("result") == null)) {
                    str2 = jSONObject.get("result").toString();
                }
            } catch (JSONException e) {
                TaoLog.Loge("Munion", e.getMessage());
            }
            if (SdkUtil.isNotEmpty(str2)) {
                try {
                    str = "redirecturl=" + URLEncoder.encode(str2, "UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    TaoLog.Loge("Munion", e2.getMessage());
                    str = "";
                }
                UserTrackLogs.trackClick(Constants.UtEventId.CLICK_RETURN, str, this.f18a, this.b);
                KeySteps.mark("cpm_click_after", "args", str, "clickid", this.f18a);
                TaoLog.Logd("Munion", "usertrack update is [args=" + str + "]");
            }
        }

        public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
            if (mtopResponse != null) {
                byte[] bytedata = mtopResponse.getBytedata();
                TaoLog.Logd("Munion", new StringBuilder().append("Cpm 请求失败 System Error：Response Code: ").append(mtopResponse.getResponseCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret code: ").append(mtopResponse.getRetCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret msg: ").append(bytedata).toString() != null ? bytedata.toString() : Constant.NULL);
                return;
            }
            TaoLog.Logd("Munion", "Cpm 请求失败 System Error");
        }
    }

    public CpmEventCommitter(Application application) {
        this(application, true);
    }

    public CpmEventCommitter(Application application, boolean z) {
        super(application);
        this.b = "";
        this.c = "";
        this.f17a = z;
    }

    private String a(String str) {
        return (TextUtils.isEmpty(str) || !str.contains("click.mz.simba.taobao.com/brand")) ? Constants.ClickIdPrefix.CPM + SdkUtil.createClickID(this.mApplication) : Constants.ClickIdPrefix.CPM_BRAND + SdkUtil.createClickID(this.mApplication);
    }

    public Uri commitEvent(String str, Uri uri) {
        if (uri != null && uri.isHierarchical()) {
            try {
                this.c = URLEncoder.encode(uri.toString(), "UTF-8");
            } catch (Exception e) {
            }
            this.b = uri.getQueryParameter(com.taobao.muniontaobaosdk.util.a.i);
            this.b = this.b == null ? "" : this.b;
        }
        return MunionUrlBuilder.appendClickidToTargetUrl(uri, commitEvent(str));
    }

    public String commitEvent(String str) {
        if (str == null || str.trim().length() == 0) {
            TaoLog.Loge("Munion", "广告请求参数或者点击URL为空");
            return "";
        }
        try {
            Pair<Long, Long> cachedCpmAdvertiseTimetag = CpmAdHelper.getCachedCpmAdvertiseTimetag(str);
            if (cachedCpmAdvertiseTimetag != null) {
                long longValue = ((Long) cachedCpmAdvertiseTimetag.first).longValue();
                long longValue2 = ((Long) cachedCpmAdvertiseTimetag.second).longValue();
                long currentTimeMillis = System.currentTimeMillis() - longValue;
                if (currentTimeMillis > longValue2) {
                    UserTrackLogs.trackLog(Constants.UtEventId.CUSTOM, "munionAdForClickExpired", String.valueOf(currentTimeMillis), str, (String) null);
                }
            }
            String a2 = a(str);
            registeListener(new a(a2, this.b));
            SendCpmInfoRequest cpmInfoRequest = MunionRequestHelper.getCpmInfoRequest(this.mApplication, a2, str);
            HashMap hashMap = new HashMap();
            hashMap.put("ad_cid", SdkUtil.md5(str));
            hashMap.put(com.taobao.muniontaobaosdk.util.a.i, this.b);
            hashMap.put("aurl", this.c);
            hashMap.put("uptime", String.valueOf(SystemClock.uptimeMillis() - Global.StartupTime));
            UserTrackLogs.trackClick(Constants.UtEventId.CLICK_BEFORE, SdkUtil.buildUTKvs(hashMap), a2);
            KeySteps.mark("cpm_click_before", SdkUtil.buildUTKvs(hashMap), "clickid=" + a2);
            startRequest(0, cpmInfoRequest, P4pCpmInfoResponse.class);
            if (!this.f17a) {
                return a2;
            }
            HashMap hashMap2 = new HashMap();
            hashMap2.put("clickid", a2);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(JSON.toJSONString(hashMap2));
            if (!EnvironmentUtils.isInTaobao() || !"on".equals(OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, "set_ut_tpk_param_switch", "on")) || !UTAppBackgroundTimeoutDetector.getInstance().shouldRewriteTpkCache()) {
                return a2;
            }
            UTAnalytics.getInstance().getDefaultTracker().addTPKCache("adTraceOprId", a2);
            UTAppBackgroundTimeoutDetector.getInstance().setShouldRewriteTpkCache(false);
            return a2;
        } catch (Exception e) {
            TaoLog.Loge("Munion", e.getMessage());
            return "";
        }
    }

    public String commitEvent(String str, String str2) {
        if (str2 != null && str2.trim().length() > 0) {
            try {
                this.c = URLEncoder.encode(str2, "UTF-8");
            } catch (Exception e) {
            }
            Uri parse = Uri.parse(str2);
            if (parse != null && parse.isHierarchical()) {
                this.b = parse.getQueryParameter(com.taobao.muniontaobaosdk.util.a.i);
                this.b = this.b == null ? "" : this.b;
            }
        }
        return MunionUrlBuilder.appendClickidToTargetUrl(str2, commitEvent(str));
    }
}
