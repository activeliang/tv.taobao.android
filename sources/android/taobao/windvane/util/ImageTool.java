package android.taobao.windvane.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageTool {
    private static final int MAX_LENGTH = 1024;

    public static void saveImageToDCIM(final Context context, String url, final Handler handler) {
        ConnectManager.getInstance().connect(url, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onFinish(android.taobao.windvane.connect.HttpResponse r13, int r14) {
                /*
                    r12 = this;
                    boolean r0 = r13.isSuccess()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    if (r0 == 0) goto L_0x0103
                    java.lang.String r0 = "mounted"
                    java.lang.String r1 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    if (r0 == 0) goto L_0x0103
                    java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    byte[] r0 = r13.getData()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r6.<init>(r0)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    android.graphics.Bitmap r7 = android.graphics.BitmapFactory.decodeStream(r6)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    android.content.Context r0 = r2     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.String r1 = "wv_save_image"
                    java.lang.String r2 = ""
                    java.lang.String r11 = android.provider.MediaStore.Images.Media.insertImage(r0, r7, r1, r2)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    if (r11 != 0) goto L_0x003a
                    android.os.Handler r0 = r4     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 405(0x195, float:5.68E-43)
                    r0.sendEmptyMessage(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x0039:
                    return
                L_0x003a:
                    boolean r0 = r7.isRecycled()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    if (r0 != 0) goto L_0x0043
                    r7.recycle()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x0043:
                    boolean r0 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    if (r0 != 0) goto L_0x0092
                    int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 19
                    if (r0 >= r1) goto L_0x0092
                    r8 = 0
                    android.content.Context r0 = r2     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    android.net.Uri r1 = android.net.Uri.parse(r11)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r2 = 1
                    java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r3 = 0
                    java.lang.String r4 = "_data"
                    r2[r3] = r4     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r3 = 0
                    r4 = 0
                    r5 = 0
                    android.database.Cursor r8 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    if (r8 == 0) goto L_0x008d
                    boolean r0 = r8.moveToFirst()     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    if (r0 == 0) goto L_0x008d
                    r0 = 0
                    java.lang.String r10 = r8.getString(r0)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    android.content.Context r0 = r2     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    android.content.Intent r1 = new android.content.Intent     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    java.lang.String r2 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE"
                    java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r3.<init>(r10)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    android.net.Uri r3 = android.net.Uri.fromFile(r3)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r1.<init>(r2, r3)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                    r0.sendBroadcast(r1)     // Catch:{ Exception -> 0x00d6, all -> 0x00ea }
                L_0x008d:
                    if (r8 == 0) goto L_0x0092
                    r8.close()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x0092:
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r0.<init>()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.String r1 = "file://"
                    java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.String r1 = android.os.Environment.DIRECTORY_PICTURES     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.io.File r1 = android.os.Environment.getExternalStoragePublicDirectory(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.String r10 = r0.toString()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 19
                    if (r0 < r1) goto L_0x00f1
                    android.content.Context r0 = r2     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 1
                    java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r2 = 0
                    r1[r2] = r10     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r2 = 0
                    android.taobao.windvane.util.ImageTool$1$1 r3 = new android.taobao.windvane.util.ImageTool$1$1     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r3.<init>()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    android.media.MediaScannerConnection.scanFile(r0, r1, r2, r3)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x00c3:
                    android.os.Handler r0 = r4     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 404(0x194, float:5.66E-43)
                    r0.sendEmptyMessage(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    goto L_0x0039
                L_0x00cc:
                    r9 = move-exception
                    android.os.Handler r0 = r4
                    r1 = 405(0x195, float:5.68E-43)
                    r0.sendEmptyMessage(r1)
                    goto L_0x0039
                L_0x00d6:
                    r0 = move-exception
                    if (r8 == 0) goto L_0x0092
                    r8.close()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    goto L_0x0092
                L_0x00dd:
                    r9 = move-exception
                    r9.printStackTrace()
                    android.os.Handler r0 = r4
                    r1 = 405(0x195, float:5.68E-43)
                    r0.sendEmptyMessage(r1)
                    goto L_0x0039
                L_0x00ea:
                    r0 = move-exception
                    if (r8 == 0) goto L_0x00f0
                    r8.close()     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x00f0:
                    throw r0     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                L_0x00f1:
                    android.content.Context r0 = r2     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    android.content.Intent r1 = new android.content.Intent     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    java.lang.String r2 = "android.intent.action.MEDIA_MOUNTED"
                    android.net.Uri r3 = android.net.Uri.parse(r10)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1.<init>(r2, r3)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r0.sendBroadcast(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    goto L_0x00c3
                L_0x0103:
                    android.os.Handler r0 = r4     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    r1 = 405(0x195, float:5.68E-43)
                    r0.sendEmptyMessage(r1)     // Catch:{ Exception -> 0x00cc, OutOfMemoryError -> 0x00dd }
                    goto L_0x0039
                */
                throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.ImageTool.AnonymousClass1.onFinish(android.taobao.windvane.connect.HttpResponse, int):void");
            }

            public void onError(int code, String message) {
                handler.sendEmptyMessage(405);
            }
        });
    }

    public static Bitmap readZoomImage(String filePath, int maxLength) {
        if (maxLength > 1024) {
            maxLength = 1024;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int be = Math.round(((float) (options.outHeight > options.outWidth ? options.outHeight : options.outWidth)) / ((float) maxLength));
        if (be < 1) {
            be = 1;
        }
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        setInPreferredConfig(options);
        if (bitmap != null) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static void setInPreferredConfig(BitmapFactory.Options options) {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int maxLength) {
        int temp;
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w > h) {
            temp = w;
        } else {
            temp = h;
        }
        if (temp <= maxLength) {
            return bitmap;
        }
        float scale = ((float) maxLength) / ((float) temp);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        bitmap.recycle();
        return createBitmap;
    }

    public static byte[] bitmapToBytes(Bitmap bm, Bitmap.CompressFormat format) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, 100, baos);
        return baos.toByteArray();
    }

    public static Drawable toDrawable(Resources res, String imageStr) {
        if (imageStr == null) {
            return null;
        }
        try {
            try {
                return new BitmapDrawable(res, BitmapFactory.decodeStream(new ByteArrayInputStream(Base64.decode(imageStr, 0))));
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }

    public static int readRotationDegree(String path) {
        int degree = 0;
        if (path == null) {
            return 0;
        }
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotate(Bitmap b, int degree) {
        if (degree == 0 || b == null) {
            return b;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
            if (b == b2) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError e) {
            return b;
        }
    }
}
