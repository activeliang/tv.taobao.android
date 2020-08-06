package anet.channel.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArray implements Comparable<ByteArray> {
    final byte[] buffer;
    int bufferLength;
    int dataLength;

    private ByteArray(byte[] bytes, int length) {
        this.buffer = bytes == null ? new byte[length] : bytes;
        this.bufferLength = this.buffer.length;
        this.dataLength = length;
    }

    public static ByteArray create(int size) {
        return new ByteArray((byte[]) null, size);
    }

    public static ByteArray wrap(byte[] bytes, int length) {
        if (bytes == null || length <= 0) {
            return null;
        }
        return new ByteArray(bytes, length);
    }

    public static ByteArray wrap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return wrap(bytes, bytes.length);
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public int getBufferLength() {
        return this.bufferLength;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public void setDataLength(int dataLength2) {
        this.dataLength = dataLength2;
    }

    public void recycle() {
        if (this.bufferLength != 0) {
            ByteArrayPool.getInstance().refund(this);
        }
    }

    public int readFrom(InputStream is) throws IOException {
        int i = 0;
        int readLen = is.read(this.buffer, 0, this.bufferLength);
        if (readLen != -1) {
            i = readLen;
        }
        this.dataLength = i;
        return readLen;
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write(this.buffer, 0, this.dataLength);
    }

    public int compareTo(ByteArray other) {
        if (this.bufferLength != other.bufferLength) {
            return this.bufferLength - other.bufferLength;
        }
        if (this.buffer == null) {
            return -1;
        }
        if (other.buffer == null) {
            return 1;
        }
        return hashCode() - other.hashCode();
    }
}
