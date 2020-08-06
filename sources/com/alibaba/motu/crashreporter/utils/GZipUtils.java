package com.alibaba.motu.crashreporter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class GZipUtils {
    public static final int BUFFER = 1024;

    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compress(bais, baos);
        byte[] output = baos.toByteArray();
        try {
            baos.flush();
        } catch (Exception e) {
        }
        baos.close();
        bais.close();
        return output;
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        byte[] data = new byte[1024];
        while (true) {
            int count = is.read(data, 0, 1024);
            if (count == -1) {
                break;
            }
            gos.write(data, 0, count);
        }
        gos.finish();
        try {
            gos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gos.close();
    }

    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        byte[] data2 = baos.toByteArray();
        try {
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        baos.close();
        bais.close();
        return data2;
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);
        byte[] data = new byte[1024];
        while (true) {
            int count = gis.read(data, 0, 1024);
            if (count != -1) {
                os.write(data, 0, count);
            } else {
                try {
                    gis.close();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
