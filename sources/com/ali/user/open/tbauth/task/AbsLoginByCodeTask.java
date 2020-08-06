package com.ali.user.open.tbauth.task;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.task.TaskWithDialog;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.RequestCode;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.ali.user.open.tbauth.ui.TbAuthWebViewActivity;
import com.ali.user.open.tbauth.ui.context.CallbackContext;
import com.ali.user.open.tbauth.util.SessionConvert;

public abstract class AbsLoginByCodeTask extends TaskWithDialog<String, Void, Void> {
    private static final String TAG = "AbsLoginByCodeTask";

    /* access modifiers changed from: protected */
    public abstract void doWhenResultFail(int i, String str);

    /* access modifiers changed from: protected */
    public abstract void doWhenResultOk(Session session);

    /* access modifiers changed from: protected */
    public abstract RpcResponse<LoginReturnData> login(String[] strArr);

    public AbsLoginByCodeTask(Activity activity) {
        super(activity);
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(String... params) {
        final RpcResponse<LoginReturnData> resultData = login(params);
        final int code = resultData.code;
        SDKLogger.d(TAG, "asyncExecute code = " + code);
        if (code == 3000) {
            Session session = null;
            try {
                if (resultData.returnValue != null) {
                    if (TbAuthContext.needSession) {
                        ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.TAOBAO, (LoginReturnData) resultData.returnValue);
                        session = ((SessionService) AliMemberSDK.getService(SessionService.class)).getSession();
                    } else {
                        session = SessionConvert.convertLoginDataToSeesion((LoginReturnData) resultData.returnValue);
                    }
                }
                final Session finalSession = session;
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                    public void run() {
                        AbsLoginByCodeTask.this.doWhenResultOk(finalSession);
                    }
                });
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (code == 13060) {
            String doubleCheckUrl = ((LoginReturnData) resultData.returnValue).h5Url;
            SDKLogger.d(TAG, "asyncExecute doubleCheckUrl = " + doubleCheckUrl);
            if (TextUtils.isEmpty(doubleCheckUrl)) {
                return null;
            }
            Activity startFrom = this.activity;
            CallbackContext.setActivity(startFrom);
            Intent intent = new Intent(startFrom, TbAuthWebViewActivity.class);
            intent.putExtra("url", doubleCheckUrl);
            intent.putExtra("token", ((LoginReturnData) resultData.returnValue).token);
            intent.putExtra("scene", ((LoginReturnData) resultData.returnValue).scene);
            TbAuthWebViewActivity.token = ((LoginReturnData) resultData.returnValue).token;
            TbAuthWebViewActivity.scene = ((LoginReturnData) resultData.returnValue).scene;
            this.activity.startActivityForResult(intent, RequestCode.OPEN_DOUBLE_CHECK);
            return null;
        } else {
            ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                public void run() {
                    SDKLogger.d(AbsLoginByCodeTask.TAG, "15 : " + resultData.message);
                    AbsLoginByCodeTask.this.doWhenResultFail(15, "login:code " + code + " " + resultData.message);
                }
            });
            return null;
        }
    }
}
