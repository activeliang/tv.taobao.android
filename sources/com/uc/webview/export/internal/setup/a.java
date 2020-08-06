package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.WebView;
import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.Date;
import java.util.HashMap;
import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: ProGuard */
public final class a extends UCSubSetupTask<a, a> {
    private static boolean a = false;

    public final void run() {
        Context context;
        a.C0009a aVar;
        String str;
        if (a && (context = (Context) this.mOptions.get("CONTEXT")) != null) {
            if (((Boolean) UCMPackageInfo.invoke(UCMPackageInfo.hadInstallUCMobile, context)).booleanValue()) {
                try {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= 12) {
                        intent.addFlags(32);
                    }
                    intent.setAction("com.UCMobile.intent.action.FRIEND");
                    intent.setClassName("com.UCMobile", "com.uc.base.push.PushFriendBridge");
                    StringBuilder append = new StringBuilder().append(context.getPackageName()).append(SymbolExpUtil.SYMBOL_COLON);
                    switch (WebView.getCoreType()) {
                        case 1:
                            str = "u3";
                            break;
                        case 2:
                            str = DispatchConstants.ANDROID;
                            break;
                        case 3:
                            str = "u4";
                            break;
                        default:
                            str = "0";
                            break;
                    }
                    intent.putExtra(BaseConfig.INTENT_KEY_SOURCE, append.append(str).toString());
                    context.startService(intent);
                } catch (Throwable th) {
                }
                if (com.uc.webview.export.internal.c.a.a.a == null && d.e != null) {
                    com.uc.webview.export.internal.c.a.a.a(d.e);
                }
                com.uc.webview.export.internal.c.a.a aVar2 = com.uc.webview.export.internal.c.a.a.a;
                if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
                    Date date = new Date(System.currentTimeMillis());
                    int intValue = ((Boolean) d.a(10010, new Object[0])).booleanValue() ? ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue() : 0;
                    if (!(intValue == 2 || intValue == 0)) {
                        intValue = (intValue * 10) + d.l;
                    }
                    String str2 = aVar2.f.format(date) + "~" + intValue;
                    synchronized (aVar2.h) {
                        if (aVar2.d == null) {
                            aVar2.d = new HashMap();
                        }
                        a.C0009a aVar3 = aVar2.d.get(str2);
                        if (aVar3 == null) {
                            a.C0009a aVar4 = new a.C0009a(aVar2, (byte) 0);
                            aVar2.d.put(str2, aVar4);
                            aVar = aVar4;
                        } else {
                            aVar = aVar3;
                        }
                        aVar.b.put("tm", aVar2.g.format(date));
                        Integer num = aVar.a.get(IWaStat.ACTIVATE_PUSH_PROCESS);
                        if (num == null) {
                            aVar.a.put(IWaStat.ACTIVATE_PUSH_PROCESS, 1);
                        } else {
                            aVar.a.put(IWaStat.ACTIVATE_PUSH_PROCESS, Integer.valueOf(num.intValue() + 1));
                        }
                    }
                }
            }
        }
    }
}
