package com.yunos.tvtaobao.uuid.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class GZipUtil {
    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compress((InputStream) bais, (OutputStream) baos);
        byte[] output = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return output;
    }

    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);
        compress((InputStream) fis, (OutputStream) fos);
        fis.close();
        fos.flush();
        fos.close();
        if (delete) {
            file.delete();
        }
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        byte[] data = new byte[1024];
        while (true) {
            int count = is.read(data, 0, 1024);
            if (count != -1) {
                gos.write(data, 0, count);
            } else {
                gos.finish();
                gos.close();
                return;
            }
        }
    }

    public static void compress(String path) throws Exception {
        compress(path, true);
    }

    public static void compress(String path, boolean delete) throws Exception {
        compress(new File(path), delete);
    }

    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress((InputStream) bais, (OutputStream) baos);
        byte[] data2 = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return data2;
    }

    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    public static void decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT, ""));
        decompress((InputStream) fis, (OutputStream) fos);
        fis.close();
        fos.flush();
        fos.close();
        if (delete) {
            file.delete();
        }
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);
        byte[] data = new byte[1024];
        while (true) {
            int count = gis.read(data, 0, 1024);
            if (count != -1) {
                os.write(data, 0, count);
            } else {
                gis.close();
                return;
            }
        }
    }

    public static void decompress(String path) throws Exception {
        decompress(path, true);
    }

    public static void decompress(String path, boolean delete) throws Exception {
        decompress(new File(path), delete);
    }
}
