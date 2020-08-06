package com.ali.user.open.ucc.alipay3;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.oauth.AppCredential;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.base.BaseUccServiceProvider;
import com.ali.user.open.ucc.model.UccParams;
import java.util.Map;

public class AlipayUccServiceProviderImpl extends BaseUccServiceProvider {
    public static final String TAG = "AlipayUccServiceProviderImpl";

    public void bind(Activity activity, UccParams uccParams, AppCredential appCredential, Map<String, String> params, UccCallback uccCallback) {
        super.bind(activity, uccParams, appCredential, params, uccCallback);
    }

    /* access modifiers changed from: protected */
    public boolean isAuthByNative(Context context, String targetSite, Map<String, String> params) {
        if (params == null || !TextUtils.equals(params.get(ParamsConstants.Key.PARAM_FORCE_NATIVE), "1")) {
            return false;
        }
        return true;
    }
}
