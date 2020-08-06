package com.yunos.tvtaobao.biz.request.ztc;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RedirectRequest {
    private static final String PREF_NAME = "ztc_init_status";
    private static final int STATUS_FAIL = 2;
    private static final int STATUS_INITING = 1;
    private static final int STATUS_NORMAL = 0;
    private static boolean systemSupportWebView = true;
    private Context context;
    /* access modifiers changed from: private */
    public String id = null;
    /* access modifiers changed from: private */
    public RedirectRequestListener mListener;
    private boolean shouldLoadWeb = true;
    private String userAgent;
    private WebView zpWebView;

    public interface RedirectRequestListener {
        void onItemIdRetrieveResult(boolean z, String str);
    }

    public RedirectRequest(Context context2, String agent, boolean isLoadUrl, RedirectRequestListener listener) {
        this.context = context2.getApplicationContext();
        this.mListener = listener;
        this.userAgent = agent;
        this.shouldLoadWeb = isLoadUrl;
    }

    public void requestParams(String url) {
        if (!TextUtils.isEmpty(url) && !Constant.NULL.equalsIgnoreCase(url)) {
            ZpLogger.d(RequestConstant.ENV_TEST, "requestParams:" + url);
            if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().getZtcConfig().isSingleRequest()) {
                doOkhttpRequest(url);
            } else {
                doOkhttpRequest(url);
            }
        } else if (this.mListener != null) {
            this.mListener.onItemIdRetrieveResult(false, (String) null);
        }
    }

    private void doOkhttpRequest(String url) {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        okhttpBuilder.addNetworkInterceptor(new Interceptor() {
            public Response intercept(Interceptor.Chain chain) throws IOException {
                ZpLogger.d(RequestConstant.ENV_TEST, "intercept:" + chain.request().url());
                Response respon = chain.proceed(chain.request());
                if (respon.isRedirect()) {
                    Uri location = Uri.parse(respon.header("Location"));
                    ZpLogger.d(RequestConstant.ENV_TEST, "response redirect location:" + location);
                    if (location.isHierarchical() && location.getQueryParameter("id") != null && RedirectRequest.this.id == null) {
                        String unused = RedirectRequest.this.id = location.getQueryParameter("id");
                        if (RedirectRequest.this.mListener != null) {
                            RedirectRequest.this.mListener.onItemIdRetrieveResult(true, RedirectRequest.this.id);
                            RedirectRequestListener unused2 = RedirectRequest.this.mListener = null;
                        }
                    }
                } else if (RedirectRequest.this.id == null && RedirectRequest.this.mListener != null) {
                    RedirectRequest.this.mListener.onItemIdRetrieveResult(false, RedirectRequest.this.id);
                    RedirectRequestListener unused3 = RedirectRequest.this.mListener = null;
                }
                return respon;
            }
        });
        Call call = okhttpBuilder.build().newCall(new Request.Builder().url(url).addHeader(HttpHeaders.USER_AGENT, this.userAgent).build());
        this.id = null;
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                ZpLogger.d(RequestConstant.ENV_TEST, "onFailure" + e.getMessage());
                if (RedirectRequest.this.mListener != null) {
                    RedirectRequest.this.mListener.onItemIdRetrieveResult(false, (String) null);
                    RedirectRequestListener unused = RedirectRequest.this.mListener = null;
                }
            }

            public void onResponse(Call call, Response response) throws IOException {
                String fid = response.request().url().queryParameter("id");
                ZpLogger.d(RequestConstant.ENV_TEST, "onResponse ,url:" + response.request().url() + ", fid:" + fid + ",id:" + RedirectRequest.this.id);
                if (fid != null && !fid.equals(RedirectRequest.this.id) && RedirectRequest.this.mListener != null) {
                    RedirectRequest.this.mListener.onItemIdRetrieveResult(!TextUtils.isEmpty(fid), fid);
                    RedirectRequestListener unused = RedirectRequest.this.mListener = null;
                }
            }
        });
    }
}
