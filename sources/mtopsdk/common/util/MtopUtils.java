package mtopsdk.common.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.IOException;
import java.lang.Character;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.TBSdkLog;
import org.json.JSONObject;

public final class MtopUtils {
    private static final char[] LOWER_CASE_ENCRYPT_CHARS = {'e', 't', 'a', 'o', 'i', 'n', 's', 'r', 'h', 'l', 'd', 'c', 'u', 'm', 'f', 'p', 'g', 'w', 'y', 'b', 'v', 'k', 'x', 'j', 'q', 'z'};
    public static final int MTOP_BIZID = 4099;
    private static final char[] NUMBER_ENCRYPT_CHARS = {'8', '6', '1', '5', '9', '2', '3', '0', '4', '7'};
    private static final String TAG = "mtopsdk.MtopUtils";
    private static final char[] UPPER_CASE_ENCRYPT_CHARS = {'E', 'T', 'A', 'O', 'I', 'N', 'S', 'R', 'H', 'L', 'D', 'C', 'U', 'M', 'F', 'P', 'G', 'W', 'Y', 'B', 'V', 'K', 'X', 'J', 'Q', 'Z'};
    public static final List<String> apiWhiteList = Arrays.asList(new String[]{"mtop.common.gettimestamp$*"});
    private static AtomicInteger counter = new AtomicInteger();
    private static volatile Context mContext = null;
    private static volatile String mProcessName = null;
    private static final int mask = Integer.MAX_VALUE;

    private MtopUtils() {
    }

    public static long convertTimeFormatGMT2Long(String gmt) {
        long time = -1;
        if (StringUtils.isBlank(gmt)) {
            return -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = sdf.parse(gmt);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[convertTimeFormatGMT2Long]parse gmt timeformat error");
        }
        if (date != null) {
            time = date.getTime() / 1000;
        }
        return time;
    }

    public static int createIntSeqNo() {
        return Integer.MAX_VALUE & counter.incrementAndGet();
    }

    @TargetApi(3)
    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    @TargetApi(4)
    public static boolean isApkDebug(Context context) {
        if (context == null) {
            context = getContext();
        }
        if (context == null) {
            TBSdkLog.e(TAG, "[isApkDebug] context is null!");
            return false;
        }
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public static String urlDecode(String input, String charset) {
        if (StringUtils.isBlank(input)) {
            return input;
        }
        String output = null;
        try {
            output = URLDecoder.decode(input, charset);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[urlDecode] URLDecoder decode error. input=" + input + ", charset= " + charset, (Throwable) e);
        }
        return output;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.io.Serializable} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0059 A[SYNTHETIC, Splitter:B:29:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0062 A[SYNTHETIC, Splitter:B:34:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.Serializable readObject(java.io.File r13, java.lang.String r14) {
        /*
            r1 = 0
            r3 = 0
            r6 = 0
            r5 = 0
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0040 }
            r2.<init>(r13, r14)     // Catch:{ Throwable -> 0x0040 }
            boolean r9 = r2.exists()     // Catch:{ Throwable -> 0x0076, all -> 0x006a }
            if (r9 != 0) goto L_0x0017
            r9 = 0
            if (r3 == 0) goto L_0x0015
            r3.close()     // Catch:{ IOException -> 0x0066 }
        L_0x0015:
            r1 = r2
        L_0x0016:
            return r9
        L_0x0017:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0076, all -> 0x006a }
            r4.<init>(r2)     // Catch:{ Throwable -> 0x0076, all -> 0x006a }
            java.io.ObjectInputStream r7 = new java.io.ObjectInputStream     // Catch:{ Throwable -> 0x0079, all -> 0x006d }
            java.io.BufferedInputStream r9 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x0079, all -> 0x006d }
            r9.<init>(r4)     // Catch:{ Throwable -> 0x0079, all -> 0x006d }
            r7.<init>(r9)     // Catch:{ Throwable -> 0x0079, all -> 0x006d }
            java.lang.Object r9 = r7.readObject()     // Catch:{ Throwable -> 0x007d, all -> 0x0071 }
            r0 = r9
            java.io.Serializable r0 = (java.io.Serializable) r0     // Catch:{ Throwable -> 0x007d, all -> 0x0071 }
            r5 = r0
            r7.close()     // Catch:{ Throwable -> 0x007d, all -> 0x0071 }
            if (r4 == 0) goto L_0x0082
            r4.close()     // Catch:{ IOException -> 0x003b }
            r6 = r7
            r3 = r4
            r1 = r2
        L_0x0039:
            r9 = r5
            goto L_0x0016
        L_0x003b:
            r9 = move-exception
            r6 = r7
            r3 = r4
            r1 = r2
            goto L_0x0039
        L_0x0040:
            r8 = move-exception
        L_0x0041:
            java.lang.String r9 = "mtopsdk.MtopUtils"
            java.lang.String r10 = "readObject error.fileDir={%s},fileName={%s}"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x005f }
            r12 = 0
            r11[r12] = r13     // Catch:{ all -> 0x005f }
            r12 = 1
            r11[r12] = r14     // Catch:{ all -> 0x005f }
            java.lang.String r10 = java.lang.String.format(r10, r11)     // Catch:{ all -> 0x005f }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r9, (java.lang.String) r10, (java.lang.Throwable) r8)     // Catch:{ all -> 0x005f }
            if (r3 == 0) goto L_0x0039
            r3.close()     // Catch:{ IOException -> 0x005d }
            goto L_0x0039
        L_0x005d:
            r9 = move-exception
            goto L_0x0039
        L_0x005f:
            r9 = move-exception
        L_0x0060:
            if (r3 == 0) goto L_0x0065
            r3.close()     // Catch:{ IOException -> 0x0068 }
        L_0x0065:
            throw r9
        L_0x0066:
            r10 = move-exception
            goto L_0x0015
        L_0x0068:
            r10 = move-exception
            goto L_0x0065
        L_0x006a:
            r9 = move-exception
            r1 = r2
            goto L_0x0060
        L_0x006d:
            r9 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0060
        L_0x0071:
            r9 = move-exception
            r6 = r7
            r3 = r4
            r1 = r2
            goto L_0x0060
        L_0x0076:
            r8 = move-exception
            r1 = r2
            goto L_0x0041
        L_0x0079:
            r8 = move-exception
            r3 = r4
            r1 = r2
            goto L_0x0041
        L_0x007d:
            r8 = move-exception
            r6 = r7
            r3 = r4
            r1 = r2
            goto L_0x0041
        L_0x0082:
            r6 = r7
            r3 = r4
            r1 = r2
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.readObject(java.io.File, java.lang.String):java.io.Serializable");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x007c A[SYNTHETIC, Splitter:B:26:0x007c] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0085 A[SYNTHETIC, Splitter:B:31:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeObject(java.io.Serializable r12, java.io.File r13, java.lang.String r14) {
        /*
            r0 = 0
            r2 = 0
            r4 = 0
            r7 = 0
            boolean r8 = r13.exists()     // Catch:{ Throwable -> 0x0060 }
            if (r8 != 0) goto L_0x000d
            r13.mkdirs()     // Catch:{ Throwable -> 0x0060 }
        L_0x000d:
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0060 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0060 }
            r8.<init>()     // Catch:{ Throwable -> 0x0060 }
            java.lang.StringBuilder r8 = r8.append(r14)     // Catch:{ Throwable -> 0x0060 }
            java.util.Random r9 = new java.util.Random     // Catch:{ Throwable -> 0x0060 }
            r9.<init>()     // Catch:{ Throwable -> 0x0060 }
            r10 = 10
            int r9 = r9.nextInt(r10)     // Catch:{ Throwable -> 0x0060 }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Throwable -> 0x0060 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0060 }
            r1.<init>(r13, r8)     // Catch:{ Throwable -> 0x0060 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0097, all -> 0x008b }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x0097, all -> 0x008b }
            java.io.ObjectOutputStream r5 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x009a, all -> 0x008e }
            java.io.BufferedOutputStream r8 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x009a, all -> 0x008e }
            r8.<init>(r3)     // Catch:{ Throwable -> 0x009a, all -> 0x008e }
            r5.<init>(r8)     // Catch:{ Throwable -> 0x009a, all -> 0x008e }
            r5.writeObject(r12)     // Catch:{ Throwable -> 0x009e, all -> 0x0092 }
            r5.flush()     // Catch:{ Throwable -> 0x009e, all -> 0x0092 }
            r5.close()     // Catch:{ Throwable -> 0x009e, all -> 0x0092 }
            r7 = 1
            if (r3 == 0) goto L_0x00a3
            r3.close()     // Catch:{ IOException -> 0x005b }
            r4 = r5
            r2 = r3
            r0 = r1
        L_0x004f:
            if (r7 == 0) goto L_0x005a
            java.io.File r8 = new java.io.File
            r8.<init>(r13, r14)
            boolean r7 = r0.renameTo(r8)
        L_0x005a:
            return r7
        L_0x005b:
            r8 = move-exception
            r4 = r5
            r2 = r3
            r0 = r1
            goto L_0x004f
        L_0x0060:
            r6 = move-exception
        L_0x0061:
            java.lang.String r8 = "mtopsdk.MtopUtils"
            java.lang.String r9 = "writeObject error.fileDir={%s},fileName={%s},object={%s}"
            r10 = 3
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x0082 }
            r11 = 0
            r10[r11] = r13     // Catch:{ all -> 0x0082 }
            r11 = 1
            r10[r11] = r14     // Catch:{ all -> 0x0082 }
            r11 = 2
            r10[r11] = r12     // Catch:{ all -> 0x0082 }
            java.lang.String r9 = java.lang.String.format(r9, r10)     // Catch:{ all -> 0x0082 }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r8, (java.lang.String) r9, (java.lang.Throwable) r6)     // Catch:{ all -> 0x0082 }
            if (r2 == 0) goto L_0x004f
            r2.close()     // Catch:{ IOException -> 0x0080 }
            goto L_0x004f
        L_0x0080:
            r8 = move-exception
            goto L_0x004f
        L_0x0082:
            r8 = move-exception
        L_0x0083:
            if (r2 == 0) goto L_0x0088
            r2.close()     // Catch:{ IOException -> 0x0089 }
        L_0x0088:
            throw r8
        L_0x0089:
            r9 = move-exception
            goto L_0x0088
        L_0x008b:
            r8 = move-exception
            r0 = r1
            goto L_0x0083
        L_0x008e:
            r8 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x0083
        L_0x0092:
            r8 = move-exception
            r4 = r5
            r2 = r3
            r0 = r1
            goto L_0x0083
        L_0x0097:
            r6 = move-exception
            r0 = r1
            goto L_0x0061
        L_0x009a:
            r6 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x0061
        L_0x009e:
            r6 = move-exception
            r4 = r5
            r2 = r3
            r0 = r1
            goto L_0x0061
        L_0x00a3:
            r4 = r5
            r2 = r3
            r0 = r1
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.writeObject(java.io.Serializable, java.io.File, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003a A[SYNTHETIC, Splitter:B:17:0x003a] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003f A[SYNTHETIC, Splitter:B:20:0x003f] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x006e A[SYNTHETIC, Splitter:B:39:0x006e] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0073 A[SYNTHETIC, Splitter:B:42:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x007c A[SYNTHETIC, Splitter:B:47:0x007c] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0081 A[SYNTHETIC, Splitter:B:50:0x0081] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readFile(java.lang.String r14) {
        /*
            r6 = 0
            r0 = 0
            r3 = 0
            java.io.File r5 = new java.io.File     // Catch:{ FileNotFoundException -> 0x009f, Throwable -> 0x0058 }
            r5.<init>(r14)     // Catch:{ FileNotFoundException -> 0x009f, Throwable -> 0x0058 }
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x009f, Throwable -> 0x0058 }
            r7.<init>(r5)     // Catch:{ FileNotFoundException -> 0x009f, Throwable -> 0x0058 }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ FileNotFoundException -> 0x00a1, Throwable -> 0x0098, all -> 0x0091 }
            r1.<init>()     // Catch:{ FileNotFoundException -> 0x00a1, Throwable -> 0x0098, all -> 0x0091 }
            r10 = 4096(0x1000, float:5.74E-42)
            byte[] r2 = new byte[r10]     // Catch:{ FileNotFoundException -> 0x0022, Throwable -> 0x009b, all -> 0x0094 }
        L_0x0016:
            int r8 = r7.read(r2)     // Catch:{ FileNotFoundException -> 0x0022, Throwable -> 0x009b, all -> 0x0094 }
            r10 = -1
            if (r8 == r10) goto L_0x0043
            r10 = 0
            r1.write(r2, r10, r8)     // Catch:{ FileNotFoundException -> 0x0022, Throwable -> 0x009b, all -> 0x0094 }
            goto L_0x0016
        L_0x0022:
            r4 = move-exception
            r0 = r1
            r6 = r7
        L_0x0025:
            java.lang.String r10 = "mtopsdk.MtopUtils"
            java.lang.String r11 = "readFile error.filePath={%s} is not found."
            r12 = 1
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0079 }
            r13 = 0
            r12[r13] = r14     // Catch:{ all -> 0x0079 }
            java.lang.String r11 = java.lang.String.format(r11, r12)     // Catch:{ all -> 0x0079 }
            mtopsdk.common.util.TBSdkLog.w(r10, r11)     // Catch:{ all -> 0x0079 }
            if (r0 == 0) goto L_0x003d
            r0.close()     // Catch:{ IOException -> 0x0087 }
        L_0x003d:
            if (r6 == 0) goto L_0x0042
            r6.close()     // Catch:{ IOException -> 0x0089 }
        L_0x0042:
            return r3
        L_0x0043:
            byte[] r3 = r1.toByteArray()     // Catch:{ FileNotFoundException -> 0x0022, Throwable -> 0x009b, all -> 0x0094 }
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ IOException -> 0x0085 }
        L_0x004c:
            if (r7 == 0) goto L_0x00a4
            r7.close()     // Catch:{ IOException -> 0x0054 }
            r0 = r1
            r6 = r7
            goto L_0x0042
        L_0x0054:
            r10 = move-exception
            r0 = r1
            r6 = r7
            goto L_0x0042
        L_0x0058:
            r9 = move-exception
        L_0x0059:
            java.lang.String r10 = "mtopsdk.MtopUtils"
            java.lang.String r11 = "readFile error.filePath={%s}"
            r12 = 1
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0079 }
            r13 = 0
            r12[r13] = r14     // Catch:{ all -> 0x0079 }
            java.lang.String r11 = java.lang.String.format(r11, r12)     // Catch:{ all -> 0x0079 }
            mtopsdk.common.util.TBSdkLog.w((java.lang.String) r10, (java.lang.String) r11, (java.lang.Throwable) r9)     // Catch:{ all -> 0x0079 }
            if (r0 == 0) goto L_0x0071
            r0.close()     // Catch:{ IOException -> 0x008b }
        L_0x0071:
            if (r6 == 0) goto L_0x0042
            r6.close()     // Catch:{ IOException -> 0x0077 }
            goto L_0x0042
        L_0x0077:
            r10 = move-exception
            goto L_0x0042
        L_0x0079:
            r10 = move-exception
        L_0x007a:
            if (r0 == 0) goto L_0x007f
            r0.close()     // Catch:{ IOException -> 0x008d }
        L_0x007f:
            if (r6 == 0) goto L_0x0084
            r6.close()     // Catch:{ IOException -> 0x008f }
        L_0x0084:
            throw r10
        L_0x0085:
            r10 = move-exception
            goto L_0x004c
        L_0x0087:
            r10 = move-exception
            goto L_0x003d
        L_0x0089:
            r10 = move-exception
            goto L_0x0042
        L_0x008b:
            r10 = move-exception
            goto L_0x0071
        L_0x008d:
            r11 = move-exception
            goto L_0x007f
        L_0x008f:
            r11 = move-exception
            goto L_0x0084
        L_0x0091:
            r10 = move-exception
            r6 = r7
            goto L_0x007a
        L_0x0094:
            r10 = move-exception
            r0 = r1
            r6 = r7
            goto L_0x007a
        L_0x0098:
            r9 = move-exception
            r6 = r7
            goto L_0x0059
        L_0x009b:
            r9 = move-exception
            r0 = r1
            r6 = r7
            goto L_0x0059
        L_0x009f:
            r4 = move-exception
            goto L_0x0025
        L_0x00a1:
            r4 = move-exception
            r6 = r7
            goto L_0x0025
        L_0x00a4:
            r0 = r1
            r6 = r7
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.MtopUtils.readFile(java.lang.String):byte[]");
    }

    public static String convertUrl(String urlStr) {
        if (TextUtils.isEmpty(urlStr)) {
            return "";
        }
        if (urlStr.startsWith(WVUtils.URL_SEPARATOR)) {
            urlStr = "http:" + urlStr;
        }
        try {
            int index1 = urlStr.indexOf(WVUtils.URL_DATA_CHAR);
            if (index1 != -1) {
                return urlStr.substring(0, index1);
            }
            int index2 = urlStr.indexOf(Constant.INTENT_JSON_MARK);
            if (index2 != -1) {
                return urlStr.substring(0, index2);
            }
            return urlStr;
        } catch (Exception e) {
            return "";
        }
    }

    public static Context getContext() {
        if (mContext == null) {
            synchronized (MtopUtils.class) {
                if (mContext == null) {
                    try {
                        Class<?> clazz = Class.forName("android.app.ActivityThread");
                        Object object = clazz.getMethod("currentActivityThread", new Class[0]).invoke(clazz, new Object[0]);
                        mContext = (Context) object.getClass().getMethod("getApplication", new Class[0]).invoke(object, new Object[0]);
                    } catch (Exception e) {
                        TBSdkLog.e(TAG, "getContext through reflection error.");
                    }
                }
            }
        }
        return mContext;
    }

    @TargetApi(3)
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return mProcessName;
        }
        if (mProcessName == null) {
            synchronized (MtopUtils.class) {
                if (mProcessName == null) {
                    try {
                        int pid = Process.myPid();
                        List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
                        if (processes != null && processes.size() > 0) {
                            Iterator i = processes.iterator();
                            while (true) {
                                if (i.hasNext()) {
                                    ActivityManager.RunningAppProcessInfo info = i.next();
                                    if (info.pid == pid) {
                                        mProcessName = info.processName;
                                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                                            TBSdkLog.i(TAG, "get current processName succeed,processName=" + mProcessName);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        TBSdkLog.e(TAG, "get current processName failed.", (Throwable) e);
                    }
                }
                break;
            }
        }
        return mProcessName;
    }

    public static boolean isAppOpenMock(Context context) {
        if (context == null) {
            context = getContext();
        }
        if (context == null) {
            TBSdkLog.e(TAG, "[isAppOpenMock] context is null!");
            return false;
        }
        try {
            byte[] byteData = readFile(context.getFilesDir().getCanonicalPath() + "/mock/openMock.json");
            if (byteData == null) {
                return false;
            }
            try {
                JSONObject jsonObject = new JSONObject(new String(byteData));
                if (jsonObject != null) {
                    return jsonObject.getBoolean("openMock");
                }
                return false;
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[isAppOpenMock]parse openMock flag error in isOpenMock.json .", (Throwable) e);
                return false;
            }
        } catch (IOException e2) {
            TBSdkLog.e(TAG, "[isAppOpenMock] parse ExternalFilesDir/mock/openMock.json filePath error.", (Throwable) e2);
            return false;
        }
    }

    public static String caesarEncrypt(String inputStr) {
        if (inputStr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputStr.length(); i++) {
            sb.append(caesarEncodeChar(inputStr.charAt(i)));
        }
        return sb.toString();
    }

    private static char caesarEncodeChar(char c) {
        if (c >= 'A' && c <= 'Z') {
            return UPPER_CASE_ENCRYPT_CHARS[c - 'A'];
        }
        if (c >= 'a' && c <= 'z') {
            return LOWER_CASE_ENCRYPT_CHARS[c - 'a'];
        }
        if (c < '0' || c > '9') {
            return c;
        }
        return NUMBER_ENCRYPT_CHARS[c - '0'];
    }

    public static final boolean isContainChineseCharacter(String inputStr) {
        if (inputStr == null) {
            return false;
        }
        char[] ch = inputStr.toCharArray();
        int i = 0;
        while (i < ch.length) {
            char c = ch[i];
            try {
                Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
                if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                    return true;
                }
                i++;
            } catch (Throwable th) {
                if (c >= 19968 && c <= 40959) {
                    return true;
                }
            }
        }
        return false;
    }
}
