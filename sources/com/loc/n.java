package com.loc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.alibaba.analytics.core.device.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: DeviceInfo */
public final class n {
    static String a = "";
    static String b = "";
    public static boolean c = false;
    static String d = "";
    static boolean e = false;
    public static a f = null;
    static int g = -1;
    static String h = "";
    static String i = "";
    private static String j = null;
    private static boolean k = false;
    /* access modifiers changed from: private */
    public static volatile boolean l = false;
    /* access modifiers changed from: private */
    public static String m = "";
    /* access modifiers changed from: private */
    public static boolean n = false;
    private static String o = "";
    private static String p = "";
    private static String q = "";
    private static String r = "";
    private static String s = "";
    private static String t = "";
    private static boolean u = false;
    private static long v;
    private static int w;
    private static String x;
    private static String y = "";

    /* compiled from: DeviceInfo */
    public interface a {
        String a();

        String b();

        Map<String, String> c();

        String d();
    }

    /* compiled from: DeviceInfo */
    static class b implements ServiceConnection {
        b() {
        }

        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.uodis.opendevice.aidl.OpenDeviceIdentifierService");
                iBinder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                String unused = n.m = obtain2.readString();
                obtain2.recycle();
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
                throw th;
            }
            obtain.recycle();
        }

        public final void onServiceDisconnected(ComponentName componentName) {
        }
    }

    public static String A(Context context) {
        try {
            return G(context);
        } catch (Throwable th) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x006a A[SYNTHETIC, Splitter:B:26:0x006a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int B(android.content.Context r5) {
        /*
            r1 = 0
            int r0 = w
            if (r0 == 0) goto L_0x0008
            int r1 = w
        L_0x0007:
            return r1
        L_0x0008:
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 16
            if (r0 < r2) goto L_0x002c
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r5.getSystemService(r0)
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0
            if (r0 == 0) goto L_0x0007
            android.app.ActivityManager$MemoryInfo r1 = new android.app.ActivityManager$MemoryInfo
            r1.<init>()
            r0.getMemoryInfo(r1)
            long r0 = r1.totalMem
            r2 = 1024(0x400, double:5.06E-321)
            long r0 = r0 / r2
            int r0 = (int) r0
        L_0x0027:
            int r1 = r0 / 1024
            w = r1
            goto L_0x0007
        L_0x002c:
            r0 = 0
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.lang.String r2 = "/proc/meminfo"
            r3.<init>(r2)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            r2.<init>(r4)     // Catch:{ Throwable -> 0x005d, all -> 0x0065 }
            java.lang.String r0 = r2.readLine()     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.String r3 = "\\s+"
            java.lang.String[] r0 = r0.split(r3)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            r3 = 1
            r0 = r0[r3]     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x005b }
            goto L_0x0027
        L_0x005b:
            r1 = move-exception
            goto L_0x0027
        L_0x005d:
            r2 = move-exception
        L_0x005e:
            if (r0 == 0) goto L_0x0063
            r0.close()     // Catch:{ IOException -> 0x006e }
        L_0x0063:
            r0 = r1
            goto L_0x0027
        L_0x0065:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L_0x0068:
            if (r2 == 0) goto L_0x006d
            r2.close()     // Catch:{ IOException -> 0x0070 }
        L_0x006d:
            throw r0
        L_0x006e:
            r0 = move-exception
            goto L_0x0063
        L_0x0070:
            r1 = move-exception
            goto L_0x006d
        L_0x0072:
            r0 = move-exception
            goto L_0x0068
        L_0x0074:
            r0 = move-exception
            r0 = r2
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.n.B(android.content.Context):int");
    }

    static String C(Context context) {
        try {
            return H(context);
        } catch (Throwable th) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0092 A[SYNTHETIC, Splitter:B:32:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0099 A[SYNTHETIC, Splitter:B:37:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String E(android.content.Context r9) {
        /*
            r1 = 1
            r3 = 0
            r0 = 0
            java.lang.String r2 = "android.permission.READ_EXTERNAL_STORAGE"
            boolean r2 = com.loc.u.a((android.content.Context) r9, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            if (r2 == 0) goto L_0x00a7
            java.lang.String r2 = "mounted"
            java.lang.String r4 = android.os.Environment.getExternalStorageState()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            boolean r2 = r2.equals(r4)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            if (r2 == 0) goto L_0x00a7
            java.io.File r2 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            r4.<init>()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.StringBuilder r2 = r4.append(r2)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.String r4 = "/.UTSystemConfig/Global/Alvin2.xml"
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            r5.<init>(r2)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            org.xmlpull.v1.XmlPullParser r6 = android.util.Xml.newPullParser()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            int r4 = r6.getEventType()     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            r2.<init>(r5)     // Catch:{ Throwable -> 0x0096, all -> 0x008d }
            java.lang.String r0 = "utf-8"
            r6.setInput(r2, r0)     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            r0 = r3
        L_0x004e:
            if (r1 == r4) goto L_0x00a6
            switch(r4) {
                case 0: goto L_0x0053;
                case 1: goto L_0x0053;
                case 2: goto L_0x0058;
                case 3: goto L_0x008b;
                case 4: goto L_0x007f;
                default: goto L_0x0053;
            }     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
        L_0x0053:
            int r4 = r6.next()     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            goto L_0x004e
        L_0x0058:
            int r4 = r6.getAttributeCount()     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            if (r4 <= 0) goto L_0x0053
            int r5 = r6.getAttributeCount()     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            r4 = r3
        L_0x0063:
            if (r4 >= r5) goto L_0x0053
            java.lang.String r7 = r6.getAttributeValue(r4)     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            java.lang.String r8 = "UTDID2"
            boolean r8 = r8.equals(r7)     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            if (r8 != 0) goto L_0x007b
            java.lang.String r8 = "UTDID"
            boolean r7 = r8.equals(r7)     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            if (r7 == 0) goto L_0x007c
        L_0x007b:
            r0 = r1
        L_0x007c:
            int r4 = r4 + 1
            goto L_0x0063
        L_0x007f:
            if (r0 == 0) goto L_0x0053
            java.lang.String r0 = r6.getText()     // Catch:{ Throwable -> 0x00af, all -> 0x00ad }
            if (r2 == 0) goto L_0x008a
            r2.close()     // Catch:{ Throwable -> 0x00a4 }
        L_0x008a:
            return r0
        L_0x008b:
            r0 = r3
            goto L_0x0053
        L_0x008d:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L_0x0090:
            if (r2 == 0) goto L_0x0095
            r2.close()     // Catch:{ Throwable -> 0x00a2 }
        L_0x0095:
            throw r0
        L_0x0096:
            r1 = move-exception
        L_0x0097:
            if (r0 == 0) goto L_0x009c
            r0.close()     // Catch:{ Throwable -> 0x00a0 }
        L_0x009c:
            java.lang.String r0 = ""
            goto L_0x008a
        L_0x00a0:
            r0 = move-exception
            goto L_0x009c
        L_0x00a2:
            r1 = move-exception
            goto L_0x0095
        L_0x00a4:
            r1 = move-exception
            goto L_0x008a
        L_0x00a6:
            r0 = r2
        L_0x00a7:
            if (r0 == 0) goto L_0x009c
            r0.close()     // Catch:{ Throwable -> 0x00a0 }
            goto L_0x009c
        L_0x00ad:
            r0 = move-exception
            goto L_0x0090
        L_0x00af:
            r0 = move-exception
            r0 = r2
            goto L_0x0097
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.n.E(android.content.Context):java.lang.String");
    }

    /* access modifiers changed from: private */
    public static String F(Context context) {
        int i2 = 0;
        if (u.c("IeGlhb21p").equalsIgnoreCase(Build.MANUFACTURER)) {
            try {
                Class<?> cls = Class.forName(u.c("WY29tLmFuZHJvaWQuaWQuaW1wbC5JZFByb3ZpZGVySW1wbA"));
                Object newInstance = cls.newInstance();
                Object invoke = cls.getMethod(u.c("MZ2V0T0FJRA"), new Class[]{Context.class}).invoke(newInstance, new Object[]{context});
                if (invoke != null) {
                    String str = (String) invoke;
                    m = str;
                    return str;
                }
            } catch (Throwable th) {
                n = true;
            }
        } else if (u.c("IaHVhd2Vp").equalsIgnoreCase(Build.MANUFACTURER)) {
            try {
                Intent intent = new Intent();
                intent.setAction(u.c("WY29tLnVvZGlzLm9wZW5kZXZpY2UuT1BFTklEU19TRVJWSUNF"));
                intent.setPackage(u.c("UY29tLmh1YXdlaS5od2lk"));
                b bVar = new b();
                if (context.bindService(intent, bVar, 1)) {
                    while (i2 < 100 && TextUtils.isEmpty(m)) {
                        i2++;
                        Thread.sleep(15);
                    }
                    context.unbindService(bVar);
                }
                return m;
            } catch (Throwable th2) {
                y.a(th2, "oa", "hw");
            }
        } else {
            "OPPO".equalsIgnoreCase(Build.MANUFACTURER);
            n = true;
        }
        return m;
    }

    private static String G(Context context) throws InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if (y != null && !"".equals(y)) {
            return y;
        }
        if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return y;
        }
        TelephonyManager L = L(context);
        if (L == null) {
            return "";
        }
        Method a2 = u.a((Class) L.getClass(), "UZ2V0U3Vic2NyaWJlcklk", (Class<?>[]) new Class[0]);
        if (a2 != null) {
            y = (String) a2.invoke(L, new Object[0]);
        }
        if (y == null) {
            y = "";
        }
        return y;
    }

    private static String H(Context context) {
        if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return null;
        }
        TelephonyManager L = L(context);
        if (L == null) {
            return "";
        }
        String simOperatorName = L.getSimOperatorName();
        return TextUtils.isEmpty(simOperatorName) ? L.getNetworkOperatorName() : simOperatorName;
    }

    private static int I(Context context) {
        ConnectivityManager J;
        NetworkInfo activeNetworkInfo;
        if (context == null || !b(context, u.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) || (J = J(context)) == null || (activeNetworkInfo = J.getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    private static ConnectivityManager J(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    private static int K(Context context) {
        TelephonyManager L;
        if (b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) && (L = L(context)) != null) {
            return L.getNetworkType();
        }
        return -1;
    }

    private static TelephonyManager L(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    private static String M(Context context) {
        String str = null;
        try {
            str = N(context);
        } catch (Throwable th) {
        }
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            byte[] bytes = u.c("MAAAAAAAAAAAAAAAAAAAAAA").getBytes("UTF-8");
            return new String(o.a(u.c("HYW1hcGFkaXVhbWFwYWRpdWFtYXBhZGl1YW1hcGFkaXU").getBytes("UTF-8"), o.b(str), bytes), "UTF-8");
        } catch (Throwable th2) {
            return "";
        }
    }

    private static String N(Context context) {
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        String[] split;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        String a2 = r.a(u.c("LYW1hcF9kZXZpY2VfYWRpdQ"));
        String O = O(context);
        if (TextUtils.isEmpty(O)) {
            return "";
        }
        File file = new File(O + File.separator + u.c("KYmFja3Vwcw"), u.c("MLmFkaXU"));
        if (!file.exists() || !file.canRead()) {
            return "";
        }
        if (file.length() == 0) {
            file.delete();
            return "";
        }
        try {
            randomAccessFile2 = new RandomAccessFile(file, UploadQueueMgr.MSGTYPE_REALTIME);
            try {
                byte[] bArr = new byte[1024];
                byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    try {
                        int read = randomAccessFile2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    } catch (Throwable th2) {
                        th = th2;
                        a((Closeable) byteArrayOutputStream);
                        a((Closeable) randomAccessFile2);
                        throw th;
                    }
                }
                String str = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                if (TextUtils.isEmpty(str) || !str.contains(u.c("SIw")) || (split = str.split(u.c("SIw"))) == null || split.length != 2 || !TextUtils.equals(a2, split[0])) {
                    a((Closeable) byteArrayOutputStream);
                    a((Closeable) randomAccessFile2);
                    return "";
                }
                String str2 = split[1];
                a((Closeable) byteArrayOutputStream);
                a((Closeable) randomAccessFile2);
                return str2;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                byteArrayOutputStream = null;
                th = th4;
                a((Closeable) byteArrayOutputStream);
                a((Closeable) randomAccessFile2);
                throw th;
            }
        } catch (Throwable th5) {
            randomAccessFile2 = null;
            th = th5;
            byteArrayOutputStream = null;
            a((Closeable) byteArrayOutputStream);
            a((Closeable) randomAccessFile2);
            throw th;
        }
    }

    private static String O(Context context) {
        if (Build.VERSION.SDK_INT < 9) {
            return null;
        }
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            Class<?> cls = Class.forName(u.c("SYW5kcm9pZC5vcy5zdG9yYWdlLlN0b3JhZ2VWb2x1bWU"));
            Method method = storageManager.getClass().getMethod(u.c("MZ2V0Vm9sdW1lTGlzdA"), new Class[0]);
            Method method2 = cls.getMethod(u.c("FZ2V0UGF0aA"), new Class[0]);
            Method method3 = cls.getMethod(u.c("DaXNSZW1vdmFibGU"), new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i2 = 0; i2 < length; i2++) {
                Object obj = Array.get(invoke, i2);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (!((Boolean) method3.invoke(obj, new Object[0])).booleanValue()) {
                    return str;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static String a() {
        return j;
    }

    public static String a(final Context context) {
        try {
            if (!TextUtils.isEmpty(b)) {
                return b;
            }
            String M = M(context);
            b = M;
            if (!TextUtils.isEmpty(M)) {
                return b;
            }
            if (f == null) {
                return "";
            }
            ab.d().submit(new Runnable() {
                public final void run() {
                    if (!n.l) {
                        try {
                            boolean unused = n.l = true;
                            Map<String, String> c = n.f.c();
                            a aVar = n.f;
                            n.h(context);
                            n.A(context);
                            String d = aVar.d();
                            if (!TextUtils.isEmpty(d)) {
                                aw.a();
                                byte[] a2 = aw.a(new av(d.getBytes(), c));
                                a aVar2 = n.f;
                                new String(a2);
                                n.b = aVar2.b();
                            }
                        } catch (Throwable th) {
                            boolean unused2 = n.l = false;
                            throw th;
                        }
                        boolean unused3 = n.l = false;
                    }
                }
            });
            return "";
        } catch (Throwable th) {
        }
    }

    public static String a(Context context, String str) {
        int i2 = 0;
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT < 21) {
                return "";
            }
            if (!TextUtils.isEmpty(h)) {
                return h;
            }
            TelephonyManager L = L(context);
            if (g == -1) {
                Method a2 = u.a(TelephonyManager.class, "UZ2V0UGhvbmVDb3VudA=", (Class<?>[]) new Class[0]);
                if (a2 != null) {
                    g = ((Integer) a2.invoke(L, new Object[0])).intValue();
                } else {
                    g = 0;
                }
            }
            Class<TelephonyManager> cls = TelephonyManager.class;
            Method a3 = u.a((Class) cls, "MZ2V0SW1laQ=", (Class<?>[]) new Class[]{Integer.TYPE});
            if (a3 == null) {
                g = 0;
                return "";
            }
            StringBuilder sb = new StringBuilder();
            while (i2 < g) {
                try {
                    sb.append((String) a3.invoke(L, new Object[]{Integer.valueOf(i2)})).append(str);
                    i2++;
                } catch (Throwable th) {
                }
            }
            String sb2 = sb.toString();
            if (sb2.length() == 0) {
                g = 0;
                return "";
            }
            String substring = sb2.substring(0, sb2.length() - 1);
            h = substring;
            return substring;
        } catch (Throwable th2) {
            return "";
        }
    }

    private static List<ScanResult> a(List<ScanResult> list) {
        int size = list.size();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= size - 1) {
                return list;
            }
            int i4 = 1;
            while (true) {
                int i5 = i4;
                if (i5 >= size - i3) {
                    break;
                }
                if (list.get(i5 - 1).level > list.get(i5).level) {
                    list.set(i5 - 1, list.get(i5));
                    list.set(i5, list.get(i5 - 1));
                }
                i4 = i5 + 1;
            }
            i2 = i3 + 1;
        }
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    public static void a(String str) {
        j = str;
    }

    public static String b() {
        try {
            return !TextUtils.isEmpty(d) ? d : f == null ? "" : f.a();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String b(Context context) {
        try {
            return H(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static boolean b(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    public static String c(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            String A = A(context);
            return (A == null || A.length() < 5) ? "" : A.substring(3, 5);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static void c() {
        try {
            if (Build.VERSION.SDK_INT > 14) {
                TrafficStats.class.getDeclaredMethod("setThreadStatsTag", new Class[]{Integer.TYPE}).invoke((Object) null, new Object[]{40964});
            }
        } catch (Throwable th) {
        }
    }

    public static int d(Context context) {
        try {
            return K(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static long d() {
        long blockCount;
        long blockSize;
        if (v != 0) {
            return v;
        }
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                blockCount = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / 1048576;
                blockSize = (statFs2.getBlockSizeLong() * statFs2.getBlockCountLong()) / 1048576;
            } else {
                blockCount = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1048576;
                blockSize = (((long) statFs2.getBlockSize()) * ((long) statFs2.getBlockCount())) / 1048576;
            }
            v = blockSize + blockCount;
        } catch (Throwable th) {
        }
        return v;
    }

    public static int e(Context context) {
        try {
            return I(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static String e() {
        if (!TextUtils.isEmpty(x)) {
            return x;
        }
        String property = System.getProperty("os.arch");
        x = property;
        return property;
    }

    public static String f(Context context) {
        try {
            return G(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String g(final Context context) {
        try {
            if (n) {
                return "";
            }
            if (!TextUtils.isEmpty(m)) {
                return m;
            }
            if (Looper.getMainLooper() != Looper.myLooper()) {
                return F(context);
            }
            ab.d().submit(new Runnable() {
                public final void run() {
                    String unused = n.F(context);
                }
            });
            return m;
        } catch (Throwable th) {
        }
    }

    private static String h() {
        try {
            for (T t2 : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (t2.getName().equalsIgnoreCase("wlan0")) {
                    byte[] bArr = null;
                    if (Build.VERSION.SDK_INT >= 9) {
                        bArr = t2.getHardwareAddress();
                    }
                    if (bArr == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b2 : bArr) {
                        String upperCase = Integer.toHexString(b2 & OnReminderListener.RET_FULL).toUpperCase();
                        if (upperCase.length() == 1) {
                            sb.append("0");
                        }
                        sb.append(upperCase).append(SymbolExpUtil.SYMBOL_COLON);
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e2) {
        }
        return "";
    }

    public static String h(Context context) {
        try {
            if (a != null && !"".equals(a)) {
                return a;
            }
            if (b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFVFRJTkdT"))) {
                a = Settings.System.getString(context.getContentResolver(), "mqBRboGZkQPcAkyk");
            }
            if (a != null && !"".equals(a)) {
                return a;
            }
            try {
                a = E(context);
            } catch (Throwable th) {
            }
            return a == null ? "" : a;
        } catch (Throwable th2) {
        }
    }

    public static String i(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if (!TextUtils.isEmpty(p)) {
            return p;
        }
        if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                return (String) u.a(Build.class, "MZ2V0U2VyaWFs", (Class<?>[]) new Class[0]).invoke(Build.class, new Object[0]);
            }
            if (Build.VERSION.SDK_INT >= 9) {
                p = Build.SERIAL;
            }
            return p == null ? "" : p;
        } catch (Throwable th) {
        }
    }

    public static String j(Context context) {
        if (!TextUtils.isEmpty(o)) {
            return o;
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), u.c(new String(w.a(13))));
            o = string;
            return string == null ? "" : o;
        } catch (Throwable th) {
            return o;
        }
    }

    static String k(Context context) {
        String str;
        WifiManager wifiManager;
        if (context == null) {
            return "";
        }
        try {
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF")) || (wifiManager = (WifiManager) context.getSystemService("wifi")) == null) {
                return "";
            }
            if (wifiManager.isWifiEnabled()) {
                str = wifiManager.getConnectionInfo().getBSSID();
                return str;
            }
            str = "";
            return str;
        } catch (Throwable th) {
        }
    }

    static String l(Context context) {
        StringBuilder sb = new StringBuilder();
        if (context != null) {
            try {
                if (b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                    if (wifiManager == null) {
                        return "";
                    }
                    if (wifiManager.isWifiEnabled()) {
                        List<ScanResult> scanResults = wifiManager.getScanResults();
                        if (scanResults == null || scanResults.size() == 0) {
                            return sb.toString();
                        }
                        List<ScanResult> a2 = a(scanResults);
                        boolean z = true;
                        int i2 = 0;
                        while (i2 < a2.size() && i2 < 7) {
                            ScanResult scanResult = a2.get(i2);
                            if (z) {
                                z = false;
                            } else {
                                sb.append(SymbolExpUtil.SYMBOL_SEMICOLON);
                            }
                            sb.append(scanResult.BSSID);
                            i2++;
                        }
                    }
                    return sb.toString();
                }
            } catch (Throwable th) {
            }
        }
        return sb.toString();
    }

    public static String m(Context context) {
        try {
            if (q != null && !"".equals(q)) {
                return q;
            }
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
                return q;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null) {
                return "";
            }
            q = wifiManager.getConnectionInfo().getMacAddress();
            if (u.c("YMDI6MDA6MDA6MDA6MDA6MDA").equals(q) || u.c("YMDA6MDA6MDA6MDA6MDA6MDA").equals(q)) {
                q = h();
            }
            return q;
        } catch (Throwable th) {
        }
    }

    static String[] n(Context context) {
        try {
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) || !b(context, u.c("EYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19DT0FSU0VfTE9DQVRJT04="))) {
                return new String[]{"", ""};
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return new String[]{"", ""};
            }
            CellLocation cellLocation = telephonyManager.getCellLocation();
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                return new String[]{new StringBuilder().append(gsmCellLocation.getLac()).append(Constants.SEPARATOR).append(gsmCellLocation.getCid()).toString(), "gsm"};
            }
            if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                int systemId = cdmaCellLocation.getSystemId();
                int networkId = cdmaCellLocation.getNetworkId();
                return new String[]{new StringBuilder().append(systemId).append(Constants.SEPARATOR).append(networkId).append(Constants.SEPARATOR).append(cdmaCellLocation.getBaseStationId()).toString(), "cdma"};
            }
            return new String[]{"", ""};
        } catch (Throwable th) {
        }
    }

    static String o(Context context) {
        try {
            TelephonyManager L = L(context);
            if (L == null) {
                return "";
            }
            String networkOperator = L.getNetworkOperator();
            return (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) ? "" : networkOperator.substring(0, 3);
        } catch (Throwable th) {
            return "";
        }
    }

    static String p(Context context) {
        try {
            TelephonyManager L = L(context);
            if (L == null) {
                return "";
            }
            String networkOperator = L.getNetworkOperator();
            return (TextUtils.isEmpty(networkOperator) || networkOperator.length() < 3) ? "" : networkOperator.substring(3);
        } catch (Throwable th) {
            return "";
        }
    }

    public static int q(Context context) {
        try {
            return K(context);
        } catch (Throwable th) {
            return -1;
        }
    }

    public static int r(Context context) {
        try {
            return I(context);
        } catch (Throwable th) {
            return -1;
        }
    }

    public static NetworkInfo s(Context context) {
        ConnectivityManager J;
        if (b(context, u.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) && (J = J(context)) != null) {
            return J.getActiveNetworkInfo();
        }
        return null;
    }

    static String t(Context context) {
        try {
            NetworkInfo s2 = s(context);
            if (s2 == null) {
                return null;
            }
            return s2.getExtraInfo();
        } catch (Throwable th) {
            return null;
        }
    }

    static String u(Context context) {
        try {
            if (r != null && !"".equals(r)) {
                return r;
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager == null) {
                return "";
            }
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int i2 = displayMetrics.widthPixels;
            int i3 = displayMetrics.heightPixels;
            r = i3 > i2 ? i2 + "*" + i3 : i3 + "*" + i2;
            return r;
        } catch (Throwable th) {
        }
    }

    public static String v(Context context) {
        try {
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
                return y;
            }
            TelephonyManager L = L(context);
            return L == null ? "" : L.getNetworkOperatorName();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String w(Context context) {
        try {
            if (!b(context, u.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF"))) {
                return "";
            }
            ConnectivityManager J = J(context);
            if (J == null) {
                return "";
            }
            NetworkInfo activeNetworkInfo = J.getActiveNetworkInfo();
            return activeNetworkInfo == null ? "" : activeNetworkInfo.getTypeName();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String x(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                return "";
            }
            if (s != null && !"".equals(s)) {
                return s;
            }
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
                return s;
            }
            TelephonyManager L = L(context);
            if (L == null) {
                return "";
            }
            Method a2 = u.a((Class) L.getClass(), "QZ2V0RGV2aWNlSWQ", (Class<?>[]) new Class[0]);
            if (Build.VERSION.SDK_INT >= 26) {
                a2 = u.a((Class) L.getClass(), "QZ2V0SW1laQ==", (Class<?>[]) new Class[0]);
            }
            if (a2 != null) {
                s = (String) a2.invoke(L, new Object[0]);
            }
            if (s == null) {
                s = "";
            }
            return s;
        } catch (Throwable th) {
        }
    }

    public static String y(Context context) {
        return x(context) + Constant.INTENT_JSON_MARK + a(context);
    }

    public static String z(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (t != null && !"".equals(t)) {
                return t;
            }
            if (!b(context, u.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
                return t;
            }
            TelephonyManager L = L(context);
            if (L == null) {
                return "";
            }
            if (Build.VERSION.SDK_INT >= 26) {
                Method a2 = u.a((Class) L.getClass(), "QZ2V0TWVpZA==", (Class<?>[]) new Class[0]);
                if (a2 != null) {
                    t = (String) a2.invoke(L, new Object[0]);
                }
                if (t == null) {
                    t = "";
                }
            }
            return t;
        } catch (Throwable th) {
        }
    }
}
