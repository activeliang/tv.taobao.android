package com.ali.auth.third.offline.login.util;

import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {
    private static Object BarcodeFormat_QR_CODE;
    private static Object EncodeHintType_MARGIN;
    private static Method encodeMethod;
    private static Method getMethod;
    private static Class qrCodereWriterClazz;

    static {
        int i = 0;
        try {
            BarcodeFormat_QR_CODE = getEnumElement("com.google.zxing.BarcodeFormat", "QR_CODE");
            EncodeHintType_MARGIN = getEnumElement("com.google.zxing.EncodeHintType", "MARGIN");
            qrCodereWriterClazz = Class.forName("com.google.zxing.qrcode.QRCodeWriter");
            getMethod = Class.forName("com.google.zxing.common.BitMatrix").getMethod("get", new Class[]{Integer.TYPE, Integer.TYPE});
            Method[] methods = qrCodereWriterClazz.getDeclaredMethods();
            int length = methods.length;
            while (i < length) {
                Method method = methods[i];
                if (!method.getName().equals("encode") || method.getParameterTypes().length != 5) {
                    i++;
                } else {
                    encodeMethod = method;
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getEnumElement(String enumName, String enmuEleName) throws Exception {
        Method enumNameMethod = Enum.class.getMethod("name", new Class[0]);
        for (Object obj : Class.forName(enumName).getEnumConstants()) {
            if (enmuEleName.equals(enumNameMethod.invoke(obj, new Object[0]))) {
                return obj;
            }
        }
        return null;
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height, String error_correction) {
        return createQRCodeBitmap(content, width, height, "UTF-8", error_correction, "0", ViewCompat.MEASURED_STATE_MASK, -1);
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height, String character_set, String error_correction, String margin, int color_black, int color_white) {
        if (TextUtils.isEmpty(content) || width < 0 || height < 0) {
            return null;
        }
        try {
            Map<Object, String> hints = new HashMap<>();
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType_MARGIN, margin);
            }
            Object qrcodeWriter = qrCodereWriterClazz.newInstance();
            Object bitMatrix = encodeMethod.invoke(qrcodeWriter, new Object[]{content, BarcodeFormat_QR_CODE, Integer.valueOf(width), Integer.valueOf(height), hints});
            int[] pixels = new int[(width * height)];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (((Boolean) getMethod.invoke(bitMatrix, new Object[]{Integer.valueOf(x), Integer.valueOf(y)})).booleanValue()) {
                        pixels[(y * width) + x] = color_black;
                    } else {
                        pixels[(y * width) + x] = color_white;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
