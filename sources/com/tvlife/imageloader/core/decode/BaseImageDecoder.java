package com.tvlife.imageloader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.tvlife.imageloader.core.assist.ContentLengthInputStream;
import com.tvlife.imageloader.core.assist.ImageScaleType;
import com.tvlife.imageloader.core.assist.ImageSize;
import com.tvlife.imageloader.core.download.BaseImageDownloader;
import com.tvlife.imageloader.core.download.ImageDownloader;
import com.tvlife.imageloader.utils.ImageSizeUtils;
import com.tvlife.imageloader.utils.IoUtils;
import com.tvlife.imageloader.utils.L;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseImageDecoder implements ImageDecoder {
    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";
    protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
    protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d° [%2$s]";
    protected static final String LOG_SABSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
    protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
    private String TAG = "BaseImageDecoder";
    protected final boolean loggingEnabled;
    private Bitmap mBitmap;

    public BaseImageDecoder(boolean loggingEnabled2) {
        this.loggingEnabled = loggingEnabled2;
    }

    public void setTestBitmap(Bitmap bm) {
        this.mBitmap = bm;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0058=Splitter:B:23:0x0058, B:19:0x004e=Splitter:B:19:0x004e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean wirteTofile(java.lang.String r15, java.lang.String r16, com.tvlife.imageloader.core.decode.ImageDecodingInfo r17, java.io.InputStream r18) {
        /*
            r14 = this;
            r1 = 1024(0x400, float:1.435E-42)
            r7 = 0
            java.io.File r3 = new java.io.File
            r3.<init>(r15)
            r5 = 0
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x006e, IOException -> 0x0057 }
            r6.<init>(r3)     // Catch:{ FileNotFoundException -> 0x006e, IOException -> 0x0057 }
            r9 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r9]     // Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x006b, all -> 0x0068 }
        L_0x0012:
            r9 = 0
            int r10 = r4.length     // Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x006b, all -> 0x0068 }
            r0 = r18
            int r8 = r0.read(r4, r9, r10)     // Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x006b, all -> 0x0068 }
            r9 = -1
            if (r8 != r9) goto L_0x0047
            r7 = 1
            com.tvlife.imageloader.utils.IoUtils.closeSilently(r6)
            r5 = 0
            r4 = 0
        L_0x0023:
            boolean r9 = r14.loggingEnabled
            if (r9 == 0) goto L_0x0046
            java.lang.String r9 = r14.TAG
            r10 = 1
            java.lang.Object[] r10 = new java.lang.Object[r10]
            r11 = 0
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "wirteTofile:     allPath = "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r15)
            java.lang.String r12 = r12.toString()
            r10[r11] = r12
            com.tvlife.imageloader.utils.L.i(r9, r10)
        L_0x0046:
            return r7
        L_0x0047:
            r9 = 0
            r6.write(r4, r9, r8)     // Catch:{ FileNotFoundException -> 0x004c, IOException -> 0x006b, all -> 0x0068 }
            goto L_0x0012
        L_0x004c:
            r2 = move-exception
            r5 = r6
        L_0x004e:
            r2.printStackTrace()     // Catch:{ all -> 0x0061 }
            com.tvlife.imageloader.utils.IoUtils.closeSilently(r5)
            r5 = 0
            r4 = 0
            goto L_0x0023
        L_0x0057:
            r2 = move-exception
        L_0x0058:
            r2.printStackTrace()     // Catch:{ all -> 0x0061 }
            com.tvlife.imageloader.utils.IoUtils.closeSilently(r5)
            r5 = 0
            r4 = 0
            goto L_0x0023
        L_0x0061:
            r9 = move-exception
        L_0x0062:
            com.tvlife.imageloader.utils.IoUtils.closeSilently(r5)
            r5 = 0
            r4 = 0
            throw r9
        L_0x0068:
            r9 = move-exception
            r5 = r6
            goto L_0x0062
        L_0x006b:
            r2 = move-exception
            r5 = r6
            goto L_0x0058
        L_0x006e:
            r2 = move-exception
            goto L_0x004e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvlife.imageloader.core.decode.BaseImageDecoder.wirteTofile(java.lang.String, java.lang.String, com.tvlife.imageloader.core.decode.ImageDecodingInfo, java.io.InputStream):boolean");
    }

    public Bitmap decode(ImageDecodingInfo decodingInfo) throws IOException {
        BitmapFactory.Options decodingOptions;
        Bitmap decodedBitmap = null;
        InputStream imageStream = null;
        String imageUrl = decodingInfo.getImageUri();
        boolean cancelDecode = false;
        try {
            imageStream = getImageStream(decodingInfo);
            if (imageStream != null) {
                String path = decodingInfo.getBitmapPath();
                String name = decodingInfo.getBitmapName();
                String allPath = path + WVNativeCallbackUtil.SEPERATER + name;
                if (this.loggingEnabled) {
                    L.i(this.TAG, "decode:   path   = " + path + ";  name = " + name + ";  allPath = " + allPath);
                }
                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
                    if (decodingInfo.isDecodeSample()) {
                        ImageFileInfo imageInfo = defineImageSizeAndRotation(imageStream, decodingInfo);
                        imageStream = resetStream(imageStream, decodingInfo);
                        decodingOptions = prepareDecodingOptions(imageInfo.imageSize, decodingInfo);
                    } else {
                        decodingOptions = decodingInfo.getDecodingOptions();
                    }
                    cancelDecode = decodingInfo.cancelLoadIfneed();
                    if (this.loggingEnabled) {
                        L.i("decode -->  ; imageUrl = " + imageUrl + "; cancelDecode = " + cancelDecode + "; decodingOptions.inSampleSize = " + decodingOptions.inSampleSize, new Object[0]);
                    }
                    if (!cancelDecode) {
                        decodedBitmap = BitmapFactory.decodeStream(imageStream, (Rect) null, decodingOptions);
                    }
                } else if (wirteTofile(allPath, path, decodingInfo, imageStream)) {
                    cancelDecode = decodingInfo.cancelLoadIfneed();
                    if (this.loggingEnabled) {
                        L.i("decode -->  ; imageUrl = " + imageUrl + "; cancelDecode = " + cancelDecode, new Object[0]);
                    }
                    if (!cancelDecode) {
                        decodedBitmap = BitmapFactory.decodeFile(allPath);
                    }
                }
            }
        } catch (IOException e) {
            if (this.loggingEnabled) {
                L.e("decode -->  ; imageUrl = " + imageUrl + "; IOException = " + e.toString(), new Object[0]);
            }
        } finally {
            IoUtils.closeSilently(imageStream);
        }
        if (decodedBitmap == null && !cancelDecode) {
            L.e(ERROR_CANT_DECODE_IMAGE, decodingInfo.getImageKey());
        }
        return decodedBitmap;
    }

    public Bitmap getBitmapFromFile(ImageDecodingInfo decodingInfo) throws IOException {
        if (this.loggingEnabled) {
            L.i(this.TAG, "getBitmapFromFile ;   Uri = " + decodingInfo.getImageUri());
        }
        FileInputStream fimageStream = new FileInputStream(ImageDownloader.Scheme.FILE.crop(decodingInfo.getImageUri()));
        Bitmap decodedBitmap = BitmapFactory.decodeStream(fimageStream);
        if (fimageStream != null) {
            fimageStream.close();
        }
        return decodedBitmap;
    }

    public Bitmap getBitmapFromOthers(ImageDecodingInfo decodingInfo) throws IOException {
        if (this.loggingEnabled) {
            L.i(this.TAG, "getBitmapFromOthers ;  Uri = " + decodingInfo.getImageUri());
        }
        InputStream imageStream = getImageStream(decodingInfo);
        try {
            return BitmapFactory.decodeStream(imageStream);
        } finally {
            IoUtils.closeSilently(imageStream);
        }
    }

    public Bitmap getBitmapFromNetwork(ImageDecodingInfo decodingInfo) throws IOException {
        Bitmap decodedBitmap = null;
        if (this.loggingEnabled) {
            L.i(this.TAG, "getBitmapFromNetwork ;   Uri = " + decodingInfo.getImageUri());
        }
        HttpURLConnection conn = createConnection(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < 5) {
            conn.disconnect();
            conn = createConnection(conn.getHeaderField("Location"), decodingInfo.getExtraForDownloader());
            redirectCount++;
        }
        BufferedInputStream inputStream = null;
        try {
            BufferedInputStream inputStream2 = new BufferedInputStream(conn.getInputStream(), 32768);
            try {
                decodedBitmap = BitmapFactory.decodeStream(inputStream2);
                IoUtils.closeSilently(inputStream2);
                conn.disconnect();
                BufferedInputStream bufferedInputStream = inputStream2;
            } catch (IOException e) {
                inputStream = inputStream2;
                IoUtils.closeSilently(inputStream);
                conn.disconnect();
                return decodedBitmap;
            } catch (Throwable th) {
                th = th;
                inputStream = inputStream2;
                IoUtils.closeSilently(inputStream);
                conn.disconnect();
                throw th;
            }
        } catch (IOException e2) {
            IoUtils.closeSilently(inputStream);
            conn.disconnect();
            return decodedBitmap;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeSilently(inputStream);
            conn.disconnect();
            throw th;
        }
        return decodedBitmap;
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);
        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < 5) {
            conn = createConnection(conn.getHeaderField("Location"), extra);
            redirectCount++;
        }
        try {
            return new ContentLengthInputStream(new BufferedInputStream(conn.getInputStream(), 32768), (long) conn.getContentLength());
        } catch (IOException e) {
            IoUtils.readAndCloseStream(conn.getErrorStream());
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(Uri.encode(url, BaseImageDownloader.ALLOWED_URI_CHARS)).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(20000);
        return conn;
    }

    /* access modifiers changed from: protected */
    public InputStream getImageStream(ImageDecodingInfo decodingInfo) throws IOException {
        return decodingInfo.getDownloader().getStream(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
    }

    /* access modifiers changed from: protected */
    public ImageFileInfo defineImageSizeAndRotation(InputStream imageStream, ImageDecodingInfo decodingInfo) throws IOException {
        ExifInfo exif;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, (Rect) null, options);
        String imageUri = decodingInfo.getImageUri();
        if (!decodingInfo.shouldConsiderExifParams() || !canDefineExifParams(imageUri, options.outMimeType)) {
            exif = new ExifInfo();
        } else {
            exif = defineExifOrientation(imageUri);
        }
        return new ImageFileInfo(new ImageSize(options.outWidth, options.outHeight, exif.rotation), exif);
    }

    private boolean canDefineExifParams(String imageUri, String mimeType) {
        return Build.VERSION.SDK_INT >= 5 && "image/jpeg".equalsIgnoreCase(mimeType) && ImageDownloader.Scheme.ofUri(imageUri) == ImageDownloader.Scheme.FILE;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0027, code lost:
        r4 = 180;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        r4 = 270;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0020, code lost:
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        r4 = 90;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.tvlife.imageloader.core.decode.BaseImageDecoder.ExifInfo defineExifOrientation(java.lang.String r9) {
        /*
            r8 = this;
            r7 = 1
            r4 = 0
            r3 = 0
            android.media.ExifInterface r1 = new android.media.ExifInterface     // Catch:{ IOException -> 0x002e }
            com.tvlife.imageloader.core.download.ImageDownloader$Scheme r5 = com.tvlife.imageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IOException -> 0x002e }
            java.lang.String r5 = r5.crop(r9)     // Catch:{ IOException -> 0x002e }
            r1.<init>(r5)     // Catch:{ IOException -> 0x002e }
            java.lang.String r5 = "Orientation"
            r6 = 1
            int r2 = r1.getAttributeInt(r5, r6)     // Catch:{ IOException -> 0x002e }
            switch(r2) {
                case 1: goto L_0x0020;
                case 2: goto L_0x001f;
                case 3: goto L_0x0027;
                case 4: goto L_0x0026;
                case 5: goto L_0x002a;
                case 6: goto L_0x0023;
                case 7: goto L_0x0022;
                case 8: goto L_0x002b;
                default: goto L_0x0019;
            }
        L_0x0019:
            com.tvlife.imageloader.core.decode.BaseImageDecoder$ExifInfo r5 = new com.tvlife.imageloader.core.decode.BaseImageDecoder$ExifInfo
            r5.<init>(r4, r3)
            return r5
        L_0x001f:
            r3 = 1
        L_0x0020:
            r4 = 0
            goto L_0x0019
        L_0x0022:
            r3 = 1
        L_0x0023:
            r4 = 90
            goto L_0x0019
        L_0x0026:
            r3 = 1
        L_0x0027:
            r4 = 180(0xb4, float:2.52E-43)
            goto L_0x0019
        L_0x002a:
            r3 = 1
        L_0x002b:
            r4 = 270(0x10e, float:3.78E-43)
            goto L_0x0019
        L_0x002e:
            r0 = move-exception
            java.lang.String r5 = "Can't read EXIF tags from file [%s]"
            java.lang.Object[] r6 = new java.lang.Object[r7]
            r7 = 0
            r6[r7] = r9
            com.tvlife.imageloader.utils.L.w(r5, r6)
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvlife.imageloader.core.decode.BaseImageDecoder.defineExifOrientation(java.lang.String):com.tvlife.imageloader.core.decode.BaseImageDecoder$ExifInfo");
    }

    /* access modifiers changed from: protected */
    public BitmapFactory.Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo decodingInfo) {
        boolean powerOf2;
        int scale;
        ImageScaleType scaleType = decodingInfo.getImageScaleType();
        if (scaleType == ImageScaleType.NONE) {
            scale = ImageSizeUtils.computeMinImageSampleSize(imageSize);
        } else {
            ImageSize targetSize = decodingInfo.getTargetSize();
            if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2) {
                powerOf2 = true;
            } else {
                powerOf2 = false;
            }
            scale = ImageSizeUtils.computeImageSampleSize(imageSize, targetSize, decodingInfo.getViewScaleType(), powerOf2);
        }
        if (scale > 1 && this.loggingEnabled) {
            L.d(LOG_SABSAMPLE_IMAGE, imageSize, imageSize.scaleDown(scale), Integer.valueOf(scale), decodingInfo.getImageKey());
        }
        if (this.loggingEnabled) {
            ImageSize targetSize2 = decodingInfo.getTargetSize();
            L.d("prepareDecodingOptions --> scale = " + scale + "; scaleType = " + scaleType + "; imageWidth = " + imageSize.getWidth() + "; imageHeight = " + imageSize.getHeight() + "; targetWidth = " + targetSize2.getWidth() + "; targetHeight = " + targetSize2.getHeight(), new Object[0]);
        }
        BitmapFactory.Options decodingOptions = decodingInfo.getDecodingOptions();
        decodingOptions.inSampleSize = scale;
        return decodingOptions;
    }

    /* access modifiers changed from: protected */
    public InputStream resetStream(InputStream imageStream, ImageDecodingInfo decodingInfo) throws IOException {
        try {
            imageStream.reset();
            return imageStream;
        } catch (IOException e) {
            IoUtils.closeSilently(imageStream);
            return getImageStream(decodingInfo);
        }
    }

    /* access modifiers changed from: protected */
    public Bitmap considerExactScaleAndOrientaiton(Bitmap subsampledBitmap, ImageDecodingInfo decodingInfo, int rotation, boolean flipHorizontal) {
        Matrix m = new Matrix();
        ImageScaleType scaleType = decodingInfo.getImageScaleType();
        if (scaleType == ImageScaleType.EXACTLY || scaleType == ImageScaleType.EXACTLY_STRETCHED) {
            ImageSize srcSize = new ImageSize(subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), rotation);
            float scale = ImageSizeUtils.computeImageScale(srcSize, decodingInfo.getTargetSize(), decodingInfo.getViewScaleType(), scaleType == ImageScaleType.EXACTLY_STRETCHED);
            if (Float.compare(scale, 1.0f) != 0) {
                m.setScale(scale, scale);
                if (this.loggingEnabled) {
                    L.d(LOG_SCALE_IMAGE, srcSize, srcSize.scale(scale), Float.valueOf(scale), decodingInfo.getImageKey());
                }
            }
        }
        if (flipHorizontal) {
            m.postScale(-1.0f, 1.0f);
            if (this.loggingEnabled) {
                L.d(LOG_FLIP_IMAGE, decodingInfo.getImageKey());
            }
        }
        if (rotation != 0) {
            m.postRotate((float) rotation);
            if (this.loggingEnabled) {
                L.d(LOG_ROTATE_IMAGE, Integer.valueOf(rotation), decodingInfo.getImageKey());
            }
        }
        Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0, subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m, true);
        if (finalBitmap != subsampledBitmap) {
            subsampledBitmap.recycle();
        }
        return finalBitmap;
    }

    protected static class ExifInfo {
        public final boolean flipHorizontal;
        public final int rotation;

        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }

        protected ExifInfo(int rotation2, boolean flipHorizontal2) {
            this.rotation = rotation2;
            this.flipHorizontal = flipHorizontal2;
        }
    }

    protected static class ImageFileInfo {
        public final ExifInfo exif;
        public final ImageSize imageSize;

        protected ImageFileInfo(ImageSize imageSize2, ExifInfo exif2) {
            this.imageSize = imageSize2;
            this.exif = exif2;
        }
    }
}
