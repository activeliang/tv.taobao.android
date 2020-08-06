package com.loc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/* compiled from: ALLog */
public final class dk {
    private static volatile dl a;

    public static void a(Throwable th) {
        if (cz.a) {
            Throwable th2 = th;
            while (true) {
                if (th2 != null) {
                    if (th2 instanceof UnknownHostException) {
                        break;
                    }
                    th2 = th2.getCause();
                } else {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    th.printStackTrace(printWriter);
                    printWriter.flush();
                    stringWriter.toString();
                    break;
                }
            }
            if (a != null) {
                dl dlVar = a;
            }
        }
    }
}
