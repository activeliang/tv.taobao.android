package com.yunos.tvtaobao.biz.focus_impl;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.focus_impl.FocusFinder;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FocusNode implements FocusConsumer, FocusFinder {
    public static boolean DEBUG = false;
    private static final String TAG = FocusNode.class.getSimpleName();
    private long buildCount = 0;
    private FocusFinder.Routine currRoutine;
    private FocusNode currRoutineResult;
    private long focusBalance = 0;
    private List<Pair<FocusNode, Rect>> focusChildren = new ArrayList();
    private long focusClickCount = 0;
    private FocusConsumer focusConsumer = null;
    private long focusDrawCount = 0;
    private long focusEnterCount = 0;
    private FocusFinder focusFinder = null;
    private long focusLeaveCount = 0;
    private List<WeakReference<FocusNode>> history = new ArrayList(4);
    private int historyIterator = 0;
    private FocusNode innerNode = null;
    private NodeInnerState innerState = NodeInnerState.idle;
    private boolean isRoot = false;
    private Binder mBinder;
    private boolean nodeFocusable = true;
    private boolean nodeHasFocus = false;
    private NodeOuterState nodeOuterState = null;
    Paint paint;
    private FocusNode parentNode = null;
    private long priority = 0;
    private Rect rectInParentNode = null;
    private RoutineCallBack routineCallBack;

    public interface Binder {
        FocusNode getNode();

        View getView();
    }

    public enum NodeInnerState {
        idle,
        findNextIng,
        findNextDone,
        focusEnterIng,
        focusEnterDone,
        focusLeaveIng,
        focusLeaveDone,
        focusClickIng,
        focusClickDone
    }

    public interface NodeOuterState {
        boolean isBusy();
    }

    public interface RoutineCallBack {
        void onPostFindNext();

        void onPostFittestLeaf();

        void onPreFindNext();

        void onPreFittestLeaf();
    }

    private FocusNode() {
    }

    public FocusNode(Binder mBinder2) {
        this.mBinder = mBinder2;
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(5.0f);
        this.paint.setColor(-16711936);
        this.paint.setPathEffect(new DashPathEffect(new float[]{1.0f, 4.0f}, 0.0f));
    }

    public long getBuildCount() {
        return this.buildCount;
    }

    private void invalidateAttachView() {
        if (this.mBinder != null && this.mBinder.getView() != null) {
            invalidateViewTree(this.mBinder.getView());
        }
    }

    private void invalidateViewTree(View view) {
        if (view != null) {
            view.invalidate();
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    invalidateViewTree(((ViewGroup) view).getChildAt(i));
                }
            }
        }
    }

    public boolean onFocusEnter() {
        this.innerState = NodeInnerState.focusEnterIng;
        this.nodeHasFocus = true;
        this.focusEnterCount++;
        this.focusBalance++;
        ZpLogger.d(getClass().getSimpleName(), hashCode() + " : " + "onFocusEnter" + " focusEnterCount:" + this.focusEnterCount + " focusBalance:" + this.focusBalance);
        boolean rtn = false;
        try {
            if (this.focusConsumer != null) {
                rtn = this.focusConsumer.onFocusEnter();
            }
            invalidateAttachView();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.innerState = NodeInnerState.focusEnterDone;
        return rtn;
    }

    public boolean onFocusLeave() {
        this.innerState = NodeInnerState.focusLeaveIng;
        this.nodeHasFocus = false;
        this.focusLeaveCount++;
        this.focusBalance = 0;
        ZpLogger.d(getClass().getSimpleName(), hashCode() + " : " + "onFocusEnter" + " focusLeaveCount:" + this.focusLeaveCount + " focusBalance:" + this.focusBalance);
        boolean rtn = false;
        try {
            if (this.focusConsumer != null) {
                rtn = this.focusConsumer.onFocusLeave();
            }
            invalidateAttachView();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.innerState = NodeInnerState.focusLeaveDone;
        return rtn;
    }

    public boolean onFocusClick() {
        this.innerState = NodeInnerState.focusLeaveIng;
        this.focusClickCount++;
        ZpLogger.d(getClass().getSimpleName(), hashCode() + " : " + "onFocusEnter" + " focusClickCount:" + this.focusClickCount);
        boolean rtn = false;
        try {
            if (this.focusConsumer != null) {
                rtn = this.focusConsumer.onFocusClick();
            }
            invalidateAttachView();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.innerState = NodeInnerState.focusLeaveDone;
        return rtn;
    }

    public boolean onFocusDraw(Canvas canvas) {
        this.focusDrawCount++;
        if (!this.nodeFocusable || !this.nodeHasFocus) {
            return false;
        }
        if (this.focusConsumer != null) {
            return this.focusConsumer.onFocusDraw(canvas);
        }
        if (DEBUG) {
            canvas.drawRect(new Rect(0, 0, this.mBinder.getView().getWidth(), this.mBinder.getView().getHeight()), this.paint);
        }
        return true;
    }

    private void pushToHistory(FocusNode focusNode) {
        if (focusNode != null) {
            if (this.history.isEmpty() || this.history.get(this.history.size() - 1).get() != focusNode) {
                this.history.add(new WeakReference(focusNode));
            }
            if (this.history.size() > 3) {
                this.history.remove(0);
            }
        }
    }

    public FocusNode historyFirst() {
        if (this.history.isEmpty()) {
            return null;
        }
        this.historyIterator = this.history.size() - 1;
        return (FocusNode) this.history.get(this.historyIterator).get();
    }

    public FocusNode historyNext() {
        if (this.history.isEmpty()) {
            return null;
        }
        this.historyIterator--;
        if (this.historyIterator < 0 || this.historyIterator > this.history.size() - 1) {
            return null;
        }
        return (FocusNode) this.history.get(this.historyIterator).get();
    }

    public long getFocusEnterCount() {
        return this.focusEnterCount;
    }

    public long getFocusLeaveCount() {
        return this.focusLeaveCount;
    }

    public long getFocusClickCount() {
        return this.focusClickCount;
    }

    public long getFocusDrawCount() {
        return this.focusDrawCount;
    }

    public long getFocusBalance() {
        return this.focusBalance;
    }

    public NodeInnerState getInnerState() {
        return this.innerState;
    }

    public NodeOuterState getNodeOuterState() {
        return this.nodeOuterState;
    }

    public void setNodeOuterState(NodeOuterState nodeOuterState2) {
        this.nodeOuterState = nodeOuterState2;
    }

    public FocusFinder getFocusFinder() {
        return this.focusFinder;
    }

    public void setFocusFinder(FocusFinder focusFinder2) {
        this.focusFinder = focusFinder2;
    }

    public FocusConsumer getFocusConsumer() {
        return this.focusConsumer;
    }

    public void setFocusConsumer(FocusConsumer focusConsumer2) {
        this.focusConsumer = focusConsumer2;
    }

    public FocusNode getInnerNode() {
        return this.innerNode;
    }

    public void setInnerNode(FocusNode innerNode2) {
        this.innerNode = innerNode2;
        pushToHistory(this.innerNode);
    }

    public long getPriority() {
        return this.priority;
    }

    public void setPriority(long priority2) {
        this.priority = priority2;
    }

    public FocusNode getParentNode() {
        return this.parentNode;
    }

    public void setParentNode(FocusNode parentNode2) {
        this.parentNode = parentNode2;
    }

    public Rect getRectInParentNode() {
        return this.rectInParentNode;
    }

    public void setRectInParentNode(Rect rectInParentNode2) {
        this.rectInParentNode = rectInParentNode2;
    }

    public boolean isNodeFocusable() {
        return this.nodeFocusable;
    }

    public void setNodeFocusable(boolean nodeFocusable2) {
        this.nodeFocusable = nodeFocusable2;
        if (getParentNode() != null) {
            getParentNode().rebuildChildren();
        }
    }

    public boolean isNodeHasFocus() {
        return this.nodeHasFocus;
    }

    public RoutineCallBack getRoutineCallBack() {
        return this.routineCallBack;
    }

    public void setRoutineCallBack(RoutineCallBack routineCallBack2) {
        this.routineCallBack = routineCallBack2;
    }

    public List<Pair<FocusNode, Rect>> getFocusChildren() {
        return this.focusChildren;
    }

    public void rebuildChildren() {
        this.focusChildren = FocusUtils.buildBranch(this);
        this.buildCount++;
        ZpLogger.d(TAG, hashCode() + " rebuildChildren (" + this.focusChildren.size() + "," + this.buildCount + ")");
        clearInnerChildIfNeed();
    }

    private void clearInnerChildIfNeed() {
        if (this.focusChildren != null && this.innerNode != null) {
            boolean find = false;
            Iterator<Pair<FocusNode, Rect>> it = this.focusChildren.iterator();
            while (true) {
                if (it.hasNext()) {
                    Pair<FocusNode, Rect> iterator = it.next();
                    if (iterator != null && iterator.first != null && iterator.first == this.innerNode) {
                        find = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!find) {
                setInnerNode((FocusNode) null);
            }
        }
    }

    private FocusNode findFittestChild() {
        if (this.innerNode != null && FocusUtils.isNodeInParent(this.innerNode, this)) {
            return this.innerNode;
        }
        FocusNode his = historyFirst();
        while (his != null) {
            if (FocusUtils.isNodeInParent(his, this)) {
                return his;
            }
            his = historyNext();
        }
        return null;
    }

    private FocusNode doFindFittestLeaf() {
        FocusNode finalFind = FocusUtils.findPriorityLeaf(this);
        if (finalFind != null) {
            return finalFind;
        }
        for (FocusNode iterator = findFittestChild(); iterator != null; iterator = iterator.findFittestChild()) {
            finalFind = iterator;
        }
        if (finalFind != null) {
            return FocusUtils.findClosestLeaf2LT(finalFind);
        }
        return null;
    }

    public boolean refocusToLeaf() {
        FocusNode leaf = findFittestLeaf();
        if (leaf == null || !FocusUtils.buildFocusPath(leaf, this)) {
            return false;
        }
        FocusUtils.focusEnterToLeaf(this);
        return true;
    }

    public boolean requestFocus(FocusNode stopNode) {
        if (isNodeHasFocus() || !FocusUtils.buildFocusPath(this, stopNode)) {
            return false;
        }
        FocusUtils.focusEnterToLeaf(stopNode);
        return true;
    }

    public Binder getBinder() {
        return this.mBinder;
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public void setRoot(boolean root) {
        this.isRoot = root;
    }

    public void invalidate() {
        View v = getBinder().getView();
        int loop = 20;
        do {
            loop--;
            if (v != null) {
                v.invalidate();
                if (v.getParent() instanceof View) {
                    v = (View) v.getParent();
                }
            }
            if (v == null || this.parentNode == null || v == this.parentNode.getBinder().getView()) {
                return;
            }
        } while (loop >= 0);
    }

    public FocusNode doFindNext(FocusFinder.Routine routine, int keyCode, KeyEvent event) {
        if (!FocusUtils.isLeafFocusNode(this)) {
            if (this.innerNode != null) {
                FocusNode tmp = this.innerNode.findNext(routine, keyCode, event);
                if (tmp != null) {
                    return tmp;
                }
                FocusNode brother = FocusUtils.findInBrothers(this.innerNode, keyCode, event);
                if (brother != null) {
                    FocusNode tmp2 = brother.findNext(routine, keyCode, event);
                    if (tmp2 == null) {
                        return brother;
                    }
                    return tmp2;
                }
            } else {
                FocusNode tmp3 = FocusUtils.findInChildren(this, keyCode, event);
                if (tmp3 != null) {
                    FocusNode leaf = tmp3.findNext(routine, keyCode, event);
                    if (leaf != null) {
                        return leaf;
                    }
                    return tmp3;
                }
            }
            return null;
        } else if (!isNodeFocusable() || isNodeHasFocus()) {
            return null;
        } else {
            return this;
        }
    }

    public FocusNode findNext(FocusFinder.Routine routine, int keyCode, KeyEvent event) {
        ZpLogger.d(TAG, hashCode() + " findNext (" + (routine != null ? Long.valueOf(routine.id) : Constant.NULL) + "," + keyCode + ")");
        this.innerState = NodeInnerState.findNextIng;
        if (this.currRoutine == null || this.currRoutine.id != routine.id) {
            try {
                if (this.routineCallBack != null) {
                    this.routineCallBack.onPreFindNext();
                }
                rebuildChildren();
                this.currRoutine = routine;
                if (this.focusFinder != null) {
                    this.currRoutineResult = this.focusFinder.findNext(routine, keyCode, event);
                } else {
                    this.currRoutineResult = doFindNext(routine, keyCode, event);
                }
                if (this.routineCallBack != null) {
                    this.routineCallBack.onPostFindNext();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            this.innerState = NodeInnerState.findNextDone;
            return this.currRoutineResult;
        }
        this.innerState = NodeInnerState.findNextDone;
        return this.currRoutineResult;
    }

    public FocusNode findFittestLeaf() {
        FocusNode rtn;
        FocusUtils.rebuildTotalPath(this);
        if (this.routineCallBack != null) {
            this.routineCallBack.onPreFittestLeaf();
        }
        if (this.focusFinder != null) {
            rtn = this.focusFinder.findFittestLeaf();
        } else {
            rtn = doFindFittestLeaf();
        }
        if (this.routineCallBack != null) {
            this.routineCallBack.onPostFittestLeaf();
        }
        return rtn;
    }

    public boolean isNodeDoingSomething() {
        if (this.innerState == NodeInnerState.focusLeaveIng || this.innerState == NodeInnerState.focusEnterIng || this.innerState == NodeInnerState.findNextIng || this.innerState == NodeInnerState.focusClickIng) {
            return true;
        }
        if (this.nodeOuterState != null) {
            return this.nodeOuterState.isBusy();
        }
        return false;
    }

    public void resetState() {
        this.focusChildren.clear();
        this.history.clear();
        this.historyIterator = 0;
        this.innerNode = null;
        this.parentNode = null;
        this.rectInParentNode = null;
        this.currRoutine = null;
        this.currRoutineResult = null;
        this.innerState = NodeInnerState.idle;
        this.priority = 0;
        this.buildCount = 0;
        this.focusBalance = 0;
        this.focusEnterCount = 0;
        this.focusLeaveCount = 0;
        this.focusClickCount = 0;
        this.focusDrawCount = 0;
        this.nodeFocusable = true;
        this.nodeHasFocus = false;
        this.isRoot = false;
    }

    public static abstract class RCBImpl implements RoutineCallBack {
        public void onPreFindNext() {
        }

        public void onPostFindNext() {
        }

        public void onPreFittestLeaf() {
        }

        public void onPostFittestLeaf() {
        }
    }
}
