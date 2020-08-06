package com.loc;

import java.nio.ByteBuffer;

/* compiled from: RobustFlatBufferBuilder */
public final class bz extends ev {
    bz(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    public final int a(CharSequence charSequence) {
        try {
            return super.a(charSequence);
        } catch (Throwable th) {
            dk.a(th);
            return super.a((CharSequence) "");
        }
    }
}
