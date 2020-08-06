package com.uc.webview.export.internal.utility;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/* compiled from: ProGuard */
final class d implements FileFilter {
    private Pattern a = Pattern.compile("cpu[0-9]+", 0);

    d() {
    }

    public final boolean accept(File file) {
        return this.a.matcher(file.getName()).matches();
    }
}
