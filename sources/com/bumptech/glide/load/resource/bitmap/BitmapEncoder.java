package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    private static final String TAG = "BitmapEncoder";
    @Nullable
    private final ArrayPool arrayPool;

    public BitmapEncoder(@NonNull ArrayPool arrayPool2) {
        this.arrayPool = arrayPool2;
    }

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0064 A[Catch:{ all -> 0x00e7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00cd A[Catch:{ all -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00d8 A[SYNTHETIC, Splitter:B:31:0x00d8] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00e3 A[SYNTHETIC, Splitter:B:36:0x00e3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encode(@android.support.annotation.NonNull com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> r17, @android.support.annotation.NonNull java.io.File r18, @android.support.annotation.NonNull com.bumptech.glide.load.Options r19) {
        /*
            r16 = this;
            java.lang.Object r2 = r17.get()
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2
            r0 = r16
            r1 = r19
            android.graphics.Bitmap$CompressFormat r4 = r0.getFormat(r2, r1)
            java.lang.String r11 = "encode: [%dx%d] %s"
            int r12 = r2.getWidth()
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            int r13 = r2.getHeight()
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            com.bumptech.glide.util.pool.GlideTrace.beginSectionFormat(r11, r12, r13, r4)
            long r8 = com.bumptech.glide.util.LogTime.getLogTime()     // Catch:{ all -> 0x00e7 }
            com.bumptech.glide.load.Option<java.lang.Integer> r11 = COMPRESSION_QUALITY     // Catch:{ all -> 0x00e7 }
            r0 = r19
            java.lang.Object r11 = r0.get(r11)     // Catch:{ all -> 0x00e7 }
            java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ all -> 0x00e7 }
            int r7 = r11.intValue()     // Catch:{ all -> 0x00e7 }
            r10 = 0
            r5 = 0
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00c2 }
            r0 = r18
            r6.<init>(r0)     // Catch:{ IOException -> 0x00c2 }
            r0 = r16
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r11 = r0.arrayPool     // Catch:{ IOException -> 0x00f4, all -> 0x00f1 }
            if (r11 == 0) goto L_0x00f7
            com.bumptech.glide.load.data.BufferedOutputStream r5 = new com.bumptech.glide.load.data.BufferedOutputStream     // Catch:{ IOException -> 0x00f4, all -> 0x00f1 }
            r0 = r16
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r11 = r0.arrayPool     // Catch:{ IOException -> 0x00f4, all -> 0x00f1 }
            r5.<init>(r6, r11)     // Catch:{ IOException -> 0x00f4, all -> 0x00f1 }
        L_0x004e:
            r2.compress(r4, r7, r5)     // Catch:{ IOException -> 0x00c2 }
            r5.close()     // Catch:{ IOException -> 0x00c2 }
            r10 = 1
            if (r5 == 0) goto L_0x005a
            r5.close()     // Catch:{ IOException -> 0x00ec }
        L_0x005a:
            java.lang.String r11 = "BitmapEncoder"
            r12 = 2
            boolean r11 = android.util.Log.isLoggable(r11, r12)     // Catch:{ all -> 0x00e7 }
            if (r11 == 0) goto L_0x00be
            java.lang.String r11 = "BitmapEncoder"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e7 }
            r12.<init>()     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = "Compressed with type: "
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r12 = r12.append(r4)     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = " of size "
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            int r13 = com.bumptech.glide.util.Util.getBitmapByteSize(r2)     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = " in "
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            double r14 = com.bumptech.glide.util.LogTime.getElapsedMillis(r8)     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r12 = r12.append(r14)     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = ", options format: "
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r13 = COMPRESSION_FORMAT     // Catch:{ all -> 0x00e7 }
            r0 = r19
            java.lang.Object r13 = r0.get(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = ", hasAlpha: "
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            boolean r13 = r2.hasAlpha()     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x00e7 }
            android.util.Log.v(r11, r12)     // Catch:{ all -> 0x00e7 }
        L_0x00be:
            com.bumptech.glide.util.pool.GlideTrace.endSection()
            return r10
        L_0x00c2:
            r3 = move-exception
        L_0x00c3:
            java.lang.String r11 = "BitmapEncoder"
            r12 = 3
            boolean r11 = android.util.Log.isLoggable(r11, r12)     // Catch:{ all -> 0x00e0 }
            if (r11 == 0) goto L_0x00d6
            java.lang.String r11 = "BitmapEncoder"
            java.lang.String r12 = "Failed to encode Bitmap"
            android.util.Log.d(r11, r12, r3)     // Catch:{ all -> 0x00e0 }
        L_0x00d6:
            if (r5 == 0) goto L_0x005a
            r5.close()     // Catch:{ IOException -> 0x00dd }
            goto L_0x005a
        L_0x00dd:
            r11 = move-exception
            goto L_0x005a
        L_0x00e0:
            r11 = move-exception
        L_0x00e1:
            if (r5 == 0) goto L_0x00e6
            r5.close()     // Catch:{ IOException -> 0x00ef }
        L_0x00e6:
            throw r11     // Catch:{ all -> 0x00e7 }
        L_0x00e7:
            r11 = move-exception
            com.bumptech.glide.util.pool.GlideTrace.endSection()
            throw r11
        L_0x00ec:
            r11 = move-exception
            goto L_0x005a
        L_0x00ef:
            r12 = move-exception
            goto L_0x00e6
        L_0x00f1:
            r11 = move-exception
            r5 = r6
            goto L_0x00e1
        L_0x00f4:
            r3 = move-exception
            r5 = r6
            goto L_0x00c3
        L_0x00f7:
            r5 = r6
            goto L_0x004e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        Bitmap.CompressFormat format = (Bitmap.CompressFormat) options.get(COMPRESSION_FORMAT);
        if (format != null) {
            return format;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    @NonNull
    public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
