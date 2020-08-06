package com.tvtaobao.android.ui3.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;
import java.util.concurrent.atomic.AtomicLong;

public class UI3Toast extends Dialog {
    public static long DURATION_LONG = 7000;
    public static long DURATION_SHORT = 3000;
    private static AtomicLong balance = new AtomicLong(0);
    private ConstraintLayout constraintLayout;
    private Runnable dismissTask = new Runnable() {
        public void run() {
            try {
                UI3Toast.this.dismiss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
    private long duration = DURATION_SHORT;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView textView;

    public UI3Toast(Context context) {
        super(context, R.style.ui3wares_dialogD);
        setContentView(R.layout.ui3wares_layout_tvtoast);
        findViews();
        getWindow().setLayout(-1, -1);
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        this.textView = (TextView) findViewById(R.id.text_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration2) {
        this.duration = duration2;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void show() {
        show(false);
    }

    public void show(boolean focusable) {
        if (!focusable && getWindow() != null) {
            getWindow().addFlags(8);
        }
        super.show();
        balance.incrementAndGet();
        if (this.constraintLayout != null) {
            this.constraintLayout.removeCallbacks(this.dismissTask);
            this.constraintLayout.postDelayed(this.dismissTask, this.duration);
        }
    }

    public void dismiss() {
        super.dismiss();
        balance.decrementAndGet();
    }

    public static long getBalance() {
        return balance.get();
    }

    public static final UI3Toast makeToast(Context context, String txt, Drawable icon, long duration2) {
        UI3Toast rtn = new UI3Toast(context);
        rtn.duration = duration2;
        rtn.textView.setText(txt);
        if (icon != null) {
            rtn.imageView.setVisibility(0);
            rtn.imageView.setImageDrawable(icon);
        } else {
            rtn.imageView.setVisibility(8);
        }
        return rtn;
    }

    public static final UI3Toast makeToast(Context context, String txt, int duration2) {
        return makeToast(context, txt, (Drawable) null, (long) duration2);
    }

    public static final UI3Toast makeToast(Context context, String txt) {
        return makeToast(context, txt, (Drawable) null, DURATION_SHORT);
    }

    public static final UI3Toast makeLongToast(Context context, String txt) {
        return makeToast(context, txt, (Drawable) null, DURATION_LONG);
    }

    public static final UI3Toast makeSuccessToast(Context context, String txt) {
        return makeToast(context, txt, context.getResources().getDrawable(R.drawable.ui3wares_success_icon), DURATION_SHORT);
    }
}
