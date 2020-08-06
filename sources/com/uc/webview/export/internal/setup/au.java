package com.uc.webview.export.internal.setup;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: ProGuard */
final class au implements FilenameFilter {
    au() {
    }

    public final boolean accept(File file, String str) {
        return str.startsWith("lib") && str.endsWith("_kr_uc.so");
    }
}
