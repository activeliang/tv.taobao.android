package android.taobao.atlas.startup.patch.releaser;

import android.os.Build;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.startup.patch.KernalConstants;
import android.taobao.atlas.startup.patch.NativePatchMerger;
import android.taobao.atlas.startup.patch.NativePatchVerifier;
import android.taobao.atlas.startup.patch.PatchMerger;
import android.taobao.atlas.startup.patch.PatchVerifier;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeLibReleaser {
    public static final String SO_PATCH_META = "SO-PATCH-INF";
    private static final String SO_PATCH_SUFFIX = ".patch";
    private static PatchMerger patchMerger;

    public static boolean releaseLibs(File apkFile, File reversionDir) throws IOException {
        ZipFile rawZip = new ZipFile(KernalConstants.APK_PATH);
        if (new File(reversionDir, "lib").exists() && new File(reversionDir, "lib").listFiles().length > 0) {
            return true;
        }
        ZipFile bundleFile = new ZipFile(apkFile);
        PatchVerifier patchVerifier = null;
        if (bundleFile.getEntry(SO_PATCH_META) != null) {
            patchVerifier = new NativePatchVerifier(bundleFile.getInputStream(bundleFile.getEntry(SO_PATCH_META)));
        }
        patchMerger = new NativePatchMerger(patchVerifier);
        String extractTag = "lib/armeabi";
        if (Build.CPU_ABI.contains("x86") && bundleFile.getEntry("lib/x86/") != null) {
            extractTag = "lib/x86";
        }
        Enumeration entries = bundleFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String entryName = zipEntry.getName();
            if (entryName.startsWith(extractTag) && !entryName.equalsIgnoreCase("../")) {
                try {
                    String targetPath = String.format("%s%s%s%s%s", new Object[]{mappingInternalDirectory(reversionDir), File.separator, "lib", File.separator, entryName.substring(entryName.lastIndexOf(File.separator) + 1, entryName.length())});
                    if (zipEntry.isDirectory()) {
                        File decompressDirFile = new File(targetPath);
                        if (!decompressDirFile.exists()) {
                            decompressDirFile.mkdirs();
                        }
                    } else {
                        File fileDirFile = new File(targetPath.substring(0, targetPath.lastIndexOf(WVNativeCallbackUtil.SEPERATER)));
                        if (!fileDirFile.exists()) {
                            fileDirFile.mkdirs();
                        }
                        inputStreamToFile(new BufferedInputStream(bundleFile.getInputStream(zipEntry)), new File(targetPath));
                        if (entryName.endsWith(SO_PATCH_SUFFIX)) {
                            File oringalSo = findBaseSo(new File(targetPath).getName().replace(SO_PATCH_SUFFIX, ""), entryName, rawZip, targetPath);
                            Log.e("NativeLibReleaser", "oringal so-->" + oringalSo.getAbsolutePath());
                            File newSo = new File(new File(targetPath).getParentFile(), new File(targetPath).getName().replace(SO_PATCH_SUFFIX, ""));
                            if (!new File(targetPath).exists() || !oringalSo.exists()) {
                                throw new IOException("maindex so merge failed! because " + targetPath + " is not exits or " + oringalSo.getAbsolutePath() + " is not exits!");
                            }
                            patchMerger.sumitForMerge(oringalSo, new File(targetPath), newSo);
                        } else {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return patchMerger.waitForResult();
    }

    private static File findBaseSo(String soName, String entryName, ZipFile rawZip, String targetPath) throws IOException {
        File libDir = new File(RuntimeVariables.androidApplication.getFilesDir().getParentFile(), "lib");
        if (new File(libDir, soName).exists() && new File(libDir, soName).canRead()) {
            return new File(libDir, soName);
        }
        File oringalSo = new File(new File(targetPath).getParentFile(), new File(targetPath).getName().replace(SO_PATCH_SUFFIX, ".old"));
        inputStreamToFile(rawZip.getInputStream(rawZip.getEntry(entryName.replace(SO_PATCH_SUFFIX, ""))), oringalSo);
        return oringalSo;
    }

    private static void inputStreamToFile(InputStream inputStream, File oringalSo) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(oringalSo));
        byte[] readContent = new byte[4096];
        int readCount = inputStream.read(readContent);
        while (readCount != -1) {
            bos.write(readContent, 0, readCount);
            readCount = inputStream.read(readContent);
        }
        bos.close();
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0037  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File mappingInternalDirectory(java.io.File r7) {
        /*
            java.lang.String r2 = r7.getAbsolutePath()
            android.content.Context r3 = android.taobao.atlas.startup.patch.KernalConstants.baseContext
            java.io.File r3 = r3.getFilesDir()
            java.lang.String r3 = r3.getAbsolutePath()
            boolean r2 = r2.startsWith(r3)
            if (r2 != 0) goto L_0x0055
            java.io.File r0 = new java.io.File
            android.content.Context r2 = android.taobao.atlas.startup.patch.KernalConstants.baseContext
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r3 = "storage/com.taobao.maindex_internal/%s"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            java.lang.String r6 = r7.getName()
            r4[r5] = r6
            java.lang.String r3 = java.lang.String.format(r3, r4)
            r0.<init>(r2, r3)
            r1 = 2
        L_0x0031:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x003a
            r0.mkdirs()
        L_0x003a:
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x0050
        L_0x0040:
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x004f
            java.lang.String r2 = "BundleArchiveRevision"
            java.lang.String r3 = "create internal LibDir Failed : com.taobao.maindex"
            android.util.Log.e(r2, r3)
        L_0x004f:
            return r0
        L_0x0050:
            int r1 = r1 + -1
            if (r1 > 0) goto L_0x0031
            goto L_0x0040
        L_0x0055:
            r0 = r7
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.releaser.NativeLibReleaser.mappingInternalDirectory(java.io.File):java.io.File");
    }
}
