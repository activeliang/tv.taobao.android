package com.loc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

/* compiled from: FlatBufferBuilder */
public class ev {
    static final Charset c = Charset.forName("UTF-8");
    static final /* synthetic */ boolean p = (!ev.class.desiredAssertionStatus());
    ByteBuffer a;
    int b;
    int d;
    int[] e;
    int f;
    boolean g;
    boolean h;
    int i;
    int[] j;
    int k;
    int l;
    boolean m;
    CharsetEncoder n;
    ByteBuffer o;

    private ev() {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        this.b = 1024;
        this.a = d(1024);
    }

    public ev(ByteBuffer byteBuffer) {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        a(byteBuffer);
    }

    private void a(short s) {
        c(2, 0);
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 2;
        this.b = i2;
        byteBuffer.putShort(i2, s);
    }

    private void c(int i2, int i3) {
        if (i2 > this.d) {
            this.d = i2;
        }
        int capacity = ((((this.a.capacity() - this.b) + i3) ^ -1) + 1) & (i2 - 1);
        while (this.b < capacity + i2 + i3) {
            int capacity2 = this.a.capacity();
            ByteBuffer byteBuffer = this.a;
            int capacity3 = byteBuffer.capacity();
            if ((-1073741824 & capacity3) != 0) {
                throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
            }
            int i4 = capacity3 << 1;
            byteBuffer.position(0);
            ByteBuffer d2 = d(i4);
            d2.position(i4 - capacity3);
            d2.put(byteBuffer);
            this.a = d2;
            this.b = (this.a.capacity() - capacity2) + this.b;
        }
        e(capacity);
    }

    private int d() {
        return this.a.capacity() - this.b;
    }

    private static ByteBuffer d(int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(i2);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        return allocate;
    }

    private void e() {
        if (this.g) {
            throw new AssertionError("FlatBuffers: object serialization must not be nested.");
        }
    }

    private void e(int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            ByteBuffer byteBuffer = this.a;
            int i4 = this.b - 1;
            this.b = i4;
            byteBuffer.put(i4, (byte) 0);
        }
    }

    private void f(int i2) {
        ByteBuffer byteBuffer = this.a;
        int i3 = this.b - 4;
        this.b = i3;
        byteBuffer.putInt(i3, i2);
    }

    private void g(int i2) {
        c(4, 0);
        f(i2);
    }

    private void h(int i2) {
        this.e[i2] = d();
    }

    public final int a() {
        if (!this.g) {
            throw new AssertionError("FlatBuffers: endVector called without startVector");
        }
        this.g = false;
        f(this.l);
        return d();
    }

    public int a(CharSequence charSequence) {
        int length = (int) (((float) charSequence.length()) * this.n.maxBytesPerChar());
        if (this.o == null || this.o.capacity() < length) {
            this.o = ByteBuffer.allocate(Math.max(128, length));
        }
        this.o.clear();
        CoderResult encode = this.n.encode(charSequence instanceof CharBuffer ? (CharBuffer) charSequence : CharBuffer.wrap(charSequence), this.o, true);
        if (encode.isError()) {
            try {
                encode.throwException();
            } catch (CharacterCodingException e2) {
                throw new Error(e2);
            }
        }
        this.o.flip();
        ByteBuffer byteBuffer = this.o;
        int remaining = byteBuffer.remaining();
        a((byte) 0);
        a(1, remaining, 1);
        ByteBuffer byteBuffer2 = this.a;
        int i2 = this.b - remaining;
        this.b = i2;
        byteBuffer2.position(i2);
        this.a.put(byteBuffer);
        return a();
    }

    public final ev a(ByteBuffer byteBuffer) {
        this.a = byteBuffer;
        this.a.clear();
        this.a.order(ByteOrder.LITTLE_ENDIAN);
        this.d = 1;
        this.b = this.a.capacity();
        this.f = 0;
        this.g = false;
        this.h = false;
        this.i = 0;
        this.k = 0;
        this.l = 0;
        return this;
    }

    public final void a(byte b2) {
        c(1, 0);
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 1;
        this.b = i2;
        byteBuffer.put(i2, b2);
    }

    public final void a(int i2) {
        c(4, 0);
        if (p || i2 <= d()) {
            f((d() - i2) + 4);
            return;
        }
        throw new AssertionError();
    }

    public final void a(int i2, byte b2) {
        if (this.m || b2 != 0) {
            a(b2);
            h(i2);
        }
    }

    public final void a(int i2, int i3) {
        if (this.m || i3 != 0) {
            g(i3);
            h(i2);
        }
    }

    public final void a(int i2, int i3, int i4) {
        e();
        this.l = i3;
        c(4, i2 * i3);
        c(i4, i2 * i3);
        this.g = true;
    }

    public final void a(int i2, long j2) {
        if (this.m || j2 != 0) {
            c(8, 0);
            ByteBuffer byteBuffer = this.a;
            int i3 = this.b - 8;
            this.b = i3;
            byteBuffer.putLong(i3, j2);
            h(i2);
        }
    }

    public final void a(int i2, short s) {
        if (this.m || s != 0) {
            a(s);
            h(i2);
        }
    }

    public final void a(boolean z) {
        int i2 = 1;
        if (this.m || z) {
            c(1, 0);
            ByteBuffer byteBuffer = this.a;
            int i3 = this.b - 1;
            this.b = i3;
            if (!z) {
                i2 = 0;
            }
            byteBuffer.put(i3, (byte) i2);
            h(0);
        }
    }

    public final int b() {
        int i2;
        if (this.e == null || !this.g) {
            throw new AssertionError("FlatBuffers: endObject called without startObject");
        }
        g(0);
        int d2 = d();
        for (int i3 = this.f - 1; i3 >= 0; i3--) {
            a((short) (this.e[i3] != 0 ? d2 - this.e[i3] : 0));
        }
        a((short) (d2 - this.i));
        a((short) ((this.f + 2) * 2));
        int i4 = 0;
        loop1:
        while (true) {
            if (i4 >= this.k) {
                i2 = 0;
                break;
            }
            int capacity = this.a.capacity() - this.j[i4];
            int i5 = this.b;
            short s = this.a.getShort(capacity);
            if (s == this.a.getShort(i5)) {
                int i6 = 2;
                while (i6 < s) {
                    if (this.a.getShort(capacity + i6) == this.a.getShort(i5 + i6)) {
                        i6 += 2;
                    }
                }
                i2 = this.j[i4];
                break loop1;
            }
            i4++;
        }
        if (i2 != 0) {
            this.b = this.a.capacity() - d2;
            this.a.putInt(this.b, i2 - d2);
        } else {
            if (this.k == this.j.length) {
                this.j = Arrays.copyOf(this.j, this.k * 2);
            }
            int[] iArr = this.j;
            int i7 = this.k;
            this.k = i7 + 1;
            iArr[i7] = d();
            this.a.putInt(this.a.capacity() - d2, d() - d2);
        }
        this.g = false;
        return d2;
    }

    public final void b(int i2) {
        e();
        if (this.e == null || this.e.length < i2) {
            this.e = new int[i2];
        }
        this.f = i2;
        Arrays.fill(this.e, 0, this.f, 0);
        this.g = true;
        this.i = d();
    }

    public final void b(int i2, int i3) {
        if (this.m || i3 != 0) {
            a(i3);
            h(i2);
        }
    }

    public final void c(int i2) {
        c(this.d, 4);
        a(i2);
        this.a.position(this.b);
        this.h = true;
    }

    public final byte[] c() {
        int i2 = this.b;
        int capacity = this.a.capacity() - this.b;
        if (!this.h) {
            throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
        }
        byte[] bArr = new byte[capacity];
        this.a.position(i2);
        this.a.get(bArr);
        return bArr;
    }
}
