package android.taobao.atlas.patch;

import android.content.Context;
import android.content.pm.PackageManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.util.AtlasFileLock;
import android.taobao.atlas.util.BundleLock;
import android.taobao.atlas.util.IOUtil;
import android.taobao.atlas.util.StringUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.Pair;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.atlas.dexmerge.MergeConstants;
import dalvik.system.DexFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class AtlasHotPatchManager implements BundleListener {
    private static final String HOTFIX_NAME_POSTFIX = ".dex";
    private static final String TAG = "AtlasHotPatchManager";
    private static final AtlasHotPatchManager sPatchManager = new AtlasHotPatchManager();
    private final String MAIN_DEX_PKG = MergeConstants.MAIN_DEX;
    private HashMap<String, String> activePatchs = new HashMap<>();
    private ConcurrentHashMap<String, Long> hotpatchBundles = new ConcurrentHashMap<>();
    private OnPatchActivatedListener mPatchListener;
    private File meta;
    private File sCurrentVersionPatchDir;

    public interface OnPatchActivatedListener {
        void onPatchActivated(String str, String str2, long j);
    }

    public static synchronized AtlasHotPatchManager getInstance() {
        AtlasHotPatchManager atlasHotPatchManager;
        synchronized (AtlasHotPatchManager.class) {
            atlasHotPatchManager = sPatchManager;
        }
        return atlasHotPatchManager;
    }

    private AtlasHotPatchManager() {
        try {
            String versionName = RuntimeVariables.androidApplication.getPackageManager().getPackageInfo(RuntimeVariables.androidApplication.getPackageName(), 0).versionName;
            File sPatchDir = new File(Framework.STORAGE_LOCATION, "hotpatch/");
            if (RuntimeVariables.sCurrentProcessName.equals(RuntimeVariables.androidApplication.getPackageName())) {
                purgeOldPatchsByAppVersion(sPatchDir, versionName);
            }
            this.sCurrentVersionPatchDir = new File(sPatchDir, versionName);
            if (!this.sCurrentVersionPatchDir.exists()) {
                this.sCurrentVersionPatchDir.mkdirs();
            }
            this.meta = new File(this.sCurrentVersionPatchDir, "meta");
            if (this.meta.exists()) {
                try {
                    readPatchInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        patchMainDex();
        Atlas.getInstance().addBundleListener(this);
    }

    public void installHotFixPatch(String targetVersion, HashMap<String, Pair<Long, InputStream>> patchEntries) throws IOException {
        if (!this.sCurrentVersionPatchDir.getName().equals(targetVersion)) {
            throw new IllegalStateException("mismatch version error");
        }
        if (!this.sCurrentVersionPatchDir.exists()) {
            this.sCurrentVersionPatchDir.mkdirs();
        }
        if (this.sCurrentVersionPatchDir.exists()) {
            File sPatchVersionDir = this.sCurrentVersionPatchDir;
            if (!sPatchVersionDir.exists()) {
                sPatchVersionDir.mkdirs();
            }
            if (!sPatchVersionDir.exists()) {
                throw new IOException("crate patch dir fail : " + sPatchVersionDir.getAbsolutePath());
            }
            for (Map.Entry<String, Pair<Long, InputStream>> entry : patchEntries.entrySet()) {
                File patchBundleDir = new File(sPatchVersionDir, entry.getKey());
                patchBundleDir.mkdirs();
                if (patchBundleDir.exists()) {
                    String lockKey = entry.getKey() + ".patch";
                    try {
                        BundleLock.WriteLock(lockKey);
                        if (((Long) entry.getValue().first).longValue() < 0) {
                            this.hotpatchBundles.remove(entry.getKey());
                        } else {
                            File hotFixFile = new File(patchBundleDir, entry.getValue().first + ".dex");
                            installDex((InputStream) entry.getValue().second, hotFixFile);
                            this.hotpatchBundles.put(entry.getKey(), Long.valueOf(((Long) entry.getValue().first).longValue()));
                            String pkgName = entry.getKey();
                            if (MergeConstants.MAIN_DEX.equals(pkgName)) {
                                activePatch(pkgName, new Patch(hotFixFile, RuntimeVariables.androidApplication.getClassLoader()));
                            } else {
                                BundleImpl bundle = (BundleImpl) Atlas.getInstance().getBundle(entry.getKey());
                                if (bundle != null) {
                                    activePatch(entry.getKey(), new Patch(hotFixFile, bundle.getClassLoader()));
                                }
                            }
                            BundleLock.WriteUnLock(lockKey);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        BundleLock.WriteUnLock(lockKey);
                    }
                }
            }
            storePatchInfo();
        }
    }

    public Map<String, Long> getAllInstallPatch() {
        return this.hotpatchBundles;
    }

    public void setPatchListener(OnPatchActivatedListener listener) {
        this.mPatchListener = listener;
    }

    public void storePatchInfo() throws IOException {
        if (!this.meta.exists()) {
            this.meta.getParentFile().mkdirs();
            this.meta.createNewFile();
        }
        if (this.meta.exists()) {
            try {
                AtlasFileLock.getInstance().LockExclusive(this.meta);
                StringBuilder bundleList = new StringBuilder("");
                for (Map.Entry entry : this.hotpatchBundles.entrySet()) {
                    bundleList.append(entry.getKey());
                    bundleList.append(Constant.NLP_CACHE_TYPE);
                    bundleList.append(entry.getValue());
                    bundleList.append(SymbolExpUtil.SYMBOL_SEMICOLON);
                }
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.meta)));
                out.writeUTF(bundleList.toString());
                out.flush();
                IOUtil.quietClose((Closeable) out);
            } finally {
                AtlasFileLock.getInstance().unLock(this.meta);
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public void readPatchInfo() throws IOException {
        String[] bundles;
        try {
            AtlasFileLock.getInstance().LockExclusive(this.meta);
            DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(this.meta)));
            String bundleListStr = input.readUTF();
            if (!TextUtils.isEmpty(bundleListStr) && (bundles = bundleListStr.split(SymbolExpUtil.SYMBOL_SEMICOLON)) != null && bundles.length > 0) {
                for (String bundleInfo : bundles) {
                    String[] infoItems = bundleInfo.split(Constant.NLP_CACHE_TYPE);
                    this.hotpatchBundles.put(infoItems[0], Long.valueOf(Long.parseLong(infoItems[1])));
                }
            }
            IOUtil.quietClose((Closeable) input);
            AtlasFileLock.getInstance().LockExclusive(this.meta);
        } catch (Throwable th) {
            AtlasFileLock.getInstance().LockExclusive(this.meta);
            throw th;
        }
    }

    private void patchMainDex() {
        if (this.hotpatchBundles.containsKey(MergeConstants.MAIN_DEX)) {
            long version = this.hotpatchBundles.get(MergeConstants.MAIN_DEX).longValue();
            File maindexPatchFile = new File(this.sCurrentVersionPatchDir, "com.taobao.maindex/" + version + ".dex");
            if (maindexPatchFile.exists()) {
                purgeOldPatchsOfBundle(maindexPatchFile, version);
                activePatch(MergeConstants.MAIN_DEX, new Patch(maindexPatchFile, RuntimeVariables.androidApplication.getClassLoader()));
            }
        }
    }

    private void patchBundle(Bundle bundle) {
        if (this.hotpatchBundles.get(bundle.getLocation()) != null) {
            String lockKey = bundle.getLocation() + ".patch";
            try {
                BundleLock.WriteLock(lockKey);
                if (this.activePatchs.get(bundle.getLocation()) == null) {
                    long version = this.hotpatchBundles.get(bundle.getLocation()).longValue();
                    File bundlePatchFile = new File(this.sCurrentVersionPatchDir, String.format("%s/%s%s", new Object[]{bundle.getLocation(), Long.valueOf(version), ".dex"}));
                    if (bundlePatchFile.exists()) {
                        purgeOldPatchsOfBundle(bundlePatchFile, version);
                        activePatch(bundle.getLocation(), new Patch(bundlePatchFile, ((BundleImpl) bundle).getClassLoader()));
                    }
                    BundleLock.WriteUnLock(lockKey);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BundleLock.WriteUnLock(lockKey);
            }
        }
    }

    private void activePatch(String patchBundleName, Patch patch) {
        this.activePatchs.put(patchBundleName, patch.file.getAbsolutePath());
        try {
            patch.activate();
            if (this.mPatchListener != null) {
                this.mPatchListener.onPatchActivated(patchBundleName, patch.file.getAbsolutePath(), this.hotpatchBundles.get(patchBundleName).longValue());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (Framework.DEBUG) {
                throw new RuntimeException(e);
            }
        }
    }

    private void installDex(InputStream hotDexStream, File targetFile) throws IOException {
        targetFile.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
        BufferedInputStream bi = new BufferedInputStream(hotDexStream);
        byte[] readContent = new byte[1024];
        for (int readCount = bi.read(readContent); readCount != -1; readCount = bi.read(readContent)) {
            bos.write(readContent, 0, readCount);
        }
        IOUtil.quietClose((Closeable) bos);
        IOUtil.quietClose((Closeable) bi);
    }

    private void purgeOldPatchsByAppVersion(File patchDir, final String currentVersion) {
        File[] oldPatchDirs;
        try {
            if (patchDir.exists() && (oldPatchDirs = patchDir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory() && !file.getName().equals(currentVersion);
                }
            })) != null) {
                for (File oldDir : oldPatchDirs) {
                    Framework.deleteDirectory(oldDir);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void purgeOldPatchsOfBundle(File validPatchFile, final long version) {
        File[] oldpatchs;
        if (RuntimeVariables.sCurrentProcessName.equals(RuntimeVariables.androidApplication.getPackageName()) && (oldpatchs = validPatchFile.getParentFile().listFiles(new FileFilter() {
            public boolean accept(File patchFile) {
                return patchFile.getName().endsWith(".dex") && !patchFile.getName().startsWith(new StringBuilder().append(version).append("").toString());
            }
        })) != null && oldpatchs.length > 0) {
            for (File patchFile : oldpatchs) {
                new Patch(patchFile, (ClassLoader) null).purge();
            }
        }
    }

    public void bundleChanged(BundleEvent event) {
        if (event.getType() == 10087) {
            patchBundle(event.getBundle());
        }
    }

    private static class Patch {
        private PatchClassLoader classLoader;
        /* access modifiers changed from: private */
        public DexFile dexFile;
        /* access modifiers changed from: private */
        public File file;
        private File odexFile;
        private ClassLoader sourceClassLoader;
        private int version;

        private Patch(File patch, ClassLoader sourceClassLoader2) {
            this.file = patch;
            String patchNameWithoutPostFix = StringUtils.substringBetween(patch.getName(), "", ".dex");
            this.odexFile = new File(this.file.getParentFile(), patchNameWithoutPostFix + ".odex");
            this.version = Integer.parseInt(patchNameWithoutPostFix);
            this.sourceClassLoader = sourceClassLoader2;
        }

        public synchronized PatchClassLoader loadDex() {
            PatchClassLoader patchClassLoader;
            if (this.classLoader == null) {
                try {
                    AtlasFileLock.getInstance().LockExclusive(this.odexFile);
                    this.dexFile = (DexFile) RuntimeVariables.sDexLoadBooster.getClass().getDeclaredMethod("loadDex", new Class[]{Context.class, String.class, String.class, Integer.TYPE, Boolean.TYPE}).invoke(RuntimeVariables.sDexLoadBooster, new Object[]{RuntimeVariables.androidApplication, this.file.getAbsolutePath(), this.odexFile.getAbsolutePath(), 0, true});
                    if (this.dexFile != null) {
                        this.classLoader = new PatchClassLoader(this.sourceClassLoader, this);
                    }
                    AtlasFileLock.getInstance().unLock(this.odexFile);
                    patchClassLoader = this.classLoader;
                } catch (Throwable th) {
                    AtlasFileLock.getInstance().unLock(this.odexFile);
                    patchClassLoader = this.classLoader;
                }
            } else {
                patchClassLoader = this.classLoader;
            }
            return patchClassLoader;
        }

        public void activate() {
            if (this.classLoader == null) {
                this.classLoader = loadDex();
            }
            if (this.classLoader != null) {
                ArrayList<Class> entryPointClasses = new ArrayList<>();
                Enumeration<String> classItems = this.dexFile.entries();
                while (classItems.hasMoreElements()) {
                    Class clz = this.classLoader.findPatchClass(classItems.nextElement().replace(WVNativeCallbackUtil.SEPERATER, "."));
                    if (isValidEntryClass(clz)) {
                        entryPointClasses.add(clz);
                    }
                }
                if (entryPointClasses != null) {
                    Iterator<Class> it = entryPointClasses.iterator();
                    while (it.hasNext()) {
                        try {
                            ((IAtlasHotPatch) it.next().newInstance()).fix(RuntimeVariables.androidApplication);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            if (Framework.DEBUG) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }

        private boolean isValidEntryClass(Class clazz) {
            Class[] interfaces = clazz.getInterfaces();
            if (interfaces == null) {
                return false;
            }
            for (Class itf : interfaces) {
                if (itf == IAtlasHotPatch.class) {
                    Process processAnno = (Process) clazz.getAnnotation(Process.class);
                    if ((processAnno != null ? processAnno.value() : RuntimeVariables.androidApplication.getPackageName()).equals(RuntimeVariables.sCurrentProcessName)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void purge() {
            if (this.odexFile.exists()) {
                this.odexFile.delete();
            }
            if (this.file.exists()) {
                this.file.delete();
            }
        }
    }

    private static class PatchClassLoader extends ClassLoader {
        private Patch patch;
        private ClassLoader sourceClassLoader;

        public PatchClassLoader(ClassLoader sourceClassLoader2, Patch patch2) {
            super(Object.class.getClassLoader());
            this.patch = patch2;
            this.sourceClassLoader = sourceClassLoader2;
        }

        /* access modifiers changed from: protected */
        public Class<?> findClass(String className) throws ClassNotFoundException {
            Class clz = findPatchClass(className);
            return clz != null ? clz : this.sourceClassLoader.loadClass(className);
        }

        /* access modifiers changed from: private */
        public Class findPatchClass(String className) {
            return this.patch.dexFile.loadClass(className, this);
        }
    }
}
