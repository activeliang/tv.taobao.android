package com.uc.webview.export.internal.c;

import android.content.Context;
import android.util.AttributeSet;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.ICookieManager;
import com.uc.webview.export.internal.interfaces.IGeolocationPermissions;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IMimeTypeMap;
import com.uc.webview.export.internal.interfaces.IWebStorage;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.utility.ReflectionUtil;

/* compiled from: ProGuard */
public class b {
    public static a b = new a();
    public static Runnable c;

    /* compiled from: ProGuard */
    protected static class a {
        public final Class<?> a = a();
        final ReflectionUtil.BindingMethod<IGlobalSettings> b = new ReflectionUtil.BindingMethod<>(this.a, "getGlobalSettings");
        final ReflectionUtil.BindingMethod<ICookieManager> c = new ReflectionUtil.BindingMethod<>(this.a, "getCookieManager");
        final ReflectionUtil.BindingMethod<UCMobileWebKit> d = new ReflectionUtil.BindingMethod<>(this.a, "getUCMobileWebKit");
        final ReflectionUtil.BindingMethod<IGeolocationPermissions> e = new ReflectionUtil.BindingMethod<>(this.a, "getGeolocationPermissions");
        final ReflectionUtil.BindingMethod<IWebStorage> f = new ReflectionUtil.BindingMethod<>(this.a, "getWebStorage");
        final ReflectionUtil.BindingMethod<IMimeTypeMap> g = new ReflectionUtil.BindingMethod<>(this.a, "getMimeTypeMap");
        final ReflectionUtil.BindingMethod<IWebView> h = new ReflectionUtil.BindingMethod<>(this.a, "createWebView", new Class[]{Context.class});
        final ReflectionUtil.BindingMethod<IWebView> i;
        final ReflectionUtil.BindingMethod<UCMobileWebKit> j;
        final ReflectionUtil.BindingMethod<Boolean> k;
        final ReflectionUtil.BindingMethod<Boolean> l;
        final ReflectionUtil.BindingMethod<Integer> m;
        final ReflectionUtil.BindingMethod<Object> n;
        final ReflectionUtil.BindingMethod<Object> o;
        final ReflectionUtil.BindingMethod<WebResourceResponse> p;

        private static Class<?> a() {
            try {
                return Class.forName(UCMPackageInfo.CORE_FACTORY_IMPL_CLASS, true, d.c);
            } catch (ClassNotFoundException e2) {
                throw new UCSetupException(4007, (Throwable) e2);
            }
        }

        public a() {
            ReflectionUtil.BindingMethod<IWebView> bindingMethod;
            ReflectionUtil.BindingMethod<Boolean> bindingMethod2;
            ReflectionUtil.BindingMethod<Boolean> bindingMethod3 = null;
            try {
                bindingMethod = new ReflectionUtil.BindingMethod<>(this.a, "createWebView", new Class[]{Context.class, AttributeSet.class});
            } catch (Throwable th) {
                bindingMethod = null;
            }
            this.i = bindingMethod;
            this.j = new ReflectionUtil.BindingMethod<>(this.a, "initUCMobileWebKit", new Class[]{Context.class, Boolean.TYPE, Boolean.TYPE});
            this.m = new ReflectionUtil.BindingMethod<>(this.a, "getCoreType");
            this.n = new ReflectionUtil.BindingMethod<>(this.a, "initSDK", new Class[]{Context.class});
            this.o = new ReflectionUtil.BindingMethod<>(this.a, "handlePerformanceTests", new Class[]{String.class});
            this.p = new ReflectionUtil.BindingMethod<>(this.a, "getResponseByUrl", new Class[]{String.class});
            try {
                bindingMethod2 = new ReflectionUtil.BindingMethod<>(this.a, "initUCMobileWebkitCoreSo", new Class[]{Context.class, String.class, String.class, String.class});
            } catch (Throwable th2) {
                bindingMethod2 = null;
            }
            this.l = bindingMethod2;
            try {
                bindingMethod3 = new ReflectionUtil.BindingMethod<>(this.a, "initUCMobileWebkitCoreSo", new Class[]{Context.class, String.class});
            } catch (Throwable th3) {
            }
            this.k = bindingMethod3;
        }
    }

    public static void j() {
        b = new a();
        if (c != null) {
            c.run();
        }
    }

    public static IGlobalSettings k() {
        return b.b.getInstance();
    }

    public static ICookieManager l() {
        return b.c.getInstance();
    }

    public static UCMobileWebKit m() {
        return b.d.getInstance();
    }

    public static IGeolocationPermissions n() {
        return b.e.getInstance();
    }

    public static IWebStorage o() {
        return b.f.getInstance();
    }

    public static IMimeTypeMap p() {
        return b.g.getInstance();
    }

    public static IWebView a(Context context, AttributeSet attributeSet) {
        if (b.i == null) {
            return b.h.invoke(new Object[]{context});
        }
        return b.i.invoke(new Object[]{context, attributeSet});
    }

    public static boolean q() {
        return b.i != null;
    }

    public static UCMobileWebKit a(Context context, boolean z, boolean z2) {
        return b.j.invoke(new Object[]{context, Boolean.valueOf(z), Boolean.valueOf(z2)});
    }

    public static boolean a(Context context, String str, String str2, String str3) {
        if (b.l != null) {
            return b.l.invoke(new Object[]{context, str, str2, str3}).booleanValue();
        }
        return b.k.invoke(new Object[]{context, str3}).booleanValue();
    }

    public static Integer r() {
        return b.m.invoke();
    }

    public static void a(Context context) {
        b.n.invoke(new Object[]{context});
    }

    public static void a(String str) {
        b.o.invoke(new Object[]{str});
    }

    public static WebResourceResponse b(String str) {
        return b.p.invoke(new Object[]{str});
    }
}
