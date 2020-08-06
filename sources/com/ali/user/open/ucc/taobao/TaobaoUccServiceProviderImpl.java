package com.ali.user.open.ucc.taobao;

import android.content.Context;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.oauth.OauthService;
import com.ali.user.open.ucc.base.BaseUccServiceProvider;
import java.util.Map;

public class TaobaoUccServiceProviderImpl extends BaseUccServiceProvider {
    public static final String TAG = "TaobaoUccServiceProviderImpl";

    /* access modifiers changed from: protected */
    public boolean isAuthByNative(Context context, String targetSite, Map<String, String> map) {
        return ((OauthService) AliMemberSDK.getService(OauthService.class)).isAppAuthSurpport(context, targetSite);
    }

    public void refreshWhenLogin(String targetSite, String loginReturnData, boolean cookieOnly) {
        ((OauthService) AliMemberSDK.getService(OauthService.class)).refreshWhenLogin(Site.TAOBAO, loginReturnData, cookieOnly);
    }
}
