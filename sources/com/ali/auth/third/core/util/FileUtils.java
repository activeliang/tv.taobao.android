package com.ali.auth.third.core.util;

import android.content.Context;
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
        try {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            if (length <= 0) {
                return "";
            }
            byte[] buffer = new byte[length];
            fin.read(buffer);
            String content = new String(buffer, "UTF-8");
            try {
                fin.close();
                return content;
            } catch (Exception e) {
                return content;
            }
        } catch (Exception e2) {
            return "";
        }
    }
}
