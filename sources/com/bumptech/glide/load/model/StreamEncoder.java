package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.InputStream;

public class StreamEncoder implements Encoder<InputStream> {
    private static final String TAG = "StreamEncoder";
    private final ArrayPool byteArrayPool;

    public StreamEncoder(ArrayPool byteArrayPool2) {
        this.byteArrayPool = byteArrayPool2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b A[Catch:{ all -> 0x004f }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0036 A[SYNTHETIC, Splitter:B:15:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0052 A[SYNTHETIC, Splitter:B:28:0x0052] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encode(@android.support.annotation.NonNull java.io.InputStream r10, @android.support.annotation.NonNull java.io.File r11, @android.support.annotation.NonNull com.bumptech.glide.load.Options r12) {
        /*
            r9 = this;
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r6 = r9.byteArrayPool
            r7 = 65536(0x10000, float:9.18355E-41)
            java.lang.Class<byte[]> r8 = byte[].class
            java.lang.Object r0 = r6.get(r7, r8)
            byte[] r0 = (byte[]) r0
            r5 = 0
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0064 }
            r3.<init>(r11)     // Catch:{ IOException -> 0x0064 }
        L_0x0013:
            int r4 = r10.read(r0)     // Catch:{ IOException -> 0x001f, all -> 0x0061 }
            r6 = -1
            if (r4 == r6) goto L_0x003f
            r6 = 0
            r3.write(r0, r6, r4)     // Catch:{ IOException -> 0x001f, all -> 0x0061 }
            goto L_0x0013
        L_0x001f:
            r1 = move-exception
            r2 = r3
        L_0x0021:
            java.lang.String r6 = "StreamEncoder"
            r7 = 3
            boolean r6 = android.util.Log.isLoggable(r6, r7)     // Catch:{ all -> 0x004f }
            if (r6 == 0) goto L_0x0034
            java.lang.String r6 = "StreamEncoder"
            java.lang.String r7 = "Failed to encode data onto the OutputStream"
            android.util.Log.d(r6, r7, r1)     // Catch:{ all -> 0x004f }
        L_0x0034:
            if (r2 == 0) goto L_0x0039
            r2.close()     // Catch:{ IOException -> 0x005d }
        L_0x0039:
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r6 = r9.byteArrayPool
            r6.put(r0)
        L_0x003e:
            return r5
        L_0x003f:
            r3.close()     // Catch:{ IOException -> 0x001f, all -> 0x0061 }
            r5 = 1
            if (r3 == 0) goto L_0x0048
            r3.close()     // Catch:{ IOException -> 0x005b }
        L_0x0048:
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r6 = r9.byteArrayPool
            r6.put(r0)
            r2 = r3
            goto L_0x003e
        L_0x004f:
            r6 = move-exception
        L_0x0050:
            if (r2 == 0) goto L_0x0055
            r2.close()     // Catch:{ IOException -> 0x005f }
        L_0x0055:
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r7 = r9.byteArrayPool
            r7.put(r0)
            throw r6
        L_0x005b:
            r6 = move-exception
            goto L_0x0048
        L_0x005d:
            r6 = move-exception
            goto L_0x0039
        L_0x005f:
            r7 = move-exception
            goto L_0x0055
        L_0x0061:
            r6 = move-exception
            r2 = r3
            goto L_0x0050
        L_0x0064:
            r1 = move-exception
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.model.StreamEncoder.encode(java.io.InputStream, java.io.File, com.bumptech.glide.load.Options):boolean");
    }
}
