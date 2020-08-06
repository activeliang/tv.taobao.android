package com.taobao.atlas.update.util;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    private static int BUFFEREDSIZE = 1024;

    public static void unzip(String zipFilename, String outputDirectory) throws IOException {
        File outFile = new File(outputDirectory);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        if (!outFile.exists()) {
            throw new IOException("file not exist");
        }
        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration en = zipFile.entries();
        while (en.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) en.nextElement();
            if (zipEntry.isDirectory()) {
                String dirName = zipEntry.getName();
                new File(outFile.getPath() + File.separator + dirName.substring(0, dirName.length() - 1)).mkdirs();
            } else {
                File f = new File(outFile.getPath() + File.separator + zipEntry.getName());
                if (!f.exists()) {
                    String[] arrFolderName = zipEntry.getName().split(WVNativeCallbackUtil.SEPERATER);
                    String strRealFolder = "";
                    for (int i = 0; i < arrFolderName.length - 1; i++) {
                        strRealFolder = strRealFolder + arrFolderName[i] + File.separator;
                    }
                    new File(outFile.getPath() + File.separator + strRealFolder).mkdirs();
                }
                f.createNewFile();
                InputStream in = zipFile.getInputStream(zipEntry);
                FileOutputStream out = new FileOutputStream(f);
                try {
                    byte[] by = new byte[BUFFEREDSIZE];
                    while (true) {
                        int c = in.read(by);
                        if (c == -1) {
                            break;
                        }
                        out.write(by, 0, c);
                    }
                    out.flush();
                    closeQuitely(out);
                    closeQuitely(in);
                } catch (IOException e) {
                    throw e;
                } catch (Throwable th) {
                    closeQuitely(out);
                    closeQuitely(in);
                    throw th;
                }
            }
        }
        zipFile.close();
    }

    private static void closeQuitely(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
