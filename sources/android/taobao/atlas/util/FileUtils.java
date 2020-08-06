package android.taobao.atlas.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileUtils {
    public static boolean CheckFileValidation(String path) {
        boolean flag = false;
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            flag = false;
        }
        if (1 != 0 || in == null) {
            return flag;
        }
        try {
            if (in.available() <= 0) {
                return false;
            }
            return flag;
        } catch (IOException e2) {
            return false;
        }
    }

    public static long getUsableSpace(File path) {
        if (path == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            return (path.getUsableSpace() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        }
        if (!path.exists()) {
            return 0;
        }
        StatFs stats = new StatFs(path.getPath());
        return ((((long) stats.getBlockSize()) * ((long) stats.getAvailableBlocks())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public static String getAvailableDisk() {
        try {
            return getUsableSpace(Environment.getDataDirectory()) + "M";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMd5ByFile(File file) throws IOException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            value = new BigInteger(1, md5.digest()).toString(16);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
        return value;
    }
}
