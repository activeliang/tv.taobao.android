package android.taobao.atlas.runtime.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.taobao.atlas.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DefaultProgress extends FrameLayout {
    private DefaultProgressDrawable mProgressDrawable;
    private ImageView mProgressView;

    public DefaultProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mProgressDrawable = new DefaultProgressDrawable(-1, 16.0f);
        this.mProgressDrawable.setCallback(this);
        View.inflate(context, R.layout.atlas_progress, this);
        this.mProgressView = (ImageView) findViewById(R.id.at_circularProgress);
        this.mProgressDrawable.setRingColor(-6710887);
        this.mProgressDrawable.setRingWidth(2.0f * context.getResources().getDisplayMetrics().density);
        int ringSize = (int) (32.0f * context.getResources().getDisplayMetrics().density);
        this.mProgressView.getLayoutParams().width = ringSize;
        this.mProgressView.getLayoutParams().height = ringSize;
        this.mProgressView.setImageDrawable(this.mProgressDrawable);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.atlas_waitview));
        setAlpha(1.0f);
    }

    public DefaultProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultProgress(Context context) {
        this(context, (AttributeSet) null);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mProgressDrawable.setBounds(0, 0, w, h);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.stop();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mProgressDrawable == null) {
            return;
        }
        if (visibility == 8 || visibility == 4) {
            this.mProgressDrawable.stop();
        } else {
            this.mProgressDrawable.start();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return who == this.mProgressDrawable || super.verifyDrawable(who);
    }
}
