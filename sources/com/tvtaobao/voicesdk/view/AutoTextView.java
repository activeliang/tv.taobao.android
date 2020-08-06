package com.tvtaobao.voicesdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.tvtaobao.voicesdk.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AutoTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private final int gravity;
    private int lines;
    private Context mContext;
    private float mHeight;
    private Rotate3dAnimation mInDown;
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutDown;
    private Rotate3dAnimation mOutUp;
    /* access modifiers changed from: private */
    public List<String> strings;
    private int textColor;
    /* access modifiers changed from: private */
    public TimeHandler timeHandler;

    public AutoTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHeight = 36.0f;
        this.lines = 0;
        this.strings = new ArrayList();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.autotext);
        this.mHeight = (float) (((double) a.getDimension(R.styleable.autotext_textSize, 36.0f)) / 1.5d);
        this.textColor = a.getColor(R.styleable.autotext_textcolor, -1);
        this.lines = a.getInt(R.styleable.autotext_lines, 0);
        this.gravity = a.getInt(R.styleable.autotext_gravity, 0);
        a.recycle();
        this.mContext = context;
        init(context);
        this.timeHandler = new TimeHandler(this);
    }

    private void init(Context context) {
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down_in));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up_out));
    }

    public View makeView() {
        TextView t = new TextView(this.mContext);
        t.setTextSize(this.mHeight);
        if (this.lines != 0) {
            t.setMaxLines(this.lines);
            t.setEllipsize(TextUtils.TruncateAt.END);
        }
        t.setTextColor(this.textColor);
        t.setMaxLines(2);
        t.setGravity(this.gravity | 16);
        return t;
    }

    public void autoScroll(List<String> s) {
        if (s != null && s.size() != 0) {
            clear();
            this.strings.addAll(s);
            if (s.size() == 1) {
                setText(s.get(0));
            } else {
                this.timeHandler.sendEmptyMessage(0);
            }
        }
    }

    public void clear() {
        this.timeHandler.removeCallbacksAndMessages((Object) null);
        this.timeHandler.clearCount();
        this.strings.clear();
    }

    public void previous() {
        if (getInAnimation() != this.mInDown) {
            setInAnimation(this.mInDown);
        }
        if (getOutAnimation() != this.mOutDown) {
            setOutAnimation(this.mOutDown);
        }
    }

    public void next() {
        if (getInAnimation() != this.mInUp) {
            setInAnimation(this.mInUp);
        }
        if (getOutAnimation() != this.mOutUp) {
            setOutAnimation(this.mOutUp);
        }
    }

    private static class TimeHandler extends Handler {
        private WeakReference<AutoTextView> mWeakReference;
        private int sCount = 0;

        public TimeHandler(AutoTextView view) {
            this.mWeakReference = new WeakReference<>(view);
        }

        public void clearCount() {
            this.sCount = 0;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (this.mWeakReference != null && this.mWeakReference.get() != null) {
                        AutoTextView view = (AutoTextView) this.mWeakReference.get();
                        List<String> strings = view.strings;
                        view.setText(strings.get(this.sCount % strings.size()));
                        this.sCount++;
                        view.timeHandler.sendEmptyMessageDelayed(0, 8000);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    class Rotate3dAnimation extends Animation {
        private Camera mCamera;
        private float mCenterX;
        private float mCenterY;
        private final float mFromDegrees;
        private final float mToDegrees;
        private final boolean mTurnIn;
        private final boolean mTurnUp;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            this.mFromDegrees = fromDegrees;
            this.mToDegrees = toDegrees;
            this.mTurnIn = turnIn;
            this.mTurnUp = turnUp;
        }

        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            this.mCamera = new Camera();
            this.mCenterY = (float) (AutoTextView.this.getHeight() / 2);
            this.mCenterX = (float) (AutoTextView.this.getWidth() / 2);
        }

        /* access modifiers changed from: protected */
        public void applyTransformation(float interpolatedTime, Transformation t) {
            float fromDegrees = this.mFromDegrees;
            float degrees = fromDegrees + ((this.mToDegrees - fromDegrees) * interpolatedTime);
            float centerX = this.mCenterX;
            float centerY = this.mCenterY;
            Camera camera = this.mCamera;
            int derection = this.mTurnUp ? 1 : -1;
            Matrix matrix = t.getMatrix();
            camera.save();
            if (this.mTurnIn) {
                camera.translate(0.0f, ((float) derection) * this.mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, ((float) derection) * this.mCenterY * interpolatedTime, 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
