package com.ali.user.open.ucc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import com.ali.user.open.ucc.util.UccConstants;
import java.util.HashMap;
import java.util.Map;

public class UccActivity extends Activity {
    public static final String TAG = "UccActivity";
    public static UccCallback mUccCallback;
    LinearLayout hiddenLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hiddenLayout = new LinearLayout(this);
        this.hiddenLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setContentView(this.hiddenLayout);
        if (KernelContext.applicationContext == null) {
            KernelContext.applicationContext = getApplicationContext();
        }
        this.hiddenLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SDKLogger.e(UccActivity.TAG, "click to destroy");
                UccActivity.this.finish();
            }
        });
        this.hiddenLayout.setClickable(false);
        this.hiddenLayout.setLongClickable(false);
        if (!KernelContext.checkServiceValid()) {
            SDKLogger.d(TAG, "static field null");
            finish();
            return;
        }
        SDKLogger.e(TAG, "before mtop call showLogin");
        auth();
    }

    /* access modifiers changed from: protected */
    public void auth() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        int type = intent.getIntExtra(UccConstants.PARAM_FUNC_TYPE, 2);
        String targetSite = intent.getStringExtra(UccConstants.PARAM_TARGET_SITE);
        String userToken = intent.getStringExtra("userToken");
        Map<String, String> param = new HashMap<>();
        param.put(ParamsConstants.Key.PARAM_ONLY_AUTHCODE, "1");
        param.put(ParamsConstants.Key.PARAM_IS_BIND, "1");
        param.put("needSession", intent.getStringExtra("needSession"));
        param.put(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY, intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY));
        param.put("scene", intent.getStringExtra("scene"));
        param.put(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN, intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN));
        param.put(ParamsConstants.Key.PARAM_NEED_BIND, intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_BIND));
        param.put("site", intent.getStringExtra("site"));
        switch (type) {
            case 1:
                ((UccService) AliMemberSDK.getService(UccService.class)).trustLogin(this, targetSite, param, new UccCallback() {
                    public void onSuccess(String targetSite, Map params) {
                        if (UccActivity.mUccCallback != null) {
                            UccActivity.mUccCallback.onSuccess(targetSite, params);
                        }
                        UccActivity.this.finish();
                    }

                    public void onFail(String targetSite, int code, String msg) {
                        if (UccActivity.mUccCallback != null) {
                            UccActivity.mUccCallback.onFail(targetSite, code, msg);
                        }
                        UccActivity.this.finish();
                    }
                });
                return;
            default:
                ((UccService) AliMemberSDK.getService(UccService.class)).bind(this, userToken, targetSite, param, new UccCallback() {
                    public void onSuccess(String targetSite, Map params) {
                        if (UccActivity.mUccCallback != null) {
                            UccActivity.mUccCallback.onSuccess(targetSite, params);
                        }
                        UccActivity.this.finish();
                    }

                    public void onFail(String targetSite, int code, String msg) {
                        if (UccActivity.mUccCallback != null) {
                            UccActivity.mUccCallback.onFail(targetSite, code, msg);
                        }
                        UccActivity.this.finish();
                    }
                });
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!KernelContext.checkServiceValid()) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SDKLogger.d(TAG, "onActivityResult requestCode = " + requestCode + " resultCode=" + resultCode);
        if (!KernelContext.checkServiceValid()) {
            finish();
            return;
        }
        this.hiddenLayout.setClickable(true);
        this.hiddenLayout.setLongClickable(true);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        mUccCallback = null;
        super.onDestroy();
    }
}
