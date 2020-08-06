package com.loc;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BinaryRequest */
public abstract class ax extends q {
    protected Context a;
    protected t b;

    public ax(Context context, t tVar) {
        if (context != null) {
            this.a = context.getApplicationContext();
        }
        this.b = tVar;
    }

    private static byte[] p() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(u.a("PANDORA$"));
            byteArrayOutputStream.write(new byte[]{1});
            byteArrayOutputStream.write(new byte[]{0});
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                y.a(th, "bre", "gbh");
                return byteArray;
            }
        } catch (Throwable th2) {
            y.a(th2, "bre", "gbh");
        }
        return null;
    }

    private byte[] q() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(new byte[]{3});
            if (i()) {
                byte[] a2 = m.a(this.a, k(), this.b != null && "navi".equals(this.b.a()));
                byteArrayOutputStream.write(u.a(a2.length));
                byteArrayOutputStream.write(a2);
            } else {
                byteArrayOutputStream.write(new byte[]{0, 0});
            }
            byte[] a3 = u.a(f());
            if (a3 == null || a3.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(u.a(a3.length));
                byteArrayOutputStream.write(a3);
            }
            byte[] a4 = u.a(j());
            if (a4 == null || a4.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(u.a(a4.length));
                byteArrayOutputStream.write(a4);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                y.a(th, "bre", "gred");
                return byteArray;
            }
        } catch (Throwable th2) {
            y.a(th2, "bre", "gred");
        }
        return new byte[]{0};
    }

    private byte[] r() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] a_ = a_();
            if (a_ == null || a_.length == 0) {
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray;
                } catch (Throwable th) {
                    y.a(th, "bre", "grrd");
                    return byteArray;
                }
            } else {
                byteArrayOutputStream.write(new byte[]{1});
                byteArrayOutputStream.write(u.a(a_.length));
                byteArrayOutputStream.write(a_);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray2;
                } catch (Throwable th2) {
                    y.a(th2, "bre", "grrd");
                    return byteArray2;
                }
            }
        } catch (Throwable th3) {
            y.a(th3, "bre", "grrd");
        }
        return new byte[]{0};
    }

    private byte[] s() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] e = e();
            if (e == null || e.length == 0) {
                byteArrayOutputStream.write(new byte[]{0});
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray;
                } catch (Throwable th) {
                    y.a(th, "bre", "gred");
                    return byteArray;
                }
            } else {
                byteArrayOutputStream.write(new byte[]{1});
                byte[] a2 = o.a(e);
                byteArrayOutputStream.write(u.a(a2.length));
                byteArrayOutputStream.write(a2);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    return byteArray2;
                } catch (Throwable th2) {
                    y.a(th2, "bre", "gred");
                    return byteArray2;
                }
            }
        } catch (Throwable th3) {
            y.a(th3, "bre", "gred");
        }
        return new byte[]{0};
    }

    public abstract byte[] a_();

    public Map<String, String> b_() {
        String f = k.f(this.a);
        String a2 = m.a();
        String a3 = m.a(this.a, a2, "key=" + f);
        HashMap hashMap = new HashMap();
        hashMap.put("ts", a2);
        hashMap.put("key", f);
        hashMap.put("scode", a3);
        return hashMap;
    }

    public final byte[] d() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(p());
            byteArrayOutputStream.write(q());
            byteArrayOutputStream.write(r());
            byteArrayOutputStream.write(s());
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return byteArray;
            } catch (Throwable th) {
                y.a(th, "bre", "geb");
                return byteArray;
            }
        } catch (Throwable th2) {
            y.a(th2, "bre", "geb");
        }
        return null;
    }

    public abstract byte[] e();

    /* access modifiers changed from: protected */
    public String f() {
        return "2.1";
    }

    public boolean i() {
        return true;
    }

    public String j() {
        return String.format("platform=Android&sdkversion=%s&product=%s", new Object[]{this.b.c(), this.b.a()});
    }

    /* access modifiers changed from: protected */
    public boolean k() {
        return false;
    }
}
