package com.taobao.munion.taosdk;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.alimama.cpm.CpmAdHelper;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.utils.KeySteps;
import com.taobao.alimama.utils.UserTrackLogs;
import com.taobao.muniontaobaosdk.util.MunionDeviceUtil;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import com.taobao.muniontaobaosdk.util.TaoLog;
import com.taobao.utils.Global;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CpmIfsCommitter {
    private static final int a = 5;

    /* renamed from: a  reason: collision with other field name */
    private static final String f19a = "ifs";

    /* renamed from: a  reason: collision with other field name */
    private static Queue<a> f20a = new ConcurrentLinkedQueue();
    private static final int b = 2;

    /* renamed from: b  reason: collision with other field name */
    private static final String f21b = "tanx.com";
    private static final int c = 1000;

    /* renamed from: a  reason: collision with other field name */
    private Application f22a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f23a;

    public enum ResultCode {
        COMMITED,
        INVALID_URL,
        DUPLICATED,
        INTERNAL_ERROR
    }

    private class a {
        private int a = 0;

        /* renamed from: a  reason: collision with other field name */
        private String f25a;
        private int b = 0;
        /* access modifiers changed from: private */

        /* renamed from: b  reason: collision with other field name */
        public String f26b;

        public a(String str, String str2) {
            this.f26b = str;
            this.f25a = str2;
        }

        public int a() {
            return this.a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public String m5a() {
            return this.f26b;
        }

        public void a(int i) {
            this.a = i;
        }

        public int b() {
            return this.b;
        }

        /* renamed from: b  reason: collision with other method in class */
        public String m6b() {
            return this.f25a;
        }

        public void b(int i) {
            this.b = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && (obj instanceof a) && b().equalsIgnoreCase(((a) obj).b());
        }
    }

    private static class b {
        public static final int a = 0;
        public static final int b = -1;
        public static final int c = 1;
        public static final int d = 2;

        private b() {
        }
    }

    class c implements NetworkCallBack.FinishListener, NetworkCallBack.ProgressListener, NetworkCallBack.ResponseCodeListener {
        private a a;

        public c(a aVar) {
            this.a = aVar;
        }

        public void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj) {
        }

        public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
            if (finishEvent == null || finishEvent.getHttpCode() != 200) {
                UserTrackLogs.trackAdLog("ifs_request_fail", "count=" + this.a.a(), SdkUtil.buildUTKvs(CpmIfsCommitter.this.f23a), CpmIfsCommitter.this.c(this.a.f26b));
                String[] strArr = new String[6];
                strArr[0] = "count=" + this.a.a();
                strArr[1] = SdkUtil.buildUTKvs(CpmIfsCommitter.this.f23a);
                strArr[2] = "ifs=" + this.a.f26b;
                strArr[3] = CpmIfsCommitter.this.c(this.a.f26b);
                strArr[4] = "error_code=" + (finishEvent == null ? Constant.NULL : String.valueOf(finishEvent.getHttpCode()));
                strArr[5] = "error_msg=" + (finishEvent == null ? Constant.NULL : finishEvent.getDesc());
                KeySteps.mark("ifs_request_fail", strArr);
                this.a.b(-1);
                boolean unused = CpmIfsCommitter.this.b(this.a);
                return;
            }
            this.a.b(2);
            UserTrackLogs.trackAdLog("ifs_request_success", SdkUtil.buildUTKvs(CpmIfsCommitter.this.f23a), CpmIfsCommitter.this.c(this.a.f26b));
            KeySteps.mark("ifs_request_success", SdkUtil.buildUTKvs(CpmIfsCommitter.this.f23a), "ifs=" + this.a.f26b, CpmIfsCommitter.this.c(this.a.f26b));
            boolean unused2 = CpmIfsCommitter.this.b(this.a);
        }

        public boolean onResponseCode(int i, Map<String, List<String>> map, Object obj) {
            return false;
        }
    }

    public CpmIfsCommitter(Application application) {
        this(application, (Map<String, String>) null);
    }

    public CpmIfsCommitter(Application application, Map<String, String> map) {
        this.f22a = application;
        this.f23a = map;
    }

    private void a() {
        int i = 0;
        for (a aVar : f20a) {
            if (aVar.b() == -1 && aVar.a() < 5) {
                a(aVar);
                i++;
                if (i >= 2) {
                    break;
                }
            }
            i = i;
        }
        TaoLog.Logi("Munion", String.format("[CpmIfsCommiter]retry fail ifs, count = %d", new Object[]{Integer.valueOf(i)}));
    }

    private void a(a aVar) {
        if (aVar != null) {
            String str = "";
            try {
                str = "ifs=" + URLEncoder.encode(aVar.a(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                TaoLog.Loge("Munion", e.getMessage());
            }
            UserTrackLogs.trackExceptionLog(Constants.UtEventId.CLICK_WAKEUP, f19a, str);
            TaoLog.Logd("Munion", "[CpmIfsCommiter]usertrack fail ifs: [args=" + str + "]");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m3a(a aVar) {
        if (this.f22a == null || aVar == null) {
            TaoLog.Loge("Munion", "[CpmIfsCommiter]async send request failed: application context or parameter is null!");
            return false;
        } else if (aVar.b() == 1) {
            return false;
        } else {
            try {
                DegradableNetwork degradableNetwork = new DegradableNetwork(this.f22a);
                RequestImpl requestImpl = new RequestImpl(aVar.a());
                requestImpl.setFollowRedirects(true);
                requestImpl.setCharset("UTF-8");
                requestImpl.setRetryTime(3);
                requestImpl.setConnectTimeout(20000);
                requestImpl.setReadTimeout(30000);
                requestImpl.addHeader("Accept", MunionDeviceUtil.getAccept(Global.getApplication(), (String) null));
                aVar.b(1);
                aVar.a(aVar.a() + 1);
                degradableNetwork.asyncSend(requestImpl, (Object) null, (Handler) null, new c(aVar));
                return true;
            } catch (Exception e) {
                TaoLog.Loge("Munion", "[CpmIfsCommiter]async send ifs request failed: " + e.getMessage());
                return false;
            }
        }
    }

    private String b(String str) {
        boolean z;
        if (this.f23a == null || !this.f23a.containsKey("pid")) {
            try {
                String queryParameter = Uri.parse(str).getQueryParameter("pid");
                if (!TextUtils.isEmpty(queryParameter)) {
                    if (this.f23a == null) {
                        this.f23a = new HashMap();
                    }
                    this.f23a.put("pid", queryParameter);
                }
            } catch (Exception e) {
            }
        }
        UserTrackLogs.trackAdLog("ifs_invoke_success", SdkUtil.buildUTKvs(this.f23a), c(str));
        KeySteps.mark("ifs_invoke_success", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(str));
        a();
        if (TextUtils.isEmpty(str)) {
            KeySteps.mark("ifs_invalid_url", "msg=url_is_null_or_empty", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(str));
            return ResultCode.INVALID_URL.name();
        } else if (f20a == null) {
            return ResultCode.INTERNAL_ERROR.name();
        } else {
            String md5 = SdkUtil.md5(str);
            if (TextUtils.isEmpty(md5)) {
                KeySteps.mark("ifs_invalid_url", "msg=md5(ifs)_error", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(str));
                return ResultCode.INVALID_URL.name();
            }
            try {
                if (!Uri.parse(str).getHost().toLowerCase().endsWith(f21b)) {
                    KeySteps.mark("ifs_invalid_url", "msg=domain_not_right", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(str));
                    return ResultCode.INVALID_URL.name();
                }
                a aVar = new a(str, md5);
                Iterator it = f20a.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    a aVar2 = (a) it.next();
                    if (aVar2.equals(aVar)) {
                        aVar = aVar2;
                        z = false;
                        break;
                    }
                }
                if (z) {
                    a(aVar);
                    return ResultCode.COMMITED.name();
                }
                UserTrackLogs.trackAdLog("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f23a), c(aVar.f26b));
                KeySteps.mark("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(aVar.f26b));
                if (aVar.b() == -1 && aVar.a() < 5) {
                    TaoLog.Logi("Munion", "[CpmIfsCommiter]commit fail ifs.");
                    a(aVar);
                }
                return ResultCode.DUPLICATED.name();
            } catch (Exception e2) {
                KeySteps.mark("ifs_invalid_url", "msg=ifs_is_not_a_url, ", SdkUtil.buildUTKvs(this.f23a), "ifs=" + str, c(str));
                return ResultCode.INVALID_URL.name();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized boolean b(a aVar) {
        boolean z;
        if (f20a == null || aVar == null) {
            z = false;
        } else {
            if (aVar.b() == -1 && aVar.a() >= 5) {
                a(aVar);
            }
            Iterator it = f20a.iterator();
            while (true) {
                if (it.hasNext()) {
                    a aVar2 = (a) it.next();
                    if (aVar2.equals(aVar)) {
                        aVar2.b(aVar.b());
                        aVar2.a(aVar.a());
                        z = true;
                        break;
                    }
                } else {
                    if (f20a.size() >= 1000) {
                        TaoLog.Logi("Munion", "[CpmIfsCommiter]ifs cache queue full!");
                        a poll = f20a.poll();
                        if (poll.b() != 2) {
                            a(poll);
                        }
                    }
                    f20a.offer(aVar);
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: private */
    public String c(String str) {
        return "useCache=" + (CpmAdHelper.isIfsUrlInCachedCpmAdvertise(str) ? "1" : "0");
    }

    public String a(String str) {
        return b(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m4a(String str) {
        b(str);
    }

    public void a(String str, String str2) {
        b(str2);
    }
}
