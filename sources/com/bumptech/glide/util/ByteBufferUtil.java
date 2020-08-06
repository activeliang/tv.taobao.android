package com.bumptech.glide.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
    private static final AtomicReference<byte[]> BUFFER_REF = new AtomicReference<>();
    private static final int BUFFER_SIZE = 16384;

    private ByteBufferUtil() {
    }

    @NonNull
    public static ByteBuffer fromFile(@NonNull File file) throws IOException {
        RandomAccessFile raf = null;
        FileChannel channel = null;
        try {
            long fileLength = file.length();
            if (fileLength > 2147483647L) {
                throw new IOException("File too large to map into memory");
            } else if (fileLength == 0) {
                throw new IOException("File unsuitable for memory mapping");
            } else {
                RandomAccessFile raf2 = new RandomAccessFile(file, UploadQueueMgr.MSGTYPE_REALTIME);
                try {
                    channel = raf2.getChannel();
                    MappedByteBuffer load = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength).load();
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                        }
                    }
                    if (raf2 != null) {
                        try {
                            raf2.close();
                        } catch (IOException e2) {
                        }
                    }
                    return load;
                } catch (Throwable th) {
                    th = th;
                    raf = raf2;
                }
            }
        } catch (Throwable th2) {
            th = th2;
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e3) {
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002d A[SYNTHETIC, Splitter:B:13:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032 A[SYNTHETIC, Splitter:B:16:0x0032] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void toFile(@android.support.annotation.NonNull java.nio.ByteBuffer r5, @android.support.annotation.NonNull java.io.File r6) throws java.io.IOException {
        /*
            r3 = 0
            r5.position(r3)
            r1 = 0
            r0 = 0
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ all -> 0x002a }
            java.lang.String r3 = "rw"
            r2.<init>(r6, r3)     // Catch:{ all -> 0x002a }
            java.nio.channels.FileChannel r0 = r2.getChannel()     // Catch:{ all -> 0x003e }
            r0.write(r5)     // Catch:{ all -> 0x003e }
            r3 = 0
            r0.force(r3)     // Catch:{ all -> 0x003e }
            r0.close()     // Catch:{ all -> 0x003e }
            r2.close()     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x0024
            r0.close()     // Catch:{ IOException -> 0x0036 }
        L_0x0024:
            if (r2 == 0) goto L_0x0029
            r2.close()     // Catch:{ IOException -> 0x0038 }
        L_0x0029:
            return
        L_0x002a:
            r3 = move-exception
        L_0x002b:
            if (r0 == 0) goto L_0x0030
            r0.close()     // Catch:{ IOException -> 0x003a }
        L_0x0030:
            if (r1 == 0) goto L_0x0035
            r1.close()     // Catch:{ IOException -> 0x003c }
        L_0x0035:
            throw r3
        L_0x0036:
            r3 = move-exception
            goto L_0x0024
        L_0x0038:
            r3 = move-exception
            goto L_0x0029
        L_0x003a:
            r4 = move-exception
            goto L_0x0030
        L_0x003c:
            r4 = move-exception
            goto L_0x0035
        L_0x003e:
            r3 = move-exception
            r1 = r2
            goto L_0x002b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.util.ByteBufferUtil.toFile(java.nio.ByteBuffer, java.io.File):void");
    }

    public static void toStream(@NonNull ByteBuffer byteBuffer, @NonNull OutputStream os) throws IOException {
        SafeArray safeArray = getSafeArray(byteBuffer);
        if (safeArray != null) {
            os.write(safeArray.data, safeArray.offset, safeArray.offset + safeArray.limit);
            return;
        }
        byte[] buffer = BUFFER_REF.getAndSet((Object) null);
        if (buffer == null) {
            buffer = new byte[16384];
        }
        while (byteBuffer.remaining() > 0) {
            int toRead = Math.min(byteBuffer.remaining(), buffer.length);
            byteBuffer.get(buffer, 0, toRead);
            os.write(buffer, 0, toRead);
        }
        BUFFER_REF.set(buffer);
    }

    @NonNull
    public static byte[] toBytes(@NonNull ByteBuffer byteBuffer) {
        SafeArray safeArray = getSafeArray(byteBuffer);
        if (safeArray != null && safeArray.offset == 0 && safeArray.limit == safeArray.data.length) {
            return byteBuffer.array();
        }
        ByteBuffer toCopy = byteBuffer.asReadOnlyBuffer();
        byte[] result = new byte[toCopy.limit()];
        toCopy.position(0);
        toCopy.get(result);
        return result;
    }

    @NonNull
    public static InputStream toStream(@NonNull ByteBuffer buffer) {
        return new ByteBufferStream(buffer);
    }

    @NonNull
    public static ByteBuffer fromStream(@NonNull InputStream stream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(16384);
        byte[] buffer = BUFFER_REF.getAndSet((Object) null);
        if (buffer == null) {
            buffer = new byte[16384];
        }
        while (true) {
            int n = stream.read(buffer);
            if (n >= 0) {
                outStream.write(buffer, 0, n);
            } else {
                BUFFER_REF.set(buffer);
                byte[] bytes = outStream.toByteArray();
                return (ByteBuffer) ByteBuffer.allocateDirect(bytes.length).put(bytes).position(0);
            }
        }
    }

    @Nullable
    private static SafeArray getSafeArray(@NonNull ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly() || !byteBuffer.hasArray()) {
            return null;
        }
        return new SafeArray(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.limit());
    }

    static final class SafeArray {
        final byte[] data;
        final int limit;
        final int offset;

        SafeArray(@NonNull byte[] data2, int offset2, int limit2) {
            this.data = data2;
            this.offset = offset2;
            this.limit = limit2;
        }
    }

    private static class ByteBufferStream extends InputStream {
        private static final int UNSET = -1;
        @NonNull
        private final ByteBuffer byteBuffer;
        private int markPos = -1;

        ByteBufferStream(@NonNull ByteBuffer byteBuffer2) {
            this.byteBuffer = byteBuffer2;
        }

        public int available() {
            return this.byteBuffer.remaining();
        }

        public int read() {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            return this.byteBuffer.get();
        }

        public synchronized void mark(int readLimit) {
            this.markPos = this.byteBuffer.position();
        }

        public boolean markSupported() {
            return true;
        }

        public int read(@NonNull byte[] buffer, int byteOffset, int byteCount) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            int toRead = Math.min(byteCount, available());
            this.byteBuffer.get(buffer, byteOffset, toRead);
            return toRead;
        }

        public synchronized void reset() throws IOException {
            if (this.markPos == -1) {
                throw new IOException("Cannot reset to unset mark position");
            }
            this.byteBuffer.position(this.markPos);
        }

        public long skip(long byteCount) throws IOException {
            if (!this.byteBuffer.hasRemaining()) {
                return -1;
            }
            long toSkip = Math.min(byteCount, (long) available());
            this.byteBuffer.position((int) (((long) this.byteBuffer.position()) + toSkip));
            return toSkip;
        }
    }
}
