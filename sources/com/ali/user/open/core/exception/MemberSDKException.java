package com.ali.user.open.core.exception;

public class MemberSDKException extends RuntimeException {
    private static final long serialVersionUID = 1357689949294215654L;
    public int code;
    public String message;
    public Throwable throwable;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MemberSDKException(int r2, java.lang.String r3, java.lang.Throwable r4) {
        /*
            r1 = this;
            if (r3 != 0) goto L_0x000f
            java.lang.String r0 = ""
        L_0x0005:
            r1.<init>(r0, r4)
            r1.code = r2
            r1.message = r3
            r1.throwable = r4
            return
        L_0x000f:
            r0 = r3
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.exception.MemberSDKException.<init>(int, java.lang.String, java.lang.Throwable):void");
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MemberSDKException(int r2, java.lang.String r3) {
        /*
            r1 = this;
            if (r3 != 0) goto L_0x000b
            java.lang.String r0 = ""
        L_0x0005:
            r1.<init>(r0)
            r1.message = r3
            return
        L_0x000b:
            r0 = r3
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.core.exception.MemberSDKException.<init>(int, java.lang.String):void");
    }
}
