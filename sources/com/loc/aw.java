package com.loc;

import android.text.TextUtils;
import java.net.URLConnection;
import java.util.Map;

/* compiled from: BaseNetManager */
public final class aw {
    public static int a = 0;
    public static String b = "";
    private static aw c;

    /* compiled from: BaseNetManager */
    public interface a {
        URLConnection a();
    }

    public static aw a() {
        if (c == null) {
            c = new aw();
        }
        return c;
    }

    public static ba a(az azVar, boolean z) throws j {
        int a2 = ay.a(azVar);
        ba baVar = null;
        try {
            baVar = a(azVar, z, a2);
        } catch (j e) {
            if (!ay.a(a2)) {
                throw e;
            }
        }
        if ((baVar != null && baVar.a != null && baVar.a.length > 0) || !ay.a(a2)) {
            return baVar;
        }
        try {
            return a(azVar, z, 3);
        } catch (j e2) {
            throw e2;
        }
    }

    public static ba a(az azVar, boolean z, int i) throws j {
        if (azVar == null) {
            try {
                throw new j("requeust is null");
            } catch (j e) {
                throw e;
            } catch (Throwable th) {
                th.printStackTrace();
                throw new j("未知的错误");
            }
        } else if (azVar.c() == null || "".equals(azVar.c())) {
            throw new j("request url is empty");
        } else {
            ay ayVar = new ay(azVar.c, azVar.d, azVar.e == null ? null : azVar.e, z);
            String l = azVar.l();
            String m = azVar.m();
            boolean o = azVar.o();
            String n = azVar.n();
            Map<String, String> b2 = azVar.b();
            byte[] d = azVar.d();
            if (d == null || d.length == 0) {
                String a2 = ay.a(azVar.b_());
                if (!TextUtils.isEmpty(a2)) {
                    d = u.a(a2);
                }
            }
            return ayVar.a(l, m, o, n, b2, d, i);
        }
    }

    public static byte[] a(az azVar) throws j {
        try {
            ba a2 = a(azVar, true);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (j e) {
            throw e;
        } catch (Throwable th) {
            throw new j("未知的错误");
        }
    }

    public static byte[] b(az azVar) throws j {
        try {
            ba a2 = a(azVar, false);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (j e) {
            throw e;
        } catch (Throwable th) {
            y.a(th, "bm", "msp");
            throw new j("未知的错误");
        }
    }

    public static ba c(az azVar) throws j {
        try {
            ba a2 = a(azVar, false);
            if (a2 != null) {
                return a2;
            }
            return null;
        } catch (j e) {
            throw e;
        } catch (Throwable th) {
            y.a(th, "bm", "mp");
            throw new j("未知的错误");
        }
    }
}
