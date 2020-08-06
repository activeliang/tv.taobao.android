package anet.channel.strategy.dispatch;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.statist.AmdcStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import mtopsdk.common.util.SymbolExpUtil;

class DispatchCore {
    static final String CHECK_SIGN_FAIL = "-1003";
    static final String EMPTY_SIGN_ERROR = "-1001";
    static final int MAX_RETRY_TIMES = 3;
    static final int NO_RETRY = 2;
    static final String READ_ANSWER_ERROR = "-1002";
    static final String REQUEST_EXCEPTION = "-1000";
    static final String RESOLVE_ANSWER_FAIL = "-1004";
    static final int RETRY_NORMAL = 1;
    static final int SUCCESS = 0;
    static final String TAG = "awcn.DispatchCore";
    static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(DispatchConstants.getAmdcServerDomain(), sslSession);
        }
    };
    static Random random = new Random();
    static AtomicInteger seq = new AtomicInteger(0);

    DispatchCore() {
    }

    static List<IConnStrategy> prepareConnStrategy(String scheme) {
        List<IConnStrategy> connStrategyList = Collections.EMPTY_LIST;
        if (!NetworkStatusHelper.isProxy()) {
            connStrategyList = StrategyCenter.getInstance().getConnStrategyListByHost(DispatchConstants.getAmdcServerDomain());
            ListIterator<IConnStrategy> iterator = connStrategyList.listIterator();
            while (iterator.hasNext()) {
                if (!iterator.next().getProtocol().protocol.equalsIgnoreCase(scheme)) {
                    iterator.remove();
                }
            }
        }
        return connStrategyList;
    }

    public static void sendRequest(Map params) {
        String urlString;
        boolean z;
        if (params == null) {
            ALog.e(TAG, "amdc request's parameter invalid!", (String) null, new Object[0]);
            return;
        }
        String scheme = StrategyCenter.getInstance().getSchemeByHost(DispatchConstants.getAmdcServerDomain(), "http");
        List<IConnStrategy> connStrategyList = prepareConnStrategy(scheme);
        int retry = 0;
        while (retry < 3) {
            Map map = new HashMap(params);
            IConnStrategy connStrategy = null;
            if (retry != 2) {
                if (!connStrategyList.isEmpty()) {
                    connStrategy = connStrategyList.remove(0);
                }
                if (connStrategy != null) {
                    urlString = buildRequestUrl(scheme, connStrategy.getIp(), connStrategy.getPort(), map, retry);
                } else {
                    urlString = buildRequestUrl(scheme, (String) null, 0, map, retry);
                }
            } else {
                String[] ips = DispatchConstants.getAmdcServerFixIp();
                if (ips == null || ips.length <= 0) {
                    urlString = buildRequestUrl(scheme, (String) null, 0, map, retry);
                } else {
                    urlString = buildRequestUrl(scheme, ips[random.nextInt(ips.length)], 0, map, retry);
                }
            }
            int status = sendOneNetworkRequest(urlString, map, retry);
            if (connStrategy != null) {
                ConnEvent connEvent = new ConnEvent();
                if (status == 0) {
                    z = true;
                } else {
                    z = false;
                }
                connEvent.isSuccess = z;
                StrategyCenter.getInstance().notifyConnEvent(DispatchConstants.getAmdcServerDomain(), connStrategy, connEvent);
            }
            if (status != 0 && status != 2) {
                retry++;
            } else {
                return;
            }
        }
    }

    private static String buildRequestUrl(String scheme, String ip, int port, Map<String, String> params, int retry) {
        StringBuilder url = new StringBuilder(64);
        if (retry == 2 && "https".equalsIgnoreCase(scheme) && random.nextBoolean()) {
            scheme = "http";
        }
        url.append(scheme);
        url.append(HttpConstant.SCHEME_SPLIT);
        if (ip != null) {
            if (port == 0) {
                port = "https".equalsIgnoreCase(scheme) ? 443 : 80;
            }
            url.append(ip).append(SymbolExpUtil.SYMBOL_COLON).append(port);
        } else {
            url.append(DispatchConstants.getAmdcServerDomain());
        }
        url.append(DispatchConstants.serverPath);
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("appkey", params.remove("appkey"));
        queryParams.put("v", params.remove("v"));
        queryParams.put("deviceId", params.remove("deviceId"));
        queryParams.put("platform", params.remove("platform"));
        url.append('?');
        url.append(Utils.encodeQueryParams(queryParams, "utf-8"));
        return url.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r28v31, resolved type: java.lang.Object[]} */
    /* JADX WARNING: type inference failed for: r26v12, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x047d A[Catch:{ all -> 0x04d1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x04ae A[SYNTHETIC, Splitter:B:125:0x04ae] */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x04d4 A[SYNTHETIC, Splitter:B:131:0x04d4] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int sendOneNetworkRequest(java.lang.String r31, java.util.Map r32, int r33) {
        /*
            java.lang.StringBuilder r26 = new java.lang.StringBuilder
            r26.<init>()
            java.lang.String r27 = "AMDC"
            java.lang.StringBuilder r26 = r26.append(r27)
            java.util.concurrent.atomic.AtomicInteger r27 = seq
            int r27 = r27.incrementAndGet()
            java.lang.String r27 = java.lang.String.valueOf(r27)
            java.lang.StringBuilder r26 = r26.append(r27)
            java.lang.String r23 = r26.toString()
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "send amdc request"
            r28 = 2
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r28 = r0
            r29 = 0
            java.lang.String r30 = "url"
            r28[r29] = r30
            r29 = 1
            r28[r29] = r31
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.i(r0, r1, r2, r3)
            java.lang.String r26 = "Env"
            r0 = r32
            r1 = r26
            java.lang.Object r13 = r0.remove(r1)
            anet.channel.entity.ENV r13 = (anet.channel.entity.ENV) r13
            r24 = 0
            r11 = 0
            java.net.URL r25 = new java.net.URL     // Catch:{ Throwable -> 0x0472 }
            r0 = r25
            r1 = r31
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0472 }
            java.net.URLConnection r26 = r25.openConnection()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r11 = r0
            r26 = 20000(0x4e20, float:2.8026E-41)
            r0 = r26
            r11.setConnectTimeout(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 20000(0x4e20, float:2.8026E-41)
            r0 = r26
            r11.setReadTimeout(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "POST"
            r0 = r26
            r11.setRequestMethod(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 1
            r0 = r26
            r11.setDoOutput(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 1
            r0 = r26
            r11.setDoInput(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "Connection"
            java.lang.String r27 = "close"
            r0 = r26
            r1 = r27
            r11.addRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "Accept-Encoding"
            java.lang.String r27 = "gzip"
            r0 = r26
            r1 = r27
            r11.addRequestProperty(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 0
            r0 = r26
            r11.setInstanceFollowRedirects(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = r25.getProtocol()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r27 = "https"
            boolean r26 = r26.equals(r27)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x00c0
            r0 = r11
            javax.net.ssl.HttpsURLConnection r0 = (javax.net.ssl.HttpsURLConnection) r0     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = r0
            javax.net.ssl.HostnameVerifier r27 = hostnameVerifier     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26.setHostnameVerifier(r27)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
        L_0x00c0:
            java.io.OutputStream r19 = r11.getOutputStream()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "utf-8"
            r0 = r32
            r1 = r26
            java.lang.String r26 = anet.channel.strategy.utils.Utils.encodeQueryParams(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            byte[] r8 = r26.getBytes()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r19
            r0.write(r8)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            int r20 = r11.getResponseCode()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.util.Map r16 = r11.getHeaderFields()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 1
            boolean r26 = anet.channel.util.ALog.isPrintLog(r26)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x0121
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r27.<init>()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r28 = "amdc response. code: "
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r27
            r1 = r20
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r27 = r27.toString()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = 2
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = r0
            r29 = 0
            java.lang.String r30 = "\nheaders"
            r28[r29] = r30     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r29 = 1
            r28[r29] = r16     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.d(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
        L_0x0121:
            r26 = 200(0xc8, float:2.8E-43)
            r0 = r20
            r1 = r26
            if (r0 == r1) goto L_0x0177
            r26 = 302(0x12e, float:4.23E-43)
            r0 = r20
            r1 = r26
            if (r0 == r1) goto L_0x0139
            r26 = 307(0x133, float:4.3E-43)
            r0 = r20
            r1 = r26
            if (r0 != r1) goto L_0x0157
        L_0x0139:
            r21 = 2
        L_0x013b:
            java.lang.String r26 = java.lang.String.valueOf(r20)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r27 = "response code not 200"
            r0 = r26
            r1 = r27
            r2 = r25
            r3 = r33
            r4 = r21
            commitStatistic(r0, r1, r2, r3, r4)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r11 == 0) goto L_0x0154
            r11.disconnect()     // Catch:{ Exception -> 0x015a }
        L_0x0154:
            r24 = r25
        L_0x0156:
            return r21
        L_0x0157:
            r21 = 1
            goto L_0x013b
        L_0x015a:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x0154
        L_0x0177:
            java.lang.String r26 = "x-am-code"
            r0 = r16
            r1 = r26
            java.lang.String r10 = anet.channel.strategy.utils.Utils.getHeaderFieldByKey(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "1000"
            r0 = r26
            boolean r26 = r0.equals(r10)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 != 0) goto L_0x01ee
            java.lang.String r26 = "1007"
            r0 = r26
            boolean r26 = r0.equals(r10)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 != 0) goto L_0x01a3
            java.lang.String r26 = "1008"
            r0 = r26
            boolean r26 = r0.equals(r10)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x01ce
        L_0x01a3:
            r21 = 2
        L_0x01a5:
            java.lang.StringBuilder r26 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26.<init>()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r27 = "return code: "
            java.lang.StringBuilder r26 = r26.append(r27)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            java.lang.StringBuilder r26 = r0.append(r10)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = r26.toString()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            r1 = r25
            r2 = r33
            r3 = r21
            commitStatistic(r10, r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r11 == 0) goto L_0x01cb
            r11.disconnect()     // Catch:{ Exception -> 0x01d1 }
        L_0x01cb:
            r24 = r25
            goto L_0x0156
        L_0x01ce:
            r21 = 1
            goto L_0x01a5
        L_0x01d1:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x01cb
        L_0x01ee:
            java.lang.String r26 = "x-am-sign"
            r0 = r16
            r1 = r26
            java.lang.String r26 = anet.channel.strategy.utils.Utils.getHeaderFieldByKey(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r22 = r26.trim()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            boolean r26 = android.text.TextUtils.isEmpty(r22)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x0240
            java.lang.String r26 = "-1001"
            java.lang.String r27 = "response sign is empty"
            r28 = 1
            r0 = r26
            r1 = r27
            r2 = r25
            r3 = r33
            r4 = r28
            commitStatistic(r0, r1, r2, r3, r4)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r21 = 1
            if (r11 == 0) goto L_0x021f
            r11.disconnect()     // Catch:{ Exception -> 0x0223 }
        L_0x021f:
            r24 = r25
            goto L_0x0156
        L_0x0223:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x021f
        L_0x0240:
            anet.channel.strategy.utils.ByteCounterInputStream r7 = new anet.channel.strategy.utils.ByteCounterInputStream     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.io.InputStream r26 = r11.getInputStream()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            r7.<init>(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "gzip"
            java.lang.String r27 = "Content-Encoding"
            r0 = r16
            r1 = r27
            java.lang.String r27 = anet.channel.strategy.utils.Utils.getHeaderFieldByKey(r0, r1)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            boolean r26 = r26.equalsIgnoreCase(r27)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            java.lang.String r6 = readAnswer(r7, r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = 1
            boolean r26 = anet.channel.util.ALog.isPrintLog(r26)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x028f
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "amdc response body"
            r28 = 2
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = r0
            r29 = 0
            java.lang.String r30 = "\nbody"
            r28[r29] = r30     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r29 = 1
            r28[r29] = r6     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.d(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
        L_0x028f:
            int r0 = r8.length     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = r0
            r0 = r26
            long r0 = (long) r0     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26 = r0
            long r28 = r7.getReadByteCount()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r31
            r1 = r26
            r3 = r28
            commitFlow(r0, r1, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            boolean r26 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 == 0) goto L_0x02e7
            java.lang.String r26 = "-1002"
            java.lang.String r27 = "read answer error"
            r28 = 1
            r0 = r26
            r1 = r27
            r2 = r25
            r3 = r33
            r4 = r28
            commitStatistic(r0, r1, r2, r3, r4)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r21 = 1
            if (r11 == 0) goto L_0x02c6
            r11.disconnect()     // Catch:{ Exception -> 0x02ca }
        L_0x02c6:
            r24 = r25
            goto L_0x0156
        L_0x02ca:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x02c6
        L_0x02e7:
            r9 = 0
            anet.channel.strategy.dispatch.IAmdcSign r17 = anet.channel.strategy.dispatch.AmdcRuntimeInfo.getSign()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r17 == 0) goto L_0x02f4
            r0 = r17
            java.lang.String r9 = r0.sign(r6)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
        L_0x02f4:
            r0 = r22
            boolean r26 = r9.equalsIgnoreCase(r0)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            if (r26 != 0) goto L_0x0368
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "check ret sign failed"
            r28 = 4
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = r0
            r29 = 0
            java.lang.String r30 = "retSign"
            r28[r29] = r30     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r29 = 1
            r28[r29] = r22     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r29 = 2
            java.lang.String r30 = "checkSign"
            r28[r29] = r30     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r29 = 3
            r28[r29] = r9     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.e(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "-1003"
            java.lang.String r27 = "check sign failed"
            r28 = 1
            r0 = r26
            r1 = r27
            r2 = r25
            r3 = r33
            r4 = r28
            commitStatistic(r0, r1, r2, r3, r4)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r21 = 1
            if (r11 == 0) goto L_0x0347
            r11.disconnect()     // Catch:{ Exception -> 0x034b }
        L_0x0347:
            r24 = r25
            goto L_0x0156
        L_0x034b:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x0347
        L_0x0368:
            org.json.JSONTokener r26 = new org.json.JSONTokener     // Catch:{ JSONException -> 0x03ed }
            r0 = r26
            r0.<init>(r6)     // Catch:{ JSONException -> 0x03ed }
            java.lang.Object r18 = r26.nextValue()     // Catch:{ JSONException -> 0x03ed }
            org.json.JSONObject r18 = (org.json.JSONObject) r18     // Catch:{ JSONException -> 0x03ed }
            anet.channel.entity.ENV r26 = anet.channel.GlobalAppRuntimeInfo.getEnv()     // Catch:{ JSONException -> 0x03ed }
            r0 = r26
            if (r0 == r13) goto L_0x03be
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "env change, do not notify result"
            r28 = 0
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ JSONException -> 0x03ed }
            r28 = r0
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.w(r0, r1, r2, r3)     // Catch:{ JSONException -> 0x03ed }
            r21 = 0
            if (r11 == 0) goto L_0x039d
            r11.disconnect()     // Catch:{ Exception -> 0x03a1 }
        L_0x039d:
            r24 = r25
            goto L_0x0156
        L_0x03a1:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x039d
        L_0x03be:
            anet.channel.strategy.dispatch.HttpDispatcher r26 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()     // Catch:{ JSONException -> 0x03ed }
            anet.channel.strategy.dispatch.DispatchEvent r27 = new anet.channel.strategy.dispatch.DispatchEvent     // Catch:{ JSONException -> 0x03ed }
            r28 = 1
            r0 = r27
            r1 = r28
            r2 = r18
            r0.<init>(r1, r2)     // Catch:{ JSONException -> 0x03ed }
            r26.fireEvent(r27)     // Catch:{ JSONException -> 0x03ed }
            java.lang.String r26 = "request success"
            r27 = 0
            r0 = r26
            r1 = r25
            r2 = r33
            r3 = r27
            commitStatistic(r10, r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r21 = 0
            if (r11 == 0) goto L_0x03e9
            r11.disconnect()     // Catch:{ Exception -> 0x0454 }
        L_0x03e9:
            r24 = r25
            goto L_0x0156
        L_0x03ed:
            r12 = move-exception
            anet.channel.strategy.dispatch.HttpDispatcher r26 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            anet.channel.strategy.dispatch.DispatchEvent r27 = new anet.channel.strategy.dispatch.DispatchEvent     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = 0
            r29 = 0
            r27.<init>(r28, r29)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r26.fireEvent(r27)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "resolve amdc anser failed"
            r28 = 0
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r28 = r0
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.e(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            java.lang.String r26 = "-1004"
            java.lang.String r27 = "resolve answer failed"
            r28 = 1
            r0 = r26
            r1 = r27
            r2 = r25
            r3 = r33
            r4 = r28
            commitStatistic(r0, r1, r2, r3, r4)     // Catch:{ Throwable -> 0x04f9, all -> 0x04f5 }
            r21 = 1
            if (r11 == 0) goto L_0x0433
            r11.disconnect()     // Catch:{ Exception -> 0x0437 }
        L_0x0433:
            r24 = r25
            goto L_0x0156
        L_0x0437:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x0433
        L_0x0454:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x03e9
        L_0x0472:
            r15 = move-exception
        L_0x0473:
            java.lang.String r14 = r15.getMessage()     // Catch:{ all -> 0x04d1 }
            boolean r26 = android.text.TextUtils.isEmpty(r14)     // Catch:{ all -> 0x04d1 }
            if (r26 == 0) goto L_0x0481
            java.lang.String r14 = r15.toString()     // Catch:{ all -> 0x04d1 }
        L_0x0481:
            java.lang.String r26 = "-1000"
            r27 = 1
            r0 = r26
            r1 = r24
            r2 = r33
            r3 = r27
            commitStatistic(r0, r14, r1, r2, r3)     // Catch:{ all -> 0x04d1 }
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "amdc request fail"
            r28 = 0
            r0 = r28
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x04d1 }
            r28 = r0
            r0 = r26
            r1 = r27
            r2 = r23
            r3 = r28
            anet.channel.util.ALog.e(r0, r1, r2, r15, r3)     // Catch:{ all -> 0x04d1 }
            r21 = 1
            if (r11 == 0) goto L_0x0156
            r11.disconnect()     // Catch:{ Exception -> 0x04b3 }
            goto L_0x0156
        L_0x04b3:
            r12 = move-exception
            java.lang.String r26 = "awcn.DispatchCore"
            java.lang.String r27 = "http disconnect failed"
            r28 = 0
            r29 = 0
            r0 = r29
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r29 = r0
            r0 = r26
            r1 = r27
            r2 = r28
            r3 = r29
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x0156
        L_0x04d1:
            r26 = move-exception
        L_0x04d2:
            if (r11 == 0) goto L_0x04d7
            r11.disconnect()     // Catch:{ Exception -> 0x04d8 }
        L_0x04d7:
            throw r26
        L_0x04d8:
            r12 = move-exception
            java.lang.String r27 = "awcn.DispatchCore"
            java.lang.String r28 = "http disconnect failed"
            r29 = 0
            r30 = 0
            r0 = r30
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r30 = r0
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r30
            anet.channel.util.ALog.e(r0, r1, r2, r12, r3)
            goto L_0x04d7
        L_0x04f5:
            r26 = move-exception
            r24 = r25
            goto L_0x04d2
        L_0x04f9:
            r15 = move-exception
            r24 = r25
            goto L_0x0473
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.dispatch.DispatchCore.sendOneNetworkRequest(java.lang.String, java.util.Map, int):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b A[SYNTHETIC, Splitter:B:17:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0055 A[SYNTHETIC, Splitter:B:27:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String readAnswer(java.io.InputStream r11, boolean r12) {
        /*
            r6 = 0
            r7 = 1024(0x400, float:1.435E-42)
            java.io.BufferedInputStream r4 = new java.io.BufferedInputStream
            r4.<init>(r11)
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>(r7)
            if (r12 == 0) goto L_0x0065
            java.util.zip.GZIPInputStream r5 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x002b }
            r5.<init>(r4)     // Catch:{ IOException -> 0x002b }
        L_0x0014:
            android.util.Base64InputStream r4 = new android.util.Base64InputStream     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            r7 = 0
            r4.<init>(r5, r7)     // Catch:{ IOException -> 0x0062, all -> 0x005f }
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r7]     // Catch:{ IOException -> 0x002b }
            r2 = 0
        L_0x001f:
            int r2 = r4.read(r1)     // Catch:{ IOException -> 0x002b }
            r7 = -1
            if (r2 == r7) goto L_0x003f
            r7 = 0
            r0.write(r1, r7, r2)     // Catch:{ IOException -> 0x002b }
            goto L_0x001f
        L_0x002b:
            r3 = move-exception
        L_0x002c:
            java.lang.String r7 = "awcn.DispatchCore"
            java.lang.String r8 = ""
            r9 = 0
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x0052 }
            anet.channel.util.ALog.e(r7, r8, r9, r3, r10)     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x003e
            r4.close()     // Catch:{ IOException -> 0x005b }
        L_0x003e:
            return r6
        L_0x003f:
            java.lang.String r7 = new java.lang.String     // Catch:{ IOException -> 0x002b }
            byte[] r8 = r0.toByteArray()     // Catch:{ IOException -> 0x002b }
            java.lang.String r9 = "utf-8"
            r7.<init>(r8, r9)     // Catch:{ IOException -> 0x002b }
            if (r4 == 0) goto L_0x0050
            r4.close()     // Catch:{ IOException -> 0x0059 }
        L_0x0050:
            r6 = r7
            goto L_0x003e
        L_0x0052:
            r6 = move-exception
        L_0x0053:
            if (r4 == 0) goto L_0x0058
            r4.close()     // Catch:{ IOException -> 0x005d }
        L_0x0058:
            throw r6
        L_0x0059:
            r6 = move-exception
            goto L_0x0050
        L_0x005b:
            r7 = move-exception
            goto L_0x003e
        L_0x005d:
            r7 = move-exception
            goto L_0x0058
        L_0x005f:
            r6 = move-exception
            r4 = r5
            goto L_0x0053
        L_0x0062:
            r3 = move-exception
            r4 = r5
            goto L_0x002c
        L_0x0065:
            r5 = r4
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.dispatch.DispatchCore.readAnswer(java.io.InputStream, boolean):java.lang.String");
    }

    static void commitStatistic(String errorCode, String errorMsg, URL url, int retryTimes, int ret) {
        if ((ret != 1 || retryTimes == 2) && GlobalAppRuntimeInfo.isTargetProcess()) {
            try {
                AmdcStatistic as = new AmdcStatistic();
                as.errorCode = errorCode;
                as.errorMsg = errorMsg;
                if (url != null) {
                    as.host = url.getHost();
                    as.url = url.toString();
                }
                as.retryTimes = retryTimes;
                AppMonitor.getInstance().commitStat(as);
            } catch (Exception e) {
            }
        }
    }

    static void commitFlow(String url, long upstream, long donwstream) {
        try {
            FlowStat flow = new FlowStat();
            flow.refer = "amdc";
            flow.protocoltype = "http";
            flow.req_identifier = url;
            flow.upstream = upstream;
            flow.downstream = donwstream;
            NetworkAnalysis.getInstance().commitFlow(flow);
        } catch (Exception e) {
            ALog.e(TAG, "commit flow info failed!", (String) null, e, new Object[0]);
        }
    }
}
