package com.yunos.tvtaobao.biz.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable;

public class QRCodeManager {
    public static Bitmap create2DCode(String str, int width, int height, Bitmap icon) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, hints);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                short pixel = -1;
                if (matrix.get(x, y)) {
                    pixel = 0;
                }
                bitmap.setPixel(x, y, pixel);
            }
        }
        if (icon != null) {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(icon, (float) ((bitmap.getWidth() - icon.getWidth()) / 2), (float) ((bitmap.getHeight() - icon.getHeight()) / 2), new Paint());
            canvas.save(31);
            canvas.restore();
        }
        return bitmap;
    }
}
