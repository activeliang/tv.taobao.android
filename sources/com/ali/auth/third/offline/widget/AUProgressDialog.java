package com.ali.auth.third.offline.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ali.auth.third.offline.R;

public class AUProgressDialog extends ProgressDialog {
    private boolean mIndeterminate;
    private CharSequence mMessage;
    private TextView mMessageView;
    private ProgressBar mProgress;
    private boolean mProgressVisiable;

    public AUProgressDialog(Context context) {
        super(context);
    }

    public AUProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliuser_progress_dialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(17170445);
        }
        this.mProgress = (ProgressBar) findViewById(16908301);
        this.mMessageView = (TextView) findViewById(R.id.aliuser_toast_message);
        setMessageAndView();
        setIndeterminate(this.mIndeterminate);
    }

    private void setMessageAndView() {
        int i = 8;
        this.mMessageView.setText(this.mMessage);
        if (this.mMessage == null || "".equals(this.mMessage)) {
            this.mMessageView.setVisibility(8);
        }
        ProgressBar progressBar = this.mProgress;
        if (this.mProgressVisiable) {
            i = 0;
        }
        progressBar.setVisibility(i);
    }

    public void setMessage(CharSequence message) {
        this.mMessage = message;
    }

    public void setProgressVisiable(boolean progressVisiable) {
        this.mProgressVisiable = progressVisiable;
    }

    public void setIndeterminate(boolean indeterminate) {
        if (this.mProgress != null) {
            this.mProgress.setIndeterminate(indeterminate);
        } else {
            this.mIndeterminate = indeterminate;
        }
    }
}
