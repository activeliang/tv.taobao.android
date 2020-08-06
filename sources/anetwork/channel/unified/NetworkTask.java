package anetwork.channel.unified;

import android.text.TextUtils;
import anet.channel.Config;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.session.HttpSession;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.cache.CacheHelper;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.http.NetworkSdkSetting;
import anetwork.channel.stat.NetworkStat;
import anetwork.channel.statist.StatisticReqTimes;
import anetwork.channel.util.RequestConstant;
import com.uc.webview.export.internal.interfaces.IWaStat;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

class NetworkTask implements IUnifiedTask {
    public static final String TAG = "anet.NetworkTask";
    Cache cache = null;
    ByteArrayOutputStream cacheBuffer = null;
    volatile Cancelable cancelable = null;
    int contentLength = 0;
    int dataChunkIndex = 0;
    Cache.Entry entry = null;
    String f_refer = DispatchConstants.OTHER;
    volatile boolean isCanceled = false;
    volatile AtomicBoolean isDone = null;
    RequestContext rc;
    int statusCode = 0;

    NetworkTask(RequestContext rc2, Cache cache2, Cache.Entry entry2) {
        this.rc = rc2;
        this.isDone = rc2.isDone;
        this.cache = cache2;
        this.entry = entry2;
        this.f_refer = rc2.config.getHeaders().get("f-refer");
    }

    public void cancel() {
        this.isCanceled = true;
        this.rc.config.getStatistic().ret = 2;
        if (this.cancelable != null) {
            this.cancelable.cancel();
        }
    }

    public void run() {
        if (!this.isCanceled) {
            if (!NetworkStatusHelper.isConnected()) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "network unavailable", this.rc.seqNum, "NetworkStatus", NetworkStatusHelper.getStatus());
                }
                this.rc.callback.onFinish(new DefaultFinishEvent(ErrorConstant.ERROR_NO_NETWORK));
                return;
            }
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "exec request", this.rc.seqNum, "retryTimes", Integer.valueOf(this.rc.config.getCurrentRetryTimes()));
            }
            try {
                sendRequest(tryGetSession(), this.rc.config.getAwcnRequest());
            } catch (Exception e) {
                ALog.e(TAG, "send request failed.", this.rc.seqNum, e, new Object[0]);
            }
        }
    }

    private HttpUrl checkCName(HttpUrl httpUrl) {
        HttpUrl tmp;
        String hostCName = this.rc.config.getHeaders().get(HttpConstant.X_HOST_CNAME);
        if (TextUtils.isEmpty(hostCName) || (tmp = HttpUrl.parse(httpUrl.urlString().replaceFirst(httpUrl.host(), hostCName))) == null) {
            return httpUrl;
        }
        return tmp;
    }

    private SessionCenter getSessionCenter() {
        String appkey = this.rc.config.getRequestProperty("APPKEY");
        if (TextUtils.isEmpty(appkey)) {
            return SessionCenter.getInstance();
        }
        ENV env = ENV.ONLINE;
        String requestEnv = this.rc.config.getRequestProperty(RequestConstant.ENVIRONMENT);
        if (RequestConstant.ENV_PRE.equalsIgnoreCase(requestEnv)) {
            env = ENV.PREPARE;
        } else if (RequestConstant.ENV_TEST.equalsIgnoreCase(requestEnv)) {
            env = ENV.TEST;
        }
        if (env != NetworkSdkSetting.CURRENT_ENV) {
            NetworkSdkSetting.CURRENT_ENV = env;
            SessionCenter.switchEnvironment(env);
        }
        Config config = Config.getConfig(appkey, env);
        if (config == null) {
            config = new Config.Builder().setAppkey(appkey).setEnv(env).setAuthCode(this.rc.config.getRequestProperty(RequestConstant.AUTH_CODE)).build();
        }
        return SessionCenter.getInstance(config);
    }

    private Session tryGetSession() {
        boolean z;
        SessionCenter instance = getSessionCenter();
        Session session = null;
        HttpUrl httpUrl = this.rc.config.getHttpUrl();
        boolean containsNonDefaultPort = httpUrl.containsNonDefaultPort();
        RequestStatistic rs = this.rc.config.getStatistic();
        if (this.rc.config.getRequestType() == 1 && NetworkConfigCenter.isSpdyEnabled() && this.rc.config.getCurrentRetryTimes() == 0 && !containsNonDefaultPort) {
            long start = System.currentTimeMillis();
            session = instance.get(checkCName(httpUrl), ConnType.TypeLevel.SPDY, 5000);
            rs.connWaitTime = System.currentTimeMillis() - start;
            if (session != null) {
                z = true;
            } else {
                z = false;
            }
            rs.spdyRequestSend = z;
        }
        if (session == null && this.rc.config.isHttpSessionEnable() && !containsNonDefaultPort && !NetworkStatusHelper.isProxy()) {
            session = instance.get(httpUrl, ConnType.TypeLevel.HTTP, 0);
        }
        if (session == null) {
            ALog.i(TAG, "create HttpSession with local DNS", this.rc.seqNum, new Object[0]);
            session = new HttpSession(GlobalAppRuntimeInfo.getContext(), new ConnInfo(httpUrl.key(), this.rc.seqNum, (IConnStrategy) null));
        }
        this.rc.statisticData.connectionType = session.getConnType().toString();
        this.rc.statisticData.isSSL = session.getConnType().isSSL();
        if (this.rc.config.getRequestType() == 1 && this.rc.config.getCurrentRetryTimes() > 0 && rs.spdyRequestSend) {
            rs.degraded = 1;
        }
        ALog.i(TAG, "tryGetSession", this.rc.seqNum, "Session", session);
        return session;
    }

    private void sendRequest(Session session, Request req) {
        if (session != null && !this.isCanceled) {
            Request.Builder builder = null;
            if (this.rc.config.isRequestCookieEnabled()) {
                String cookie = CookieManager.getCookie(this.rc.config.getUrlString());
                if (!TextUtils.isEmpty(cookie)) {
                    builder = req.newBuilder();
                    String customCookie = req.getHeaders().get(HttpConstant.COOKIE);
                    if (!TextUtils.isEmpty(customCookie)) {
                        cookie = StringUtils.concatString(customCookie, "; ", cookie);
                    }
                    builder.addHeader(HttpConstant.COOKIE, cookie);
                }
            }
            if (this.entry != null) {
                if (builder == null) {
                    builder = req.newBuilder();
                }
                if (this.entry.etag != null) {
                    builder.addHeader("If-None-Match", this.entry.etag);
                }
                if (this.entry.lastModified > 0) {
                    builder.addHeader("If-Modified-Since", CacheHelper.toGMTDate(this.entry.lastModified));
                }
            }
            final Request request = builder == null ? req : builder.build();
            StatisticReqTimes.getIntance().putReq(request.getHttpUrl());
            this.rc.config.getStatistic().requestStart = System.currentTimeMillis();
            this.cancelable = session.request(request, new RequestCb() {
                public void onResponseCode(int code, Map<String, List<String>> headers) {
                    String location;
                    if (!NetworkTask.this.isDone.get()) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(NetworkTask.TAG, "onResponseCode", request.getSeq(), "code", Integer.valueOf(code));
                            ALog.i(NetworkTask.TAG, "onResponseCode", request.getSeq(), "headers", headers);
                        }
                        if (HttpHelper.checkRedirect(request, code) && (location = HttpHelper.getSingleHeaderFieldByKey(headers, "Location")) != null) {
                            HttpUrl httpUrl = HttpUrl.parse(location);
                            if (httpUrl == null) {
                                ALog.e(NetworkTask.TAG, "redirect url is invalid!", request.getSeq(), "redirect url", location);
                            } else if (NetworkTask.this.isDone.compareAndSet(false, true)) {
                                if (httpUrl.scheme() == null) {
                                    httpUrl.setScheme(StrategyCenter.getInstance().getSchemeByHost(httpUrl.host(), (String) null));
                                }
                                httpUrl.lockScheme();
                                NetworkTask.this.rc.config.redirectToUrl(httpUrl);
                                NetworkTask.this.rc.statisticData.host = NetworkTask.this.rc.config.getHttpUrl().host();
                                NetworkTask.this.rc.isDone = new AtomicBoolean();
                                NetworkTask.this.rc.runningTask = new NetworkTask(NetworkTask.this.rc, (Cache) null, (Cache.Entry) null);
                                ThreadPoolExecutorFactory.submitPriorityTask(NetworkTask.this.rc.runningTask, 0);
                                return;
                            } else {
                                return;
                            }
                        }
                        try {
                            NetworkTask.this.rc.cancelTimeoutTask();
                            NetworkTask.this.statusCode = code;
                            CookieManager.setCookie(NetworkTask.this.rc.config.getUrlString(), headers);
                            NetworkTask.this.contentLength = HttpHelper.parseContentLength(headers);
                            if (code != 304 || NetworkTask.this.entry == null) {
                                if (NetworkTask.this.cache != null && "GET".equals(request.getMethod())) {
                                    NetworkTask.this.entry = CacheHelper.parseCacheHeaders(headers);
                                    if (NetworkTask.this.entry != null) {
                                        HttpHelper.removeHeaderFiledByKey(headers, "Cache-Control");
                                        headers.put("Cache-Control", Arrays.asList(new String[]{"no-store"}));
                                        NetworkTask.this.cacheBuffer = new ByteArrayOutputStream(NetworkTask.this.contentLength != 0 ? NetworkTask.this.contentLength : 5120);
                                    }
                                }
                                NetworkTask.this.rc.callback.onResponseCode(code, headers);
                                return;
                            }
                            NetworkTask.this.entry.responseHeaders.putAll(headers);
                            NetworkTask.this.rc.callback.onResponseCode(200, NetworkTask.this.entry.responseHeaders);
                            NetworkTask.this.rc.callback.onDataReceiveSize(1, NetworkTask.this.entry.data.length, ByteArray.wrap(NetworkTask.this.entry.data));
                        } catch (Exception e) {
                            ALog.w(NetworkTask.TAG, "[onResponseCode] error.", NetworkTask.this.rc.seqNum, e, new Object[0]);
                        }
                    }
                }

                public void onDataReceive(ByteArray data, boolean fin) {
                    if (!NetworkTask.this.isDone.get()) {
                        if (NetworkTask.this.dataChunkIndex == 0) {
                            ALog.i(NetworkTask.TAG, "[onDataReceive] receive first data chunk!", NetworkTask.this.rc.seqNum, new Object[0]);
                        }
                        if (fin) {
                            ALog.i(NetworkTask.TAG, "[onDataReceive] receive last data chunk!", NetworkTask.this.rc.seqNum, new Object[0]);
                        }
                        try {
                            NetworkTask.this.dataChunkIndex++;
                            NetworkTask.this.rc.callback.onDataReceiveSize(NetworkTask.this.dataChunkIndex, NetworkTask.this.contentLength, data);
                            if (NetworkTask.this.cacheBuffer != null) {
                                NetworkTask.this.cacheBuffer.write(data.getBuffer(), 0, data.getDataLength());
                                if (fin) {
                                    String cacheKey = NetworkTask.this.rc.config.getUrlString();
                                    NetworkTask.this.entry.data = NetworkTask.this.cacheBuffer.toByteArray();
                                    long start = System.currentTimeMillis();
                                    NetworkTask.this.cache.put(cacheKey, NetworkTask.this.entry);
                                    ALog.i(NetworkTask.TAG, "write cache", NetworkTask.this.rc.seqNum, IWaStat.KEY_COST, Long.valueOf(System.currentTimeMillis() - start), "size", Integer.valueOf(NetworkTask.this.entry.data.length), "key", cacheKey);
                                }
                            }
                        } catch (Exception e) {
                            ALog.w(NetworkTask.TAG, "[onDataReceive] error.", NetworkTask.this.rc.seqNum, e, new Object[0]);
                        }
                    }
                }

                public void onFinish(int code, String msg, RequestStatistic rs) {
                    DefaultFinishEvent finishEvent;
                    if (!NetworkTask.this.isDone.getAndSet(true)) {
                        NetworkTask.this.rc.cancelTimeoutTask();
                        if (ALog.isPrintLog(2)) {
                            ALog.i(NetworkTask.TAG, "[onFinish]", NetworkTask.this.rc.seqNum, "code", Integer.valueOf(code), "msg", msg);
                        }
                        if (code < 0) {
                            try {
                                if (NetworkTask.this.rc.config.isAllowRetry()) {
                                    NetworkTask.this.rc.config.retryRequest();
                                    NetworkTask.this.rc.isDone = new AtomicBoolean();
                                    NetworkTask.this.rc.runningTask = new NetworkTask(NetworkTask.this.rc, NetworkTask.this.cache, NetworkTask.this.entry);
                                    rs.appendErrorTrace(code);
                                    ThreadPoolExecutorFactory.submitPriorityTask(NetworkTask.this.rc.runningTask);
                                    return;
                                }
                            } catch (Exception e) {
                                return;
                            }
                        }
                        if (NetworkTask.this.statusCode == 0) {
                            NetworkTask.this.statusCode = code;
                        }
                        rs.statusCode = NetworkTask.this.statusCode;
                        rs.msg = msg;
                        NetworkTask.this.rc.statisticData.filledBy(rs);
                        if (NetworkTask.this.statusCode != 304 || NetworkTask.this.entry == null) {
                            finishEvent = new DefaultFinishEvent(NetworkTask.this.statusCode, msg, NetworkTask.this.rc.statisticData);
                        } else {
                            rs.protocolType = "cache";
                            finishEvent = new DefaultFinishEvent(200, msg, NetworkTask.this.rc.statisticData);
                        }
                        NetworkTask.this.rc.callback.onFinish(finishEvent);
                        if (ALog.isPrintLog(2)) {
                            ALog.i(NetworkTask.TAG, NetworkTask.this.rc.statisticData.toString(), NetworkTask.this.rc.seqNum, new Object[0]);
                        }
                        if (code != -200) {
                            AppMonitor.getInstance().commitStat(rs);
                        }
                        if (code >= 0) {
                            BandWidthSampler.getInstance().onDataReceived(rs.start, rs.start + rs.oneWayTime, rs.recDataSize);
                        }
                        NetworkAnalysis.getInstance().commitFlow(new FlowStat(NetworkTask.this.f_refer, rs));
                        NetworkStat.getNetworkStat().put(NetworkTask.this.rc.config.getUrlString(), NetworkTask.this.rc.statisticData);
                        StatisticReqTimes.getIntance().updateReqTimes(request.getHttpUrl(), System.currentTimeMillis());
                    }
                }
            });
        }
    }
}
