package com.loc;

/* compiled from: LogJsonDataStrategy */
public final class bl extends bo {
    private StringBuilder a = new StringBuilder();
    private boolean b = true;

    public bl() {
    }

    public bl(bo boVar) {
        super(boVar);
    }

    /* access modifiers changed from: protected */
    public final byte[] a(byte[] bArr) {
        byte[] a2 = u.a(this.a.toString());
        this.d = a2;
        this.b = true;
        this.a.delete(0, this.a.length());
        return a2;
    }

    public final void b(byte[] bArr) {
        String a2 = u.a(bArr);
        if (this.b) {
            this.b = false;
        } else {
            this.a.append(",");
        }
        this.a.append("{\"log\":\"").append(a2).append("\"}");
    }
}
