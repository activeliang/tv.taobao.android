package com.yunos.tvtaobao.payment.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import com.yunos.tvtaobao.payment.R;

public class CustomConfirmDialog extends Dialog {
    private String TAG = "QuitPayConfirmDialog";
    /* access modifiers changed from: private */
    public Activity mActivityContext;

    public CustomConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomConfirmDialog(Context context) {
        super(context);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {
        private boolean cancelable = true;
        private Context context;
        private String message;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setMessage(String message2) {
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

        public Builder setNegativeButton(int negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText2);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomConfirmDialog show() {
            CustomConfirmDialog dialog = create();
            dialog.show();
            return dialog;
        }

        public CustomConfirmDialog create() {
            final CustomConfirmDialog dialog = new CustomConfirmDialog(this.context, R.style.payment_QRdialog);
            dialog.setContentView(R.layout.payment_dialog_common);
            if (this.context instanceof Activity) {
                Activity unused = dialog.mActivityContext = (Activity) this.context;
            }
            TextView positiveButton = (TextView) dialog.findViewById(R.id.positive);
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
            TextView negativeButton = (TextView) dialog.findViewById(R.id.negative);
            if (this.negativeButtonText != null) {
                negativeButton.setVisibility(0);
                negativeButton.setText(this.negativeButtonText);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.negativeButtonClickListener != null) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                negativeButton.setVisibility(8);
            }
            if (this.positiveButtonText == null && this.negativeButtonText == null) {
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
