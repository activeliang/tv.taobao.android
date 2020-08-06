package com.uc.webview.export.internal.b;

import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.internal.interfaces.InvokeObject;

@Interface
/* compiled from: ProGuard */
public final class a implements InvokeObject {
    Object a;
    int b;

    public a(int i, Object obj) {
        this.a = obj;
        this.b = i;
    }

    public final int a() {
        return this.b;
    }

    public final Object invoke(int i, Object[] objArr) {
        return null;
    }
}
