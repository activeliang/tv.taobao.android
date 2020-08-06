package com.ali.auth.third.offline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;
import com.ali.auth.third.offline.login.RequestCode;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.task.LoginBySsoTokenTask;
import java.util.Map;
import org.json.JSONObject;

public class LoginActivity extends FragmentActivity {
    public static final String FRAGMENT_TAG = "login_fragment";
    public static final String TAG = "login.LoginActivity";
    protected FragmentManager mFragmentManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        SDKLogger.d(TAG, "LoginActivity onCreate");
        super.onCreate(savedInstanceState);
        setOrientation();
        if (KernelContext.context == null) {
            KernelContext.context = getApplicationContext();
        }
        if (!KernelContext.checkServiceValid()) {
            SDKLogger.d(TAG, "static field null");
            LoginStatus.resetLoginFlag();
            finish();
            return;
        }
        CallbackContext.setActivity(this);
        SDKLogger.e(TAG, "before mtop call showLogin");
        auth();
    }

    public void setOrientation() {
        if (ConfigManager.getInstance().getOrientation() != 0) {
            if (getResources().getConfiguration().orientation == 2) {
                setRequestedOrientation(1);
            }
            getWindow().setSoftInputMode(18);
        } else if (getResources().getConfiguration().orientation == 1) {
            setRequestedOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LoginStatus.resetLoginFlag();
        CallbackContext.loginCallback = null;
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void auth() {
        switch (getIntent().getIntExtra("login_type", 0)) {
            case 1:
                try {
                    String params = getIntent().getStringExtra("params");
                    Map<String, Object> paramMsp = null;
                    if (!TextUtils.isEmpty(params)) {
                        paramMsp = JSONUtils.toMap(new JSONObject(params));
                    }
                    LoginComponent.INSTANCE.goQrCodeLogin(this, paramMsp);
                    return;
                } catch (Throwable th) {
                    return;
                }
            case 5:
                try {
                    String ssoToken = getIntent().getStringExtra("token");
                    new LoginBySsoTokenTask(this, (LoginCallback) CallbackContext.loginCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{ssoToken});
                    return;
                } catch (Throwable th2) {
                    return;
                }
            default:
                setLayout();
                return;
        }
    }

    private void setLayout() {
        try {
            setContentView(R.layout.user_login_activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mFragmentManager = getSupportFragmentManager();
        openFragmentByAppId(getIntent());
    }

    private void openFragmentByAppId(Intent intent) {
        Fragment loginFragment;
        try {
            if (ConfigManager.getInstance().getFullyCustomizedLoginFragment() != null) {
                loginFragment = (Fragment) ConfigManager.getInstance().getFullyCustomizedLoginFragment().newInstance();
            } else {
                loginFragment = new LoginFragment();
            }
            Fragment fragment = this.mFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (fragment != null) {
                this.mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
            this.mFragmentManager.beginTransaction().add(R.id.loginContainer, loginFragment, FRAGMENT_TAG).commitAllowingStateLoss();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!KernelContext.checkServiceValid()) {
            finish();
            LoginStatus.resetLoginFlag();
        }
    }

    public void onBackPressed() {
        Message errorMessage = MessageUtils.createMessage(10003, new Object[0]);
        if (CallbackContext.loginCallback != null) {
            ((LoginCallback) CallbackContext.loginCallback).onFailure(errorMessage.code, errorMessage.message);
        }
        finishCurrentAndNotify();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void finishCurrentAndNotify() {
        /*
            r1 = this;
            r1.finish()     // Catch:{ Throwable -> 0x0009, all -> 0x0007 }
            com.ali.auth.third.offline.login.util.LoginStatus.resetLoginFlag()     // Catch:{ Throwable -> 0x0009, all -> 0x0007 }
        L_0x0006:
            return
        L_0x0007:
            r0 = move-exception
            throw r0
        L_0x0009:
            r0 = move-exception
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.offline.LoginActivity.finishCurrentAndNotify():void");
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SDKLogger.d(TAG, "onActivityResult requestCode = " + requestCode + " resultCode=" + resultCode);
        if (!KernelContext.checkServiceValid()) {
            finish();
        } else if (requestCode == RequestCode.OPEN_DOUBLE_CHECK) {
            super.onActivityResult(requestCode, resultCode, data);
            Fragment loginFragment = this.mFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (loginFragment != null && loginFragment.isVisible()) {
                loginFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (CallbackContext.activity == null) {
                CallbackContext.setActivity(this);
            }
            CallbackContext.onActivityResult(requestCode, resultCode, data);
        }
    }
}
