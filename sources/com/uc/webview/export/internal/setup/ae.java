package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.internal.c.a.a;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.SetupTask;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/* compiled from: ProGuard */
final class ae implements ValueCallback<SetupTask> {
    LinkedList<Pair<String, HashMap<String, String>>> a = new LinkedList<>();
    final /* synthetic */ ValueCallback b;
    final /* synthetic */ z c;

    ae(z zVar, ValueCallback valueCallback) {
        this.c = zVar;
        this.b = valueCallback;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        a.C0009a aVar;
        SetupTask setupTask = (SetupTask) obj;
        if (setupTask.getStat().second == null) {
            if (a.a == null && d.e != null) {
                a.a(d.e);
            }
            a aVar2 = a.a;
            String str = (String) setupTask.getStat().first;
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
                    Integer num = aVar.a.get(str);
                    if (num == null) {
                        aVar.a.put(str, 1);
                    } else {
                        aVar.a.put(str, Integer.valueOf(num.intValue() + 1));
                    }
                }
                return;
            }
            return;
        }
        this.a.add(setupTask.getStat());
        boolean z = SetupTask.getTotalLoadedUCM() != null;
        boolean z2 = z && SetupTask.getTotalLoadedUCM().coreType == 2;
        boolean z3 = z && SetupTask.getTotalLoadedUCM().coreType != 2;
        if (((String) setupTask.getStat().first).equals(IWaStat.SETUP_TOTAL_EXCEPTION) || z2 || Log.sPrintLog) {
            Iterator it = this.a.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                if (this.b == null) {
                    if (a.a == null && d.e != null) {
                        a.a(d.e);
                    }
                    a aVar5 = a.a;
                    if (Log.sPrintLog) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("ev_ac=").append((String) pair.first);
                        for (Map.Entry entry : ((HashMap) pair.second).entrySet()) {
                            sb.append("`").append((String) entry.getKey()).append("=").append((String) entry.getValue());
                        }
                        Log.d("SDKWaStat", sb.toString());
                    }
                    if (((Boolean) d.a(10006, "stat", true)).booleanValue() && !d.f) {
                        synchronized (aVar5.h) {
                            if (aVar5.e == null) {
                                aVar5.e = new ArrayList();
                            }
                            ((HashMap) pair.second).put("tm", aVar5.g.format(new Date(System.currentTimeMillis())));
                            aVar5.e.add(new a.b((String) pair.first, (Map) pair.second));
                        }
                    }
                } else {
                    setupTask.mStat = pair;
                    this.b.onReceiveValue(setupTask);
                }
            }
            this.a.clear();
        } else if (z3) {
            this.a.clear();
        }
    }
}
