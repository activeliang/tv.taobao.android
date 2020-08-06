package com.yunos.tvtaobao.biz.focus_impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.focus_impl.FakeListView;
import com.yunos.tvtaobao.biz.focus_impl.FocusFinder;
import com.yunos.tvtaobao.biz.focus_impl.FocusNode;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Observable;

public class FocusFakeListView extends FakeListView implements FocusNode.Binder {
    /* access modifiers changed from: private */
    public static final String TAG = FocusFakeListView.class.getSimpleName();
    /* access modifiers changed from: private */
    public FocusConsumer focusConsumer;
    FocusNode focusNode;
    FocusNodeProxy focusNodeProxy;
    /* access modifiers changed from: private */
    public NoNextListener noNextListener;
    /* access modifiers changed from: private */
    public Positions positions;

    public interface NoNextListener {
        void onNoNext(int i, KeyEvent keyEvent);
    }

    public FocusFakeListView(Context context) {
        this(context, (AttributeSet) null);
    }

    public FocusFakeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusFakeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.focusNodeProxy = new FocusNodeProxy();
        this.focusNode = new FocusNode(this);
        this.positions = new Positions();
        this.focusConsumer = null;
        this.noNextListener = null;
        this.focusNode.setFocusFinder(this.focusNodeProxy);
        this.focusNode.setFocusConsumer(this.focusNodeProxy);
        this.focusNode.setNodeOuterState(this.focusNodeProxy);
    }

    public FocusConsumer getFocusConsumer() {
        return this.focusConsumer;
    }

    public void setFocusConsumer(FocusConsumer focusConsumer2) {
        this.focusConsumer = focusConsumer2;
    }

    public NoNextListener getNoNextListener() {
        return this.noNextListener;
    }

    public void setNoNextListener(NoNextListener noNextListener2) {
        this.noNextListener = noNextListener2;
    }

    public View getView() {
        return this;
    }

    public FocusNode getNode() {
        return this.focusNode;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.focusNode.onFocusDraw(canvas);
    }

    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof FocusNode.Binder) {
            ((FocusNode.Binder) child).getNode().resetState();
        }
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof FocusNode.Binder) {
            FocusUtils.focusLeaveToLeaf(((FocusNode.Binder) child).getNode());
        }
    }

    /* access modifiers changed from: protected */
    public void onAdapterDataSetChange(FakeListView.Adapter.NotifyParam notifyParam) {
        super.onAdapterDataSetChange(notifyParam);
        if (getAdapter().getItemCount() == 0) {
            this.positions.clear();
        }
    }

    public FocusNode getFocusNode(int pos) {
        FakeListView.ViewHolder vh = getViewHolder(pos);
        if (vh == null || !(vh.itemView instanceof FocusNode.Binder)) {
            return null;
        }
        return ((FocusNode.Binder) vh.itemView).getNode();
    }

    public int getCandidatePos(int rmvPos) {
        Pair<Integer, Integer> pair = getLayoutRange();
        if (rmvPos >= ((Integer) pair.first).intValue() && rmvPos <= ((Integer) pair.second).intValue()) {
            if (rmvPos >= (((Integer) pair.second).intValue() + ((Integer) pair.first).intValue()) / 2 && rmvPos != ((Integer) pair.second).intValue()) {
                return this.positions.getNextUpPos(rmvPos);
            }
        }
        return -1;
    }

    public void innerFocusOn(int pos) {
        FocusNode fn = getFocusNode(pos);
        if (fn != null) {
            ZpLogger.d(TAG, tagObjHashCode() + " innerFocusOn : " + pos + " ok ");
            FocusUtils.buildFocusPath(fn, getNode());
            return;
        }
        ZpLogger.d(TAG, tagObjHashCode() + " innerFocusOn : failed cause can not find FocusNode for " + pos);
    }

    public void selectOn(int pos, Runnable runWhenScrollOver) {
        selectOn(pos, false, (Runnable) null, runWhenScrollOver);
    }

    public void selectOn(int pos, Runnable beforeSetNotify, Runnable runWhenScrollOver) {
        selectOn(pos, false, beforeSetNotify, runWhenScrollOver);
    }

    public void selectOn(int pos, boolean immediately, Runnable beforeSetNotify, Runnable runWhenScrollOver) {
        ZpLogger.d(TAG, tagObjHashCode() + " selectOn param: " + pos);
        int position = Math.max(0, Math.min(getAdapter().getItemCount() - 1, Math.max(0, pos)));
        if (getAdapter() != null && position >= 0 && position < getAdapter().getItemCount()) {
            ZpLogger.d(TAG, tagObjHashCode() + " selectOn real: " + position);
            boolean unused = this.positions.setNow(position, beforeSetNotify);
            scrollToPos(pos, immediately, runWhenScrollOver);
        }
    }

    public void refocusOnIfNeed(final FocusManager focusManager, int pos) {
        final int finalPos;
        int tmpPos = this.positions.checkPos(pos);
        if (-1 == tmpPos) {
            finalPos = this.positions.getPosAfterNotify();
        } else {
            finalPos = tmpPos;
        }
        if (getAdapter().getItemCount() > 0 && finalPos != -1) {
            selectOn(finalPos, new Runnable() {
                public void run() {
                    if (!FocusFakeListView.this.getNode().isNodeHasFocus()) {
                        FocusFakeListView.this.innerFocusOn(finalPos);
                    } else if (focusManager != null) {
                        focusManager.focusOn(FocusFakeListView.this.getNode());
                    }
                }
            });
        }
    }

    public void refocusOnIfNeed(FocusManager focusManager) {
        refocusOnIfNeed(focusManager, this.positions.getPosAfterNotify());
    }

    public class Positions extends Observable {
        private int now = -1;
        private int old = -1;
        private int[] unSelectableItems = null;

        public Positions() {
        }

        public void clear() {
            this.now = -1;
            this.old = -1;
        }

        /* access modifiers changed from: private */
        public boolean setNow(int focusPosition, Runnable beforeSetNotify) {
            ZpLogger.d(FocusFakeListView.TAG, FocusFakeListView.this.tagObjHashCode() + " " + hashCode() + " setNow " + focusPosition);
            this.old = this.now;
            if (focusPosition == this.now) {
                return false;
            }
            this.now = focusPosition;
            ZpLogger.d(FocusFakeListView.TAG, FocusFakeListView.this.tagObjHashCode() + " " + hashCode() + " setNow " + focusPosition + " success !");
            if (beforeSetNotify != null) {
                try {
                    beforeSetNotify.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            setChanged();
            notifyObservers();
            return true;
        }

        public int getNow() {
            return this.now;
        }

        public int getOld() {
            return this.old;
        }

        private boolean isPosUnSelectable(int pos) {
            if (this.unSelectableItems != null) {
                for (int i : this.unSelectableItems) {
                    if (i == pos) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void setUnSelectableItem(int... index) {
            this.unSelectableItems = index;
        }

        public int getPosAfterNotify() {
            if (FocusFakeListView.this.getAdapter().getItemCount() <= 0) {
                return -1;
            }
            if (getNow() < 0 || getNow() >= FocusFakeListView.this.getAdapter().getItemCount()) {
                int pos = FocusFakeListView.this.getAdapter().getItemCount() - 1;
                if (isPosUnSelectable(pos)) {
                    pos = getNextDownPos(pos);
                }
                if (isPosUnSelectable(pos)) {
                    return getNextUpPos(pos);
                }
                return pos;
            }
            int pos2 = getNow();
            if (isPosUnSelectable(pos2)) {
                pos2 = getNextDownPos(pos2);
            }
            if (isPosUnSelectable(pos2)) {
                return getNextUpPos(pos2);
            }
            return pos2;
        }

        /* access modifiers changed from: private */
        public int checkPos(int pos) {
            if (pos < 0) {
                pos = 0;
            }
            if (pos >= FocusFakeListView.this.getAdapter().getItemCount()) {
                pos = FocusFakeListView.this.getAdapter().getItemCount() - 1;
            }
            if (!isPosUnSelectable(pos)) {
                return pos;
            }
            return -1;
        }

        private int getNextDownPos(int pos) {
            int step = 1;
            int now2 = pos;
            int newPos = now2;
            while (true) {
                int tmp = now2 + step;
                if (tmp >= FocusFakeListView.this.getAdapter().getItemCount()) {
                    return newPos;
                }
                if (!isPosUnSelectable(tmp)) {
                    return tmp;
                }
                step++;
            }
        }

        /* access modifiers changed from: private */
        public int getNextUpPos(int pos) {
            int step = -1;
            int now2 = pos;
            int newPos = now2;
            while (true) {
                int tmp = now2 + step;
                if (tmp < 0) {
                    return newPos;
                }
                if (!isPosUnSelectable(tmp)) {
                    return tmp;
                }
                step--;
            }
        }

        /* access modifiers changed from: private */
        public boolean hasNextDownPos(int pos) {
            int step = 1;
            int now2 = pos;
            while (true) {
                int tmp = now2 + step;
                if (tmp >= FocusFakeListView.this.getAdapter().getItemCount()) {
                    return false;
                }
                if (!isPosUnSelectable(tmp)) {
                    return true;
                }
                step++;
            }
        }

        /* access modifiers changed from: private */
        public boolean hasNextUpPos(int pos) {
            int step = -1;
            int now2 = pos;
            while (true) {
                int tmp = now2 + step;
                if (tmp < 0) {
                    return false;
                }
                if (!isPosUnSelectable(tmp)) {
                    return true;
                }
                step--;
            }
        }
    }

    public Positions getPositions() {
        return this.positions;
    }

    private class FocusNodeProxy implements FocusFinder, FocusConsumer, FocusNode.NodeOuterState {
        FindNextStuff findNextStuffCache;

        private FocusNodeProxy() {
            this.findNextStuffCache = null;
        }

        private class FindNextStuff {
            static final int FLAG_ALREADY_FOCUS_ON = 32;
            static final int FLAG_ARRIVE_FIRST = 4;
            static final int FLAG_ARRIVE_LAST = 8;
            static final int FLAG_EVENT_EAT = 16;
            static final int FLAG_KEY_DOWN = 2;
            static final int FLAG_KEY_UP = 1;
            KeyEvent event;
            int flag = 0;
            int keyCode;
            FocusFinder.Routine routine;
            FocusNode target;

            public FindNextStuff(FocusNode target2, FocusFinder.Routine routine2, int keyCode2, KeyEvent event2) {
                this.target = target2;
                this.routine = routine2;
                this.keyCode = keyCode2;
                this.event = event2;
            }

            public boolean isEventConsumed() {
                return (this.flag & 16) == 16;
            }

            public boolean isNeedScrollForKeyUp() {
                return (this.flag & 1) == 1;
            }

            public boolean isNeedScrollForKeyUDown() {
                return (this.flag & 2) == 2;
            }

            public boolean isArriveTop() {
                return (this.flag & 4) == 4;
            }

            public boolean isArriveBottom() {
                return (this.flag & 8) == 8;
            }

            public boolean isAlreadyFocusOn() {
                return (this.flag & 32) == 32;
            }
        }

        private void dealWithFindNextStuff(FindNextStuff findNextStuff) {
            if (FocusFakeListView.this.focusNode.isNodeHasFocus()) {
                ZpLogger.d(FocusFakeListView.TAG, FocusFakeListView.this.tagObjHashCode() + " dealWithFindNextStuff has focus");
                findNextStuff.flag |= 32;
                if (findNextStuff.target != null) {
                    findNextStuff.flag |= 16;
                } else if (findNextStuff.keyCode == 20) {
                    if (FocusFakeListView.this.positions.hasNextDownPos(FocusFakeListView.this.positions.getNow())) {
                        findNextStuff.flag |= 16;
                        findNextStuff.flag |= 2;
                    } else {
                        findNextStuff.flag |= 8;
                    }
                } else if (findNextStuff.keyCode == 19) {
                    if (FocusFakeListView.this.positions.hasNextUpPos(FocusFakeListView.this.positions.getNow())) {
                        findNextStuff.flag |= 16;
                        findNextStuff.flag |= 1;
                    } else {
                        findNextStuff.flag |= 4;
                    }
                }
            } else {
                ZpLogger.d(FocusFakeListView.TAG, FocusFakeListView.this.tagObjHashCode() + " dealWithFindNextStuff no focus");
                if (findNextStuff.target != null) {
                    findNextStuff.flag |= 16;
                }
            }
            if (findNextStuff.isArriveTop() || findNextStuff.isArriveBottom()) {
                this.findNextStuffCache = null;
                if (FocusFakeListView.this.noNextListener != null) {
                    FocusFakeListView.this.noNextListener.onNoNext(findNextStuff.keyCode, findNextStuff.event);
                }
            } else if (!findNextStuff.isEventConsumed()) {
                this.findNextStuffCache = null;
            }
        }

        private void doFocusEnter() {
            Runnable focusOnTask = new Runnable() {
                public void run() {
                    FocusFakeListView.this.focusNode.rebuildChildren();
                    FocusNode posNode = FocusFakeListView.this.getFocusNode(FocusFakeListView.this.positions.getNow());
                    if (posNode != null) {
                        for (int i = 0; i < FocusFakeListView.this.focusNode.getFocusChildren().size(); i++) {
                            Pair<FocusNode, Rect> tmp = FocusFakeListView.this.focusNode.getFocusChildren().get(i);
                            if (tmp.first == posNode) {
                                FocusUtils.buildFocusPath(((FocusNode) tmp.first).findFittestLeaf(), FocusFakeListView.this.focusNode);
                                FocusUtils.focusEnterToLeaf((FocusNode) tmp.first);
                                FocusFakeListView.this.scrollToPos(FocusFakeListView.this.positions.getNow(), true, (Runnable) null);
                            } else {
                                FocusUtils.focusLeaveToLeaf((FocusNode) tmp.first);
                            }
                        }
                    }
                }
            };
            if (this.findNextStuffCache != null) {
                if (this.findNextStuffCache.target != null) {
                    FakeListView.ViewHolder vh = FocusFakeListView.this.getViewHolder(this.findNextStuffCache.target.getBinder().getView());
                    if (vh == null) {
                        focusOnTask.run();
                    } else if (vh.getPosNow() == FocusFakeListView.this.positions.getNow() || FocusFakeListView.this.positions.getNow() == -1 || this.findNextStuffCache.isAlreadyFocusOn()) {
                        FocusUtils.buildFocusPath(this.findNextStuffCache.target, FocusFakeListView.this.focusNode);
                        FocusUtils.focusEnterToLeaf(FocusFakeListView.this.focusNode);
                        FocusFakeListView.this.selectOn(vh.getPosNow(), (Runnable) null);
                    } else {
                        focusOnTask.run();
                    }
                } else if (this.findNextStuffCache.isNeedScrollForKeyUp()) {
                    FocusFakeListView.this.selectOn(FocusFakeListView.this.positions.getNow() - 1, (Runnable) null);
                } else if (this.findNextStuffCache.isNeedScrollForKeyUDown()) {
                    FocusFakeListView.this.selectOn(FocusFakeListView.this.positions.getNow() + 1, (Runnable) null);
                }
                this.findNextStuffCache = null;
                return;
            }
            focusOnTask.run();
        }

        private void doFocusLeave() {
            for (int i = 0; i < FocusFakeListView.this.focusNode.getFocusChildren().size(); i++) {
                FocusUtils.focusLeaveToLeaf((FocusNode) FocusFakeListView.this.focusNode.getFocusChildren().get(i).first);
            }
        }

        private void doFocusClick() {
            FocusNode posNode = FocusFakeListView.this.getFocusNode(FocusFakeListView.this.positions.getNow());
            if (posNode != null) {
                for (int i = 0; i < FocusFakeListView.this.focusNode.getFocusChildren().size(); i++) {
                    Pair<FocusNode, Rect> tmp = FocusFakeListView.this.focusNode.getFocusChildren().get(i);
                    if (tmp.first == posNode) {
                        FocusUtils.focusClickToLeaf((FocusNode) tmp.first);
                        return;
                    }
                }
            }
        }

        public boolean onFocusEnter() {
            doFocusEnter();
            if (FocusFakeListView.this.focusConsumer != null) {
                return FocusFakeListView.this.focusConsumer.onFocusEnter();
            }
            return true;
        }

        public boolean onFocusLeave() {
            doFocusLeave();
            if (FocusFakeListView.this.focusConsumer != null) {
                return FocusFakeListView.this.focusConsumer.onFocusLeave();
            }
            return true;
        }

        public boolean onFocusClick() {
            doFocusClick();
            if (FocusFakeListView.this.focusConsumer != null) {
                return FocusFakeListView.this.focusConsumer.onFocusClick();
            }
            return true;
        }

        public boolean onFocusDraw(Canvas canvas) {
            return false;
        }

        public FocusNode findNext(FocusFinder.Routine routine, int keyCode, KeyEvent event) {
            FocusNode target = FocusFakeListView.this.focusNode.doFindNext(routine, keyCode, event);
            ZpLogger.d(FocusFakeListView.TAG, FocusFakeListView.this.tagObjHashCode() + " findNext " + (target != null ? Integer.valueOf(target.hashCode()) : Constant.NULL));
            this.findNextStuffCache = new FindNextStuff(target, routine, keyCode, event);
            dealWithFindNextStuff(this.findNextStuffCache);
            if (this.findNextStuffCache == null || !this.findNextStuffCache.isEventConsumed()) {
                return null;
            }
            return FocusFakeListView.this.focusNode;
        }

        public FocusNode findFittestLeaf() {
            return FocusFakeListView.this.focusNode;
        }

        public boolean isBusy() {
            return FocusFakeListView.this.isNotifyDoing() || FocusFakeListView.this.isScrollDoing();
        }
    }
}
