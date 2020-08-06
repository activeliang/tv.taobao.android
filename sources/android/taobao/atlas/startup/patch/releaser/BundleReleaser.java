package android.taobao.atlas.startup.patch.releaser;

import android.app.PreVerifier;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.startup.DexFileCompat;
import android.taobao.atlas.startup.patch.KernalConstants;
import android.util.Log;
import com.taobao.android.runtime.AndroidRuntime;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BundleReleaser {
    private static final String DEX_SUFFIX = ".dex";
    public static final int MSG_ID_DEX_OPT_DONE = 2;
    private static final int MSG_ID_DEX_RELEASE_DONE = 1;
    private static final int MSG_ID_RELEASE_DONE = 5;
    private static final int MSG_ID_RELEASE_FAILED = 6;
    private static final int MSG_ID_RESOURCE_RELEASE_DONE = 3;
    private static final int MSG_ID_SOLIB_RELEASE_DONE = 4;
    private static final String TAG = BundleReleaser.class.getSimpleName();
    private File apkFile;
    /* access modifiers changed from: private */
    public DexFile[] dexFiles = null;
    private boolean externalStorage = false;
    private Handler handler;
    private boolean hasReleased;
    private boolean isReleasing = false;
    private ProcessCallBack processCallBack;
    private File reversionDir;
    private ExecutorService service;

    public interface ProcessCallBack {
        void onAllFinish();

        void onFailed() throws IOException;

        void onFinish(int i);
    }

    public DexFile[] getDexFile() {
        return this.dexFiles;
    }

    public BundleReleaser(File reversionDir2, boolean hasReleased2) {
        if (Boolean.FALSE.booleanValue()) {
            String.valueOf(PreVerifier.class);
        }
        this.hasReleased = hasReleased2;
        this.reversionDir = reversionDir2;
        if (!reversionDir2.getAbsolutePath().startsWith(KernalConstants.baseContext.getFilesDir().getAbsolutePath())) {
            this.externalStorage = true;
        }
        if (Looper.getMainLooper() != Looper.myLooper() && Looper.myLooper() == null) {
            Looper.prepare();
        }
        this.handler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                try {
                    return BundleReleaser.this.handleMsg(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        this.service = Executors.newFixedThreadPool(3);
    }

    /* access modifiers changed from: private */
    public boolean handleMsg(Message msg) throws IOException {
        switch (msg.what) {
            case 1:
                if (this.processCallBack != null) {
                    this.processCallBack.onFinish(1);
                }
                dexOptimization();
                return true;
            case 2:
                this.isReleasing = false;
                if (this.processCallBack != null) {
                    this.processCallBack.onFinish(2);
                }
                try {
                    release(ReleaseType.RESOURCE);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return true;
                }
            case 3:
                if (this.processCallBack != null) {
                    this.processCallBack.onFinish(3);
                }
                try {
                    release(ReleaseType.SOLIB);
                    break;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    break;
                }
            case 5:
                if (this.processCallBack == null) {
                    return true;
                }
                this.processCallBack.onAllFinish();
                return true;
            case 6:
                if (this.processCallBack != null) {
                    this.processCallBack.onFailed();
                    break;
                }
                break;
        }
        return false;
    }

    public void release(ProcessCallBack processCallBack2, File bundleFile, boolean start) throws IOException {
        Log.e(TAG, "release doing--->" + this.isReleasing);
        this.apkFile = bundleFile;
        if (!this.isReleasing) {
            this.isReleasing = true;
            this.processCallBack = processCallBack2;
            if (!start) {
                release(ReleaseType.DEX);
                return;
            }
            dexOptimization();
            if (!this.handler.hasMessages(6)) {
                processCallBack2.onFinish(2);
                processCallBack2.onAllFinish();
                return;
            }
            processCallBack2.onFailed();
        }
    }

    public void release(ReleaseType releaseType) throws IOException {
        switch (releaseType) {
            case DEX:
                try {
                    Log.e(TAG, "DexReleaser start!");
                    boolean result = DexReleaser.releaseDexes(this.apkFile, this.reversionDir, this.externalStorage);
                    Log.e(TAG, "DexReleaser done!----->" + result);
                    Message message = this.handler.obtainMessage();
                    if (result) {
                        message.what = 1;
                    } else {
                        message.what = 6;
                    }
                    this.handler.sendMessage(message);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            case RESOURCE:
                try {
                    Log.e(TAG, "ResourceReleaser start!");
                    boolean result2 = ResourceReleaser.releaseResource(this.apkFile, this.reversionDir);
                    Log.e(TAG, "ResourceReleaser done!----->" + result2);
                    Message message2 = this.handler.obtainMessage();
                    if (result2) {
                        message2.what = 3;
                    } else {
                        message2.what = 6;
                    }
                    this.handler.sendMessage(message2);
                    return;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return;
                }
            case SOLIB:
                try {
                    Log.e(TAG, "NativeLibReleaser start!");
                    boolean result3 = NativeLibReleaser.releaseLibs(this.apkFile, this.reversionDir);
                    Log.e(TAG, "NativeLibReleaser done!----->" + result3);
                    Message message3 = this.handler.obtainMessage();
                    if (result3) {
                        message3.what = 5;
                    } else {
                        message3.what = 6;
                    }
                    this.handler.sendMessage(message3);
                    return;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    private void dexOptimization() {
        Log.e(TAG, "dexOptimization start");
        File[] validDexes = this.reversionDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String pathname) {
                return pathname.endsWith(".dex");
            }
        });
        File[] rawMainDexZip = this.reversionDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String pathname) {
                return pathname.endsWith(".zip");
            }
        });
        if (validDexes != null && validDexes.length > 0) {
            validDexes = sortDexs(validDexes);
            if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 24) {
                PatchDexProfile.instance(RuntimeVariables.androidApplication).disableJitCompile();
            }
        } else if (rawMainDexZip != null) {
            validDexes = rawMainDexZip;
        }
        this.dexFiles = new DexFile[validDexes.length];
        if (!this.externalStorage && Build.VERSION.SDK_INT >= 21 && !this.hasReleased) {
            KernalConstants.dexBooster.setVerificationEnabled(true);
            Log.e(TAG, "enable verify");
        }
        if (!this.hasReleased) {
            Log.e(TAG, "start dexopt | hasRelease : " + this.hasReleased);
            final CountDownLatch countDownLatch = new CountDownLatch(validDexes.length);
            for (int i = 0; i < validDexes.length; i++) {
                final int j = i;
                final File[] finalValidDexes = validDexes;
                this.service.submit(new Runnable() {
                    public void run() {
                        BundleReleaser.this.dexFiles[j] = BundleReleaser.this.dexoptInternal(finalValidDexes[j]);
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "start dexopt | hasRelease : " + this.hasReleased);
            for (int i2 = 0; i2 < validDexes.length; i2++) {
                this.dexFiles[i2] = dexoptInternal(validDexes[i2]);
            }
        }
        if (!this.externalStorage && Build.VERSION.SDK_INT >= 21 && !this.hasReleased) {
            KernalConstants.dexBooster.setVerificationEnabled(false);
        }
        Log.e(TAG, "dex opt done");
        this.handler.sendMessage(this.handler.obtainMessage(2));
    }

    private String[] toString(File[] validDexes) {
        String[] ss = new String[validDexes.length];
        for (int i = 0; i < ss.length; i++) {
            ss[i] = validDexes[i].getPath();
        }
        return ss;
    }

    private File[] sortDexs(File[] validDexes) {
        if (validDexes == null) {
            return validDexes;
        }
        List<File> files = Arrays.asList(validDexes);
        Collections.sort(files, new Comparator<File>() {
            public int compare(File lhs, File rhs) {
                if (lhs.getName().equals(DexFormat.DEX_IN_JAR_NAME)) {
                    return -1;
                }
                if (rhs.getName().equals(DexFormat.DEX_IN_JAR_NAME)) {
                    return 1;
                }
                return Integer.valueOf(lhs.getName().substring(7, lhs.getName().indexOf("."))).intValue() - Integer.valueOf(rhs.getName().substring(7, rhs.getName().indexOf("."))).intValue();
            }
        });
        return (File[]) files.toArray(new File[0]);
    }

    /* access modifiers changed from: private */
    public DexFile dexoptInternal(File validDex) {
        String str;
        long startTime = System.currentTimeMillis();
        DexFile dexFile = null;
        String optimizedPath = optimizedPathFor(validDex, dexOptDir());
        try {
            if (!this.externalStorage) {
                dexFile = AndroidRuntime.getInstance().loadDex(validDex.getPath(), optimizedPath, 0, (ClassLoader) null);
                if (!new File(optimizedPath).exists()) {
                    Log.e(TAG, "odex not exist");
                }
            } else if (Build.VERSION.SDK_INT < 21 || !isVMMultidexCapable(System.getProperty("java.vm.version"))) {
                dexFile = DexFileCompat.loadDex(KernalConstants.baseContext, validDex.getPath(), optimizedPath, 0);
            } else {
                optimizedPath = KernalConstants.baseContext.getFilesDir() + File.separator + "fake.dex";
                new File(optimizedPath).createNewFile();
                dexFile = KernalConstants.dexBooster.loadDex(KernalConstants.baseContext, validDex.getPath(), optimizedPath, 0, true);
            }
            if (!verifyDexFile(dexFile, optimizedPath)) {
                this.handler.sendMessage(this.handler.obtainMessage(6));
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.handler.sendMessage(this.handler.obtainMessage(6));
        } finally {
            str = "dex %s consume %d ms";
            Log.e(TAG, String.format(str, new Object[]{validDex.getAbsolutePath(), Long.valueOf(System.currentTimeMillis() - startTime)}));
        }
        return dexFile;
    }

    private boolean verifyDexFile(DexFile dexFile, String optimizedPath) throws IOException {
        if (dexFile == null) {
            return false;
        }
        if (this.externalStorage) {
            return true;
        }
        if (!checkDexValid(dexFile)) {
            return false;
        }
        if (this.hasReleased || Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT >= 26) {
            return true;
        }
        KernalConstants.dexBooster.isOdexValid(optimizedPath);
        return true;
    }

    public boolean checkDexValid(DexFile odexFile) throws IOException {
        return true;
    }

    private String optimizedPathFor(File path, File optimizedDirectory) {
        String fileName = path.getName();
        if (!fileName.endsWith(".dex")) {
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot < 0) {
                fileName = fileName + ".dex";
            } else {
                StringBuilder sb = new StringBuilder(lastDot + 4);
                sb.append(fileName, 0, lastDot);
                sb.append(".dex");
                fileName = sb.toString();
            }
        }
        return new File(optimizedDirectory, fileName).getPath();
    }

    private File dexOptDir() {
        File optDir = new File(this.reversionDir, "opt");
        if (!optDir.exists()) {
            optDir.mkdirs();
        }
        return optDir;
    }

    public void close() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Looper.myLooper().quit();
        }
        this.handler.removeCallbacksAndMessages((Object) null);
        this.handler = null;
        this.service.shutdown();
    }

    static boolean isVMMultidexCapable(String versionString) {
        boolean isMultidexCapable = false;
        if (versionString != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    isMultidexCapable = major > 2 || (major == 2 && Integer.parseInt(matcher.group(2)) >= 1);
                } catch (NumberFormatException e) {
                }
            }
        }
        Log.i(TAG, "VM with version " + versionString + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }
}
