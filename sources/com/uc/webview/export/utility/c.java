package com.uc.webview.export.utility;

import java.util.Formatter;
import java.util.Locale;

/* compiled from: ProGuard */
final class c extends ThreadLocal<Formatter> {
    StringBuilder a = new StringBuilder();

    c() {
    }

    public final /* synthetic */ Object get() {
        Formatter formatter = (Formatter) super.get();
        this.a.setLength(0);
        return formatter;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public synchronized Formatter initialValue() {
        return new Formatter(this.a, Locale.getDefault());
    }
}
