package com.ali.user.sso.utils;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {
    public static void writeFileData(Context context, String filename, String message) {
        try {
            FileOutputStream fout = context.openFileOutput(filename, 0);
            fout.write(message.getBytes());
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileData(Context context, String fileName) {
        String content = "";
        try {
            FileInputStream fin = context.openFileInput(fileName);
            if (fin.available() > 0) {
                byte[] b = new byte[fin.available()];
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                while (fin.read(b) != -1) {
                    buffer.write(b);
                }
                String content2 = new String(buffer.toByteArray());
                try {
                    buffer.close();
                    content = content2;
                } catch (Exception e) {
                    e = e;
                    content = content2;
                    e.printStackTrace();
                    return content;
                }
            }
            fin.close();
        } catch (Exception e2) {
            e = e2;
        }
        return content;
    }
}
