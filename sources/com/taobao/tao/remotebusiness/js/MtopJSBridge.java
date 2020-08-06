package com.taobao.tao.remotebusiness.js;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;
import org.json.JSONArray;
import org.json.JSONObject;

public class MtopJSBridge {
    private static final String AUTO_LOGIN_ONLY = "AutoLoginOnly";
    private static final String AUTO_LOGIN_WITH_MANUAL = "AutoLoginAndManualLogin";
    private static final String DATA_TYPE_JSON = "json";
    private static final String DATA_TYPE_ORIGINAL_JSON = "originaljson";
    static final String TAG = "mtopsdk.MtopJSBridge";
    static volatile ScheduledExecutorService scheduledExecutorService;

    public interface MtopJSParam {
        public static final String API = "api";
        public static final String DATA = "data";
        public static final String DATA_TYPE = "dataType";
        public static final String EXT_HEADERS = "ext_headers";
        public static final String EXT_QUERYS = "ext_querys";
        public static final String METHOD = "method";
        public static final String MP_HOST = "mpHost";
        public static final String NEED_LOGIN = "needLogin";
        public static final String PAGE_URL = "pageUrl";
        public static final String SEC_TYPE = "secType";
        public static final String SESSION_OPTION = "sessionOption";
        public static final String TIMEOUT = "timeout";
        public static final String TTID = "ttid";
        @Deprecated
        public static final String USER_AGENT = "user-agent";
        public static final String V = "v";
        public static final String X_UA = "x-ua";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    public static void sendMtopRequest(Map<String, Object> jsParamMap, @NonNull IRemoteBaseListener listener) {
        if (listener == null) {
            TBSdkLog.e(TAG, "illegal param listener.");
        } else if (jsParamMap == null || jsParamMap.isEmpty()) {
            TBSdkLog.e(TAG, "illegal param jsParamMap.");
            listener.onSystemError(0, new MtopResponse(ErrorConstant.ERRCODE_ILLEGAL_JSPARAM_ERROR, ErrorConstant.ERRMSG_ILLEGAL_JSPARAM_ERROR), (Object) null);
        } else {
            MtopBusiness mtopBusiness = buildMtopBusiness(jsParamMap);
            if (mtopBusiness == null) {
                listener.onSystemError(0, new MtopResponse(ErrorConstant.ERRCODE_PARSE_JSPARAM_ERROR, ErrorConstant.ERRMSG_PARSE_JSPARAM_ERROR), (Object) null);
                return;
            }
            int timeout = 20000;
            try {
                timeout = ((Integer) jsParamMap.get(MtopJSParam.TIMEOUT)).intValue();
                if (timeout < 0) {
                    timeout = 20000;
                } else if (timeout > 60000) {
                    timeout = 60000;
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, "parse timeout (jsParam field) error.");
            }
            final MtopJSListener jsListener = new MtopJSListener(mtopBusiness, listener);
            mtopBusiness.registerListener((IRemoteListener) jsListener);
            mtopBusiness.startRequest();
            getScheduledExecutorService().schedule(new Runnable() {
                public void run() {
                    jsListener.onTimeOut();
                }
            }, (long) timeout, TimeUnit.MILLISECONDS);
        }
    }

    private static MtopBusiness buildMtopBusiness(Map<String, Object> jsParamMap) {
        MtopBusiness mtopBusiness = null;
        try {
            JSONObject jsonObject = new JSONObject(jsParamMap);
            String api = jsonObject.getString("api");
            String v = jsonObject.optString("v", "*");
            Map<String, String> dataMap = null;
            String data = "{}";
            JSONObject dataParam = jsonObject.optJSONObject("data");
            if (dataParam != null) {
                dataMap = new HashMap<>();
                Iterator it = dataParam.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = dataParam.get(key);
                    dataMap.put(key, value.toString());
                    if (!(value instanceof JSONArray) && !(value instanceof JSONObject)) {
                        dataParam.put(key, value.toString());
                    }
                }
                data = dataParam.toString();
            }
            boolean needLogin = jsonObject.optBoolean(MtopJSParam.NEED_LOGIN, false);
            String sessionOption = jsonObject.optString(MtopJSParam.SESSION_OPTION, AUTO_LOGIN_WITH_MANUAL);
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName(api);
            mtopRequest.setVersion(v);
            mtopRequest.setNeedEcode(needLogin);
            mtopRequest.setData(data);
            mtopRequest.dataParams = dataMap;
            mtopBusiness = MtopBusiness.build(mtopRequest);
            mtopBusiness.showLoginUI(!AUTO_LOGIN_ONLY.equalsIgnoreCase(sessionOption));
            if (MethodEnum.POST.getMethod().equalsIgnoreCase(jsonObject.optString("method", "GET"))) {
                mtopBusiness.reqMethod(MethodEnum.POST);
            }
            String mpHost = jsonObject.optString(MtopJSParam.MP_HOST, "");
            if (StringUtils.isNotBlank(mpHost)) {
                mtopBusiness.setCustomDomain(mpHost);
            }
            if (jsonObject.optInt(MtopJSParam.SEC_TYPE, 0) > 0) {
                mtopBusiness.useWua();
            }
            String dataType = jsonObject.optString(MtopJSParam.DATA_TYPE, "");
            if (!StringUtils.isBlank(dataType) && (DATA_TYPE_JSON.equals(dataType) || DATA_TYPE_ORIGINAL_JSON.equals(dataType))) {
                mtopBusiness.setJsonType(JsonTypeEnum.valueOf(dataType.toUpperCase(Locale.US)));
            }
            Map<String, String> headerMap = null;
            JSONObject ext_headers = jsonObject.optJSONObject(MtopJSParam.EXT_HEADERS);
            if (ext_headers != null) {
                headerMap = new HashMap<>();
                Iterator<String> it2 = ext_headers.keys();
                while (it2.hasNext()) {
                    String key2 = it2.next();
                    String value2 = ext_headers.getString(key2);
                    if (!TextUtils.isEmpty(key2) && !TextUtils.isEmpty(value2)) {
                        headerMap.put(key2, value2);
                    }
                }
            }
            String x_ua = jsonObject.optString("x-ua");
            if (!StringUtils.isBlank(x_ua)) {
                if (headerMap == null) {
                    headerMap = new HashMap<>();
                }
                headerMap.put("x-ua", URLEncoder.encode(x_ua, "utf-8"));
            }
            mtopBusiness.headers(headerMap);
            JSONObject ext_querys = jsonObject.optJSONObject(MtopJSParam.EXT_QUERYS);
            if (ext_querys != null) {
                Iterator<String> it3 = ext_querys.keys();
                while (it3.hasNext()) {
                    String key3 = it3.next();
                    String value3 = ext_querys.getString(key3);
                    if (!TextUtils.isEmpty(key3) && !TextUtils.isEmpty(value3)) {
                        mtopBusiness.addHttpQueryParameter(key3, value3);
                    }
                }
            }
            String ttid = jsonObject.optString("ttid");
            if (!StringUtils.isBlank(ttid)) {
                mtopBusiness.ttid(ttid);
            }
            String pageUrl = jsonObject.optString(MtopJSParam.PAGE_URL);
            if (!StringUtils.isBlank(pageUrl)) {
                mtopBusiness.setPageUrl(pageUrl);
            }
            mtopBusiness.setReqSource(1);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "parse mtop jsParamMap error, jsParamMap=" + jsParamMap, (Throwable) e);
        }
        return mtopBusiness;
    }

    static ScheduledExecutorService getScheduledExecutorService() {
        if (scheduledExecutorService == null) {
            synchronized (MtopJSBridge.class) {
                if (scheduledExecutorService == null) {
                    scheduledExecutorService = Executors.newScheduledThreadPool(1);
                }
            }
        }
        return scheduledExecutorService;
    }

    static class MtopJSListener implements IRemoteBaseListener, IRemoteCacheListener {
        private MtopResponse cachedResponse;
        private AtomicBoolean isFinish = new AtomicBoolean(false);
        final IRemoteBaseListener listener;
        private final MtopBusiness mtopBusiness;

        public MtopJSListener(MtopBusiness mtopBusiness2, IRemoteBaseListener listener2) {
            this.mtopBusiness = mtopBusiness2;
            this.listener = listener2;
        }

        public void onTimeOut() {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onTimeOut");
                }
                this.mtopBusiness.cancelRequest();
                try {
                    if (this.cachedResponse != null) {
                        this.listener.onSuccess(0, this.cachedResponse, (BaseOutDo) null, (Object) null);
                    } else {
                        this.listener.onSystemError(0, (MtopResponse) null, (Object) null);
                    }
                } catch (Exception e) {
                    TBSdkLog.e(MtopJSBridge.TAG, "do onTimeOut callback error.", (Throwable) e);
                }
            }
        }

        public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object requestContext) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onSuccess");
                }
                final int i = requestType;
                final MtopResponse mtopResponse = response;
                final BaseOutDo baseOutDo = pojo;
                final Object obj = requestContext;
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onSuccess(i, mtopResponse, baseOutDo, obj);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onSuccess callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }

        public void onError(final int requestType, final MtopResponse response, final Object requestContext) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onError");
                }
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onError(requestType, response, requestContext);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onError callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }

        public void onCached(MtopCacheEvent event, BaseOutDo pojo, Object context) {
            if (event != null) {
                this.cachedResponse = event.getMtopResponse();
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(MtopJSBridge.TAG, "callback onCached");
            }
        }

        public void onSystemError(final int requestType, final MtopResponse response, final Object requestContext) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onSystemError");
                }
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onSystemError(requestType, response, requestContext);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onSystemError callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }
    }
}
