package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ProGuard */
final class ac implements ValueCallback<u> {
    final /* synthetic */ z a;

    ac(z zVar) {
        this.a = zVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        if (uVar.getException() != null) {
            UCSetupException unused = this.a.e = uVar.getException();
            UCSetupTask unused2 = this.a.f = uVar;
        }
        Integer num = (Integer) this.a.mOptions.get(UCCore.OPTION_DELETE_CORE_POLICY);
        if (num != null && num.intValue() != 0 && (uVar instanceof ag) && ((uVar.getException().errCode() == 1008 && (num.intValue() & 1) != 0) || ((uVar.getException().errCode() == 3007 && (num.intValue() & 2) != 0) || (uVar.getException().errCode() == 4005 && (num.intValue() & 4) != 0)))) {
            if (this.a.i == null) {
                List unused3 = this.a.i = new ArrayList();
            }
            this.a.i.add((ag) uVar);
        }
        if (z.a.size() > 0) {
            ((UCSetupTask) z.a.pop()).start();
        } else if (this.a.b != null) {
            ((u) ((u) ((u) this.a.b.invoke(10001, this.a)).onEvent(BlitzServiceUtils.CSUCCESS, this.a.j)).onEvent("exception", this.a.k)).start();
            u unused4 = this.a.b = null;
        } else {
            this.a.setException(uVar.getException());
        }
    }
}
