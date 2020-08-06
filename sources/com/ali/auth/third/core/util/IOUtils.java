package com.ali.auth.third.core.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    private static final String TAG = IOUtils.class.getSimpleName();

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static String toString(InputStream in, String charset) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            while (true) {
                int count = in.read(data, 0, 1024);
                if (count != -1) {
                    outStream.write(data, 0, count);
                } else {
                    String str = new String(outStream.toByteArray(), "UTF-8");
                    closeQuietly(in);
                    return str;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            closeQuietly(in);
            throw th;
        }
    }
}
