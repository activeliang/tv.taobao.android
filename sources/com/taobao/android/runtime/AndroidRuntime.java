package com.taobao.android.runtime;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Log;
import com.taobao.android.dex.interpret.ARTUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AndroidRuntime {
    static final String PREF_ENABLED = "enabled";
    static final String PREF_EXCLUDE_DEXES = "excludeDexes";
    static final String PREF_EXCLUDE_VERSIONS = "excludeVersions";
    static final String PREF_GROUP_SETTINGS = "runtime";
    private static final String TAG = "RuntimeUtils";
    private static volatile AndroidRuntime mInstance;
    private static Method sGetCurrentInstructionSetMethod;
    private static boolean sGetCurrentInstructionSetMethodFetched;
    private static Method sLoadDexMethod;
    private static boolean sLoadDexMethodFetched;
    private static Class<?> sVMRuntimeClass;
    private static boolean sVMRuntimeClassFetched;
    private Context mContext;
    private boolean mEnabled;
    private String mExcludeDexes;
    private String mExcludeVersions;
    private IMonitor mMonitor;
    private SharedPreferences mPreferences;
    private String mProcessName;

    private AndroidRuntime() {
    }

    public static AndroidRuntime getInstance() {
        if (mInstance == null) {
            synchronized (AndroidRuntime.class) {
                if (mInstance == null) {
                    mInstance = new AndroidRuntime();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, boolean hookedJavaVM) {
        Boolean success;
        boolean z = true;
        this.mContext = context.getApplicationContext();
        this.mPreferences = context.getSharedPreferences(PREF_GROUP_SETTINGS, 0);
        this.mEnabled = this.mPreferences.getBoolean(PREF_ENABLED, true);
        if (!this.mEnabled) {
            Log.e(TAG, "- RuntimeUtils init: mEnabled=" + this.mEnabled);
            return;
        }
        this.mExcludeVersions = this.mPreferences.getString(PREF_EXCLUDE_VERSIONS, (String) null);
        if (this.mExcludeVersions != null) {
            if (this.mExcludeVersions.contains(String.valueOf(Build.VERSION.SDK_INT))) {
                z = false;
            }
            this.mEnabled = z;
            if (!this.mEnabled) {
                Log.e(TAG, "- RuntimeUtils init: mEnabled=" + this.mEnabled + ", excludeVersions=" + this.mExcludeVersions + ", version=" + Build.VERSION.SDK_INT);
                return;
            }
        }
        if ((context.getApplicationInfo().flags & 2) == 0 && "OPPO".equalsIgnoreCase(Build.BRAND) && Build.VERSION.SDK_INT == 23) {
            Log.e(TAG, "- AndroidRuntime init: Build.VERSION.SDK_INT=23, BRAND=OPPO, is disabled");
            this.mEnabled = false;
            return;
        }
        Log.e(TAG, "- RuntimeUtils init: mEnabled=" + this.mEnabled);
        this.mExcludeDexes = this.mPreferences.getString(PREF_EXCLUDE_DEXES, (String) null);
        if (VMUtil.IS_VM_ART) {
            success = Boolean.valueOf(ARTUtils.init(context, hookedJavaVM));
        } else {
            success = Boolean.valueOf(DalvikUtils.init());
        }
        trace("init", success);
        Log.e(TAG, "- RuntimeUtils init: success=" + success);
    }

    public void setVerificationEnabled(boolean enabled) {
        Boolean success;
        if (!this.mEnabled) {
            Log.e(TAG, "- RuntimeUtils setVerificationEnabled disabled.");
            return;
        }
        if (VMUtil.IS_VM_ART) {
            success = ARTUtils.setVerificationEnabled(enabled);
        } else {
            success = DalvikUtils.setClassVerifyMode(enabled ? 3 : 1);
        }
        Log.e(TAG, "- RuntimeUtils setVerificationEnabled: enabled=" + enabled + ", success=" + success);
        trace("setVerificationEnabled", success);
    }

    public void disableJitCompilation() {
        if (!this.mEnabled) {
            Log.e(TAG, "- RuntimeUtils setVerificationEnabled disabled.");
        } else if (!VMUtil.IS_VM_ART) {
            Boolean success = DalvikUtils.disableJitCompilation();
            Log.e(TAG, "- RuntimeUtils disableJitCompilation: success=" + success);
            trace("disableJitCompilation", success);
        }
    }

    public DexFile loadDex(Context context, String sourcePathName, String outputPathName, int flags, boolean interpretOnly) throws IOException {
        return loadDex(context, sourcePathName, outputPathName, flags, (ClassLoader) null, interpretOnly);
    }

    public DexFile loadDex(Context context, String sourcePathName, String outputPathName, int flags, ClassLoader loader, boolean interpretOnly) throws IOException {
        String outputPathName2 = getOutputPathName(sourcePathName, outputPathName);
        if (!this.mEnabled) {
            Log.e(TAG, "- RuntimeUtils loadDex disabled.");
            return loadDex(sourcePathName, outputPathName2, flags, loader);
        } else if (this.mExcludeDexes != null && this.mExcludeDexes.contains(new File(sourcePathName).getName())) {
            Log.e(TAG, "- RuntimeUtils loadDex disabled: sourcePathName=" + sourcePathName + ", mExcludeDexes=" + this.mExcludeDexes);
            return loadDex(sourcePathName, outputPathName2, flags, loader);
        } else if (!VMUtil.IS_VM_ART) {
            return DalvikUtils.loadDex(sourcePathName, outputPathName2, flags);
        } else {
            if (interpretOnly) {
                new File(outputPathName2).delete();
            }
            Boolean dex2oatEnabled = null;
            if (!isOdexValid(outputPathName2, true)) {
                Boolean success = ARTUtils.setIsDex2oatEnabled(false);
                trace("setIsDex2oatEnabled", success);
                Log.d(TAG, "- RuntimeUtils setIsDex2oatEnabled: enabled=false, success=" + success + ", outputPathName=" + outputPathName2);
                dex2oatEnabled = ARTUtils.isDex2oatEnabled();
            }
            long start = System.currentTimeMillis();
            DexFile loadDex = loadDex(sourcePathName, outputPathName2, flags, loader);
            Log.d(TAG, "- RuntimeUtils loadDex: dex2oatEnabled=" + dex2oatEnabled + ", IsVerificationEnabled=" + ARTUtils.IsVerificationEnabled() + ", sourcePathName=" + sourcePathName + ", outputPathName=" + outputPathName2 + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
            if (!interpretOnly && dex2oatEnabled != null && !dex2oatEnabled.booleanValue()) {
                Dex2OatService.start(context, sourcePathName, outputPathName2);
            }
            ARTUtils.setIsDex2oatEnabled(true);
            return loadDex;
        }
    }

    public static String getCurrentInstructionSet() {
        fetchGetCurrentInstructionSetMethod();
        if (sGetCurrentInstructionSetMethod != null) {
            try {
                return (String) sGetCurrentInstructionSetMethod.invoke((Object) null, new Object[0]);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
        return null;
    }

    private static void fetchVMRuntimeClass() {
        if (!sVMRuntimeClassFetched) {
            try {
                sVMRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "Failed to retrieve VMRuntime class", e);
            }
            sVMRuntimeClassFetched = true;
        }
    }

    private static void fetchGetCurrentInstructionSetMethod() {
        if (!sGetCurrentInstructionSetMethodFetched) {
            try {
                fetchVMRuntimeClass();
                sGetCurrentInstructionSetMethod = sVMRuntimeClass.getDeclaredMethod("getCurrentInstructionSet", new Class[0]);
                sGetCurrentInstructionSetMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Failed to retrieve getCurrentInstructionSet method", e);
            }
            sGetCurrentInstructionSetMethodFetched = true;
        }
    }

    static String getOutputPathName(String sourcePathName, String outputPathName) {
        if (Build.VERSION.SDK_INT < 26) {
            return outputPathName;
        }
        try {
            int pos = sourcePathName.lastIndexOf(47);
            if (pos == -1) {
                Log.e(TAG, "Dex location " + sourcePathName + " has no directory.");
                return outputPathName;
            }
            String dir = (sourcePathName.substring(0, pos + 1) + "oat") + WVNativeCallbackUtil.SEPERATER + getCurrentInstructionSet();
            String file = sourcePathName.substring(pos + 1);
            int pos2 = file.lastIndexOf(46);
            if (pos2 == -1) {
                Log.e(TAG, "Dex location " + sourcePathName + " has no extension.");
                return outputPathName;
            }
            return dir + WVNativeCallbackUtil.SEPERATER + file.substring(0, pos2) + ".odex";
        } catch (Exception e) {
            Log.e(TAG, "Failed to get current instruction set", e);
            return outputPathName;
        }
    }

    public DexFile loadDex(String sourcePathName, String outputPathName, int flags, ClassLoader loader) throws IOException {
        if (Build.VERSION.SDK_INT >= 26) {
            fetchLoadDexMethod();
            if (sLoadDexMethod != null) {
                try {
                    return (DexFile) sLoadDexMethod.invoke((Object) null, new Object[]{sourcePathName, outputPathName, Integer.valueOf(flags), loader, Array.newInstance(Class.forName("dalvik.system.DexPathList$Element"), 0)});
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                } catch (ClassNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return DexFile.loadDex(sourcePathName, outputPathName, flags);
    }

    private void fetchLoadDexMethod() {
        if (!sLoadDexMethodFetched) {
            Class<DexFile> cls = DexFile.class;
            try {
                sLoadDexMethod = cls.getDeclaredMethod("loadDex", new Class[]{String.class, String.class, Integer.TYPE, ClassLoader.class, Class.forName("[Ldalvik.system.DexPathList$Element;")});
                sLoadDexMethod.setAccessible(true);
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "Failed to retrieve dalvik.system.DexPathList.Element class", e);
            } catch (NoSuchMethodException e2) {
                Log.i(TAG, "Failed to retrieve loadDex method", e2);
            }
            sLoadDexMethodFetched = true;
        }
    }

    public boolean isOdexValid(String outputPathName) {
        return isOdexValid(outputPathName, false);
    }

    public boolean isOdexValid(String outputPathName, boolean deleteInvalid) {
        if (!VMUtil.IS_VM_ART) {
            return true;
        }
        File file = new File(outputPathName);
        if (file.exists() && file.length() > 0) {
            try {
                OatFile.fromFile(file);
                Log.i(TAG, "- odexFile is valid: odexFile=" + outputPathName);
                return true;
            } catch (Exception e) {
                if (deleteInvalid) {
                    file.delete();
                }
                trace("loadDex", false);
                Log.e(TAG, "- odexFile is invalid: odexFile=" + outputPathName, e);
            }
        }
        return false;
    }

    public void setEnabled(boolean enabled) {
        if (this.mPreferences == null) {
            Log.w(TAG, "Trying to call setEnabled() without init");
            return;
        }
        Log.e(TAG, "- RuntimeUtils setEnabled: enabled=" + enabled);
        this.mEnabled = enabled;
        this.mPreferences.edit().putBoolean(PREF_ENABLED, enabled).commit();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setExcludeVersions(String excludeVersions) {
        if (this.mPreferences == null) {
            Log.w(TAG, "Trying to call setExcludeVersions() without init");
            return;
        }
        Log.d(TAG, "- RuntimeUtils setExcludeVersions: excludeVersions=" + excludeVersions);
        this.mExcludeVersions = excludeVersions;
        this.mPreferences.edit().putString(PREF_EXCLUDE_VERSIONS, excludeVersions).commit();
    }

    public void setExcludeDexes(String excludeDexes) {
        if (this.mPreferences == null) {
            Log.w(TAG, "Trying to call setExcludeDexes() without init");
            return;
        }
        Log.d(TAG, "- RuntimeUtils setExcludeDexes: excludeDexes=" + excludeDexes);
        this.mExcludeDexes = excludeDexes;
        this.mPreferences.edit().putString(PREF_EXCLUDE_DEXES, excludeDexes).commit();
    }

    public void setMonitor(IMonitor monitor) {
        this.mMonitor = monitor;
    }

    private void trace(String typeID, Boolean success) {
        if (this.mMonitor != null) {
            this.mMonitor.trace(typeID, "typeID=" + typeID + ", success=" + success + ", model=" + Build.MODEL + ", version=" + Build.VERSION.RELEASE, success == null ? false : success.booleanValue());
        }
    }

    public String getProcessName() {
        if (this.mProcessName == null) {
            int pid = Process.myPid();
            try {
                for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) this.mContext.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        this.mProcessName = appProcess.processName;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
        return this.mProcessName;
    }

    public void setProcessName(String processName) {
        this.mProcessName = processName;
    }
}
