package com.loc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: AbstractBuilder */
public abstract class bx {
    bz a = new bz(this.b);
    private ByteBuffer b;

    bx(int i) {
        this.b = ByteBuffer.allocate(i);
        this.b.order(ByteOrder.LITTLE_ENDIAN);
    }

    public final bx a() {
        this.a.a(this.b);
        return this;
    }
}
