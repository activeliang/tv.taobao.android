package com.yunos.tvtaobao.biz.h5.plugin;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import com.google.zxing.WriterException;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.core.helpers.YunOSOrderManager;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.activity.TaoBaoBlitzActivity;
import com.yunos.tvtaobao.biz.dialog.QRDialog;
import com.yunos.tvtaobao.biz.dialog.QuitPayConfirmDialog;
import com.yunos.tvtaobao.biz.qrcode.QRCodeManager;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.payment.utils.AlipayAuthUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.alipay.AlipayPaymentManager;
import com.zhiping.tvtao.payment.alipay.task.AgreementPayTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class TvTaoBaoPayPlugin {
    private static String ALIPAYTRADENO = "trade_no";
    private static String ERROR = "error";
    private static String FROMCART = "fromCart";
    private static String ITEMID = "item_id";
    private static String KEY = "key";
    private static String PAYTYPE = "payType";
    private static String PREPAY = "prePay";
    private static String PRICE = "price";
    /* access modifiers changed from: private */
    public static String RESULT = "result";
    /* access modifiers changed from: private */
    public static String TAG = "TvTaoBaoPayPlugin";
    private static String TAOBAOORDERNO = "order_no";
    private static String TITLE = "title";
    private static String VALUE = "value";
    private static String WAIMAI_TAOBAO_PAY = "waimaiTaobaoPay";
    private PayJsCallback mPayJsCallback;
    /* access modifiers changed from: private */
    public WeakReference<TaoBaoBlitzActivity> mTaoBaoBlitzActivityReference;

    public TvTaoBaoPayPlugin(WeakReference<TaoBaoBlitzActivity> taoBaoBlitzActivity) {
        this.mTaoBaoBlitzActivityReference = taoBaoBlitzActivity;
        onInitPayPlugin();
    }

    private void onInitPayPlugin() {
        this.mPayJsCallback = new PayJsCallback(new WeakReference(this));
        BlitzPlugin.bindingJs("tvtaobao_pay", this.mPayJsCallback);
    }

    /* access modifiers changed from: private */
    public boolean onHandleCallPay(String param, long cbData) {
        final String param_final = param;
        final long cbData_final = cbData;
        ZpLogger.i(TAG, "onHandleCallPay --> param_final  =" + param_final + ";  cbData_final = " + cbData_final);
        TaoBaoBlitzActivity taoBaoBlitzActivity = null;
        if (!(this.mTaoBaoBlitzActivityReference == null || this.mTaoBaoBlitzActivityReference.get() == null)) {
            taoBaoBlitzActivity = (TaoBaoBlitzActivity) this.mTaoBaoBlitzActivityReference.get();
        }
        if (taoBaoBlitzActivity == null) {
            BzResult result = new BzResult();
            result.addData(RESULT, "false");
            BlitzPlugin.responseJs(false, result.toJsonString(), cbData_final);
        } else {
            taoBaoBlitzActivity.runOnUiThread(new Runnable() {
                public void run() {
                    TvTaoBaoPayPlugin.this.onHandleTvPay(param_final, cbData_final);
                }
            });
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void onHandleTvPay(String param, long cbData) {
        long cbData_addr = cbData;
        ZpLogger.i(TAG, "onHandleTvPay --> param  =" + param + ";  cbData_addr = " + cbData_addr);
        try {
            JSONObject jSONObject = new JSONObject(param);
            String title = jSONObject.optString(TITLE);
            String price = jSONObject.optString(PRICE);
            String alipayTradeNo = jSONObject.optString(ALIPAYTRADENO);
            String taobaoOrderNo = jSONObject.optString(TAOBAOORDERNO);
            String item_id = jSONObject.optString(ITEMID);
            String optString = jSONObject.optString(PAYTYPE, "alipay");
            boolean waimaiTaobaoPay = jSONObject.optBoolean(WAIMAI_TAOBAO_PAY, false);
            boolean prepay = jSONObject.optBoolean(PREPAY, false);
            boolean fromCart = jSONObject.optBoolean(FROMCART, false);
            ZpLogger.i(TAG, "onHandleTvPay --> title  =" + title + ";  price = " + price + "; alipayTradeNo = " + alipayTradeNo + "; taobaoOrderNo = " + taobaoOrderNo);
            String order = "";
            if (!TextUtils.isEmpty(alipayTradeNo)) {
                if (!TextUtils.isEmpty(order)) {
                    order = order + "&";
                }
                order = order + "orderNo=" + alipayTradeNo;
            }
            if (!TextUtils.isEmpty(title)) {
                if (!TextUtils.isEmpty(order)) {
                    order = order + "&";
                }
                order = order + "subject=" + title;
            }
            if (!TextUtils.isEmpty(price)) {
                if (!TextUtils.isEmpty(order)) {
                    order = order + "&";
                }
                order = order + "price=" + price;
            }
            String order2 = order + "&orderType=trade";
            if (!TextUtils.isEmpty(taobaoOrderNo)) {
                if (!TextUtils.isEmpty(order2)) {
                    order2 = order2 + "&";
                }
                order2 = order2 + "taobaoOrderNo=" + taobaoOrderNo;
            }
            ZpLogger.i(TAG, "onHandleTvPay --> order =  " + order2);
            YunOSOrderManager orderManager = new YunOSOrderManager();
            orderManager.GenerateOrder(order2);
            if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().taobaoPay) {
                aliTVPay(orderManager, cbData_addr, price, title, item_id, taobaoOrderNo, fromCart, waimaiTaobaoPay, prepay);
                return;
            }
            showQRDialog(taobaoOrderNo, price, cbData_addr, jSONObject.has(WAIMAI_TAOBAO_PAY), prepay);
        } catch (Exception e) {
            ZpLogger.i(TAG, "onHandleTvPay --> e =  " + e.toString());
        }
    }

    /* access modifiers changed from: private */
    public void showQRDialog(String orderNo, String price, long callbackAddr, boolean isWaimai, boolean prePay) {
        String priceLabel;
        Bitmap qrBitmap = null;
        final BaseActivity activity = (BaseActivity) this.mTaoBaoBlitzActivityReference.get();
        int width = ((TaoBaoBlitzActivity) this.mTaoBaoBlitzActivityReference.get()).getResources().getDimensionPixelSize(R.dimen.dp_334);
        int height = width;
        if (isWaimai) {
            try {
                qrBitmap = QRCodeManager.create2DCode("http://h5.m.taobao.com/app/waimai/orderlist.html", width, height, (Bitmap) null);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            qrBitmap = QRCodeManager.create2DCode("http://h5.m.taobao.com/mlapp/olist.html?OrderListType=wait_to_pay", width, height, (Bitmap) null);
        }
        Double realprice = Double.valueOf(Double.parseDouble(price));
        if (realprice.doubleValue() % 100.0d > ClientTraceData.b.f47a) {
            priceLabel = String.format("%.2f", new Object[]{Double.valueOf(realprice.doubleValue() / 100.0d)});
        } else {
            priceLabel = String.format("%.0f", new Object[]{Double.valueOf(realprice.doubleValue() / 100.0d)});
        }
        final QRDialog dialog = new QRDialog.Builder(activity).setCancelable(true).setQrCodeBitmap(qrBitmap).setTitle(createSpannableTitle("打开【手机淘宝】扫码付款")).setQRCodeText(onHandlerSpanned("订单合计" + priceLabel + "元")).create();
        dialog.setPrePay(prePay);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(orderNo);
        dialog.setBizOrderIds(ids);
        final long j = callbackAddr;
        dialog.setDelegate(new QRDialog.QRDialogDelegate() {
            public void QRDialogSuccess(QRDialog dialog, boolean success) {
                BlitzPlugin.responseJs(success, "{\"result\":\"true\",\"ret\":\"HY_SUCCESS\"}", j);
            }
        });
        final long j2 = callbackAddr;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() != 4) {
                    return false;
                }
                if (keyEvent.getAction() == 0) {
                    Utils.utCustomHit("Expose_STJumpcode_pop_out_payment", Utils.getProperties());
                    new QuitPayConfirmDialog.Builder(activity).setTitle("退出支付").setMessage("支付未完成,是否确认退出?").setPositiveButton("确定退出", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.utControlHit("STJumpcode_pop_out_payment", "button_out", Utils.getProperties());
                            dialogInterface.dismiss();
                            dialog.dismiss();
                            BlitzPlugin.responseJs(true, "{\"result\":\"false\",\"ret\":\"HY_FAILED\"}", j2);
                        }
                    }).setNegativeButton("继续支付", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.utControlHit("STJumpcode_pop_out_payment", "button_cancel", Utils.getProperties());
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }
                return true;
            }
        });
        dialog.show();
    }

    private SpannableStringBuilder createSpannableTitle(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("【手机淘宝】").matcher(src);
        while (m.find()) {
            style.setSpan(new ForegroundColorSpan(-37873), m.start(), m.end(), 34);
        }
        return style;
    }

    private SpannableStringBuilder onHandlerSpanned(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("\\d+(.)*\\d+").matcher(src);
        while (m.find()) {
            int start = m.start();
            if (start > 0 && "-".equals(src.substring(start - 1, start))) {
                start--;
            }
            style.setSpan(new ForegroundColorSpan(-37873), start, m.end(), 34);
        }
        return style;
    }

    private void aliTVPay(YunOSOrderManager orderManager, long cbData_addr, String price, String title, String item_id, String orderId, boolean fromCart, boolean isWaimai, boolean prePay) {
        String order = orderManager.getOrder();
        String sign = orderManager.getSign();
        final double priceVal = Double.valueOf(price).doubleValue() / 100.0d;
        new Bundle().putString("provider", "alipay");
        final BzResult result = new BzResult();
        TaoBaoBlitzActivity taoBaoBlitzActivity = null;
        if (!(this.mTaoBaoBlitzActivityReference == null || this.mTaoBaoBlitzActivityReference.get() == null)) {
            taoBaoBlitzActivity = (TaoBaoBlitzActivity) this.mTaoBaoBlitzActivityReference.get();
        }
        final TaoBaoBlitzActivity mTaoBaoBlitzActivity = taoBaoBlitzActivity;
        if (mTaoBaoBlitzActivity == null) {
            result.addData(RESULT, "false");
            BlitzPlugin.responseJs(false, result.toJsonString(), cbData_addr);
            return;
        }
        mTaoBaoBlitzActivity.OnWaitProgressDialog(true);
        final String str = title;
        final String str2 = item_id;
        final long j = cbData_addr;
        final String str3 = orderId;
        final String str4 = price;
        final boolean z = isWaimai;
        final boolean z2 = prePay;
        AlipayPaymentManager.doPay(mTaoBaoBlitzActivity, priceVal, (String) null, orderId, fromCart, prePay, new AlipayPaymentManager.AlipayAgreementPayListener() {
            public void paymentSuccess(double price, String account) {
                mTaoBaoBlitzActivity.OnWaitProgressDialog(false);
                String controlName = Utils.getControlName(mTaoBaoBlitzActivity.getFullPageName(), "Pay", (Integer) null, new String[0]);
                Map<String, String> lProperties = Utils.getProperties();
                if (!TextUtils.isEmpty(str)) {
                    lProperties.put("title", str);
                }
                if (!TextUtils.isEmpty(str2)) {
                    lProperties.put("item_id", str2);
                }
                lProperties.put("result", BlitzServiceUtils.CSUCCESS);
                result.addData(TvTaoBaoPayPlugin.RESULT, "true");
                result.setSuccess();
                BlitzPlugin.responseJs(true, result.toJsonString(), j);
                Utils.utCustomHit(mTaoBaoBlitzActivity.getFullPageName(), controlName, lProperties);
            }

            public void paymentFailure(String msg) {
                mTaoBaoBlitzActivity.OnWaitProgressDialog(false);
                TvTaoBaoPayPlugin.this.showQRDialog(str3, str4, j, z, z2);
            }

            public boolean handleAuth(AgreementPayTask agreementPayTask, String s) {
                AlipayAuthUtil.doHandleAuth((Context) TvTaoBaoPayPlugin.this.mTaoBaoBlitzActivityReference.get(), agreementPayTask, s, str3, priceVal, new AlipayAuthUtil.ExitPayListener() {
                    public void onSureExit() {
                        AnonymousClass4.this.paymentCancel();
                        Utils.utCustomHit("sure_exit", Utils.getProperties());
                    }

                    public void onResumePay() {
                        Utils.utCustomHit("cancel_exit", Utils.getProperties());
                    }
                });
                return true;
            }

            public void paymentCancel() {
                mTaoBaoBlitzActivity.OnWaitProgressDialog(false);
                String controlName = Utils.getControlName(mTaoBaoBlitzActivity.getFullPageName(), "Pay", (Integer) null, new String[0]);
                Map<String, String> lProperties = Utils.getProperties();
                if (!TextUtils.isEmpty(str)) {
                    lProperties.put("title", str);
                }
                if (!TextUtils.isEmpty(str2)) {
                    lProperties.put("item_id", str2);
                }
                lProperties.put("result", "fail");
                result.addData(TvTaoBaoPayPlugin.RESULT, "false");
                result.setSuccess();
                BlitzPlugin.responseJs(true, result.toJsonString(), j);
                Utils.utCustomHit(mTaoBaoBlitzActivity.getFullPageName(), controlName, lProperties);
            }
        });
    }

    private static class PayJsCallback implements BlitzPlugin.JsCallback {
        private WeakReference<TvTaoBaoPayPlugin> mReference;

        public PayJsCallback(WeakReference<TvTaoBaoPayPlugin> reference) {
            this.mReference = reference;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.i(TvTaoBaoPayPlugin.TAG, "onCall --> param  =" + param + ";  cbData = " + cbData);
            if (this.mReference != null && this.mReference.get() != null) {
                boolean unused = ((TvTaoBaoPayPlugin) this.mReference.get()).onHandleCallPay(param, cbData);
            }
        }
    }
}
