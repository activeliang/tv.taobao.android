package anet.channel.bytes;

import android.support.v4.media.session.PlaybackStateCompat;
import anet.channel.util.ALog;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

public class ByteArrayPool {
    public static final int MAX_POOL_SIZE = 524288;
    public static final String TAG = "awcn.ByteArrayPool";
    private final TreeSet<ByteArray> byteArrayPool = new TreeSet<>();
    private final Random random = new Random();
    private long reused = 0;
    private final ByteArray std = ByteArray.create(0);
    private long total = 0;

    public static ByteArrayPool getInstance() {
        return SingleInstance.instance;
    }

    static class SingleInstance {
        static ByteArrayPool instance = new ByteArrayPool();

        SingleInstance() {
        }
    }

    public synchronized void refund(ByteArray byteArray) {
        ByteArray deletedItem;
        if (byteArray != null) {
            if (byteArray.bufferLength < 524288) {
                this.total += (long) byteArray.bufferLength;
                this.byteArrayPool.add(byteArray);
                while (this.total > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED) {
                    if (this.random.nextBoolean()) {
                        deletedItem = this.byteArrayPool.pollFirst();
                    } else {
                        deletedItem = this.byteArrayPool.pollLast();
                    }
                    this.total -= (long) deletedItem.bufferLength;
                }
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "ByteArray Pool refund", (String) null, "refund", Integer.valueOf(byteArray.getBufferLength()), "total", Long.valueOf(this.total));
                }
            }
        }
    }

    public synchronized ByteArray retrieve(int length) {
        ByteArray ret;
        if (length >= 524288) {
            ret = ByteArray.create(length);
        } else {
            this.std.bufferLength = length;
            ret = this.byteArrayPool.ceiling(this.std);
            if (ret == null) {
                ret = ByteArray.create(length);
            } else {
                Arrays.fill(ret.buffer, (byte) 0);
                ret.dataLength = 0;
                this.byteArrayPool.remove(ret);
                this.total -= (long) ret.bufferLength;
                this.reused += (long) length;
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "ByteArray Pool retrieve", (String) null, "retrieve", Integer.valueOf(length), "reused", Long.valueOf(this.reused));
                }
            }
        }
        return ret;
    }

    public ByteArray retrieveAndCopy(byte[] bytes, int len) {
        ByteArray byteArray = retrieve(len);
        System.arraycopy(bytes, 0, byteArray.buffer, 0, len);
        byteArray.dataLength = len;
        return byteArray;
    }
}
