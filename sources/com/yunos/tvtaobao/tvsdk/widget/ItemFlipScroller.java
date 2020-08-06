package com.yunos.tvtaobao.tvsdk.widget;

import android.util.SparseArray;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tvtaobao.tvsdk.widget.interpolator.AccelerateDecelerateFrameInterpolator;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemFlipScroller {
    /* access modifiers changed from: private */
    public boolean DEBUG = false;
    private final String TAG = "ItemFlipScroller";
    private int flip_scroll_frame_count = 20;
    private int hor_delay_distance = 50;
    private int hor_delay_frame_count = 2;
    private AccelerateDecelerateFrameInterpolator mAccelerateDecelerateFrameInterpolator = new AccelerateDecelerateFrameInterpolator();
    private int mColumnCount;
    private int mCurrFrameIndex;
    private int mCurrSelectedPosition = -1;
    private int mEndListIndex = -1;
    private boolean mFastFlip;
    private FastStep mFastStep = new FastStep();
    private int mFinalFrameCount;
    private boolean mFinished = true;
    private List<List<FlipItem>> mFlipItemColumnList = new ArrayList();
    private SparseArray<FlipItem> mFlipItemMap = new SparseArray<>();
    private List<Integer> mFooterViewList;
    private List<Integer> mHeaderViewList;
    private boolean mIsDown;
    private boolean mIsFastStepComeDown = true;
    private boolean mItemDelayAnim = true;
    private ItemFlipScrollerListener mItemFlipScrollerListener;
    private int mPreAddTempItemIndex = -1;
    private int mPreSelectedPosition = -1;
    private int mStartListIndex = -1;
    private boolean mStartingFlipScroll;
    private int mTotalItemCount;
    /* access modifiers changed from: private */
    public int min_fast_setp_discance = 16;
    private int ver_delay_distance = 25;
    private int ver_delay_frame_count = 1;

    private enum FlipItemFastStatus {
        UNSTART,
        START_UNSTOP,
        STOP
    }

    private enum FlipItemPositionType {
        HEATER,
        ADAPTER,
        FOOTER
    }

    private enum FlipListChangeType {
        INIT,
        ADD_BEFORE,
        ADD_AFTER,
        TEMP,
        REFRESH,
        UNKNOWN
    }

    public interface ItemFlipScrollerListener {
        void onFinished();

        void onOffsetNewChild(int i, int i2, int i3);
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    public void setDelayAnim(boolean delay) {
        if (this.mFinished) {
            this.mItemDelayAnim = delay;
        } else {
            ZpLogger.e("ItemFlipScroller", "setDelayAnim must in Finished use");
        }
    }

    public void setStartingFlipScroll() {
        this.mStartingFlipScroll = true;
    }

    public void setItemFlipScrollerListener(ItemFlipScrollerListener listener) {
        this.mItemFlipScrollerListener = listener;
    }

    public void setSelectedPosition(int selectedPosition) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "setSelectedPosition selectedPosition=" + selectedPosition + " mPreSelectedPosition=" + this.mPreSelectedPosition);
        }
        this.mPreSelectedPosition = this.mCurrSelectedPosition;
        this.mCurrSelectedPosition = selectedPosition;
    }

    public void setHeaderViewInfo(List<Integer> headerInfo) {
        this.mHeaderViewList = headerInfo;
    }

    public void setFooterViewInfo(List<Integer> footerInfo) {
        this.mFooterViewList = footerInfo;
    }

    public void setTotalItemCount(int totalCount) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "setTotalItemCount totalCount=" + totalCount);
        }
        this.mTotalItemCount = totalCount;
    }

    public void startComeDown() {
        if (this.mFastFlip && this.mIsFastStepComeDown) {
            if (this.mIsDown) {
                FlipItem item = getFlipItemColumn(0, 0);
                if (item != null) {
                    int distance = 0;
                    if (this.mItemDelayAnim) {
                        int firstRowStart = getRowStart(item.mIndex);
                        int selectedRowStart = getRowStart(this.mCurrSelectedPosition);
                        int otherColumn = 0;
                        int headerCount = this.mHeaderViewList.size();
                        if (headerCount > 0 && firstRowStart < headerCount) {
                            int totalCount = Math.min(headerCount - 1, selectedRowStart);
                            for (int i = firstRowStart; i <= totalCount; i++) {
                                otherColumn = (this.mHeaderViewList.get(i).intValue() >> 16) - 1;
                            }
                        }
                        int footerCount = this.mFooterViewList.size();
                        if (footerCount > 0 && selectedRowStart >= this.mTotalItemCount - footerCount) {
                            int totalCount2 = Math.max(this.mTotalItemCount - footerCount, firstRowStart);
                            for (int i2 = selectedRowStart; i2 >= totalCount2; i2--) {
                                otherColumn = (this.mFooterViewList.get(i2).intValue() >> 16) - 1;
                            }
                        }
                        distance = (((Math.abs(((selectedRowStart - firstRowStart) / this.mColumnCount) + otherColumn) + 1) * this.hor_delay_distance) + ((this.mColumnCount - 1) * this.ver_delay_distance)) * 2;
                    }
                    this.mFastStep.resetComeDown((item.mFinalDistance - item.mLastDistance) - distance);
                    return;
                }
                return;
            }
            FlipItem item2 = getLastColumnFirsterItem();
            if (item2 != null) {
                int distance2 = 0;
                if (this.mItemDelayAnim) {
                    int lastRowStart = getRowStart(item2.mIndex);
                    int selectedRowStart2 = getRowStart(this.mCurrSelectedPosition);
                    int otherColumn2 = 0;
                    int headerCount2 = this.mHeaderViewList.size();
                    if (headerCount2 > 0 && selectedRowStart2 < headerCount2) {
                        int totalCount3 = Math.min(headerCount2 - 1, selectedRowStart2);
                        for (int i3 = selectedRowStart2; i3 <= totalCount3; i3++) {
                            otherColumn2 = (this.mHeaderViewList.get(i3).intValue() >> 16) - 1;
                        }
                    }
                    int footerCount2 = this.mFooterViewList.size();
                    if (footerCount2 > 0 && lastRowStart >= this.mTotalItemCount - footerCount2) {
                        int totalCount4 = Math.max(this.mTotalItemCount - footerCount2, selectedRowStart2);
                        for (int i4 = lastRowStart; i4 >= totalCount4; i4--) {
                            otherColumn2 = (this.mFooterViewList.get(i4).intValue() >> 16) - 1;
                        }
                    }
                    distance2 = (((lastRowStart - selectedRowStart2) / this.mColumnCount) + otherColumn2 + 1) * this.hor_delay_distance * 2;
                }
                this.mFastStep.resetComeDown((item2.mFinalDistance - item2.mLastDistance) + distance2);
            }
        }
    }

    public void setFastScrollOffset(int index, int secondIndex, int offset) {
        FlipItem item;
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "setFastScrollOffset index=" + index + " secondIndex=" + secondIndex + " offset=" + offset + " mStartingFlipScroll=" + this.mStartingFlipScroll);
        }
        if (this.mStartingFlipScroll && (item = this.mFlipItemMap.get(getFlipItemMapKey(index, secondIndex))) != null) {
            item.mFastScrollOffset = offset;
        }
    }

    public int getFlipItemLeftMoveDistance(int index, int secondIndex) {
        FlipItem item = getFlipItem(index, secondIndex);
        if (item == null) {
            return 0;
        }
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "getFlipItemLeftMoveDistance mFinalDistance=" + item.mFinalDistance + " mLastDistance=" + item.mLastDistance);
        }
        return item.mFinalDistance - item.mLastDistance;
    }

    public int getFlipColumnFirstItemLeftMoveDistance(int index) {
        if (index > this.mEndListIndex) {
            index = this.mEndListIndex;
        } else if (index < this.mStartListIndex) {
            index = this.mStartListIndex;
        }
        int secondIndex = 0;
        if (this.mHeaderViewList.size() > 0 && index < this.mHeaderViewList.size()) {
            secondIndex = -1;
        } else if (this.mFooterViewList.size() > 0 && index >= this.mTotalItemCount - this.mFooterViewList.size()) {
            secondIndex = -1;
        }
        FlipItem item = getFlipItem(index, secondIndex);
        if (item == null) {
            return 0;
        }
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "getFlipColumnFirstItemLeftMoveDistance mFinalDistance=" + item.mFinalDistance + " mLastDistance=" + item.mLastDistance);
        }
        if (getFlipItemColumn(item.mColumnIndex, 0) != null) {
            return item.mFinalDistance - item.mLastDistance;
        }
        return 0;
    }

    public float getFlipItemMoveRatio(int index, int secondIndex) {
        FlipItem item = getFlipItem(index, secondIndex);
        if (item != null) {
            return ((float) item.mLastDistance) / ((float) item.mFinalDistance);
        }
        return 1.0f;
    }

    public int getFastFlipStep() {
        return this.mFastStep.getCurrStep();
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    public void clearChild() {
        this.mFlipItemColumnList.clear();
        this.mFlipItemMap.clear();
        this.mStartListIndex = -1;
        this.mEndListIndex = -1;
    }

    public boolean isDown() {
        return this.mIsDown;
    }

    public void startScroll(int distance, int frameCount, boolean computerRealDistance) {
        if (this.mColumnCount <= 0) {
            ZpLogger.e("ItemFlipScroller", "error must set mColumnCount before start scroll");
        }
        this.mStartingFlipScroll = false;
        this.mCurrFrameIndex = 0;
        this.mFinalFrameCount = frameCount;
        if (distance < 0) {
            this.mIsDown = true;
        } else {
            this.mIsDown = false;
        }
        if (!this.mFinished) {
            this.mFastFlip = true;
        }
        int realDistance = distance;
        if (computerRealDistance) {
            realDistance = getRealFinalDistance(distance);
        }
        resetItemData(realDistance);
        this.mFinished = false;
        ZpLogger.i("ItemFlipScroller", "startScroll distance=" + distance + " frameCount=" + frameCount + " mFastFlip=" + this.mFastFlip);
    }

    public void startComputDistanceScroll(int distance, int frameCount) {
        startScroll(distance, frameCount, true);
    }

    public void startComputDistanceScroll(int distance) {
        startScroll(distance, this.flip_scroll_frame_count, true);
    }

    public void startRealScroll(int distance, int frameCount) {
        startScroll(distance, frameCount, false);
    }

    public void startRealScroll(int distance) {
        startScroll(distance, this.flip_scroll_frame_count, false);
    }

    public void finish() {
        this.mPreSelectedPosition = -1;
        this.mStartingFlipScroll = false;
        this.mFinalFrameCount = 0;
        if (this.mFastFlip) {
            this.mFastStep.finished();
        }
        this.mFastFlip = false;
        clearChild();
        this.mFinished = true;
        if (this.mItemFlipScrollerListener != null) {
            this.mItemFlipScrollerListener.onFinished();
        }
        ZpLogger.i("ItemFlipScroller", CommonData.KEY_FINISHED);
    }

    public boolean computeScrollOffset() {
        boolean finished;
        if (this.mFinished) {
            return false;
        }
        if (this.mFastFlip) {
            boolean unused = this.mFastStep.computerOffset();
        }
        if (this.mIsDown) {
            finished = computerFlipScrollDown();
        } else {
            finished = computerFlipScrollUp();
        }
        if (!finished) {
            return true;
        }
        finish();
        return false;
    }

    public int getCurrDelta(int index, int secondIndex) {
        FlipItem item;
        if (!this.mFinished && (item = this.mFlipItemMap.get(getFlipItemMapKey(index, secondIndex))) != null) {
            return item.mCurrDelta;
        }
        return 0;
    }

    public void addGridView(int start, int end, boolean isDown) {
        if (start <= end) {
            if (this.mStartListIndex < 0 || this.mEndListIndex < 0) {
                makeInitFlipItemList(start, end, isDown);
                this.mStartListIndex = start;
                this.mEndListIndex = end;
                return;
            }
            if (start < this.mStartListIndex) {
                addBefore(start);
            } else if (start > this.mStartListIndex && this.mFastFlip) {
                int diff = start - this.mStartListIndex;
                if ((this.mStartListIndex < this.mHeaderViewList.size() || diff % this.mColumnCount == 0) && 0 == 0) {
                    if (this.DEBUG) {
                        ZpLogger.i("ItemFlipScroller", "clear start=" + start + " mEndListIndex=" + this.mEndListIndex);
                    }
                    this.mFlipItemColumnList.clear();
                    makeInitFlipItemList(start, this.mEndListIndex, this.mIsDown);
                    this.mStartListIndex = start;
                }
            }
            if (end > this.mEndListIndex) {
                addAfter(end);
            } else if (end < this.mEndListIndex && this.mFastFlip) {
                int headerSize = this.mHeaderViewList.size();
                int adapter = headerSize;
                if (end < headerSize) {
                    return;
                }
                if ((this.mEndListIndex >= this.mTotalItemCount - this.mFooterViewList.size() || ((end - adapter) + 1) % this.mColumnCount == 0) && 0 == 0) {
                    if (this.DEBUG) {
                        ZpLogger.i("ItemFlipScroller", "clear mStartListIndex=" + this.mStartListIndex + " end=" + end + " mEndListIndex=" + this.mEndListIndex + " mTotalItemCount=" + this.mTotalItemCount);
                    }
                    this.mFlipItemColumnList.clear();
                    makeInitFlipItemList(this.mStartListIndex, end, this.mIsDown);
                    this.mEndListIndex = end;
                }
            }
        }
    }

    public void checkAddView(int start, int end) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "checkAddView start=" + start + " end=" + end + " mFinished=" + this.mFinished + " mIsDown=" + this.mIsDown);
        }
        if (!this.mFinished) {
            if (start < this.mStartListIndex && !this.mIsDown) {
                addBefore(start);
            }
            if (end > this.mEndListIndex && this.mIsDown) {
                addAfter(end);
            }
        }
    }

    private int getCurrDistance(int distance, float input, boolean needInterpolation) {
        float output;
        if (needInterpolation) {
            output = getInterpolation(input);
        } else {
            output = input;
        }
        return (int) (((float) distance) * output);
    }

    private float getInterpolation(float input) {
        return this.mAccelerateDecelerateFrameInterpolator.getInterpolation(input);
    }

    private boolean computerFlipScrollUp() {
        this.mCurrFrameIndex++;
        if (this.mFastFlip) {
            return computerUpFast();
        }
        return computerUpNormal();
    }

    private boolean computerUpNormal() {
        int horCount = 0;
        boolean finished = true;
        for (int i = this.mFlipItemColumnList.size() - 1; i >= 0; i--) {
            List<FlipItem> itemList = this.mFlipItemColumnList.get(i);
            if (itemList == null) {
                break;
            }
            int verCount = 0;
            for (int j = itemList.size() - 1; j >= 0; j--) {
                FlipItem item = itemList.get(j);
                int delayFrameCount = (this.hor_delay_frame_count * horCount) + (this.ver_delay_frame_count * verCount);
                int itemCurrFrameCount = this.mCurrFrameIndex;
                if (this.mItemDelayAnim) {
                    itemCurrFrameCount -= delayFrameCount;
                }
                if (itemCurrFrameCount > 0) {
                    if (itemCurrFrameCount >= item.mFinalFrameCount) {
                        if (item.mLastDistance > 0 && item.mLastDistance < item.mFinalDistance) {
                            finished = false;
                        } else if (item.mLastDistance < 0 && item.mLastDistance > item.mFinalDistance) {
                            finished = false;
                        }
                        item.mCurrDelta = item.mFinalDistance - item.mLastDistance;
                        item.mLastDistance += item.mCurrDelta;
                        item.mCurrFrameCount = item.mFinalFrameCount;
                        item.mTotalMoveDistance += item.mCurrDelta;
                    } else {
                        item.mCurrDelta = getCurrDistance(item.mFinalDistance, ((float) itemCurrFrameCount) / ((float) item.mFinalFrameCount), true) - item.mLastDistance;
                        if (item.mCurrDelta < 0) {
                            item.mCurrDelta = 0;
                        }
                        item.mLastDistance += item.mCurrDelta;
                        item.mCurrFrameCount = itemCurrFrameCount;
                        item.mTotalMoveDistance += item.mCurrDelta;
                        finished = false;
                    }
                    verCount++;
                    if (this.mStartingFlipScroll) {
                        item.mFastScrollStartingOffset += item.mCurrDelta;
                    }
                }
            }
            horCount++;
        }
        return finished;
    }

    private boolean computerUpFast() {
        boolean needDelay;
        FlipItemFastStatus preVertiaclItemStatus;
        FlipItemFastStatus preVertiaclItemStatus2;
        boolean finished = true;
        if (this.mItemDelayAnim) {
            needDelay = true;
        } else {
            needDelay = false;
        }
        int preColumnDistance = 0;
        int preColumnDelta = 0;
        FlipItemFastStatus preColumnItemStatus = FlipItemFastStatus.UNSTART;
        FlipItemFastStatus flipItemFastStatus = FlipItemFastStatus.UNSTART;
        int listSize = this.mFlipItemColumnList.size();
        int columnIndex = listSize - 1;
        while (true) {
            if (columnIndex >= 0) {
                List<FlipItem> itemList = this.mFlipItemColumnList.get(columnIndex);
                if (itemList == null || itemList.size() <= 0) {
                    break;
                }
                FlipItem columnLastItem = itemList.get(itemList.size() - 1);
                columnLastItem.mCurrDelta = 0;
                if (columnIndex == listSize - 1) {
                    if (columnLastItem.mLastDistance == 0) {
                        finished = computerUpFastEachItem(columnLastItem, 0, true);
                    } else {
                        if (columnLastItem.mLastDistance >= columnLastItem.mFinalDistance) {
                            columnLastItem.mCurrDelta = 0;
                        } else {
                            finished = computerUpFastEachItem(columnLastItem, 0, true);
                        }
                    }
                    if (columnLastItem.mFinalDistance <= this.hor_delay_distance * 2 || columnLastItem.mFinalDistance <= this.ver_delay_distance * 2) {
                        needDelay = false;
                    }
                } else if (preColumnItemStatus.compareTo(FlipItemFastStatus.STOP) == 0) {
                    if (!computerUpFastEachItem(columnLastItem, 0, true)) {
                        finished = false;
                    }
                } else if (preColumnItemStatus.compareTo(FlipItemFastStatus.START_UNSTOP) == 0) {
                    int diff = preColumnDistance - columnLastItem.mTotalMoveDistance;
                    if (!needDelay || diff > this.hor_delay_distance) {
                        int moveDistance = diff - this.hor_delay_distance;
                        if (!needDelay) {
                            moveDistance = preColumnDelta;
                        }
                        if (!computerUpFastEachItem(columnLastItem, moveDistance, false)) {
                            finished = false;
                        }
                    } else {
                        for (int clearColumnIndex = columnIndex; clearColumnIndex >= 0; clearColumnIndex--) {
                            List<FlipItem> clearItemList = this.mFlipItemColumnList.get(clearColumnIndex);
                            for (int clearVerticalIndex = clearItemList.size() - 1; clearVerticalIndex >= 0; clearVerticalIndex--) {
                                clearItemList.get(clearVerticalIndex).mCurrDelta = 0;
                            }
                        }
                    }
                } else {
                    int diff2 = preColumnDistance - columnLastItem.mTotalMoveDistance;
                    if (needDelay && diff2 > this.ver_delay_distance && !computerUpFastEachItem(columnLastItem, 0, true)) {
                        finished = false;
                    }
                }
                preColumnDelta = columnLastItem.mCurrDelta;
                int preVerticalDelta = columnLastItem.mCurrDelta;
                preColumnDistance = columnLastItem.mTotalMoveDistance;
                int preVerticalDistance = columnLastItem.mTotalMoveDistance;
                if (this.mStartingFlipScroll) {
                    columnLastItem.mFastScrollStartingOffset += columnLastItem.mCurrDelta;
                }
                if (columnLastItem.mLastDistance >= columnLastItem.mFinalDistance) {
                    preColumnItemStatus = FlipItemFastStatus.STOP;
                    preVertiaclItemStatus = FlipItemFastStatus.STOP;
                } else if (columnLastItem.mCurrTotalMoveDistance != 0) {
                    preColumnItemStatus = FlipItemFastStatus.START_UNSTOP;
                    preVertiaclItemStatus = FlipItemFastStatus.START_UNSTOP;
                } else {
                    preColumnItemStatus = FlipItemFastStatus.UNSTART;
                    preVertiaclItemStatus = FlipItemFastStatus.UNSTART;
                }
                int verticalIndex = itemList.size() - 2;
                while (true) {
                    if (verticalIndex < 0) {
                        break;
                    }
                    FlipItem item = itemList.get(verticalIndex);
                    item.mCurrDelta = 0;
                    if (preVertiaclItemStatus2.compareTo(FlipItemFastStatus.STOP) == 0) {
                        if (!computerUpFastEachItem(item, 0, true)) {
                            finished = false;
                        }
                    } else if (preVertiaclItemStatus2.compareTo(FlipItemFastStatus.START_UNSTOP) == 0) {
                        int diff3 = preVerticalDistance - item.mTotalMoveDistance;
                        if (!needDelay || diff3 > this.ver_delay_distance) {
                            int moveDistance2 = diff3 - this.ver_delay_distance;
                            if (!needDelay) {
                                moveDistance2 = preVerticalDelta;
                            }
                            if (!computerUpFastEachItem(item, moveDistance2, false)) {
                                finished = false;
                            }
                        } else {
                            for (int clearIndex = verticalIndex; clearIndex >= 0; clearIndex--) {
                                itemList.get(clearIndex).mCurrDelta = 0;
                            }
                        }
                    } else {
                        int diff4 = preVerticalDistance - item.mTotalMoveDistance;
                        if (needDelay && diff4 > this.ver_delay_distance && !computerUpFastEachItem(item, 0, true)) {
                            finished = false;
                        }
                    }
                    preVerticalDelta = item.mCurrDelta;
                    preVerticalDistance = item.mTotalMoveDistance;
                    if (this.mStartingFlipScroll) {
                        item.mFastScrollStartingOffset += item.mCurrDelta;
                    }
                    if (item.mLastDistance >= item.mFinalDistance) {
                        preVertiaclItemStatus2 = FlipItemFastStatus.STOP;
                    } else if (item.mCurrTotalMoveDistance != 0) {
                        preVertiaclItemStatus2 = FlipItemFastStatus.START_UNSTOP;
                    } else {
                        preVertiaclItemStatus2 = FlipItemFastStatus.UNSTART;
                    }
                    verticalIndex--;
                }
                columnIndex--;
            } else {
                break;
            }
        }
        return finished;
    }

    private boolean computerUpFastEachItem(FlipItem item, int moveDistance, boolean preItemStoped) {
        if (!preItemStoped) {
            item.mCurrDelta = moveDistance;
            item.mCurrFrameCount++;
            if (item.mCurrFrameCount > item.mFinalFrameCount) {
                item.mCurrFrameCount = item.mFinalFrameCount;
            }
            int left = item.mFinalDistance - item.mLastDistance;
            if (left < item.mCurrDelta) {
                item.mCurrDelta = left;
            }
            item.mCurrTotalMoveDistance += item.mCurrDelta;
            item.mLastDistance += item.mCurrDelta;
            item.mTotalMoveDistance += item.mCurrDelta;
            return false;
        } else if (item.mLastDistance < item.mFinalDistance) {
            int currDistance = item.mLastDistance + this.mFastStep.getCurrStep();
            if (currDistance > item.mFinalDistance) {
                currDistance = item.mFinalDistance;
            }
            item.mCurrDelta = currDistance - item.mLastDistance;
            item.mCurrTotalMoveDistance += item.mCurrDelta;
            item.mLastDistance += item.mCurrDelta;
            item.mTotalMoveDistance += item.mCurrDelta;
            item.mCurrFrameCount++;
            if (item.mCurrFrameCount > item.mFinalFrameCount) {
                item.mCurrFrameCount = item.mFinalFrameCount;
            }
            return false;
        } else {
            item.mCurrDelta = 0;
            item.mCurrFrameCount = item.mFinalFrameCount;
            item.mLastDistance = item.mFinalDistance;
            return true;
        }
    }

    private boolean computerFlipScrollDown() {
        this.mCurrFrameIndex++;
        if (this.mFastFlip) {
            return computerDownFast();
        }
        return computerDownNormal();
    }

    private boolean computerDownNormal() {
        List<FlipItem> itemList;
        int horCount = 0;
        boolean finished = true;
        Iterator<List<FlipItem>> it = this.mFlipItemColumnList.iterator();
        while (it.hasNext() && (itemList = it.next()) != null) {
            int verCount = 0;
            for (FlipItem item : itemList) {
                int delayFrameCount = (this.hor_delay_frame_count * horCount) + (this.ver_delay_frame_count * verCount);
                int itemCurrFrameCount = this.mCurrFrameIndex;
                if (this.mItemDelayAnim) {
                    itemCurrFrameCount -= delayFrameCount;
                }
                if (itemCurrFrameCount > 0) {
                    if (itemCurrFrameCount >= item.mFinalFrameCount) {
                        if (item.mLastDistance > 0 && item.mLastDistance < item.mFinalDistance) {
                            finished = false;
                        } else if (item.mLastDistance < 0 && item.mLastDistance > item.mFinalDistance) {
                            finished = false;
                        }
                        item.mCurrDelta = item.mFinalDistance - item.mLastDistance;
                        item.mLastDistance += item.mCurrDelta;
                        item.mCurrFrameCount = item.mFinalFrameCount;
                        item.mTotalMoveDistance += item.mCurrDelta;
                    } else {
                        item.mCurrDelta = getCurrDistance(item.mFinalDistance, ((float) itemCurrFrameCount) / ((float) item.mFinalFrameCount), true) - item.mLastDistance;
                        if (item.mCurrDelta > 0) {
                            item.mCurrDelta = 0;
                        }
                        item.mLastDistance += item.mCurrDelta;
                        item.mCurrFrameCount = itemCurrFrameCount;
                        item.mTotalMoveDistance += item.mCurrDelta;
                        finished = false;
                    }
                    verCount++;
                    if (this.mStartingFlipScroll) {
                        item.mFastScrollStartingOffset += item.mCurrDelta;
                    }
                }
            }
            horCount++;
        }
        return finished;
    }

    private boolean computerDownFast() {
        boolean needDelay;
        List<FlipItem> itemList;
        FlipItemFastStatus preVertiaclItemStatus;
        FlipItemFastStatus preVertiaclItemStatus2;
        boolean finished = true;
        if (this.mItemDelayAnim) {
            needDelay = true;
        } else {
            needDelay = false;
        }
        int preColumnDistance = 0;
        int preColumnDelta = 0;
        FlipItemFastStatus preColumnItemStatus = FlipItemFastStatus.UNSTART;
        FlipItemFastStatus flipItemFastStatus = FlipItemFastStatus.UNSTART;
        int columnIndex = 0;
        while (true) {
            if (columnIndex >= this.mFlipItemColumnList.size() || (itemList = this.mFlipItemColumnList.get(columnIndex)) == null || itemList.size() <= 0) {
                break;
            }
            FlipItem columnFirstItem = itemList.get(0);
            columnFirstItem.mCurrDelta = 0;
            if (columnIndex == 0) {
                if (columnFirstItem.mLastDistance == 0) {
                    finished = computerDownFastEachItem(columnFirstItem, 0, true);
                } else {
                    if (columnFirstItem.mLastDistance <= columnFirstItem.mFinalDistance) {
                        columnFirstItem.mCurrDelta = 0;
                    } else {
                        finished = computerDownFastEachItem(columnFirstItem, 0, true);
                    }
                }
                if (columnFirstItem.mFinalDistance >= (-(this.hor_delay_distance * 2)) || columnFirstItem.mFinalDistance >= (-(this.ver_delay_distance * 2))) {
                    needDelay = false;
                }
            } else if (preColumnItemStatus.compareTo(FlipItemFastStatus.STOP) == 0) {
                if (!computerDownFastEachItem(columnFirstItem, 0, true)) {
                    finished = false;
                }
            } else if (preColumnItemStatus.compareTo(FlipItemFastStatus.START_UNSTOP) == 0) {
                int diff = columnFirstItem.mTotalMoveDistance - preColumnDistance;
                if (!needDelay || diff > this.hor_delay_distance) {
                    int moveDistance = this.hor_delay_distance - diff;
                    if (!needDelay) {
                        moveDistance = preColumnDelta;
                    }
                    if (!computerDownFastEachItem(columnFirstItem, moveDistance, false)) {
                        finished = false;
                    }
                } else {
                    for (int clearColumnIndex = columnIndex; clearColumnIndex < this.mFlipItemColumnList.size(); clearColumnIndex++) {
                        List<FlipItem> clearItemList = this.mFlipItemColumnList.get(clearColumnIndex);
                        for (int clearVerticalIndex = 0; clearVerticalIndex < clearItemList.size(); clearVerticalIndex++) {
                            clearItemList.get(clearVerticalIndex).mCurrDelta = 0;
                        }
                    }
                }
            } else {
                int diff2 = columnFirstItem.mTotalMoveDistance - preColumnDistance;
                if (needDelay && diff2 > this.ver_delay_distance && !computerDownFastEachItem(columnFirstItem, 0, true)) {
                    finished = false;
                }
            }
            preColumnDelta = columnFirstItem.mCurrDelta;
            int preVerticalDelta = columnFirstItem.mCurrDelta;
            preColumnDistance = columnFirstItem.mTotalMoveDistance;
            int preVerticalDistance = columnFirstItem.mTotalMoveDistance;
            if (this.mStartingFlipScroll) {
                columnFirstItem.mFastScrollStartingOffset += columnFirstItem.mCurrDelta;
            }
            if (columnFirstItem.mLastDistance <= columnFirstItem.mFinalDistance) {
                preColumnItemStatus = FlipItemFastStatus.STOP;
                preVertiaclItemStatus = FlipItemFastStatus.STOP;
            } else if (columnFirstItem.mCurrTotalMoveDistance != 0) {
                preColumnItemStatus = FlipItemFastStatus.START_UNSTOP;
                preVertiaclItemStatus = FlipItemFastStatus.START_UNSTOP;
            } else {
                preColumnItemStatus = FlipItemFastStatus.UNSTART;
                preVertiaclItemStatus = FlipItemFastStatus.UNSTART;
            }
            int verticalIndex = 1;
            while (true) {
                if (verticalIndex >= itemList.size()) {
                    break;
                }
                FlipItem item = itemList.get(verticalIndex);
                item.mCurrDelta = 0;
                if (preVertiaclItemStatus2.compareTo(FlipItemFastStatus.STOP) == 0) {
                    if (!computerDownFastEachItem(item, 0, true)) {
                        finished = false;
                    }
                } else if (preVertiaclItemStatus2.compareTo(FlipItemFastStatus.START_UNSTOP) == 0) {
                    int diff3 = item.mTotalMoveDistance - preVerticalDistance;
                    if (!needDelay || diff3 > this.ver_delay_distance) {
                        int moveDistance2 = this.ver_delay_distance - diff3;
                        if (!needDelay) {
                            moveDistance2 = preVerticalDelta;
                        }
                        if (!computerDownFastEachItem(item, moveDistance2, false)) {
                            finished = false;
                        }
                    } else {
                        for (int clearIndex = verticalIndex; clearIndex < itemList.size(); clearIndex++) {
                            itemList.get(clearIndex).mCurrDelta = 0;
                        }
                    }
                } else {
                    int diff4 = item.mTotalMoveDistance - preVerticalDistance;
                    if (needDelay && diff4 > this.ver_delay_distance && !computerDownFastEachItem(item, 0, true)) {
                        finished = false;
                    }
                }
                preVerticalDelta = item.mCurrDelta;
                preVerticalDistance = item.mTotalMoveDistance;
                if (this.mStartingFlipScroll) {
                    item.mFastScrollStartingOffset += item.mCurrDelta;
                }
                if (item.mLastDistance <= item.mFinalDistance) {
                    preVertiaclItemStatus2 = FlipItemFastStatus.STOP;
                } else if (item.mCurrTotalMoveDistance != 0) {
                    preVertiaclItemStatus2 = FlipItemFastStatus.START_UNSTOP;
                } else {
                    preVertiaclItemStatus2 = FlipItemFastStatus.UNSTART;
                }
                verticalIndex++;
            }
            columnIndex++;
        }
        return finished;
    }

    private boolean computerDownFastEachItem(FlipItem item, int moveDistance, boolean preItemStoped) {
        if (!preItemStoped) {
            item.mCurrDelta = moveDistance;
            item.mCurrFrameCount++;
            if (item.mCurrFrameCount > item.mFinalFrameCount) {
                item.mCurrFrameCount = item.mFinalFrameCount;
            }
            int left = item.mFinalDistance - item.mLastDistance;
            if (left > item.mCurrDelta) {
                item.mCurrDelta = left;
            }
            item.mCurrTotalMoveDistance += item.mCurrDelta;
            item.mLastDistance += item.mCurrDelta;
            item.mTotalMoveDistance += item.mCurrDelta;
            return false;
        } else if (item.mLastDistance > item.mFinalDistance) {
            int currDistance = item.mLastDistance + this.mFastStep.getCurrStep();
            if (currDistance < item.mFinalDistance) {
                currDistance = item.mFinalDistance;
            }
            item.mCurrDelta = currDistance - item.mLastDistance;
            item.mCurrTotalMoveDistance += item.mCurrDelta;
            item.mLastDistance += item.mCurrDelta;
            item.mTotalMoveDistance += item.mCurrDelta;
            item.mCurrFrameCount++;
            if (item.mCurrFrameCount > item.mFinalFrameCount) {
                item.mCurrFrameCount = item.mFinalFrameCount;
            }
            return false;
        } else {
            item.mCurrDelta = 0;
            item.mCurrFrameCount = item.mFinalFrameCount;
            item.mLastDistance = item.mFinalDistance;
            return true;
        }
    }

    private void makeFlipItemList(FlipListChangeType type, int start, int end, Object obj) {
        int columnIndex;
        List<FlipItem> columnFlipItemList;
        FlipItem last;
        FlipItem first;
        int headerCount = this.mHeaderViewList != null ? this.mHeaderViewList.size() : 0;
        int footerCount = this.mFooterViewList != null ? this.mFooterViewList.size() : 0;
        int currAdapterColumn = -1;
        int firstIndex = -1;
        int lastIndex = -1;
        if (this.mFlipItemColumnList.size() > 0) {
            List<FlipItem> firstList = this.mFlipItemColumnList.get(0);
            if (!(firstList == null || (first = firstList.get(0)) == null)) {
                firstIndex = first.mIndex;
            }
            List<FlipItem> lastList = this.mFlipItemColumnList.get(this.mFlipItemColumnList.size() - 1);
            if (!(lastList == null || (last = lastList.get(lastList.size() - 1)) == null)) {
                lastIndex = last.mIndex;
            }
        }
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "makeFlipItemList firstIndex=" + firstIndex + " lastIndex=" + lastIndex + " start=" + start + " end=" + end + " type=" + type);
        }
        for (int i = start; i <= end; i++) {
            if (i < firstIndex || i > lastIndex) {
                if (headerCount > 0 && i < headerCount) {
                    int currHeader = this.mHeaderViewList.get(i).intValue();
                    int headerColumnCount = currHeader >> 16;
                    int headerVerticalCount = currHeader & 255;
                    int columnIndex2 = this.mFlipItemColumnList.size();
                    for (int c = 0; c < headerColumnCount; c++) {
                        ArrayList arrayList = new ArrayList();
                        this.mFlipItemColumnList.add(arrayList);
                        for (int v = 0; v < headerVerticalCount; v++) {
                            FlipItem item = getMakedFlipItem(type, FlipItemPositionType.HEATER, i, (c * headerVerticalCount) + v, c + columnIndex2, v, obj);
                            if (item != null) {
                                arrayList.add(item);
                            }
                        }
                    }
                } else if (footerCount <= 0 || i < this.mTotalItemCount - footerCount) {
                    int adatperStart = start;
                    if (type == FlipListChangeType.ADD_AFTER) {
                        adatperStart = this.mStartListIndex;
                    }
                    int inScreenHeaderCount = headerCount - adatperStart;
                    if (inScreenHeaderCount <= 0) {
                        inScreenHeaderCount = 0;
                    }
                    int adapterColumnIndex = ((i - adatperStart) - inScreenHeaderCount) / this.mColumnCount;
                    int adapterVerticalIntex = ((i - adatperStart) - inScreenHeaderCount) % this.mColumnCount;
                    if (adapterColumnIndex != currAdapterColumn) {
                        currAdapterColumn = adapterColumnIndex;
                        columnFlipItemList = new ArrayList<>();
                        this.mFlipItemColumnList.add(columnFlipItemList);
                        columnIndex = this.mFlipItemColumnList.size() - 1;
                    } else {
                        columnIndex = this.mFlipItemColumnList.size() - 1;
                        columnFlipItemList = this.mFlipItemColumnList.get(columnIndex);
                    }
                    FlipItem item2 = getMakedFlipItem(type, FlipItemPositionType.ADAPTER, i, 0, columnIndex, adapterVerticalIntex, obj);
                    if (item2 != null) {
                        columnFlipItemList.add(item2);
                    }
                } else {
                    int currFooter = this.mFooterViewList.get(i).intValue();
                    int footerColumnCount = currFooter >> 16;
                    int footerVerticalCount = currFooter & 255;
                    int columnIndex3 = this.mFlipItemColumnList.size();
                    for (int c2 = 0; c2 < footerColumnCount; c2++) {
                        ArrayList arrayList2 = new ArrayList();
                        this.mFlipItemColumnList.add(arrayList2);
                        for (int v2 = 0; v2 < footerVerticalCount; v2++) {
                            FlipItem item3 = getMakedFlipItem(type, FlipItemPositionType.FOOTER, i, (c2 * footerVerticalCount) + v2, c2 + columnIndex3, v2, obj);
                            if (item3 != null) {
                                arrayList2.add(item3);
                            }
                        }
                    }
                }
            } else if (headerCount > 0 && i < headerCount) {
                int currHeader2 = this.mHeaderViewList.get(i).intValue();
                int headerColumnCount2 = currHeader2 >> 16;
                int headerVerticalCount2 = currHeader2 & 255;
                for (int c3 = 0; c3 < headerColumnCount2; c3++) {
                    for (int v3 = 0; v3 < headerVerticalCount2; v3++) {
                        refreshFlipItem(i, (c3 * headerVerticalCount2) + v3, obj, type);
                    }
                }
            } else if (footerCount <= 0 || i < this.mTotalItemCount - footerCount) {
                refreshFlipItem(i, 0, obj, type);
            } else {
                int currFooter2 = this.mFooterViewList.get(i).intValue();
                int footerColumnCount2 = currFooter2 >> 16;
                int footerVerticalCount2 = currFooter2 & 255;
                for (int c4 = 0; c4 < footerColumnCount2; c4++) {
                    this.mFlipItemColumnList.add(new ArrayList());
                    for (int v4 = 0; v4 < footerVerticalCount2; v4++) {
                        refreshFlipItem(i, (c4 * footerVerticalCount2) + v4, obj, type);
                    }
                }
            }
        }
    }

    private FlipItem getMakedFlipItem(FlipListChangeType changeType, FlipItemPositionType posType, int index, int secondIndex, int columnIndex, int verticalIndex, Object object) {
        FlipItem preItem;
        FlipItem item = null;
        switch (changeType) {
            case INIT:
            case ADD_BEFORE:
                item = getFlipItem(index, secondIndex);
                if (item == null) {
                    item = new FlipItem(index, 0);
                    this.mFlipItemMap.put(getFlipItemMapKey(index, secondIndex), item);
                }
                item.mColumnIndex = columnIndex;
                item.mVerticalIndex = verticalIndex;
                break;
            case ADD_AFTER:
                if (object != null && (object instanceof FlipItem)) {
                    FlipItem item2 = addAfterItem((FlipItem) object, index, secondIndex);
                    if (item2 == null) {
                        item2 = new FlipItem(index, 0);
                        this.mFlipItemMap.put(getFlipItemMapKey(index, secondIndex), item2);
                    }
                    item.mColumnIndex = columnIndex;
                    item.mVerticalIndex = verticalIndex;
                    break;
                }
            case TEMP:
                FlipItem item3 = getFlipItem(index, secondIndex);
                if (item3 == null) {
                    item3 = new FlipItem(index, 0);
                    this.mFlipItemMap.put(getFlipItemMapKey(index, secondIndex), item3);
                }
                item.mColumnIndex = columnIndex;
                item.mVerticalIndex = verticalIndex;
                if (this.mIsDown) {
                    preItem = getFlipItemColumn(columnIndex - 1, 0);
                } else {
                    preItem = getFirstColumnLasterItem();
                }
                if (preItem != null) {
                    int left = preItem.mFinalDistance - preItem.mLastDistance;
                    int leftCount = preItem.mFinalFrameCount - preItem.mCurrFrameCount;
                    item.mFinalFrameCount = preItem.mFinalFrameCount;
                    item.mLastDistance = preItem.mLastDistance;
                    item.mFinalDistance = preItem.mFinalDistance;
                    item.mTotalMoveDistance = preItem.mTotalMoveDistance;
                    if (this.DEBUG) {
                        ZpLogger.i("ItemFlipScroller", "getMakedFlipItem unknown left=" + left + " index=" + preItem.mIndex + " mFinalDistance=" + preItem.mFinalDistance + " mLastDistance=" + preItem.mLastDistance + " leftCount=" + leftCount + " mFinalFrameCount=" + preItem.mFinalFrameCount + " mCurrFrameCount=" + preItem.mCurrFrameCount + " mTotalMoveDistance=" + preItem.mTotalMoveDistance);
                        break;
                    }
                }
                break;
        }
        return item;
    }

    private void refreshFlipItem(int index, int secondIndex, Object obj, FlipListChangeType type) {
        FlipItem item;
        if (type.compareTo(FlipListChangeType.ADD_AFTER) == 0) {
            addAfterItem((FlipItem) obj, index, 0);
        } else if (type.compareTo(FlipListChangeType.ADD_BEFORE) == 0) {
            addBeforeItem((FlipItem) obj, index, 0);
        } else if (type.compareTo(FlipListChangeType.TEMP) == 0 && !this.mIsDown && (item = getFlipItem(index, secondIndex)) != null) {
            FlipItem preItem = null;
            int i = 0;
            while (true) {
                if (i < this.mFlipItemColumnList.size()) {
                    List<FlipItem> itemList = this.mFlipItemColumnList.get(i);
                    if (itemList != null && itemList.size() > 0 && itemList.get(0).mIndex >= ((Integer) obj).intValue()) {
                        preItem = itemList.get(itemList.size() - 1);
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (preItem != null) {
                int left = preItem.mFinalDistance - preItem.mLastDistance;
                int leftCount = preItem.mFinalFrameCount - preItem.mCurrFrameCount;
                item.mFinalFrameCount = preItem.mFinalFrameCount;
                item.mLastDistance = preItem.mLastDistance;
                item.mFinalDistance = preItem.mFinalDistance;
                item.mTotalMoveDistance = preItem.mTotalMoveDistance;
                if (this.DEBUG) {
                    ZpLogger.i("ItemFlipScroller", "getMakedFlipItem unknown left=" + left + " index=" + preItem.mIndex + " mFinalDistance=" + preItem.mFinalDistance + " mLastDistance=" + preItem.mLastDistance + " leftCount=" + leftCount + " mFinalFrameCount=" + preItem.mFinalFrameCount + " mCurrFrameCount=" + preItem.mCurrFrameCount + " mTotalMoveDistance=" + preItem.mTotalMoveDistance);
                }
            }
        }
    }

    private void makeInitFlipItemList(int start, int end, boolean isDown) {
        int tempStart;
        if (isDown) {
            makeFlipItemList(FlipListChangeType.INIT, start, this.mColumnCount + end, (Object) null);
            this.mPreAddTempItemIndex = start;
            return;
        }
        if (start <= this.mHeaderViewList.size()) {
            tempStart = start - 1;
        } else {
            tempStart = start - this.mColumnCount;
        }
        if (tempStart < 0) {
            tempStart = 0;
        }
        makeFlipItemList(FlipListChangeType.INIT, tempStart, end, (Object) null);
        this.mPreAddTempItemIndex = tempStart;
    }

    private void makeBeforeFlipItemList(int start, int end) {
        int tempStart;
        if (start <= this.mHeaderViewList.size()) {
            tempStart = start - 1;
        } else {
            tempStart = start - this.mColumnCount;
            if (tempStart < this.mHeaderViewList.size()) {
                tempStart = this.mHeaderViewList.size() - 1;
            }
        }
        if (tempStart < 0) {
            tempStart = 0;
        }
        makeFlipItemList(FlipListChangeType.ADD_BEFORE, tempStart, end, (Object) null);
        int tempEnd = Math.max(this.mPreAddTempItemIndex, start - 1);
        if (tempStart <= tempEnd && tempStart < this.mPreAddTempItemIndex) {
            makeFlipItemList(FlipListChangeType.TEMP, tempStart, tempEnd, Integer.valueOf(this.mPreAddTempItemIndex));
        }
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "makeBeforeFlipItemList start=" + start + " end=" + end + " tempStart=" + tempStart + " tempEnd=" + tempEnd + " mPreAddTempItemIndex=" + this.mPreAddTempItemIndex);
        }
        this.mPreAddTempItemIndex = tempStart;
    }

    private void makeAfterFlipItemList(int start, int end, FlipItem lastHeaderItem) {
        makeFlipItemList(FlipListChangeType.ADD_AFTER, start, end, lastHeaderItem);
        makeTempFlipItemList(start, end, true, FlipListChangeType.TEMP);
    }

    private void makeTempFlipItemList(int start, int end, boolean after, FlipListChangeType type) {
        if (!after) {
            int tempStart = start - this.mColumnCount;
            if (tempStart < 0) {
                tempStart = 0;
            }
            int tempEnd = start - 1;
            if (tempEnd < 0) {
                tempEnd = 0;
            }
            if (tempEnd <= this.mHeaderViewList.size() - 1) {
                makeFlipItemList(type, tempEnd, tempEnd, (Object) null);
            } else {
                makeFlipItemList(type, tempStart, tempEnd, (Object) null);
            }
        } else {
            makeFlipItemList(type, end + 1, this.mColumnCount + end, (Object) null);
        }
    }

    private FlipItem getFlipItem(int index, int secondIndex) {
        if (secondIndex == -1) {
            if (this.mHeaderViewList.size() > 0 && index < this.mHeaderViewList.size()) {
                int currHeader = this.mHeaderViewList.get(index).intValue();
                secondIndex = ((currHeader >> 16) * (currHeader & 255)) - 1;
            } else if (this.mFooterViewList.size() > 0 && index >= this.mTotalItemCount - this.mFooterViewList.size()) {
                int currFooter = this.mFooterViewList.get(index).intValue();
                secondIndex = ((currFooter >> 16) * (currFooter & 255)) - 1;
            }
        }
        return this.mFlipItemMap.get(getFlipItemMapKey(index, secondIndex));
    }

    private FlipItem getFlipItemColumn(int column, int vertical) {
        try {
            List<FlipItem> itemList = this.mFlipItemColumnList.get(column);
            if (itemList != null && itemList.size() > 0) {
                return itemList.get(vertical);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlipItem getCurrColumnLastItem(int column) {
        try {
            List<FlipItem> itemList = this.mFlipItemColumnList.get(column);
            if (itemList != null && itemList.size() > 0) {
                return itemList.get(itemList.size() - 1);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlipItem getFirstColumnLasterItem() {
        for (int i = 0; i < this.mFlipItemColumnList.size(); i++) {
            List<FlipItem> itemList = this.mFlipItemColumnList.get(i);
            if (itemList != null && itemList.size() > 0 && itemList.get(0).mIndex >= this.mStartListIndex) {
                return itemList.get(itemList.size() - 1);
            }
        }
        return null;
    }

    private FlipItem getLastColumnFirsterItem() {
        for (int i = 1; i <= this.mFlipItemColumnList.size(); i++) {
            List<FlipItem> itemList = this.mFlipItemColumnList.get(this.mFlipItemColumnList.size() - i);
            if (itemList != null && itemList.size() > 0 && itemList.get(itemList.size() - 1).mIndex <= this.mEndListIndex) {
                return itemList.get(0);
            }
        }
        return null;
    }

    private int getFlipItemMapKey(int index, int secondIndex) {
        return (index << 8) | secondIndex;
    }

    private void resetItemData(int newFinalDistance) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "resetItemData newFinalDistance=" + newFinalDistance);
        }
        boolean first = true;
        for (List<FlipItem> itemList : this.mFlipItemColumnList) {
            for (FlipItem item : itemList) {
                item.mCurrDelta = 0;
                item.mCurrTotalMoveDistance = 0;
                item.mFinalFrameCount += this.mFinalFrameCount;
                item.mFastScrollStartingOffset = 0;
                item.mFastScrollOffset = 0;
                item.mFinalDistance += newFinalDistance;
                if (first && this.mFastFlip) {
                    int step = (int) (((float) (item.mFinalDistance - item.mLastDistance)) / ((float) this.flip_scroll_frame_count));
                    if (this.mIsDown) {
                        if (step > (-this.min_fast_setp_discance)) {
                            step = -this.min_fast_setp_discance;
                        }
                    } else if (step < this.min_fast_setp_discance) {
                        step = this.min_fast_setp_discance;
                    }
                    this.mFastStep.setStartStep(step);
                    first = false;
                }
                if (this.DEBUG) {
                    ZpLogger.i("ItemFlipScroller", "resetItemData index=" + item.mIndex + " newFinalDistance=" + newFinalDistance + " mTotalMoveDistance=" + item.mTotalMoveDistance + " mLastDistance=" + item.mLastDistance + " mFinalDistance=" + item.mFinalDistance + " mFinalFrameCount=" + item.mFinalFrameCount + " mFastStep=" + this.mFastStep);
                }
            }
        }
    }

    private int getRealFinalDistance(int newFinalDistance) {
        int realFinalDistance = newFinalDistance;
        int preSelectedIndex = this.mPreSelectedPosition;
        if (preSelectedIndex > this.mEndListIndex) {
            preSelectedIndex = this.mEndListIndex;
        } else if (preSelectedIndex < this.mStartListIndex) {
            preSelectedIndex = this.mStartListIndex;
        }
        FlipItem preSelectedItem = this.mFlipItemMap.get(getFlipItemMapKey(preSelectedIndex, 0));
        if (preSelectedItem != null) {
            FlipItem columnLastItem = getCurrColumnLastItem(preSelectedItem.mColumnIndex);
            if (columnLastItem != null) {
                int selectedItemLeft = columnLastItem.mFinalDistance - columnLastItem.mLastDistance;
                realFinalDistance = (newFinalDistance - columnLastItem.mFastScrollStartingOffset) - selectedItemLeft;
                if (this.DEBUG) {
                    ZpLogger.i("ItemFlipScroller", "getRealFinalDistance columnFirstItem=" + columnLastItem.mIndex + " selectedItemLeft=" + selectedItemLeft + " mFastOffset=" + columnLastItem.mFastScrollStartingOffset + " realFinalDistance=" + realFinalDistance + " newFinalDistance=" + newFinalDistance + " mFinalDistance=" + columnLastItem.mFinalDistance + " mLastDistance=" + columnLastItem.mLastDistance + " preSelectedIndex=" + preSelectedIndex);
                }
            }
        } else if (!this.mFinished) {
            ZpLogger.e("ItemFlipScroller", "error getRealFinalDistance selectedItem == null position=" + preSelectedIndex);
            return realFinalDistance;
        }
        return realFinalDistance;
    }

    private void addBefore(int start) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "addGridView start < mStartListIndex start=" + start + " mStartListIndex=" + this.mStartListIndex);
        }
        FlipItem firstFooterItem = getFirstColumnLasterItem();
        if (firstFooterItem != null) {
            this.mFlipItemColumnList.clear();
            makeBeforeFlipItemList(start, this.mEndListIndex);
            int headerCount = this.mHeaderViewList != null ? this.mHeaderViewList.size() : 0;
            int footerCount = this.mFooterViewList != null ? this.mFooterViewList.size() : 0;
            for (int i = this.mStartListIndex - 1; i >= start; i--) {
                if (headerCount > 0 && i < headerCount) {
                    int currHeader = this.mHeaderViewList.get(i).intValue();
                    int headerVerticalCount = currHeader & 255;
                    for (int c = (currHeader >> 16) - 1; c >= 0; c--) {
                        for (int v = headerVerticalCount - 1; v >= 0; v--) {
                            addBeforeItem(firstFooterItem, i, (c * headerVerticalCount) + v);
                        }
                    }
                } else if (footerCount <= 0 || i < this.mTotalItemCount - footerCount) {
                    addBeforeItem(firstFooterItem, i, 0);
                } else {
                    int currFooter = this.mFooterViewList.get(i).intValue();
                    int footerVerticalCount = currFooter & 255;
                    for (int c2 = (currFooter >> 16) - 1; c2 >= 0; c2--) {
                        for (int v2 = footerVerticalCount - 1; v2 >= 0; v2--) {
                            addBeforeItem(firstFooterItem, i, (c2 * footerVerticalCount) + v2);
                        }
                    }
                }
            }
            this.mStartListIndex = start;
        }
    }

    private FlipItem addBeforeItem(FlipItem firstFooterItem, int index, int secondIndex) {
        if (firstFooterItem == null) {
            return null;
        }
        FlipItem item = getFlipItem(index, secondIndex);
        if (item != null) {
            int currLastDistance = item.mLastDistance;
            if (currLastDistance < 0) {
                currLastDistance = 0;
            }
            if (this.mItemFlipScrollerListener != null) {
                int delta = (((currLastDistance - firstFooterItem.mLastDistance) + firstFooterItem.mFastScrollStartingOffset) + firstFooterItem.mFastScrollOffset) - item.mFastScrollStartingOffset;
                if (this.DEBUG) {
                    ZpLogger.i("ItemFlipScroller", "addGridView  start < mStartListIndex index=" + index + " currLastDistance=" + currLastDistance + " firstFooterItem index=" + firstFooterItem.mIndex + " mLastDistance=" + firstFooterItem.mLastDistance + " mFastScrollStartingOffset=" + firstFooterItem.mFastScrollStartingOffset + " mFastScrollOffset=" + firstFooterItem.mFastScrollOffset + " item.mFastScrollStartingOffset=" + item.mFastScrollStartingOffset + " delta=" + delta);
                }
                if (delta != 0) {
                    this.mItemFlipScrollerListener.onOffsetNewChild(index, secondIndex, delta);
                }
            }
        }
        return item;
    }

    private void addAfter(int end) {
        if (this.DEBUG) {
            ZpLogger.i("ItemFlipScroller", "addGridView end > mEndListIndex end=" + end + " mEndListIndex=" + this.mEndListIndex + " mFastFlip=" + this.mFastFlip);
        }
        FlipItem lastHeaderItem = getLastColumnFirsterItem();
        if (lastHeaderItem != null) {
            makeAfterFlipItemList(this.mEndListIndex + 1, end, lastHeaderItem);
            this.mEndListIndex = end;
        }
    }

    private FlipItem addAfterItem(FlipItem lastHeaderItem, int index, int secondIndex) {
        if (lastHeaderItem == null) {
            return null;
        }
        FlipItem item = getFlipItem(index, secondIndex);
        if (item != null) {
            int currLastDistance = item.mLastDistance;
            if (currLastDistance > 0) {
                currLastDistance = 0;
            }
            if (this.mItemFlipScrollerListener != null) {
                int delta = (((currLastDistance - lastHeaderItem.mLastDistance) + lastHeaderItem.mFastScrollStartingOffset) + lastHeaderItem.mFastScrollOffset) - item.mFastScrollStartingOffset;
                if (this.DEBUG) {
                    ZpLogger.i("ItemFlipScroller", "addGridView  end > mEndListIndex onOffsetNewChild index=" + index + " currLastDistance=" + currLastDistance + " mLastDistance=" + lastHeaderItem.mLastDistance + " last Index=" + lastHeaderItem.mIndex + " curr Index=" + item.mIndex + " curr startingOffset=" + item.mFastScrollStartingOffset + " mFastScrollStartingOffset=" + lastHeaderItem.mFastScrollStartingOffset + " mFastScrollOffset=" + lastHeaderItem.mFastScrollOffset + " delta=" + delta);
                }
                if (delta != 0) {
                    this.mItemFlipScrollerListener.onOffsetNewChild(index, secondIndex, delta);
                }
            }
        }
        return item;
    }

    private boolean itemIsFinished(int index) {
        if (index < this.mHeaderViewList.size()) {
            int header = this.mHeaderViewList.get(index).intValue();
            FlipItem headerLastItem = getFlipItem(index, ((header >> 16) * (header & 255)) - 1);
            if (!(headerLastItem == null || headerLastItem.mFinalDistance - headerLastItem.mLastDistance == 0)) {
                return false;
            }
        } else if (index >= this.mTotalItemCount - this.mFooterViewList.size()) {
            int footer = this.mFooterViewList.get(index).intValue();
            FlipItem footerLastItem = getFlipItem(index, ((footer >> 16) * (footer & 255)) - 1);
            if (!(footerLastItem == null || footerLastItem.mFinalDistance - footerLastItem.mLastDistance == 0)) {
                return false;
            }
        } else {
            FlipItem item = getFlipItem(index, 0);
            if (!(item == null || item.mFinalDistance - item.mLastDistance == 0)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public int getRowStart(int position) {
        int i = position;
        if (position < this.mHeaderViewList.size()) {
            return position;
        }
        if (position >= this.mTotalItemCount - this.mFooterViewList.size()) {
            return position;
        }
        int newPosition = position - this.mHeaderViewList.size();
        return (newPosition - (newPosition % this.mColumnCount)) + this.mHeaderViewList.size();
    }

    private class FlipItem {
        public int mColumnIndex;
        public int mCurrDelta;
        public int mCurrFrameCount;
        public int mCurrTotalMoveDistance;
        public int mFastScrollOffset;
        public int mFastScrollStartingOffset;
        public int mFinalDistance;
        public int mFinalFrameCount;
        public int mIndex;
        public int mLastDistance;
        public int mTotalMoveDistance;
        public int mVerticalIndex;

        public FlipItem(int index, int finalDistance) {
            this.mIndex = index;
            this.mFinalDistance = finalDistance;
        }
    }

    private class FastStep {
        private int mCurrDelta;
        private int mCurrDistance;
        private Interpolator mInterpolator;
        private boolean mIsComeDown;
        private boolean mIsPositive;
        private int mStartDelta;
        private int mStopDelta;
        private int mStopDistance;

        private FastStep() {
            this.mInterpolator = new DecelerateInterpolator(0.4f);
        }

        /* access modifiers changed from: private */
        public void setStartStep(int step) {
            this.mIsComeDown = false;
            this.mStartDelta = step;
            this.mCurrDelta = this.mStartDelta;
        }

        /* access modifiers changed from: private */
        public void resetComeDown(int target) {
            if (ItemFlipScroller.this.DEBUG) {
                ZpLogger.i("ItemFlipScroller", "resetComeDown target=" + target + " mCurrDelta=" + this.mCurrDelta);
            }
            if (!this.mIsComeDown) {
                this.mIsComeDown = true;
                this.mCurrDistance = this.mCurrDelta;
                this.mStopDistance = target;
                if (this.mStartDelta > 0) {
                    this.mStopDelta = ItemFlipScroller.this.min_fast_setp_discance;
                    this.mIsPositive = true;
                    return;
                }
                this.mStopDelta = -ItemFlipScroller.this.min_fast_setp_discance;
                this.mIsPositive = false;
            }
        }

        /* access modifiers changed from: private */
        public boolean computerOffset() {
            if (!this.mIsComeDown) {
                return true;
            }
            if ((this.mIsPositive && this.mCurrDistance > this.mStopDistance) || (!this.mIsPositive && this.mCurrDistance < this.mStopDistance)) {
                return false;
            }
            this.mCurrDelta = 0;
            float input = ((float) this.mCurrDistance) / ((float) this.mStopDistance);
            float output = input;
            if (this.mInterpolator != null) {
                output = this.mInterpolator.getInterpolation(input);
            }
            this.mCurrDelta = this.mStartDelta + ((int) (((float) (this.mStopDelta - this.mStartDelta)) * output));
            if (this.mIsPositive && this.mCurrDelta < ItemFlipScroller.this.min_fast_setp_discance) {
                this.mCurrDelta = ItemFlipScroller.this.min_fast_setp_discance;
            } else if (!this.mIsPositive && this.mCurrDelta > (-ItemFlipScroller.this.min_fast_setp_discance)) {
                this.mCurrDelta = -ItemFlipScroller.this.min_fast_setp_discance;
            }
            this.mCurrDistance += this.mCurrDelta;
            if (!ItemFlipScroller.this.DEBUG) {
                return true;
            }
            ZpLogger.i("ItemFlipScroller", "computerOffset input=" + input + " output=" + output + " mCurrDelta=" + this.mCurrDelta + " mCurrDistance=" + this.mCurrDistance + " mStopDistance=" + this.mStopDistance);
            return true;
        }

        /* access modifiers changed from: private */
        public int getCurrStep() {
            return this.mCurrDelta;
        }

        /* access modifiers changed from: private */
        public void finished() {
            this.mCurrDelta = 0;
        }
    }

    public int getHor_delay_distance() {
        return this.hor_delay_distance;
    }

    public void setHor_delay_distance(int hor_delay_distance2) {
        this.hor_delay_distance = hor_delay_distance2;
    }

    public int getVer_delay_distance() {
        return this.ver_delay_distance;
    }

    public void setVer_delay_distance(int ver_delay_distance2) {
        this.ver_delay_distance = ver_delay_distance2;
    }

    public int getMin_fast_setp_discance() {
        return this.min_fast_setp_discance;
    }

    public void setMin_fast_setp_discance(int min_fast_setp_discance2) {
        this.min_fast_setp_discance = min_fast_setp_discance2;
    }

    public int getFlip_scroll_frame_count() {
        return this.flip_scroll_frame_count;
    }

    public void setFlip_scroll_frame_count(int flip_scroll_frame_count2) {
        this.flip_scroll_frame_count = flip_scroll_frame_count2;
    }

    public int getHor_delay_frame_count() {
        return this.hor_delay_frame_count;
    }

    public void setHor_delay_frame_count(int hor_delay_frame_count2) {
        this.hor_delay_frame_count = hor_delay_frame_count2;
    }

    public int getVer_delay_frame_count() {
        return this.ver_delay_frame_count;
    }

    public void setVer_delay_frame_count(int ver_delay_frame_count2) {
        this.ver_delay_frame_count = ver_delay_frame_count2;
    }
}
