package com.taobao.muniontaobaosdk.p4p.anticheat.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Base64;
import com.taobao.muniontaobaosdk.p4p.anticheat.a;
import com.taobao.muniontaobaosdk.util.TaoLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClientTraceData {
    private static final String j = ClientTraceData.class.getName();
    private static int w = 0;
    public byte a;

    /* renamed from: a  reason: collision with other field name */
    public double f28a;

    /* renamed from: a  reason: collision with other field name */
    public int f29a;

    /* renamed from: a  reason: collision with other field name */
    private Context f30a;

    /* renamed from: a  reason: collision with other field name */
    public String f31a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f32a;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f33a;
    public byte b;

    /* renamed from: b  reason: collision with other field name */
    public double f34b;

    /* renamed from: b  reason: collision with other field name */
    public int f35b;

    /* renamed from: b  reason: collision with other field name */
    public String f36b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f37b;
    public int c;

    /* renamed from: c  reason: collision with other field name */
    public String f38c;

    /* renamed from: c  reason: collision with other field name */
    public boolean f39c;
    public int d;

    /* renamed from: d  reason: collision with other field name */
    public String f40d;
    public int e;

    /* renamed from: e  reason: collision with other field name */
    public String f41e;
    public int f;

    /* renamed from: f  reason: collision with other field name */
    public String f42f;
    public int g;

    /* renamed from: g  reason: collision with other field name */
    public String f43g;
    public int h;

    /* renamed from: h  reason: collision with other field name */
    public String f44h;
    public int i;

    /* renamed from: i  reason: collision with other field name */
    public String f45i;

    /* renamed from: j  reason: collision with other field name */
    public int f46j;
    public int k;
    public int l;
    public int m;
    public int n;
    public int o;
    public int p;
    public int q;
    public int r;
    public int s;
    public int t;
    public int u;
    public int v;

    public static class a {
        public static final int a = 0;
        public static final int b = 1;
        public static final int c = 2;
        public static final int d = 3;
        public static final int e = 0;
        public static final int f = 1;
        public static final int g = 2;
        public static final int h = 0;
        public static final int i = 1;
        public static final int j = 2;
        public static final int k = 3;
    }

    public static class b {
        private static final byte a = 1;

        /* renamed from: a  reason: collision with other field name */
        public static final double f47a = 0.0d;

        /* renamed from: a  reason: collision with other field name */
        public static final int f48a = 0;

        /* renamed from: a  reason: collision with other field name */
        public static final String f49a = "viewW";
        /* access modifiers changed from: private */

        /* renamed from: a  reason: collision with other field name */
        public static final byte[] f50a = {-1};
        private static final byte b = 0;

        /* renamed from: b  reason: collision with other field name */
        public static final int f51b = -1;

        /* renamed from: b  reason: collision with other field name */
        public static final String f52b = "viewH";
        /* access modifiers changed from: private */

        /* renamed from: b  reason: collision with other field name */
        public static final byte[] f53b = {-1, -1, -1, -1, -1, -1, -1};
        private static final int c = 180;

        /* renamed from: c  reason: collision with other field name */
        public static final String f54c = "longitude";
        /* access modifiers changed from: private */

        /* renamed from: c  reason: collision with other field name */
        public static final byte[] f55c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        public static final String d = "latitude";
        private static final String e = "1.1";
        private static final String f = "\r\n";
    }

    private ClientTraceData() {
    }

    public ClientTraceData(Context context, Bundle bundle) {
        this.f30a = context;
        if (bundle != null) {
            this.i = bundle.getInt(b.f49a, -1);
            this.f46j = bundle.getInt(b.f52b, -1);
            this.f28a = bundle.getDouble(b.f54c, b.f47a);
            this.f34b = bundle.getDouble(b.d, b.f47a);
        } else {
            this.i = -1;
            this.f46j = -1;
            this.f28a = b.f47a;
            this.f34b = b.f47a;
        }
        a();
    }

    public static int a(InputStream inputStream) throws IOException {
        int read = 0 | ((inputStream.read() << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (inputStream.read() & 255);
        if (read != 65535) {
            return read;
        }
        return -1;
    }

    private String a() {
        StringBuilder sb = new StringBuilder();
        sb.append("emulator:" + sb);
        sb.append("\r\n");
        sb.append("osVersion:" + this.f35b);
        sb.append("\r\n");
        sb.append("MAC:" + this.f38c);
        sb.append("\r\n");
        sb.append("IMSI:" + this.f40d);
        sb.append("\r\n");
        sb.append("deviceId:" + this.f41e);
        sb.append("\r\n");
        sb.append("deviceType:" + this.a);
        sb.append("\r\n");
        sb.append("manufacturer:" + this.f42f);
        sb.append("\r\n");
        sb.append("appWidth:" + this.c);
        sb.append("\r\n");
        sb.append("appHight:" + this.d);
        sb.append("\r\n");
        sb.append("screenDensity:" + this.e);
        sb.append("\r\n");
        sb.append("screenBright:" + this.f);
        sb.append("\r\n");
        sb.append("netType:" + this.g);
        sb.append("\r\n");
        sb.append("netProtocol:" + this.h);
        sb.append("\r\n");
        sb.append("appRunTime:" + w);
        sb.append("\r\n");
        sb.append("isConvered:-1");
        sb.append("\r\n");
        sb.append("adOpenness:" + this.b);
        sb.append("\r\n");
        sb.append("inVisio:-1");
        sb.append("\r\n");
        sb.append("adWidth:" + this.i);
        sb.append("\r\n");
        sb.append("adHeight:" + this.f46j);
        sb.append("\r\n");
        sb.append("touchNum:" + this.k);
        sb.append("\r\n");
        sb.append("touchDownX:" + this.i);
        sb.append("\r\n");
        sb.append("touchDownY:" + this.f46j);
        sb.append("\r\n");
        sb.append("touchUpX:" + this.k);
        sb.append("\r\n");
        sb.append("touchUpY:" + this.f46j);
        sb.append("\r\n");
        sb.append("touchMoveX:" + this.k);
        sb.append("\r\n");
        sb.append("touchMoveY:" + this.k);
        sb.append("\r\n");
        sb.append("touchTime:" + this.k);
        sb.append("\r\n");
        sb.append("availPower:" + this.k);
        sb.append("\r\n");
        sb.append("totalMemory:" + this.k);
        sb.append("\r\n");
        sb.append("availMemory:" + this.k);
        sb.append("\r\n");
        sb.append("netTraffic:" + this.k);
        sb.append("\r\n");
        sb.append("packName:" + this.k);
        sb.append("\r\n");
        sb.append("longitude:" + this.k);
        sb.append("\r\n");
        sb.append("latitude:" + this.k);
        sb.append("\r\n");
        sb.append("isRoot:-1");
        sb.append("\r\n");
        return sb.toString();
    }

    /* renamed from: a  reason: collision with other method in class */
    private void m13a() {
        this.f31a = "1.1";
        if (w == 0) {
            w = com.taobao.muniontaobaosdk.p4p.anticheat.a.b();
        }
        this.f29a = 1;
        this.f35b = com.taobao.muniontaobaosdk.p4p.anticheat.a.a();
        this.f36b = com.taobao.muniontaobaosdk.p4p.anticheat.a.a();
        this.f38c = com.taobao.muniontaobaosdk.p4p.anticheat.a.a(this.f30a);
        this.f40d = com.taobao.muniontaobaosdk.p4p.anticheat.a.b(this.f30a);
        this.f41e = com.taobao.muniontaobaosdk.p4p.anticheat.a.c(this.f30a);
        this.a = 0;
        this.f42f = com.taobao.muniontaobaosdk.p4p.anticheat.a.b();
        this.c = com.taobao.muniontaobaosdk.p4p.anticheat.a.a(this.f30a);
        this.d = com.taobao.muniontaobaosdk.p4p.anticheat.a.b(this.f30a);
        this.f32a = false;
        this.f37b = false;
        this.b = -1;
        this.k = -1;
        this.l = -1;
        this.m = -1;
        this.n = -1;
        this.o = -1;
        this.p = -1;
        this.q = -1;
        this.r = -1;
        this.s = -1;
        this.e = com.taobao.muniontaobaosdk.p4p.anticheat.a.c(this.f30a);
        this.f = com.taobao.muniontaobaosdk.p4p.anticheat.a.d(this.f30a);
        this.g = com.taobao.muniontaobaosdk.p4p.anticheat.a.e(this.f30a);
        this.h = com.taobao.muniontaobaosdk.p4p.anticheat.a.f(this.f30a);
        this.t = com.taobao.muniontaobaosdk.p4p.anticheat.a.g(this.f30a);
        this.u = com.taobao.muniontaobaosdk.p4p.anticheat.a.h(this.f30a);
        this.v = -1;
        this.f43g = com.taobao.muniontaobaosdk.p4p.anticheat.a.d(this.f30a);
        this.f39c = false;
        this.f45i = "";
    }

    private static void a(byte b2, byte[] bArr, int i2) {
        bArr[i2] = b2;
    }

    public static void a(OutputStream outputStream, double d2, int i2) throws IOException {
        int i3 = -1;
        if (d2 != b.f47a) {
            int floor = (int) Math.floor(Math.abs(d2));
            i3 = (((d2 > b.f47a ? floor + 180 : 180 - floor) << 6) & 32704) | (((int) Math.floor(com.taobao.muniontaobaosdk.p4p.anticheat.a.a(Math.abs(d2)) * 60.0d)) & 63) | 0;
        }
        a(outputStream, i3);
    }

    public static void a(OutputStream outputStream, int i2) throws IOException {
        outputStream.write((byte) (i2 >> 8));
        outputStream.write((byte) (i2 >> 0));
    }

    public static void a(OutputStream outputStream, String str) throws IOException {
        if (str == null) {
            outputStream.write(b.f55c);
        } else if (str.trim().length() > 0) {
            outputStream.write(str.getBytes());
        } else {
            outputStream.write((byte) str.trim().length());
        }
    }

    private static void a(String str, byte[] bArr, int i2) {
        try {
            String[] split = str.split("\\.");
            bArr[i2] = Integer.valueOf(split[0]).byteValue();
            bArr[i2 + 1] = Integer.valueOf(split[1]).byteValue();
        } catch (Exception e2) {
        }
    }

    private static void a(byte[] bArr, byte[] bArr2, int i2) {
        int i3 = i2 + 1;
        bArr2[i2] = bArr[0];
        int i4 = i3 + 1;
        bArr2[i3] = bArr[5];
        bArr2[i4] = bArr[10];
        bArr2[i4 + 1] = bArr[15];
    }

    /* renamed from: a  reason: collision with other method in class */
    private byte[] m14a() throws IOException {
        int i2 = 1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b((OutputStream) byteArrayOutputStream, this.f36b);
        a((OutputStream) byteArrayOutputStream, this.f35b);
        a((OutputStream) byteArrayOutputStream, this.f38c);
        c(byteArrayOutputStream, this.f40d);
        b((OutputStream) byteArrayOutputStream, this.f41e);
        byteArrayOutputStream.write(this.a);
        b((OutputStream) byteArrayOutputStream, this.f42f);
        a((OutputStream) byteArrayOutputStream, this.c);
        a((OutputStream) byteArrayOutputStream, this.d);
        byteArrayOutputStream.write((byte) this.e);
        byteArrayOutputStream.write((byte) this.f);
        byteArrayOutputStream.write((byte) this.g);
        byteArrayOutputStream.write((byte) this.h);
        b((OutputStream) byteArrayOutputStream, w);
        byteArrayOutputStream.write(this.f32a ? 1 : 0);
        byteArrayOutputStream.write(this.b);
        if (!this.f37b) {
            i2 = 0;
        }
        byteArrayOutputStream.write(i2);
        a((OutputStream) byteArrayOutputStream, this.i);
        a((OutputStream) byteArrayOutputStream, this.f46j);
        a((OutputStream) byteArrayOutputStream, this.k);
        a((OutputStream) byteArrayOutputStream, this.l);
        a((OutputStream) byteArrayOutputStream, this.m);
        a((OutputStream) byteArrayOutputStream, this.n);
        a((OutputStream) byteArrayOutputStream, this.o);
        a((OutputStream) byteArrayOutputStream, this.p);
        a((OutputStream) byteArrayOutputStream, this.q);
        a((OutputStream) byteArrayOutputStream, this.r);
        byteArrayOutputStream.write((byte) this.s);
        a((OutputStream) byteArrayOutputStream, this.t);
        a((OutputStream) byteArrayOutputStream, this.u);
        a((OutputStream) byteArrayOutputStream, this.v);
        b((OutputStream) byteArrayOutputStream, this.f43g);
        a((OutputStream) byteArrayOutputStream, this.f28a, 180);
        a((OutputStream) byteArrayOutputStream, this.f34b, 180);
        byteArrayOutputStream.write(b.f50a);
        b((OutputStream) byteArrayOutputStream, this.f44h);
        b((OutputStream) byteArrayOutputStream, this.f45i);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] bArr = new byte[(byteArray.length + 7)];
        this.f33a = a.C0007a.a(byteArray);
        a(this.f31a, bArr, 0);
        a(this.f33a, bArr, 2);
        a((byte) this.f29a, bArr, 6);
        System.arraycopy(byteArray, 0, bArr, 7, byteArray.length);
        try {
            byteArrayOutputStream.close();
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        } catch (Throwable th) {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
        return Base64.encode(bArr, 0);
    }

    public static void b(OutputStream outputStream, int i2) throws IOException {
        outputStream.write((byte) (i2 >> 24));
        outputStream.write((byte) (i2 >> 16));
        outputStream.write((byte) (i2 >> 8));
        outputStream.write((byte) (i2 >> 0));
    }

    public static void b(OutputStream outputStream, String str) throws IOException {
        if (str == null) {
            outputStream.write(b.f50a);
        } else if (str.trim().length() > 0) {
            outputStream.write((byte) str.length());
            outputStream.write(str.getBytes());
        } else {
            outputStream.write((byte) str.trim().length());
        }
    }

    public static void c(OutputStream outputStream, String str) throws IOException {
        if (str == null || str.trim().length() <= 0) {
            outputStream.write(b.f53b);
            return;
        }
        try {
            long longValue = Long.valueOf(str).longValue();
            outputStream.write((byte) ((int) (longValue >> 48)));
            outputStream.write((byte) ((int) (longValue >> 40)));
            outputStream.write((byte) ((int) (longValue >> 32)));
            outputStream.write((byte) ((int) (longValue >> 24)));
            outputStream.write((byte) ((int) (longValue >> 16)));
            outputStream.write((byte) ((int) (longValue >> 8)));
            outputStream.write((byte) ((int) (longValue >> 0)));
        } catch (Exception e2) {
        }
    }

    public String encode() throws IOException {
        return encode((String) null);
    }

    public String encode(String str) throws IOException {
        TaoLog.Logd(j, "Encode data:" + a());
        this.f44h = str;
        return com.taobao.muniontaobaosdk.p4p.anticheat.a.a(new String(a()));
    }
}
