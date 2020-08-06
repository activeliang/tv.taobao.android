package com.zhiping.tvtao.payment.alipay;

import android.support.annotation.NonNull;
import com.zhiping.tvtao.payment.alipay.request.ReleaseContractRequest;
import com.zhiping.tvtao.payment.alipay.task.AlipayAuthCheckTask;
import com.zhiping.tvtao.payment.alipay.task.AlipayAuthTask;
import com.zhiping.tvtao.payment.alipay.task.AlipayTokenResult;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import java.util.concurrent.Executor;

public class AlipayAuthManager {
    private static AlipayAuthCheckTask authCheckTask;
    private static AlipayAuthTask authTask;

    public interface AuthCheckListener {
        void onAuthCheckResult(boolean z, String str, String str2);
    }

    public interface AuthListener {
        void onAuthFailure();

        void onAuthQrGenerated(String str);

        void onAuthSuccess(String str);
    }

    public static void authCheck(final AuthCheckListener listener) {
        if (authCheckTask != null) {
            authCheckTask.cancel(true);
        }
        authCheckTask = new AlipayAuthCheckTask() {
            /* access modifiers changed from: protected */
            public void onPostExecute(AlipayAuthCheckTask.AlipayAuthCheckResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    listener.onAuthCheckResult(result.auth, result.alipayId, result.account);
                }
            }
        };
        authCheckTask.executeOnExecutor(new Executor() {
            public void execute(@NonNull Runnable command) {
                new Thread(command).start();
            }
        }, new String[0]);
    }

    public static AlipayAuthTask doAuth(String alipayId, final AuthListener listener) {
        if (authTask != null) {
            authTask.cancel(true);
        }
        authTask = new AlipayAuthTask();
        authTask.setAlipayUserId(alipayId);
        authTask.setListener(new AlipayAuthTask.AlipayAuthTaskListener() {
            public void onReceivedAlipayAuthStateNotify(AlipayAuthTask.AlipayAuthTaskResult result) {
                String qrCode = null;
                if (result.getStep() == AlipayAuthTask.STEP.GEN_QRCODE) {
                    if (listener != null) {
                        if (result.getQrResult() != null) {
                            qrCode = result.getQrResult().qrCode;
                        }
                        listener.onAuthQrGenerated(qrCode);
                    }
                } else if (result.getStep() == AlipayAuthTask.STEP.TOKEN_GET && listener != null) {
                    if (result.getTokenResult() instanceof AlipayTokenResult) {
                        listener.onAuthSuccess(result.getTokenResult().token);
                    } else {
                        listener.onAuthSuccess((String) null);
                    }
                }
            }
        });
        authTask.executeOnExecutor(new Executor() {
            public void execute(@NonNull Runnable command) {
                new Thread(command).start();
            }
        }, new Object[0]);
        return authTask;
    }

    public static void releaseContract() {
        ReleaseContractRequest releaseContractRequest = new ReleaseContractRequest();
        releaseContractRequest.setNeedUmt(true);
        MtopHelper.asycSendRequest(releaseContractRequest, (MtopHelper.MtopListener) null);
    }

    public static void dispose() {
        if (authTask != null) {
            authTask.setListener((AlipayAuthTask.AlipayAuthTaskListener) null);
            authTask.cancel(true);
        }
        authTask = null;
        if (authCheckTask != null) {
            authCheckTask.cancel(true);
        }
        authCheckTask = null;
    }
}
