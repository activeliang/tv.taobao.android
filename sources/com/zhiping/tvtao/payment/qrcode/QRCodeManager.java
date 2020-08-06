package com.zhiping.tvtao.payment.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;

public class QRCodeManager {
    public static Bitmap create2DCode(String str, int width, int height, Bitmap icon) throws WriterException {
        return create2DCode(str, width, height, icon, 0, 0);
    }

    public static Bitmap create2DCode(String str, int width, int height, Bitmap icon, int iconWidth, int iconHeight) throws WriterException {
        int logoWidth;
        int logoHeight;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[(width * height)];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[(y * width) + x] = -16777216;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        if (icon != null) {
            Canvas canvas = new Canvas(bitmap);
            if (iconWidth <= 0) {
                logoWidth = icon.getWidth();
            } else {
                logoWidth = iconWidth;
            }
            if (iconHeight <= 0) {
                logoHeight = icon.getHeight();
            } else {
                logoHeight = iconHeight;
            }
            Rect rect = new Rect(0, 0, icon.getWidth(), icon.getHeight());
            canvas.drawBitmap(icon, rect, new Rect((bitmap.getWidth() - logoWidth) / 2, (bitmap.getHeight() - logoHeight) / 2, (bitmap.getWidth() + logoWidth) / 2, (bitmap.getHeight() + logoHeight) / 2), new Paint());
            canvas.save(31);
            canvas.restore();
        }
        return bitmap;
    }

    public static Bitmap create2DCode(Bitmap codeBm, int width, int height, Bitmap icon, int iconWidth, int iconHeight) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect srcRect = new Rect();
        srcRect.set(0, 0, codeBm.getWidth(), codeBm.getHeight());
        Rect distRect = new Rect();
        distRect.set(0, 0, width, height);
        canvas.drawBitmap(codeBm, srcRect, distRect, (Paint) null);
        if (icon != null) {
            int logoWidth = iconWidth > 0 ? iconWidth : icon.getWidth();
            int logoHeight = iconHeight > 0 ? iconHeight : icon.getHeight();
            canvas.drawBitmap(icon, new Rect(0, 0, icon.getWidth(), icon.getHeight()), new Rect((bitmap.getWidth() - logoWidth) / 2, (bitmap.getHeight() - logoHeight) / 2, (bitmap.getWidth() + logoWidth) / 2, (bitmap.getHeight() + logoHeight) / 2), new Paint());
            canvas.save(31);
            canvas.restore();
        }
        return bitmap;
    }
}
