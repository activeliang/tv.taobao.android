package com.loc;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.ali.auth.third.core.rpc.safe.AESCrypto;
import com.bumptech.glide.BuildConfig;
import com.loc.as;
import com.loc.cc;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.crypto.KeyGenerator;

/* compiled from: CollectionManager */
public final class ds implements Cdo {
    /* access modifiers changed from: private */
    public static long k = 0;
    Context a = null;
    eb b = null;
    ea c = null;
    cy d;
    bb e;
    /* access modifiers changed from: private */
    public ArrayList<ce> f = new ArrayList<>();
    private Handler g;
    private LocationManager h;
    private a i;
    private volatile boolean j = false;

    /* compiled from: CollectionManager */
    static class a implements LocationListener {
        private ds a;

        a(ds dsVar) {
            this.a = dsVar;
        }

        /* access modifiers changed from: package-private */
        public final void a() {
            this.a = null;
        }

        /* access modifiers changed from: package-private */
        public final void a(ds dsVar) {
            this.a = dsVar;
        }

        public final void onLocationChanged(Location location) {
            try {
                if (this.a != null) {
                    ab.d().submit(new Runnable(location) {
                        final /* synthetic */ Location a;

                        {
                            this.a = r2;
                        }

                        public final void run() {
                            try {
                                Bundle extras = this.a.getExtras();
                                int i = 0;
                                if (extras != null) {
                                    i = extras.getInt("satellites");
                                }
                                if (!et.a(this.a, i)) {
                                    if (ds.this.b != null && !ds.this.b.r) {
                                        ds.this.b.f();
                                    }
                                    ArrayList<dh> a2 = ds.this.b.a();
                                    List<da> a3 = ds.this.c.a();
                                    cc.a aVar = new cc.a();
                                    dg dgVar = new dg();
                                    dgVar.i = this.a.getAccuracy();
                                    dgVar.f = this.a.getAltitude();
                                    dgVar.d = this.a.getLatitude();
                                    dgVar.h = this.a.getBearing();
                                    dgVar.e = this.a.getLongitude();
                                    dgVar.j = this.a.isFromMockProvider();
                                    dgVar.a = this.a.getProvider();
                                    dgVar.g = this.a.getSpeed();
                                    dgVar.l = (byte) i;
                                    dgVar.b = System.currentTimeMillis();
                                    dgVar.c = this.a.getTime();
                                    dgVar.k = this.a.getTime();
                                    aVar.a = dgVar;
                                    aVar.b = a2;
                                    WifiInfo c = ds.this.b.c();
                                    if (c != null) {
                                        aVar.c = dh.a(c.getBSSID());
                                    }
                                    aVar.d = eb.w;
                                    aVar.f = this.a.getTime();
                                    aVar.g = (byte) n.q(ds.this.a);
                                    aVar.h = n.v(ds.this.a);
                                    aVar.e = ds.this.b.q;
                                    aVar.j = et.a(ds.this.a);
                                    aVar.i = a3;
                                    ce a4 = cy.a(aVar);
                                    if (a4 != null) {
                                        ds.this.f.add(a4);
                                        if (ds.this.f.size() >= 5) {
                                            ds.this.f();
                                        }
                                        ds.this.d();
                                    }
                                }
                            } catch (Throwable th) {
                                en.a(th, "cl", "coll");
                            }
                        }
                    });
                }
            } catch (Throwable th) {
            }
        }

        public final void onProviderDisabled(String str) {
        }

        public final void onProviderEnabled(String str) {
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    ds(Context context) {
        this.a = context;
        this.e = new bb();
        bh.a(this.a, this.e, z.k, 100, 1024000, "0");
        this.e.f = new bt(context, em.l, "kKey", new br(context, em.j, em.k, em.k * 10, "carrierLocKey"));
        this.e.e = new aq();
    }

    static /* synthetic */ void a(as asVar, List list) {
        if (asVar != null) {
            try {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    asVar.c((String) it.next());
                }
                asVar.close();
            } catch (Throwable th) {
                ab.b(th, "aps", "dlo");
            }
        }
    }

    /* access modifiers changed from: private */
    public static byte[] a(int i2) {
        try {
            KeyGenerator instance = KeyGenerator.getInstance(AESCrypto.ALGORITHM);
            if (instance == null) {
                return null;
            }
            instance.init(i2);
            return instance.generateKey().getEncoded();
        } catch (Throwable th) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static List<ce> b(as asVar, bb bbVar, List<String> list, byte[] bArr) {
        String str;
        as.b a2;
        InputStream a3;
        ArrayList arrayList = new ArrayList();
        try {
            File b2 = asVar.b();
            if (b2 != null && b2.exists()) {
                int i2 = 0;
                for (String str2 : b2.list()) {
                    if (!(!str2.contains(".0") || (a2 = asVar.a(str)) == null || (a3 = a2.a()) == null)) {
                        int i3 = 0;
                        byte[] bArr2 = new byte[2];
                        a3.read(bArr2);
                        int b3 = et.b(bArr2);
                        if (b3 == 0 || b3 > 65535) {
                            break;
                        }
                        byte[] bArr3 = new byte[b3];
                        a3.read(bArr3);
                        byte[] bArr4 = new byte[2];
                        while (a3.read(bArr4) >= 0) {
                            byte[] bArr5 = new byte[et.b(bArr4)];
                            a3.read(bArr5);
                            byte[] a4 = o.a(bArr3, bArr5, u.c());
                            i3 += a4.length;
                            byte[] bArr6 = new byte[4];
                            a3.read(bArr6);
                            arrayList.add(new ce(((bArr6[0] & OnReminderListener.RET_FULL) << 24) | (bArr6[3] & OnReminderListener.RET_FULL) | ((bArr6[2] & OnReminderListener.RET_FULL) << 8) | ((bArr6[1] & OnReminderListener.RET_FULL) << 16), o.b(bArr, u.b(a4), u.c())));
                        }
                        i2 += i3;
                        list.add((str = str2.split("\\.")[0]));
                        if (i2 > bbVar.f.b()) {
                            break;
                        }
                    }
                }
                return arrayList;
            }
        } catch (Throwable th) {
            th.printStackTrace();
            ab.b(th, "aps", "upc");
        }
        return arrayList;
    }

    private static byte[] b(int i2) {
        byte[] bArr = new byte[2];
        bArr[1] = (byte) (i2 & 255);
        bArr[0] = (byte) ((65280 & i2) >> 8);
        return bArr;
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (this.f != null && this.f.size() != 0) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.f);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] a2 = a(256);
                if (a2 != null) {
                    byteArrayOutputStream.write(b(a2.length));
                    byteArrayOutputStream.write(a2);
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ce ceVar = (ce) it.next();
                        byte[] b2 = ceVar.b();
                        if (b2.length >= 10 && b2.length <= 65535) {
                            byte[] b3 = o.b(a2, b2, u.c());
                            byteArrayOutputStream.write(b(b3.length));
                            byteArrayOutputStream.write(b3);
                            int a3 = ceVar.a();
                            byteArrayOutputStream.write(new byte[]{(byte) ((a3 >> 24) & 255), (byte) ((a3 >> 16) & 255), (byte) ((a3 >> 8) & 255), (byte) (a3 & 255)});
                        }
                    }
                    bc.a(Long.toString(System.currentTimeMillis()), byteArrayOutputStream.toByteArray(), this.e);
                    this.f.clear();
                }
            }
        } catch (Throwable th) {
            en.a(th, "clm", "wtD");
        }
    }

    public final dn a(dm dmVar) {
        try {
            eh ehVar = new eh();
            ehVar.a(dmVar.b);
            ehVar.a(dmVar.a);
            ehVar.a(dmVar.d);
            aw.a();
            ba c2 = aw.c(ehVar);
            dn dnVar = new dn();
            dnVar.c = c2.a;
            dnVar.b = c2.b;
            dnVar.a = 200;
            return dnVar;
        } catch (Throwable th) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final void a() {
        try {
            if (!(this.i == null || this.h == null)) {
                this.h.removeUpdates(this.i);
            }
            if (this.i != null) {
                this.i.a();
            }
            if (this.j) {
                f();
                this.b.a((ds) null);
                this.c.a((ds) null);
                this.c = null;
                this.b = null;
                this.g = null;
                this.j = false;
            }
        } catch (Throwable th) {
            en.a(th, "clm", "stc");
        }
    }

    public final void a(ea eaVar, eb ebVar, Handler handler) {
        if (!this.j && eaVar != null && ebVar != null && handler != null) {
            this.j = true;
            this.c = eaVar;
            this.b = ebVar;
            this.b.a(this);
            this.c.a(this);
            this.g = handler;
            try {
                if (this.h == null && this.g != null) {
                    this.h = (LocationManager) this.a.getSystemService("location");
                }
                if (this.i == null) {
                    this.i = new a(this);
                }
                this.i.a(this);
                if (!(this.i == null || this.h == null)) {
                    this.h.requestLocationUpdates("passive", 1000, -1.0f, this.i);
                }
                if (this.d == null) {
                    this.d = new cy(BuildConfig.VERSION_NAME, k.f(this.a), "S128DF1572465B890OE3F7A13167KLEI", k.c(this.a), this);
                    this.d.a(n.x(this.a)).b(n.h(this.a)).c(n.a(this.a)).d(n.g(this.a)).e(n.A(this.a)).f(n.i(this.a)).g(Build.MODEL).h(Build.MANUFACTURER).i(Build.BRAND).a(Build.VERSION.SDK_INT).j(Build.VERSION.RELEASE).a(dh.a(n.m(this.a))).k(n.m(this.a));
                }
            } catch (Throwable th) {
                en.a(th, "col", "init");
            }
        }
    }

    public final void b() {
        try {
            if (this.d != null && this.b != null) {
                cy.b((List<dh>) this.b.a());
            }
        } catch (Throwable th) {
            en.a(th, "cl", "upw");
        }
    }

    public final void c() {
        try {
            if (this.d != null && this.c != null) {
                cy.a(this.c.a());
            }
        } catch (Throwable th) {
            en.a(th, "cl", "upc");
        }
    }

    public final void d() {
        try {
            if (System.currentTimeMillis() - k >= 60000) {
                ab.d().submit(new Runnable() {
                    public final void run() {
                        as asVar = null;
                        try {
                            long unused = ds.k = System.currentTimeMillis();
                            if (ds.this.e.f.c()) {
                                asVar = as.a(new File(ds.this.e.a), ds.this.e.b);
                                ArrayList arrayList = new ArrayList();
                                byte[] e = ds.a(128);
                                if (e == null) {
                                    try {
                                        asVar.close();
                                        return;
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                        return;
                                    }
                                } else {
                                    List a2 = ds.b(asVar, ds.this.e, arrayList, e);
                                    if (a2 == null || a2.size() == 0) {
                                        try {
                                            asVar.close();
                                            return;
                                        } catch (Throwable th2) {
                                            th2.printStackTrace();
                                            return;
                                        }
                                    } else {
                                        ds.this.e.f.a(true);
                                        if (cy.a(u.b(cy.a(ec.a(e), o.b(e, cy.a(), u.c()), a2)))) {
                                            ds.a(asVar, arrayList);
                                        }
                                    }
                                }
                            }
                            if (asVar != null) {
                                try {
                                    asVar.close();
                                } catch (Throwable th3) {
                                    th3.printStackTrace();
                                }
                            }
                        } catch (Throwable th4) {
                            th4.printStackTrace();
                        }
                    }
                });
            }
        } catch (Throwable th) {
        }
    }
}
