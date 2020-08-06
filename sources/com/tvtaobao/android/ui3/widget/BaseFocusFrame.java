package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.tvtaobao.android.ui3.helper.FocusFrameHelper;

public class BaseFocusFrame extends FrameLayout {
    private FocusFrameHelper focusFrameHelper;

    public interface ExtFocusChangeListener {
        void onFocusChanged(boolean z, int i, Rect rect, View view);
    }

    public BaseFocusFrame(Context context) {
        this(context, (AttributeSet) null);
    }

    public BaseFocusFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFocusFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.focusFrameHelper = new FocusFrameHelper(this);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        this.focusFrameHelper.drawFocusFrame(canvas);
        super.dispatchDraw(canvas);
    }

    public FocusFrameHelper getFocusFrameHelper() {
        return this.focusFrameHelper;
    }

    public static class ExtFocusChangeDispatcher {
        private ExtFocusChangeListener listener;

        public ExtFocusChangeListener getListener() {
            return this.listener;
        }

        public void setListener(ExtFocusChangeListener listener2) {
            this.listener = listener2;
        }

        public void doFocusChangeDispatch(boolean gainFocus, int direction, Rect previouslyFocusedRec, View focus) {
            if (this.listener != null) {
                this.listener.onFocusChanged(gainFocus, direction, previouslyFocusedRec, focus);
            }
        }
    }
}
