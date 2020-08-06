package android.taobao.atlas.startup;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.taobao.atlas.startup.patch.KernalConstants;
import android.text.TextUtils;
import android.util.Log;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;

public class KernalVersionManager {
    private static KernalVersionManager sBaseInfoManager;
    private final File BASELINEINFO;
    private final File BASELINEINFO_DIR;
    private final File BASELINEINFO_NEW;
    public String CURRENT_STORAGE_LOCATION;
    private String CURRENT_UPDATE_BUNDLES;
    private String CURRENT_VERSIONAME;
    private String DEXPATCH_BUNDLES;
    public String DEXPATCH_STORAGE_LOCATION;
    private String LAST_STORAGE_LOCATION;
    private String LAST_UPDATE_BUNDLES;
    private String LAST_VERSIONNAME;
    public boolean cachePreVersion;
    private HashMap<String, String> currentUpdateBundles;
    public ConcurrentHashMap<String, Long> dexPatchBundles;

    private static class SingleTonHolder {
        /* access modifiers changed from: private */
        public static final KernalVersionManager INSTANCE = new KernalVersionManager();

        private SingleTonHolder() {
        }
    }

    public static KernalVersionManager instance() {
        return SingleTonHolder.INSTANCE;
    }

    public String toString() {
        Object[] objArr = new Object[4];
        objArr[0] = this.LAST_VERSIONNAME;
        objArr[1] = this.CURRENT_VERSIONAME;
        objArr[2] = this.LAST_UPDATE_BUNDLES;
        objArr[3] = TextUtils.isEmpty(this.DEXPATCH_BUNDLES) ? "" : this.DEXPATCH_BUNDLES;
        return String.format("%s@%s--%s--dexPatchBundles:%s", objArr);
    }

    public void reset() {
        this.LAST_VERSIONNAME = "";
        this.LAST_UPDATE_BUNDLES = "";
        this.LAST_STORAGE_LOCATION = "";
        this.CURRENT_STORAGE_LOCATION = "";
        this.DEXPATCH_STORAGE_LOCATION = "";
        this.CURRENT_VERSIONAME = "";
        this.CURRENT_UPDATE_BUNDLES = "";
        this.DEXPATCH_BUNDLES = "";
        this.currentUpdateBundles.clear();
        this.dexPatchBundles.clear();
    }

    private KernalVersionManager() {
        this.currentUpdateBundles = new HashMap<>();
        this.dexPatchBundles = new ConcurrentHashMap<>();
        this.cachePreVersion = false;
        this.BASELINEINFO_DIR = new File(KernalConstants.baseContext.getFilesDir().getAbsolutePath() + File.separatorChar + "bundleBaseline");
        this.BASELINEINFO = new File(this.BASELINEINFO_DIR, "updateInfo");
        this.BASELINEINFO_NEW = new File(this.BASELINEINFO_DIR, "updateInfo_new");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0059 A[LOOP:1: B:13:0x0059->B:20:0x0084, LOOP_START, PHI: r11 
      PHI: (r11v4 'retry' int) = (r11v3 'retry' int), (r11v5 'retry' int) binds: [B:12:0x004e, B:20:0x0084] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() {
        /*
            r19 = this;
            android.taobao.atlas.startup.patch.KernalFileLock r16 = android.taobao.atlas.startup.patch.KernalFileLock.getInstance()
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO_DIR
            r17 = r0
            r16.LockExclusive(r17)
            boolean r16 = r19.shouldSyncUpdateInThisProcess()
            if (r16 == 0) goto L_0x0087
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO_NEW
            r16 = r0
            boolean r16 = r16.exists()
            if (r16 == 0) goto L_0x0087
            android.content.Context r16 = android.taobao.atlas.startup.patch.KernalConstants.baseContext
            r0 = r19
            r1 = r16
            r0.killChildProcesses(r1)
            r11 = 3
        L_0x0029:
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r16 = r0
            boolean r16 = r16.exists()
            if (r16 == 0) goto L_0x003e
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r16 = r0
            r16.delete()
        L_0x003e:
            int r11 = r11 + -1
            if (r11 <= 0) goto L_0x004e
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r16 = r0
            boolean r16 = r16.exists()
            if (r16 != 0) goto L_0x0029
        L_0x004e:
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO_NEW
            r16 = r0
            long r12 = r16.length()
            r11 = 3
        L_0x0059:
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO_NEW
            r16 = r0
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r17 = r0
            boolean r16 = r16.renameTo(r17)
            if (r16 == 0) goto L_0x0082
        L_0x006b:
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r16 = r0
            long r16 = r16.length()
            int r16 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
            if (r16 == 0) goto L_0x0087
            java.lang.RuntimeException r16 = new java.lang.RuntimeException
            java.lang.String r17 = "rename baselineinfo fail"
            r16.<init>(r17)
            throw r16
        L_0x0082:
            int r11 = r11 + -1
            if (r11 > 0) goto L_0x0059
            goto L_0x006b
        L_0x0087:
            android.taobao.atlas.startup.patch.KernalFileLock r16 = android.taobao.atlas.startup.patch.KernalFileLock.getInstance()
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO_DIR
            r17 = r0
            r16.unLock(r17)
            java.lang.String r2 = ""
            java.lang.String r15 = ""
            java.lang.String r10 = ""
            java.lang.String r9 = ""
            java.lang.String r8 = ""
            java.lang.String r3 = ""
            java.lang.String r5 = ""
            java.lang.String r4 = ""
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO
            r16 = r0
            boolean r16 = r16.exists()
            if (r16 == 0) goto L_0x0176
            java.io.DataInputStream r7 = new java.io.DataInputStream     // Catch:{ Throwable -> 0x0154 }
            java.io.BufferedInputStream r16 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x0154 }
            java.io.FileInputStream r17 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0154 }
            r0 = r19
            java.io.File r0 = r0.BASELINEINFO     // Catch:{ Throwable -> 0x0154 }
            r18 = r0
            r17.<init>(r18)     // Catch:{ Throwable -> 0x0154 }
            r16.<init>(r17)     // Catch:{ Throwable -> 0x0154 }
            r0 = r16
            r7.<init>(r0)     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r10 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r9 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r8 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r2 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r15 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r3 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r4 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r5 = r7.readUTF()     // Catch:{ Throwable -> 0x0154 }
            boolean r16 = r7.readBoolean()     // Catch:{ Throwable -> 0x0154 }
            r0 = r16
            r1 = r19
            r1.cachePreVersion = r0     // Catch:{ Throwable -> 0x0154 }
            boolean r16 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x0125
            boolean r16 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x0125
            java.io.File r14 = new java.io.File     // Catch:{ Throwable -> 0x0154 }
            r14.<init>(r5)     // Catch:{ Throwable -> 0x0154 }
            boolean r16 = r14.exists()     // Catch:{ Throwable -> 0x0154 }
            if (r16 == 0) goto L_0x011f
            r0 = r19
            java.lang.String r16 = r0.getStorageState(r14)     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r17 = "mounted"
            boolean r16 = r16.equals(r17)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x0125
        L_0x011f:
            java.lang.String r4 = ""
            java.lang.String r5 = ""
        L_0x0125:
            boolean r16 = android.text.TextUtils.isEmpty(r15)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x019a
            boolean r16 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x019a
            java.io.File r14 = new java.io.File     // Catch:{ Throwable -> 0x0154 }
            r14.<init>(r3)     // Catch:{ Throwable -> 0x0154 }
            boolean r16 = r14.exists()     // Catch:{ Throwable -> 0x0154 }
            if (r16 == 0) goto L_0x014b
            r0 = r19
            java.lang.String r16 = r0.getStorageState(r14)     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r17 = "mounted"
            boolean r16 = r16.equals(r17)     // Catch:{ Throwable -> 0x0154 }
            if (r16 != 0) goto L_0x019a
        L_0x014b:
            java.io.IOException r16 = new java.io.IOException     // Catch:{ Throwable -> 0x0154 }
            java.lang.String r17 = "update bundle location storage is not usable"
            r16.<init>(r17)     // Catch:{ Throwable -> 0x0154 }
            throw r16     // Catch:{ Throwable -> 0x0154 }
        L_0x0154:
            r6 = move-exception
            java.lang.String r16 = android.taobao.atlas.startup.patch.KernalConstants.PROCESS
            android.content.Context r17 = android.taobao.atlas.startup.patch.KernalConstants.baseContext
            java.lang.String r17 = r17.getPackageName()
            boolean r16 = r16.equals(r17)
            if (r16 == 0) goto L_0x0166
            r19.rollbackHardly()
        L_0x0166:
            android.content.Context r16 = android.taobao.atlas.startup.patch.KernalConstants.baseContext
            r0 = r19
            r1 = r16
            r0.killChildProcesses(r1)
            int r16 = android.os.Process.myPid()
            android.os.Process.killProcess(r16)
        L_0x0176:
            r0 = r19
            r0.LAST_VERSIONNAME = r10
            r0 = r19
            r0.LAST_UPDATE_BUNDLES = r9
            r0 = r19
            r0.CURRENT_VERSIONAME = r2
            r0 = r19
            r0.CURRENT_UPDATE_BUNDLES = r15
            r0 = r19
            r0.DEXPATCH_BUNDLES = r4
            r0 = r19
            r0.LAST_STORAGE_LOCATION = r8
            r0 = r19
            r0.CURRENT_STORAGE_LOCATION = r3
            r0 = r19
            r0.DEXPATCH_STORAGE_LOCATION = r5
            r19.parseUpdatedBundles()
            return
        L_0x019a:
            r7.close()     // Catch:{ Throwable -> 0x019e }
            goto L_0x0176
        L_0x019e:
            r16 = move-exception
            goto L_0x0176
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.KernalVersionManager.init():void");
    }

    public void removeBaseLineInfo() {
        if (this.BASELINEINFO_DIR.exists()) {
            deleteDirectory(this.BASELINEINFO_DIR);
        }
        File bundleupdate = new File(KernalConstants.baseContext.getFilesDir(), "bundleupdate");
        if (bundleupdate.exists()) {
            deleteDirectory(bundleupdate);
        }
    }

    public void deleteDirectory(File path) {
        File[] files = path.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
            path.delete();
        }
    }

    public String getBaseBundleVersion(String bundleName) {
        return this.currentUpdateBundles.get(bundleName);
    }

    public long getDexPatchBundleVersion(String bundleName) {
        if (this.dexPatchBundles.containsKey(bundleName)) {
            return this.dexPatchBundles.get(bundleName).longValue();
        }
        return -1;
    }

    public Set<String> getUpdateBundles() {
        return this.currentUpdateBundles.keySet();
    }

    public String lastVersionName() {
        return this.LAST_VERSIONNAME;
    }

    public String currentVersionName() {
        return this.CURRENT_VERSIONAME;
    }

    public boolean isCachePreVersion() {
        if (Boolean.FALSE.booleanValue()) {
            Log.e("KernalVersionManager", "can no be inlined");
        }
        return this.cachePreVersion;
    }

    public boolean isUpdated(String bundleName) {
        if (!TextUtils.isEmpty(getBaseBundleVersion(bundleName))) {
            return true;
        }
        return false;
    }

    public boolean isDexPatched(String bundleName) {
        return getDexPatchBundleVersion(bundleName) > 0;
    }

    public synchronized void parseUpdatedBundles() {
        String[] bundles;
        String[] bundles2;
        synchronized (this) {
            if (!TextUtils.isEmpty(this.CURRENT_UPDATE_BUNDLES) && (bundles2 = this.CURRENT_UPDATE_BUNDLES.split(SymbolExpUtil.SYMBOL_SEMICOLON)) != null && bundles2.length > 0) {
                for (String bundleInfo : bundles2) {
                    String[] infoItems = bundleInfo.split(Constant.NLP_CACHE_TYPE);
                    this.currentUpdateBundles.put(infoItems[0], infoItems[1]);
                }
            }
            if (!TextUtils.isEmpty(this.DEXPATCH_BUNDLES) && (bundles = this.DEXPATCH_BUNDLES.split(SymbolExpUtil.SYMBOL_SEMICOLON)) != null && bundles.length > 0) {
                for (String bundleInfo2 : bundles) {
                    String[] infoItems2 = bundleInfo2.split(Constant.NLP_CACHE_TYPE);
                    this.dexPatchBundles.put(infoItems2[0], Long.valueOf(Long.parseLong(infoItems2[1])));
                }
            }
        }
    }

    public void rollbackHardly() {
        try {
            File baseLineDir = this.BASELINEINFO_DIR;
            if (!baseLineDir.exists()) {
                baseLineDir.mkdirs();
            }
            File deprecated = new File(baseLineDir, "deprecated_mark");
            if (!deprecated.exists()) {
                deprecated.createNewFile();
            }
            File file = this.BASELINEINFO;
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    public void rollback() {
        File baseinfoDir = new File(this.BASELINEINFO_DIR.getAbsolutePath());
        if (!baseinfoDir.exists()) {
            baseinfoDir.mkdir();
        }
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(baseinfoDir.getAbsolutePath(), "baselineInfo"))));
            out.writeUTF("");
            out.writeUTF("");
            out.writeUTF("");
            if (this.cachePreVersion) {
                out.writeUTF(this.LAST_VERSIONNAME);
                out.writeUTF(this.LAST_UPDATE_BUNDLES);
                out.writeUTF(this.LAST_STORAGE_LOCATION);
            } else {
                out.writeUTF("");
                out.writeUTF("");
                out.writeUTF("");
            }
            out.writeUTF("");
            out.writeUTF("");
            out.writeBoolean(this.cachePreVersion);
            out.flush();
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
            rollbackHardly();
        }
    }

    public void saveDexPatchInfo(HashMap<String, String> infos, String storageLocation) throws IOException {
        for (Map.Entry entry : infos.entrySet()) {
            if (!entry.getValue().equals("-1") || !this.dexPatchBundles.containsKey(entry.getKey())) {
                this.dexPatchBundles.put((String) entry.getKey(), Long.valueOf(Long.parseLong((String) entry.getValue())));
            } else {
                this.dexPatchBundles.remove(entry.getKey());
            }
        }
        StringBuilder bundleList = new StringBuilder("");
        for (Map.Entry entry2 : this.dexPatchBundles.entrySet()) {
            bundleList.append(entry2.getKey());
            bundleList.append(Constant.NLP_CACHE_TYPE);
            bundleList.append(entry2.getValue());
            bundleList.append(SymbolExpUtil.SYMBOL_SEMICOLON);
        }
        File baseinfoFile = this.BASELINEINFO_DIR;
        if (!baseinfoFile.exists()) {
            baseinfoFile.mkdirs();
        }
        File newBaselineInfoFile = new File(this.BASELINEINFO.getAbsolutePath());
        if (!newBaselineInfoFile.exists()) {
            newBaselineInfoFile.createNewFile();
        }
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(newBaselineInfoFile.getAbsolutePath()))));
        String bundleListStr = bundleList.toString();
        out.writeUTF(this.LAST_VERSIONNAME);
        out.writeUTF(this.LAST_UPDATE_BUNDLES != null ? this.LAST_UPDATE_BUNDLES : "");
        out.writeUTF(this.LAST_STORAGE_LOCATION != null ? this.LAST_STORAGE_LOCATION : "");
        out.writeUTF(TextUtils.isEmpty(this.CURRENT_VERSIONAME) ? KernalConstants.INSTALLED_VERSIONNAME : this.CURRENT_VERSIONAME);
        out.writeUTF(this.CURRENT_UPDATE_BUNDLES);
        out.writeUTF(this.CURRENT_STORAGE_LOCATION);
        out.writeUTF(bundleListStr);
        if (storageLocation == null) {
            storageLocation = "";
        }
        out.writeUTF(storageLocation);
        out.writeBoolean(this.cachePreVersion);
        out.flush();
        out.close();
    }

    public void saveUpdateInfo(String newBaselineVersion, HashMap<String, String> infos, boolean cachePreVersion2, String storageLocation) throws IOException {
        StringBuilder bundleList = new StringBuilder("");
        for (Map.Entry entry : infos.entrySet()) {
            bundleList.append(entry.getKey());
            bundleList.append(Constant.NLP_CACHE_TYPE);
            bundleList.append(entry.getValue());
            bundleList.append(SymbolExpUtil.SYMBOL_SEMICOLON);
        }
        File baseinfoFile = this.BASELINEINFO_DIR;
        if (!baseinfoFile.exists()) {
            baseinfoFile.mkdirs();
        }
        File newBaselineInfoFile = new File(this.BASELINEINFO_NEW.getAbsolutePath());
        if (!newBaselineInfoFile.exists()) {
            newBaselineInfoFile.createNewFile();
        }
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(newBaselineInfoFile.getAbsolutePath()))));
        String bundleListStr = bundleList.toString();
        if (cachePreVersion2) {
            out.writeUTF(!TextUtils.isEmpty(this.CURRENT_VERSIONAME) ? this.CURRENT_VERSIONAME : "");
            out.writeUTF(this.CURRENT_UPDATE_BUNDLES != null ? this.CURRENT_UPDATE_BUNDLES : "");
            out.writeUTF(this.CURRENT_STORAGE_LOCATION != null ? this.CURRENT_STORAGE_LOCATION : "");
        } else {
            out.writeUTF("");
            out.writeUTF("");
            out.writeUTF("");
        }
        out.writeUTF(newBaselineVersion);
        out.writeUTF(bundleListStr);
        if (storageLocation == null) {
            storageLocation = "";
        }
        out.writeUTF(storageLocation);
        out.writeUTF("");
        out.writeUTF("");
        out.writeBoolean(cachePreVersion2);
        out.flush();
        out.close();
    }

    private boolean shouldSyncUpdateInThisProcess() {
        String processName = KernalConstants.PROCESS;
        if (processName == null || (!processName.equals(KernalConstants.baseContext.getPackageName()) && !processName.toLowerCase().contains(":safemode"))) {
            return false;
        }
        return true;
    }

    public void killChildProcesses(Context context) {
        try {
            long uid = (long) context.getApplicationInfo().uid;
            List<ActivityManager.RunningAppProcessInfo> a = ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
            for (int i = 0; i < a.size(); i++) {
                ActivityManager.RunningAppProcessInfo b = a.get(i);
                if (((long) b.uid) == uid && !b.processName.equals(context.getPackageName())) {
                    Process.killProcess(b.pid);
                }
            }
        } catch (Exception e) {
        }
    }

    private String getStorageState(File path) {
        if (path.getAbsolutePath().startsWith(KernalConstants.baseContext.getFilesDir().getAbsolutePath())) {
            return "mounted";
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return Environment.getStorageState(path);
        }
        try {
            if (path.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        } catch (IOException e) {
        }
        return "unknown";
    }
}
