package com.taobao.atlas.dex.util;

import com.taobao.atlas.dexmerge.dx.util.Output;

public interface AnnotatedOutput extends Output {
    void annotate(int i, String str);

    void annotate(String str);

    boolean annotates();

    void endAnnotation();

    int getAnnotationWidth();

    boolean isVerbose();
}
