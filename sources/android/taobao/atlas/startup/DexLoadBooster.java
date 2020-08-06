package android.taobao.atlas.startup;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.runtime.AndroidRuntime;
import com.taobao.android.runtime.Dex2OatService;
import com.uc.browser.aerie.DalvikPatch;
import dalvik.system.DexFile;
import java.io.IOException;

public class DexLoadBooster {
    private static final String TAG = "DexLoadBooster";

    public void init(Context context) {
        try {
            boolean isTaobao = "com.taobao.taobao".equals(context.getPackageName());
            if (isYunOS()) {
                Log.d("RuntimeUtils", "- RuntimeUtils init: isYunOS. Invalid disable");
                return;
            }
            if (isTaobao) {
                Dex2OatService.setBootCompleted(false);
            }
            AndroidRuntime.getInstance().init(context, isClassAvailable("com.ali.mobisecenhance.ld.startup.ConfigInfo"));
            if (AndroidRuntime.getInstance().isEnabled()) {
                Log.e("AndroidRuntime", Dex2OatService.class.toString());
                if (Build.VERSION.SDK_INT <= 20) {
                    DalvikPatch.patchIfPossible();
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "Couldn't initialize.", e);
        }
    }

    public void setVerificationEnabled(boolean enabled) {
        AndroidRuntime.getInstance().setVerificationEnabled(enabled);
    }

    public DexFile loadDex(Context context, String sourcePathName, String outputPathName, int flags, boolean interpretOnly) throws IOException {
        return AndroidRuntime.getInstance().loadDex(context, sourcePathName, outputPathName, flags, interpretOnly);
    }

    public boolean isOdexValid(String outputPathName) {
        return AndroidRuntime.getInstance().isOdexValid(outputPathName);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isYunOS() {
        /*
            r11 = this;
            r5 = 1
            r6 = 0
            r2 = 0
            r3 = 0
            java.lang.String r4 = "android.os.SystemProperties"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = "get"
            r8 = 1
            java.lang.Class[] r8 = new java.lang.Class[r8]     // Catch:{ Exception -> 0x005d }
            r9 = 0
            java.lang.Class<java.lang.String> r10 = java.lang.String.class
            r8[r9] = r10     // Catch:{ Exception -> 0x005d }
            java.lang.reflect.Method r1 = r4.getMethod(r7, r8)     // Catch:{ Exception -> 0x005d }
            r4 = 0
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x005d }
            r8 = 0
            java.lang.String r9 = "ro.yunos.version"
            r7[r8] = r9     // Catch:{ Exception -> 0x005d }
            java.lang.Object r4 = r1.invoke(r4, r7)     // Catch:{ Exception -> 0x005d }
            r0 = r4
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x005d }
            r2 = r0
            r4 = 0
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x005d }
            r8 = 0
            java.lang.String r9 = "java.vm.name"
            r7[r8] = r9     // Catch:{ Exception -> 0x005d }
            java.lang.Object r4 = r1.invoke(r4, r7)     // Catch:{ Exception -> 0x005d }
            r0 = r4
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x005d }
            r3 = r0
        L_0x003e:
            if (r3 == 0) goto L_0x004d
            java.lang.String r4 = r3.toLowerCase()
            java.lang.String r7 = "lemur"
            boolean r4 = r4.contains(r7)
            if (r4 != 0) goto L_0x0059
        L_0x004d:
            if (r2 == 0) goto L_0x005b
            java.lang.String r4 = r2.trim()
            int r4 = r4.length()
            if (r4 <= 0) goto L_0x005b
        L_0x0059:
            r4 = r5
        L_0x005a:
            return r4
        L_0x005b:
            r4 = r6
            goto L_0x005a
        L_0x005d:
            r4 = move-exception
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.DexLoadBooster.isYunOS():boolean");
    }

    public static boolean isClassAvailable(String className) {
        if (TextUtils.isEmpty(className)) {
            return false;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (Throwable t) {
            Log.e(TAG, "Unexpected exception when checking if class:" + className + " exists at " + "runtime", t);
            return false;
        }
    }
}
