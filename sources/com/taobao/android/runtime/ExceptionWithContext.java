package com.taobao.android.runtime;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ExceptionWithContext extends RuntimeException {
    private StringBuffer context;

    public static ExceptionWithContext withContext(Throwable ex, String str, Object... formatArgs) {
        ExceptionWithContext ewc;
        if (ex instanceof ExceptionWithContext) {
            ewc = (ExceptionWithContext) ex;
        } else {
            ewc = new ExceptionWithContext(ex);
        }
        ewc.addContext(String.format(str, formatArgs));
        return ewc;
    }

    public ExceptionWithContext(String message, Object... formatArgs) {
        this((Throwable) null, message, formatArgs);
    }

    public ExceptionWithContext(Throwable cause) {
        this(cause, (String) null, new Object[0]);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ExceptionWithContext(java.lang.Throwable r4, java.lang.String r5, java.lang.Object... r6) {
        /*
            r3 = this;
            if (r5 == 0) goto L_0x0028
            java.lang.String r1 = formatMessage(r5, r6)
        L_0x0006:
            r3.<init>(r1, r4)
            boolean r1 = r4 instanceof com.taobao.android.runtime.ExceptionWithContext
            if (r1 == 0) goto L_0x0031
            com.taobao.android.runtime.ExceptionWithContext r4 = (com.taobao.android.runtime.ExceptionWithContext) r4
            java.lang.StringBuffer r1 = r4.context
            java.lang.String r0 = r1.toString()
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            int r2 = r0.length()
            int r2 = r2 + 200
            r1.<init>(r2)
            r3.context = r1
            java.lang.StringBuffer r1 = r3.context
            r1.append(r0)
        L_0x0027:
            return
        L_0x0028:
            if (r4 == 0) goto L_0x002f
            java.lang.String r1 = r4.getMessage()
            goto L_0x0006
        L_0x002f:
            r1 = 0
            goto L_0x0006
        L_0x0031:
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r2 = 200(0xc8, float:2.8E-43)
            r1.<init>(r2)
            r3.context = r1
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.runtime.ExceptionWithContext.<init>(java.lang.Throwable, java.lang.String, java.lang.Object[]):void");
    }

    private static String formatMessage(String message, Object... formatArgs) {
        if (message == null) {
            return null;
        }
        return String.format(message, formatArgs);
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        out.println(this.context);
    }

    public void addContext(String str) {
        if (str == null) {
            throw new NullPointerException("str == null");
        }
        this.context.append(str);
        if (!str.endsWith("\n")) {
            this.context.append(10);
        }
    }

    public String getContext() {
        return this.context.toString();
    }

    public void printContext(PrintStream out) {
        out.println(getMessage());
        out.print(this.context);
    }

    public void printContext(PrintWriter out) {
        out.println(getMessage());
        out.print(this.context);
    }
}
