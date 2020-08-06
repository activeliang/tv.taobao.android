package android.taobao.atlas.runtime;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class WindowSessionProxy {
    public static void delegateWindowSession(Application mRawApplication) {
        try {
            WindowManager windowManager = (WindowManager) mRawApplication.getSystemService("window");
            Field mGlobalField = windowManager.getClass().getDeclaredField("mGlobal");
            mGlobalField.setAccessible(true);
            final Object windowManagerGlobal = mGlobalField.get(windowManager);
            Method getWindowSession = windowManagerGlobal.getClass().getDeclaredMethod("getWindowSession", new Class[0]);
            getWindowSession.setAccessible(true);
            Field sWindowSession = windowManagerGlobal.getClass().getDeclaredField("sWindowSession");
            sWindowSession.setAccessible(true);
            final Object windowSession = getWindowSession.invoke((Object) null, new Object[0]);
            sWindowSession.set((Object) null, Proxy.newProxyInstance(mRawApplication.getClassLoader(), windowSession.getClass().getInterfaces(), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object res = method.invoke(windowSession, args);
                    if (!method.getName().equals("addToDisplay")) {
                        return res;
                    }
                    if (((Integer) res).intValue() != -2 && ((Integer) res).intValue() != -1) {
                        return res;
                    }
                    Log.e("WindowSessionProxy", "bad token");
                    WindowSessionProxy.clearLastView(windowManagerGlobal);
                    return -6;
                }
            }));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public static void clearLastView(Object windowManagerGlobal) {
        try {
            Field f = windowManagerGlobal.getClass().getDeclaredField("mViews");
            f.setAccessible(true);
            Field f1 = windowManagerGlobal.getClass().getDeclaredField("mRoots");
            f.setAccessible(true);
            Field f2 = windowManagerGlobal.getClass().getDeclaredField("mParams");
            f2.setAccessible(true);
            List<View> mViews = (List) f.get(windowManagerGlobal);
            List<Object> mRoots = (List) f1.get(windowManagerGlobal);
            List<WindowManager.LayoutParams> mParams = (List) f2.get(windowManagerGlobal);
            mViews.remove(mViews.size() - 1);
            mRoots.remove(mRoots.size() - 1);
            mParams.remove(mParams.size() - 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
