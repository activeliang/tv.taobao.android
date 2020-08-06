package com.taobao.taobaoavsdk.widget.extra.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.InputDeviceCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DanmakuView extends View {
    private static final int STATUS_HIDDEN = 4;
    private static final int STATUS_PAUSE = 2;
    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_STOP = 3;
    public static final String TAG = "DanmakuView";
    private static final float mPartition = 0.95f;
    private static Random random = new Random();
    private Paint fpsPaint;
    private LinkedList<Float> lines;
    private HashMap<Integer, ArrayList<IDanmakuItem>> mChannelMap;
    private int[] mChannelY;
    private Context mDWContext;
    /* access modifiers changed from: private */
    public final List<List<IDanmakuItem>> mDanmuList;
    private float mEndYOffset = 0.9f;
    private int mMaxRow = 1;
    private int mMaxRunningPerRow = 1;
    /* access modifiers changed from: private */
    public int mPickItemInterval = 100;
    private int mSelfIndex = -1;
    private boolean mShowDebug = false;
    private float mStartYOffset = 0.0f;
    private IDWDanmakuTimelineAdapter mTimelineAdapter;
    private long previousTime = 0;
    private volatile int status = 3;
    private LinkedList<Long> times;

    public DanmakuView(Context context, IDWDanmakuTimelineAdapter adapter) {
        super(context);
        this.mDWContext = context;
        this.mTimelineAdapter = adapter;
        int videoTime = this.mTimelineAdapter.getTotalTime();
        this.mDanmuList = new ArrayList(videoTime / this.mPickItemInterval);
        for (int i = 0; i < videoTime / this.mPickItemInterval; i++) {
            this.mDanmuList.add(new ArrayList());
        }
        if (videoTime % this.mPickItemInterval > 0) {
            this.mDanmuList.add(new ArrayList());
        }
    }

    private void checkYOffset(float start, float end) {
        if (start >= end) {
            throw new IllegalArgumentException("start_Y_offset must < end_Y_offset");
        } else if (start < 0.0f || start >= 1.0f || end < 0.0f || end > 1.0f) {
            throw new IllegalArgumentException("start_Y_offset and end_Y_offset must between 0 and 1)");
        }
    }

    public void init() {
        setBackgroundColor(0);
        setDrawingCacheBackgroundColor(0);
        calculation();
    }

    private synchronized void calculation() {
        if (this.mShowDebug) {
            this.fpsPaint = new TextPaint(1);
            this.fpsPaint.setColor(InputDeviceCompat.SOURCE_ANY);
            this.fpsPaint.setTextSize(20.0f);
            this.times = new LinkedList<>();
            this.lines = new LinkedList<>();
        }
        initChannelMap();
        initChannelY();
    }

    private void initChannelMap() {
        this.mChannelMap = new HashMap<>(this.mMaxRow);
        for (int i = 0; i < this.mMaxRow; i++) {
            this.mChannelMap.put(Integer.valueOf(i), new ArrayList<>(this.mMaxRunningPerRow));
        }
    }

    private void initChannelY() {
        this.mChannelY = new int[this.mMaxRow];
        float rowHeight = (((float) getHeight()) * (this.mEndYOffset - this.mStartYOffset)) / ((float) this.mMaxRow);
        float baseOffset = ((float) getHeight()) * this.mStartYOffset;
        for (int i = 0; i < this.mMaxRow; i++) {
            this.mChannelY[i] = (int) (((((float) (i + 1)) * rowHeight) + baseOffset) - ((3.0f * rowHeight) / 4.0f));
        }
        if (this.mShowDebug) {
            this.lines.add(Float.valueOf(baseOffset));
            for (int i2 = 0; i2 < this.mMaxRow; i2++) {
                this.lines.add(Float.valueOf((((float) (i2 + 1)) * rowHeight) + baseOffset));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int indexY;
        if (this.status != 4) {
            super.onDraw(canvas);
            try {
                canvas.drawColor(0);
                for (int i = 0; i < this.mChannelMap.size(); i++) {
                    Iterator<IDanmakuItem> it = this.mChannelMap.get(Integer.valueOf(i)).iterator();
                    while (it.hasNext()) {
                        IDanmakuItem item = it.next();
                        if (item.isOut()) {
                            it.remove();
                            item.addDrawingList(false);
                        } else {
                            item.doDraw(canvas, this.status == 2);
                        }
                    }
                }
                if (System.currentTimeMillis() - this.previousTime > ((long) this.mPickItemInterval)) {
                    this.previousTime = System.currentTimeMillis();
                    List<IDanmakuItem> currentList = null;
                    if (this.mSelfIndex >= 0) {
                        currentList = this.mDanmuList.get(this.mSelfIndex);
                        this.mSelfIndex = -1;
                    } else if (this.mTimelineAdapter.getVideoStatus()) {
                        currentList = this.mDanmuList.get(((int) ((long) this.mTimelineAdapter.getCurrentTime())) / this.mPickItemInterval);
                    }
                    if (currentList != null && currentList.size() > 0) {
                        int count = currentList.size();
                        for (int i2 = 0; i2 < count; i2++) {
                            IDanmakuItem di = currentList.get(i2);
                            if (!di.drawing() && (indexY = findVacant(di)) >= 0 && !this.mChannelMap.get(Integer.valueOf(indexY)).contains(di)) {
                                di.setStartPosition(canvas.getWidth() - 2, this.mChannelY[indexY]);
                                di.doDraw(canvas, this.status == 2);
                                this.mChannelMap.get(Integer.valueOf(indexY)).add(di);
                                di.addDrawingList(true);
                            }
                        }
                    }
                }
                if (this.mShowDebug) {
                    canvas.drawText("FPS:" + ((int) fps()), 5.0f, 20.0f, this.fpsPaint);
                    Iterator it2 = this.lines.iterator();
                    while (it2.hasNext()) {
                        float yp = ((Float) it2.next()).floatValue();
                        canvas.drawLine(0.0f, yp, (float) getWidth(), yp, this.fpsPaint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 16) {
                postInvalidateOnAnimation(getLeft(), getTop(), getRight(), getBottom());
            } else {
                postInvalidate(getLeft(), getTop(), getRight(), getBottom());
            }
        }
    }

    public void seek(int seekTime) {
    }

    private int findVacant(IDanmakuItem item) {
        int i = 0;
        while (i < this.mMaxRow) {
            try {
                if (this.mChannelMap.get(Integer.valueOf(i)).size() == 0) {
                    return i;
                }
                i++;
            } catch (Exception e) {
                Log.w(TAG, "findVacant,Exception:" + e.toString());
            }
        }
        int ind = random.nextInt(this.mMaxRow);
        for (int i2 = 0; i2 < this.mMaxRow; i2++) {
            ArrayList<IDanmakuItem> list = this.mChannelMap.get(Integer.valueOf((i2 + ind) % this.mMaxRow));
            if (list.size() <= this.mMaxRunningPerRow && !item.willHit(list.get(list.size() - 1))) {
                return (i2 + ind) % this.mMaxRow;
            }
        }
        return -1;
    }

    private void clearPlayingItems() {
        if (this.mChannelMap != null) {
            synchronized (this.mChannelMap) {
                for (int i = 0; i < this.mChannelMap.size(); i++) {
                    ArrayList<IDanmakuItem> list = this.mChannelMap.get(Integer.valueOf(i));
                    if (list != null) {
                        list.clear();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initChannelY();
    }

    public boolean isPaused() {
        return 2 == this.status;
    }

    public void pause() {
        this.status = 2;
        invalidate(getLeft(), getTop(), getRight(), getBottom());
    }

    public void start() {
        this.status = 1;
        invalidate(getLeft(), getTop(), getRight(), getBottom());
    }

    public void show() {
        this.status = 1;
        invalidate(getLeft(), getTop(), getRight(), getBottom());
    }

    public void hide() {
        this.status = 4;
        invalidate(getLeft(), getTop(), getRight(), getBottom());
    }

    private void clearRunning() {
        if (this.mChannelMap != null && !this.mChannelMap.isEmpty()) {
            this.mChannelMap.clear();
        }
    }

    public void setShowDebug(boolean showDebug) {
        this.mShowDebug = showDebug;
    }

    public void setMaxRow(int maxRow) {
        this.mMaxRow = maxRow;
        clearRunning();
    }

    public void setPickItemInterval(int pickItemInterval) {
        this.mPickItemInterval = pickItemInterval;
    }

    public void setMaxRunningPerRow(int maxRunningPerRow) {
        this.mMaxRunningPerRow = maxRunningPerRow;
    }

    public void setStartYOffset(float startYOffset, float endYOffset) {
        checkYOffset(startYOffset, endYOffset);
        clearRunning();
        this.mStartYOffset = startYOffset;
        this.mEndYOffset = endYOffset;
    }

    public void addItem(IDanmakuItem item) {
        synchronized (this.mDanmuList) {
            int index = ((int) item.showTime()) / this.mPickItemInterval;
            if (index < this.mDanmuList.size()) {
                this.mDanmuList.get(index).add(item);
            }
        }
    }

    public void addItemToHead(IDanmakuItem item) {
        synchronized (this.mDanmuList) {
            int index = ((int) item.showTime()) / this.mPickItemInterval;
            if (index < this.mDanmuList.size() && index >= 0) {
                this.mDanmuList.get(index).add(0, item);
                this.mSelfIndex = index;
            }
        }
    }

    public void addItem(final List<IDanmakuItem> list, boolean backgroundLoad) {
        if (list != null && list.size() != 0) {
            if (backgroundLoad) {
                new Thread() {
                    public void run() {
                        synchronized (DanmakuView.this.mDanmuList) {
                            int size = list.size();
                            int totalSize = DanmakuView.this.mDanmuList.size();
                            for (int i = 0; i < size; i++) {
                                IDanmakuItem danmakuItem = (IDanmakuItem) list.get(i);
                                int index = ((int) danmakuItem.showTime()) / DanmakuView.this.mPickItemInterval;
                                if (index < totalSize) {
                                    ((List) DanmakuView.this.mDanmuList.get(index)).add(danmakuItem);
                                }
                            }
                        }
                        DanmakuView.this.postInvalidate();
                    }
                }.start();
                return;
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                IDanmakuItem danmakuItem = list.get(i);
                int index = ((int) danmakuItem.showTime()) / this.mPickItemInterval;
                if (index < this.mDanmuList.size()) {
                    this.mDanmuList.get(index).add(danmakuItem);
                }
            }
        }
    }

    public void destroy() {
        this.mDanmuList.clear();
    }

    private double fps() {
        long lastTime = System.nanoTime();
        this.times.addLast(Long.valueOf(lastTime));
        double difference = ((double) (lastTime - this.times.getFirst().longValue())) / 1.0E9d;
        if (this.times.size() > 100) {
            this.times.removeFirst();
        }
        return difference > ClientTraceData.b.f47a ? ((double) this.times.size()) / difference : ClientTraceData.b.f47a;
    }
}
