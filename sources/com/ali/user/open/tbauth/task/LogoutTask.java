package com.ali.user.open.tbauth.task;

import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.model.RpcResponse;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.task.AbsAsyncTask;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.device.DeviceTokenManager;
import com.ali.user.open.history.AccountHistoryManager;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.service.impl.SessionManager;
import com.ali.user.open.tbauth.callback.LogoutCallback;

public class LogoutTask extends AbsAsyncTask<Void, Void, Void> {
    /* access modifiers changed from: private */
    public LogoutCallback mLogoutCallback;

    public LogoutTask(LogoutCallback logoutCallback) {
        this.mLogoutCallback = logoutCallback;
    }

    /* access modifiers changed from: protected */
    public Void asyncExecute(Void... params) {
        try {
            SDKLogger.e("logout task", "into logout " + SessionManager.INSTANCE.getInternalSession().toString());
            if (!TextUtils.isEmpty(SessionManager.INSTANCE.getInternalSession().userId) && !TextUtils.isEmpty(SessionManager.INSTANCE.getInternalSession().userId)) {
                RpcRepository.logout(new RpcRequestCallbackWithCode() {
                    public void onSuccess(RpcResponse response) {
                    }

                    public void onSystemError(String code, RpcResponse response) {
                    }

                    public void onError(String code, RpcResponse response) {
                    }
                });
            }
            AccountHistoryManager.getInstance().clearHistoryAccount();
            DeviceTokenManager.getInstance().clearDeviceToken();
            ResultCode resultCode = ((SessionService) AliMemberSDK.getService(SessionService.class)).logout(Site.TAOBAO);
            if (ResultCode.SUCCESS.equals(resultCode)) {
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                    public void run() {
                        if (LogoutTask.this.mLogoutCallback != null) {
                            LogoutTask.this.mLogoutCallback.onSuccess();
                        }
                    }
                });
                return null;
            }
            CommonUtils.onFailure(this.mLogoutCallback, resultCode);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ResultCode resultCode2 = ((SessionService) AliMemberSDK.getService(SessionService.class)).logout(Site.TAOBAO);
            if (ResultCode.SUCCESS.equals(resultCode2)) {
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                    public void run() {
                        if (LogoutTask.this.mLogoutCallback != null) {
                            LogoutTask.this.mLogoutCallback.onSuccess();
                        }
                    }
                });
                return null;
            }
            CommonUtils.onFailure(this.mLogoutCallback, resultCode2);
            return null;
        } catch (Throwable th) {
            Throwable th2 = th;
            ResultCode resultCode3 = ((SessionService) AliMemberSDK.getService(SessionService.class)).logout(Site.TAOBAO);
            if (ResultCode.SUCCESS.equals(resultCode3)) {
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postUITask(new Runnable() {
                    public void run() {
                        if (LogoutTask.this.mLogoutCallback != null) {
                            LogoutTask.this.mLogoutCallback.onSuccess();
                        }
                    }
                });
            } else {
                CommonUtils.onFailure(this.mLogoutCallback, resultCode3);
            }
            throw th2;
        }
    }

    /* access modifiers changed from: protected */
    public void doWhenException(Throwable t) {
        CommonUtils.onFailure(this.mLogoutCallback, ResultCode.create(10010, t.getMessage()));
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
    }
}
