package com.yunos.tvtaobao.biz.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.dialog.PayConfirmDialog;
import com.yunos.tvtaobao.biz.dialog.util.SnapshotUtil;
import com.yunos.tvtaobao.biz.listener.BizRequestListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.OrderDetailMO;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRDialog extends Dialog {
    private static final long EXPIRE_TIME = 180000;
    /* access modifiers changed from: private */
    public String TAG;
    private ArrayList<String> bizOrderIds;
    /* access modifiers changed from: private */
    public volatile int currentID;
    /* access modifiers changed from: private */
    public Activity mActivityContext;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    private QRDialogDelegate mDelegate;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public RelativeLayout mOutermostLayout;
    /* access modifiers changed from: private */
    public boolean prePay;
    private Runnable queryCurrentRunnable;
    private Runnable queryNextRunnable;
    /* access modifiers changed from: private */
    public OrderDetailMO[] results;
    private SnapshotUtil.OnFronstedGlassSreenDoneListener screenShotListener;
    private long timeMillis;

    public interface QRDialogDelegate {
        void QRDialogSuccess(QRDialog qRDialog, boolean z);
    }

    static /* synthetic */ int access$108(QRDialog x0) {
        int i = x0.currentID;
        x0.currentID = i + 1;
        return i;
    }

    public void setDelegate(QRDialogDelegate delegate) {
        this.mDelegate = delegate;
    }

    public QRDialog(Context context) {
        super(context);
        this.TAG = "STJumpcode";
        this.currentID = 0;
        this.timeMillis = 0;
        this.prePay = false;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                QRDialog.this.queryCurrent();
            }
        };
        this.queryNextRunnable = new Runnable() {
            public void run() {
                QRDialog.access$108(QRDialog.this);
                QRDialog.this.queryCurrent();
            }
        };
        this.screenShotListener = new SnapshotUtil.OnFronstedGlassSreenDoneListener() {
            public void onFronstedGlassSreenDone(Bitmap bmp) {
                ZpLogger.v(QRDialog.this.TAG, QRDialog.this.TAG + ".onFronstedGlassSreenDone.bmp = " + bmp);
                Bitmap unused = QRDialog.this.mBitmap = bmp;
                if (QRDialog.this.mOutermostLayout == null) {
                    return;
                }
                if (QRDialog.this.mBitmap == null || QRDialog.this.mBitmap.isRecycled()) {
                    QRDialog.this.mOutermostLayout.setBackgroundColor(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_80));
                    return;
                }
                QRDialog.this.mOutermostLayout.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(QRDialog.this.mBitmap), new ColorDrawable(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_50))}));
            }
        };
    }

    public QRDialog(Context context, int theme) {
        super(context, theme);
        this.TAG = "STJumpcode";
        this.currentID = 0;
        this.timeMillis = 0;
        this.prePay = false;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                QRDialog.this.queryCurrent();
            }
        };
        this.queryNextRunnable = new Runnable() {
            public void run() {
                QRDialog.access$108(QRDialog.this);
                QRDialog.this.queryCurrent();
            }
        };
        this.screenShotListener = new SnapshotUtil.OnFronstedGlassSreenDoneListener() {
            public void onFronstedGlassSreenDone(Bitmap bmp) {
                ZpLogger.v(QRDialog.this.TAG, QRDialog.this.TAG + ".onFronstedGlassSreenDone.bmp = " + bmp);
                Bitmap unused = QRDialog.this.mBitmap = bmp;
                if (QRDialog.this.mOutermostLayout == null) {
                    return;
                }
                if (QRDialog.this.mBitmap == null || QRDialog.this.mBitmap.isRecycled()) {
                    QRDialog.this.mOutermostLayout.setBackgroundColor(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_80));
                    return;
                }
                QRDialog.this.mOutermostLayout.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(QRDialog.this.mBitmap), new ColorDrawable(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_50))}));
            }
        };
        this.mHandler = new Handler();
    }

    public QRDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.TAG = "STJumpcode";
        this.currentID = 0;
        this.timeMillis = 0;
        this.prePay = false;
        this.queryCurrentRunnable = new Runnable() {
            public void run() {
                QRDialog.this.queryCurrent();
            }
        };
        this.queryNextRunnable = new Runnable() {
            public void run() {
                QRDialog.access$108(QRDialog.this);
                QRDialog.this.queryCurrent();
            }
        };
        this.screenShotListener = new SnapshotUtil.OnFronstedGlassSreenDoneListener() {
            public void onFronstedGlassSreenDone(Bitmap bmp) {
                ZpLogger.v(QRDialog.this.TAG, QRDialog.this.TAG + ".onFronstedGlassSreenDone.bmp = " + bmp);
                Bitmap unused = QRDialog.this.mBitmap = bmp;
                if (QRDialog.this.mOutermostLayout == null) {
                    return;
                }
                if (QRDialog.this.mBitmap == null || QRDialog.this.mBitmap.isRecycled()) {
                    QRDialog.this.mOutermostLayout.setBackgroundColor(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_80));
                    return;
                }
                QRDialog.this.mOutermostLayout.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(QRDialog.this.mBitmap), new ColorDrawable(QRDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_50))}));
            }
        };
        this.mHandler = new Handler();
    }

    public void setBizOrderIds(ArrayList<String> bizOrderIds2) {
        this.bizOrderIds = bizOrderIds2;
    }

    public void setPrePay(boolean prePay2) {
        this.prePay = prePay2;
    }

    public void show() {
        if (this.mOutermostLayout != null) {
            this.mOutermostLayout.setBackgroundDrawable((Drawable) null);
        }
        super.show();
        this.timeMillis = System.currentTimeMillis();
        if (this.bizOrderIds != null && this.bizOrderIds.size() > 0) {
            this.results = new OrderDetailMO[this.bizOrderIds.size()];
            queryIDs();
        }
        if (this.mActivityContext != null) {
            SnapshotUtil.getFronstedSreenShot(new WeakReference(this.mActivityContext), 5, 0.0f, this.screenShotListener);
        }
        Map<String, String> trackMap = Utils.getProperties();
        trackMap.put(SPMConfig.SPM, SPMConfig.PAGE_MASHANGTAO_EXPOSE);
        Utils.utExposeHit("", "Page_STJumpcode", trackMap);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Utils.utPageAppear(this.TAG, this.TAG);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: private */
    public void queryCurrent() {
        if (this.bizOrderIds != null && this.currentID >= 0 && this.currentID < this.bizOrderIds.size()) {
            BusinessRequest.getBusinessRequest().requestOrderDetail(Long.valueOf(Long.parseLong(this.bizOrderIds.get(this.currentID))), new BizRequestListener<OrderDetailMO>(new WeakReference((BaseActivity) this.mActivityContext)) {
                public boolean onError(int resultCode, String msg) {
                    QRDialog.this.queryAgain();
                    return true;
                }

                public void onSuccess(OrderDetailMO data) {
                    QRDialog.this.results[QRDialog.this.currentID] = data;
                    if ((!"BUYER_PAYED_DEPOSIT".equals(data.getOrderInfo().getOrderStatusCode()) || QRDialog.this.prePay) && !"WAIT_BUYER_PAY".equals(data.getOrderInfo().getOrderStatusCode())) {
                        QRDialog.this.queryNext();
                    } else {
                        QRDialog.this.queryAgain();
                    }
                }

                public boolean ifFinishWhenCloseErrorDialog() {
                    return false;
                }
            });
        }
    }

    private void queryIDs() {
        this.results = new OrderDetailMO[this.bizOrderIds.size()];
        this.currentID = 0;
        queryCurrent();
    }

    /* access modifiers changed from: private */
    public void queryAgain() {
        if (!isExpire()) {
            this.mHandler.removeCallbacks(this.queryCurrentRunnable);
            this.mHandler.removeCallbacks(this.queryNextRunnable);
            this.mHandler.postDelayed(this.queryCurrentRunnable, 5000);
        }
    }

    /* access modifiers changed from: private */
    public void queryNext() {
        String sumText;
        if (!isExpire()) {
            this.mHandler.removeCallbacks(this.queryNextRunnable);
            this.mHandler.removeCallbacks(this.queryCurrentRunnable);
            boolean allQueried = this.currentID == this.bizOrderIds.size() + -1;
            if (allQueried && this.results.length > 0 && this.results[this.results.length - 1] != null && "WAIT_BUYER_PAY".equals(this.results[this.results.length - 1].getOrderInfo().getOrderStatusCode())) {
                allQueried = false;
            }
            double sum = ClientTraceData.b.f47a;
            if (allQueried) {
                OrderDetailMO[] orderDetailMOArr = this.results;
                int length = orderDetailMOArr.length;
                int i = 0;
                while (i < length) {
                    OrderDetailMO mo = orderDetailMOArr[i];
                    if (checkPaid(mo.getOrderInfo().getOrderStatusCode())) {
                        sum += Double.parseDouble(mo.getOrderInfo().getTotalPrice());
                        i++;
                    } else {
                        return;
                    }
                }
                final QRDialogDelegate delegate = this.mDelegate;
                if (sum == ((double) ((int) sum))) {
                    sumText = String.format("成功付款 %.0f 元", new Object[]{Double.valueOf(sum)});
                } else {
                    sumText = String.format("成功付款 %.2f 元", new Object[]{Double.valueOf(sum)});
                }
                new PayConfirmDialog.Builder(this.mActivityContext).setCancelable(false).setMessage((CharSequence) onHandlerSpanned(sumText)).setPositiveButton("按OK键完成", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (delegate != null) {
                            delegate.QRDialogSuccess(QRDialog.this, true);
                        }
                        dialogInterface.dismiss();
                    }
                }).create().show();
                dismiss();
                return;
            }
            this.mHandler.postDelayed(this.queryNextRunnable, 5000);
        }
    }

    private SpannableStringBuilder onHandlerSpanned(String src) {
        SpannableStringBuilder style = new SpannableStringBuilder(src);
        Matcher m = Pattern.compile("\\d+(.)*\\d*").matcher(src);
        while (m.find()) {
            int start = m.start();
            if (start > 0 && "-".equals(src.substring(start - 1, start))) {
                start--;
            }
            style.setSpan(new ForegroundColorSpan(-37873), start, m.end(), 34);
        }
        return style;
    }

    private boolean checkPaid(String statusCode) {
        if ("TRADE_FINISHED".equals(statusCode) || "WAIT_SELLER_SEND_GOODS".equals(statusCode)) {
            return true;
        }
        if ((!"BUYER_PAYED_DEPOSIT".equals(statusCode) || this.bizOrderIds == null || this.bizOrderIds.size() != 1 || !this.prePay) && !"WAIT_BUYER_CONFIRM_GOODS".equals(statusCode)) {
            return false;
        }
        return true;
    }

    private boolean isExpire() {
        if (System.currentTimeMillis() - this.timeMillis > EXPIRE_TIME) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        super.dismiss();
        Utils.utUpdatePageProperties(this.TAG, Utils.getProperties());
        Utils.utPageDisAppear(this.TAG);
        this.mHandler.removeCallbacks(this.queryCurrentRunnable);
        this.mHandler.removeCallbacks(this.queryNextRunnable);
        if (this.mOutermostLayout != null) {
            this.mOutermostLayout.setBackgroundDrawable((Drawable) null);
        }
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
        this.mActivityContext = null;
        this.mOutermostLayout = null;
    }

    public static class Builder {
        private boolean cancelable = true;
        private Context context;
        private Bitmap mBitmap;
        private CharSequence mText;
        private CharSequence mTitle;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setCancelable(boolean cancelable2) {
            this.cancelable = cancelable2;
            return this;
        }

        public Builder setQRCodeText(CharSequence text) {
            this.mText = text;
            return this;
        }

        public Builder setQrCodeBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public QRDialog create() {
            QRDialog dialog = new QRDialog(this.context, R.style.ytbv_QR_Dialog);
            dialog.setContentView(R.layout.ytm_activity_buildorder_qrdialog);
            RelativeLayout unused = dialog.mOutermostLayout = (RelativeLayout) dialog.findViewById(R.id.qrcode_layout);
            if (this.context instanceof Activity) {
                Activity unused2 = dialog.mActivityContext = (Activity) this.context;
            }
            TextView titleView = (TextView) dialog.findViewById(R.id.title);
            if (titleView != null && !TextUtils.isEmpty(this.mTitle)) {
                titleView.setText(this.mTitle);
            }
            TextView textView = (TextView) dialog.findViewById(R.id.qrcode_text);
            if (textView != null && !TextUtils.isEmpty(this.mText)) {
                textView.setText(this.mText);
            }
            ImageView imageView = (ImageView) dialog.findViewById(R.id.qrcode_image);
            if (!(imageView == null || this.mBitmap == null)) {
                imageView.setImageBitmap(this.mBitmap);
            }
            dialog.setCancelable(this.cancelable);
            return dialog;
        }
    }
}
