package com.zhiping.tvtao.payment.alipay;

import android.content.Context;
import android.support.annotation.NonNull;
import com.zhiping.tvtao.payment.alipay.request.GetOrderDetailRequest;
import com.zhiping.tvtao.payment.alipay.request.base.BaseMtopRequest;
import com.zhiping.tvtao.payment.alipay.request.base.MtopResponse;
import com.zhiping.tvtao.payment.alipay.task.AgreementPayTask;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import java.util.concurrent.Executor;

public class AlipayPaymentManager {

    public interface AlipayAgreementPayListener {
        boolean handleAuth(AgreementPayTask agreementPayTask, String str);

        void paymentFailure(String str);

        void paymentSuccess(double d, String str);
    }

    public interface OrderStatusRequestListener {
        void onRequestFail(String str, String str2);

        void onRequestSuccess(String str, String str2);
    }

    public interface SimpleAgreementPayListener {
        void paymentFailure(String str);

        void paymentSuccess();
    }

    public static AgreementPayTask doPay(String taobaoOrderNo, String alipayUserId, final SimpleAgreementPayListener listener) {
        AgreementPayTask payTask = new AgreementPayTask();
        payTask.setBizOrderId(taobaoOrderNo);
        payTask.setCheckDepositStatus(false);
        payTask.setBuyerId(alipayUserId);
        payTask.setListener(new AgreementPayTask.AgreementPayListener() {
            public void onPayMentSuccess(AgreementPayTask task, String alipayAccount) {
                if (listener != null) {
                    listener.paymentSuccess();
                }
            }

            public void onPayMentFailure(AgreementPayTask task, String errorMsg) {
                if (listener != null) {
                    listener.paymentFailure(errorMsg);
                }
            }

            public void onNeedAuth(AgreementPayTask task, String userId) {
                if (listener != null) {
                    listener.paymentFailure("");
                }
            }
        });
        payTask.executeOnExecutor(new Executor() {
            public void execute(@NonNull Runnable command) {
                new Thread(command).start();
            }
        }, new Object[0]);
        return payTask;
    }

    public static AgreementPayTask doPay(Context context, final double price, String alipayUserId, String taobaoOrderNo, boolean fromCart, boolean prePay, final AlipayAgreementPayListener listener) {
        AgreementPayTask payTask = new AgreementPayTask();
        payTask.setBizOrderId(taobaoOrderNo);
        payTask.setCheckDepositStatus(!fromCart || prePay);
        payTask.setBuyerId(alipayUserId);
        payTask.setListener(new AgreementPayTask.AgreementPayListener() {
            public void onPayMentSuccess(AgreementPayTask task, String alipayAccount) {
                if (listener != null) {
                    listener.paymentSuccess(price, alipayAccount);
                }
            }

            public void onPayMentFailure(AgreementPayTask task, String errorMsg) {
                if (listener != null) {
                    listener.paymentFailure(errorMsg);
                }
            }

            public void onNeedAuth(AgreementPayTask task, String userId) {
                if (listener == null) {
                    task.stop();
                } else if (!listener.handleAuth(task, userId)) {
                    task.stop();
                }
            }
        });
        payTask.executeOnExecutor(new Executor() {
            public void execute(@NonNull Runnable command) {
                new Thread(command).start();
            }
        }, new Object[0]);
        return payTask;
    }

    public static void getOrderStatus(final String orderId, final OrderStatusRequestListener listener) {
        MtopHelper.asycSendRequest(new GetOrderDetailRequest(orderId), new MtopHelper.MtopListener() {
            public void onFinish(BaseMtopRequest request, MtopResponse response) {
                if (response.isSuccess()) {
                    String statusCode = GetOrderDetailRequest.getResponseStatus(response.getJsonData());
                    if (listener != null) {
                        listener.onRequestSuccess(orderId, statusCode);
                    }
                } else if (listener != null) {
                    listener.onRequestFail(orderId, response.getErrorMsg());
                }
            }
        });
    }

    public static void dispose() {
    }
}
