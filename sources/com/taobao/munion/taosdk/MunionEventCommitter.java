package com.taobao.munion.taosdk;

import android.net.Uri;
import android.support.annotation.Keep;

@Keep
public interface MunionEventCommitter {
    Uri commitEvent(String str, Uri uri);

    String commitEvent(String str);

    String commitEvent(String str, String str2);
}
