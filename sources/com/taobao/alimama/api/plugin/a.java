package com.taobao.alimama.api.plugin;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.taobao.alimama.api.AbsServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class a {
    private List<IPlugin> a = new ArrayList(5);

    /* renamed from: com.taobao.alimama.api.plugin.a$a  reason: collision with other inner class name */
    private static class C0004a {
        /* access modifiers changed from: private */
        public static final a a = new a();

        private C0004a() {
        }
    }

    public static a b() {
        return C0004a.a;
    }

    private static List<String> c() {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add("com.taobao.alimama.common.plugin.Plugin");
        return arrayList;
    }

    public Map<Class<?>, Class<? extends AbsServiceImpl>> a() {
        HashMap hashMap = new HashMap();
        for (IPlugin services : this.a) {
            Map<Class<?>, Class<? extends AbsServiceImpl>> services2 = services.services();
            if (services2 != null) {
                hashMap.putAll(services2);
            }
        }
        return hashMap;
    }

    public void a(Context context) {
        Log.i(com.taobao.alimama.api.a.a, "init plugin loader...");
        long elapsedRealtime = SystemClock.elapsedRealtime();
        List<String> c = c();
        if (c != null && c.size() != 0) {
            ArrayList<Class> arrayList = new ArrayList<>(5);
            for (String next : c) {
                try {
                    Class<?> loadClass = context.getClassLoader().loadClass(next);
                    if (IPlugin.class.isAssignableFrom(loadClass)) {
                        arrayList.add(loadClass);
                        Log.i(com.taobao.alimama.api.a.a, "find plugin [" + next + "]");
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            for (Class cls : arrayList) {
                try {
                    this.a.add((IPlugin) cls.newInstance());
                } catch (Throwable th2) {
                    Log.e(com.taobao.alimama.api.a.a, "unable to load plugin " + cls.getName(), th2);
                }
            }
            Log.i(com.taobao.alimama.api.a.a, "plugin loader init done, total find plugin count=" + this.a.size() + ", cost=" + (SystemClock.elapsedRealtime() - elapsedRealtime));
        }
    }
}
