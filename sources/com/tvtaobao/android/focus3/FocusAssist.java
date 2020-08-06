package com.tvtaobao.android.focus3;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import com.bftv.fui.constantplugin.Constant;
import com.tvtaobao.android.focus3.FocusLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FocusAssist {
    public static final String NAME_FOCUS_STYLE_A = "NAME_FOCUS_STYLE_A";
    public static final String NAME_FOCUS_STYLE_B = "NAME_FOCUS_STYLE_B";
    /* access modifiers changed from: private */
    public static final String TAG = FocusAssist.class.getSimpleName();
    private static HashMap<Window, FocusAssist> totalWFPairs = new HashMap<>();
    /* access modifiers changed from: private */
    public FocusSearchRuleRuler defaultFocusSearchRuleRuler;
    /* access modifiers changed from: private */
    public HashMap<String, ExtClickClient> extClickClients = new HashMap<>();
    /* access modifiers changed from: private */
    public HashMap<String, ExtKeyEventClient> extKeyEventClients = new HashMap<>();
    /* access modifiers changed from: private */
    public WeakReference<FocusLayout> fl = null;
    /* access modifiers changed from: private */
    public FocusSearchRuleRuler focusSearchRuleRuler;
    /* access modifiers changed from: private */
    public ArrayList<FocusSearchRule> focusSearchRules = new ArrayList<>();
    /* access modifiers changed from: private */
    public KeyEventDispatchDelegate keyEventDispatchDelegate;
    private ViewTreeObserver.OnGlobalFocusChangeListener onGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            Focus3Logger.i(FocusAssist.TAG, ".onGlobalFocusChanged()");
            try {
                FocusAssist.this.doFocusChangedDispatch(oldFocus, newFocus);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            Focus3Logger.i(FocusAssist.TAG, ".onGlobalFocusChanged() done");
        }
    };
    private ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
        public void onScrollChanged() {
            Focus3Logger.i(FocusAssist.TAG, ".onScrollChanged() ");
            try {
                FocusAssist.this.doScrollChangedDispatch();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Focus3Logger.i(FocusAssist.TAG, ".onScrollChanged() done");
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<RequestFocusRule> requestFocusRules = new ArrayList<>();
    private Queue<Runnable> unregisterTasks = new ArrayDeque();
    /* access modifiers changed from: private */
    public HashMap<String, FocusNode> vfMap = new HashMap<>();
    private WeakReference<Window> w = null;

    public interface ExtClickListener {
        void onClick(View view);
    }

    public interface ExtKeyEventListener {
        void onKeyEvent(KeyEvent keyEvent);
    }

    public interface FocusListener {
        void onFocusIn(View view, View view2, View view3);

        void onFocusOut(View view, View view2, View view3);
    }

    public interface FocusSearchRule {
        String getRuleName();

        View onFocusSearch(View view, int i, View view2);
    }

    public interface FocusSearchRuleRuler {
        View onRuleApplied(View view, int i, View view2, List<Pair<FocusSearchRule, View>> list, Pair<FocusSearchRule, View> pair);
    }

    public interface KeyEventDispatchDelegate {
        boolean dispatchKeyEvent(KeyEvent keyEvent);
    }

    public interface RequestFocusRule {
        String getRuleName();

        boolean requestFocus(int i, Rect rect);
    }

    private FocusAssist() {
    }

    private FocusAssist(Window dfsdgkjw) {
        if (dfsdgkjw == null) {
            throw new RuntimeException("window can not be null");
        }
        this.w = new WeakReference<>(dfsdgkjw);
        init();
    }

    private void registerListeners() {
        Focus3Logger.i(TAG, ".registerListeners ");
        if (this.w != null && this.w.get() != null && ((Window) this.w.get()).getDecorView() != null && ((Window) this.w.get()).getDecorView().getViewTreeObserver() != null) {
            ((Window) this.w.get()).getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this.onGlobalFocusChangeListener);
            ((Window) this.w.get()).getDecorView().getViewTreeObserver().addOnScrollChangedListener(this.onScrollChangedListener);
        }
    }

    private void unregisterListeners() {
        Focus3Logger.i(TAG, ".unregisterListeners ");
        if (this.w != null && this.w.get() != null && ((Window) this.w.get()).getDecorView() != null && ((Window) this.w.get()).getDecorView().getViewTreeObserver() != null) {
            ((Window) this.w.get()).getDecorView().getViewTreeObserver().removeOnGlobalFocusChangeListener(this.onGlobalFocusChangeListener);
            ((Window) this.w.get()).getDecorView().getViewTreeObserver().removeOnScrollChangedListener(this.onScrollChangedListener);
        }
    }

    private void enqueueDeleteTask(Runnable runnable) {
        synchronized (this.unregisterTasks) {
            this.unregisterTasks.add(runnable);
        }
    }

    private Runnable dequeueDeleteTask() {
        Runnable rtn;
        synchronized (this.unregisterTasks) {
            rtn = this.unregisterTasks.poll();
        }
        return rtn;
    }

    /* access modifiers changed from: private */
    public void manageDeleteTaskQueue() {
        while (!this.unregisterTasks.isEmpty()) {
            Runnable task = dequeueDeleteTask();
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static FocusAssist obtain(Window window) {
        FocusAssist rtn = totalWFPairs.get(window);
        if (rtn != null) {
            return rtn;
        }
        FocusAssist rtn2 = new FocusAssist(window);
        totalWFPairs.put(window, rtn2);
        return rtn2;
    }

    public static void release(Window window) {
        FocusAssist f = totalWFPairs.remove(window);
        if (f != null) {
            f.clear();
        }
    }

    public FocusAssist register(View view) {
        register(view, (Drawable) null);
        return this;
    }

    public FocusAssist register(View view, FocusListener focusListener) {
        register(view, focusListener, (Drawable) null, (Rect) null);
        return this;
    }

    public FocusAssist register(View view, Drawable drawable) {
        register(view, (FocusListener) null, drawable, (Rect) null);
        return this;
    }

    public FocusAssist register(View view, Drawable drawable, Rect focusPadding) {
        register(view, (FocusListener) null, drawable, focusPadding);
        return this;
    }

    public FocusAssist register(View view, FocusListener focusListener, Drawable drawable, Rect focusPadding) {
        synchronized (this.vfMap) {
            this.vfMap.put(calculateKey(view), new FocusNode(this, view, focusListener, drawable, focusPadding));
        }
        return this;
    }

    public FocusAssist unregister(final View view) {
        enqueueDeleteTask(new Runnable() {
            public void run() {
                synchronized (FocusAssist.this.vfMap) {
                    FocusAssist.this.vfMap.remove(FocusAssist.this.calculateKey(view));
                }
                Focus3Logger.i(FocusAssist.TAG, ".unregister vfMap:" + FocusAssist.this.vfMap.size());
            }
        });
        return this;
    }

    public FocusAssist registerExtKeyEventListener(View view, ExtKeyEventListener keyEventPreDispatchListener) {
        synchronized (this.extKeyEventClients) {
            this.extKeyEventClients.put(calculateKey(view), new ExtKeyEventClient(view, keyEventPreDispatchListener));
        }
        return this;
    }

    public FocusAssist unregisterExtKeyEventListener(final View view) {
        enqueueDeleteTask(new Runnable() {
            public void run() {
                synchronized (FocusAssist.this.extKeyEventClients) {
                    FocusAssist.this.extKeyEventClients.remove(FocusAssist.this.calculateKey(view));
                }
                Focus3Logger.i(FocusAssist.TAG, ".unregister extKeyEventClients:" + FocusAssist.this.extKeyEventClients.size());
            }
        });
        return this;
    }

    public FocusAssist registerExtClickListener(View view, ExtClickListener clickListener) {
        Focus3Logger.i(TAG, ".registerExtClickListener 4 " + Focus3Util.getString(view));
        synchronized (this.extClickClients) {
            this.extClickClients.put(calculateKey(view), new ExtClickClient(view, clickListener));
        }
        return this;
    }

    public FocusAssist unregisterExtClickListener(final View view) {
        enqueueDeleteTask(new Runnable() {
            public void run() {
                synchronized (FocusAssist.this.extClickClients) {
                    FocusAssist.this.extClickClients.remove(FocusAssist.this.calculateKey(view));
                }
                Focus3Logger.i(FocusAssist.TAG, ".unregister extClickClients:" + FocusAssist.this.extClickClients.size());
            }
        });
        return this;
    }

    public FocusAssist registerRequestFocusRule(RequestFocusRule requestFocusRule) {
        synchronized (this.requestFocusRules) {
            this.requestFocusRules.remove(requestFocusRule);
            if (requestFocusRule != null) {
                this.requestFocusRules.add(requestFocusRule);
            }
        }
        return this;
    }

    public FocusAssist unregisterRequestFocusRule(final RequestFocusRule requestFocusRule) {
        enqueueDeleteTask(new Runnable() {
            public void run() {
                synchronized (FocusAssist.this.requestFocusRules) {
                    FocusAssist.this.requestFocusRules.remove(requestFocusRule);
                }
                Focus3Logger.i(FocusAssist.TAG, ".unregister requestFocusRules:" + FocusAssist.this.requestFocusRules.size());
            }
        });
        return this;
    }

    public FocusAssist registerFocusSearchRule(FocusSearchRule focusSearchRule) {
        synchronized (this.focusSearchRules) {
            this.focusSearchRules.remove(focusSearchRule);
            if (focusSearchRule != null) {
                this.focusSearchRules.add(focusSearchRule);
            }
        }
        return this;
    }

    public FocusAssist unregisterFocusSearchRule(final FocusSearchRule focusSearchRule) {
        enqueueDeleteTask(new Runnable() {
            public void run() {
                synchronized (FocusAssist.this.focusSearchRules) {
                    FocusAssist.this.focusSearchRules.remove(focusSearchRule);
                }
                Focus3Logger.i(FocusAssist.TAG, ".unregister focusSearchRules:" + FocusAssist.this.focusSearchRules.size());
            }
        });
        return this;
    }

    public FocusSearchRuleRuler getFocusSearchRuleRuler() {
        return this.focusSearchRuleRuler;
    }

    public FocusAssist setFocusSearchRuleRuler(FocusSearchRuleRuler focusSearchRuleRuler2) {
        this.focusSearchRuleRuler = focusSearchRuleRuler2;
        return this;
    }

    public boolean reqFocusSearchRulePos(FocusSearchRule rule, int pos) {
        if (pos >= 0 && pos < this.focusSearchRules.size() && rule != null) {
            synchronized (this.focusSearchRules) {
                int oldPos = -1;
                int i = 0;
                while (true) {
                    if (i < this.focusSearchRules.size()) {
                        if (this.focusSearchRules.get(i) == rule && pos != i) {
                            oldPos = i;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                if (oldPos != -1 && oldPos != pos) {
                    this.focusSearchRules.remove(oldPos);
                    this.focusSearchRules.add(pos, rule);
                    return true;
                }
            }
        }
        return false;
    }

    public KeyEventDispatchDelegate getKeyEventDispatchDelegate() {
        return this.keyEventDispatchDelegate;
    }

    public void setKeyEventDispatchDelegate(KeyEventDispatchDelegate keyEventDispatchDelegate2) {
        this.keyEventDispatchDelegate = keyEventDispatchDelegate2;
    }

    public void lockFocusRoutine() {
        FocusLayout fl2 = getFocusLayout();
        if (fl2 != null) {
            fl2.lockFocusRoutine();
        }
    }

    public void unlockFocusRoutine() {
        FocusLayout fl2 = getFocusLayout();
        if (fl2 != null) {
            fl2.unlockFocusRoutine();
        }
    }

    public boolean isFocusRoutineLocked() {
        FocusLayout fl2 = getFocusLayout();
        if (fl2 != null) {
            return fl2.isFocusRoutineLocked();
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public FocusLayout getFocusLayout() {
        if (this.fl == null) {
            return null;
        }
        return (FocusLayout) this.fl.get();
    }

    private void clear() {
        Focus3Logger.i(TAG, ".clear ");
        unregisterListeners();
        if (this.vfMap != null) {
            synchronized (this.vfMap) {
                this.vfMap.clear();
            }
        }
        if (this.extKeyEventClients != null) {
            synchronized (this.extKeyEventClients) {
                this.extKeyEventClients.clear();
            }
        }
        if (this.extClickClients != null) {
            synchronized (this.extClickClients) {
                this.extClickClients.clear();
            }
        }
        if (this.focusSearchRules != null) {
            synchronized (this.focusSearchRules) {
                this.focusSearchRules.clear();
            }
        }
        if (this.requestFocusRules != null) {
            synchronized (this.requestFocusRules) {
                this.requestFocusRules.clear();
            }
        }
    }

    private void init() {
        Window window = (Window) this.w.get();
        if (window != null && window.getDecorView() != null && window.getDecorView().getViewTreeObserver() != null) {
            registerListeners();
            this.fl = new WeakReference<>(FocusLayout.inject(window));
            if (this.fl.get() != null) {
                ((FocusLayout) this.fl.get()).setDrawCallBack(new FocusLayout.DrawCallBack() {
                    public void beforeDraw(Canvas canvas) {
                        Focus3Logger.i(FocusAssist.TAG, ".beforeDraw()");
                    }

                    public void afterDraw(Canvas canvas) {
                        Focus3Logger.i(FocusAssist.TAG, ".afterDraw()");
                        try {
                            synchronized (FocusAssist.this.vfMap) {
                                Iterator<Map.Entry<String, FocusNode>> iterator = FocusAssist.this.vfMap.entrySet().iterator();
                                while (iterator != null && iterator.hasNext()) {
                                    Map.Entry<String, FocusNode> entry = iterator.next();
                                    if (entry == null || entry.getValue() == null || !entry.getValue().isNodeAvailable()) {
                                        iterator.remove();
                                    } else {
                                        entry.getValue().doFocusStateDraw(canvas);
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        Focus3Logger.i(FocusAssist.TAG, ".afterDraw() done");
                    }
                });
                ((FocusLayout) this.fl.get()).setPreKeyEventDispatch(new FocusLayout.PreKeyEventDispatch() {
                    public void onPreKeyEventDispatch(KeyEvent keyEvent) {
                        Focus3Logger.i(FocusAssist.TAG, ".onPreKeyEventDispatch()");
                        try {
                            FocusAssist.this.manageDeleteTaskQueue();
                            synchronized (FocusAssist.this.extKeyEventClients) {
                                Iterator<Map.Entry<String, ExtKeyEventClient>> iterator = FocusAssist.this.extKeyEventClients.entrySet().iterator();
                                while (iterator != null && iterator.hasNext()) {
                                    Map.Entry<String, ExtKeyEventClient> entry = iterator.next();
                                    if (entry.getValue() == null || entry.getValue().v == null || !Focus3Util.isBinA((View) FocusAssist.this.fl.get(), (View) entry.getValue().v.get())) {
                                        iterator.remove();
                                    } else {
                                        try {
                                            entry.getValue().listener.onKeyEvent(keyEvent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                        }
                        Focus3Logger.i(FocusAssist.TAG, ".onPreKeyEventDispatch() done");
                    }
                });
                ((FocusLayout) this.fl.get()).setViewLifeCircleListener(new FocusLayout.ViewLifeCircleListener() {
                    public void onAttach() {
                    }

                    public void onDetach() {
                    }
                });
                ((FocusLayout) this.fl.get()).setClickHappenListener(new FocusLayout.ClickHappenListener() {
                    public void onClickHappen() {
                        Focus3Logger.i(FocusAssist.TAG, ".onClickHappen()");
                        try {
                            FocusAssist.this.manageDeleteTaskQueue();
                            View currFocusOn = null;
                            if (!(FocusAssist.this.fl == null || FocusAssist.this.fl.get() == null)) {
                                currFocusOn = ((FocusLayout) FocusAssist.this.fl.get()).findFocus();
                            }
                            List<ExtClickClient> tmp = new ArrayList<>();
                            synchronized (FocusAssist.this.extClickClients) {
                                Iterator<Map.Entry<String, ExtClickClient>> iterator = FocusAssist.this.extClickClients.entrySet().iterator();
                                while (iterator != null && iterator.hasNext()) {
                                    Map.Entry<String, ExtClickClient> entry = iterator.next();
                                    if (entry.getValue() == null || entry.getValue().v == null || !Focus3Util.isBinA((View) FocusAssist.this.fl.get(), (View) entry.getValue().v.get())) {
                                        iterator.remove();
                                    } else if (entry.getValue().v.get() != currFocusOn) {
                                        try {
                                            if (Focus3Util.isBinA((View) entry.getValue().v.get(), currFocusOn)) {
                                                tmp.add(entry.getValue());
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                            for (int i = tmp.size() - 1; i >= 0; i--) {
                                if (tmp.get(i) == null) {
                                    tmp.remove(i);
                                } else if (tmp.get(i).v == null) {
                                    tmp.remove(i);
                                } else if (tmp.get(i).v.get() == null) {
                                    tmp.remove(i);
                                } else if (tmp.get(i).listener == null) {
                                    tmp.remove(i);
                                }
                            }
                            Collections.sort(tmp, new Comparator<ExtClickClient>() {
                                public int compare(ExtClickClient o1, ExtClickClient o2) {
                                    if (Focus3Util.isBinA((View) o1.v.get(), (View) o2.v.get())) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });
                            for (int i2 = tmp.size() - 1; i2 >= 0; i2--) {
                                tmp.get(i2).listener.onClick((View) tmp.get(i2).v.get());
                            }
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                        }
                        Focus3Logger.i(FocusAssist.TAG, ".onClickHappen() done");
                    }
                });
                this.defaultFocusSearchRuleRuler = new FocusSearchRuleRuler() {
                    public View onRuleApplied(View focused, int direction, View androidFocusSearchResult, List<Pair<FocusSearchRule, View>> ruleAndRltList, Pair<FocusSearchRule, View> pair) {
                        View view;
                        String str = null;
                        View rtn = androidFocusSearchResult;
                        Pair<FocusSearchRule, View> picked = null;
                        if (ruleAndRltList != null) {
                            int i = 0;
                            while (true) {
                                if (i >= ruleAndRltList.size()) {
                                    break;
                                } else if (ruleAndRltList.get(i).second != androidFocusSearchResult) {
                                    picked = ruleAndRltList.get(i);
                                    break;
                                } else {
                                    i++;
                                }
                            }
                        }
                        if (picked != null) {
                            rtn = (View) picked.second;
                        }
                        String access$000 = FocusAssist.TAG;
                        StringBuilder append = new StringBuilder().append(" defaultRulerRlt: ");
                        if (picked != null) {
                            view = (View) picked.second;
                        } else {
                            view = null;
                        }
                        StringBuilder append2 = append.append(Focus3Util.getString(view)).append(", pickedRuleName: ");
                        if (picked != null) {
                            str = ((FocusSearchRule) picked.first).getRuleName();
                        }
                        Focus3Logger.i(access$000, append2.append(str).toString());
                        if (FocusAssist.this.focusSearchRuleRuler == null) {
                            return rtn;
                        }
                        View rtn2 = FocusAssist.this.focusSearchRuleRuler.onRuleApplied(focused, direction, androidFocusSearchResult, ruleAndRltList, picked);
                        Focus3Logger.i(FocusAssist.TAG, " outRulerRlt: " + Focus3Util.getString(rtn2));
                        return rtn2;
                    }
                };
                ((FocusLayout) this.fl.get()).setFocusSearchCallBack(new FocusLayout.FocusSearchCallBack() {
                    public View focusSearch(View focused, int direction, View androidFocusSearchResult) {
                        Focus3Logger.i(FocusAssist.TAG, ".focusSearch() ruleNum:" + FocusAssist.this.focusSearchRules.size());
                        FocusAssist.this.manageDeleteTaskQueue();
                        View view = androidFocusSearchResult;
                        List<Pair<FocusSearchRule, View>> ruleAndRltList = new ArrayList<>();
                        synchronized (FocusAssist.this.focusSearchRules) {
                            int i = 0;
                            while (i < FocusAssist.this.focusSearchRules.size()) {
                                try {
                                    FocusSearchRule rule = (FocusSearchRule) FocusAssist.this.focusSearchRules.get(i);
                                    if (rule != null) {
                                        Focus3Logger.i(FocusAssist.TAG, " rule(" + i + "," + rule.getRuleName() + ")");
                                        View ruleRlt = rule.onFocusSearch(focused, direction, androidFocusSearchResult);
                                        Focus3Logger.i(FocusAssist.TAG, " rule(" + i + "," + rule.getRuleName() + ") ruleRlt:" + Focus3Util.getString(ruleRlt) + " normalRlt:" + Focus3Util.getString(androidFocusSearchResult));
                                        ruleAndRltList.add(Pair.create(rule, ruleRlt));
                                    }
                                    i++;
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        View mergedResult = FocusAssist.this.defaultFocusSearchRuleRuler.onRuleApplied(focused, direction, androidFocusSearchResult, ruleAndRltList, (Pair<FocusSearchRule, View>) null);
                        Focus3Logger.i(FocusAssist.TAG, ".focusSearch() done " + Focus3Util.getString(mergedResult));
                        return mergedResult;
                    }
                });
                ((FocusLayout) this.fl.get()).setRequestFocusCallBack(new FocusLayout.RequestFocusCallBack() {
                    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
                        Focus3Logger.i(FocusAssist.TAG, ".requestFocus() ruleNum:" + FocusAssist.this.requestFocusRules.size());
                        FocusAssist.this.manageDeleteTaskQueue();
                        boolean rtn = false;
                        synchronized (FocusAssist.this.requestFocusRules) {
                            int i = 0;
                            while (i < FocusAssist.this.requestFocusRules.size()) {
                                try {
                                    RequestFocusRule rule = (RequestFocusRule) FocusAssist.this.requestFocusRules.get(i);
                                    if (rule != null) {
                                        Focus3Logger.i(FocusAssist.TAG, ".requestFocus() rule(" + i + "," + rule.getRuleName() + ")");
                                        rtn = rule.requestFocus(direction, previouslyFocusedRect);
                                        Focus3Logger.i(FocusAssist.TAG, ".requestFocus() rule(" + i + "," + rule.getRuleName() + ") rtn:" + rtn);
                                        if (!rtn) {
                                        }
                                    }
                                    i++;
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Focus3Logger.i(FocusAssist.TAG, ".requestFocus() done");
                        return rtn;
                    }
                });
                ((FocusLayout) this.fl.get()).setDispatchKeyEventInterrupter(new FocusLayout.DispatchKeyEventInterrupter() {
                    public boolean interruptDispatch(KeyEvent event) {
                        if (FocusAssist.this.keyEventDispatchDelegate != null) {
                            return FocusAssist.this.keyEventDispatchDelegate.dispatchKeyEvent(event);
                        }
                        return false;
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public String calculateKey(View view) {
        if (view != null) {
            return view.getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + view.hashCode();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void doFocusChangedDispatch(View oldFocus, View newFocus) {
        if (!(this.fl == null || this.fl.get() == null)) {
            ((FocusLayout) this.fl.get()).postInvalidate();
        }
        manageDeleteTaskQueue();
        synchronized (this.vfMap) {
            Iterator<Map.Entry<String, FocusNode>> iterator = this.vfMap.entrySet().iterator();
            while (iterator != null && iterator.hasNext()) {
                Map.Entry<String, FocusNode> entry = iterator.next();
                if (entry == null || entry.getValue() == null || !entry.getValue().isNodeAvailable()) {
                    iterator.remove();
                } else {
                    entry.getValue().doFocusChangedDispatch(oldFocus, newFocus);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void doScrollChangedDispatch() {
        if (this.fl != null && this.fl.get() != null) {
            ((FocusLayout) this.fl.get()).postInvalidate();
        }
    }
}
