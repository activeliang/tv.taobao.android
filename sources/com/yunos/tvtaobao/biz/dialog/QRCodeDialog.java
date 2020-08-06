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
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tvtaobao.biz.dialog.util.SnapshotUtil;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class QRCodeDialog extends Dialog {
    /* access modifiers changed from: private */
    public String TAG = "QRCodeDialog";
    /* access modifiers changed from: private */
    public Activity mActivityContext;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    /* access modifiers changed from: private */
    public RelativeLayout mOutermostLayout;
    /* access modifiers changed from: private */
    public ImageView qrcode_active_image;
    private SnapshotUtil.OnFronstedGlassSreenDoneListener screenShotListener = new SnapshotUtil.OnFronstedGlassSreenDoneListener() {
        public void onFronstedGlassSreenDone(Bitmap bmp) {
            ZpLogger.v(QRCodeDialog.this.TAG, QRCodeDialog.this.TAG + ".onFronstedGlassSreenDone.bmp = " + bmp);
            Bitmap unused = QRCodeDialog.this.mBitmap = bmp;
            if (QRCodeDialog.this.mOutermostLayout == null) {
                return;
            }
            if (QRCodeDialog.this.mBitmap == null || QRCodeDialog.this.mBitmap.isRecycled()) {
                QRCodeDialog.this.mOutermostLayout.setBackgroundColor(QRCodeDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_80));
                return;
            }
            QRCodeDialog.this.mOutermostLayout.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(QRCodeDialog.this.mBitmap), new ColorDrawable(QRCodeDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_50))}));
        }
    };

    public QRCodeDialog(Context context) {
        super(context);
    }

    public QRCodeDialog(Context context, int theme) {
        super(context, theme);
    }

    public QRCodeDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void show() {
        if (this.mOutermostLayout != null) {
            this.mOutermostLayout.setBackgroundDrawable((Drawable) null);
        }
        super.show();
        if (this.mActivityContext != null) {
            SnapshotUtil.getFronstedSreenShot(new WeakReference(this.mActivityContext), 5, 0.0f, this.screenShotListener);
        }
    }

    public void dismiss() {
        super.dismiss();
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
        private String mText;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setCancelable(boolean cancelable2) {
            this.cancelable = cancelable2;
            return this;
        }

        public Builder setQRCodeText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setQrCodeBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
            return this;
        }

        public QRCodeDialog create() {
            QRCodeDialog dialog = new QRCodeDialog(this.context, R.style.ytbv_QR_Dialog);
            dialog.setContentView(R.layout.ytbv_qrcode_layout_new);
            RelativeLayout unused = dialog.mOutermostLayout = (RelativeLayout) dialog.findViewById(R.id.qrcode_layout);
            ImageView unused2 = dialog.qrcode_active_image = (ImageView) dialog.findViewById(R.id.qrcode_active_image);
            if (this.context instanceof Activity) {
                Activity unused3 = dialog.mActivityContext = (Activity) this.context;
            }
            TextView textView = (TextView) dialog.findViewById(R.id.qrcode_text);
            if (textView != null && !TextUtils.isEmpty(this.mText)) {
                textView.setText(this.mText);
            }
            ImageView imageView = (ImageView) dialog.findViewById(R.id.qrcode_image);
            if (!(imageView == null || this.mBitmap == null)) {
                imageView.setImageBitmap(this.mBitmap);
            }
            if (!StringUtil.isEmpty((String) RtEnv.get(RtEnv.MASHANGTAO_ICON, ""))) {
                ImageLoaderManager.get().displayImage((String) RtEnv.get(RtEnv.MASHANGTAO_ICON, ""), dialog.qrcode_active_image);
            }
            dialog.setCancelable(this.cancelable);
            return dialog;
        }
    }
}
