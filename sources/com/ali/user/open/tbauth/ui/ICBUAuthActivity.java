package com.ali.user.open.tbauth.ui;

import android.app.Activity;
import android.content.Intent;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.Environment;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ResourceUtils;
import com.ali.user.open.tbauth.RequestCode;

public class ICBUAuthActivity extends TbAuthActivity {
    /* access modifiers changed from: protected */
    public void auth() {
        showH5Login(this);
    }

    public void showH5Login(Activity activity) {
        SDKLogger.d(TbAuthActivity.TAG, "open H5 login");
        Intent h5Intent = new Intent(activity, TbAuthWebViewActivity.class);
        StringBuilder builder = new StringBuilder();
        if (ConfigManager.getInstance().getEnvironment().equals(Environment.TEST)) {
            builder.append(ConfigManager.ICBU_LOGIN_HOST_DAILY);
        } else {
            builder.append(ConfigManager.ICBU_LOGIN_HOST);
        }
        builder.append(((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
        if (ConfigManager.getInstance().getCurrentLanguage() != null) {
            builder.append("&lang=");
            builder.append(ConfigManager.getInstance().getCurrentLanguage().toString());
        }
        h5Intent.putExtra("url", builder.toString());
        h5Intent.putExtra("title", ResourceUtils.getString(activity.getApplicationContext(), "member_sdk_authorize_title"));
        activity.startActivityForResult(h5Intent, RequestCode.OPEN_ICBU_H5_LOGIN);
    }
}
