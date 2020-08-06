package android.taobao.atlas.startup.patch.releaser;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceReleaser {
    public static boolean releaseResource(File apkFile, File revisionDir) throws IOException {
        String targetPath;
        if (new File(revisionDir, "newAssets/assets").exists() || new File(revisionDir, "newAssets/AndroidManifest.xml").exists()) {
            return true;
        }
        ZipFile bundleFile = new ZipFile(apkFile);
        Enumeration entries = bundleFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String entryName = zipEntry.getName();
            if ((entryName.startsWith("assets") || entryName.equals("AndroidManifest.xml")) && !entryName.equalsIgnoreCase("../")) {
                int numAttempts = 0;
                boolean isExtractionSuccessful = false;
                while (true) {
                    if (numAttempts >= 3 || isExtractionSuccessful) {
                        break;
                    }
                    numAttempts++;
                    try {
                        if (entryName.equals("AndroidManifest.xml")) {
                            targetPath = String.format("%s%s%s%s%s", new Object[]{revisionDir, File.separator, "newAssets", File.separator, entryName.substring(entryName.lastIndexOf(File.separator) + 1, entryName.length())});
                        } else {
                            targetPath = String.format("%s%s%s%s%s", new Object[]{revisionDir, File.separator, "newAssets/assets", File.separator, entryName.substring(entryName.lastIndexOf(File.separator) + 1, entryName.length())});
                        }
                        if (zipEntry.isDirectory()) {
                            File decompressDirFile = new File(targetPath);
                            if (!decompressDirFile.exists()) {
                                decompressDirFile.mkdirs();
                                break;
                            }
                        } else {
                            File fileDirFile = new File(targetPath.substring(0, targetPath.lastIndexOf(WVNativeCallbackUtil.SEPERATER)));
                            if (!fileDirFile.exists()) {
                                fileDirFile.mkdirs();
                            }
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPath));
                            BufferedInputStream bi = new BufferedInputStream(bundleFile.getInputStream(zipEntry));
                            byte[] readContent = new byte[4096];
                            for (int readCount = bi.read(readContent); readCount != -1; readCount = bi.read(readContent)) {
                                bos.write(readContent, 0, readCount);
                            }
                            isExtractionSuccessful = true;
                            bos.close();
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }
                if (!zipEntry.isDirectory() && entryName.startsWith("assets") && !isExtractionSuccessful) {
                    return false;
                }
            }
        }
        if (!new File(revisionDir, "newAssets/assets").exists() || new File(revisionDir, "newAssets/AndroidManifest.xml").exists()) {
            return true;
        }
        return false;
    }
}
