package com.taobao.taobaoavsdk.widget.extra.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import com.taobao.taobaoavsdk.IAVObject;
import mtopsdk.common.util.SymbolExpUtil;

public class DanmakuItem implements IDanmakuItem, IAVObject {
    private static int sBaseSpeed = 3;
    private static TextPaint strokePaint = new TextPaint();
    private StaticLayout borderStaticLayout;
    private int mContainerHeight;
    private int mContainerWidth;
    private String mContent;
    private int mContentHeight;
    private int mContentWidth;
    private Context mContext;
    private int mCurrX;
    private int mCurrY;
    private DWDanmakuStyle mDanmakuStyle;
    private boolean mDrawing;
    private long mShowTime;
    private float mSpeedFactorX;
    private int mTextColor = -1;
    private int mTextSize;
    private StaticLayout staticLayout;

    static {
        strokePaint.setARGB(255, 0, 0, 0);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(4.0f);
        strokePaint.setAntiAlias(true);
    }

    public DanmakuItem(Context context, String nick, String content, boolean send, long showTime, int width, int startX, int startY, DWDanmakuStyle style) {
        this.mContext = context;
        this.mDanmakuStyle = style;
        this.mContainerWidth = width;
        StringBuilder contentBuilder = new StringBuilder();
        if (send) {
            contentBuilder.append("★");
        }
        StringBuilder nickBuilder = new StringBuilder();
        if (nick.length() == 2) {
            nickBuilder.append(nick.charAt(0)).append("**").append(nick.charAt(1));
        } else if (nick.length() == 1) {
            nickBuilder.append(nick.charAt(0)).append("**");
        } else if (nick.length() > 2) {
            nickBuilder.append(nick.charAt(0)).append("**").append(nick.charAt(nick.length() - 1));
        }
        contentBuilder.append(nickBuilder.toString()).append(SymbolExpUtil.SYMBOL_COLON).append(content);
        if (send) {
            contentBuilder.append("★");
        }
        this.mContent = contentBuilder.toString();
        this.mShowTime = showTime;
        this.mCurrX = startX;
        this.mCurrY = startY;
        setTextColor(this.mDanmakuStyle.textColor);
        setTextSize(this.mDanmakuStyle.textSize);
        this.mSpeedFactorX = this.mDanmakuStyle.speedFactorX;
        measure();
    }

    public DanmakuItem(Context context, String content, long showTime, int width, int startX, int startY, DWDanmakuStyle style) {
        this.mContext = context;
        this.mDanmakuStyle = style;
        this.mContainerWidth = width;
        this.mContent = content;
        this.mShowTime = showTime;
        this.mCurrX = startX;
        this.mCurrY = startY;
        setTextColor(this.mDanmakuStyle.textColor);
        setTextSize(this.mDanmakuStyle.textSize);
        this.mSpeedFactorX = this.mDanmakuStyle.speedFactorX;
        measure();
    }

    public void addDrawingList(boolean drawing) {
        this.mDrawing = drawing;
    }

    public boolean drawing() {
        return this.mDrawing;
    }

    public long showTime() {
        return this.mShowTime;
    }

    private void measure() {
        TextPaint tp = new TextPaint();
        tp.setAntiAlias(true);
        tp.setColor(this.mTextColor);
        tp.setTextSize((float) this.mTextSize);
        strokePaint.setTextSize((float) this.mTextSize);
        tp.setShadowLayer((float) this.mDanmakuStyle.textShadowRadius, (float) this.mDanmakuStyle.textShadowX, (float) this.mDanmakuStyle.textShadowY, this.mDanmakuStyle.textShadowColor);
        tp.setFakeBoldText(this.mDanmakuStyle.isBold);
        this.mContentHeight = getFontHeight(tp);
        SpannableString spannableString = new SpannableString(this.mContent);
        spannableString.setSpan(new BackgroundColorSpan(this.mDanmakuStyle.textBackgroundColor), 0, this.mContent.length(), 33);
        this.staticLayout = new StaticLayout(spannableString, tp, ((int) Layout.getDesiredWidth(this.mContent, 0, this.mContent.length(), tp)) + 1, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.mContentWidth = this.staticLayout.getWidth();
        this.borderStaticLayout = new StaticLayout(spannableString, strokePaint, ((int) Layout.getDesiredWidth(this.mContent, 0, this.mContent.length(), tp)) + 1, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    public void doDraw(Canvas canvas, boolean pause) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        if (!(canvasWidth == this.mContainerWidth && canvasHeight == this.mContainerHeight)) {
            this.mContainerWidth = canvasWidth;
            this.mContainerHeight = canvasHeight;
        }
        canvas.save();
        canvas.translate((float) this.mCurrX, (float) this.mCurrY);
        this.borderStaticLayout.draw(canvas);
        this.staticLayout.draw(canvas);
        canvas.restore();
        if (!pause) {
            this.mCurrX = (int) (((float) this.mCurrX) - (((float) sBaseSpeed) * this.mSpeedFactorX));
            if (this.mCurrX <= 0) {
                this.mCurrX--;
            }
        }
    }

    public void setTextSize(int textSizeInDip) {
        if (textSizeInDip > 0) {
            this.mTextSize = dip2px(this.mContext, (float) textSizeInDip);
            measure();
            return;
        }
        this.mTextSize = dip2px(this.mContext, 12.0f);
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        measure();
    }

    public void setStartPosition(int x, int y) {
        this.mCurrX = x;
        this.mCurrY = y;
    }

    public void setSpeedFactor(float factor) {
        this.mSpeedFactorX = factor;
    }

    public float getSpeedFactor() {
        return this.mSpeedFactorX;
    }

    public boolean isOut() {
        return this.mCurrX < 0 && Math.abs(this.mCurrX) > this.mContentWidth;
    }

    public void release() {
        this.mContext = null;
    }

    public int getWidth() {
        return this.mContentWidth;
    }

    public int getHeight() {
        return this.mContentHeight;
    }

    public int getCurrX() {
        return this.mCurrX;
    }

    public int getCurrY() {
        return this.mCurrY;
    }

    public boolean willHit(IDanmakuItem runningItem) {
        if (runningItem.getWidth() + runningItem.getCurrX() > this.mContainerWidth) {
            return true;
        }
        if (runningItem.getSpeedFactor() >= this.mSpeedFactorX) {
            return false;
        }
        float len1 = (float) (runningItem.getCurrX() + runningItem.getWidth());
        if (this.mSpeedFactorX * (len1 / (runningItem.getSpeedFactor() * ((float) sBaseSpeed))) * ((float) sBaseSpeed) <= len1) {
            return false;
        }
        return true;
    }

    public static int getBaseSpeed() {
        return sBaseSpeed;
    }

    public static void setBaseSpeed(int baseSpeed) {
        sBaseSpeed = baseSpeed;
    }

    private static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private static int getFontHeight(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return ((int) Math.ceil((double) (fm.descent - fm.top))) + 2;
    }
}
