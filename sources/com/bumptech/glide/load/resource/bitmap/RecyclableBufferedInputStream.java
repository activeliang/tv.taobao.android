package com.bumptech.glide.load.resource.bitmap;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
    private volatile byte[] buf;
    private final ArrayPool byteArrayPool;
    private int count;
    private int marklimit;
    private int markpos;
    private int pos;

    public RecyclableBufferedInputStream(@NonNull InputStream in, @NonNull ArrayPool byteArrayPool2) {
        this(in, byteArrayPool2, 65536);
    }

    @VisibleForTesting
    RecyclableBufferedInputStream(@NonNull InputStream in, @NonNull ArrayPool byteArrayPool2, int bufferSize) {
        super(in);
        this.markpos = -1;
        this.byteArrayPool = byteArrayPool2;
        this.buf = (byte[]) byteArrayPool2.get(bufferSize, byte[].class);
    }

    public synchronized int available() throws IOException {
        InputStream localIn;
        localIn = this.in;
        if (this.buf == null || localIn == null) {
            throw streamClosed();
        }
        return (this.count - this.pos) + localIn.available();
    }

    private static IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }

    public synchronized void fixMarkLimit() {
        this.marklimit = this.buf.length;
    }

    public synchronized void release() {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
    }

    public void close() throws IOException {
        if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
        }
        InputStream localIn = this.in;
        this.in = null;
        if (localIn != null) {
            localIn.close();
        }
    }

    private int fillbuf(InputStream localIn, byte[] localBuf) throws IOException {
        int i;
        if (this.markpos == -1 || this.pos - this.markpos >= this.marklimit) {
            int result = localIn.read(localBuf);
            if (result > 0) {
                this.markpos = -1;
                this.pos = 0;
                this.count = result;
            }
            return result;
        }
        if (this.markpos == 0 && this.marklimit > localBuf.length && this.count == localBuf.length) {
            int newLength = localBuf.length * 2;
            if (newLength > this.marklimit) {
                newLength = this.marklimit;
            }
            byte[] newbuf = (byte[]) this.byteArrayPool.get(newLength, byte[].class);
            System.arraycopy(localBuf, 0, newbuf, 0, localBuf.length);
            byte[] oldbuf = localBuf;
            this.buf = newbuf;
            localBuf = newbuf;
            this.byteArrayPool.put(oldbuf);
        } else if (this.markpos > 0) {
            System.arraycopy(localBuf, this.markpos, localBuf, 0, localBuf.length - this.markpos);
        }
        this.pos -= this.markpos;
        this.markpos = 0;
        this.count = 0;
        int bytesread = localIn.read(localBuf, this.pos, localBuf.length - this.pos);
        if (bytesread <= 0) {
            i = this.pos;
        } else {
            i = this.pos + bytesread;
        }
        this.count = i;
        return bytesread;
    }

    public synchronized void mark(int readlimit) {
        this.marklimit = Math.max(this.marklimit, readlimit);
        this.markpos = this.pos;
    }

    public boolean markSupported() {
        return true;
    }

    public synchronized int read() throws IOException {
        byte b = -1;
        synchronized (this) {
            byte[] localBuf = this.buf;
            InputStream localIn = this.in;
            if (localBuf == null || localIn == null) {
                throw streamClosed();
            } else if (this.pos < this.count || fillbuf(localIn, localBuf) != -1) {
                if (localBuf != this.buf && (localBuf = this.buf) == null) {
                    throw streamClosed();
                } else if (this.count - this.pos > 0) {
                    int i = this.pos;
                    this.pos = i + 1;
                    b = localBuf[i] & OnReminderListener.RET_FULL;
                }
            }
        }
        return b;
    }

    public synchronized int read(@NonNull byte[] buffer, int offset, int byteCount) throws IOException {
        int required;
        int read;
        int i = -1;
        synchronized (this) {
            byte[] localBuf = this.buf;
            if (localBuf == null) {
                throw streamClosed();
            } else if (byteCount == 0) {
                i = 0;
            } else {
                InputStream localIn = this.in;
                if (localIn == null) {
                    throw streamClosed();
                }
                if (this.pos < this.count) {
                    int copylength = this.count - this.pos >= byteCount ? byteCount : this.count - this.pos;
                    System.arraycopy(localBuf, this.pos, buffer, offset, copylength);
                    this.pos += copylength;
                    if (copylength == byteCount || localIn.available() == 0) {
                        i = copylength;
                    } else {
                        offset += copylength;
                        required = byteCount - copylength;
                    }
                } else {
                    required = byteCount;
                }
                while (true) {
                    if (this.markpos == -1 && required >= localBuf.length) {
                        read = localIn.read(buffer, offset, required);
                        if (read == -1) {
                            if (required != byteCount) {
                                i = byteCount - required;
                            }
                        }
                    } else if (fillbuf(localIn, localBuf) == -1) {
                        if (required != byteCount) {
                            i = byteCount - required;
                        }
                    } else if (localBuf == this.buf || (localBuf = this.buf) != null) {
                        read = this.count - this.pos >= required ? required : this.count - this.pos;
                        System.arraycopy(localBuf, this.pos, buffer, offset, read);
                        this.pos += read;
                    } else {
                        throw streamClosed();
                    }
                    required -= read;
                    if (required == 0) {
                        i = byteCount;
                        break;
                    } else if (localIn.available() == 0) {
                        i = byteCount - required;
                        break;
                    } else {
                        offset += read;
                    }
                }
            }
        }
        return i;
    }

    public synchronized void reset() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream is closed");
        } else if (-1 == this.markpos) {
            throw new InvalidMarkException("Mark has been invalidated, pos: " + this.pos + " markLimit: " + this.marklimit);
        } else {
            this.pos = this.markpos;
        }
    }

    public synchronized long skip(long byteCount) throws IOException {
        if (byteCount < 1) {
            byteCount = 0;
        } else {
            byte[] localBuf = this.buf;
            if (localBuf == null) {
                throw streamClosed();
            }
            InputStream localIn = this.in;
            if (localIn == null) {
                throw streamClosed();
            } else if (((long) (this.count - this.pos)) >= byteCount) {
                this.pos = (int) (((long) this.pos) + byteCount);
            } else {
                long read = ((long) this.count) - ((long) this.pos);
                this.pos = this.count;
                if (this.markpos == -1 || byteCount > ((long) this.marklimit)) {
                    byteCount = read + localIn.skip(byteCount - read);
                } else if (fillbuf(localIn, localBuf) == -1) {
                    byteCount = read;
                } else if (((long) (this.count - this.pos)) >= byteCount - read) {
                    this.pos = (int) ((((long) this.pos) + byteCount) - read);
                } else {
                    long read2 = (((long) this.count) + read) - ((long) this.pos);
                    this.pos = this.count;
                    byteCount = read2;
                }
            }
        }
        return byteCount;
    }

    static class InvalidMarkException extends IOException {
        private static final long serialVersionUID = -4338378848813561757L;

        InvalidMarkException(String detailMessage) {
            super(detailMessage);
        }
    }
}
