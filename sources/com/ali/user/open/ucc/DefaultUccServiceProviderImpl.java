package com.ali.user.open.ucc;

import android.content.Context;
import com.ali.user.open.ucc.base.BaseUccServiceProvider;
import java.util.Map;

public class DefaultUccServiceProviderImpl extends BaseUccServiceProvider {
    public static final String TAG = "AlipayUccServiceProviderImpl";

    /* access modifiers changed from: protected */
    public boolean isAuthByNative(Context context, String targetSite, Map<String, String> map) {
        return false;
    }
}
