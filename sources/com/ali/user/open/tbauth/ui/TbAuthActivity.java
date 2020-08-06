package com.ali.user.open.tbauth.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.tbauth.TbAuthComponent;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.tbauth.task.RpcPresenter;
import com.ali.user.open.tbauth.ui.context.CallbackContext;

public class TbAuthActivity extends Activity {
    public static final String TAG = "login.TbAuthActivity";
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
                SDKLogger.e(TbAuthActivity.TAG, "click to destroy");
                TbAuthActivity.this.finish();
            }
        });
        this.hiddenLayout.setClickable(false);
        this.hiddenLayout.setLongClickable(false);
        if (!KernelContext.checkServiceValid()) {
            SDKLogger.d(TAG, "static field null");
            finish();
            return;
        }
        CallbackContext.setActivity(this);
        SDKLogger.e(TAG, "before mtop call showLogin");
        auth();
    }

    /* access modifiers changed from: protected */
    public void auth() {
        switch (getIntent().getIntExtra("login_type", 0)) {
            case 1:
                RpcPresenter.loginByIVToken(this, getIntent().getIntExtra("site", 0), getIntent().getStringExtra(TbAuthConstants.LOGIN_TOKEN), getIntent().getStringExtra("scene"), getIntent().getStringExtra(TbAuthConstants.H5_QUERY_STR), (LoginCallback) CallbackContext.loginCallback);
                return;
            case 4:
                return;
            default:
                TbAuthComponent.INSTANCE.showLogin(this);
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
        if (CallbackContext.activity == null) {
            CallbackContext.setActivity(this);
        }
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }
}
