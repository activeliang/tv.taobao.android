package android.taobao.atlas.startup;

import android.content.Context;
import android.os.Build;
import dalvik.system.DexFile;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DexFileCompat implements Serializable {
    public static Field mCookie;
    public static Field mFileName;
    public static Method openDexFile;

    static {
        Class<DexFile> cls = DexFile.class;
        try {
            openDexFile = cls.getDeclaredMethod("openDexFile", new Class[]{String.class, String.class, Integer.TYPE});
            openDexFile.setAccessible(true);
            mCookie = DexFile.class.getDeclaredField("mCookie");
            mCookie.setAccessible(true);
            mFileName = DexFile.class.getDeclaredField("mFileName");
            mFileName.setAccessible(true);
        } catch (Throwable e) {
            if (Build.VERSION.SDK_INT > 15) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DexFile loadDex(Context context, String sourcePathName, String outputPathName, int flags) throws Exception {
        if (Build.VERSION.SDK_INT <= 15) {
            return DexFile.loadDex(sourcePathName, outputPathName, flags);
        }
        DexFile dexFile = DexFile.loadDex(context.getApplicationInfo().sourceDir, (String) null, 0);
        try {
            int cookie = ((Integer) openDexFile.invoke((Object) null, new Object[]{sourcePathName, outputPathName, Integer.valueOf(flags)})).intValue();
            mFileName.set(dexFile, sourcePathName);
            mCookie.set(dexFile, Integer.valueOf(cookie));
            return dexFile;
        } catch (Exception e) {
            throw e;
        }
    }
}
