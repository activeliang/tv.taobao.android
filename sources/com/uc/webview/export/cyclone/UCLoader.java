package com.uc.webview.export.cyclone;

import dalvik.system.DexClassLoader;

/* compiled from: ProGuard */
public class UCLoader extends DexClassLoader {
    public UCLoader(String str, String str2, String str3, ClassLoader classLoader) {
        super(str, str2, str3, classLoader);
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String str) throws ClassNotFoundException {
        Class<?> cls = null;
        try {
            cls = super.findClass(str);
        } catch (Exception e) {
        }
        if (cls == null) {
            return getParent().loadClass(str);
        }
        return cls;
    }
}
