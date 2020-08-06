package com.yunos.tvtaobao.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.payment.R;
import com.yunos.tvtaobao.payment.analytics.Utils;
import com.yunos.tvtaobao.payment.qrcode.QRCodeManager;
import com.zhiping.tvtao.payment.alipay.request.GetOrderDetailRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.common.MtopProgressEvent;
import mtopsdk.mtop.intf.Mtop;

@Deprecated
public class TaobaoPayQRDialog extends Dialog {
    private static final long EXPIRE_TIME = 180000;
    private String TAG;
    /* access modifiers changed from: private */
    public TextView errorTv;
    private QRDialogDelegate mDelegate;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public TextView mQRTitle1;
    /* access modifiers changed from: private */
    public TextView mQRTitle2;
    /* access modifiers changed from: private */
    public TextView messageTv;
    /* access modifiers changed from: private */
    public double price;
    /* access modifiers changed from: private */
    public ImageView qrCodeImageView;
    private Runnable queryCurrentRunnable;
    /* access modifiers changed from: private */
    public String taobaoOrderId;
    private long timeMillis;
    private int type;

    public interface QRDialogDelegate {
        void paymentComplete(TaobaoPayQRDialog taobaoPayQRDialog, boolean z);
    }

    public void setDelegate(QRDialogDelegate delegate) {
        this.mDelegate = delegate;
    }

    public TaobaoPayQRDialog(Context context) {
        super(context);
        this.TAG = "TaobaoQRDialog";
        this.timeMillis = 0;
        this.type = 0;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                TaobaoPayQRDialog.this.queryCurrent();
            }
        };
    }

    public TaobaoPayQRDialog(Context context, int theme) {
        super(context, theme);
        this.TAG = "TaobaoQRDialog";
        this.timeMillis = 0;
        this.type = 0;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                TaobaoPayQRDialog.this.queryCurrent();
            }
        };
        this.mHandler = new Handler();
    }

    public TaobaoPayQRDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.TAG = "TaobaoQRDialog";
        this.timeMillis = 0;
        this.type = 0;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                TaobaoPayQRDialog.this.queryCurrent();
            }
        };
        this.mHandler = new Handler();
    }

    public void show() {
        try {
            super.show();
            Utils.utCustomHit("Expore_Pay_Pay", Utils.getProperties());
            this.timeMillis = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQRCode() {
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.dp_334);
        try {
            this.qrCodeImageView.setImageBitmap(QRCodeManager.create2DCode("http://h5.m.taobao.com/mlapp/olist.html?OrderListType=wait_to_pay", width, width, BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_taobao_qr_small)));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        generateQRCode();
        Utils.utCustomHit("Expore_Pay_QRcode", Utils.getProperties());
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDelegate = null;
    }

    private boolean isExpire() {
        if (System.currentTimeMillis() - this.timeMillis > EXPIRE_TIME) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void queryAgain() {
        if (!isExpire()) {
            this.mHandler.removeCallbacks(this.queryCurrentRunnable);
            this.mHandler.postDelayed(this.queryCurrentRunnable, 5000);
        }
    }

    /* access modifiers changed from: private */
    public void queryCurrent() {
        if (!TextUtils.isEmpty(this.taobaoOrderId)) {
            Mtop.instance(getContext()).build((Object) new GetOrderDetailRequest(this.taobaoOrderId), (String) null).useWua().addListener(new DefaultMtopCallback() {
                public void onDataReceived(MtopProgressEvent event, Object context) {
                    super.onDataReceived(event, context);
                }

                public void onFinished(MtopFinishEvent event, Object context) {
                    super.onFinished(event, context);
                    if (event.getMtopResponse().isApiSuccess()) {
                        String statusCode = GetOrderDetailRequest.getResponseStatus(event.getMtopResponse().getDataJsonObject());
                        if (!"WAIT_BUYER_PAY".equals(statusCode)) {
                            TaobaoPayQRDialog.this.notifyComplete(statusCode);
                            TaobaoPayQRDialog.this.dismiss();
                            return;
                        }
                        TaobaoPayQRDialog.this.queryAgain();
                    }
                }
            }).asyncRequest();
        }
    }

    /* access modifiers changed from: private */
    public void notifyComplete(String statusCode) {
        if (this.mDelegate != null) {
            this.mDelegate.paymentComplete(this, checkPaid(statusCode));
        }
    }

    private boolean checkPaid(String statusCode) {
        if (!"TRADE_FINISHED".equals(statusCode) && !"WAIT_SELLER_SEND_GOODS".equals(statusCode) && !"BUYER_PAYED_DEPOSIT".equals(statusCode) && !"WAIT_BUYER_CONFIRM_GOODS".equals(statusCode)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public SpannableStringBuilder spanPrice(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("¥ \\d+(.)*\\d*").matcher(src);
        while (m.find()) {
            style.setSpan(new ForegroundColorSpan(-43776), m.start(), m.end(), 18);
        }
        return style;
    }

    /* access modifiers changed from: private */
    public SpannableStringBuilder spanTaobao(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("【手机淘宝】").matcher(src);
        while (m.find()) {
            style.setSpan(new ForegroundColorSpan(-43776), m.start(), m.end(), 18);
        }
        return style;
    }

    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == 4) {
            notifyComplete("UserCancel");
            dismiss();
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private Context context;
        private String errordesc;
        private double orderPrice;
        private String taobaoId;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setOrderPrice(double orderPrice2) {
            this.orderPrice = orderPrice2;
            return this;
        }

        public Builder setTaobaoOrderId(String taobaoOrderId) {
            this.taobaoId = taobaoOrderId;
            return this;
        }

        public Builder setErrordesc(String errordesc2) {
            this.errordesc = errordesc2;
            return this;
        }

        public TaobaoPayQRDialog create() {
            TaobaoPayQRDialog dialog = new TaobaoPayQRDialog(this.context, R.style.payment_QRdialog);
            dialog.setContentView(R.layout.payment_dialog_taobaoqrcode);
            TextView unused = dialog.mQRTitle1 = (TextView) dialog.findViewById(R.id.tv_qrcode_title_1);
            TextView unused2 = dialog.mQRTitle2 = (TextView) dialog.findViewById(R.id.tv_qrcode_title_2);
            ImageView unused3 = dialog.qrCodeImageView = (ImageView) dialog.findViewById(R.id.qrcode_image);
            TextView unused4 = dialog.messageTv = (TextView) dialog.findViewById(R.id.message1);
            TextView unused5 = dialog.errorTv = (TextView) dialog.findViewById(R.id.errordesc);
            double unused6 = dialog.price = this.orderPrice;
            String unused7 = dialog.taobaoOrderId = this.taobaoId;
            String price = String.format("订单合计 ¥ %.0f 元", new Object[]{Double.valueOf(this.orderPrice)});
            if (this.orderPrice - ((double) ((int) this.orderPrice)) > ClientTraceData.b.f47a) {
                price = String.format("订单合计 ¥ %.2f 元", new Object[]{Double.valueOf(this.orderPrice)});
            }
            dialog.mQRTitle2.setText(dialog.spanPrice(price));
            dialog.errorTv.setText(this.errordesc);
            dialog.messageTv.setText(dialog.spanTaobao("打开【手机淘宝】扫码付款"));
            dialog.setCancelable(false);
            return dialog;
        }
    }
}
