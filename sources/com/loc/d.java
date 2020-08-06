package com.loc;

import android.app.Application;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.location.APSService;
import com.amap.api.location.UmidtokenInfo;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: AmapLocationManager */
public final class d {
    private static boolean C = true;
    /* access modifiers changed from: private */
    public static boolean E = false;
    /* access modifiers changed from: private */
    public boolean A = false;
    private volatile boolean B = false;
    /* access modifiers changed from: private */
    public boolean D = true;
    private h F = null;
    private ServiceConnection G = new ServiceConnection() {
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                d.this.h = new Messenger(iBinder);
                boolean unused = d.this.A = true;
                d.this.q = true;
            } catch (Throwable th) {
                en.a(th, "ALManager", "onServiceConnected");
            }
        }

        public final void onServiceDisconnected(ComponentName componentName) {
            d.this.h = null;
            boolean unused = d.this.A = false;
        }
    };
    AMapLocationClientOption a = new AMapLocationClientOption();
    public c b;
    g c = null;
    ArrayList<AMapLocationListener> d = new ArrayList<>();
    boolean e = false;
    public boolean f = true;
    i g;
    Messenger h = null;
    Messenger i = null;
    Intent j = null;
    int k = 0;
    b l = null;
    boolean m = false;
    AMapLocationClientOption.AMapLocationMode n = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
    Object o = new Object();
    er p = null;
    boolean q = false;
    e r = null;
    String s = null;
    AMapLocationQualityReport t = null;
    boolean u = false;
    boolean v = false;
    a w = null;
    String x = null;
    boolean y = false;
    private Context z;

    /* compiled from: AmapLocationManager */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            String str;
            Throwable th;
            String str2;
            boolean z = false;
            try {
                super.handleMessage(message);
                switch (message.what) {
                    case 11:
                        try {
                            d.this.a(message.getData());
                            return;
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            str = str2;
                            th = th3;
                            break;
                        }
                    case 12:
                        str2 = "handleMessage ACTION_GPS_LOCATIONSUCCESS";
                        d.a(d.this, message);
                        return;
                    case 1002:
                        str2 = "handleMessage SET_LISTENER";
                        d.a(d.this, (AMapLocationListener) message.obj);
                        return;
                    case 1003:
                        str2 = "handleMessage START_LOCATION";
                        d.this.j();
                        d.this.a(13, (Bundle) null);
                        return;
                    case 1004:
                        str2 = "handleMessage STOP_LOCATION";
                        d.this.k();
                        d.this.a(14, (Bundle) null);
                        return;
                    case 1005:
                        str2 = "handleMessage REMOVE_LISTENER";
                        d.b(d.this, (AMapLocationListener) message.obj);
                        return;
                    case 1008:
                        str2 = "handleMessage START_SOCKET";
                        d.g(d.this);
                        return;
                    case 1009:
                        str2 = "handleMessage STOP_SOCKET";
                        d.h(d.this);
                        return;
                    case 1011:
                        d.this.a(14, (Bundle) null);
                        d.this.h();
                        return;
                    case PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW:
                        str2 = "handleMessage ACTION_SAVE_LAST_LOCATION";
                        d.b(d.this, message);
                        return;
                    case PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW:
                        str2 = "handleMessage START_GPS_LOCATION";
                        d.this.c.a(d.this.a);
                        d.this.a(1025, (Object) null, 300000);
                        return;
                    case PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW:
                        str2 = "handleMessage START_LBS_LOCATION";
                        if (d.this.c.b()) {
                            d.this.a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, 1000);
                            return;
                        } else {
                            d.d(d.this);
                            return;
                        }
                    case PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW:
                        str2 = "handleMessage STOP_GPS_LOCATION";
                        d.this.c.a();
                        d.this.a(1025);
                        return;
                    case PointerIconCompat.TYPE_ZOOM_IN:
                        str2 = "handleMessage SET_OPTION";
                        d.this.a = (AMapLocationClientOption) message.obj;
                        if (d.this.a != null) {
                            d.f(d.this);
                            return;
                        }
                        return;
                    case 1023:
                        str2 = "handleMessage ACTION_ENABLE_BACKGROUND";
                        d.c(d.this, message);
                        return;
                    case 1024:
                        str2 = "handleMessage ACTION_DISABLE_BACKGROUND";
                        d.d(d.this, message);
                        return;
                    case 1025:
                        str2 = "handleMessage ACTION_REBOOT_GPS_LOCATION";
                        g gVar = d.this.c;
                        if (gVar.c != null && !gVar.c.isOnceLocation() && et.b() - gVar.d > 300000) {
                            z = true;
                        }
                        if (z) {
                            d.this.c.a();
                            d.this.c.a(d.this.a);
                        }
                        d.this.a(1025, (Object) null, 300000);
                        return;
                    default:
                        return;
                }
            } catch (Throwable th4) {
                Throwable th5 = th4;
                str = null;
                th = th5;
            }
            if (str == null) {
                str = "handleMessage";
            }
            en.a(th, "AMapLocationManage$MHandlerr", str);
        }
    }

    /* compiled from: AmapLocationManager */
    static class b extends HandlerThread {
        d a = null;

        public b(String str, d dVar) {
            super(str);
            this.a = dVar;
        }

        /* access modifiers changed from: protected */
        public final void onLooperPrepared() {
            try {
                this.a.g.a();
                this.a.m();
                super.onLooperPrepared();
            } catch (Throwable th) {
            }
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
            }
        }
    }

    /* compiled from: AmapLocationManager */
    public class c extends Handler {
        public c() {
        }

        public c(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            r0 = android.os.Message.obtain();
            r0.what = 12;
            r0.obj = r6.obj;
            r5.a.w.sendMessage(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r6) {
            /*
                r5 = this;
                r1 = 0
                super.handleMessage(r6)     // Catch:{ Throwable -> 0x010c }
                com.loc.d r0 = com.loc.d.this     // Catch:{ Throwable -> 0x010c }
                boolean r0 = r0.m     // Catch:{ Throwable -> 0x010c }
                if (r0 == 0) goto L_0x0011
                boolean r0 = com.loc.en.e()     // Catch:{ Throwable -> 0x010c }
                if (r0 != 0) goto L_0x0011
            L_0x0010:
                return
            L_0x0011:
                int r0 = r6.what     // Catch:{ Throwable -> 0x010c }
                switch(r0) {
                    case 1: goto L_0x0017;
                    case 2: goto L_0x0058;
                    case 3: goto L_0x0010;
                    case 4: goto L_0x0016;
                    case 5: goto L_0x006f;
                    case 6: goto L_0x008c;
                    case 7: goto L_0x00e4;
                    case 8: goto L_0x0052;
                    case 9: goto L_0x00f9;
                    case 10: goto L_0x0045;
                    default: goto L_0x0016;
                }     // Catch:{ Throwable -> 0x010c }
            L_0x0016:
                goto L_0x0010
            L_0x0017:
                java.lang.String r1 = "handleMessage RESULT_LBS_LOCATIONSUCCESS"
                com.loc.d r0 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.d$a r0 = r0.w     // Catch:{ Throwable -> 0x0035 }
                android.os.Message r0 = r0.obtainMessage()     // Catch:{ Throwable -> 0x0035 }
                r2 = 11
                r0.what = r2     // Catch:{ Throwable -> 0x0035 }
                android.os.Bundle r2 = r6.getData()     // Catch:{ Throwable -> 0x0035 }
                r0.setData(r2)     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.d$a r2 = r2.w     // Catch:{ Throwable -> 0x0035 }
                r2.sendMessage(r0)     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x0035:
                r0 = move-exception
                r4 = r0
                r0 = r1
                r1 = r4
            L_0x0039:
                if (r0 != 0) goto L_0x003e
                java.lang.String r0 = "handleMessage"
            L_0x003e:
                java.lang.String r2 = "AmapLocationManager$MainHandler"
                com.loc.en.a(r1, r2, r0)
                goto L_0x0010
            L_0x0045:
                java.lang.String r1 = "handleMessage RESULT_LBS_ON_CALLBACK"
                java.lang.Object r0 = r6.obj     // Catch:{ Throwable -> 0x0035 }
                com.amap.api.location.AMapLocation r0 = (com.amap.api.location.AMapLocation) r0     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.d.a((com.loc.d) r2, (com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x0052:
                r0 = 0
                r2 = 2141(0x85d, float:3.0E-42)
                com.loc.er.a((java.lang.String) r0, (int) r2)     // Catch:{ Throwable -> 0x010c }
            L_0x0058:
                java.lang.String r1 = "handleMessage RESULT_GPS_LOCATIONSUCCESS"
                android.os.Message r0 = android.os.Message.obtain()     // Catch:{ Throwable -> 0x0035 }
                r2 = 12
                r0.what = r2     // Catch:{ Throwable -> 0x0035 }
                java.lang.Object r2 = r6.obj     // Catch:{ Throwable -> 0x0035 }
                r0.obj = r2     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.d$a r2 = r2.w     // Catch:{ Throwable -> 0x0035 }
                r2.sendMessage(r0)     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x006f:
                java.lang.String r1 = "handleMessage RESULT_GPS_LOCATIONCHANGE"
                android.os.Bundle r0 = r6.getData()     // Catch:{ Throwable -> 0x0035 }
                java.lang.String r2 = "optBundle"
                com.loc.d r3 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.amap.api.location.AMapLocationClientOption r3 = r3.a     // Catch:{ Throwable -> 0x0035 }
                android.os.Bundle r3 = com.loc.en.a((com.amap.api.location.AMapLocationClientOption) r3)     // Catch:{ Throwable -> 0x0035 }
                r0.putBundle(r2, r3)     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                r3 = 10
                r2.a((int) r3, (android.os.Bundle) r0)     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x008c:
                java.lang.String r1 = "handleMessage RESULT_GPS_GEO_SUCCESS"
                android.os.Bundle r0 = r6.getData()     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.g r2 = r2.c     // Catch:{ Throwable -> 0x0035 }
                if (r2 == 0) goto L_0x0010
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                com.loc.g r2 = r2.c     // Catch:{ Throwable -> 0x0035 }
                if (r0 == 0) goto L_0x0010
                java.lang.Class<com.amap.api.location.AMapLocation> r3 = com.amap.api.location.AMapLocation.class
                java.lang.ClassLoader r3 = r3.getClassLoader()     // Catch:{ Throwable -> 0x00d8 }
                r0.setClassLoader(r3)     // Catch:{ Throwable -> 0x00d8 }
                java.lang.String r3 = "I_MAX_GEO_DIS"
                int r3 = r0.getInt(r3)     // Catch:{ Throwable -> 0x00d8 }
                r2.g = r3     // Catch:{ Throwable -> 0x00d8 }
                java.lang.String r3 = "I_MIN_GEO_DIS"
                int r3 = r0.getInt(r3)     // Catch:{ Throwable -> 0x00d8 }
                r2.h = r3     // Catch:{ Throwable -> 0x00d8 }
                java.lang.String r3 = "loc"
                android.os.Parcelable r0 = r0.getParcelable(r3)     // Catch:{ Throwable -> 0x00d8 }
                com.amap.api.location.AMapLocation r0 = (com.amap.api.location.AMapLocation) r0     // Catch:{ Throwable -> 0x00d8 }
                java.lang.String r3 = r0.getAdCode()     // Catch:{ Throwable -> 0x00d8 }
                boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00d8 }
                if (r3 != 0) goto L_0x0010
                java.lang.Object r3 = r2.o     // Catch:{ Throwable -> 0x00d8 }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x00d8 }
                r2.y = r0     // Catch:{ all -> 0x00d5 }
                monitor-exit(r3)     // Catch:{ all -> 0x00d5 }
                goto L_0x0010
            L_0x00d5:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x00d5 }
                throw r0     // Catch:{ Throwable -> 0x00d8 }
            L_0x00d8:
                r0 = move-exception
                java.lang.String r2 = "GpsLocation"
                java.lang.String r3 = "setLastGeoLocation"
                com.loc.en.a(r0, r2, r3)     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x00e4:
                java.lang.String r1 = "handleMessage RESULT_NGPS_ABLE"
                android.os.Bundle r0 = r6.getData()     // Catch:{ Throwable -> 0x0035 }
                com.loc.d r2 = com.loc.d.this     // Catch:{ Throwable -> 0x0035 }
                java.lang.String r3 = "ngpsAble"
                boolean r0 = r0.getBoolean(r3)     // Catch:{ Throwable -> 0x0035 }
                boolean unused = r2.D = r0     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x00f9:
                java.lang.String r1 = "handleMessage RESULT_INSTALLED_MOCK_APP"
                android.os.Bundle r0 = r6.getData()     // Catch:{ Throwable -> 0x0035 }
                java.lang.String r2 = "installMockApp"
                boolean r0 = r0.getBoolean(r2)     // Catch:{ Throwable -> 0x0035 }
                boolean unused = com.loc.d.E = r0     // Catch:{ Throwable -> 0x0035 }
                goto L_0x0010
            L_0x010c:
                r0 = move-exception
                r4 = r0
                r0 = r1
                r1 = r4
                goto L_0x0039
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.d.c.handleMessage(android.os.Message):void");
        }
    }

    public d(Context context, Intent intent) {
        this.z = context;
        this.j = intent;
        en.e();
        try {
            if (Looper.myLooper() == null) {
                this.b = new c(this.z.getMainLooper());
            } else {
                this.b = new c();
            }
        } catch (Throwable th) {
            en.a(th, "ALManager", "init 1");
        }
        try {
            this.g = new i(this.z);
        } catch (Throwable th2) {
            en.a(th2, "ALManager", "init 5");
        }
        this.l = new b("amapLocManagerThread", this);
        this.l.setPriority(5);
        this.l.start();
        this.w = a(this.l.getLooper());
        try {
            this.c = new g(this.z, this.b);
        } catch (Throwable th3) {
            en.a(th3, "ALManager", "init 3");
        }
        if (this.p == null) {
            this.p = new er();
        }
    }

    private a a(Looper looper) {
        a aVar;
        synchronized (this.o) {
            this.w = new a(looper);
            aVar = this.w;
        }
        return aVar;
    }

    private dw a(dr drVar) {
        if (this.a.isLocationCacheEnable()) {
            try {
                return drVar.g();
            } catch (Throwable th) {
                en.a(th, "ALManager", "doFirstCacheLoc");
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void a(int i2) {
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeMessages(i2);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(int i2, Bundle bundle) {
        if (bundle == null) {
            try {
                bundle = new Bundle();
            } catch (Throwable th) {
                Throwable th2 = th;
                boolean z2 = (th2 instanceof IllegalStateException) && th2.getMessage().contains("sending message to a Handler on a dead thread");
                if ((th2 instanceof RemoteException) || z2) {
                    this.h = null;
                    this.A = false;
                }
                en.a(th2, "ALManager", "sendLocMessage");
                return;
            }
        }
        if (TextUtils.isEmpty(this.s)) {
            this.s = en.b(this.z);
        }
        bundle.putString("c", this.s);
        Message obtain = Message.obtain();
        obtain.what = i2;
        obtain.setData(bundle);
        obtain.replyTo = this.i;
        if (this.h != null) {
            this.h.send(obtain);
        }
    }

    /* access modifiers changed from: private */
    public void a(int i2, Object obj, long j2) {
        synchronized (this.o) {
            if (this.w != null) {
                Message obtain = Message.obtain();
                obtain.what = i2;
                if (obj instanceof Bundle) {
                    obtain.setData((Bundle) obj);
                } else {
                    obtain.obj = obj;
                }
                this.w.sendMessageDelayed(obtain, j2);
            }
        }
    }

    private void a(Intent intent, boolean z2) {
        if (this.z != null) {
            if (Build.VERSION.SDK_INT < 26 || !z2) {
                this.z.startService(intent);
            } else if (!o()) {
                Log.e("amapapi", "-------------调用后台定位服务，缺少权限：android.permission.FOREGROUND_SERVICE--------------");
                return;
            } else {
                try {
                    this.z.getClass().getMethod("startForegroundService", new Class[]{Intent.class}).invoke(this.z, new Object[]{intent});
                } catch (Throwable th) {
                    this.z.startService(intent);
                }
            }
            this.y = true;
        }
    }

    /* access modifiers changed from: private */
    public void a(Bundle bundle) {
        dq dqVar;
        AMapLocation aMapLocation;
        AMapLocation aMapLocation2;
        dq dqVar2;
        Throwable th;
        if (bundle != null) {
            try {
                bundle.setClassLoader(AMapLocation.class.getClassLoader());
                aMapLocation = (AMapLocation) bundle.getParcelable("loc");
                this.x = bundle.getString("nb");
                dqVar = (dq) bundle.getParcelable("statics");
                if (aMapLocation != null) {
                    try {
                        if (aMapLocation.getErrorCode() == 0 && this.c != null) {
                            this.c.w = 0;
                            if (!TextUtils.isEmpty(aMapLocation.getAdCode())) {
                                this.c.y = aMapLocation;
                            }
                        }
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        dqVar2 = dqVar;
                        th = th3;
                        en.a(th, "AmapLocationManager", "resultLbsLocationSuccess");
                        aMapLocation2 = null;
                        a(aMapLocation2, dqVar2);
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                dqVar2 = null;
                en.a(th, "AmapLocationManager", "resultLbsLocationSuccess");
                aMapLocation2 = null;
                a(aMapLocation2, dqVar2);
            }
        } else {
            dqVar = null;
            aMapLocation = null;
        }
        if (this.c != null) {
            aMapLocation = this.c.a(aMapLocation, this.x);
        }
        dq dqVar3 = dqVar;
        aMapLocation2 = aMapLocation;
        dqVar2 = dqVar3;
        a(aMapLocation2, dqVar2);
    }

    private synchronized void a(AMapLocation aMapLocation, dq dqVar) {
        if (aMapLocation == null) {
            aMapLocation = new AMapLocation("");
            aMapLocation.setErrorCode(8);
            aMapLocation.setLocationDetail("amapLocation is null#0801");
        }
        if (!"gps".equalsIgnoreCase(aMapLocation.getProvider())) {
            aMapLocation.setProvider("lbs");
        }
        if (this.t == null) {
            this.t = new AMapLocationQualityReport();
        }
        this.t.setLocationMode(this.a.getLocationMode());
        if (this.c != null) {
            this.t.setGPSSatellites(this.c.d());
            this.t.setGpsStatus(this.c.c());
        }
        this.t.setWifiAble(et.h(this.z));
        this.t.setNetworkType(et.i(this.z));
        if (aMapLocation.getLocationType() == 1 || "gps".equalsIgnoreCase(aMapLocation.getProvider())) {
            this.t.setNetUseTime(0);
        }
        if (dqVar != null) {
            this.t.setNetUseTime(dqVar.a());
        }
        this.t.setInstallHighDangerMockApp(E);
        aMapLocation.setLocationQualityReport(this.t);
        try {
            if (this.B) {
                String str = this.x;
                Bundle bundle = new Bundle();
                bundle.putParcelable("loc", aMapLocation);
                bundle.putString("lastLocNb", str);
                a((int) PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, (Object) bundle, 0);
                if (dqVar != null) {
                    dqVar.d(et.b());
                }
                er.a(this.z, aMapLocation, dqVar);
                er.a(this.z, aMapLocation);
                AMapLocation clone = aMapLocation.clone();
                Message obtainMessage = this.b.obtainMessage();
                obtainMessage.what = 10;
                obtainMessage.obj = clone;
                this.b.sendMessage(obtainMessage);
            }
        } catch (Throwable th) {
            en.a(th, "ALManager", "handlerLocation part3");
        }
        if (!this.m || en.e()) {
            if (this.a.isOnceLocation()) {
                k();
                a(14, (Bundle) null);
            }
        }
        return;
    }

    static /* synthetic */ void a(d dVar, Message message) {
        try {
            AMapLocation aMapLocation = (AMapLocation) message.obj;
            if (dVar.f && dVar.h != null) {
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", en.a(dVar.a));
                dVar.a(0, bundle);
                if (dVar.B) {
                    dVar.a(13, (Bundle) null);
                }
                dVar.f = false;
            }
            dVar.a(aMapLocation, (dq) null);
            if (dVar.D) {
                dVar.a(7, (Bundle) null);
            }
            dVar.a(1025);
            dVar.a(1025, (Object) null, 300000);
        } catch (Throwable th) {
            en.a(th, "ALManager", "resultGpsLocationSuccess");
        }
    }

    static /* synthetic */ void a(d dVar, AMapLocation aMapLocation) {
        try {
            if (aMapLocation.getErrorCode() != 0) {
                aMapLocation.setLocationType(0);
            }
            if (aMapLocation.getErrorCode() == 0) {
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                if ((latitude == ClientTraceData.b.f47a && longitude == ClientTraceData.b.f47a) || latitude < -90.0d || latitude > 90.0d || longitude < -180.0d || longitude > 180.0d) {
                    er.a("errorLatLng", aMapLocation.toStr());
                    aMapLocation.setLocationType(0);
                    aMapLocation.setErrorCode(8);
                    aMapLocation.setLocationDetail("LatLng is error#0802");
                }
            }
            if ("gps".equalsIgnoreCase(aMapLocation.getProvider()) || !dVar.c.b()) {
                aMapLocation.setAltitude(et.c(aMapLocation.getAltitude()));
                aMapLocation.setBearing(et.a(aMapLocation.getBearing()));
                aMapLocation.setSpeed(et.a(aMapLocation.getSpeed()));
                Iterator<AMapLocationListener> it = dVar.d.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().onLocationChanged(aMapLocation);
                    } catch (Throwable th) {
                    }
                }
            }
        } catch (Throwable th2) {
        }
    }

    static /* synthetic */ void a(d dVar, AMapLocationListener aMapLocationListener) {
        if (aMapLocationListener == null) {
            throw new IllegalArgumentException("listener参数不能为null");
        }
        if (dVar.d == null) {
            dVar.d = new ArrayList<>();
        }
        if (!dVar.d.contains(aMapLocationListener)) {
            dVar.d.add(aMapLocationListener);
        }
    }

    private static void a(dr drVar, dw dwVar) {
        if (dwVar != null) {
            try {
                if (dwVar.getErrorCode() == 0) {
                    drVar.b(dwVar);
                }
            } catch (Throwable th) {
                en.a(th, "ALManager", "apsLocation:doFirstAddCache");
            }
        }
    }

    private dw b(dr drVar) {
        Throwable th;
        dw dwVar;
        AMapLocation aMapLocation;
        boolean z2 = false;
        String str = null;
        boolean z3 = true;
        dq dqVar = new dq();
        try {
            dqVar.c(et.b());
            String apikey = AMapLocationClientOption.getAPIKEY();
            if (!TextUtils.isEmpty(apikey)) {
                l.a(this.z, apikey);
            }
        } catch (Throwable th2) {
            th = th2;
            dwVar = null;
        }
        try {
            String umidtoken = UmidtokenInfo.getUmidtoken();
            if (!TextUtils.isEmpty(umidtoken)) {
                n.a(umidtoken);
            }
        } catch (Throwable th3) {
            en.a(th3, "ALManager", "apsLocation setUmidToken");
        }
        try {
            drVar.a(this.z);
            drVar.a(this.a);
            drVar.b(dqVar);
        } catch (Throwable th4) {
            en.a(th4, "ALManager", "initApsBase");
        }
        boolean s2 = em.s();
        dw a2 = a(drVar);
        if (a2 == null) {
            if (!s2) {
                z2 = true;
            }
            try {
                a2 = drVar.a(z2, dqVar);
                if (!s2) {
                    a(drVar, a2);
                }
            } catch (Throwable th5) {
                Throwable th6 = th5;
                dwVar = a2;
                th = th6;
            }
        } else {
            z3 = false;
        }
        if (a2 != null) {
            str = a2.k();
            aMapLocation = a2.clone();
        } else {
            aMapLocation = null;
        }
        try {
            if (this.a.isLocationCacheEnable() && this.g != null) {
                aMapLocation = this.g.a(aMapLocation, str, this.a.getLastLocationLifeCycle());
            }
        } catch (Throwable th7) {
            en.a(th7, "ALManager", "fixLastLocation");
        }
        try {
            Bundle bundle = new Bundle();
            if (aMapLocation != null) {
                bundle.putParcelable("loc", aMapLocation);
                bundle.putString("nb", a2.k());
                bundle.putParcelable("statics", dqVar);
            }
            a(bundle);
        } catch (Throwable th8) {
            en.a(th8, "ALManager", "apsLocation:callback");
        }
        if (z3 && s2) {
            try {
                drVar.c();
                a(drVar, drVar.a(true, new dq()));
            } catch (Throwable th9) {
                en.a(th9, "ALManager", "apsLocation:doFirstNetLocate 2");
            }
        }
        try {
            drVar.e();
            return a2;
        } catch (Throwable th10) {
            return a2;
        }
        throw th;
        try {
            en.a(th, "ALManager", "apsLocation");
            try {
                drVar.e();
                return dwVar;
            } catch (Throwable th11) {
                return dwVar;
            }
        } catch (Throwable th12) {
        }
    }

    static /* synthetic */ void b(d dVar, Message message) {
        try {
            Bundle data = message.getData();
            AMapLocation aMapLocation = (AMapLocation) data.getParcelable("loc");
            String string = data.getString("lastLocNb");
            if (aMapLocation != null) {
                AMapLocation aMapLocation2 = null;
                try {
                    if (i.b != null) {
                        aMapLocation2 = i.b.a();
                    } else if (dVar.g != null) {
                        aMapLocation2 = dVar.g.b();
                    }
                    er.a(aMapLocation2, aMapLocation);
                } catch (Throwable th) {
                }
            }
            if (dVar.g.a(aMapLocation, string)) {
                dVar.g.d();
            }
        } catch (Throwable th2) {
            en.a(th2, "ALManager", "doSaveLastLocation");
        }
    }

    static /* synthetic */ void b(d dVar, AMapLocationListener aMapLocationListener) {
        if (!dVar.d.isEmpty() && dVar.d.contains(aMapLocationListener)) {
            dVar.d.remove(aMapLocationListener);
        }
        if (dVar.d.isEmpty()) {
            dVar.k();
        }
    }

    static /* synthetic */ void c(d dVar, Message message) {
        if (message != null) {
            try {
                Bundle data = message.getData();
                if (data != null) {
                    int i2 = data.getInt(UploadQueueMgr.MSGTYPE_INTERVAL, 0);
                    Intent n2 = dVar.n();
                    n2.putExtra(UploadQueueMgr.MSGTYPE_INTERVAL, i2);
                    n2.putExtra("h", (Notification) data.getParcelable("h"));
                    n2.putExtra("g", 1);
                    dVar.a(n2, true);
                }
            } catch (Throwable th) {
                en.a(th, "ALManager", "doEnableBackgroundLocation");
            }
        }
    }

    static /* synthetic */ void d(d dVar) {
        try {
            if (C || !dVar.q) {
                C = false;
                dw b2 = dVar.b(new dr());
                if (dVar.i()) {
                    Bundle bundle = new Bundle();
                    String str = "0";
                    if (b2 != null && (b2.getLocationType() == 2 || b2.getLocationType() == 4)) {
                        str = "1";
                    }
                    bundle.putBundle("optBundle", en.a(dVar.a));
                    bundle.putString("isCacheLoc", str);
                    dVar.a(0, bundle);
                    if (dVar.B) {
                        dVar.a(13, (Bundle) null);
                    }
                }
            } else {
                try {
                    if (dVar.q && !dVar.A && !dVar.v) {
                        dVar.v = true;
                        dVar.m();
                    }
                } catch (Throwable th) {
                    dVar.v = true;
                    en.a(th, "ALManager", "doLBSLocation reStartService");
                }
                if (dVar.i()) {
                    dVar.v = false;
                    Bundle bundle2 = new Bundle();
                    bundle2.putBundle("optBundle", en.a(dVar.a));
                    bundle2.putString("d", UmidtokenInfo.getUmidtoken());
                    if (!dVar.c.b()) {
                        dVar.a(1, bundle2);
                    }
                }
            }
            try {
                if (!dVar.a.isOnceLocation()) {
                    dVar.l();
                }
            } catch (Throwable th2) {
            }
        } catch (Throwable th3) {
        }
    }

    static /* synthetic */ void d(d dVar, Message message) {
        if (message != null) {
            try {
                Bundle data = message.getData();
                if (data != null) {
                    boolean z2 = data.getBoolean("j", true);
                    Intent n2 = dVar.n();
                    n2.putExtra("j", z2);
                    n2.putExtra("g", 2);
                    dVar.a(n2, false);
                }
            } catch (Throwable th) {
                en.a(th, "ALManager", "doDisableBackgroundLocation");
            }
        }
    }

    static /* synthetic */ void f(d dVar) {
        g gVar = dVar.c;
        AMapLocationClientOption aMapLocationClientOption = dVar.a;
        if (aMapLocationClientOption == null) {
            aMapLocationClientOption = new AMapLocationClientOption();
        }
        gVar.c = aMapLocationClientOption;
        if (!(gVar.c.getLocationMode() == AMapLocationClientOption.AMapLocationMode.Device_Sensors || gVar.a == null)) {
            gVar.a.removeMessages(8);
        }
        if (gVar.r != gVar.c.getGeoLanguage()) {
            synchronized (gVar.o) {
                gVar.y = null;
            }
        }
        gVar.r = gVar.c.getGeoLanguage();
        if (dVar.B && !dVar.a.getLocationMode().equals(dVar.n)) {
            dVar.k();
            dVar.j();
        }
        dVar.n = dVar.a.getLocationMode();
        if (dVar.p != null) {
            if (dVar.a.isOnceLocation()) {
                dVar.p.a(dVar.z, 0);
            } else {
                dVar.p.a(dVar.z, 1);
            }
            dVar.p.a(dVar.z, dVar.a);
        }
    }

    static /* synthetic */ void g(d dVar) {
        try {
            if (dVar.h != null) {
                dVar.k = 0;
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", en.a(dVar.a));
                dVar.a(2, bundle);
                return;
            }
            dVar.k++;
            if (dVar.k < 10) {
                dVar.a(1008, (Object) null, 50);
            }
        } catch (Throwable th) {
            en.a(th, "ALManager", "startAssistantLocationImpl");
        }
    }

    static /* synthetic */ void h(d dVar) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("optBundle", en.a(dVar.a));
            dVar.a(3, bundle);
        } catch (Throwable th) {
            en.a(th, "ALManager", "stopAssistantLocationImpl");
        }
    }

    private boolean i() {
        boolean z2 = true;
        int i2 = 0;
        do {
            try {
                if (this.h != null) {
                    break;
                }
                Thread.sleep(100);
                i2++;
            } catch (Throwable th) {
                en.a(th, "ALManager", "checkAPSManager");
                z2 = false;
            }
        } while (i2 < 50);
        if (this.h == null) {
            Message obtain = Message.obtain();
            Bundle bundle = new Bundle();
            AMapLocation aMapLocation = new AMapLocation("");
            aMapLocation.setErrorCode(10);
            if (!et.l(this.z.getApplicationContext())) {
                aMapLocation.setLocationDetail("请检查配置文件是否配置服务，并且manifest中service标签是否配置在application标签内#1003");
            } else {
                aMapLocation.setLocationDetail("启动ApsServcie失败#1001");
            }
            bundle.putParcelable("loc", aMapLocation);
            obtain.setData(bundle);
            obtain.what = 1;
            this.b.sendMessage(obtain);
            z2 = false;
        }
        if (!z2) {
            if (!et.l(this.z.getApplicationContext())) {
                er.a((String) null, 2103);
            } else {
                er.a((String) null, 2101);
            }
        }
        return z2;
    }

    /* access modifiers changed from: private */
    public synchronized void j() {
        long j2 = 0;
        synchronized (this) {
            if (this.a == null) {
                this.a = new AMapLocationClientOption();
            }
            if (!this.B) {
                this.B = true;
                switch (this.a.getLocationMode()) {
                    case Battery_Saving:
                        a((int) PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, (Object) null, 0);
                        a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, 0);
                        break;
                    case Device_Sensors:
                        a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW);
                        a((int) PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, (Object) null, 0);
                        break;
                    case Hight_Accuracy:
                        a((int) PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, (Object) null, 0);
                        if (this.a.isGpsFirst() && this.a.isOnceLocation()) {
                            j2 = this.a.getGpsFirstTimeout();
                        }
                        a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, j2);
                        break;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void k() {
        try {
            a(1025);
            if (this.c != null) {
                this.c.a();
            }
            a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW);
            this.B = false;
            this.k = 0;
        } catch (Throwable th) {
            en.a(th, "ALManager", "stopLocation");
        }
    }

    private void l() {
        long j2 = 1000;
        if (this.a.getLocationMode() != AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
            if (this.a.getInterval() >= 1000) {
                j2 = this.a.getInterval();
            }
            a((int) PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, j2);
        }
    }

    /* access modifiers changed from: private */
    public void m() {
        try {
            if (this.i == null) {
                this.i = new Messenger(this.b);
            }
            this.z.bindService(n(), this.G, 1);
        } catch (Throwable th) {
        }
    }

    private Intent n() {
        if (this.j == null) {
            this.j = new Intent(this.z, APSService.class);
        }
        String str = "";
        try {
            str = !TextUtils.isEmpty(AMapLocationClientOption.getAPIKEY()) ? AMapLocationClientOption.getAPIKEY() : k.f(this.z);
        } catch (Throwable th) {
            en.a(th, "ALManager", "startServiceImpl p2");
        }
        this.j.putExtra("a", str);
        this.j.putExtra("b", k.c(this.z));
        this.j.putExtra("d", UmidtokenInfo.getUmidtoken());
        this.j.putExtra("f", AMapLocationClientOption.isDownloadCoordinateConvertLibrary());
        return this.j;
    }

    private boolean o() {
        int i2;
        if (et.k(this.z)) {
            try {
                i2 = eq.b(((Application) this.z.getApplicationContext()).getBaseContext(), "checkSelfPermission", "android.permission.FOREGROUND_SERVICE");
            } catch (Throwable th) {
                i2 = -1;
            }
            if (i2 != 0) {
                return false;
            }
        }
        return true;
    }

    public final void a(int i2, Notification notification) {
        if (i2 != 0 && notification != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putInt(UploadQueueMgr.MSGTYPE_INTERVAL, i2);
                bundle.putParcelable("h", notification);
                a(1023, (Object) bundle, 0);
            } catch (Throwable th) {
                en.a(th, "ALManager", "disableBackgroundLocation");
            }
        }
    }

    public final void a(WebView webView) {
        if (this.F == null) {
            this.F = new h(this.z, webView);
        }
        this.F.a();
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        try {
            a((int) PointerIconCompat.TYPE_ZOOM_IN, (Object) aMapLocationClientOption.clone(), 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "setLocationOption");
        }
    }

    public final void a(AMapLocationListener aMapLocationListener) {
        try {
            a(1002, (Object) aMapLocationListener, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "setLocationListener");
        }
    }

    public final void a(boolean z2) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("j", z2);
            a(1024, (Object) bundle, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "disableBackgroundLocation");
        }
    }

    public final boolean a() {
        return this.A;
    }

    public final void b() {
        try {
            a(1003, (Object) null, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "startLocation");
        }
    }

    public final void b(AMapLocationListener aMapLocationListener) {
        try {
            a(1005, (Object) aMapLocationListener, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "unRegisterLocationListener");
        }
    }

    public final void c() {
        try {
            a(1004, (Object) null, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "stopLocation");
        }
    }

    public final void d() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(1011, (Object) null, 0);
            this.m = true;
        } catch (Throwable th) {
            en.a(th, "ALManager", "onDestroy");
        }
    }

    public final AMapLocation e() {
        AMapLocation aMapLocation = null;
        try {
            if (!(this.g == null || (aMapLocation = this.g.b()) == null)) {
                aMapLocation.setTrustedLevel(3);
            }
        } catch (Throwable th) {
            en.a(th, "ALManager", "getLastKnownLocation");
        }
        return aMapLocation;
    }

    public final void f() {
        try {
            a(1008, (Object) null, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "startAssistantLocation");
        }
    }

    public final void g() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(1009, (Object) null, 0);
        } catch (Throwable th) {
            en.a(th, "ALManager", "stopAssistantLocation");
        }
    }

    /* access modifiers changed from: package-private */
    public final void h() {
        a(12, (Bundle) null);
        C = true;
        this.f = true;
        this.A = false;
        this.q = false;
        k();
        if (this.p != null) {
            this.p.b(this.z);
        }
        er.a(this.z);
        if (this.r != null) {
            this.r.d.sendEmptyMessage(11);
        } else if (this.G != null) {
            this.z.unbindService(this.G);
        }
        try {
            if (this.y) {
                this.z.stopService(n());
            }
        } catch (Throwable th) {
        }
        this.y = false;
        if (this.d != null) {
            this.d.clear();
            this.d = null;
        }
        this.G = null;
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeCallbacksAndMessages((Object) null);
            }
            this.w = null;
        }
        if (this.l != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    eq.a((Object) this.l, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
                } catch (Throwable th2) {
                    this.l.quit();
                }
            } else {
                this.l.quit();
            }
        }
        this.l = null;
        if (this.b != null) {
            this.b.removeCallbacksAndMessages((Object) null);
        }
        if (this.g != null) {
            this.g.c();
            this.g = null;
        }
    }
}
