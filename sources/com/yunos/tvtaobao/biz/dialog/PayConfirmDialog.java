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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.dialog.util.SnapshotUtil;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.StaticFocusDrawable;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class PayConfirmDialog extends Dialog {
    /* access modifiers changed from: private */
    public String TAG = "successpayment";
    /* access modifiers changed from: private */
    public Activity mActivityContext;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    /* access modifiers changed from: private */
    public DialogFocusPositionManager mOutermostLayout;
    private SnapshotUtil.OnFronstedGlassSreenDoneListener screenShotListener = new SnapshotUtil.OnFronstedGlassSreenDoneListener() {
        public void onFronstedGlassSreenDone(Bitmap bmp) {
            ZpLogger.v(PayConfirmDialog.this.TAG, PayConfirmDialog.this.TAG + ".onFronstedGlassSreenDone.bmp = " + bmp);
            Bitmap unused = PayConfirmDialog.this.mBitmap = bmp;
            if (PayConfirmDialog.this.mOutermostLayout == null) {
                return;
            }
            if (PayConfirmDialog.this.mBitmap == null || PayConfirmDialog.this.mBitmap.isRecycled()) {
                PayConfirmDialog.this.mOutermostLayout.setBackgroundColor(PayConfirmDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_80));
                return;
            }
            PayConfirmDialog.this.mOutermostLayout.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(PayConfirmDialog.this.mBitmap), new ColorDrawable(PayConfirmDialog.this.mActivityContext.getResources().getColor(R.color.ytbv_shadow_color_50))}));
        }
    };

    public PayConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public PayConfirmDialog(Context context) {
        super(context);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Utils.utPageAppear(this.TAG, this.TAG);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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
        Utils.utUpdatePageProperties(this.TAG, Utils.getProperties());
        Utils.utPageDisAppear(this.TAG);
        if (this.mOutermostLayout != null) {
            this.mOutermostLayout.setBackgroundDrawable((Drawable) null);
        }
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }
    }

    public static class Builder {
        private boolean cancelable = true;
        private Context context;
        private CharSequence message;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setMessage(CharSequence message2) {
            this.message = message2;
            return this;
        }

        public Builder setMessage(int message2) {
            this.message = (String) this.context.getText(message2);
            return this;
        }

        public Builder setCancelable(boolean cancelable2) {
            this.cancelable = cancelable2;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText2);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public PayConfirmDialog show() {
            PayConfirmDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public PayConfirmDialog create() {
            final PayConfirmDialog dialog = new PayConfirmDialog(this.context, R.style.ytbv_CustomDialog);
            dialog.setContentView(R.layout.dialog_confirmpay);
            DialogFocusPositionManager unused = dialog.mOutermostLayout = (DialogFocusPositionManager) dialog.findViewById(R.id.super_parent);
            dialog.mOutermostLayout.setSelector(new StaticFocusDrawable(this.context.getResources().getDrawable(R.drawable.ytbv_common_focus)));
            dialog.mOutermostLayout.initView();
            dialog.mOutermostLayout.requestFocus();
            if (this.context instanceof Activity) {
                Activity unused2 = dialog.mActivityContext = (Activity) this.context;
            }
            Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);
            if (this.positiveButtonText != null) {
                positiveButton.setVisibility(0);
                positiveButton.setText(this.positiveButtonText);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.positiveButtonClickListener != null) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    }
                });
            } else {
                positiveButton.setVisibility(8);
            }
            if (this.positiveButtonText == null) {
                dialog.findViewById(R.id.foot).setVisibility(8);
            }
            if (this.message != null) {
                ((TextView) dialog.findViewById(R.id.message)).setText(this.message);
            }
            dialog.setCancelable(this.cancelable);
            positiveButton.requestFocus();
            return dialog;
        }
    }
}
