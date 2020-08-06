package com.yunos.tvtaobao.payment.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yunos.tvtaobao.payment.R;

public class PayConfirmDialog extends Dialog {
    private String TAG = "successpayment";
    /* access modifiers changed from: private */
    public Activity mActivityContext;

    public PayConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public PayConfirmDialog(Context context) {
        super(context);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void dismiss() {
        super.dismiss();
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
            final PayConfirmDialog dialog = new PayConfirmDialog(this.context, R.style.payment_QRdialog);
            dialog.setContentView(R.layout.dialog_confirmpay);
            if (this.context instanceof Activity) {
                Activity unused = dialog.mActivityContext = (Activity) this.context;
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
