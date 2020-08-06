package com.taobao.alimama.cpm.ifs;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.taobao.alimama.cpm.CpmAdHelper;
import com.taobao.alimama.net.NetRequestCallback;
import com.taobao.alimama.net.NetRequestManager;
import com.taobao.alimama.net.core.future.NetFuture;
import com.taobao.alimama.net.core.state.NetRequestRetryPolicy;
import com.taobao.alimama.net.core.task.AliHttpRequestTask;
import com.taobao.alimama.threads.AdThreadExecutor;
import com.taobao.alimama.utils.KeySteps;
import com.taobao.alimama.utils.UserTrackLogs;
import com.taobao.muniontaobaosdk.util.MunionDeviceUtil;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import com.taobao.utils.Global;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class NEW_IfsCommitter {
    private static final int a = 1000;

    /* renamed from: a  reason: collision with other field name */
    private static final String f0a = "tanx.com";

    /* renamed from: a  reason: collision with other field name */
    private static Map<String, NetFuture> f1a = new ConcurrentHashMap();

    /* renamed from: a  reason: collision with other field name */
    private static Queue<String> f2a = new ConcurrentLinkedQueue();
    private String b;

    /* renamed from: b  reason: collision with other field name */
    private Map<String, String> f3b;
    /* access modifiers changed from: private */
    public String c;

    public enum ResultCode {
        COMMITED,
        INVALID_URL,
        DUPLICATED,
        INTERNAL_ERROR
    }

    private class a implements NetRequestCallback {
        private a() {
        }

        public void onFinalFailed(String str, String str2) {
            UserTrackLogs.trackAdLog("ifs_request_fail", SdkUtil.buildUTKvs(NEW_IfsCommitter.a(NEW_IfsCommitter.this)), NEW_IfsCommitter.this.b(NEW_IfsCommitter.a(NEW_IfsCommitter.this)));
            KeySteps.mark("ifs_request_fail", SdkUtil.buildUTKvs(NEW_IfsCommitter.a(NEW_IfsCommitter.this)), "ifs=" + NEW_IfsCommitter.a(NEW_IfsCommitter.this), NEW_IfsCommitter.this.b(NEW_IfsCommitter.a(NEW_IfsCommitter.this)), "error_code=" + str, "error_msg=" + str2);
            NEW_IfsCommitter.a().remove(NEW_IfsCommitter.this.c);
        }

        public void onSuccess(String str, Object obj) {
            UserTrackLogs.trackAdLog("ifs_request_success", SdkUtil.buildUTKvs(NEW_IfsCommitter.a(NEW_IfsCommitter.this)), NEW_IfsCommitter.this.b(NEW_IfsCommitter.a(NEW_IfsCommitter.this)));
            KeySteps.mark("ifs_request_success", SdkUtil.buildUTKvs(NEW_IfsCommitter.a(NEW_IfsCommitter.this)), "ifs=" + NEW_IfsCommitter.a(NEW_IfsCommitter.this), NEW_IfsCommitter.this.b(NEW_IfsCommitter.a(NEW_IfsCommitter.this)));
            NEW_IfsCommitter.a().remove(NEW_IfsCommitter.this.c);
            if (NEW_IfsCommitter.a().size() >= 1000) {
                NEW_IfsCommitter.a().poll();
            }
            NEW_IfsCommitter.a().offer(NEW_IfsCommitter.this.c);
        }

        public void onTempFailed(String str, String str2) {
        }
    }

    NEW_IfsCommitter(@NonNull String str, @Nullable Map<String, String> map) {
        this.b = str;
        this.f3b = map;
        this.c = a(str);
    }

    private static String a(String str) {
        return SdkUtil.md5(str);
    }

    private void a() {
        if (f2a.contains(this.c)) {
            UserTrackLogs.trackAdLog("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f3b), b(this.b));
            KeySteps.mark("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f3b), "ifs=" + this.b, b(this.b));
            return;
        }
        NetFuture netFuture = f1a.get(this.c);
        if (netFuture != null) {
            netFuture.retryNow();
            return;
        }
        AliHttpRequestTask.Builder builder = new AliHttpRequestTask.Builder(this.b, NetRequestRetryPolicy.RETRY_FIVE_TIMES);
        builder.setFollowRedirect(true);
        builder.setInnerRetryTimes(3);
        builder.setConnectTimeout(20000);
        builder.setReadTimeout(30000);
        builder.addHeader("Accept", MunionDeviceUtil.getAccept(Global.getApplication(), (String) null));
        AliHttpRequestTask aliHttpRequestTask = new AliHttpRequestTask(builder);
        aliHttpRequestTask.setCallback(new a());
        f1a.put(this.c, NetRequestManager.getInstance().startRequest(aliHttpRequestTask));
    }

    /* access modifiers changed from: private */
    public String b(String str) {
        return "useCache=" + (CpmAdHelper.isIfsUrlInCachedCpmAdvertise(str) ? "1" : "0");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public String m0a() {
        UserTrackLogs.trackAdLog("ifs_invoke_success", SdkUtil.buildUTKvs(this.f3b), b(this.b));
        KeySteps.mark("ifs_invoke_success", SdkUtil.buildUTKvs(this.f3b), "ifs=" + this.b, b(this.b));
        if (TextUtils.isEmpty(this.b) || TextUtils.isEmpty(this.c)) {
            KeySteps.mark("ifs_invalid_url", "msg=url_is_empty_or_hash_error", SdkUtil.buildUTKvs(this.f3b), "ifs=" + this.b, b(this.b));
            return ResultCode.INVALID_URL.name();
        }
        if (this.f3b == null || !this.f3b.containsKey("pid")) {
            try {
                String queryParameter = Uri.parse(this.b).getQueryParameter("pid");
                if (!TextUtils.isEmpty(queryParameter)) {
                    if (this.f3b == null) {
                        this.f3b = new HashMap();
                    }
                    this.f3b.put("pid", queryParameter);
                }
            } catch (Exception e) {
            }
        }
        String host = Uri.parse(this.b).getHost();
        if (host == null || !host.endsWith(f0a)) {
            KeySteps.mark("ifs_invalid_url", "msg=domain_not_right", SdkUtil.buildUTKvs(this.f3b), "ifs=" + this.b, b(this.b));
            return ResultCode.INVALID_URL.name();
        } else if (f2a.contains(this.c)) {
            UserTrackLogs.trackAdLog("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f3b), b(this.b));
            KeySteps.mark("ifs_invoke_duplicated", SdkUtil.buildUTKvs(this.f3b), "ifs=" + this.b, b(this.b));
            return ResultCode.DUPLICATED.name();
        } else {
            AdThreadExecutor.execute(new Runnable() {
                public void run() {
                    NEW_IfsCommitter.a(NEW_IfsCommitter.this);
                }
            });
            return ResultCode.COMMITED.name();
        }
    }
}
