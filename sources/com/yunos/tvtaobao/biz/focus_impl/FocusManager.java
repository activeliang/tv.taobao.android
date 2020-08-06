package com.yunos.tvtaobao.biz.focus_impl;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import com.yunos.tvtaobao.biz.focus_impl.FocusFinder;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public class FocusManager {
    /* access modifiers changed from: private */
    public static String TAG = FocusManager.class.getSimpleName();
    private List<Runnable> cachedTask = new ArrayList();
    private boolean enable = false;
    private FocusContext focusContext;
    /* access modifiers changed from: private */
    public FocusRoot focusRoot;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private FocusManager() {
    }

    private FocusManager(FocusContext focusContext2) {
        this.focusContext = focusContext2;
    }

    public static FocusManager create(FocusContext focusContext2) {
        return new FocusManager(focusContext2);
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void enable(boolean enable2) {
        this.enable = enable2;
        if (this.focusContext != null && this.focusContext.getAttachedWindow() != null) {
            this.focusRoot = (FocusRoot) this.focusContext.getAttachedWindow().getDecorView().findViewWithTag(FocusRoot.TAG_FLAG_FOR_ROOT);
        }
    }

    public void destroy() {
        this.focusContext = null;
        this.mainHandler = null;
        this.focusRoot = null;
        this.cachedTask = null;
    }

    public void onKeyEvent(final int keyCode, final KeyEvent event) {
        if (this.enable) {
            if (keyCode == 19) {
                runInWorkThread(new Runnable() {
                    public void run() {
                        FocusManager.this.move(keyCode, event);
                    }
                });
            } else if (keyCode == 20) {
                runInWorkThread(new Runnable() {
                    public void run() {
                        FocusManager.this.move(keyCode, event);
                    }
                });
            } else if (keyCode == 21) {
                runInWorkThread(new Runnable() {
                    public void run() {
                        FocusManager.this.move(keyCode, event);
                    }
                });
            } else if (keyCode == 22) {
                runInWorkThread(new Runnable() {
                    public void run() {
                        FocusManager.this.move(keyCode, event);
                    }
                });
            } else if (keyCode == 23 || keyCode == 66) {
                runInWorkThread(new Runnable() {
                    public void run() {
                        FocusManager.this.sure();
                    }
                });
            }
        }
    }

    public void focusOn(final FocusNode next) {
        runInWorkThread(new Runnable() {
            public void run() {
                if (FocusManager.this.focusRoot.getNode().getBuildCount() == 0) {
                    FocusUtils.rebuildTotalPath(FocusManager.this.focusRoot.getNode());
                }
                FocusNode candidateNode = next;
                if (!FocusUtils.isLeafFocusNode(candidateNode)) {
                    candidateNode = candidateNode.findFittestLeaf();
                }
                final FocusNode leaf = candidateNode;
                FocusManager.this.runInActivityThread(new Runnable() {
                    public void run() {
                        long time = System.currentTimeMillis();
                        FocusNode old = FocusManager.this.focusRoot.getCurrFocusOn();
                        FocusManager.this.focusRoot.setCurrFocusOn(leaf);
                        FocusUtils.syncFocusState(leaf, old, (FocusNode) null);
                        ZpLogger.d(FocusManager.TAG, " focusOn() cost time : " + (System.currentTimeMillis() - time));
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void move(int keyCode, KeyEvent event) {
        if (this.focusRoot.getCurrFocusOn() == null || !this.focusRoot.getCurrFocusOn().isNodeDoingSomething()) {
            long time = System.currentTimeMillis();
            this.focusRoot.getNode().rebuildChildren();
            FocusFinder.Routine thisRoutine = new FocusFinder.Routine(time);
            if (this.focusRoot.getCurrFocusOn() != null) {
                FocusNode next = FocusUtils.findNext(thisRoutine, this.focusRoot.getCurrFocusOn(), keyCode, event);
                if (next != null) {
                    focusOn(next);
                } else {
                    ZpLogger.d(TAG, " no nextInNowNode target ! ");
                }
            } else if (this.focusRoot != null) {
                FocusNode next2 = this.focusRoot.getNode().findNext(thisRoutine, keyCode, event);
                if (next2 != null) {
                    this.focusRoot.setCurrFocusOn(next2);
                    FocusUtils.syncFocusState(next2, (FocusNode) null, (FocusNode) null);
                } else {
                    ZpLogger.d(TAG, " no nextInNowNode target ! ");
                }
            } else {
                ZpLogger.d(TAG, " no focusRoot !!! ");
            }
            ZpLogger.d(TAG, " move() cost time : " + (System.currentTimeMillis() - time));
            return;
        }
        ZpLogger.d(TAG, " move() cancel cause innerState doing " + this.focusRoot.getCurrFocusOn().getInnerState());
    }

    /* access modifiers changed from: private */
    public void sure() {
        if (this.focusRoot == null || this.focusRoot.getCurrFocusOn() == null) {
            ZpLogger.d(TAG, " no currFocusOn for sure! ");
        } else {
            runInActivityThread(new Runnable() {
                public void run() {
                    FocusUtils.focusClickToLeaf(FocusManager.this.focusRoot.getNode());
                }
            });
        }
    }

    private void runInWorkThread(final Runnable runnable) {
        if (this.mainHandler != null) {
            this.mainHandler.post(new Runnable() {
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        synchronized (this.cachedTask) {
            this.cachedTask.add(runnable);
        }
    }

    /* access modifiers changed from: private */
    public void runInActivityThread(final Runnable runnable) {
        this.mainHandler.post(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
