package com.ali.auth.third.core.service;

import android.app.Activity;
import com.ali.auth.third.core.model.SNSConfig;
import com.ali.auth.third.core.model.SNSSignInAccount;

public interface SNSSignInListener {
    void onCancel(String str);

    void onError(String str, int i, String str2);

    void onSucceed(Activity activity, SNSConfig sNSConfig, SNSSignInAccount sNSSignInAccount);
}
