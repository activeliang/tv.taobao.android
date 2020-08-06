package com.loc;

import android.net.Uri;
import android.text.TextUtils;

/* compiled from: IPV6Request */
public abstract class q extends az {
    public final boolean g() {
        return true;
    }

    public String h() {
        if (TextUtils.isEmpty(c())) {
            return c();
        }
        String c = c();
        Uri parse = Uri.parse(c);
        return !parse.getAuthority().startsWith("dualstack-") ? parse.buildUpon().authority("dualstack-" + parse.getAuthority()).build().toString() : c;
    }
}
