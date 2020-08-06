package android.taobao.atlas.startup.patch;

import android.content.Context;
import android.os.Looper;
import android.taobao.atlas.startup.patch.releaser.BundleReleaser;
import android.text.TextUtils;
import dalvik.system.DexFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipFile;

class KernalBundleArchive {
    private static final int BUFFER_SIZE = 16384;
    private static final String BUNDLE_NAME = "com_taobao_maindex.zip";
    public static final String DEXPATCH_DIR = "dexpatch/";
    public static final String TAG = "KernalBundleArchive";
    private File bundleDir;
    private boolean hasResources = false;
    private File libraryDirectory;
    private Context mContext;
    /* access modifiers changed from: private */
    public DexFile[] odexFile;
    private File revisionDir;

    public KernalBundleArchive(Context context, File bundleDir2, String version, long dexPatchVersion, String process) throws IOException {
        this.mContext = context;
        this.bundleDir = bundleDir2;
        if (process.equals(KernalConstants.baseContext.getPackageName())) {
            purge(version, dexPatchVersion);
        }
        if (dexPatchVersion > 0) {
            this.revisionDir = new File(bundleDir2, "dexpatch/" + dexPatchVersion);
        } else {
            this.revisionDir = new File(bundleDir2, version);
        }
        if (this.revisionDir == null || !this.revisionDir.exists()) {
            throw new IOException("can not find kernal bundle");
        }
        this.libraryDirectory = new File(mappingInternalDirectory(), "lib");
        if (!new KernalBundleRelease(this.revisionDir, true).release(new File(this.revisionDir, BUNDLE_NAME), true) || this.odexFile == null) {
            throw new IOException("process patch failed!");
        }
    }

    public KernalBundleArchive(File bundleDir2, File file, String version, long dexPatchVersion) throws IOException {
        this.bundleDir = bundleDir2;
        if (dexPatchVersion > 0) {
            this.revisionDir = new File(bundleDir2, "dexpatch/" + dexPatchVersion);
        } else {
            this.revisionDir = new File(bundleDir2, version);
        }
        if (!this.revisionDir.exists()) {
            this.revisionDir.mkdirs();
        }
        File bundleFile = new File(this.revisionDir, BUNDLE_NAME);
        if (!file.renameTo(bundleFile)) {
            copyInputStreamToFile(new FileInputStream(file), bundleFile);
        }
        ZipFile zip = new ZipFile(bundleFile);
        this.hasResources = false;
        if (zip.getEntry("resources.arsc") != null || new File(this.revisionDir, "newAssets/assets").exists()) {
            this.hasResources = true;
        }
        zip.close();
        this.libraryDirectory = new File(mappingInternalDirectory(), "lib");
        if (!new KernalBundleRelease(this.revisionDir, false).release(bundleFile, false) || this.odexFile == null) {
            throw new IOException("process mainDex failed!");
        }
    }

    public void purge(String uniqueTag, final long dexPatchVersion) {
        try {
            File[] dexPatchs = new File(this.bundleDir, "dexpatch/").listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    if (dexPatchVersion <= 0 || !filename.equals(dexPatchVersion + "")) {
                        return true;
                    }
                    return false;
                }
            });
            if (dexPatchs != null) {
                for (File patch : dexPatchs) {
                    if (patch.isDirectory()) {
                        deleteDirectory(patch);
                    }
                }
            }
            for (File dir : this.bundleDir.listFiles()) {
                if (dir.isDirectory() && !dir.getName().contains("dexpatch") && !dir.getName().equals(uniqueTag)) {
                    deleteDirectory(dir);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] readContent = new byte[16384];
        while (true) {
            int bytesRead = input.read(readContent);
            if (bytesRead != -1) {
                output.write(readContent, 0, bytesRead);
            } else {
                return;
            }
        }
    }

    public File getLibraryDirectory() {
        return this.libraryDirectory;
    }

    public DexFile[] getOdexFile() {
        return this.odexFile;
    }

    public File getArchiveFile() {
        return new File(this.revisionDir, BUNDLE_NAME);
    }

    public File getRevisionDir() {
        return this.revisionDir;
    }

    public static void deleteDirectory(File path) {
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

    public static String substringAfter(String str, String separator) {
        int pos;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (separator == null || (pos = str.indexOf(separator)) == -1) {
            return "";
        }
        return str.substring(separator.length() + pos);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0022 A[SYNTHETIC, Splitter:B:11:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0027 A[SYNTHETIC, Splitter:B:14:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002c A[SYNTHETIC, Splitter:B:17:0x002c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyInputStreamToFile(java.io.InputStream r7, java.io.File r8) throws java.io.IOException {
        /*
            r3 = 0
            r1 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ all -> 0x005e }
            r4.<init>(r8)     // Catch:{ all -> 0x005e }
            java.nio.channels.FileChannel r1 = r4.getChannel()     // Catch:{ all -> 0x001e }
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r6]     // Catch:{ all -> 0x001e }
        L_0x000f:
            int r5 = r7.read(r0)     // Catch:{ all -> 0x001e }
            if (r5 <= 0) goto L_0x0030
            r6 = 0
            java.nio.ByteBuffer r6 = java.nio.ByteBuffer.wrap(r0, r6, r5)     // Catch:{ all -> 0x001e }
            r1.write(r6)     // Catch:{ all -> 0x001e }
            goto L_0x000f
        L_0x001e:
            r6 = move-exception
            r3 = r4
        L_0x0020:
            if (r7 == 0) goto L_0x0025
            r7.close()     // Catch:{ Exception -> 0x004f }
        L_0x0025:
            if (r1 == 0) goto L_0x002a
            r1.close()     // Catch:{ Exception -> 0x0054 }
        L_0x002a:
            if (r3 == 0) goto L_0x002f
            r3.close()     // Catch:{ Exception -> 0x0059 }
        L_0x002f:
            throw r6
        L_0x0030:
            if (r7 == 0) goto L_0x0035
            r7.close()     // Catch:{ Exception -> 0x0040 }
        L_0x0035:
            if (r1 == 0) goto L_0x003a
            r1.close()     // Catch:{ Exception -> 0x0045 }
        L_0x003a:
            if (r4 == 0) goto L_0x003f
            r4.close()     // Catch:{ Exception -> 0x004a }
        L_0x003f:
            return
        L_0x0040:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0035
        L_0x0045:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003a
        L_0x004a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003f
        L_0x004f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0025
        L_0x0054:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x002a
        L_0x0059:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x002f
        L_0x005e:
            r6 = move-exception
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.KernalBundleArchive.copyInputStreamToFile(java.io.InputStream, java.io.File):void");
    }

    private File mappingInternalDirectory() {
        if (this.revisionDir.getAbsolutePath().startsWith(KernalConstants.baseContext.getFilesDir().getAbsolutePath())) {
            return this.revisionDir;
        }
        return new File(KernalConstants.baseContext.getFilesDir(), String.format("storage/com.taobao.maindex_internal/%s", new Object[]{this.revisionDir.getName()}));
    }

    public class KernalBundleRelease {
        /* access modifiers changed from: private */
        public BundleReleaser mBundlereleaser;

        public KernalBundleRelease(File dir, boolean hasReleasedBefore) {
            this.mBundlereleaser = new BundleReleaser(dir, hasReleasedBefore);
        }

        public boolean release(File bundleFile, boolean start) throws IOException {
            final Boolean[] success = {true};
            this.mBundlereleaser.release(new BundleReleaser.ProcessCallBack() {
                public void onFailed() throws IOException {
                    success[0] = false;
                    DexFile[] unused = KernalBundleArchive.this.odexFile = null;
                    KernalBundleRelease.this.mBundlereleaser.close();
                }

                public void onFinish(int event) {
                    if (event == 2) {
                        DexFile[] unused = KernalBundleArchive.this.odexFile = KernalBundleRelease.this.mBundlereleaser.getDexFile();
                    }
                }

                public void onAllFinish() {
                    KernalBundleRelease.this.mBundlereleaser.close();
                }
            }, bundleFile, start);
            if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
                Looper.loop();
            }
            return success[0].booleanValue();
        }
    }
}
