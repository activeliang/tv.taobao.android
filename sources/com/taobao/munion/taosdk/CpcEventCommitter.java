package com.taobao.munion.taosdk;

import android.app.Application;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.net.pojo.response.P4pCpcInfoResponse;
import com.taobao.alimama.threads.AdThreadExecutor;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class CpcEventCommitter extends MunionRemoteBusiness implements MunionEventCommitter {
    private static final int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static final String f12a = "clickid";

    /* renamed from: a  reason: collision with other field name */
    private boolean f13a;
    /* access modifiers changed from: private */
    public String b;
    private String c;

    class a implements IRemoteBaseListener {

        /* renamed from: a  reason: collision with other field name */
        private String f15a;
        private String b;

        public a(String str, String str2) {
            this.f15a = str;
            this.b = str2;
        }

        public void onError(int i, MtopResponse mtopResponse, Object obj) {
            if (mtopResponse != null) {
                byte[] bytedata = mtopResponse.getBytedata();
                TaoLog.Logd("Munion", new StringBuilder().append("Cpc 请求失败：Response Code: ").append(mtopResponse.getResponseCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret code: ").append(mtopResponse.getRetCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret msg: ").append(bytedata).toString() != null ? bytedata.toString() : Constant.NULL);
                return;
            }
            TaoLog.Logd("Munion", "Cpc 请求失败");
        }

        public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
            String str;
            Object data = ((P4pCpcInfoResponse) baseOutDo).getData();
            TaoLog.Logd("Munion", "Cpc 请求成功！ result is :" + data.toString());
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
                UserTrackLogs.trackClick(Constants.UtEventId.CLICK_RETURN, str, this.f15a, this.b);
                KeySteps.mark("cpc_click_after", "args", str, "clickid", this.f15a);
                TaoLog.Logd("Munion", "usertrack update is [args=" + str + "]");
            }
        }

        public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
            if (mtopResponse != null) {
                byte[] bytedata = mtopResponse.getBytedata();
                TaoLog.Logd("Munion", new StringBuilder().append("Cpc 请求失败 System Error：Response Code: ").append(mtopResponse.getResponseCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret code: ").append(mtopResponse.getRetCode()).append(SymbolExpUtil.SYMBOL_SEMICOLON).append("ret msg: ").append(bytedata).toString() != null ? bytedata.toString() : Constant.NULL);
                return;
            }
            TaoLog.Logd("Munion", "Cpc 请求失败 System Error");
        }
    }

    public CpcEventCommitter(Application application) {
        this(application, true);
    }

    public CpcEventCommitter(Application application, boolean z) {
        super(application);
        this.b = "";
        this.c = "";
        this.f13a = z;
    }

    private void a(final String str, final String str2) {
        AdThreadExecutor.execute(new Runnable() {
            public void run() {
                String str;
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
                    httpURLConnection.setInstanceFollowRedirects(false);
                    String headerField = httpURLConnection.getHeaderField("Location");
                    if (TextUtils.isEmpty(headerField)) {
                        TaoLog.Logd("Munion", "Cpc 请求失败：Response Code: " + httpURLConnection.getResponseCode() + SymbolExpUtil.SYMBOL_SEMICOLON + "Response msg: " + httpURLConnection.getResponseMessage());
                        return;
                    }
                    try {
                        str = "redirecturl=" + URLEncoder.encode(headerField, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        TaoLog.Loge("Munion", e.getMessage());
                        str = "";
                    }
                    UserTrackLogs.trackClick(Constants.UtEventId.CLICK_RETURN, str, str2, CpcEventCommitter.this.b);
                    KeySteps.mark("cpc_click_after", "args", str, "clickid", str2);
                    TaoLog.Logd("Munion", "usertrack update is [args=" + str + "]");
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public String a(String str, boolean z) {
        if (str == null || str.trim().length() == 0) {
            TaoLog.Loge("Munion", "广告请求参数或者点击URL为空");
            return "";
        }
        try {
            String str2 = Constants.ClickIdPrefix.CPC + SdkUtil.createClickID(this.mApplication);
            HashMap hashMap = new HashMap();
            hashMap.put("ad_cid", SdkUtil.md5(str));
            hashMap.put(com.taobao.muniontaobaosdk.util.a.i, this.b);
            hashMap.put("aurl", this.c);
            hashMap.put("uptime", String.valueOf(SystemClock.uptimeMillis() - Global.StartupTime));
            UserTrackLogs.trackClick(Constants.UtEventId.CLICK_BEFORE, SdkUtil.buildUTKvs(hashMap), str2);
            KeySteps.mark("cpc_click_before", SdkUtil.buildUTKvs(hashMap), "clickid=" + str2);
            if (z) {
                a(str, str2);
            } else {
                registeListener(new a(str2, this.b));
                startRequest(0, MunionRequestHelper.getCpcInfoRequest(this.mApplication, str2, str), P4pCpcInfoResponse.class);
            }
            if (!this.f13a) {
                return str2;
            }
            HashMap hashMap2 = new HashMap();
            hashMap2.put("clickid", str2);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(JSON.toJSONString(hashMap2));
            if (!EnvironmentUtils.isInTaobao() || !"on".equals(OrangeConfig.getInstance().getConfig(Constants.ORANGE_GROUP_NAME, "set_ut_tpk_param_switch", "on")) || !UTAppBackgroundTimeoutDetector.getInstance().shouldRewriteTpkCache()) {
                return str2;
            }
            UTAnalytics.getInstance().getDefaultTracker().addTPKCache("adTraceOprId", str2);
            UTAppBackgroundTimeoutDetector.getInstance().setShouldRewriteTpkCache(false);
            return str2;
        } catch (Exception e) {
            TaoLog.Loge("Munion", e.getMessage());
            return "";
        }
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
        return a(str, false);
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
