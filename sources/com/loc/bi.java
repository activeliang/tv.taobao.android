package com.loc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* compiled from: ByteJoinDataStrategy */
public final class bi extends bo {
    ByteArrayOutputStream a = new ByteArrayOutputStream();

    public bi() {
    }

    public bi(bo boVar) {
        super(boVar);
    }

    /* access modifiers changed from: protected */
    public final byte[] a(byte[] bArr) {
        byte[] byteArray = this.a.toByteArray();
        try {
            this.a.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.a = new ByteArrayOutputStream();
        return byteArray;
    }

    public final void b(byte[] bArr) {
        try {
            this.a.write(bArr);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
