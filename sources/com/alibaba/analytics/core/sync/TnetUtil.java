package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.ByteUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.WuaHelper;
import com.alibaba.analytics.utils.ZipDictUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.android.spdy.SessionCb;
import org.android.spdy.SessionExtraCb;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdySession;
import org.android.spdy.SuperviseConnectInfo;

public class TnetUtil {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int ENVIRONMENT_BETA = 1;
    private static final int ENVIRONMENT_DAILY = 3;
    private static final int ENVIRONMENT_ONLINE = 0;
    private static final int ENVIRONMENT_PRE = 2;
    private static final int GCRY_CIPHER_AES128 = 16;
    private static final int HEAD_LENGTH = 8;
    private static final Object Lock_Event = new Object();
    /* access modifiers changed from: private */
    public static final Object Lock_Object = new Object();
    private static final int PROTOCOL_MAX_LENGTH = 131072;
    private static final String SSL_TIKET_KEY2 = "accs_ssl_key2_";
    private static final String SecurityGuard_HOST = "adashx.m.taobao.com";
    private static final int TNET_ENVIRONMENT = 0;
    private static final int WAIT_TIMEOUT = 60000;
    private static boolean bFirstSpdySession = true;
    /* access modifiers changed from: private */
    public static int errorCode = -1;
    private static boolean isGetWuaBeforeSpdySession = false;
    public static int mErrorCode = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    /* access modifiers changed from: private */
    public static ByteArrayOutputStream mResponseCache = null;
    /* access modifiers changed from: private */
    public static long mResponseLen = 0;
    /* access modifiers changed from: private */
    public static long mResponseReceiveLen = 0;
    private static byte[] protocolBytes = null;
    private static int sendBytes = 0;
    /* access modifiers changed from: private */
    public static SpdySession spdySessionUT = null;

    static /* synthetic */ long access$614(long x0) {
        long j = mResponseReceiveLen + x0;
        mResponseReceiveLen = j;
        return j;
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    static com.alibaba.analytics.core.sync.BizResponse sendRequest(byte[] r24) {
        /*
            com.alibaba.analytics.utils.Logger.d()
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r5 = mMonitor
            int r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_REQUEST_SEND
            r7 = 0
            r20 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r8 = java.lang.Double.valueOf(r20)
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r6, r7, r8)
            r5.onEvent(r6)
            com.alibaba.analytics.core.sync.BizResponse r11 = new com.alibaba.analytics.core.sync.BizResponse
            r11.<init>()
            java.lang.Object r6 = Lock_Object
            monitor-enter(r6)
            protocolBytes = r24     // Catch:{ all -> 0x01dd }
            r5 = 0
            sendBytes = r5     // Catch:{ all -> 0x01dd }
            monitor-exit(r6)     // Catch:{ all -> 0x01dd }
            java.lang.Object r20 = Lock_Event
            monitor-enter(r20)
            java.io.ByteArrayOutputStream r5 = mResponseCache     // Catch:{ all -> 0x01ff }
            if (r5 == 0) goto L_0x002f
            java.io.ByteArrayOutputStream r5 = mResponseCache     // Catch:{ IOException -> 0x022e }
            r5.close()     // Catch:{ IOException -> 0x022e }
        L_0x002f:
            r5 = 0
            mResponseCache = r5     // Catch:{ all -> 0x01ff }
            r6 = 0
            mResponseReceiveLen = r6     // Catch:{ all -> 0x01ff }
            r6 = 0
            mResponseLen = r6     // Catch:{ all -> 0x01ff }
            long r18 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01ff }
            r5 = -1
            errorCode = r5     // Catch:{ all -> 0x01ff }
            org.android.spdy.SpdySession r5 = spdySessionUT     // Catch:{ Exception -> 0x01e7 }
            if (r5 != 0) goto L_0x0205
            boolean r5 = bFirstSpdySession     // Catch:{ Exception -> 0x01e7 }
            if (r5 != 0) goto L_0x0053
            com.alibaba.analytics.core.Variables r5 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r5 = r5.isGzipUpload()     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x0205
        L_0x0053:
            com.alibaba.analytics.core.Variables r5 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r5 = r5.isSelfMonitorTurnOn()     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x006f
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r5 = mMonitor     // Catch:{ Exception -> 0x01e7 }
            int r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_CREATE_SESSION     // Catch:{ Exception -> 0x01e7 }
            r7 = 0
            r22 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r8 = java.lang.Double.valueOf(r22)     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r6, r7, r8)     // Catch:{ Exception -> 0x01e7 }
            r5.onEvent(r6)     // Catch:{ Exception -> 0x01e7 }
        L_0x006f:
            com.alibaba.analytics.core.Variables r5 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x01e7 }
            android.content.Context r12 = r5.getContext()     // Catch:{ Exception -> 0x01e7 }
            org.android.spdy.SpdyVersion r5 = org.android.spdy.SpdyVersion.SPDY3     // Catch:{ Exception -> 0x01e7 }
            org.android.spdy.SpdySessionKind r6 = org.android.spdy.SpdySessionKind.NONE_SESSION     // Catch:{ Exception -> 0x01e7 }
            org.android.spdy.SpdyAgent r15 = org.android.spdy.SpdyAgent.getInstance(r12, r5, r6)     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.core.sync.TnetSecuritySDK r5 = com.alibaba.analytics.core.sync.TnetSecuritySDK.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r5 = r5.getInitSecurityCheck()     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x0091
            com.alibaba.analytics.core.sync.TnetUtil$1 r5 = new com.alibaba.analytics.core.sync.TnetUtil$1     // Catch:{ Exception -> 0x01e7 }
            r5.<init>()     // Catch:{ Exception -> 0x01e7 }
            r15.setAccsSslCallback(r5)     // Catch:{ Exception -> 0x01e7 }
        L_0x0091:
            com.alibaba.analytics.core.sync.TnetUtil$UTSessionCb r9 = new com.alibaba.analytics.core.sync.TnetUtil$UTSessionCb     // Catch:{ Exception -> 0x01e7 }
            r5 = 0
            r9.<init>()     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.core.sync.TnetHostPortMgr r5 = com.alibaba.analytics.core.sync.TnetHostPortMgr.getInstance()     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r14 = r5.getEntity()     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r3 = r14.getHost()     // Catch:{ Exception -> 0x01e7 }
            int r4 = r14.getPort()     // Catch:{ Exception -> 0x01e7 }
            org.android.spdy.SessionInfo r2 = new org.android.spdy.SessionInfo     // Catch:{ Exception -> 0x01e7 }
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r10 = 4240(0x1090, float:5.942E-42)
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.core.sync.TnetSecuritySDK r5 = com.alibaba.analytics.core.sync.TnetSecuritySDK.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r5 = r5.getInitSecurityCheck()     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x01e0
            r5 = 8
            r2.setPubKeySeqNum(r5)     // Catch:{ Exception -> 0x01e7 }
        L_0x00c1:
            r5 = 0
            r6 = 6
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x01e7 }
            r7 = 0
            java.lang.String r8 = "host"
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            r7 = 1
            r6[r7] = r3     // Catch:{ Exception -> 0x01e7 }
            r7 = 2
            java.lang.String r8 = "port"
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            r7 = 3
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x01e7 }
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            r7 = 4
            java.lang.String r8 = "TNET_ENVIRONMENT"
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            r7 = 5
            r8 = 0
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x01e7 }
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Exception -> 0x01e7 }
            r5 = 10000(0x2710, float:1.4013E-41)
            r2.setConnectionTimeoutMs(r5)     // Catch:{ Exception -> 0x01e7 }
            java.lang.Object r6 = Lock_Object     // Catch:{ Exception -> 0x01e7 }
            monitor-enter(r6)     // Catch:{ Exception -> 0x01e7 }
            org.android.spdy.SpdySession r5 = r15.createSession(r2)     // Catch:{ all -> 0x0202 }
            spdySessionUT = r5     // Catch:{ all -> 0x0202 }
            boolean r5 = isGetWuaBeforeSpdySession     // Catch:{ all -> 0x0202 }
            if (r5 != 0) goto L_0x012a
            java.lang.String r5 = com.alibaba.analytics.utils.WuaHelper.getMiniWua()     // Catch:{ all -> 0x0202 }
            com.alibaba.analytics.core.sync.BizRequest.mMiniWua = r5     // Catch:{ all -> 0x0202 }
            java.lang.String r5 = ""
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0202 }
            r8 = 0
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0202 }
            r10.<init>()     // Catch:{ all -> 0x0202 }
            java.lang.String r21 = "GetWua by createSession:"
            r0 = r21
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ all -> 0x0202 }
            java.lang.String r21 = com.alibaba.analytics.core.sync.BizRequest.mMiniWua     // Catch:{ all -> 0x0202 }
            r0 = r21
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ all -> 0x0202 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0202 }
            r7[r8] = r10     // Catch:{ all -> 0x0202 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r7)     // Catch:{ all -> 0x0202 }
        L_0x012a:
            r5 = 0
            isGetWuaBeforeSpdySession = r5     // Catch:{ all -> 0x0202 }
            monitor-exit(r6)     // Catch:{ all -> 0x0202 }
            java.lang.String r5 = ""
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x01e7 }
            r7 = 0
            java.lang.String r8 = "createSession"
            r6[r7] = r8     // Catch:{ Exception -> 0x01e7 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Exception -> 0x01e7 }
            java.lang.Object r5 = Lock_Event     // Catch:{ Exception -> 0x01e7 }
            r6 = 60000(0xea60, double:2.9644E-319)
            r5.wait(r6)     // Catch:{ Exception -> 0x01e7 }
        L_0x0145:
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01ff }
            long r16 = r6 - r18
            r6 = 60000(0xea60, double:2.9644E-319)
            int r5 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1))
            if (r5 < 0) goto L_0x017e
            com.alibaba.analytics.core.Variables r5 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ all -> 0x01ff }
            boolean r5 = r5.isSelfMonitorTurnOn()     // Catch:{ all -> 0x01ff }
            if (r5 == 0) goto L_0x016e
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r5 = mMonitor     // Catch:{ all -> 0x01ff }
            int r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.TNET_REQUEST_TIMEOUT     // Catch:{ all -> 0x01ff }
            r7 = 0
            r22 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r8 = java.lang.Double.valueOf(r22)     // Catch:{ all -> 0x01ff }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r6 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r6, r7, r8)     // Catch:{ all -> 0x01ff }
            r5.onEvent(r6)     // Catch:{ all -> 0x01ff }
        L_0x016e:
            closeSession()     // Catch:{ all -> 0x01ff }
            r5 = 0
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x01ff }
            r7 = 0
            java.lang.String r8 = "WAIT_TIMEOUT"
            r6[r7] = r8     // Catch:{ all -> 0x01ff }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ all -> 0x01ff }
        L_0x017e:
            monitor-exit(r20)     // Catch:{ all -> 0x01ff }
            int r5 = sendBytes
            long r6 = (long) r5
            com.alibaba.analytics.core.sync.BizRequest.recordTraffic(r6)
            java.lang.Object r6 = Lock_Object
            monitor-enter(r6)
            r5 = 0
            protocolBytes = r5     // Catch:{ all -> 0x022b }
            r5 = 0
            sendBytes = r5     // Catch:{ all -> 0x022b }
            monitor-exit(r6)     // Catch:{ all -> 0x022b }
            int r5 = errorCode
            r11.errCode = r5
            r0 = r16
            r11.rt = r0
            java.lang.String r5 = com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData
            r11.data = r5
            r5 = 0
            com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData = r5
            int r5 = errorCode
            mErrorCode = r5
            java.lang.String r5 = "PostData"
            r6 = 6
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r7 = 0
            java.lang.String r8 = "isSuccess"
            r6[r7] = r8
            r7 = 1
            boolean r8 = r11.isSuccess()
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r8)
            r6[r7] = r8
            r7 = 2
            java.lang.String r8 = "errCode"
            r6[r7] = r8
            r7 = 3
            int r8 = r11.errCode
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r6[r7] = r8
            r7 = 4
            java.lang.String r8 = "rt"
            r6[r7] = r8
            r7 = 5
            long r0 = r11.rt
            r20 = r0
            java.lang.Long r8 = java.lang.Long.valueOf(r20)
            r6[r7] = r8
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)
            return r11
        L_0x01dd:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x01dd }
            throw r5
        L_0x01e0:
            r5 = 9
            r2.setPubKeySeqNum(r5)     // Catch:{ Exception -> 0x01e7 }
            goto L_0x00c1
        L_0x01e7:
            r13 = move-exception
            closeSession()     // Catch:{ all -> 0x01ff }
            java.lang.String r5 = ""
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x01ff }
            r7 = 0
            java.lang.String r8 = "CreateSession Exception"
            r6[r7] = r8     // Catch:{ all -> 0x01ff }
            r7 = 1
            r6[r7] = r13     // Catch:{ all -> 0x01ff }
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ all -> 0x01ff }
            goto L_0x0145
        L_0x01ff:
            r5 = move-exception
            monitor-exit(r20)     // Catch:{ all -> 0x01ff }
            throw r5
        L_0x0202:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0202 }
            throw r5     // Catch:{ Exception -> 0x01e7 }
        L_0x0205:
            org.android.spdy.SpdySession r5 = spdySessionUT     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x0226
            boolean r5 = bFirstSpdySession     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x0217
            com.alibaba.analytics.core.Variables r5 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r5 = r5.isGzipUpload()     // Catch:{ Exception -> 0x01e7 }
            if (r5 == 0) goto L_0x0226
        L_0x0217:
            org.android.spdy.SpdySession r5 = spdySessionUT     // Catch:{ Exception -> 0x01e7 }
            sendCustomControlFrame(r5)     // Catch:{ Exception -> 0x01e7 }
            java.lang.Object r5 = Lock_Event     // Catch:{ Exception -> 0x01e7 }
            r6 = 60000(0xea60, double:2.9644E-319)
            r5.wait(r6)     // Catch:{ Exception -> 0x01e7 }
            goto L_0x0145
        L_0x0226:
            closeSession()     // Catch:{ Exception -> 0x01e7 }
            goto L_0x0145
        L_0x022b:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x022b }
            throw r5
        L_0x022e:
            r5 = move-exception
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.TnetUtil.sendRequest(byte[]):com.alibaba.analytics.core.sync.BizResponse");
    }

    /* access modifiers changed from: private */
    public static void sendCustomControlFrame(SpdySession spdySession) {
        synchronized (Lock_Object) {
            while (spdySession == spdySessionUT && spdySessionUT != null && protocolBytes != null && protocolBytes.length > sendBytes) {
                try {
                    if (protocolBytes.length - sendBytes > 131072) {
                        spdySession.sendCustomControlFrame(-1, -1, -1, 131072, ByteUtils.subBytes(protocolBytes, sendBytes, 131072));
                        sendBytes += 131072;
                    } else {
                        int len = protocolBytes.length - sendBytes;
                        if (len > 0) {
                            spdySession.sendCustomControlFrame(-1, -1, -1, len, ByteUtils.subBytes(protocolBytes, sendBytes, len));
                            sendBytes += len;
                        }
                    }
                } catch (SpdyErrorException e) {
                    Logger.e("", "SpdyErrorException", e);
                    if (e.SpdyErrorGetCode() != -3848) {
                        errorCode = e.SpdyErrorGetCode();
                        closeSession();
                    }
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void closeSession() {
        Logger.d();
        synchronized (Lock_Object) {
            if (spdySessionUT != null) {
                spdySessionUT.closeSession();
            }
            spdySessionUT = null;
            BizRequest.closeOutputStream();
            ZipDictUtils.clear();
        }
        sendCallbackNotify();
    }

    /* access modifiers changed from: private */
    public static void sendCallbackNotify() {
        synchronized (Lock_Event) {
            Lock_Event.notifyAll();
        }
    }

    private static class UTSessionCb implements SessionCb, SessionExtraCb {
        private byte[] sslMeta;

        private UTSessionCb() {
        }

        public void spdySessionConnectCB(SpdySession session, SuperviseConnectInfo data) {
            if (session == TnetUtil.spdySessionUT) {
                TnetUtil.sendCustomControlFrame(session);
            }
        }

        public void spdyPingRecvCallback(SpdySession session, long unique_id, Object sessionUserData) {
        }

        public void spdyCustomControlFrameRecvCallback(SpdySession session, Object sessionUserData, int node, int type, int flags, int length, byte[] data) {
            if (session == TnetUtil.spdySessionUT) {
                if (TnetUtil.mResponseCache == null) {
                    ByteArrayOutputStream unused = TnetUtil.mResponseCache = new ByteArrayOutputStream(1024);
                    long unused2 = TnetUtil.mResponseLen = TnetUtil.getResponseBodyLen(data);
                }
                if (TnetUtil.mResponseLen != -1) {
                    try {
                        TnetUtil.mResponseCache.write(data);
                    } catch (IOException e) {
                    }
                    TnetUtil.access$614((long) data.length);
                    if (TnetUtil.mResponseLen == TnetUtil.mResponseReceiveLen - 8) {
                        try {
                            TnetUtil.mResponseCache.flush();
                        } catch (IOException e2) {
                        }
                        byte[] temp = TnetUtil.mResponseCache.toByteArray();
                        try {
                            TnetUtil.mResponseCache.close();
                        } catch (IOException e3) {
                        }
                        int unused3 = TnetUtil.errorCode = BizRequest.parseResult(temp);
                        if (TnetUtil.errorCode != 0) {
                            TnetUtil.closeSession();
                        }
                        TnetUtil.sendCallbackNotify();
                        return;
                    }
                    return;
                }
                int unused4 = TnetUtil.errorCode = -1;
                TnetUtil.closeSession();
                TnetUtil.sendCallbackNotify();
                return;
            }
            Logger.w("[spdyCustomControlFrameRecvCallback]", " session != spdySessionUT");
        }

        public void spdyCustomControlFrameFailCallback(SpdySession session, Object sessionUserData, int id, int error) {
        }

        public void spdySessionFailedError(SpdySession session, int error, Object sessionUserData) {
            if (Variables.getInstance().isSelfMonitorTurnOn()) {
                TnetUtil.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.TNET_REQUEST_ERROR, (String) null, Double.valueOf(1.0d)));
            }
            if (session == TnetUtil.spdySessionUT) {
                int unused = TnetUtil.errorCode = error;
                TnetUtil.closeSession();
            }
        }

        public void spdySessionCloseCallback(SpdySession session, Object userData, SuperviseConnectInfo connInfo, int error) {
            if (session == TnetUtil.spdySessionUT) {
                int unused = TnetUtil.errorCode = error;
                synchronized (TnetUtil.Lock_Object) {
                    SpdySession unused2 = TnetUtil.spdySessionUT = null;
                }
            }
        }

        public void bioPingRecvCallback(SpdySession session, int pingId) {
        }

        public byte[] getSSLMeta(SpdySession session) {
            if (!TnetSecuritySDK.getInstance().getInitSecurityCheck()) {
                return this.sslMeta;
            }
            byte[] temp = TnetSecuritySDK.getInstance().getByteArray("accs_ssl_key2_adashx.m.taobao.com");
            return temp != null ? temp : new byte[0];
        }

        public int putSSLMeta(SpdySession session, byte[] sslMeta2) {
            if (TnetSecuritySDK.getInstance().getInitSecurityCheck()) {
                return TnetUtil.securityGuardPutSslTicket2(sslMeta2);
            }
            this.sslMeta = sslMeta2;
            return 0;
        }

        public void spdySessionOnWritable(SpdySession spdySession, Object o, int i) {
            if (spdySession == TnetUtil.spdySessionUT) {
                TnetUtil.sendCustomControlFrame(spdySession);
            }
        }
    }

    /* access modifiers changed from: private */
    public static int securityGuardPutSslTicket2(byte[] value) {
        if (value == null) {
            return -1;
        }
        return TnetSecuritySDK.getInstance().putByteArray("accs_ssl_key2_adashx.m.taobao.com", value) == 0 ? -1 : 0;
    }

    /* access modifiers changed from: private */
    public static long getResponseBodyLen(byte[] data) {
        if (data == null || data.length < 12) {
            return -1;
        }
        return (long) ByteUtils.bytesToInt(data, 1, 3);
    }

    static void initTnetStream() {
        synchronized (Lock_Object) {
            if (spdySessionUT == null) {
                ZipDictUtils.clear();
                BizRequest.initOutputStream();
                bFirstSpdySession = true;
            } else {
                bFirstSpdySession = false;
            }
        }
    }

    static void refreshMiniWua() {
        if (spdySessionUT == null) {
            BizRequest.mMiniWua = WuaHelper.getMiniWua();
            isGetWuaBeforeSpdySession = true;
        }
    }
}
