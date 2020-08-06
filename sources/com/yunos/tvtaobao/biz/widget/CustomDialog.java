package com.yunos.tvtaobao.biz.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import com.yunos.tvtaobao.biz.dialog.DialogFocusLeftRightPositionManager;
import com.yunos.tvtaobao.businessview.R;

public class CustomDialog extends Dialog {
    private DialogFocusLeftRightPositionManager mOutermostLayout;

    public CustomDialog(Context context, int style) {
        super(context, style);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = 17;
        window.setAttributes(params);
    }

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public void show() {
        super.show();
    }

    public static class Builder {
        private boolean cancelable = true;
        /* access modifiers changed from: private */
        public Context context;
        private int dialogIcon = 0;
        private int dialogType;
        private boolean hasIcon;
        private CharSequence message;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private String resultMessage;
        private int style = R.style.RoundCornerDialog;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setType(int type) {
            this.dialogType = type;
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

        public Builder setResultMessage(String message2) {
            this.resultMessage = message2;
            return this;
        }

        public Builder setMessage(CharSequence message2) {
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

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(this.context, this.style);
            if (this.dialogType == 1) {
                dialog.setContentView(R.layout.layout_normal_dialog);
                ImageView ivDialogIcon = (ImageView) dialog.findViewById(R.id.iv_icon);
                if (this.dialogIcon != 0) {
                    ivDialogIcon.setImageResource(this.dialogIcon);
                }
                if (this.hasIcon) {
                    ivDialogIcon.setVisibility(0);
                } else {
                    ivDialogIcon.setVisibility(8);
                }
                TextView dialogResult = (TextView) dialog.findViewById(R.id.tv_result);
                if (!TextUtils.isEmpty(this.resultMessage)) {
                    dialogResult.setText(this.resultMessage);
                }
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
            } else if (this.dialogType == 2) {
                dialog.setContentView(R.layout.layout_button_dialog);
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
                                positiveButton.setBackgroundResource(R.drawable.gradient_dialog_right_focus);
                                positiveButton.setTextColor(Builder.this.context.getResources().getColor(R.color.ytm_white));
                                return;
                            }
                            positiveButton.setBackgroundResource(R.color.transparent);
                            positiveButton.setTextColor(Builder.this.context.getResources().getColor(R.color.new_cart_grey));
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
                                negativeButton.setBackgroundResource(R.drawable.gradient_dialog_left_focus);
                                negativeButton.setTextColor(Builder.this.context.getResources().getColor(R.color.ytm_white));
                                return;
                            }
                            negativeButton.setBackgroundResource(R.color.transparent);
                            negativeButton.setTextColor(Builder.this.context.getResources().getColor(R.color.new_cart_grey));
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
            }
            dialog.setCancelable(this.cancelable);
            return dialog;
        }
    }
}
