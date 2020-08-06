package com.zhiping.tvtao.payment;

import android.content.Context;
import com.zhiping.tvtao.payment.alipay.AlipayAuthManager;
import com.zhiping.tvtao.payment.alipay.AlipayPaymentManager;
import com.zhiping.tvtao.payment.alipay.task.AgreementPayTask;
import com.zhiping.tvtao.payment.alipay.task.AlipayAuthTask;
import com.zhiping.tvtao.payment.utils.MtopHelper;
import java.util.Map;

public class AlipayManager {
    private static BizInfoProvider bizInfoProvider;
    private static Context sContext;

    public interface BizInfoProvider {
        Map<String, String> extraParamsForMtop(String str, String str2);

        String getDeviceId();
    }

    public interface CancelableTask {
        void pause();

        void resume();

        void stop();
    }

    public static BizInfoProvider getBizInfoProvider() {
        return bizInfoProvider;
    }

    public static void init(Context context, BizInfoProvider provider, boolean isThird) {
        bizInfoProvider = provider;
        MtopHelper.init(context, isThird);
        if (context != null) {
            sContext = context.getApplicationContext();
        }
    }

    public static void init(Context context, boolean isThird) {
        init(context, (BizInfoProvider) null, isThird);
    }

    public static AgreementPayTask doPay(String orderId, String alipayuserId, AlipayPaymentManager.SimpleAgreementPayListener listener) {
        return AlipayPaymentManager.doPay(orderId, alipayuserId, listener);
    }

    public static AgreementPayTask doPay(Context context, double price, String alipayUserId, String taobaoOrderNo, boolean fromCart, boolean prePay, AlipayPaymentManager.AlipayAgreementPayListener listener) {
        return AlipayPaymentManager.doPay(context, price, alipayUserId, taobaoOrderNo, fromCart, prePay, listener);
    }

    public static void getOrderStatus(String orderId, AlipayPaymentManager.OrderStatusRequestListener listener) {
        AlipayPaymentManager.getOrderStatus(orderId, listener);
    }

    public static void authCheck(AlipayAuthManager.AuthCheckListener listener) {
        AlipayAuthManager.authCheck(listener);
    }

    public static AlipayAuthTask doAuth(String alipayId, AlipayAuthManager.AuthListener listener) {
        return AlipayAuthManager.doAuth(alipayId, listener);
    }

    public static void releaseContract() {
        AlipayAuthManager.releaseContract();
    }

    public static void dispose() {
        AlipayAuthManager.dispose();
        AlipayPaymentManager.dispose();
        MtopHelper.dispose();
    }
}
