package com.tvtaobao.android.addresswares;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.FullScreenDialog;

public class AddressChosePromptDlg extends FullScreenDialog {
    /* access modifiers changed from: private */
    public ViewGroup buttonArea;
    /* access modifiers changed from: private */
    public FrameLayout content;
    /* access modifiers changed from: private */
    public View contentDivider;
    /* access modifiers changed from: private */
    public EventListener listener;
    /* access modifiers changed from: private */
    public ImageView logo;
    /* access modifiers changed from: private */
    public TextView negativeButton;
    /* access modifiers changed from: private */
    public TextView positiveButton;
    /* access modifiers changed from: private */
    public TextView title;
    /* access modifiers changed from: private */
    public ViewGroup titleArea;
    /* access modifiers changed from: private */
    public TextView tvContent;

    public interface EventListener {
        void onCancel(AddressChosePromptDlg addressChosePromptDlg);

        void onOK(AddressChosePromptDlg addressChosePromptDlg);
    }

    public void setListener(EventListener listener2) {
        this.listener = listener2;
    }

    public AddressChosePromptDlg(Context context) {
        super(context);
        findViews();
    }

    private void findViews() {
        this.titleArea = (ViewGroup) findViewById(R.id.titleArea);
        this.content = (FrameLayout) findViewById(R.id.content);
        this.buttonArea = (ViewGroup) findViewById(R.id.buttonArea);
        this.logo = (ImageView) findViewById(R.id.icon);
        this.title = (TextView) findViewById(R.id.title);
        this.positiveButton = (TextView) findViewById(R.id.positiveButton);
        this.negativeButton = (TextView) findViewById(R.id.negativeButton);
        this.tvContent = (TextView) findViewById(R.id.tv_content);
        this.contentDivider = findViewById(R.id.content_divider);
    }

    public void show() {
        super.show(true);
    }

    public View onCreateView() {
        return getLayoutInflater().inflate(R.layout.addresswares_chose_prompt_dlg, (ViewGroup) null);
    }

    public static class Builder {
        private View contentView;
        private Context context;
        private boolean hasButton = true;
        /* access modifiers changed from: private */
        public EventListener listener;
        private int logoResId;
        private String msg;
        private boolean needTitle = false;
        private String negativeMessage;
        private String positiveMessage;
        private String title;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setLogo(int logoResId2) {
            this.logoResId = logoResId2;
            return this;
        }

        public Builder setTitle(String title2) {
            this.title = title2;
            this.needTitle = !TextUtils.isEmpty(title2);
            return this;
        }

        public Builder setNeedTitle(boolean needTitle2) {
            this.needTitle = needTitle2;
            return this;
        }

        public Builder setContentView(View contentView2) {
            this.contentView = contentView2;
            return this;
        }

        public Builder setMessage(String msg2) {
            this.msg = msg2;
            return this;
        }

        public Builder setHasButton(boolean hasButton2) {
            this.hasButton = hasButton2;
            return this;
        }

        public Builder setButtons(String positiveButtonTxt, String negativeButtonTxt, EventListener eventListener) {
            this.positiveMessage = positiveButtonTxt;
            this.negativeMessage = negativeButtonTxt;
            this.listener = eventListener;
            this.hasButton = eventListener != null;
            return this;
        }

        public AddressChosePromptDlg create() {
            final AddressChosePromptDlg dialog = new AddressChosePromptDlg(this.context);
            if (!this.needTitle) {
                dialog.titleArea.setVisibility(8);
                dialog.contentDivider.setVisibility(8);
            } else {
                dialog.titleArea.setVisibility(0);
                dialog.contentDivider.setVisibility(0);
                if (this.logoResId != 0) {
                    dialog.logo.setImageResource(this.logoResId);
                }
                dialog.title.setText(this.title);
            }
            if (this.contentView != null) {
                dialog.tvContent.setVisibility(8);
                dialog.content.addView(this.contentView);
            } else if (!TextUtils.isEmpty(this.msg)) {
                dialog.tvContent.setText(this.msg);
                dialog.tvContent.setVisibility(0);
            } else {
                dialog.content.setVisibility(8);
            }
            if (this.hasButton) {
                EventListener unused = dialog.listener = this.listener;
                dialog.buttonArea.setVisibility(0);
                dialog.positiveButton.setText(this.positiveMessage);
                dialog.negativeButton.setText(this.negativeMessage);
                dialog.positiveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.listener != null) {
                            Builder.this.listener.onOK(dialog);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.negativeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (dialog.listener != null) {
                            dialog.listener.onCancel(dialog);
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                dialog.buttonArea.setVisibility(8);
            }
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dlg) {
                    if (dialog.listener != null) {
                        dialog.listener.onCancel(dialog);
                    }
                }
            });
            return dialog;
        }
    }
}
