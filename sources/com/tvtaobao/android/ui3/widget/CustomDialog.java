package com.tvtaobao.android.ui3.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;

public class CustomDialog extends Dialog {

    public enum Type {
        no_btn,
        no_btn_no_atuoclose,
        one_btn,
        double_btn
    }

    public CustomDialog(Context context, int style) {
        super(context, style);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public void show() {
        super.show();
    }

    public static class Builder {
        private boolean cancelable = true;
        /* access modifiers changed from: private */
        public Context context;
        private int countdownSeconds = -1;
        private String deputyCopywriter;
        private int dialogIcon = 0;
        private Type dialogType;
        private boolean hasIcon;
        private String mainCopywriter;
        private String message;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private String oneLineResultMessage;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener positiveButtonClickListener;
        /* access modifiers changed from: private */
        public String positiveButtonText;
        private int style = R.style.ui3wares_dialogA;
        private String twoLineResultMessage;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setType(Type type) {
            this.dialogType = type;
            if (type == Type.no_btn) {
                this.style = R.style.ui3wares_dialogB;
            }
            return this;
        }

        public Builder setIcon(int icon) {
            this.dialogIcon = icon;
            return this;
        }

        public Builder setHasIcon(boolean hasIcon2) {
            this.hasIcon = hasIcon2;
            return this;
        }

        public Builder setOneLineResultMessage(String message2) {
            this.oneLineResultMessage = message2;
            return this;
        }

        public Builder setTwoLineResultMessage(String message2) {
            this.twoLineResultMessage = message2;
            return this;
        }

        public Builder setMessage(String message2) {
            this.message = message2;
            return this;
        }

        public Builder setCancelable(boolean cancelable2) {
            this.cancelable = cancelable2;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setStyle(int style2) {
            this.style = style2;
            return this;
        }

        public Builder setMainCopywriter(String mainCopywriter2) {
            this.mainCopywriter = mainCopywriter2;
            return this;
        }

        public Builder setDeputyCopywriter(String deputyCopywriter2) {
            this.deputyCopywriter = deputyCopywriter2;
            return this;
        }

        public Builder setCountdownSeconds(int countdownSeconds2) {
            this.countdownSeconds = countdownSeconds2;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(this.context, this.style);
            switch (this.dialogType) {
                case no_btn:
                case no_btn_no_atuoclose:
                    dialog.setContentView(R.layout.ui3wares_layout_normal_dialog);
                    ImageView ivDialogIcon = (ImageView) dialog.findViewById(R.id.iv_icon);
                    if (this.dialogIcon != 0) {
                        ivDialogIcon.setImageResource(this.dialogIcon);
                    }
                    if (this.hasIcon) {
                        ivDialogIcon.setVisibility(0);
                    } else {
                        ivDialogIcon.setVisibility(8);
                    }
                    if (!TextUtils.isEmpty(this.oneLineResultMessage)) {
                        TextView dialogResult = (TextView) dialog.findViewById(R.id.tv_result);
                        dialogResult.setText(this.oneLineResultMessage);
                        dialogResult.setTextSize(0, (float) this.context.getResources().getDimensionPixelSize(R.dimen.values_sp_30));
                        dialogResult.setVisibility(0);
                        dialogResult.setMaxLines(1);
                        dialogResult.setEllipsize(TextUtils.TruncateAt.END);
                    }
                    if (!TextUtils.isEmpty(this.twoLineResultMessage)) {
                        TextView dialogResult2 = (TextView) dialog.findViewById(R.id.tv_result);
                        dialogResult2.setText(this.twoLineResultMessage);
                        dialogResult2.setTextSize(0, (float) this.context.getResources().getDimensionPixelSize(R.dimen.values_sp_28));
                        dialogResult2.setVisibility(0);
                        dialogResult2.setMaxLines(2);
                        dialogResult2.setEllipsize(TextUtils.TruncateAt.END);
                    }
                    RelativeLayout rlMainAndDeputy = (RelativeLayout) dialog.findViewById(R.id.rl_main_and_deputy);
                    TextView tvMainCopywriterNoButton = (TextView) dialog.findViewById(R.id.tv_main_copywriter);
                    TextView tvDeputyCopywriterNoButton = (TextView) dialog.findViewById(R.id.tv_deputy_copywriter);
                    if (!TextUtils.isEmpty(this.mainCopywriter)) {
                        rlMainAndDeputy.setVisibility(0);
                        tvMainCopywriterNoButton.setVisibility(0);
                        tvMainCopywriterNoButton.setText(this.mainCopywriter);
                    } else {
                        tvMainCopywriterNoButton.setVisibility(8);
                        rlMainAndDeputy.setVisibility(8);
                    }
                    if (!TextUtils.isEmpty(this.deputyCopywriter)) {
                        if (rlMainAndDeputy.getVisibility() == 8 || rlMainAndDeputy.getVisibility() == 4) {
                            rlMainAndDeputy.setVisibility(0);
                        }
                        tvDeputyCopywriterNoButton.setText(this.deputyCopywriter);
                        tvDeputyCopywriterNoButton.setVisibility(0);
                    } else {
                        tvDeputyCopywriterNoButton.setVisibility(8);
                    }
                    if (this.dialogType == Type.no_btn) {
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            public void onShow(DialogInterface dialogInterface) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if ((Builder.this.context instanceof Activity) && !((Activity) Builder.this.context).isFinishing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                }, AbstractClientManager.BIND_SERVICE_TIMEOUT);
                            }
                        });
                        break;
                    }
                    break;
                case one_btn:
                    dialog.setContentView(R.layout.ui3wares_layout_one_button_dialog);
                    final TextView onePositiveButton = (TextView) dialog.findViewById(R.id.positiveButton);
                    if (onePositiveButton != null) {
                        onePositiveButton.setVisibility(0);
                        if (this.countdownSeconds > 0) {
                            new CountDownTimer((long) ((this.countdownSeconds + 1) * 1000), 1000) {
                                public void onTick(long millisUntilFinished) {
                                    if (Builder.this.context != null) {
                                        onePositiveButton.setText(Builder.this.positiveButtonText + "（" + ((int) (millisUntilFinished / 1000)) + "）");
                                    }
                                }

                                public void onFinish() {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            }.start();
                        } else {
                            onePositiveButton.setText(this.positiveButtonText);
                        }
                        onePositiveButton.requestFocus();
                        onePositiveButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (Builder.this.positiveButtonClickListener != null) {
                                    Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                                }
                            }
                        });
                    } else {
                        dialog.findViewById(R.id.foot).setVisibility(8);
                    }
                    if (this.message != null) {
                        TextView msgTextView = (TextView) dialog.findViewById(R.id.message);
                        msgTextView.setText(this.message);
                        msgTextView.setVisibility(0);
                    }
                    if (this.mainCopywriter != null) {
                        TextView tvMainCopywriter = (TextView) dialog.findViewById(R.id.tv_main_copywriter);
                        tvMainCopywriter.setText(this.mainCopywriter);
                        tvMainCopywriter.setVisibility(0);
                    }
                    if (this.mainCopywriter != null) {
                        TextView tvDeputyCopywriter = (TextView) dialog.findViewById(R.id.tv_deputy_copywriter);
                        tvDeputyCopywriter.setText(this.deputyCopywriter);
                        tvDeputyCopywriter.setVisibility(0);
                        break;
                    }
                    break;
                case double_btn:
                    dialog.setContentView(R.layout.ui3wares_layout_button_dialog);
                    final TextView positiveButton = (TextView) dialog.findViewById(R.id.positiveButton);
                    if (this.positiveButtonText != null) {
                        positiveButton.setVisibility(0);
                        positiveButton.setText(this.positiveButtonText);
                        positiveButton.requestFocus();
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (Builder.this.positiveButtonClickListener != null) {
                                    Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                                }
                            }
                        });
                        positiveButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    positiveButton.setTextColor(Builder.this.context.getResources().getColor(17170443));
                                } else {
                                    positiveButton.setTextColor(Builder.this.context.getResources().getColor(R.color.values_color_606060));
                                }
                            }
                        });
                    } else {
                        positiveButton.setVisibility(8);
                    }
                    final TextView negativeButton = (TextView) dialog.findViewById(R.id.negativeButton);
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
                        negativeButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    negativeButton.setTextColor(Builder.this.context.getResources().getColor(17170443));
                                } else {
                                    negativeButton.setTextColor(Builder.this.context.getResources().getColor(R.color.values_color_606060));
                                }
                            }
                        });
                    } else {
                        negativeButton.setVisibility(8);
                    }
                    if (this.positiveButtonText == null && this.negativeButtonText == null) {
                        dialog.findViewById(R.id.foot).setVisibility(8);
                    }
                    if (this.message != null) {
                        TextView msgTextView2 = (TextView) dialog.findViewById(R.id.message);
                        msgTextView2.setText(this.message);
                        msgTextView2.setVisibility(0);
                    }
                    if (this.mainCopywriter != null) {
                        TextView tvMainCopywriter2 = (TextView) dialog.findViewById(R.id.tv_main_copywriter);
                        tvMainCopywriter2.setText(this.mainCopywriter);
                        tvMainCopywriter2.setVisibility(0);
                    }
                    if (this.mainCopywriter != null) {
                        TextView tvDeputyCopywriter2 = (TextView) dialog.findViewById(R.id.tv_deputy_copywriter);
                        tvDeputyCopywriter2.setText(this.deputyCopywriter);
                        tvDeputyCopywriter2.setVisibility(0);
                        break;
                    }
                    break;
            }
            dialog.setCancelable(this.cancelable);
            return dialog;
        }
    }
}
