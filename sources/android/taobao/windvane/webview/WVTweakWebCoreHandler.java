package android.taobao.windvane.webview;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.util.TaoLog;
import android.webkit.WebView;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WVTweakWebCoreHandler {
    static Handler sProxyHandler = null;

    public static void tryTweakWebCoreHandler() {
        if (Build.VERSION.SDK_INT == 15 && !"SAMSUNG".equalsIgnoreCase(Build.BRAND)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.w("TweakWebCoreHandler", "BRAND: " + Build.BRAND);
            }
            tweakWebCoreHandler();
        }
    }

    public static void tweakWebCoredump(WebView webvew) {
        try {
            Field f = Class.forName("android.webkit.WebView").getDeclaredField("mProvider");
            f.setAccessible(true);
            Object mProvider = f.get(webvew);
            Class<?> WebViewClassic = Class.forName("android.webkit.WebViewClassic");
            WebViewClassic.cast(mProvider);
            Field f2 = WebViewClassic.getDeclaredField("mWebViewCore");
            f2.setAccessible(true);
            Object mWebViewCore = f2.get(mProvider);
            Method sendMessage = Class.forName("android.webkit.WebViewCore").getDeclaredMethod("sendMessage", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
            sendMessage.setAccessible(true);
            sendMessage.invoke(mWebViewCore, new Object[]{Integer.valueOf(Opcodes.REM_FLOAT), 1, 0});
            sendMessage.invoke(mWebViewCore, new Object[]{Integer.valueOf(Opcodes.ADD_DOUBLE), 1, 0});
            Method nativeDumpDisplayTree = WebViewClassic.getDeclaredMethod("nativeDumpDisplayTree", new Class[]{String.class});
            nativeDumpDisplayTree.setAccessible(true);
            nativeDumpDisplayTree.invoke(mProvider, new Object[]{webvew.getUrl()});
        } catch (Throwable e) {
            TaoLog.e("TweakWebCoreHandler", "tweakWebCoreHandler exception: " + e);
        }
    }

    private static void tweakWebCoreHandler() {
        if (sProxyHandler == null) {
            try {
                Field f = Class.forName("android.webkit.WebViewCore").getDeclaredField("sWebCoreHandler");
                f.setAccessible(true);
                Object h = f.get((Object) null);
                Method m = Handler.class.getDeclaredMethod("getIMessenger", (Class[]) null);
                m.setAccessible(true);
                Object mMessenger = m.invoke(h, (Object[]) null);
                sProxyHandler = new WebCoreProxyHandler((Handler) h);
                if (mMessenger != null) {
                    Field f1 = Handler.class.getDeclaredField("mMessenger");
                    f1.setAccessible(true);
                    f1.set(sProxyHandler, mMessenger);
                }
                f.set((Object) null, sProxyHandler);
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("TweakWebCoreHandler", "sWebCoreHandler: " + h);
                }
            } catch (Throwable e) {
                TaoLog.e("TweakWebCoreHandler", "tweakWebCoreHandler exception: " + e);
            }
            if (sProxyHandler == null) {
                sProxyHandler = new Handler();
            }
        }
    }

    static class WebCoreProxyHandler extends Handler {
        final Handler handler;

        public WebCoreProxyHandler(Handler handler2) {
            super(handler2.getLooper());
            this.handler = handler2;
        }

        public void handleMessage(Message msg) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d("WebCoreProxyHandler", "handle message: " + msg.what);
                }
                this.handler.handleMessage(msg);
            } catch (Throwable e) {
                TaoLog.e("WebCoreProxyHandler", "handleMessage exception: " + e);
            }
        }
    }
}
