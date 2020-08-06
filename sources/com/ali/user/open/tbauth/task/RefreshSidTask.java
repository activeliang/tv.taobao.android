package com.ali.user.open.tbauth.task;

import android.app.Activity;
import android.webkit.WebView;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.task.TaskWithDialog;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.tbauth.TbAuthComponent;
import com.ali.user.open.tbauth.ui.context.CallbackContext;

public class RefreshSidTask extends TaskWithDialog<String, Void, Void> {
    private WebView view;

    public RefreshSidTask(WebView view2) {
        super((Activity) view2.getContext());
        this.view = view2;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        if (!((SessionService) AliMemberSDK.getService(SessionService.class)).isSessionValid()) {
            CallbackContext.setActivity(this.activity);
            TbAuthComponent.INSTANCE.showLogin(this.activity);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.toastSystemException();
    }
}
