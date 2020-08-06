package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anet.channel.util.ALog;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.entity.RequestConfig;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ParcelableInputStreamImpl extends ParcelableInputStream.Stub {
    private static final ByteArray EOS = ByteArray.create(0);
    private static final String TAG = "anet.ParcelableInputStreamImpl";
    private int blockIndex;
    private int blockOffset;
    private LinkedList<ByteArray> byteList = new LinkedList<>();
    private int contentLength;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    final ReentrantLock lock = new ReentrantLock();
    final Condition newDataArrive = this.lock.newCondition();
    private int receivedLength = 0;
    private int rto = 10000;
    private String seqNo = "";
    private String url = "";

    public void write(ByteArray b) {
        if (!this.isClosed.get()) {
            this.lock.lock();
            try {
                this.byteList.add(b);
                this.receivedLength += b.getDataLength();
                this.newDataArrive.signal();
            } finally {
                this.lock.unlock();
            }
        }
    }

    public void writeEnd() {
        write(EOS);
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "set EOS flag to stream", this.seqNo, new Object[0]);
        }
        if (this.contentLength != 0 && this.contentLength != this.receivedLength) {
            ALog.e(TAG, "data length no match!", this.seqNo, "ContentLength", Integer.valueOf(this.contentLength), "Received", Integer.valueOf(this.receivedLength), "url", this.url);
        }
    }

    private void recycleCurrentItem() {
        this.lock.lock();
        try {
            this.byteList.set(this.blockIndex, EOS).recycle();
        } finally {
            this.lock.unlock();
        }
    }

    public int available() throws RemoteException {
        if (this.isClosed.get()) {
            throw new RuntimeException("Stream is closed");
        }
        int count = 0;
        this.lock.lock();
        try {
            if (this.blockIndex == this.byteList.size()) {
                return 0;
            }
            ListIterator<ByteArray> iterator = this.byteList.listIterator(this.blockIndex);
            while (iterator.hasNext()) {
                count += iterator.next().getDataLength();
            }
            int count2 = count - this.blockOffset;
            this.lock.unlock();
            return count2;
        } finally {
            this.lock.unlock();
        }
    }

    public void close() throws RemoteException {
        if (this.isClosed.compareAndSet(false, true)) {
            this.lock.lock();
            try {
                Iterator i$ = this.byteList.iterator();
                while (i$.hasNext()) {
                    ByteArray byteArray = (ByteArray) i$.next();
                    if (byteArray != EOS) {
                        byteArray.recycle();
                    }
                }
                this.byteList.clear();
                this.byteList = null;
                this.blockIndex = -1;
                this.blockOffset = -1;
                this.contentLength = 0;
            } finally {
                this.lock.unlock();
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r2v2, types: [byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int readByte() throws android.os.RemoteException {
        /*
            r7 = this;
            java.util.concurrent.atomic.AtomicBoolean r3 = r7.isClosed
            boolean r3 = r3.get()
            if (r3 == 0) goto L_0x0011
            java.lang.RuntimeException r3 = new java.lang.RuntimeException
            java.lang.String r4 = "Stream is closed"
            r3.<init>(r4)
            throw r3
        L_0x0011:
            r2 = 0
            java.util.concurrent.locks.ReentrantLock r3 = r7.lock
            r3.lock()
        L_0x0017:
            int r3 = r7.blockIndex     // Catch:{ InterruptedException -> 0x003a }
            java.util.LinkedList<anet.channel.bytes.ByteArray> r4 = r7.byteList     // Catch:{ InterruptedException -> 0x003a }
            int r4 = r4.size()     // Catch:{ InterruptedException -> 0x003a }
            if (r3 != r4) goto L_0x004e
            java.util.concurrent.locks.Condition r3 = r7.newDataArrive     // Catch:{ InterruptedException -> 0x003a }
            int r4 = r7.rto     // Catch:{ InterruptedException -> 0x003a }
            long r4 = (long) r4     // Catch:{ InterruptedException -> 0x003a }
            java.util.concurrent.TimeUnit r6 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x003a }
            boolean r3 = r3.await(r4, r6)     // Catch:{ InterruptedException -> 0x003a }
            if (r3 != 0) goto L_0x004e
            r7.close()     // Catch:{ InterruptedException -> 0x003a }
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ InterruptedException -> 0x003a }
            java.lang.String r4 = "await timeout."
            r3.<init>(r4)     // Catch:{ InterruptedException -> 0x003a }
            throw r3     // Catch:{ InterruptedException -> 0x003a }
        L_0x003a:
            r1 = move-exception
            r7.close()     // Catch:{ all -> 0x0047 }
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ all -> 0x0047 }
            java.lang.String r4 = "await interrupt"
            r3.<init>(r4)     // Catch:{ all -> 0x0047 }
            throw r3     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r3 = move-exception
            java.util.concurrent.locks.ReentrantLock r4 = r7.lock
            r4.unlock()
            throw r3
        L_0x004e:
            java.util.LinkedList<anet.channel.bytes.ByteArray> r3 = r7.byteList     // Catch:{ InterruptedException -> 0x003a }
            int r4 = r7.blockIndex     // Catch:{ InterruptedException -> 0x003a }
            java.lang.Object r0 = r3.get(r4)     // Catch:{ InterruptedException -> 0x003a }
            anet.channel.bytes.ByteArray r0 = (anet.channel.bytes.ByteArray) r0     // Catch:{ InterruptedException -> 0x003a }
            anet.channel.bytes.ByteArray r3 = EOS     // Catch:{ InterruptedException -> 0x003a }
            if (r0 != r3) goto L_0x0063
            r2 = -1
        L_0x005d:
            java.util.concurrent.locks.ReentrantLock r3 = r7.lock
            r3.unlock()
            return r2
        L_0x0063:
            int r3 = r7.blockOffset     // Catch:{ InterruptedException -> 0x003a }
            int r4 = r0.getDataLength()     // Catch:{ InterruptedException -> 0x003a }
            if (r3 >= r4) goto L_0x007a
            byte[] r3 = r0.getBuffer()     // Catch:{ InterruptedException -> 0x003a }
            int r4 = r7.blockOffset     // Catch:{ InterruptedException -> 0x003a }
            byte r2 = r3[r4]     // Catch:{ InterruptedException -> 0x003a }
            int r3 = r7.blockOffset     // Catch:{ InterruptedException -> 0x003a }
            int r3 = r3 + 1
            r7.blockOffset = r3     // Catch:{ InterruptedException -> 0x003a }
            goto L_0x005d
        L_0x007a:
            r7.recycleCurrentItem()     // Catch:{ InterruptedException -> 0x003a }
            int r3 = r7.blockIndex     // Catch:{ InterruptedException -> 0x003a }
            int r3 = r3 + 1
            r7.blockIndex = r3     // Catch:{ InterruptedException -> 0x003a }
            r3 = 0
            r7.blockOffset = r3     // Catch:{ InterruptedException -> 0x003a }
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.aidl.adapter.ParcelableInputStreamImpl.readByte():int");
    }

    public int readBytes(byte[] b, int off, int len) throws RemoteException {
        if (this.isClosed.get()) {
            throw new RuntimeException("Stream is closed");
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || off + len > b.length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int desOffset = off;
            int desEnd = off + len;
            this.lock.lock();
            while (desOffset < desEnd) {
                try {
                    if (this.blockIndex != this.byteList.size() || this.newDataArrive.await((long) this.rto, TimeUnit.MILLISECONDS)) {
                        ByteArray byteArray = this.byteList.get(this.blockIndex);
                        if (byteArray == EOS) {
                            break;
                        }
                        int srcLength = byteArray.getDataLength() - this.blockOffset;
                        int desLength = desEnd - desOffset;
                        if (srcLength < desLength) {
                            System.arraycopy(byteArray.getBuffer(), this.blockOffset, b, desOffset, srcLength);
                            desOffset += srcLength;
                            recycleCurrentItem();
                            this.blockIndex++;
                            this.blockOffset = 0;
                        } else {
                            System.arraycopy(byteArray.getBuffer(), this.blockOffset, b, desOffset, desLength);
                            this.blockOffset += desLength;
                            desOffset += desLength;
                        }
                    } else {
                        close();
                        throw new RuntimeException("await timeout.");
                    }
                } catch (InterruptedException e) {
                    close();
                    throw new RuntimeException("await interrupt");
                } catch (Throwable th) {
                    this.lock.unlock();
                    throw th;
                }
            }
            this.lock.unlock();
            int readCount = desOffset - off;
            if (readCount > 0) {
                return readCount;
            }
            return -1;
        }
    }

    public int read(byte[] b) throws RemoteException {
        return readBytes(b, 0, b.length);
    }

    public long skip(int n) throws RemoteException {
        int skip = 0;
        this.lock.lock();
        while (true) {
            if (skip >= n) {
                break;
            }
            try {
                if (this.blockIndex != this.byteList.size()) {
                    ByteArray byteArray = this.byteList.get(this.blockIndex);
                    if (byteArray == EOS) {
                        break;
                    }
                    int length = byteArray.getDataLength();
                    if (length - this.blockOffset < n - skip) {
                        skip += length - this.blockOffset;
                        recycleCurrentItem();
                        this.blockIndex++;
                        this.blockOffset = 0;
                        break;
                    }
                    skip = n;
                    this.blockOffset += n - skip;
                } else {
                    break;
                }
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        this.lock.unlock();
        return (long) skip;
    }

    public int length() throws RemoteException {
        return this.contentLength;
    }

    public void init(RequestConfig config, int contentLength2) {
        this.contentLength = contentLength2;
        this.seqNo = config.getSeqNo();
        this.url = config.getUrlString();
        this.rto = config.getReadTimeout();
    }
}
