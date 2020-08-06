package com.uc.webview.export.internal.setup;

import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.File;
import java.io.FilenameFilter;

/* compiled from: ProGuard */
final class at implements FilenameFilter {
    at() {
    }

    public final boolean accept(File file, String str) {
        return file != null && str != null && file.getPath().startsWith("lib") && str.startsWith("lib") && str.endsWith(MergeConstants.SO_SUFFIX) && !str.startsWith("libkernel");
    }
}
