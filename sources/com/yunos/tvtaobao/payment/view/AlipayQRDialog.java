package com.yunos.tvtaobao.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.payment.R;
import com.yunos.tvtaobao.payment.analytics.Utils;
import com.yunos.tvtaobao.payment.qrcode.QRCodeManager;
import com.yunos.tvtaobao.payment.utils.UtilsDistance;
import com.zhiping.tvtao.payment.alipay.task.AlipayAuthTask;
import com.zhiping.tvtao.payment.alipay.task.AlipayQRResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlipayQRDialog extends Dialog implements AlipayAuthTask.AlipayAuthTaskListener {
    private static final long EXPIRE_TIME = 180000;
    private String TAG;
    /* access modifiers changed from: private */
    public String alipayUserId;
    private AlipayAuthTask authTask;
    private QRDialogDelegate mDelegate;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public TextView mQRTitle1;
    /* access modifiers changed from: private */
    public TextView mQRTitle2;
    /* access modifiers changed from: private */
    public TextView messageTv1;
    /* access modifiers changed from: private */
    public double price;
    /* access modifiers changed from: private */
    public ImageView qrCodeImageView;
    /* access modifiers changed from: private */
    public String taobaoOrderId;
    private long timeMillis;
    private int type;

    public interface QRDialogDelegate {
        void QRDialogSuccess(AlipayQRDialog alipayQRDialog, boolean z);
    }

    public void setDelegate(QRDialogDelegate delegate) {
        this.mDelegate = delegate;
    }

    public AlipayQRDialog(Context context) {
        super(context);
        this.TAG = "QRDialog";
        this.timeMillis = 0;
        this.type = 0;
    }

    public AlipayQRDialog(Context context, int theme) {
        super(context, theme);
        this.TAG = "QRDialog";
        this.timeMillis = 0;
        this.type = 0;
        this.mHandler = new Handler();
    }

    public AlipayQRDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.TAG = "QRDialog";
        this.timeMillis = 0;
        this.type = 0;
        this.mHandler = new Handler();
    }

    public void show() {
        try {
            super.show();
            Utils.utCustomHit("Expore_Pay_Authorization", Utils.getProperties());
            this.timeMillis = System.currentTimeMillis();
            generateQRCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQRCode() {
        if (this.authTask != null) {
            this.authTask.cancel(true);
        }
        this.authTask = new AlipayAuthTask();
        this.authTask.setAlipayUserId(this.alipayUserId);
        this.authTask.setListener(this);
        this.authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Utils.utCustomHit("Expore_Pay_Authorization", Utils.getProperties());
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDelegate = null;
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
    public SpannableStringBuilder spanAlipay(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("【支付宝】").matcher(src);
        while (m.find()) {
            style.setSpan(new ForegroundColorSpan(-16736023), m.start(), m.end(), 18);
        }
        return style;
    }

    private boolean isExpire() {
        if (System.currentTimeMillis() - this.timeMillis > EXPIRE_TIME) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        try {
            super.dismiss();
            if (this.authTask != null) {
                this.authTask.cancel(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.authTask = null;
    }

    public void onReceivedAlipayAuthStateNotify(AlipayAuthTask.AlipayAuthTaskResult result) {
        boolean z;
        boolean z2 = true;
        boolean z3 = result.getStep() == AlipayAuthTask.STEP.GEN_QRCODE;
        if (result.getStatus() == AlipayAuthTask.STATUS.SUCCESS) {
            z = true;
        } else {
            z = false;
        }
        if (z3 && z) {
            try {
                this.qrCodeImageView.setImageBitmap(QRCodeManager.create2DCode(result.getQrResult() instanceof AlipayQRResult ? result.getQrResult().qrCode : null, UtilsDistance.dp2px(getContext(), 324), UtilsDistance.dp2px(getContext(), 324), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.payment_icon_alipay)));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else if (result.getStep() == AlipayAuthTask.STEP.TOKEN_GET && result.getStatus() == AlipayAuthTask.STATUS.SUCCESS) {
            if (this.mDelegate != null) {
                QRDialogDelegate qRDialogDelegate = this.mDelegate;
                if (result.getStatus() != AlipayAuthTask.STATUS.SUCCESS) {
                    z2 = false;
                }
                qRDialogDelegate.QRDialogSuccess(this, z2);
            }
            dismiss();
        }
    }

    public static class Builder {
        private String alipayUserId;
        private Context context;
        private double orderPrice;
        private String taobaoOrderId;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setAlipayUserId(String userId) {
            this.alipayUserId = userId;
            return this;
        }

        public Builder setTaobaoOrderId(String orderId) {
            this.taobaoOrderId = orderId;
            return this;
        }

        public Builder setOrderPrice(double price) {
            this.orderPrice = price;
            return this;
        }

        public AlipayQRDialog create() {
            AlipayQRDialog dialog = new AlipayQRDialog(this.context, R.style.payment_QRdialog);
            dialog.setContentView(R.layout.payment_dialog_alipayqrcode);
            TextView unused = dialog.mQRTitle1 = (TextView) dialog.findViewById(R.id.tv_qrcode_title_1);
            TextView unused2 = dialog.mQRTitle2 = (TextView) dialog.findViewById(R.id.tv_qrcode_title_2);
            ImageView unused3 = dialog.qrCodeImageView = (ImageView) dialog.findViewById(R.id.qrcode_image);
            TextView unused4 = dialog.messageTv1 = (TextView) dialog.findViewById(R.id.message1);
            String price = String.format("订单合计 ¥ %.0f 元", new Object[]{Double.valueOf(this.orderPrice)});
            if (this.orderPrice - ((double) ((int) this.orderPrice)) > ClientTraceData.b.f47a) {
                price = String.format("订单合计 ¥ %.2f 元", new Object[]{Double.valueOf(this.orderPrice)});
            }
            dialog.mQRTitle1.setText(dialog.spanPrice(price));
            dialog.mQRTitle2.setText(dialog.spanAlipay("请使用【支付宝】完成付款"));
            String unused5 = dialog.alipayUserId = this.alipayUserId;
            String unused6 = dialog.taobaoOrderId = this.taobaoOrderId;
            double unused7 = dialog.price = this.orderPrice;
            dialog.setCancelable(false);
            return dialog;
        }
    }
}
