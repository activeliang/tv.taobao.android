package android.taobao.atlas.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.taobao.atlas.framework.Framework;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;

public class SoLoader {
    private static final File LIB_DIR = new File(Framework.STORAGE_LOCATION, "lib");

    static {
        if (!LIB_DIR.exists()) {
            LIB_DIR.mkdirs();
        }
    }

    public static void loadLibrary(String libName) {
        if (TextUtils.isEmpty(libName)) {
            throw new RuntimeException("can not load library without a name");
        }
        try {
            Runtime.getRuntime().loadLibrary(libName);
        } catch (UnsatisfiedLinkError error) {
            if (!LIB_DIR.exists()) {
                LIB_DIR.mkdirs();
            }
            if (!LIB_DIR.exists()) {
                throw error;
            }
            String soName = System.mapLibraryName(libName);
            if (supportArmeabi()) {
                if (findLocalLibrary(soName) == null) {
                    extractSoFromApk(soName);
                }
                File soFile = findLocalLibrary(soName);
                if (soFile != null) {
                    System.load(soFile.getAbsolutePath());
                    return;
                }
                throw error;
            }
        }
    }

    @TargetApi(21)
    private static boolean supportArmeabi() {
        if (Build.VERSION.SDK_INT >= 21) {
            String[] abis = Build.SUPPORTED_ABIS;
            if (abis != null) {
                for (String abi : abis) {
                    if (abi.equalsIgnoreCase("armeabi")) {
                        return true;
                    }
                }
            }
        } else if (Build.CPU_ABI.contains("armeabi") || Build.CPU_ABI2.contains("armeabi")) {
            return true;
        }
        return false;
    }

    private static File findLocalLibrary(String libraryName) {
        if (LIB_DIR.exists()) {
            File soFile = new File(LIB_DIR, libraryName);
            if (soFile.exists()) {
                return soFile;
            }
        }
        return null;
    }

    /* JADX INFO: finally extract failed */
    private static synchronized void extractSoFromApk(String soName) {
        ZipEntry targetEntry;
        synchronized (SoLoader.class) {
            AtlasFileLock.getInstance().LockExclusive(LIB_DIR);
            if (findLocalLibrary(soName) != null) {
                AtlasFileLock.getInstance().unLock(LIB_DIR);
            } else {
                int retryCount = 2;
                do {
                    retryCount--;
                    try {
                        String entryName = String.format("lib/armeabi/%s", new Object[]{soName});
                        if (!(ApkUtils.getApk() == null || (targetEntry = ApkUtils.getApk().getEntry(entryName)) == null)) {
                            File targetFile = new File(LIB_DIR, soName);
                            File targetFileTmp = new File(LIB_DIR, soName + ".tmp");
                            if (!targetFileTmp.exists() || targetFileTmp.length() != targetEntry.getSize()) {
                                if (targetFileTmp.exists()) {
                                    targetFileTmp.delete();
                                }
                                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFileTmp.getAbsolutePath()));
                                BufferedInputStream bi = new BufferedInputStream(ApkUtils.getApk().getInputStream(targetEntry));
                                byte[] readContent = new byte[512];
                                for (int readCount = bi.read(readContent); readCount != -1; readCount = bi.read(readContent)) {
                                    bos.write(readContent, 0, readCount);
                                }
                                try {
                                    bos.close();
                                    bi.close();
                                } catch (Throwable th) {
                                }
                            }
                            if (!targetFileTmp.exists()) {
                                continue;
                            } else if (targetFileTmp.length() == targetEntry.getSize()) {
                                targetFileTmp.renameTo(targetFile);
                                if (!targetFile.exists()) {
                                    targetFileTmp.renameTo(targetFile);
                                }
                                if (targetFile.exists()) {
                                    break;
                                }
                            } else {
                                targetFileTmp.delete();
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AtlasFileLock.getInstance().unLock(LIB_DIR);
                    } catch (Throwable th2) {
                        AtlasFileLock.getInstance().unLock(LIB_DIR);
                        throw th2;
                    }
                } while (retryCount > 0);
                break;
                AtlasFileLock.getInstance().unLock(LIB_DIR);
            }
        }
    }
}
