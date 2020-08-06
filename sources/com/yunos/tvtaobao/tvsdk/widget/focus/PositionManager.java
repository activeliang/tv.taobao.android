package com.yunos.tvtaobao.tvsdk.widget.focus;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.yunos.tvtaobao.tvsdk.utils.SystemProUtils;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.DrawListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.FocusListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.PositionListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.AlphaParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.ScaleParams;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.LinkedList;
import java.util.List;

public abstract class PositionManager {
    protected static final boolean DEBUG = false;
    public static final int FOCUS_ASYNC_DRAW = 1;
    public static final int FOCUS_STATIC_DRAW = 2;
    public static final int FOCUS_SYNC_DRAW = 0;
    public static final int STATE_DRAWING = 1;
    public static final int STATE_IDLE = 0;
    protected static final String TAG = "PositionManager";
    int mAlphaFrame = 1;
    int mAlphaFrameRate = 50;
    private AlphaList mAlphaList = new AlphaList();
    Rect mClipFocusRect = new Rect();
    DrawListener mConvertSelector;
    boolean mCurNodeAdded = false;
    private NodeManager mCurNodeManager = new NodeManager();
    FocusListener mFocus = null;
    int mFocusFrame = 1;
    int mFocusFrameRate = 50;
    int mFocusMode = 2;
    Rect mFocusRect = new Rect();
    protected boolean mForceDrawFocus = false;
    int mFrame = 1;
    FocusListener mLastFocus = null;
    protected ItemListener mLastItem;
    PositionListener mListener;
    private NodeManager mNodeManager = new NodeManager();
    boolean mPause = false;
    int mScaleFrame = 1;
    int mScaleFrameRate = 50;
    private ScaledList mScaledList = new ScaledList();
    DrawListener mSelector;
    protected Interpolator mSelectorPolator = null;
    boolean mStart = true;

    public static PositionManager createPositionManager(int focusMode, PositionListener l) {
        ZpLogger.i(TAG, "createPositionManager focusMode = " + focusMode);
        if (focusMode == 0) {
            SystemProUtils.setGlobalFocusMode(focusMode);
            return new SyncPositionManager(focusMode, l);
        } else if (focusMode == 2) {
            SystemProUtils.setGlobalFocusMode(focusMode);
            return new StaticPositionManager(focusMode, l);
        } else {
            ZpLogger.e(TAG, "createPositionManager focusMode not support");
            return null;
        }
    }

    public boolean isFocusBackground() {
        if (this.mFocus == null) {
            return false;
        }
        return this.mFocus.isFocusBackground();
    }

    public PositionManager(int focusMode, PositionListener l) {
        this.mListener = l;
        this.mFocusMode = focusMode;
    }

    public boolean isFocusStarted() {
        return this.mStart;
    }

    public void focusStart() {
        if (!this.mStart || this.mPause) {
            this.mStart = true;
            this.mPause = false;
            reset();
            this.mListener.postInvalidate();
        }
    }

    public void focusPause() {
        if (!this.mPause) {
            this.mPause = true;
        }
    }

    public void focusStop() {
        ZpLogger.i(TAG, "PositionManager.focusStop");
        if (this.mStart) {
            this.mStart = false;
        }
    }

    public void draw(Canvas canvas) {
        drawOut(canvas);
        if (!this.mStart) {
            drawCurOut(canvas);
        }
    }

    public void drawBeforeFocus(ItemListener item, Canvas canvas) {
        item.drawBeforeFocus(canvas);
    }

    public void drawAfterFocus(ItemListener item, Canvas canvas) {
        item.drawAfterFocus(canvas);
    }

    public void release() {
        ZpLogger.d(TAG, "release");
        this.mScaledList.clear();
        this.mAlphaList.clear();
        this.mNodeManager.clear();
        this.mCurNodeManager.clear();
    }

    /* access modifiers changed from: protected */
    public void drawCurOut(Canvas canvas) {
        this.mCurNodeManager.drawOut(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawOut(Canvas canvas) {
        this.mNodeManager.drawOut(canvas);
    }

    /* access modifiers changed from: protected */
    public void preDrawOut(Canvas canvas) {
        this.mNodeManager.preDrawOut(canvas);
    }

    /* access modifiers changed from: protected */
    public void postDrawOut(Canvas canvas) {
        this.mNodeManager.postDrawOut(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawUnscale(Canvas canvas) {
        this.mScaledList.drawOut(canvas);
    }

    /* access modifiers changed from: protected */
    public void preDrawUnscale(Canvas canvas) {
        this.mScaledList.preDrawOut(canvas);
    }

    public void forceDrawFocus() {
        this.mForceDrawFocus = true;
        if (this.mListener != null) {
            this.mListener.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void postDrawUnscale(Canvas canvas) {
        this.mScaledList.postDrawOut(canvas);
    }

    /* access modifiers changed from: package-private */
    public void drawFocus(Canvas canvas) {
        if (this.mFocus == null || this.mFocusRect.isEmpty() || this.mSelector == null) {
            ZpLogger.w(TAG, "drawFocus mFocus = " + this.mFocus);
            return;
        }
        this.mSelector.setRect(this.mFocusRect);
        if (this.mClipFocusRect.isEmpty() || !clipCanvasIsNeeded(this.mFocusRect, this.mClipFocusRect)) {
            this.mSelector.draw(canvas);
        } else {
            canvas.save();
            canvas.clipRect(this.mClipFocusRect);
            this.mSelector.draw(canvas);
            canvas.restore();
        }
        if (this.mConvertSelector != null) {
            this.mConvertSelector.setRect(this.mFocusRect);
            this.mConvertSelector.draw(canvas);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawFocus(Canvas canvas, Rect rect, float alpha, DrawListener selector, Rect clipRect) {
        if (rect != null && selector != null) {
            if (this.mFocus == null || rect.isEmpty()) {
                ZpLogger.w(TAG, "drawFocus mFocus = " + this.mFocus);
                return;
            }
            selector.setRect(rect);
            selector.setAlpha(alpha);
            if (clipRect.isEmpty() || !clipCanvasIsNeeded(rect, clipRect)) {
                selector.draw(canvas);
                return;
            }
            canvas.save(2);
            canvas.clipRect(clipRect);
            selector.draw(canvas);
            canvas.restore();
        }
    }

    /* access modifiers changed from: package-private */
    public void drawStaticFocus(Canvas canvas, ItemListener item) {
        if (this.mFocus != null) {
            drawStaticFocus(canvas, item, this.mFocus.getParams().getScaleParams().getScaleX(), this.mFocus.getParams().getScaleParams().getScaleY());
        }
    }

    /* access modifiers changed from: package-private */
    public void drawStaticFocus(Canvas canvas, ItemListener item, float scaleX, float scaleY) {
        if (this.mFocus != null) {
            this.mFocusRect.set(getDstRect(scaleX, scaleY, item.isScale()));
            this.mClipFocusRect.set(getClipFocusRect(this.mFocus));
            offsetManualPadding(this.mFocusRect, item);
            drawFocus(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public Rect getFinalRect(ItemListener item) {
        if (this.mFocus == null) {
            return null;
        }
        Rect r = new Rect();
        ScaleParams scaleParams = this.mFocus.getParams().getScaleParams();
        r.set(getDstRect(scaleParams.getScaleX(), scaleParams.getScaleY(), item.isScale()));
        offsetManualPadding(r, item);
        return r;
    }

    public Rect getDstRect(float scaleX, float scaleY, boolean isScale) {
        if (this.mFocus == null) {
            return null;
        }
        FocusRectParams params = this.mFocus.getFocusParams();
        Rect r = new Rect();
        r.set(params.focusRect());
        this.mListener.offsetDescendantRectToItsCoords((View) this.mFocus, r);
        if (!isScale) {
            return r;
        }
        ScalePositionManager.instance().getScaledRect(r, scaleX, scaleY, params.coefX(), params.coefY());
        return r;
    }

    public Rect getClipFocusRect(FocusListener focus) {
        Rect clipRect;
        Rect r = new Rect();
        if (!(focus == null || (clipRect = focus.getClipFocusRect()) == null)) {
            r.set(clipRect);
            this.mListener.offsetDescendantRectToItsCoords((View) focus, r);
        }
        return r;
    }

    public Rect getAlphaListItemRect(ItemListener item, boolean isScale) {
        Rect r = new Rect();
        try {
            FocusRectParams params = item.getFocusParams();
            r = item.getFocusParams().focusRect();
            if (!(item instanceof View)) {
                return null;
            }
            View thisView = (View) item;
            while (!(thisView instanceof FocusPositionManager) && thisView != null) {
                ViewParent parent = thisView.getParent();
                if (parent instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) parent;
                    group.offsetDescendantRectToMyCoords(thisView, r);
                    thisView = group;
                } else if (parent == null) {
                    return null;
                } else {
                    return r;
                }
            }
            if (isScale) {
                ScalePositionManager.instance().getScaledRect(r, item.getScaleX(), item.getScaleY(), params.coefX(), params.coefY());
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelector(DrawListener selector) {
        this.mSelector = selector;
    }

    public void setConvertSelector(DrawListener convertSelector) {
        this.mConvertSelector = convertSelector;
    }

    /* access modifiers changed from: protected */
    public void removeScaleNode(ItemListener item) {
        this.mScaledList.remove(item);
    }

    private void addNode(ItemListener item) {
        if (this.mFocus != null) {
            if (item == null) {
                ZpLogger.w(TAG, "addNode: item is null");
                return;
            }
            this.mNodeManager.release();
            if (SystemProUtils.getGlobalFocusMode() == 2) {
                AlphaParams alphaParams = this.mFocus.getParams().getAlphaParams();
                Interpolator alphaInterpolator = alphaParams.getAlphaInteroplator();
                if (alphaInterpolator == null) {
                    alphaInterpolator = new LinearInterpolator();
                }
                this.mNodeManager.init(this.mAlphaList, new AlphaInfo(item, this.mAlphaFrame, this.mAlphaFrameRate, alphaParams.getToAlpha(), alphaInterpolator, this.mSelector, this.mFocus));
            }
            ScaleParams scaledParams = this.mFocus.getParams().getScaleParams();
            Interpolator scaleInterpolator = scaledParams.getScaleInterpolator();
            if (scaleInterpolator == null) {
                scaleInterpolator = new LinearInterpolator();
            }
            this.mNodeManager.init(this.mScaledList, new ScaledInfo(item, this.mScaleFrame, this.mScaleFrameRate, scaledParams.getScaleX(), scaledParams.getScaleY(), scaleInterpolator));
        }
    }

    /* access modifiers changed from: protected */
    public void addCurNode(ItemListener item) {
        if (this.mFocus != null) {
            if (item == null) {
                ZpLogger.w(TAG, "addCurNode: item is null");
            } else if (this.mCurNodeManager.mList.size() <= 0 || !this.mCurNodeAdded) {
                this.mCurNodeAdded = true;
                this.mCurNodeManager.release();
                ScaledList scaledList = new ScaledList();
                AlphaList alphaList = new AlphaList();
                if (SystemProUtils.getGlobalFocusMode() == 2) {
                    AlphaParams alphaParams = this.mFocus.getParams().getAlphaParams();
                    Interpolator alphaInterpolator = alphaParams.getAlphaInteroplator();
                    if (alphaInterpolator == null) {
                        alphaInterpolator = new LinearInterpolator();
                    }
                    this.mCurNodeManager.init(alphaList, new AlphaInfo(item, this.mAlphaFrame, this.mAlphaFrameRate, alphaParams.getToAlpha(), alphaInterpolator, this.mSelector, this.mFocus));
                }
                ScaleParams scaledParams = this.mFocus.getParams().getScaleParams();
                Interpolator scaleInterpolator = scaledParams.getScaleInterpolator();
                if (scaleInterpolator == null) {
                    scaleInterpolator = new LinearInterpolator();
                }
                this.mCurNodeManager.init(scaledList, new ScaledInfo(item, this.mScaleFrame, this.mScaleFrameRate, scaledParams.getScaleX(), scaledParams.getScaleY(), scaleInterpolator));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeNode(ItemListener item) {
        this.mNodeManager.remove(item);
    }

    private void addScaledNode(ItemListener item) {
        if (item == null) {
            ZpLogger.w(TAG, "addScaledNode: item is null");
        } else if (this.mFocus != null) {
            ScaleParams params = this.mFocus.getParams().getScaleParams();
            Interpolator scaleInterpolator = params.getScaleInterpolator();
            if (scaleInterpolator == null) {
                scaleInterpolator = new LinearInterpolator();
            }
            this.mScaledList.add(new ScaledInfo(item, this.mScaleFrame, this.mScaleFrameRate, params.getScaleX(), params.getScaleY(), scaleInterpolator));
        }
    }

    public void stop() {
        if (this.mFocus != null) {
            resetSelector();
            addNode(this.mFocus.getItem());
        }
    }

    public void clear() {
        this.mFocus = null;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (this.mFocus == null) {
            return false;
        }
        return this.mFocus.preOnKeyDown(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mFocus == null) {
            return false;
        }
        return this.mFocus.onKeyDown(keyCode, event);
    }

    public void reset() {
        this.mFrame = 1;
        this.mScaleFrame = 1;
        this.mFocusFrame = 1;
        this.mAlphaFrame = 1;
        this.mListener.invalidate();
    }

    public void reset(FocusListener focus) {
        ZpLogger.d(TAG, "reset focus = " + focus);
        if (focus != null) {
            this.mLastFocus = this.mFocus;
            this.mFocus = focus;
            if (this.mFocusMode == 2) {
                this.mFocusFrameRate = 0;
            } else {
                this.mFocusFrameRate = this.mFocus.getParams().getFocusParams().getFocusFrameRate();
            }
            this.mScaleFrameRate = this.mFocus.getParams().getScaleParams().getScaleFrameRate();
            this.mAlphaFrameRate = this.mFocus.getParams().getAlphaParams().getAlphaFrameRate();
            reset();
            return;
        }
        ZpLogger.e(TAG, "reset focus is null");
    }

    /* access modifiers changed from: package-private */
    public void offsetManualPadding(Rect r, ItemListener item) {
        Rect rPadding = item.getManualPadding();
        if (rPadding != null) {
            r.left += rPadding.left;
            r.right += rPadding.right;
            r.top += rPadding.top;
            r.bottom += rPadding.bottom;
        }
    }

    public boolean isFinished() {
        return this.mFrame > Math.max(this.mFocusFrameRate, this.mScaleFrameRate);
    }

    public boolean canDrawNext() {
        return true;
    }

    public int getFrame() {
        return this.mFrame;
    }

    public int getTotalFrame() {
        return Math.max(this.mFocusFrameRate, this.mScaleFrameRate);
    }

    public int getFocusFrameRate() {
        return this.mFocusFrameRate;
    }

    public int getCurFocusFrame() {
        return this.mFocusFrame;
    }

    public void resetSelector() {
    }

    public DrawListener getSelector() {
        return this.mSelector;
    }

    public DrawListener getConvertSelector() {
        return this.mConvertSelector;
    }

    class NodeManager {
        List<BaseList> mList = new LinkedList();

        NodeManager() {
        }

        public void init(BaseList list, Info info) {
            synchronized (this) {
                this.mList.add(list);
                list.add(info);
            }
            PositionManager.this.mListener.invalidate();
        }

        public void clear() {
            synchronized (this) {
                for (int index = 0; index < this.mList.size(); index++) {
                    this.mList.get(index).clear();
                }
                this.mList.clear();
            }
        }

        public void release() {
            synchronized (this) {
                this.mList.clear();
            }
        }

        public void remove(ItemListener item) {
            for (int index = 0; index < this.mList.size(); index++) {
                this.mList.get(index).remove(item);
            }
        }

        public void preDrawOut(Canvas canvas) {
            for (int index = 0; index < this.mList.size(); index++) {
                this.mList.get(index).preDrawOut(canvas);
            }
        }

        public void drawOut(Canvas canvas) {
            for (int index = 0; index < this.mList.size(); index++) {
                this.mList.get(index).drawOut(canvas);
            }
        }

        public void postDrawOut(Canvas canvas) {
            for (int index = 0; index < this.mList.size(); index++) {
                this.mList.get(index).postDrawOut(canvas);
            }
        }
    }

    class ScaledList extends BaseList {
        ScaledList() {
            super();
        }

        /* access modifiers changed from: protected */
        public void drawOut(Info info, Canvas canvas) {
            if (info.getFrame() > 0) {
                if (info instanceof ScaledInfo) {
                    ScaledInfo scaledInfo = (ScaledInfo) info;
                    float itemDiffScaleXValue = scaledInfo.getScaleX() - 1.0f;
                    float itemDiffScaleYValue = scaledInfo.getScaleY() - 1.0f;
                    float coef = scaledInfo.getInterpolator().getInterpolation(((float) (scaledInfo.getFrame() - 1)) / ((float) scaledInfo.getFrameRate()));
                    scaledInfo.getItem().setScaleX(1.0f + (itemDiffScaleXValue * coef));
                    scaledInfo.getItem().setScaleY(1.0f + (itemDiffScaleYValue * coef));
                    scaledInfo.subFrame();
                    return;
                }
                info.subFrame();
            }
        }
    }

    class AlphaList extends BaseList {
        AlphaList() {
            super();
        }

        /* access modifiers changed from: protected */
        public void drawOut(Info info, Canvas canvas) {
            if (info.getFrame() > 0) {
                if (info instanceof AlphaInfo) {
                    AlphaInfo alphaInfo = (AlphaInfo) info;
                    float dstAlpha = alphaInfo.getAlpha() * alphaInfo.getInterpolator().getInterpolation(((float) (alphaInfo.getFrame() - 1)) / ((float) alphaInfo.getFrameRate()));
                    Rect rect = PositionManager.this.getAlphaListItemRect(info.getItem(), true);
                    if (rect != null) {
                        PositionManager.this.offsetManualPadding(rect, info.getItem());
                    }
                    Rect clipFocusRect = PositionManager.this.getClipFocusRect(alphaInfo.mLastFocus);
                    if (PositionManager.this.mLastItem != info.mItem) {
                        PositionManager.this.drawFocus(canvas, rect, dstAlpha, alphaInfo.mSelector, clipFocusRect);
                    }
                    info.subFrame();
                    return;
                }
                info.subFrame();
            }
        }
    }

    private abstract class BaseList {
        Object lock;
        boolean mIsBreak;
        List<Info> mList;

        /* access modifiers changed from: protected */
        public abstract void drawOut(Info info, Canvas canvas);

        private BaseList() {
            this.mList = new LinkedList();
            this.lock = new Object();
            this.mIsBreak = false;
        }

        public void add(Info info) {
            setBreak(true);
            synchronized (this) {
                this.mList.add(info);
            }
            PositionManager.this.mListener.invalidate();
        }

        public void clear() {
            synchronized (this) {
                this.mList.clear();
            }
        }

        public void remove(ItemListener item) {
            for (int index = 0; index < this.mList.size(); index++) {
                if (this.mList.get(index).getItem() == item) {
                    this.mList.remove(index);
                    return;
                }
            }
        }

        public void preDrawOut(Canvas canvas) {
            for (int index = 0; index < this.mList.size(); index++) {
                Info info = this.mList.get(index);
                if (info.getFrame() >= 0) {
                    PositionManager.this.drawBeforeFocus(info.getItem(), canvas);
                }
            }
        }

        public void drawOut(Canvas canvas) {
            setBreak(false);
            synchronized (this) {
                while (this.mList.size() > 0 && !isBreak() && this.mList.get(0).isFinished()) {
                    this.mList.remove(0);
                }
                for (int index = 0; index < this.mList.size() && !isBreak(); index++) {
                    drawOut(this.mList.get(index), canvas);
                }
                if (this.mList.size() > 0) {
                    PositionManager.this.mListener.invalidate();
                }
            }
        }

        public void postDrawOut(Canvas canvas) {
            for (int index = 0; index < this.mList.size(); index++) {
                Info info = this.mList.get(index);
                if (info.getFrame() >= 0) {
                    PositionManager.this.drawAfterFocus(info.getItem(), canvas);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void setBreak(boolean isBreak) {
            synchronized (this.lock) {
                this.mIsBreak = isBreak;
            }
        }

        /* access modifiers changed from: protected */
        public boolean isBreak() {
            boolean z;
            synchronized (this.lock) {
                z = this.mIsBreak;
            }
            return z;
        }
    }

    private abstract class Info {
        protected int mFrame;
        protected int mFrameRate;
        protected Interpolator mInterpolator;
        protected ItemListener mItem;

        private Info() {
        }

        public boolean isFinished() {
            return getFrame() <= 0 && this.mItem.isFinished();
        }

        public ItemListener getItem() {
            return this.mItem;
        }

        public void subFrame() {
            this.mFrame--;
        }

        public int getFrame() {
            return this.mFrame;
        }

        public int getFrameRate() {
            return this.mFrameRate;
        }

        public Interpolator getInterpolator() {
            return this.mInterpolator;
        }
    }

    class AlphaInfo extends Info {
        float mAlpha;
        FocusListener mLastFocus = null;
        DrawListener mSelector;

        public AlphaInfo(ItemListener item, int frame, int frameRate, float alpha, Interpolator alphaInterpolator, DrawListener selector, FocusListener lastFocus) {
            super();
            this.mItem = item;
            AlphaParams alphaParams = PositionManager.this.mFocus.getParams().getAlphaParams();
            float fromAlpha = alphaParams.getFromAlpha();
            if (alphaParams.getToAlpha() > 1.0f || fromAlpha < 0.0f) {
                this.mFrame = -1;
            } else {
                this.mFrame = frame - 1;
            }
            this.mFrameRate = frameRate;
            this.mAlpha = alpha;
            this.mInterpolator = alphaInterpolator;
            this.mSelector = selector;
            this.mLastFocus = lastFocus;
        }

        public void setAlpha(float dstAlpha) {
            this.mAlpha = dstAlpha;
        }

        public float getAlpha() {
            return this.mAlpha;
        }
    }

    class ScaledInfo extends Info {
        float mScaleX;
        float mScaleY;

        public ScaledInfo() {
            super();
        }

        public ScaledInfo(ItemListener item, int frame, int frameRate, float scaleX, float scaleY, Interpolator scaleInterpolator) {
            super();
            this.mItem = item;
            if (item.getScaleX() > 1.0f || item.getScaleY() > 1.0f) {
                this.mFrame = frame - 1;
            } else {
                this.mFrame = -1;
            }
            this.mFrameRate = frameRate;
            this.mScaleX = scaleX;
            this.mScaleY = scaleY;
            this.mInterpolator = scaleInterpolator;
        }

        public float getScaleX() {
            return this.mScaleX;
        }

        public float getScaleY() {
            return this.mScaleY;
        }
    }

    public void setSelectorInterpolator(Interpolator selectorPolator) {
        this.mSelectorPolator = selectorPolator;
    }

    public Interpolator getSelectorInterpolator() {
        return this.mSelectorPolator;
    }

    public boolean clipCanvasIsNeeded(Rect focusRect, Rect clipRect) {
        if (SystemProUtils.getGlobalFocusMode() != 2) {
            return false;
        }
        if (focusRect.left < clipRect.left || focusRect.right > clipRect.right || focusRect.top < clipRect.top || focusRect.bottom > clipRect.bottom) {
            return true;
        }
        return false;
    }
}
