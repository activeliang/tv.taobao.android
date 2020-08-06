package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.service.WVEventId;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.UCMPackageInfo;
import com.uc.webview.export.internal.utility.c;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/* compiled from: ProGuard */
public abstract class o extends UCSubSetupTask<o, o> {
    protected boolean a = false;
    protected boolean b = false;

    /* access modifiers changed from: protected */
    public final void a(UCMPackageInfo uCMPackageInfo) {
        String str = d.w == null ? null : d.w.get("load");
        if (!c.a(str) && !str.equals(uCMPackageInfo.coreCode)) {
            if (str.equals("u3") || str.equals("u4")) {
                throw new UCSetupException((int) WVApiPlugin.REQUEST_PICK_PHOTO, String.format("UCM with core code [%s] is excluded by param load [%s].", new Object[]{this.mUCM.coreCode, str}));
            }
            throw new UCSetupException((int) WVEventId.WEBVIEW_ONCREATE, String.format("UCM param load value [%s] unknown.", new Object[]{str}));
        }
    }

    protected static void a(String str, ClassLoader classLoader) {
        String str2;
        int i = 0;
        if (str != null) {
            try {
                if (str.length() > 0) {
                    String[] split = str.split(",");
                    if (split.length > 0 && (str2 = (String) Class.forName("com.uc.webview.browser.shell.Build", false, classLoader).getField("CORE_VERSION").get((Object) null)) != null && str2.length() > 0) {
                        int length = split.length;
                        while (i < length) {
                            String trim = split[i].trim();
                            if (trim.length() <= 0 || !str2.contains(trim)) {
                                i++;
                            } else {
                                throw new UCSetupException(4013, String.format("UCM version [%s] is excluded by rules [%s].", new Object[]{str2, str}));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new UCSetupException(4012, (Throwable) e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void a(UCMPackageInfo uCMPackageInfo, Context context, ClassLoader classLoader) {
        Method method;
        Object[] objArr;
        boolean z;
        int i = 0;
        try {
            Class<?> loadClass = classLoader.loadClass("com.uc.webview.browser.shell.SdkAuthentication");
            try {
                method = loadClass.getMethod("tryLoadUCCore", new Class[]{Context.class, UCMPackageInfo.class, HashMap.class});
                objArr = new Object[]{context, uCMPackageInfo, this.mOptions};
            } catch (NoSuchMethodException e) {
                try {
                    method = loadClass.getMethod("tryLoadUCCore", new Class[]{Context.class, UCMPackageInfo.class});
                    objArr = new Object[]{context, uCMPackageInfo};
                } catch (NoSuchMethodException e2) {
                    throw new UCSetupException(4015, (Throwable) e2);
                }
            }
            try {
                if (!c.a((Boolean) ReflectionUtil.invoke((Object) null, loadClass, method, objArr))) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    throw new UCSetupException(4017, "tryLoadUCCore return false.");
                }
            } catch (UCSetupException e3) {
                throw e3;
            } catch (Throwable th) {
                String message = th.getMessage();
                int indexOf = message.indexOf("9");
                if (indexOf == 0) {
                    try {
                        i = c.b(message.substring(indexOf, indexOf + 4));
                    } catch (Exception e4) {
                    }
                }
                if (i >= 9000) {
                    throw new UCSetupException(i, th);
                }
                throw new UCSetupException(4016, th);
            }
        } catch (ClassNotFoundException e5) {
            throw new UCSetupException(4014, (Throwable) e5);
        }
    }

    protected static void b(UCMPackageInfo uCMPackageInfo, Context context, ClassLoader classLoader) {
        String[][] strArr;
        String[][] strArr2;
        try {
            Class<?> cls = Class.forName("com.uc.webview.browser.shell.NativeLibraries", true, classLoader);
            if (cls != null) {
                Field declaredField = cls.getDeclaredField("LIBRARIES");
                declaredField.setAccessible(true);
                strArr2 = (String[][]) declaredField.get((Object) null);
            } else {
                strArr2 = null;
            }
            strArr = strArr2;
        } catch (Throwable th) {
            strArr = null;
        }
        if (strArr != null) {
            String str = uCMPackageInfo.soDirPath;
            if (str == null) {
                str = context.getApplicationInfo().nativeLibraryDir;
            }
            for (String[] strArr3 : strArr) {
                String str2 = strArr3[0];
                long c = c.c(strArr3[1]);
                File file = new File(str, str2);
                if (file.length() != c) {
                    throw new UCSetupException(1008, String.format("So file [%s] with length [%d] mismatch to predefined [%d].", new Object[]{file, Long.valueOf(file.length()), Long.valueOf(c)}));
                }
            }
        }
    }
}
