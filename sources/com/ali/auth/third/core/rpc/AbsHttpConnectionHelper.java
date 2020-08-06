package com.ali.auth.third.core.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AbsHttpConnectionHelper {
    protected static ByteArrayOutputStream readResponse(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        while (true) {
            int len = in.read(data);
            if (len == -1) {
                return byteArrayOutputStream;
            }
            byteArrayOutputStream.write(data, 0, len);
        }
    }

    protected static void filterResponseCode(int responseCode) {
        if (responseCode != 200) {
            throw new RuntimeException("http request exception, response code: " + responseCode);
        }
    }

    protected static String getCharset(String contentType) {
        return "utf-8";
    }
}
