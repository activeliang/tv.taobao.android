package com.tvtaobao.android.ui3.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.taobao.windvane.service.WVEventId;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;

public class FloatWindow {
    FrameLayout floatArea;
    WindowManager.LayoutParams lp4FloatArea;

    public interface ContentInflater {
        View getView();
    }

    public interface LayoutParamSetter {
        void doConfig(WindowManager.LayoutParams layoutParams);
    }

    private FloatWindow() {
    }

    private FloatWindow(Context context) {
        this.floatArea = new FrameLayout(context.getApplicationContext()) {
            /* access modifiers changed from: protected */
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
            }

            /* access modifiers changed from: protected */
            public void onDetachedFromWindow() {
                super.onDetachedFromWindow();
            }

            /* access modifiers changed from: protected */
            public void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
            }
        };
        this.floatArea.setBackgroundColor(0);
        this.lp4FloatArea = new WindowManager.LayoutParams();
        this.lp4FloatArea.type = WVEventId.PAGE_onReceivedTitle;
        this.lp4FloatArea.format = 1;
        this.lp4FloatArea.flags = 8;
        this.lp4FloatArea.gravity = 51;
        this.lp4FloatArea.x = 0;
        this.lp4FloatArea.y = 0;
        this.lp4FloatArea.width = Opcodes.REM_INT_LIT8;
        this.lp4FloatArea.height = 200;
    }

    public static FloatWindow obtain(Context context) {
        return new FloatWindow(context);
    }

    public FrameLayout getFloatArea() {
        return this.floatArea;
    }

    public FloatWindow inflateContent(ContentInflater contentInflater) {
        if (contentInflater != null) {
            this.floatArea.removeAllViews();
            this.floatArea.addView(contentInflater.getView(), new FrameLayout.LayoutParams(-1, -1));
        }
        return this;
    }

    public FloatWindow configLayoutParam(LayoutParamSetter layoutParamSetter) {
        if (layoutParamSetter != null) {
            layoutParamSetter.doConfig(this.lp4FloatArea);
        }
        return this;
    }

    public void show() {
        try {
            ((WindowManager) this.floatArea.getContext().getSystemService("window")).addView(this.floatArea, this.lp4FloatArea);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        try {
            ((WindowManager) this.floatArea.getContext().getSystemService("window")).removeView(this.floatArea);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
