package com.ali.user.open.ucc.icbu;

import android.content.Context;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.oauth.OauthService;
import com.ali.user.open.ucc.base.BaseUccServiceProvider;
import java.util.Map;

public class IcbuUccServiceProviderImpl extends BaseUccServiceProvider {
    /* access modifiers changed from: protected */
    public boolean isAuthByNative(Context context, String targetSite, Map<String, String> map) {
        return false;
    }

    public void refreshWhenLogin(String targetSite, String loginReturnData, boolean cookieOnly) {
        ((OauthService) AliMemberSDK.getService(OauthService.class)).refreshWhenLogin(Site.ICBU, loginReturnData, cookieOnly);
    }
}
