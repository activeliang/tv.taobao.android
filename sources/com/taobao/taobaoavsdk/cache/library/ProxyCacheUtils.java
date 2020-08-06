package com.taobao.taobaoavsdk.cache.library;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import anetwork.channel.aidl.ParcelableInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ProxyCacheUtils {
    static final int DEFAULT_BUFFER_SIZE = 8192;
    static final String LOG_TAG = "ProxyCache";
    static final int MAX_ARRAY_PREVIEW = 16;

    static String getSupposablyMime(String url) {
        MimeTypeMap mimes = MimeTypeMap.getSingleton();
        String extension = getFileExtensionFromUrl(url);
        if (TextUtils.isEmpty(extension)) {
            return null;
        }
        return mimes.getMimeTypeFromExtension(extension);
    }

    static String getFileExtensionFromUrl(String url) {
        String filename;
        int dotPos;
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf(35);
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            int query = url.lastIndexOf(63);
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf(47);
            if (filenamePos >= 0) {
                filename = url.substring(filenamePos + 1);
            } else {
                filename = url;
            }
            if (!filename.isEmpty() && Pattern.matches("[a-zA-Z_0-9\\@\\.\\-\\(\\)\\%]+", filename) && (dotPos = filename.lastIndexOf(46)) >= 0) {
                return filename.substring(dotPos + 1);
            }
        }
        return "";
    }

    static void assertBuffer(byte[] buffer, long offset, int length) {
        boolean z;
        boolean z2 = true;
        Preconditions.checkNotNull(buffer, "Buffer must be not null!");
        if (offset >= 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Data offset must be positive!");
        if (length < 0 || length > buffer.length) {
            z2 = false;
        }
        Preconditions.checkArgument(z2, "Length must be in range [0..buffer.length]");
    }

    static String preview(byte[] data, int length) {
        int previewLength = Math.min(16, Math.max(length, 0));
        String preview = Arrays.toString(Arrays.copyOfRange(data, 0, previewLength));
        if (previewLength < length) {
            return preview.substring(0, preview.length() - 1) + ", ...]";
        }
        return preview;
    }

    static String encode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding url", e);
        }
    }

    static String decode(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error decoding url", e);
        }
    }

    static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    static void close(ParcelableInputStream closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static String computeMD5(String string) {
        try {
            return bytesToHexString(MessageDigest.getInstance("MD5").digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(bytes[i])}));
        }
        return sb.toString();
    }
}
