package com.uc.webview.export.utility;

import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSetupException;
import java.util.HashMap;

/* compiled from: ProGuard */
final class b extends HashMap<String, String> {
    final /* synthetic */ UCSetupException a;
    final /* synthetic */ SetupTask b;

    b(SetupTask setupTask, UCSetupException uCSetupException) {
        int i = 256;
        this.b = setupTask;
        this.a = uCSetupException;
        String str = "";
        String str2 = "";
        try {
            str = this.a.getRootCause().getClass().getName();
            String message = this.a.getRootCause().getMessage();
            str2 = message.substring(0, 256 > message.length() ? message.length() : i);
        } catch (Exception e) {
        }
        put(IWaStat.KEY_CNT, "1");
        put("err", String.valueOf(this.a.errCode()));
        put(IWaStat.KEY_CLASS, str);
        put("msg", str2);
    }
}
