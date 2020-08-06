package android.taobao.windvane.jsbridge;

import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class WVPluginManager {
    public static final String KEY_METHOD = "method";
    public static final String KEY_NAME = "name";
    private static final String SEPARATOR = "::";
    private static final String TAG = "WVPluginManager";
    private static final Map<String, String> aliasPlugins = new HashMap();
    private static IJsBridgeService jsBridgeService = null;
    private static final Map<String, WVApiPlugin> objPlugins = new HashMap();
    private static final Map<String, WVPluginInfo> plugins = new HashMap();

    public static void registerPlugin(String name, Class<? extends WVApiPlugin> cls) {
        registerPlugin(name, cls, true);
    }

    public static void registerPlugin(String name, Class<? extends WVApiPlugin> cls, boolean customLoader) {
        if (!TextUtils.isEmpty(name) && cls != null) {
            ClassLoader loader = null;
            if (customLoader) {
                loader = cls.getClassLoader();
            }
            plugins.put(name, new WVPluginInfo(cls.getName(), loader));
        }
    }

    public static void registerPluginwithParam(String name, Class<? extends WVApiPlugin> cls, Object... obj) {
        if (!TextUtils.isEmpty(name) && cls != null) {
            WVPluginInfo info = new WVPluginInfo(cls.getName(), cls.getClassLoader());
            if (obj != null) {
                info.setParamObj(obj);
            }
            plugins.put(name, info);
        }
    }

    @Deprecated
    public static void registerPlugin(String name, String fullClassName) {
        registerPlugin(name, fullClassName, (ClassLoader) null);
    }

    public static void registerPlugin(String name, Object obj) {
        try {
            if (obj instanceof WVApiPlugin) {
                objPlugins.put(name, (WVApiPlugin) obj);
            }
        } catch (Throwable e) {
            if (TaoLog.getLogStatus()) {
                TaoLog.e(TAG, "registerPlugin by Object error : " + e.getMessage());
            }
        }
    }

    @Deprecated
    public static void registerPlugin(String name, String fullClassName, ClassLoader loader) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(fullClassName)) {
            plugins.put(name, new WVPluginInfo(fullClassName, loader));
        }
    }

    public static void registerPlugin(String name, String fullClassName, ClassLoader loader, Object... obj) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(fullClassName)) {
            WVPluginInfo info = new WVPluginInfo(fullClassName, loader);
            info.setParamObj(obj);
            plugins.put(name, info);
        }
    }

    public static void registerWVJsBridgeService(IJsBridgeService service) {
        jsBridgeService = service;
    }

    public static void registerAlias(String aliasName, String aliasMethod, String originalName, String originalMethod) {
        if (!plugins.containsKey(originalName) || TextUtils.isEmpty(aliasName) || TextUtils.isEmpty(aliasMethod)) {
            TaoLog.w(TAG, "registerAlias quit, this is no original plugin or alias is invalid.");
        } else if (!TextUtils.isEmpty(originalName) && !TextUtils.isEmpty(originalMethod)) {
            aliasPlugins.put(aliasName + SEPARATOR + aliasMethod, originalName + SEPARATOR + originalMethod);
        }
    }

    public static Map<String, String> getOriginalPlugin(String aliasName, String aliasMethod) {
        int index;
        if (TextUtils.isEmpty(aliasName) || TextUtils.isEmpty(aliasMethod)) {
            TaoLog.w(TAG, "getOriginalPlugin failed, alias is empty.");
            return null;
        }
        String original = aliasPlugins.get(aliasName + SEPARATOR + aliasMethod);
        if (TextUtils.isEmpty(original) || (index = original.indexOf(SEPARATOR)) <= 0) {
            return null;
        }
        String name = original.substring(0, index);
        String method = original.substring(SEPARATOR.length() + index);
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("method", method);
        return map;
    }

    public static void unregisterPlugin(String name) {
        if (plugins.containsKey(name)) {
            plugins.remove(name);
        } else if (objPlugins.containsKey(name)) {
            objPlugins.remove(name);
        }
    }

    public static void unregisterAlias(String aliasName, String aliasMethod) {
        if (TextUtils.isEmpty(aliasName) || TextUtils.isEmpty(aliasMethod)) {
            TaoLog.w(TAG, "unregisterAlias quit, alias is invalid.");
        } else {
            aliasPlugins.remove(aliasName + SEPARATOR + aliasMethod);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0013, code lost:
        if (android.text.TextUtils.isEmpty(r1) != false) goto L_0x0015;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.taobao.windvane.jsbridge.WVApiPlugin createPlugin(java.lang.String r12, android.content.Context r13, android.taobao.windvane.webview.IWVWebView r14) {
        /*
            r9 = 0
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo> r8 = plugins
            java.lang.Object r7 = r8.get(r12)
            android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo r7 = (android.taobao.windvane.jsbridge.WVPluginManager.WVPluginInfo) r7
            if (r7 == 0) goto L_0x0015
            java.lang.String r1 = r7.getClassName()
            boolean r8 = android.text.TextUtils.isEmpty(r1)
            if (r8 == 0) goto L_0x004e
        L_0x0015:
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVApiPlugin> r8 = objPlugins
            boolean r8 = r8.containsKey(r12)
            if (r8 == 0) goto L_0x0026
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVApiPlugin> r8 = objPlugins
            java.lang.Object r8 = r8.get(r12)
            android.taobao.windvane.jsbridge.WVApiPlugin r8 = (android.taobao.windvane.jsbridge.WVApiPlugin) r8
        L_0x0025:
            return r8
        L_0x0026:
            android.taobao.windvane.jsbridge.IJsBridgeService r8 = jsBridgeService
            if (r8 == 0) goto L_0x0077
            android.taobao.windvane.jsbridge.IJsBridgeService r8 = jsBridgeService
            java.lang.Class r3 = r8.getBridgeClass(r12)
            if (r3 != 0) goto L_0x0034
            r8 = r9
            goto L_0x0025
        L_0x0034:
            r8 = 1
            registerPlugin((java.lang.String) r12, (java.lang.Class<? extends android.taobao.windvane.jsbridge.WVApiPlugin>) r3, (boolean) r8)
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo> r8 = plugins
            java.lang.Object r7 = r8.get(r12)
            android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo r7 = (android.taobao.windvane.jsbridge.WVPluginManager.WVPluginInfo) r7
            if (r7 == 0) goto L_0x0048
            java.lang.String r8 = r7.getClassName()
            if (r8 != 0) goto L_0x004a
        L_0x0048:
            r8 = r9
            goto L_0x0025
        L_0x004a:
            java.lang.String r1 = r7.getClassName()
        L_0x004e:
            java.lang.ClassLoader r5 = r7.getClassLoader()     // Catch:{ Exception -> 0x00ad }
            if (r5 != 0) goto L_0x0099
            java.lang.Class r2 = java.lang.Class.forName(r1)     // Catch:{ Exception -> 0x00ad }
        L_0x0058:
            if (r2 == 0) goto L_0x00d7
            java.lang.Class<android.taobao.windvane.jsbridge.WVApiPlugin> r8 = android.taobao.windvane.jsbridge.WVApiPlugin.class
            boolean r8 = r8.isAssignableFrom(r2)     // Catch:{ Exception -> 0x00ad }
            if (r8 == 0) goto L_0x00d7
            java.lang.Object r6 = r2.newInstance()     // Catch:{ Exception -> 0x00ad }
            android.taobao.windvane.jsbridge.WVApiPlugin r6 = (android.taobao.windvane.jsbridge.WVApiPlugin) r6     // Catch:{ Exception -> 0x00ad }
            java.lang.Object r8 = r7.paramObj     // Catch:{ Exception -> 0x00ad }
            if (r8 == 0) goto L_0x009e
            java.lang.Object r8 = r7.paramObj     // Catch:{ Exception -> 0x00ad }
            r6.initialize(r13, r14, r8)     // Catch:{ Exception -> 0x00ad }
        L_0x0075:
            r8 = r6
            goto L_0x0025
        L_0x0077:
            boolean r8 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r8 == 0) goto L_0x0097
            java.lang.String r8 = "WVPluginManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "create plugin failed, plugin not register or empty, "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r10 = r10.toString()
            android.taobao.windvane.util.TaoLog.w(r8, r10)
        L_0x0097:
            r8 = r9
            goto L_0x0025
        L_0x0099:
            java.lang.Class r2 = r5.loadClass(r1)     // Catch:{ Exception -> 0x00ad }
            goto L_0x0058
        L_0x009e:
            boolean r8 = r14 instanceof android.taobao.windvane.webview.WVWebView     // Catch:{ Exception -> 0x00ad }
            if (r8 == 0) goto L_0x00a9
            r0 = r14
            android.taobao.windvane.webview.WVWebView r0 = (android.taobao.windvane.webview.WVWebView) r0     // Catch:{ Exception -> 0x00ad }
            r8 = r0
            r6.initialize((android.content.Context) r13, (android.taobao.windvane.webview.WVWebView) r8)     // Catch:{ Exception -> 0x00ad }
        L_0x00a9:
            r6.initialize((android.content.Context) r13, (android.taobao.windvane.webview.IWVWebView) r14)     // Catch:{ Exception -> 0x00ad }
            goto L_0x0075
        L_0x00ad:
            r4 = move-exception
            java.lang.String r8 = "WVPluginManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "create plugin error: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r11 = ". "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r4.getMessage()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.taobao.windvane.util.TaoLog.e(r8, r10)
        L_0x00d7:
            boolean r8 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r8 == 0) goto L_0x00f7
            java.lang.String r8 = "WVPluginManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "create plugin failed: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r10 = r10.toString()
            android.taobao.windvane.util.TaoLog.w(r8, r10)
        L_0x00f7:
            r8 = r9
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.WVPluginManager.createPlugin(java.lang.String, android.content.Context, android.taobao.windvane.webview.IWVWebView):android.taobao.windvane.jsbridge.WVApiPlugin");
    }

    static class WVPluginInfo {
        private ClassLoader classLoader;
        private String className;
        /* access modifiers changed from: private */
        public Object paramObj;

        WVPluginInfo(String className2) {
            this.className = className2;
        }

        WVPluginInfo(String className2, ClassLoader classLoader2) {
            this.className = className2;
            this.classLoader = classLoader2;
        }

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String className2) {
            this.className = className2;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }

        public void setClassLoader(ClassLoader classLoader2) {
            this.classLoader = classLoader2;
        }

        public Object getParamObj() {
            return this.paramObj;
        }

        public void setParamObj(Object paramObj2) {
            this.paramObj = paramObj2;
        }
    }
}
