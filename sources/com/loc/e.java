package com.loc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* compiled from: ApsManager */
public final class e {
    static boolean g = false;
    /* access modifiers changed from: private */
    public i A = null;
    /* access modifiers changed from: private */
    public List<Messenger> B;
    private boolean C = true;
    private String D = "";
    private final int E = 5000;
    private String F = "jsonp1";
    String a = null;
    b b = null;
    AMapLocation c = null;
    a d = null;
    Context e = null;
    dr f = null;
    HashMap<Messenger, Long> h = new HashMap<>();
    er i = null;
    long j = 0;
    long k = 0;
    String l = null;
    AMapLocationClientOption m = null;
    AMapLocationClientOption n = new AMapLocationClientOption();
    ServerSocket o = null;
    boolean p = false;
    Socket q = null;
    boolean r = false;
    c s = null;
    private boolean t = false;
    private boolean u = false;
    private long v = 0;
    private long w = 0;
    private dw x = null;
    private long y = 0;
    private int z = 0;

    /* compiled from: ApsManager */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x0087 A[SYNTHETIC, Splitter:B:31:0x0087] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0092 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x009d A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x00cc A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x00e0 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00eb A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x00f6 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x0102 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x010e A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:55:0x011a A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x0121 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x0128 A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x015c A[Catch:{ Throwable -> 0x00c1, Throwable -> 0x007c }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r8) {
            /*
                r7 = this;
                r3 = 1
                r2 = 0
                android.os.Bundle r1 = r8.getData()     // Catch:{ Throwable -> 0x0068 }
                android.os.Messenger r2 = r8.replyTo     // Catch:{ Throwable -> 0x019a }
                if (r1 == 0) goto L_0x0073
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x019a }
                if (r0 != 0) goto L_0x0073
                java.lang.String r0 = "c"
                java.lang.String r0 = r1.getString(r0)     // Catch:{ Throwable -> 0x019a }
                com.loc.e r4 = com.loc.e.this     // Catch:{ Throwable -> 0x019a }
                java.lang.String r5 = r4.l     // Catch:{ Throwable -> 0x019a }
                boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x019a }
                if (r5 == 0) goto L_0x0029
                android.content.Context r5 = r4.e     // Catch:{ Throwable -> 0x019a }
                java.lang.String r5 = com.loc.en.b(r5)     // Catch:{ Throwable -> 0x019a }
                r4.l = r5     // Catch:{ Throwable -> 0x019a }
            L_0x0029:
                boolean r5 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x019a }
                if (r5 != 0) goto L_0x0037
                java.lang.String r4 = r4.l     // Catch:{ Throwable -> 0x019a }
                boolean r0 = r0.equals(r4)     // Catch:{ Throwable -> 0x019a }
                if (r0 != 0) goto L_0x0066
            L_0x0037:
                r0 = 0
            L_0x0038:
                if (r0 != 0) goto L_0x0073
                int r0 = r8.what     // Catch:{ Throwable -> 0x019a }
                if (r0 != r3) goto L_0x0065
                r0 = 0
                r4 = 2102(0x836, float:2.946E-42)
                com.loc.er.a((java.lang.String) r0, (int) r4)     // Catch:{ Throwable -> 0x019a }
                java.lang.String r0 = "invalid handlder scode!!!#1002"
                com.loc.dw r0 = com.loc.e.a(10, (java.lang.String) r0)     // Catch:{ Throwable -> 0x019a }
                com.loc.dq r4 = new com.loc.dq     // Catch:{ Throwable -> 0x019a }
                r4.<init>()     // Catch:{ Throwable -> 0x019a }
                java.lang.String r5 = "#1002"
                r4.f(r5)     // Catch:{ Throwable -> 0x019a }
                java.lang.String r5 = "conitue"
                r4.e(r5)     // Catch:{ Throwable -> 0x019a }
                com.loc.e r5 = com.loc.e.this     // Catch:{ Throwable -> 0x019a }
                java.lang.String r6 = r0.k()     // Catch:{ Throwable -> 0x019a }
                r5.a(r2, r0, r6, r4)     // Catch:{ Throwable -> 0x019a }
            L_0x0065:
                return
            L_0x0066:
                r0 = r3
                goto L_0x0038
            L_0x0068:
                r0 = move-exception
                r1 = r2
            L_0x006a:
                java.lang.String r4 = "ApsServiceCore"
                java.lang.String r5 = "ActionHandler handlerMessage"
                com.loc.en.a(r0, r4, r5)     // Catch:{ Throwable -> 0x007c }
            L_0x0073:
                int r0 = r8.what     // Catch:{ Throwable -> 0x007c }
                switch(r0) {
                    case 0: goto L_0x0087;
                    case 1: goto L_0x0092;
                    case 2: goto L_0x009d;
                    case 3: goto L_0x00cc;
                    case 4: goto L_0x00e0;
                    case 5: goto L_0x00eb;
                    case 6: goto L_0x0078;
                    case 7: goto L_0x00f6;
                    case 8: goto L_0x0078;
                    case 9: goto L_0x0102;
                    case 10: goto L_0x010e;
                    case 11: goto L_0x011a;
                    case 12: goto L_0x0121;
                    case 13: goto L_0x0128;
                    case 14: goto L_0x015c;
                    default: goto L_0x0078;
                }     // Catch:{ Throwable -> 0x007c }
            L_0x0078:
                super.handleMessage(r8)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0065
            L_0x007c:
                r0 = move-exception
                java.lang.String r1 = "actionHandler"
                java.lang.String r2 = "handleMessage"
                com.loc.en.a(r0, r1, r2)
                goto L_0x0065
            L_0x0087:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.a((com.loc.e) r0, (android.os.Messenger) r2, (android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x0092:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.b(r0, r2, r1)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x009d:
                if (r1 == 0) goto L_0x0065
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x007c }
                if (r0 != 0) goto L_0x0065
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r1 = 0
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                boolean r1 = r0.r     // Catch:{ Throwable -> 0x00c1 }
                if (r1 != 0) goto L_0x0078
                com.loc.e$c r1 = new com.loc.e$c     // Catch:{ Throwable -> 0x00c1 }
                r1.<init>()     // Catch:{ Throwable -> 0x00c1 }
                r0.s = r1     // Catch:{ Throwable -> 0x00c1 }
                com.loc.e$c r1 = r0.s     // Catch:{ Throwable -> 0x00c1 }
                r1.start()     // Catch:{ Throwable -> 0x00c1 }
                r1 = 1
                r0.r = r1     // Catch:{ Throwable -> 0x00c1 }
                goto L_0x0078
            L_0x00c1:
                r0 = move-exception
                java.lang.String r1 = "ApsServiceCore"
                java.lang.String r2 = "startSocket"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x00cc:
                if (r1 == 0) goto L_0x0065
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x007c }
                if (r0 != 0) goto L_0x0065
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r1 = 0
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.b()     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x00e0:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.a((com.loc.e) r0)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x00eb:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.b((com.loc.e) r0)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x00f6:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.c(r0)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x0102:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.e.a((com.loc.e) r0, (android.os.Messenger) r2)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x010e:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.a((android.os.Messenger) r2, (android.os.Bundle) r1)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x011a:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.c()     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x0121:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.h.remove(r2)     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x0128:
                android.os.Messenger r0 = r8.replyTo     // Catch:{ Throwable -> 0x007c }
                if (r0 == 0) goto L_0x0078
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                if (r1 == 0) goto L_0x0078
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                boolean r1 = r1.contains(r0)     // Catch:{ Throwable -> 0x007c }
                if (r1 != 0) goto L_0x0078
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                r1.add(r0)     // Catch:{ Throwable -> 0x007c }
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r0 = r0.B     // Catch:{ Throwable -> 0x007c }
                int r0 = r0.size()     // Catch:{ Throwable -> 0x007c }
                if (r0 != r3) goto L_0x0078
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                r0.g()     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x015c:
                android.os.Messenger r0 = r8.replyTo     // Catch:{ Throwable -> 0x007c }
                if (r0 == 0) goto L_0x017d
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                if (r1 == 0) goto L_0x017d
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                boolean r1 = r1.contains(r0)     // Catch:{ Throwable -> 0x007c }
                if (r1 == 0) goto L_0x017d
                com.loc.e r1 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r1 = r1.B     // Catch:{ Throwable -> 0x007c }
                r1.remove(r0)     // Catch:{ Throwable -> 0x007c }
            L_0x017d:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r0 = r0.B     // Catch:{ Throwable -> 0x007c }
                if (r0 == 0) goto L_0x0078
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                java.util.List r0 = r0.B     // Catch:{ Throwable -> 0x007c }
                int r0 = r0.size()     // Catch:{ Throwable -> 0x007c }
                if (r0 != 0) goto L_0x0078
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x007c }
                com.loc.dr r0 = r0.f     // Catch:{ Throwable -> 0x007c }
                r0.i()     // Catch:{ Throwable -> 0x007c }
                goto L_0x0078
            L_0x019a:
                r0 = move-exception
                goto L_0x006a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.e.a.handleMessage(android.os.Message):void");
        }
    }

    /* compiled from: ApsManager */
    class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void onLooperPrepared() {
            /*
                r3 = this;
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x001b }
                com.loc.i r1 = new com.loc.i     // Catch:{ Throwable -> 0x001b }
                com.loc.e r2 = com.loc.e.this     // Catch:{ Throwable -> 0x001b }
                android.content.Context r2 = r2.e     // Catch:{ Throwable -> 0x001b }
                r1.<init>(r2)     // Catch:{ Throwable -> 0x001b }
                com.loc.i unused = r0.A = r1     // Catch:{ Throwable -> 0x001b }
            L_0x000e:
                com.loc.e r0 = com.loc.e.this     // Catch:{ Throwable -> 0x0026 }
                com.loc.dr r1 = new com.loc.dr     // Catch:{ Throwable -> 0x0026 }
                r1.<init>()     // Catch:{ Throwable -> 0x0026 }
                r0.f = r1     // Catch:{ Throwable -> 0x0026 }
                super.onLooperPrepared()     // Catch:{ Throwable -> 0x0026 }
            L_0x001a:
                return
            L_0x001b:
                r0 = move-exception
                java.lang.String r1 = "APSManager$ActionThread"
                java.lang.String r2 = "init 2"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0026 }
                goto L_0x000e
            L_0x0026:
                r0 = move-exception
                java.lang.String r1 = "APSManager$ActionThread"
                java.lang.String r2 = "onLooperPrepared"
                com.loc.en.a(r0, r1, r2)
                goto L_0x001a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.e.b.onLooperPrepared():void");
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
                en.a(th, "APSManager$ActionThread", "run");
            }
        }
    }

    /* compiled from: ApsManager */
    class c extends Thread {
        c() {
        }

        public final void run() {
            try {
                if (!e.this.p) {
                    e.this.p = true;
                    e.this.o = new ServerSocket(43689);
                }
                while (e.this.p && e.this.o != null) {
                    e.this.q = e.this.o.accept();
                    e.a(e.this, e.this.q);
                }
            } catch (Throwable th) {
                en.a(th, "ApsServiceCore", "run");
            }
            super.run();
        }
    }

    public e(Context context) {
        this.e = context;
    }

    /* access modifiers changed from: private */
    public static dw a(int i2, String str) {
        try {
            dw dwVar = new dw("");
            dwVar.setErrorCode(i2);
            dwVar.setLocationDetail(str);
            return dwVar;
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "newInstanceAMapLoc");
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void a(Bundle bundle) {
        try {
            if (!this.t) {
                en.a(this.e);
                if (bundle != null) {
                    this.n = en.a(bundle.getBundle("optBundle"));
                }
                this.f.a(this.e);
                this.f.a();
                a(this.n);
                this.f.b();
                this.t = true;
                this.C = true;
                this.D = "";
                if (this.B != null && this.B.size() > 0) {
                    g();
                }
            }
        } catch (Throwable th) {
            this.C = false;
            this.D = th.getMessage();
            en.a(th, "ApsServiceCore", "init");
        }
    }

    private void a(Messenger messenger) {
        try {
            this.f.b(this.e);
            if (em.i()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("ngpsAble", em.k());
                a(messenger, 7, bundle);
                em.j();
            }
            if (em.q()) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("installMockApp", true);
                a(messenger, 9, bundle2);
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "initAuth");
        }
    }

    private static void a(Messenger messenger, int i2, Bundle bundle) {
        if (messenger != null) {
            try {
                Message obtain = Message.obtain();
                obtain.setData(bundle);
                obtain.what = i2;
                messenger.send(obtain);
            } catch (Throwable th) {
                en.a(th, "ApsServiceCore", "sendMessage");
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Messenger messenger, AMapLocation aMapLocation, String str, dq dqVar) {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(AMapLocation.class.getClassLoader());
        bundle.putParcelable("loc", aMapLocation);
        bundle.putString("nb", str);
        bundle.putParcelable("statics", dqVar);
        this.h.put(messenger, Long.valueOf(et.b()));
        a(messenger, 1, bundle);
    }

    private void a(AMapLocationClientOption aMapLocationClientOption) {
        try {
            if (this.f != null) {
                this.f.a(aMapLocationClientOption);
            }
            if (aMapLocationClientOption != null) {
                g = aMapLocationClientOption.isKillProcess();
                if (!(this.m == null || (aMapLocationClientOption.isOffset() == this.m.isOffset() && aMapLocationClientOption.isNeedAddress() == this.m.isNeedAddress() && aMapLocationClientOption.isLocationCacheEnable() == this.m.isLocationCacheEnable() && this.m.getGeoLanguage() == aMapLocationClientOption.getGeoLanguage()))) {
                    this.w = 0;
                    this.c = null;
                }
                this.m = aMapLocationClientOption;
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "setExtra");
        }
    }

    static /* synthetic */ void a(e eVar) {
        try {
            if (eVar.z < em.b()) {
                eVar.z++;
                eVar.f.d();
                eVar.d.sendEmptyMessageDelayed(4, AbstractClientManager.BIND_SERVICE_TIMEOUT);
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "doGpsFusion");
        }
    }

    static /* synthetic */ void a(e eVar, Messenger messenger) {
        try {
            eVar.a(messenger);
            em.d(eVar.e);
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "doCallOtherSer");
        }
    }

    static /* synthetic */ void a(e eVar, Messenger messenger, Bundle bundle) {
        if (bundle != null) {
            try {
                if (!bundle.isEmpty() && !eVar.u) {
                    eVar.u = true;
                    eVar.a(messenger);
                    em.d(eVar.e);
                    try {
                        eVar.f.f();
                    } catch (Throwable th) {
                    }
                    eVar.e();
                    if (em.a(eVar.y) && "1".equals(bundle.getString("isCacheLoc"))) {
                        eVar.y = et.b();
                        eVar.f.d();
                    }
                }
            } catch (Throwable th2) {
                en.a(th2, "ApsServiceCore", "doInitAuth");
            }
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:92:0x0130=Splitter:B:92:0x0130, B:75:0x0101=Splitter:B:75:0x0101, B:33:0x0063=Splitter:B:33:0x0063, B:64:0x00e8=Splitter:B:64:0x00e8} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void a(com.loc.e r6, java.net.Socket r7) {
        /*
            r2 = 0
            if (r7 != 0) goto L_0x0004
        L_0x0003:
            return
        L_0x0004:
            int r3 = com.loc.en.g     // Catch:{ Throwable -> 0x0035 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.io.InputStream r4 = r7.getInputStream()     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            java.lang.String r5 = "UTF-8"
            r0.<init>(r4, r5)     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x006f, all -> 0x00f4 }
            r6.a((java.io.BufferedReader) r1)     // Catch:{ Throwable -> 0x013e }
            java.lang.String r0 = r6.f()     // Catch:{ Throwable -> 0x013e }
            com.loc.en.g = r3     // Catch:{ Throwable -> 0x0035 }
            r6.b((java.lang.String) r0)     // Catch:{ Throwable -> 0x0040 }
            r1.close()     // Catch:{ Throwable -> 0x002a }
            r7.close()     // Catch:{ Throwable -> 0x002a }
            goto L_0x0003
        L_0x002a:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "inSocetLn part3"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x0035:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "inSocetLn part4"
            com.loc.en.a(r0, r1, r2)
            goto L_0x0003
        L_0x0040:
            r0 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part2"
            com.loc.en.a(r0, r2, r3)     // Catch:{ all -> 0x005c }
            r1.close()     // Catch:{ Throwable -> 0x0051 }
            r7.close()     // Catch:{ Throwable -> 0x0051 }
            goto L_0x0003
        L_0x0051:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "inSocetLn part3"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x005c:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x0064 }
            r7.close()     // Catch:{ Throwable -> 0x0064 }
        L_0x0063:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0064:
            r1 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part3"
            com.loc.en.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0063
        L_0x006f:
            r0 = move-exception
            r1 = r2
        L_0x0071:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x013c }
            r4.<init>()     // Catch:{ all -> 0x013c }
            java.lang.String r5 = r6.F     // Catch:{ all -> 0x013c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r5 = "&&"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r5 = r6.F     // Catch:{ all -> 0x013c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r5 = "({'package':'"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r5 = r6.a     // Catch:{ all -> 0x013c }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r5 = "','error_code':1,'error':'params error'})"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x013c }
            java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x013c }
            java.lang.String r4 = "apm"
            java.lang.String r5 = "inSocetLn"
            com.loc.en.a(r0, r4, r5)     // Catch:{ all -> 0x013c }
            com.loc.en.g = r3     // Catch:{ Throwable -> 0x0035 }
            r6.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x00c3 }
            r1.close()     // Catch:{ Throwable -> 0x00b7 }
            r7.close()     // Catch:{ Throwable -> 0x00b7 }
            goto L_0x0003
        L_0x00b7:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "inSocetLn part3"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x00c3:
            r0 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part2"
            com.loc.en.a(r0, r2, r3)     // Catch:{ all -> 0x00e1 }
            r1.close()     // Catch:{ Throwable -> 0x00d5 }
            r7.close()     // Catch:{ Throwable -> 0x00d5 }
            goto L_0x0003
        L_0x00d5:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "inSocetLn part3"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0003
        L_0x00e1:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x00e9 }
            r7.close()     // Catch:{ Throwable -> 0x00e9 }
        L_0x00e8:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x00e9:
            r1 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part3"
            com.loc.en.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x00e8
        L_0x00f4:
            r0 = move-exception
            r1 = r2
        L_0x00f6:
            com.loc.en.g = r3     // Catch:{ Throwable -> 0x0035 }
            r6.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x010d }
            r1.close()     // Catch:{ Throwable -> 0x0102 }
            r7.close()     // Catch:{ Throwable -> 0x0102 }
        L_0x0101:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0102:
            r1 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part3"
            com.loc.en.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0101
        L_0x010d:
            r2 = move-exception
            java.lang.String r3 = "apm"
            java.lang.String r4 = "inSocetLn part2"
            com.loc.en.a(r2, r3, r4)     // Catch:{ all -> 0x0129 }
            r1.close()     // Catch:{ Throwable -> 0x011e }
            r7.close()     // Catch:{ Throwable -> 0x011e }
            goto L_0x0101
        L_0x011e:
            r1 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part3"
            com.loc.en.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0101
        L_0x0129:
            r0 = move-exception
            r1.close()     // Catch:{ Throwable -> 0x0131 }
            r7.close()     // Catch:{ Throwable -> 0x0131 }
        L_0x0130:
            throw r0     // Catch:{ Throwable -> 0x0035 }
        L_0x0131:
            r1 = move-exception
            java.lang.String r2 = "apm"
            java.lang.String r3 = "inSocetLn part3"
            com.loc.en.a(r1, r2, r3)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x0130
        L_0x013c:
            r0 = move-exception
            goto L_0x00f6
        L_0x013e:
            r0 = move-exception
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.e.a(com.loc.e, java.net.Socket):void");
    }

    private void a(BufferedReader bufferedReader) throws Exception {
        String[] split;
        String[] split2;
        String[] split3;
        String readLine = bufferedReader.readLine();
        int i2 = 30000;
        if (readLine != null && readLine.length() > 0 && (split = readLine.split(" ")) != null && split.length > 1 && (split2 = split[1].split("\\?")) != null && split2.length > 1 && (split3 = split2[1].split("&")) != null && split3.length > 0) {
            int i3 = 30000;
            for (String split4 : split3) {
                String[] split5 = split4.split("=");
                if (split5 != null && split5.length > 1) {
                    if ("to".equals(split5[0])) {
                        i3 = et.g(split5[1]);
                    }
                    if ("callback".equals(split5[0])) {
                        this.F = split5[1];
                    }
                }
            }
            i2 = i3;
        }
        en.g = i2;
    }

    private AMapLocationClientOption b(Bundle bundle) {
        AMapLocationClientOption a2 = en.a(bundle.getBundle("optBundle"));
        a(a2);
        try {
            String string = bundle.getString("d");
            if (!TextUtils.isEmpty(string)) {
                n.a(string);
            }
        } catch (Throwable th) {
            en.a(th, "APSManager", "parseBundle");
        }
        return a2;
    }

    static /* synthetic */ void b(e eVar) {
        try {
            if (em.e()) {
                eVar.f.d();
            } else if (!et.e(eVar.e)) {
                eVar.f.d();
            }
            eVar.d.sendEmptyMessageDelayed(5, (long) (em.d() * 1000));
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "doOffFusion");
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void b(com.loc.e r11, android.os.Messenger r12, android.os.Bundle r13) {
        /*
            r10 = 9
            r1 = 0
            if (r13 == 0) goto L_0x000b
            boolean r0 = r13.isEmpty()     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x000c
        L_0x000b:
            return
        L_0x000c:
            com.loc.dq r3 = new com.loc.dq     // Catch:{ Throwable -> 0x007e }
            r3.<init>()     // Catch:{ Throwable -> 0x007e }
            java.lang.String r0 = "conitue"
            r3.e(r0)     // Catch:{ Throwable -> 0x007e }
            com.amap.api.location.AMapLocationClientOption r4 = r11.b((android.os.Bundle) r13)     // Catch:{ Throwable -> 0x007e }
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r11.h     // Catch:{ Throwable -> 0x007e }
            boolean r0 = r0.containsKey(r12)     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x0041
            boolean r0 = r4.isOnceLocation()     // Catch:{ Throwable -> 0x007e }
            if (r0 != 0) goto L_0x0041
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r11.h     // Catch:{ Throwable -> 0x007e }
            java.lang.Object r0 = r0.get(r12)     // Catch:{ Throwable -> 0x007e }
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ Throwable -> 0x007e }
            long r6 = r0.longValue()     // Catch:{ Throwable -> 0x007e }
            long r8 = com.loc.et.b()     // Catch:{ Throwable -> 0x007e }
            long r6 = r8 - r6
            r8 = 800(0x320, double:3.953E-321)
            int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r0 < 0) goto L_0x000b
        L_0x0041:
            boolean r0 = r11.C     // Catch:{ Throwable -> 0x007e }
            if (r0 != 0) goto L_0x0089
            r0 = 9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x007e }
            java.lang.String r2 = "init error : "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r2 = r11.D     // Catch:{ Throwable -> 0x007e }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r2 = "#0901"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r0 = a((int) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x007e }
            r11.x = r0     // Catch:{ Throwable -> 0x007e }
            java.lang.String r0 = "#0901"
            r3.f(r0)     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r1 = r11.x     // Catch:{ Throwable -> 0x007e }
            java.lang.String r1 = r1.k()     // Catch:{ Throwable -> 0x007e }
            r11.a(r12, r0, r1, r3)     // Catch:{ Throwable -> 0x007e }
            r0 = 0
            r1 = 2091(0x82b, float:2.93E-42)
            com.loc.er.a((java.lang.String) r0, (int) r1)     // Catch:{ Throwable -> 0x007e }
            goto L_0x000b
        L_0x007e:
            r0 = move-exception
            java.lang.String r1 = "ApsServiceCore"
            java.lang.String r2 = "doLocation"
            com.loc.en.a(r0, r1, r2)
            goto L_0x000b
        L_0x0089:
            long r6 = com.loc.et.b()     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            boolean r0 = com.loc.et.a((com.loc.dw) r0)     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x00e8
            long r8 = r11.w     // Catch:{ Throwable -> 0x007e }
            long r6 = r6 - r8
            r8 = 600(0x258, double:2.964E-321)
            int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r0 >= 0) goto L_0x00e8
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r1 = r11.x     // Catch:{ Throwable -> 0x007e }
            java.lang.String r1 = r1.k()     // Catch:{ Throwable -> 0x007e }
            r11.a(r12, r0, r1, r3)     // Catch:{ Throwable -> 0x007e }
        L_0x00a9:
            r11.a((android.os.Messenger) r12)     // Catch:{ Throwable -> 0x007e }
            boolean r0 = com.loc.em.r()     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x00b5
            r11.e()     // Catch:{ Throwable -> 0x007e }
        L_0x00b5:
            long r0 = r11.y     // Catch:{ Throwable -> 0x007e }
            boolean r0 = com.loc.em.a((long) r0)     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x000b
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x000b
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x007e }
            r1 = 2
            if (r0 == r1) goto L_0x00db
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x007e }
            r1 = 4
            if (r0 == r1) goto L_0x00db
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x007e }
            if (r0 != r10) goto L_0x000b
        L_0x00db:
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x007e }
            r11.y = r0     // Catch:{ Throwable -> 0x007e }
            com.loc.dr r0 = r11.f     // Catch:{ Throwable -> 0x007e }
            r0.d()     // Catch:{ Throwable -> 0x007e }
            goto L_0x000b
        L_0x00e8:
            long r6 = com.loc.et.b()     // Catch:{ Throwable -> 0x007e }
            r3.c((long) r6)     // Catch:{ Throwable -> 0x007e }
            com.loc.dr r0 = r11.f     // Catch:{ Throwable -> 0x015c }
            com.loc.dw r0 = r0.a((com.loc.dq) r3)     // Catch:{ Throwable -> 0x015c }
            r11.x = r0     // Catch:{ Throwable -> 0x015c }
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x015c }
            int r0 = r0.getLocationType()     // Catch:{ Throwable -> 0x015c }
            r2 = 6
            if (r0 == r2) goto L_0x0105
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x015c }
            r0.getLocationType()     // Catch:{ Throwable -> 0x015c }
        L_0x0105:
            com.loc.dr r0 = r11.f     // Catch:{ Throwable -> 0x015c }
            com.loc.dw r2 = r11.x     // Catch:{ Throwable -> 0x015c }
            com.loc.dw r0 = r0.a((com.loc.dw) r2)     // Catch:{ Throwable -> 0x015c }
            r11.x = r0     // Catch:{ Throwable -> 0x015c }
        L_0x010f:
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            boolean r0 = com.loc.et.a((com.loc.dw) r0)     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x011d
            long r6 = com.loc.et.b()     // Catch:{ Throwable -> 0x007e }
            r11.w = r6     // Catch:{ Throwable -> 0x007e }
        L_0x011d:
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            if (r0 != 0) goto L_0x0132
            r0 = 8
            java.lang.String r2 = "loc is null#0801"
            com.loc.dw r0 = a((int) r0, (java.lang.String) r2)     // Catch:{ Throwable -> 0x007e }
            r11.x = r0     // Catch:{ Throwable -> 0x007e }
            java.lang.String r0 = "#0801"
            r3.f(r0)     // Catch:{ Throwable -> 0x007e }
        L_0x0132:
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            if (r0 == 0) goto L_0x01a2
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            java.lang.String r1 = r0.k()     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r0 = r11.x     // Catch:{ Throwable -> 0x007e }
            com.amap.api.location.AMapLocation r0 = r0.clone()     // Catch:{ Throwable -> 0x007e }
            r2 = r1
        L_0x0143:
            boolean r1 = r4.isLocationCacheEnable()     // Catch:{ Throwable -> 0x0197 }
            if (r1 == 0) goto L_0x0157
            com.loc.i r1 = r11.A     // Catch:{ Throwable -> 0x0197 }
            if (r1 == 0) goto L_0x0157
            com.loc.i r1 = r11.A     // Catch:{ Throwable -> 0x0197 }
            long r4 = r4.getLastLocationLifeCycle()     // Catch:{ Throwable -> 0x0197 }
            com.amap.api.location.AMapLocation r0 = r1.a(r0, r2, r4)     // Catch:{ Throwable -> 0x0197 }
        L_0x0157:
            r11.a(r12, r0, r2, r3)     // Catch:{ Throwable -> 0x007e }
            goto L_0x00a9
        L_0x015c:
            r0 = move-exception
            r2 = 0
            r5 = 2081(0x821, float:2.916E-42)
            com.loc.er.a((java.lang.String) r2, (int) r5)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r2 = "#0801"
            r3.f(r2)     // Catch:{ Throwable -> 0x007e }
            r2 = 8
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x007e }
            java.lang.String r6 = "loc error : "
            r5.<init>(r6)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r6 = r0.getMessage()     // Catch:{ Throwable -> 0x007e }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r6 = "#0801"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x007e }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x007e }
            com.loc.dw r2 = a((int) r2, (java.lang.String) r5)     // Catch:{ Throwable -> 0x007e }
            r11.x = r2     // Catch:{ Throwable -> 0x007e }
            java.lang.String r2 = "ApsServiceCore"
            java.lang.String r5 = "run part2"
            com.loc.en.a(r0, r2, r5)     // Catch:{ Throwable -> 0x007e }
            goto L_0x010f
        L_0x0197:
            r1 = move-exception
            java.lang.String r4 = "ApsServiceCore"
            java.lang.String r5 = "fixLastLocation"
            com.loc.en.a(r1, r4, r5)     // Catch:{ Throwable -> 0x007e }
            goto L_0x0157
        L_0x01a2:
            r0 = r1
            r2 = r1
            goto L_0x0143
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.e.b(com.loc.e, android.os.Messenger, android.os.Bundle):void");
    }

    private void b(String str) throws UnsupportedEncodingException, IOException {
        PrintStream printStream = new PrintStream(this.q.getOutputStream(), true, "UTF-8");
        printStream.println("HTTP/1.0 200 OK");
        printStream.println("Content-Length:" + str.getBytes("UTF-8").length);
        printStream.println();
        printStream.println(str);
        printStream.close();
    }

    static /* synthetic */ void c(e eVar) {
        try {
            if (em.a(eVar.e, eVar.v)) {
                eVar.v = et.a();
                eVar.f.d();
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "doNGps");
        }
    }

    public static void d() {
        g = false;
    }

    private void e() {
        try {
            this.d.removeMessages(4);
            if (em.a()) {
                this.d.sendEmptyMessage(4);
            }
            this.d.removeMessages(5);
            if (em.c() && em.d() > 2) {
                this.d.sendEmptyMessage(5);
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "checkConfig");
        }
    }

    private String f() {
        long currentTimeMillis = System.currentTimeMillis();
        if (et.e(this.e)) {
            return this.F + "&&" + this.F + "({'package':'" + this.a + "','error_code':36,'error':'app is background'})";
        }
        if (this.x == null || currentTimeMillis - this.x.getTime() > 5000) {
            try {
                this.x = this.f.a(new dq());
            } catch (Throwable th) {
                en.a(th, "ApsServiceCore", "getSocketLocResult");
            }
        }
        return this.x == null ? this.F + "&&" + this.F + "({'package':'" + this.a + "','error_code':8,'error':'unknown error'})" : this.x.getErrorCode() != 0 ? this.F + "&&" + this.F + "({'package':'" + this.a + "','error_code':" + this.x.getErrorCode() + ",'error':'" + this.x.getErrorInfo() + "'})" : this.F + "&&" + this.F + "({'package':'" + this.a + "','error_code':0,'error':'','location':{'y':" + this.x.getLatitude() + ",'precision':" + this.x.getAccuracy() + ",'x':" + this.x.getLongitude() + "},'version_code':'4.9.0','version':'4.9.0'})";
    }

    /* access modifiers changed from: private */
    public void g() {
        try {
            if (this.f != null && this.f != null) {
                this.f.a((Handler) this.d);
                this.f.h();
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "startColl");
        }
    }

    public final void a() {
        try {
            this.i = new er();
            this.b = new b("amapLocCoreThread");
            this.b.setPriority(5);
            this.b.start();
            this.d = new a(this.b.getLooper());
            this.B = new ArrayList();
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "onCreate");
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(Messenger messenger, Bundle bundle) {
        float f2;
        if (bundle != null) {
            try {
                if (!bundle.isEmpty() && em.n()) {
                    double d2 = bundle.getDouble("lat");
                    double d3 = bundle.getDouble("lon");
                    b(bundle);
                    if (this.c != null) {
                        f2 = et.a(new double[]{d2, d3, this.c.getLatitude(), this.c.getLongitude()});
                        if (f2 < ((float) (em.o() * 3))) {
                            Bundle bundle2 = new Bundle();
                            bundle2.setClassLoader(AMapLocation.class.getClassLoader());
                            bundle2.putInt("I_MAX_GEO_DIS", em.o() * 3);
                            bundle2.putInt("I_MIN_GEO_DIS", em.o());
                            bundle2.putParcelable("loc", this.c);
                            a(messenger, 6, bundle2);
                        }
                    } else {
                        f2 = -1.0f;
                    }
                    if (f2 == -1.0f || f2 > ((float) em.o())) {
                        a(bundle);
                        this.c = this.f.a(d2, d3);
                    }
                }
            } catch (Throwable th) {
                en.a(th, "ApsServiceCore", "doLocationGeo");
            }
        }
    }

    public final void b() {
        try {
            if (this.q != null) {
                this.q.close();
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "doStopScocket 1");
        }
        try {
            if (this.o != null) {
                this.o.close();
            }
        } catch (Throwable th2) {
            en.a(th2, "ApsServiceCore", "doStopScocket 2");
        }
        try {
            if (this.s != null) {
                this.s.interrupt();
            }
        } catch (Throwable th3) {
        }
        this.s = null;
        this.o = null;
        this.p = false;
        this.r = false;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void c() {
        /*
            r8 = this;
            r4 = 0
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r8.h     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x000e
            java.util.HashMap<android.os.Messenger, java.lang.Long> r0 = r8.h     // Catch:{ Throwable -> 0x00b9 }
            r0.clear()     // Catch:{ Throwable -> 0x00b9 }
            r0 = 0
            r8.h = r0     // Catch:{ Throwable -> 0x00b9 }
        L_0x000e:
            java.util.List<android.os.Messenger> r0 = r8.B     // Catch:{ Throwable -> 0x00ad }
            if (r0 == 0) goto L_0x0017
            java.util.List<android.os.Messenger> r0 = r8.B     // Catch:{ Throwable -> 0x00ad }
            r0.clear()     // Catch:{ Throwable -> 0x00ad }
        L_0x0017:
            com.loc.dr r0 = r8.f     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x0022
            com.loc.dr r0 = r8.f     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            r0.b((android.content.Context) r1)     // Catch:{ Throwable -> 0x00b9 }
        L_0x0022:
            com.loc.e$a r0 = r8.d     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x002c
            com.loc.e$a r0 = r8.d     // Catch:{ Throwable -> 0x00b9 }
            r1 = 0
            r0.removeCallbacksAndMessages(r1)     // Catch:{ Throwable -> 0x00b9 }
        L_0x002c:
            com.loc.e$b r0 = r8.b     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x0043
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00b9 }
            r1 = 18
            if (r0 < r1) goto L_0x00cc
            com.loc.e$b r0 = r8.b     // Catch:{ Throwable -> 0x00c4 }
            java.lang.Class<android.os.HandlerThread> r1 = android.os.HandlerThread.class
            java.lang.String r2 = "quitSafely"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x00c4 }
            com.loc.eq.a((java.lang.Object) r0, (java.lang.Class<?>) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x00c4 }
        L_0x0043:
            r0 = 0
            r8.b = r0     // Catch:{ Throwable -> 0x00b9 }
            r0 = 0
            r8.d = r0     // Catch:{ Throwable -> 0x00b9 }
            com.loc.i r0 = r8.A     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x0055
            com.loc.i r0 = r8.A     // Catch:{ Throwable -> 0x00b9 }
            r0.c()     // Catch:{ Throwable -> 0x00b9 }
            r0 = 0
            r8.A = r0     // Catch:{ Throwable -> 0x00b9 }
        L_0x0055:
            r8.b()     // Catch:{ Throwable -> 0x00b9 }
            r0 = 0
            r8.t = r0     // Catch:{ Throwable -> 0x00b9 }
            r0 = 0
            r8.u = r0     // Catch:{ Throwable -> 0x00b9 }
            com.loc.dr r0 = r8.f     // Catch:{ Throwable -> 0x00b9 }
            r0.e()     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r0 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            com.loc.er.a(r0)     // Catch:{ Throwable -> 0x00b9 }
            com.loc.er r0 = r8.i     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x009e
            long r0 = r8.j     // Catch:{ Throwable -> 0x00b9 }
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x009e
            long r0 = r8.k     // Catch:{ Throwable -> 0x00b9 }
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x009e
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x00b9 }
            long r2 = r8.j     // Catch:{ Throwable -> 0x00b9 }
            long r6 = r0 - r2
            com.loc.er r0 = r8.i     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            int r2 = r0.c(r1)     // Catch:{ Throwable -> 0x00b9 }
            com.loc.er r0 = r8.i     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            int r3 = r0.d(r1)     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            long r4 = r8.k     // Catch:{ Throwable -> 0x00b9 }
            com.loc.er.a(r1, r2, r3, r4, r6)     // Catch:{ Throwable -> 0x00b9 }
            com.loc.er r0 = r8.i     // Catch:{ Throwable -> 0x00b9 }
            android.content.Context r1 = r8.e     // Catch:{ Throwable -> 0x00b9 }
            r0.e(r1)     // Catch:{ Throwable -> 0x00b9 }
        L_0x009e:
            com.loc.ab.b()     // Catch:{ Throwable -> 0x00b9 }
            boolean r0 = g     // Catch:{ Throwable -> 0x00b9 }
            if (r0 == 0) goto L_0x00ac
            int r0 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x00b9 }
            android.os.Process.killProcess(r0)     // Catch:{ Throwable -> 0x00b9 }
        L_0x00ac:
            return
        L_0x00ad:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "des1"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x0017
        L_0x00b9:
            r0 = move-exception
            java.lang.String r1 = "apm"
            java.lang.String r2 = "tdest"
            com.loc.en.a(r0, r1, r2)
            goto L_0x00ac
        L_0x00c4:
            r0 = move-exception
            com.loc.e$b r0 = r8.b     // Catch:{ Throwable -> 0x00b9 }
            r0.quit()     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x0043
        L_0x00cc:
            com.loc.e$b r0 = r8.b     // Catch:{ Throwable -> 0x00b9 }
            r0.quit()     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.e.c():void");
    }
}
