package com.tvtaobao.voicesdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tvtaobao.voicesdk.R;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.util.BitMapUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QRCodeUtil {
    /* access modifiers changed from: private */
    public static QRCodeUtil mUtil;
    private Context mContext;
    /* access modifiers changed from: private */
    public ImageView mQRCode;
    /* access modifiers changed from: private */
    public DisplayHandler myHandler;

    public static QRCodeUtil getInstance(Context context) {
        if (mUtil == null) {
            mUtil = new QRCodeUtil(context);
        }
        return mUtil;
    }

    private QRCodeUtil(Context context) {
        this.mContext = context;
        if (this.myHandler == null) {
            this.myHandler = new DisplayHandler();
        }
    }

    public void intData(String url, ImageView qrImage) {
        mUtil.mQRCode = qrImage;
        new OkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputInstream = response.body().byteStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = inputInstream.read(buffer);
                        if (len != -1) {
                            outStream.write(buffer, 0, len);
                        } else {
                            outStream.close();
                            inputInstream.close();
                            Message message = new Message();
                            message.what = 1;
                            message.obj = outStream;
                            QRCodeUtil.mUtil.myHandler.sendMessage(message);
                            return;
                        }
                    }
                }
            }
        });
    }

    private static class DisplayHandler extends Handler {
        private DisplayHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    byte[] bmp_buffer = ((ByteArrayOutputStream) msg.obj).toByteArray();
                    Bitmap bitmap = QRCodeUtil.replaceBitmapColor(BitmapFactory.decodeByteArray(bmp_buffer, 0, bmp_buffer.length), 0, -1);
                    if (bitmap != null) {
                        try {
                            Bitmap bitmap2 = QRCodeUtil.create2DCode(QRCodeUtil.handleBitmap(bitmap), QRCodeUtil.mUtil.mQRCode.getWidth(), QRCodeUtil.mUtil.mQRCode.getHeight(), BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.icon_zhifubao));
                            if (bitmap != null) {
                                QRCodeUtil.mUtil.mQRCode.setImageBitmap(bitmap2);
                                return;
                            } else {
                                QRCodeUtil.mUtil.mQRCode.setImageBitmap(bitmap);
                                return;
                            }
                        } catch (WriterException e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                if (mBitmap.getPixel(j, i) == oldColor) {
                    mBitmap.setPixel(j, i, newColor);
                }
            }
        }
        return mBitmap;
    }

    public static String handleBitmap(Bitmap bitmap) {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        int[] pixels = new int[(bitmap.getWidth() * bitmap.getHeight())];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        try {
            return new QRCodeReader().decode(new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels))), hints).getText();
        } catch (ChecksumException e) {
            try {
                e.printStackTrace();
                return "";
            } catch (NotFoundException e2) {
                e2.printStackTrace();
                return "";
            }
        } catch (FormatException e3) {
            e3.printStackTrace();
            return "";
        }
    }

    public static Bitmap create2DCode(String str, int width, int height, Bitmap icon) throws WriterException {
        if (str.isEmpty() || width <= 0 || height <= 0) {
            return null;
        }
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
        if (icon == null) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap);
        Bitmap icon2 = ThumbnailUtils.extractThumbnail(icon, bitmap.getWidth() / 6, bitmap.getHeight() / 6, 2);
        canvas.drawBitmap(icon2, (float) ((bitmap.getWidth() - icon2.getWidth()) / 2), (float) ((bitmap.getHeight() - icon2.getHeight()) / 2), new Paint());
        canvas.save(31);
        canvas.restore();
        return bitmap;
    }
}
