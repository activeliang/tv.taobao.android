package android.taobao.atlas.versionInfo;

import android.content.pm.PackageInfo;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.WrapperUtil;
import android.text.TextUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BaselineInfoManager {
    private static BaselineInfoManager sBaseInfoManager;
    private String dexPatchStorageLocation;
    private Object mVersionManager;
    private String updateBundleStorageLocation;

    public static synchronized BaselineInfoManager instance() {
        BaselineInfoManager baselineInfoManager;
        synchronized (BaselineInfoManager.class) {
            if (sBaseInfoManager == null) {
                sBaseInfoManager = new BaselineInfoManager();
            }
            baselineInfoManager = sBaseInfoManager;
        }
        return baselineInfoManager;
    }

    private BaselineInfoManager() {
    }

    public String toString() {
        return this.mVersionManager.toString();
    }

    public void reset() {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("reset", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void removeBaseLineInfo() {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("removeBaseLineInfo", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String getBaseBundleVersion(String bundleName) {
        try {
            return (String) this.mVersionManager.getClass().getDeclaredMethod("getBaseBundleVersion", new Class[]{String.class}).invoke(this.mVersionManager, new Object[]{bundleName});
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getDexPatchBundleVersion(String bundleName) {
        try {
            return ((Long) this.mVersionManager.getClass().getDeclaredMethod("getDexPatchBundleVersion", new Class[]{String.class}).invoke(this.mVersionManager, new Object[]{bundleName})).longValue();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ConcurrentHashMap<String, Long> getDexPatchBundles() {
        try {
            Field dexPatchBundles = this.mVersionManager.getClass().getDeclaredField("dexPatchBundles");
            if (!dexPatchBundles.isAccessible()) {
                dexPatchBundles.setAccessible(true);
            }
            return (ConcurrentHashMap) dexPatchBundles.get(this.mVersionManager);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUpdated(String bundleName) {
        try {
            return ((Boolean) this.mVersionManager.getClass().getDeclaredMethod("isUpdated", new Class[]{String.class}).invoke(this.mVersionManager, new Object[]{bundleName})).booleanValue();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDexPatched(String bundleName) {
        try {
            return ((Boolean) this.mVersionManager.getClass().getDeclaredMethod("isDexPatched", new Class[]{String.class}).invoke(this.mVersionManager, new Object[]{bundleName})).booleanValue();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isCachePreVersion() {
        try {
            return ((Boolean) this.mVersionManager.getClass().getDeclaredMethod("isCachePreVersion", new Class[0]).invoke(this.mVersionManager, new Object[0])).booleanValue();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public String currentVersionName() {
        try {
            return (String) this.mVersionManager.getClass().getDeclaredMethod("currentVersionName", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    public void rollbackHardly() {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("rollbackHardly", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String lastVersionName() {
        try {
            return (String) this.mVersionManager.getClass().getDeclaredMethod("lastVersionName", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    public Set<String> getUpdateBundles() {
        try {
            return (Set) this.mVersionManager.getClass().getDeclaredMethod("getUpdateBundles", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            return new HashMap().keySet();
        }
    }

    private void rollbackInternal() {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("rollback", new Class[0]).invoke(this.mVersionManager, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        if (!TextUtils.isEmpty(lastVersionName())) {
            List<String> bundles = new ArrayList<>(getUpdateBundles());
            PackageInfo info = WrapperUtil.getPackageInfo(RuntimeVariables.androidApplication);
            if (!RuntimeVariables.sCachePreVersionBundles || bundles == null || info.versionName.equals(lastVersionName()) || bundles.size() <= 0) {
                rollbackHardly();
            } else {
                rollbackInternal();
            }
        } else {
            rollbackHardly();
        }
    }

    public void saveBaselineInfo(String newBaselineVersion, HashMap<String, String> infos, String storageLocation) throws IOException {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("saveUpdateInfo", new Class[]{String.class, HashMap.class, Boolean.TYPE, String.class}).invoke(this.mVersionManager, new Object[]{newBaselineVersion, infos, Boolean.valueOf(RuntimeVariables.sCachePreVersionBundles), storageLocation});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
        WrapperUtil.persisitKeyPointLog(newBaselineVersion);
    }

    public void saveDexPathInfo(HashMap<String, String> infos, String storageLocation) throws IOException {
        try {
            this.mVersionManager.getClass().getDeclaredMethod("saveDexPatchInfo", new Class[]{HashMap.class, String.class}).invoke(this.mVersionManager, new Object[]{infos, storageLocation});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
    }
}
