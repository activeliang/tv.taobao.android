package com.uc.webview.export.internal.utility;

import android.os.Build;
import android.os.SystemClock;
import android.taobao.windvane.jsbridge.utils.YearClass;
import android.taobao.windvane.service.WVEventId;
import android.util.Pair;
import anet.channel.request.Request;
import com.uc.webview.export.internal.setup.UCSetupException;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/* compiled from: ProGuard */
public final class c {
    private static final Long a = 10000L;
    private static final HashMap<String, Pair<Long, Object>> b = new HashMap<>();

    public static boolean a(String str) {
        return str == null || str.length() == 0;
    }

    public static String a() {
        try {
            File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(new d());
            Log.d("Utils", "CPU Count: " + listFiles.length);
            return String.valueOf(listFiles.length);
        } catch (Throwable th) {
            Log.d("Utils", "CPU Count: Failed.");
            return "1";
        }
    }

    public static String b() {
        return String.valueOf(b(d("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").trim()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String d(java.lang.String r5) {
        /*
            r2 = 0
            java.lang.String r0 = ""
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ Throwable -> 0x0048, all -> 0x0035 }
            r3.<init>(r5)     // Catch:{ Throwable -> 0x0048, all -> 0x0035 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x004b, all -> 0x0043 }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x004b, all -> 0x0043 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x001d, all -> 0x0046 }
            r2.<init>()     // Catch:{ Throwable -> 0x001d, all -> 0x0046 }
        L_0x0013:
            java.lang.String r4 = r1.readLine()     // Catch:{ Throwable -> 0x001d, all -> 0x0046 }
            if (r4 == 0) goto L_0x002a
            r2.append(r4)     // Catch:{ Throwable -> 0x001d, all -> 0x0046 }
            goto L_0x0013
        L_0x001d:
            r2 = move-exception
            r2 = r3
        L_0x001f:
            if (r2 == 0) goto L_0x0024
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
        L_0x0024:
            if (r1 == 0) goto L_0x0029
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
        L_0x0029:
            return r0
        L_0x002a:
            java.lang.String r0 = r2.toString()     // Catch:{ Throwable -> 0x001d, all -> 0x0046 }
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            goto L_0x0029
        L_0x0035:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x0038:
            if (r3 == 0) goto L_0x003d
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
        L_0x003d:
            if (r1 == 0) goto L_0x0042
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
        L_0x0042:
            throw r0
        L_0x0043:
            r0 = move-exception
            r1 = r2
            goto L_0x0038
        L_0x0046:
            r0 = move-exception
            goto L_0x0038
        L_0x0048:
            r1 = move-exception
            r1 = r2
            goto L_0x001f
        L_0x004b:
            r1 = move-exception
            r1 = r2
            r2 = r3
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.c.d(java.lang.String):java.lang.String");
    }

    public static boolean a(Boolean bool) {
        return bool == null || !bool.booleanValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d9 A[SYNTHETIC, Splitter:B:41:0x00d9] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x013e A[Catch:{ Throwable -> 0x0079, Throwable -> 0x00dd, all -> 0x017f, all -> 0x0183, all -> 0x00cd, Throwable -> 0x0175, Throwable -> 0x0177 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0159 A[Catch:{ Throwable -> 0x0079, Throwable -> 0x00dd, all -> 0x017f, all -> 0x0183, all -> 0x00cd, Throwable -> 0x0175, Throwable -> 0x0177 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:65:0x0138=Splitter:B:65:0x0138, B:43:0x00dc=Splitter:B:43:0x00dc} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File a(java.io.File r11, java.io.File r12, java.io.File r13, boolean r14) {
        /*
            r2 = 0
            boolean r0 = r12.exists()
            if (r0 == 0) goto L_0x000a
            if (r14 != 0) goto L_0x000a
        L_0x0009:
            return r12
        L_0x000a:
            boolean r0 = r13.exists()
            if (r0 == 0) goto L_0x002a
            long r0 = r11.length()
            long r4 = r13.length()
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x002a
            long r0 = r11.lastModified()
            long r4 = r13.lastModified()
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x002a
            r12 = r13
            goto L_0x0009
        L_0x002a:
            java.io.File r6 = new java.io.File
            java.lang.String r0 = r13.getParent()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r3 = "bak_"
            r1.<init>(r3)
            java.lang.String r3 = r13.getName()
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r6.<init>(r0, r1)
            boolean r0 = r6.exists()
            if (r0 == 0) goto L_0x0071
            long r0 = r11.length()
            long r4 = r6.length()
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x006e
            long r0 = r11.lastModified()
            r6.setLastModified(r0)
            long r0 = r6.lastModified()
            long r4 = r11.lastModified()
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x006e
            r12 = r6
            goto L_0x0009
        L_0x006e:
            r6.delete()
        L_0x0071:
            if (r14 == 0) goto L_0x00e6
            java.lang.Throwable r0 = new java.lang.Throwable     // Catch:{ Throwable -> 0x0079 }
            r0.<init>()     // Catch:{ Throwable -> 0x0079 }
            throw r0     // Catch:{ Throwable -> 0x0079 }
        L_0x0079:
            r0 = move-exception
            r13.delete()     // Catch:{ Throwable -> 0x00dd }
            java.io.File r7 = new java.io.File     // Catch:{ Throwable -> 0x00dd }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00dd }
            r0.<init>()     // Catch:{ Throwable -> 0x00dd }
            java.lang.String r1 = r13.getAbsolutePath()     // Catch:{ Throwable -> 0x00dd }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00dd }
            java.lang.String r1 = ".tmp"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x00dd }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00dd }
            r7.<init>(r0)     // Catch:{ Throwable -> 0x00dd }
            r7.createNewFile()     // Catch:{ Throwable -> 0x00dd }
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ all -> 0x017f }
            r0.<init>(r11)     // Catch:{ all -> 0x017f }
            java.nio.channels.FileChannel r1 = r0.getChannel()     // Catch:{ all -> 0x017f }
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ all -> 0x0183 }
            r0.<init>(r7)     // Catch:{ all -> 0x0183 }
            java.nio.channels.FileChannel r0 = r0.getChannel()     // Catch:{ all -> 0x0183 }
            r2 = 0
            long r4 = r1.size()     // Catch:{ all -> 0x00cd }
            long r2 = r0.transferFrom(r1, r2, r4)     // Catch:{ all -> 0x00cd }
            long r4 = r11.length()     // Catch:{ all -> 0x00cd }
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x012e
            r7.delete()     // Catch:{ all -> 0x00cd }
            java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ all -> 0x00cd }
            java.lang.String r3 = "Size mismatch."
            r2.<init>(r3)     // Catch:{ all -> 0x00cd }
            throw r2     // Catch:{ all -> 0x00cd }
        L_0x00cd:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r0
            r0 = r10
        L_0x00d2:
            if (r2 == 0) goto L_0x00d7
            r2.close()     // Catch:{ Throwable -> 0x0179 }
        L_0x00d7:
            if (r1 == 0) goto L_0x00dc
            r1.close()     // Catch:{ Throwable -> 0x017c }
        L_0x00dc:
            throw r0     // Catch:{ Throwable -> 0x00dd }
        L_0x00dd:
            r0 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r1 = new com.uc.webview.export.internal.setup.UCSetupException
            r2 = 1007(0x3ef, float:1.411E-42)
            r1.<init>((int) r2, (java.lang.Throwable) r0)
            throw r1
        L_0x00e6:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r1 = "ln -s "
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r1 = r11.getAbsolutePath()     // Catch:{ Throwable -> 0x0079 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r1 = " "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r1 = r12.getAbsolutePath()     // Catch:{ Throwable -> 0x0079 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0079 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0079 }
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x0079 }
            java.lang.Process r0 = r1.exec(r0)     // Catch:{ Throwable -> 0x0079 }
            com.uc.webview.export.cyclone.UCElapseTime r1 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ Throwable -> 0x0079 }
            r1.<init>()     // Catch:{ Throwable -> 0x0079 }
        L_0x0116:
            long r4 = r1.getMilis()     // Catch:{ Throwable -> 0x0079 }
            r8 = 500(0x1f4, double:2.47E-321)
            int r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r3 >= 0) goto L_0x0009
            int r3 = r0.exitValue()     // Catch:{ IllegalThreadStateException -> 0x012c }
            if (r3 == 0) goto L_0x0009
            java.lang.Throwable r3 = new java.lang.Throwable     // Catch:{ IllegalThreadStateException -> 0x012c }
            r3.<init>()     // Catch:{ IllegalThreadStateException -> 0x012c }
            throw r3     // Catch:{ IllegalThreadStateException -> 0x012c }
        L_0x012c:
            r3 = move-exception
            goto L_0x0116
        L_0x012e:
            if (r1 == 0) goto L_0x0133
            r1.close()     // Catch:{ Throwable -> 0x0175 }
        L_0x0133:
            if (r0 == 0) goto L_0x0138
            r0.close()     // Catch:{ Throwable -> 0x0177 }
        L_0x0138:
            boolean r0 = r7.renameTo(r13)     // Catch:{ Throwable -> 0x00dd }
            if (r0 != 0) goto L_0x0159
            r7.delete()     // Catch:{ Throwable -> 0x00dd }
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Throwable -> 0x00dd }
            r1 = 1005(0x3ed, float:1.408E-42)
            java.lang.String r2 = "Rename [%s] to [%s] failed."
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00dd }
            r4 = 0
            r3[r4] = r7     // Catch:{ Throwable -> 0x00dd }
            r4 = 1
            r3[r4] = r13     // Catch:{ Throwable -> 0x00dd }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ Throwable -> 0x00dd }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x00dd }
            throw r0     // Catch:{ Throwable -> 0x00dd }
        L_0x0159:
            long r0 = r11.lastModified()     // Catch:{ Throwable -> 0x00dd }
            r13.setLastModified(r0)     // Catch:{ Throwable -> 0x00dd }
            long r0 = r13.lastModified()     // Catch:{ Throwable -> 0x00dd }
            long r2 = r11.lastModified()     // Catch:{ Throwable -> 0x00dd }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0172
            r13.renameTo(r6)     // Catch:{ Throwable -> 0x00dd }
            r12 = r6
            goto L_0x0009
        L_0x0172:
            r12 = r13
            goto L_0x0009
        L_0x0175:
            r1 = move-exception
            goto L_0x0133
        L_0x0177:
            r0 = move-exception
            goto L_0x0138
        L_0x0179:
            r2 = move-exception
            goto L_0x00d7
        L_0x017c:
            r1 = move-exception
            goto L_0x00dc
        L_0x017f:
            r0 = move-exception
            r1 = r2
            goto L_0x00d2
        L_0x0183:
            r0 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
            goto L_0x00d2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.c.a(java.io.File, java.io.File, java.io.File, boolean):java.io.File");
    }

    public static Pair<Long, Long> a(String str, URL url) {
        boolean z;
        Pair pair = b.get(str);
        Pair<Long, Long> pair2 = (Pair) ((pair == null || Long.valueOf(SystemClock.currentThreadTimeMillis() - ((Long) pair.first).longValue()).longValue() >= a.longValue()) ? null : pair.second);
        if (pair2 == null) {
            try {
                URL url2 = new URL(url, str);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setRequestMethod(Request.Method.HEAD);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode < 200 || responseCode > 303) {
                    throw new RuntimeException("Response code not correct:" + responseCode);
                }
                if (responseCode == 300 || responseCode == 301 || responseCode == 302 || responseCode == 303) {
                    String headerField = httpURLConnection.getHeaderField("Location");
                    if (headerField == null || headerField.length() == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        throw new RuntimeException("Redirect location is null.");
                    }
                    pair2 = a(headerField, url2);
                } else {
                    long lastModified = httpURLConnection.getLastModified();
                    long contentLength = (long) httpURLConnection.getContentLength();
                    if (contentLength <= 0) {
                        throw new RuntimeException("Total size is not correct:" + contentLength);
                    }
                    httpURLConnection.disconnect();
                    pair2 = new Pair<>(Long.valueOf(contentLength), Long.valueOf(lastModified));
                }
                b.put(str, new Pair(Long.valueOf(SystemClock.currentThreadTimeMillis()), pair2));
            } catch (Throwable th) {
                throw new UCSetupException((int) YearClass.CLASS_2009, th);
            }
        }
        return pair2;
    }

    public static Boolean a(HashMap<String, Object> hashMap, String str) {
        Object obj = hashMap.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.valueOf(Boolean.parseBoolean((String) obj));
        }
        throw new UCSetupException((int) WVEventId.WV_JSCALLBAK_ERROR, String.format("\"true\" or \"false\" or boolean expected with key:[%s], now is [%s]", new Object[]{str, obj}));
    }

    public static boolean a(File file) {
        try {
            if (!file.exists()) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= 9) {
                return file.setReadable(true, false);
            }
            Runtime.getRuntime().exec("chmod 644 " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.e("Utils", "setReadable", e);
            return false;
        }
    }

    public static boolean b(File file) {
        try {
            if (file.exists()) {
                return file.canRead();
            }
        } catch (Exception e) {
            Log.e("Utils", "canRead", e);
        }
        return false;
    }

    public static boolean c(File file) {
        if (file != null) {
            try {
                if (file.exists()) {
                    if (Build.VERSION.SDK_INT >= 9) {
                        return file.setExecutable(true, false);
                    }
                    Runtime.getRuntime().exec("chmod 711 " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                Log.e("Utils", "setExecutable", e);
                return false;
            }
        }
        return true;
    }

    public static boolean d(File file) {
        try {
            if (!file.exists() || Build.VERSION.SDK_INT < 9) {
                return false;
            }
            return file.canExecute();
        } catch (Exception e) {
            Log.e("Utils", "canExecute", e);
            return false;
        }
    }

    public static int b(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long c(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
