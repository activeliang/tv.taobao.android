package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.zhiping.dev.android.logger.ZpLogger;

public class DrawRectFocusPositionManager extends FocusPositionManager {
    private final String TAG = "DrawRectFocusPositionManager";
    private RectF mFocusItemRectF;
    private Handler mHandler;
    private boolean mInitedFocus;
    private Paint mPaint;
    private Rect mRectPadding;
    private Path mRectPath;
    private RectF mTmpRectF;

    public DrawRectFocusPositionManager(Context context) {
        super(context);
        init();
    }

    public DrawRectFocusPositionManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawRectFocusPositionManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawFocusRect(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!this.mInitedFocus) {
            requestChangeFocusItemRect();
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!this.mInitedFocus) {
            requestChangeFocusItemRect();
        }
    }

    public void setDrawRectColor(int color) {
        this.mPaint.setColor(color);
    }

    public void setDrawRectAlpha(float alpha) {
        this.mPaint.setAlpha(51);
    }

    public void clearFocusItemRect() {
        ZpLogger.i("DrawRectFocusPositionManager", "clearFocusItemRect");
        setFocusItemRect(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()));
    }

    public void setFullFocusItemRect() {
        ZpLogger.i("DrawRectFocusPositionManager", "setFullFocusItemRect");
        setFocusItemRect((RectF) null);
    }

    public void setFocusItemRect(RectF rectF) {
        ZpLogger.i("DrawRectFocusPositionManager", "setFocusItemRect rectF=" + rectF);
        if (this.mFocusItemRectF == null) {
            this.mFocusItemRectF = new RectF(rectF);
        } else if (rectF != null) {
            this.mFocusItemRectF.set(rectF);
        } else {
            this.mFocusItemRectF.setEmpty();
        }
        invalidate();
    }

    public void changeFocusItemRect(Rect rect) {
        this.mRectPadding = rect;
        changeFocusItemRect();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0017, code lost:
        r2 = (com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener) r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void changeFocusItemRect() {
        /*
            r9 = this;
            java.lang.String r7 = "DrawRectFocusPositionManager"
            java.lang.String r8 = "changeFocusItemRect"
            com.zhiping.dev.android.logger.ZpLogger.i(r7, r8)
            android.view.View r1 = r9.getFocused()
            com.yunos.tvtaobao.tvsdk.widget.focus.PositionManager r4 = r9.getPositionManager()
            if (r4 == 0) goto L_0x006b
            boolean r7 = r1 instanceof com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener
            if (r7 == 0) goto L_0x006b
            r2 = r1
            com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener r2 = (com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener) r2
            com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener r3 = r2.getItem()
            if (r3 == 0) goto L_0x006b
            com.yunos.tvtaobao.tvsdk.widget.focus.params.Params r7 = r2.getParams()
            if (r7 == 0) goto L_0x006b
            com.yunos.tvtaobao.tvsdk.widget.focus.params.Params r7 = r2.getParams()
            com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams r7 = r7.getScaleParams()
            if (r7 == 0) goto L_0x006b
            com.yunos.tvtaobao.tvsdk.widget.focus.params.Params r7 = r2.getParams()
            com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams r7 = r7.getScaleParams()
            float r5 = r7.getScaleX()
            com.yunos.tvtaobao.tvsdk.widget.focus.params.Params r7 = r2.getParams()
            com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams r7 = r7.getScaleParams()
            float r6 = r7.getScaleY()
            android.graphics.RectF r7 = r9.mFocusItemRectF     // Catch:{ Exception -> 0x006c }
            boolean r8 = r3.isScale()     // Catch:{ Exception -> 0x006c }
            android.graphics.Rect r8 = r4.getDstRect(r5, r6, r8)     // Catch:{ Exception -> 0x006c }
            r7.set(r8)     // Catch:{ Exception -> 0x006c }
            android.graphics.RectF r7 = r9.mFocusItemRectF     // Catch:{ Exception -> 0x006c }
            android.graphics.Rect r8 = r3.getManualPadding()     // Catch:{ Exception -> 0x006c }
            r9.offsetManualPadding(r7, r8)     // Catch:{ Exception -> 0x006c }
            android.graphics.RectF r7 = r9.mFocusItemRectF     // Catch:{ Exception -> 0x006c }
            android.graphics.Rect r8 = r9.mRectPadding     // Catch:{ Exception -> 0x006c }
            r9.offsetManualPadding(r7, r8)     // Catch:{ Exception -> 0x006c }
            r7 = 1
            r9.mInitedFocus = r7     // Catch:{ Exception -> 0x006c }
            r9.invalidate()     // Catch:{ Exception -> 0x006c }
        L_0x006b:
            return
        L_0x006c:
            r0 = move-exception
            java.lang.String r7 = "DrawRectFocusPositionManager"
            java.lang.String r8 = "changeFocusItemRect error"
            com.zhiping.dev.android.logger.ZpLogger.w(r7, r8)
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.widget.DrawRectFocusPositionManager.changeFocusItemRect():void");
    }

    private void requestChangeFocusItemRect() {
        ZpLogger.i("DrawRectFocusPositionManager", "requestChangeFocusItemRect");
        this.mHandler.post(new Runnable() {
            public void run() {
                DrawRectFocusPositionManager.this.changeFocusItemRect();
            }
        });
    }

    private void drawFocusRect(Canvas canvas) {
        this.mRectPath.reset();
        if (checkRectValid()) {
            this.mRectPath.addRect(this.mFocusItemRectF, Path.Direction.CW);
            this.mTmpRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            this.mRectPath.addRect(this.mTmpRectF, Path.Direction.CCW);
            this.mRectPath.close();
            canvas.drawPath(this.mRectPath, this.mPaint);
            return;
        }
        this.mTmpRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
        canvas.drawRect(this.mTmpRectF, this.mPaint);
    }

    private void offsetManualPadding(RectF r, Rect padding) {
        if (padding != null && !padding.isEmpty()) {
            r.left += (float) padding.left;
            r.right += (float) padding.right;
            r.top += (float) padding.top;
            r.bottom += (float) padding.bottom;
        }
    }

    private boolean checkRectValid() {
        if (this.mFocusItemRectF == null || this.mFocusItemRectF.isEmpty()) {
            return false;
        }
        return true;
    }

    private void init() {
        this.mHandler = new Handler();
        this.mFocusItemRectF = new RectF();
        this.mTmpRectF = new RectF();
        this.mRectPath = new Path();
        this.mPaint = new Paint();
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setAlpha(51);
        this.mPaint.setStyle(Paint.Style.FILL);
    }
}
