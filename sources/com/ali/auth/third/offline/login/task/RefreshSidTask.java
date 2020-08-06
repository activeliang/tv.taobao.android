package com.ali.auth.third.offline.login.task;

import android.app.Activity;
import android.webkit.WebView;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.task.TaskWithDialog;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginComponent;

public class RefreshSidTask extends TaskWithDialog<String, Void, Void> {
    private WebView view;

    public RefreshSidTask(WebView view2) {
        super((Activity) view2.getContext());
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        if (!KernelContext.credentialService.isSessionValid()) {
            CallbackContext.setActivity(this.activity);
            LoginComponent.INSTANCE.showLogin(this.activity);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.toastSystemException();
    }
}
