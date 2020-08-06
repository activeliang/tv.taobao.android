package tv.danmaku.ijk.media.player;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LDNetTraceRoute {
    private static final String MATCH_PING_IP = "(?<=from ).*(?=: icmp_seq=1 ttl=)";
    private static final String MATCH_PING_TIME = "(?<=time=).*?ms";
    private static final String MATCH_TRACE_IP = "(?<=From )(?:[0-9]{1,3}\\.){3}[0-9]{1,3}";
    private static LDNetTraceRoute instance;
    private final String LOG_TAG = "LDNetTraceRoute";
    public boolean isCTrace = true;
    LDNetTraceRouteListener listener;

    public interface LDNetTraceRouteListener {
        void OnNetTraceFinished();

        void OnNetTraceUpdated(String str);
    }

    public native void startJNICTraceRoute(String str);

    private LDNetTraceRoute() {
    }

    public static LDNetTraceRoute getInstance() {
        if (instance == null) {
            instance = new LDNetTraceRoute();
        }
        return instance;
    }

    public void initListenter(LDNetTraceRouteListener listener2) {
        this.listener = listener2;
    }

    public void startTraceRoute(String host) {
        if (this.isCTrace) {
            try {
                startJNICTraceRoute(host);
            } catch (UnsatisfiedLinkError e) {
                e.printStackTrace();
                Log.i("LDNetTraceRoute", "调用java模拟traceRoute");
                execTrace(new TraceTask(host, 1));
            }
        } else {
            execTrace(new TraceTask(host, 1));
        }
    }

    public void resetInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public void printTraceInfo(String log) {
        this.listener.OnNetTraceUpdated(log);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065 A[SYNTHETIC, Splitter:B:21:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0074 A[SYNTHETIC, Splitter:B:29:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0080 A[SYNTHETIC, Splitter:B:35:0x0080] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x0060=Splitter:B:18:0x0060, B:26:0x006f=Splitter:B:26:0x006f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String execPing(tv.danmaku.ijk.media.player.LDNetTraceRoute.PingTask r10) {
        /*
            r9 = this;
            r2 = 0
            java.lang.String r5 = ""
            r3 = 0
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            r7.<init>()     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.String r8 = "ping -c 1 "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.String r8 = r10.getHost()     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.lang.Process r2 = r6.exec(r7)     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            java.io.InputStream r7 = r2.getInputStream()     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            r6.<init>(r7)     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            r4.<init>(r6)     // Catch:{ IOException -> 0x005f, InterruptedException -> 0x006e }
            r1 = 0
        L_0x0034:
            java.lang.String r1 = r4.readLine()     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            if (r1 == 0) goto L_0x004c
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            r6.<init>()     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            java.lang.StringBuilder r6 = r6.append(r5)     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            java.lang.StringBuilder r6 = r6.append(r1)     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            java.lang.String r5 = r6.toString()     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            goto L_0x0034
        L_0x004c:
            r4.close()     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            r2.waitFor()     // Catch:{ IOException -> 0x008f, InterruptedException -> 0x008c, all -> 0x0089 }
            if (r4 == 0) goto L_0x0057
            r4.close()     // Catch:{ Exception -> 0x005c }
        L_0x0057:
            r2.destroy()     // Catch:{ Exception -> 0x005c }
            r3 = r4
        L_0x005b:
            return r5
        L_0x005c:
            r6 = move-exception
            r3 = r4
            goto L_0x005b
        L_0x005f:
            r0 = move-exception
        L_0x0060:
            r0.printStackTrace()     // Catch:{ all -> 0x007d }
            if (r3 == 0) goto L_0x0068
            r3.close()     // Catch:{ Exception -> 0x006c }
        L_0x0068:
            r2.destroy()     // Catch:{ Exception -> 0x006c }
            goto L_0x005b
        L_0x006c:
            r6 = move-exception
            goto L_0x005b
        L_0x006e:
            r0 = move-exception
        L_0x006f:
            r0.printStackTrace()     // Catch:{ all -> 0x007d }
            if (r3 == 0) goto L_0x0077
            r3.close()     // Catch:{ Exception -> 0x007b }
        L_0x0077:
            r2.destroy()     // Catch:{ Exception -> 0x007b }
            goto L_0x005b
        L_0x007b:
            r6 = move-exception
            goto L_0x005b
        L_0x007d:
            r6 = move-exception
        L_0x007e:
            if (r3 == 0) goto L_0x0083
            r3.close()     // Catch:{ Exception -> 0x0087 }
        L_0x0083:
            r2.destroy()     // Catch:{ Exception -> 0x0087 }
        L_0x0086:
            throw r6
        L_0x0087:
            r7 = move-exception
            goto L_0x0086
        L_0x0089:
            r6 = move-exception
            r3 = r4
            goto L_0x007e
        L_0x008c:
            r0 = move-exception
            r3 = r4
            goto L_0x006f
        L_0x008f:
            r0 = move-exception
            r3 = r4
            goto L_0x0060
        */
        throw new UnsupportedOperationException("Method not decompiled: tv.danmaku.ijk.media.player.LDNetTraceRoute.execPing(tv.danmaku.ijk.media.player.LDNetTraceRoute$PingTask):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x015a A[SYNTHETIC, Splitter:B:36:0x015a] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01dd A[SYNTHETIC, Splitter:B:54:0x01dd] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0122=Splitter:B:22:0x0122, B:33:0x0155=Splitter:B:33:0x0155} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void execTrace(tv.danmaku.ijk.media.player.LDNetTraceRoute.TraceTask r24) {
        /*
            r23 = this;
            java.lang.String r21 = "(?<=From )(?:[0-9]{1,3}\\.){3}[0-9]{1,3}"
            java.util.regex.Pattern r12 = java.util.regex.Pattern.compile(r21)
            java.lang.String r21 = "(?<=from ).*(?=: icmp_seq=1 ttl=)"
            java.util.regex.Pattern r10 = java.util.regex.Pattern.compile(r21)
            java.lang.String r21 = "(?<=time=).*?ms"
            java.util.regex.Pattern r11 = java.util.regex.Pattern.compile(r21)
            r15 = 0
            r16 = 0
            r4 = 0
            r17 = r16
        L_0x001b:
            if (r4 != 0) goto L_0x0203
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r22 = 30
            r0 = r21
            r1 = r22
            if (r0 >= r1) goto L_0x0203
            java.lang.String r19 = ""
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r21.<init>()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.String r22 = "ping -c 1 -t "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            int r22 = r24.getHop()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.String r22 = " "
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.String r22 = r24.getHost()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.StringBuilder r21 = r21.append(r22)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.String r2 = r21.toString()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.lang.Runtime r21 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r0 = r21
            java.lang.Process r15 = r0.exec(r2)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.io.BufferedReader r16 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.io.InputStreamReader r21 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            java.io.InputStream r22 = r15.getInputStream()     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r21.<init>(r22)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r0 = r16
            r1 = r21
            r0.<init>(r1)     // Catch:{ IOException -> 0x0222, InterruptedException -> 0x021a, all -> 0x0216 }
            r5 = 0
        L_0x0070:
            java.lang.String r5 = r16.readLine()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r5 == 0) goto L_0x008e
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21.<init>()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            r1 = r19
            java.lang.StringBuilder r21 = r0.append(r1)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            java.lang.StringBuilder r21 = r0.append(r5)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r19 = r21.toString()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            goto L_0x0070
        L_0x008e:
            r16.close()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r15.waitFor()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r19
            java.util.regex.Matcher r7 = r12.matcher(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21 = 256(0x100, float:3.59E-43)
            r0 = r21
            r6.<init>(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            boolean r21 = r7.find()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 == 0) goto L_0x0163
            java.lang.String r13 = r7.group()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            tv.danmaku.ijk.media.player.LDNetTraceRoute$PingTask r14 = new tv.danmaku.ijk.media.player.LDNetTraceRoute$PingTask     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r23
            r14.<init>(r13)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r23
            java.lang.String r18 = r0.execPing(r14)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r18.length()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 != 0) goto L_0x00cd
            java.lang.String r21 = "unknown host or network error\n"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r4 = 1
        L_0x00c9:
            r17 = r16
            goto L_0x001b
        L_0x00cd:
            r0 = r18
            java.util.regex.Matcher r9 = r11.matcher(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            boolean r21 = r9.find()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 == 0) goto L_0x0137
            java.lang.String r20 = r9.group()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r6.append(r13)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r20
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
        L_0x0106:
            r0 = r23
            tv.danmaku.ijk.media.player.LDNetTraceRoute$LDNetTraceRouteListener r0 = r0.listener     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21 = r0
            java.lang.String r22 = r6.toString()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21.OnNetTraceUpdated(r22)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r21 + 1
            r0 = r24
            r1 = r21
            r0.setHop(r1)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            goto L_0x00c9
        L_0x0121:
            r3 = move-exception
        L_0x0122:
            r3.printStackTrace()     // Catch:{ all -> 0x01da }
            if (r16 == 0) goto L_0x012a
            r16.close()     // Catch:{ Exception -> 0x021f }
        L_0x012a:
            r15.destroy()     // Catch:{ Exception -> 0x021f }
        L_0x012d:
            r0 = r23
            tv.danmaku.ijk.media.player.LDNetTraceRoute$LDNetTraceRouteListener r0 = r0.listener
            r21 = r0
            r21.OnNetTraceFinished()
            return
        L_0x0137:
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r6.append(r13)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t timeout \t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            goto L_0x0106
        L_0x0154:
            r3 = move-exception
        L_0x0155:
            r3.printStackTrace()     // Catch:{ all -> 0x01da }
            if (r16 == 0) goto L_0x015d
            r16.close()     // Catch:{ Exception -> 0x0161 }
        L_0x015d:
            r15.destroy()     // Catch:{ Exception -> 0x0161 }
            goto L_0x012d
        L_0x0161:
            r21 = move-exception
            goto L_0x012d
        L_0x0163:
            r0 = r19
            java.util.regex.Matcher r8 = r10.matcher(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            boolean r21 = r8.find()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 == 0) goto L_0x01bc
            java.lang.String r13 = r8.group()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r19
            java.util.regex.Matcher r9 = r11.matcher(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            boolean r21 = r9.find()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 == 0) goto L_0x01b9
            java.lang.String r20 = r9.group()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r6.append(r13)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r20
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r23
            tv.danmaku.ijk.media.player.LDNetTraceRoute$LDNetTraceRouteListener r0 = r0.listener     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21 = r0
            java.lang.String r22 = r6.toString()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21.OnNetTraceUpdated(r22)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
        L_0x01b9:
            r4 = 1
            goto L_0x00c9
        L_0x01bc:
            int r21 = r19.length()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            if (r21 != 0) goto L_0x01e4
            java.lang.String r21 = "unknown host or network error\t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r4 = 1
        L_0x01cb:
            r0 = r23
            tv.danmaku.ijk.media.player.LDNetTraceRoute$LDNetTraceRouteListener r0 = r0.listener     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21 = r0
            java.lang.String r22 = r6.toString()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r21.OnNetTraceUpdated(r22)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            goto L_0x00c9
        L_0x01da:
            r21 = move-exception
        L_0x01db:
            if (r16 == 0) goto L_0x01e0
            r16.close()     // Catch:{ Exception -> 0x0214 }
        L_0x01e0:
            r15.destroy()     // Catch:{ Exception -> 0x0214 }
        L_0x01e3:
            throw r21
        L_0x01e4:
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            java.lang.String r21 = "\t\t timeout \t"
            r0 = r21
            r6.append(r0)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r24.getHop()     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            int r21 = r21 + 1
            r0 = r24
            r1 = r21
            r0.setHop(r1)     // Catch:{ IOException -> 0x0121, InterruptedException -> 0x0154 }
            goto L_0x01cb
        L_0x0203:
            if (r17 == 0) goto L_0x0208
            r17.close()     // Catch:{ Exception -> 0x020f }
        L_0x0208:
            r15.destroy()     // Catch:{ Exception -> 0x020f }
            r16 = r17
            goto L_0x012d
        L_0x020f:
            r21 = move-exception
            r16 = r17
            goto L_0x012d
        L_0x0214:
            r22 = move-exception
            goto L_0x01e3
        L_0x0216:
            r21 = move-exception
            r16 = r17
            goto L_0x01db
        L_0x021a:
            r3 = move-exception
            r16 = r17
            goto L_0x0155
        L_0x021f:
            r21 = move-exception
            goto L_0x012d
        L_0x0222:
            r3 = move-exception
            r16 = r17
            goto L_0x0122
        */
        throw new UnsupportedOperationException("Method not decompiled: tv.danmaku.ijk.media.player.LDNetTraceRoute.execTrace(tv.danmaku.ijk.media.player.LDNetTraceRoute$TraceTask):void");
    }

    private class PingTask {
        private static final String MATCH_PING_HOST_IP = "(?<=\\().*?(?=\\))";
        private String host;

        public String getHost() {
            return this.host;
        }

        public PingTask(String host2) {
            this.host = host2;
            Matcher m = Pattern.compile(MATCH_PING_HOST_IP).matcher(host2);
            if (m.find()) {
                this.host = m.group();
            }
        }
    }

    private class TraceTask {
        private int hop;
        private final String host;

        public TraceTask(String host2, int hop2) {
            this.host = host2;
            this.hop = hop2;
        }

        public String getHost() {
            return this.host;
        }

        public int getHop() {
            return this.hop;
        }

        public void setHop(int hop2) {
            this.hop = hop2;
        }
    }
}
