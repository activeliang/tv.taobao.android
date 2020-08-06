package com.tvtaobao.android.marketgames.dfw.act;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tvtaobao.android.focus3.FocusAssist;
import com.tvtaobao.android.focus3.FocusSearchRuleA;
import com.tvtaobao.android.marketgames.R;
import com.tvtaobao.android.marketgames.dfw.DfwConfig;
import com.tvtaobao.android.marketgames.dfw.DfwHandler;
import com.tvtaobao.android.marketgames.dfw.DfwLauncher;
import com.tvtaobao.android.marketgames.dfw.core.GameSceneChoreographer;
import com.tvtaobao.android.marketgames.dfw.core.ObjDice;
import com.tvtaobao.android.marketgames.dfw.core.ObjPlayer;
import com.tvtaobao.android.marketgames.dfw.view.LoadingView;
import com.tvtaobao.android.marketgames.dfw.view.NewGamerGuideView;
import com.tvtaobao.android.marketgames.dfw.wares.IBoGameData;
import com.tvtaobao.android.marketgames.dfw.wares.IDataRequest;
import com.tvtaobao.android.marketgames.dfw.wares.IImageLoader;
import com.tvtaobao.android.ui3.helper.EasySurfaceView;
import com.tvtaobao.android.ui3.helper.RenderThread;
import com.tvtaobao.android.ui3.widget.CustomDialog;
import com.tvtaobao.android.ui3.widget.UI3Toast;
import com.tvtaobao.android.values.Flag;
import com.tvtaobao.android.values.ValuesHelper;
import java.util.concurrent.atomic.AtomicInteger;

public class GameDaFuWengActivity extends Activity {
    static final int FLAG_GAME_LOAD_DOING = 1;
    public static final String TAG = GameDaFuWengActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public ImageView btnAwardList;
    /* access modifiers changed from: private */
    public ImageView btnRollDice;
    /* access modifiers changed from: private */
    public TextView btnRollDiceEffect;
    /* access modifiers changed from: private */
    public TextView btnRollDiceTip;
    /* access modifiers changed from: private */
    public ImageView btnRule;
    /* access modifiers changed from: private */
    public ImageView btnTaskList;
    private ConstraintLayout constraintLayout;
    private DfwHandler dfwHandler;
    /* access modifiers changed from: private */
    public DfwLauncher dfwLauncher;
    private EasySurfaceView gameScene;
    /* access modifiers changed from: private */
    public GameSceneChoreographer gameSceneChoreographer;
    private View gameSceneCover;
    /* access modifiers changed from: private */
    public Flag gameflag = new Flag();
    /* access modifiers changed from: private */
    public IBoGameData iBoGameData;
    /* access modifiers changed from: private */
    public RenderThread renderThread = new RenderThread();
    /* access modifiers changed from: private */
    public StateManager stateManager;
    /* access modifiers changed from: private */
    public ValuesHelper valuesHelper = new ValuesHelper();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate bgn");
        super.onCreate(savedInstanceState);
        this.renderThread.turnOnLog(DfwConfig.showRenderThreadLog);
        this.renderThread.setFrameInterval(30);
        this.renderThread.pause(true);
        this.renderThread.startDrawThread();
        onLoadStart();
        setContentView(R.layout.marketgames_activity_game_dafuweng);
        findViews();
        this.stateManager = new StateManager();
        init();
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityCreated(this, savedInstanceState);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.dfwHandler = new DfwHandler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (!(GameDaFuWengActivity.this.stateManager == null || GameDaFuWengActivity.this.stateManager.btnRollDiceSM == null)) {
                        GameDaFuWengActivity.this.stateManager.btnRollDiceSM.applyBtnRollDiceTipState();
                    }
                    if (GameDaFuWengActivity.this.gameSceneChoreographer != null) {
                        GameDaFuWengActivity.this.gameSceneChoreographer.onGameDataUpdate();
                    }
                }
                return true;
            }
        });
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onDfwHandlerPrepared(this.dfwHandler);
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        FocusAssist.obtain(getWindow()).registerFocusSearchRule(new FocusSearchRuleA() {
        });
        Log.i(TAG, "onCreate end");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.i(TAG, "onResume bgn");
        this.renderThread.pause(false);
        super.onResume();
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityResumed(this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onResume end");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(TAG, "onPause bgn");
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityPaused(this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onPause();
        this.renderThread.pause(true);
        Log.i(TAG, "onPause end");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        Log.i(TAG, "onStart bgn");
        super.onStart();
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityStarted(this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onStart end");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.i(TAG, "onStop bgn");
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityStopped(this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onStop();
        Log.i(TAG, "onStop end");
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState bgn");
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivitySaveInstanceState(this, outState);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState end");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i(TAG, "onDestroy bgn");
        try {
            FocusAssist.release(getWindow());
            this.renderThread.killDrawThread();
            this.renderThread = null;
            this.gameSceneChoreographer.onDestory();
            this.gameSceneChoreographer = null;
            this.dfwHandler = null;
            this.iBoGameData = null;
            this.stateManager = null;
            this.dfwLauncher = null;
            this.valuesHelper.clr();
            this.valuesHelper = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            if (this.dfwLauncher.eventListener != null) {
                this.dfwLauncher.eventListener.onActivityDestroyed(this);
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy end");
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent bgn");
        if (this.gameSceneChoreographer != null && this.gameSceneChoreographer.isRollOrJumpDoing()) {
            return true;
        }
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        Log.i(TAG, "dispatchKeyEvent end");
        return dispatchKeyEvent;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event == null || event.getKeyCode() != 4 || this.valuesHelper.has("reqGameSceneCfgError") || this.dfwLauncher.eventListener == null) {
            return super.onKeyUp(keyCode, event);
        }
        boolean handle = false;
        try {
            handle = this.dfwLauncher.eventListener.onExitGame(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (!handle) {
            return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.gameScene = (EasySurfaceView) findViewById(R.id.game_scene);
        this.btnTaskList = (ImageView) findViewById(R.id.btn_task_list);
        this.btnRollDice = (ImageView) findViewById(R.id.btn_roll_dice);
        this.btnAwardList = (ImageView) findViewById(R.id.btn_award_list);
        this.btnRule = (ImageView) findViewById(R.id.btn_rule);
        this.btnRollDiceTip = (TextView) findViewById(R.id.btn_roll_dice_tip);
        this.btnRollDiceEffect = (TextView) findViewById(R.id.btn_roll_dice_effect);
        this.gameSceneCover = findViewById(R.id.game_scene_cover);
    }

    private void init() {
        this.dfwLauncher = DfwLauncher.getFromIntent(getIntent());
        if (this.dfwLauncher != null && this.dfwLauncher.isCfgOk()) {
            final LoadingView loadView = new LoadingView(this, (IBoGameData.IBoLoadData) null, this.dfwLauncher.imageLoader);
            loadView.setPercent(20.0f);
            loadView.show(new Runnable() {
                public void run() {
                    try {
                        if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                            GameDaFuWengActivity.this.dfwLauncher.eventListener.onLoadingShow(GameDaFuWengActivity.this, loadView);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            loadView.getBg().setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                            GameDaFuWengActivity.this.dfwLauncher.eventListener.onLoadingClick(GameDaFuWengActivity.this, loadView.getBg());
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            loadView.getBg().setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event != null && keyCode == 4 && event.getAction() == 1) {
                        try {
                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                return GameDaFuWengActivity.this.dfwLauncher.eventListener.onLoadingExit(GameDaFuWengActivity.this, loadView.getBg());
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            getWindow().getDecorView().postDelayed(new Runnable() {
                long bgnLoadTime = System.currentTimeMillis();
                CustomDialog timeOutDlg;

                public void run() {
                    try {
                        Log.i(GameDaFuWengActivity.TAG, "progress_check");
                        if (System.currentTimeMillis() - this.bgnLoadTime > DfwConfig.resourceLoad_TimeOut) {
                            this.bgnLoadTime = System.currentTimeMillis();
                            if (this.timeOutDlg == null) {
                                CustomDialog.Builder builder = new CustomDialog.Builder(GameDaFuWengActivity.this);
                                builder.setCancelable(true);
                                builder.setType(CustomDialog.Type.double_btn);
                                builder.setPositiveButton("继续等待", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        GameDaFuWengActivity.this.finish();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.setMessage("资源装载超时");
                                this.timeOutDlg = builder.create();
                            }
                            if (this.timeOutDlg != null && !this.timeOutDlg.isShowing()) {
                                this.timeOutDlg.show();
                            }
                        }
                        if (GameDaFuWengActivity.this.gameSceneChoreographer == null) {
                            GameDaFuWengActivity.this.getWindow().getDecorView().postDelayed(this, 100);
                        } else if (GameDaFuWengActivity.this.gameSceneChoreographer.getGrm().isAllResLoadFinished()) {
                            Log.i(GameDaFuWengActivity.TAG, "progress_check finish");
                            GameDaFuWengActivity.this.onLoadFinish();
                            loadView.setPercent(100.0f);
                            GameDaFuWengActivity.this.gameSceneChoreographer.flag.clrFlag(1);
                            GameDaFuWengActivity.this.getWindow().getDecorView().postDelayed(new Runnable() {
                                public void showNewGamerGuide() {
                                    final NewGamerGuideView newGamerGuideView = new NewGamerGuideView(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getGuideData(), GameDaFuWengActivity.this.dfwLauncher.imageLoader);
                                    newGamerGuideView.setAlpha(0.0f);
                                    newGamerGuideView.getBg().setOnClickListener(new View.OnClickListener() {
                                        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
                                        /* Code decompiled incorrectly, please refer to instructions dump. */
                                        public void onClick(android.view.View r5) {
                                            /*
                                                r4 = this;
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6$3 r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.AnonymousClass3.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6 r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.DfwLauncher r1 = r1.dfwLauncher     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.wares.IEventListener r1 = r1.eventListener     // Catch:{ Throwable -> 0x0034 }
                                                if (r1 == 0) goto L_0x0029
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6$3 r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.AnonymousClass3.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6 r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity r1 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.DfwLauncher r1 = r1.dfwLauncher     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.wares.IEventListener r1 = r1.eventListener     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6$3 r2 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.AnonymousClass3.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6 r2 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity r2 = com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.this     // Catch:{ Throwable -> 0x0034 }
                                                com.tvtaobao.android.marketgames.dfw.view.NewGamerGuideView r3 = r1     // Catch:{ Throwable -> 0x0034 }
                                                android.widget.ImageView r3 = r3.getBg()     // Catch:{ Throwable -> 0x0034 }
                                                r1.onNewGamerGuideClick(r2, r3)     // Catch:{ Throwable -> 0x0034 }
                                            L_0x0029:
                                                com.tvtaobao.android.marketgames.dfw.view.NewGamerGuideView r1 = r1     // Catch:{ Throwable -> 0x0039 }
                                                com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6$3$1$1 r2 = new com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity$6$3$1$1     // Catch:{ Throwable -> 0x0039 }
                                                r2.<init>()     // Catch:{ Throwable -> 0x0039 }
                                                r1.dismiss(r2)     // Catch:{ Throwable -> 0x0039 }
                                            L_0x0033:
                                                return
                                            L_0x0034:
                                                r0 = move-exception
                                                r0.printStackTrace()     // Catch:{ Throwable -> 0x0039 }
                                                goto L_0x0029
                                            L_0x0039:
                                                r0 = move-exception
                                                r0.printStackTrace()
                                                goto L_0x0033
                                            */
                                            throw new UnsupportedOperationException("Method not decompiled: com.tvtaobao.android.marketgames.dfw.act.GameDaFuWengActivity.AnonymousClass6.AnonymousClass3.AnonymousClass1.onClick(android.view.View):void");
                                        }
                                    });
                                    newGamerGuideView.getBg().setOnKeyListener(new View.OnKeyListener() {
                                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                                            if (event == null || keyCode != 4 || event.getAction() != 1) {
                                                return false;
                                            }
                                            newGamerGuideView.dismiss(new Runnable() {
                                                public void run() {
                                                    GameDaFuWengActivity.this.valuesHelper.set("gameSceneIsVisible", "yes");
                                                    GameDaFuWengActivity.this.btnRollDice.requestFocus();
                                                    try {
                                                        if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                                            GameDaFuWengActivity.this.dfwLauncher.eventListener.onNewGamerGuideDismiss(GameDaFuWengActivity.this, newGamerGuideView);
                                                        }
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            return true;
                                        }
                                    });
                                    newGamerGuideView.show(new Runnable() {
                                        public void run() {
                                            try {
                                                if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                                    GameDaFuWengActivity.this.dfwLauncher.eventListener.onNewGamerGuideShow(GameDaFuWengActivity.this, newGamerGuideView);
                                                }
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            try {
                                                newGamerGuideView.getContentView().setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    animator.setDuration(500);
                                    animator.start();
                                }

                                public void run() {
                                    ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            try {
                                                loadView.getContentView().setAlpha(1.0f - ((Float) animation.getAnimatedValue()).floatValue());
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    animator.addListener(new Animator.AnimatorListener() {
                                        public void onAnimationStart(Animator animation) {
                                        }

                                        public void onAnimationEnd(Animator animation) {
                                            try {
                                                loadView.dismiss(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                                                GameDaFuWengActivity.this.dfwLauncher.eventListener.onLoadingDismiss(GameDaFuWengActivity.this, loadView);
                                                            }
                                                        } catch (Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                if (GameDaFuWengActivity.this.iBoGameData.isNewGamer()) {
                                                    AnonymousClass3.this.showNewGamerGuide();
                                                    return;
                                                }
                                                GameDaFuWengActivity.this.valuesHelper.set("gameSceneIsVisible", "yes");
                                                GameDaFuWengActivity.this.btnRollDice.requestFocus();
                                            } catch (Throwable e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        public void onAnimationCancel(Animator animation) {
                                        }

                                        public void onAnimationRepeat(Animator animation) {
                                        }
                                    });
                                    animator.setDuration(500);
                                    animator.start();
                                }
                            }, 300);
                        } else {
                            loadView.setPercent(20.0f + ((float) (((double) (GameDaFuWengActivity.this.gameSceneChoreographer.getGrm().getPercent() * 100.0f)) * 0.8d)));
                            GameDaFuWengActivity.this.getWindow().getDecorView().postDelayed(this, 100);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, 100);
            this.dfwLauncher.dataRequest.reqGameSceneCfg(new IDataRequest.IDRCallBack<IBoGameData>() {
                public void onSuccess(IBoGameData data) {
                    IBoGameData unused = GameDaFuWengActivity.this.iBoGameData = data;
                    if (GameDaFuWengActivity.this.iBoGameData != null) {
                        loadView.setBgImgLoadListener(new LoadingView.BgImgLoadListener() {
                            public void onBgLoadSuccess() {
                                GameDaFuWengActivity.this.doInitGameScene();
                            }

                            public void onBgLoadFailed() {
                                GameDaFuWengActivity.this.doInitGameScene();
                            }
                        });
                        loadView.setDataProvider(GameDaFuWengActivity.this.iBoGameData.getLoadData());
                    }
                }

                public void onFailed(String msg) {
                    GameDaFuWengActivity.this.valuesHelper.set("reqGameSceneCfgError", "yes");
                    UI3Toast toast = UI3Toast.makeToast(GameDaFuWengActivity.this, "" + msg);
                    toast.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            try {
                                GameDaFuWengActivity.this.finish();
                            } catch (Throwable th) {
                            }
                        }
                    });
                    toast.show();
                }
            });
        }
    }

    public DfwLauncher getDfwLauncher() {
        return this.dfwLauncher;
    }

    private void onLoadStart() {
        this.gameflag.setFlag(1);
    }

    /* access modifiers changed from: private */
    public void onLoadFinish() {
        this.gameflag.clrFlag(1);
        this.gameSceneCover.setVisibility(8);
        if (this.stateManager != null && this.stateManager.btnRollDiceSM != null) {
            this.stateManager.btnRollDiceSM.applyBtnRollDiceTipState();
        }
    }

    /* access modifiers changed from: private */
    public void doInitGameScene() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            getWindow().getDecorView().post(new Runnable() {
                public void run() {
                    GameDaFuWengActivity.this.doInitGameScene();
                }
            });
        } else if (this.iBoGameData != null) {
            try {
                int color = Color.parseColor(this.iBoGameData.getBgColor());
                this.constraintLayout.setBackgroundColor(color);
                this.gameSceneCover.setBackgroundColor(color);
            } catch (Throwable th) {
            }
            this.gameSceneChoreographer = new GameSceneChoreographer(this, this.gameScene, this.dfwLauncher.imageLoader, this.iBoGameData, this.dfwLauncher.dataRequest, this.dfwLauncher.eventListener);
            this.gameSceneChoreographer.flag.setFlag(1);
            this.gameScene.setRenderClient(this.gameScene.makeDefaultRenderClient(this.gameSceneChoreographer)).setRenderThread(this.renderThread);
            this.stateManager.btnRollDiceSM.load();
            this.stateManager.btnAwardListSM.load();
            this.stateManager.btnAwardListSM.apply();
            this.stateManager.btnTaskListSM.load();
            this.stateManager.btnTaskListSM.apply();
            this.stateManager.btnGameRuleSM.load();
            this.stateManager.btnGameRuleSM.apply();
            this.stateManager.refreshRemainTimes();
        }
    }

    class StateManager {
        BtnAwardListSM btnAwardListSM = new BtnAwardListSM();
        BtnGameRuleSM btnGameRuleSM = new BtnGameRuleSM();
        BtnRollDiceSM btnRollDiceSM = new BtnRollDiceSM();
        BtnTaskListSM btnTaskListSM = new BtnTaskListSM();

        StateManager() {
        }

        /* access modifiers changed from: package-private */
        public void refreshRemainTimes() {
            this.btnRollDiceSM.apply();
        }

        /* access modifiers changed from: package-private */
        public void syncGlobalFocusState() {
            this.btnRollDiceSM.syncFocusState();
            this.btnAwardListSM.syncFocusState();
            this.btnTaskListSM.syncFocusState();
            this.btnGameRuleSM.syncFocusState();
        }

        class BtnRollDiceSM {
            Runnable check = new Runnable() {
                public void run() {
                    if (GameDaFuWengActivity.this.btnRollDice.hasFocus()) {
                        BtnRollDiceSM.this.playEffect();
                    }
                }
            };
            Drawable effect;
            boolean effectPlaying = false;
            Runnable effectTask = new Runnable() {
                public void run() {
                    try {
                        if (GameDaFuWengActivity.this.valuesHelper != null) {
                            if (GameDaFuWengActivity.this.btnRollDiceEffect.getMeasuredWidth() == 0 || GameDaFuWengActivity.this.btnRollDiceEffect.getMeasuredHeight() == 0) {
                                GameDaFuWengActivity.this.btnRollDice.postDelayed(this, 50);
                                if (DfwConfig.showDebugInfo) {
                                    Toast.makeText(GameDaFuWengActivity.this, "btnRollDiceEffect size = 0", 0).show();
                                }
                            } else if (!GameDaFuWengActivity.this.valuesHelper.has("gameSceneIsVisible")) {
                                GameDaFuWengActivity.this.btnRollDice.postDelayed(this, 50);
                            } else {
                                final int btnRollDiceEffectWidth = GameDaFuWengActivity.this.btnRollDiceEffect.getMeasuredWidth();
                                int btnRollDiceEffectHeight = GameDaFuWengActivity.this.btnRollDiceEffect.getMeasuredHeight();
                                final int btnRollDiceWidth = GameDaFuWengActivity.this.btnRollDice.getMeasuredWidth();
                                GameDaFuWengActivity.this.btnRollDiceEffect.setPivotX((float) (btnRollDiceEffectWidth / 2));
                                GameDaFuWengActivity.this.btnRollDiceEffect.setPivotY((float) (btnRollDiceEffectHeight / 2));
                                Log.i(GameDaFuWengActivity.TAG, " btnRollDiceEffectWidth:" + btnRollDiceEffectWidth + " btnRollDiceEffectHeight:" + btnRollDiceEffectHeight + " btnRollDiceWidth:" + btnRollDiceWidth);
                                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                                animator.setDuration(DfwConfig.rollDiceBtnEffectDuration);
                                final ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                                    int bgn = -1;
                                    int end = -1;
                                    int nowX = -1;
                                    float step = 0.0f;
                                    StringBuffer stringBuffer = new StringBuffer();
                                    int tranX = -1;

                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        try {
                                            if (this.bgn == -1) {
                                                this.bgn = (btnRollDiceEffectWidth / 2) - 20;
                                                this.end = (btnRollDiceWidth - (btnRollDiceEffectWidth / 2)) + 24;
                                            }
                                            float val = ((Float) animation.getAnimatedValue()).floatValue();
                                            if (val < 0.0f) {
                                                val = 0.0f;
                                            }
                                            if (val > 1.0f) {
                                                val = 1.0f;
                                            }
                                            float scale = 1.0f;
                                            if (val < 0.1f) {
                                                scale = val / 0.1f;
                                            }
                                            if (val > 0.9f) {
                                                scale = (0.1f - (val - 0.9f)) / 0.1f;
                                            }
                                            this.nowX = (int) (((float) this.bgn) + (((float) (this.end - this.bgn)) * val));
                                            this.tranX = (int) (((float) this.nowX) - (((float) btnRollDiceEffectWidth) / 2.0f));
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setTranslationX((float) this.tranX);
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setScaleX(scale);
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setScaleY(scale);
                                            if (DfwConfig.showDebugInfo) {
                                                if (val == 0.0f) {
                                                    this.step = 0.0f;
                                                    this.stringBuffer.delete(0, this.stringBuffer.length() - 1);
                                                }
                                                if (val > this.step) {
                                                    this.step += 0.3f;
                                                    this.stringBuffer.append(" tranX:" + this.tranX + " scale:" + scale + " \n");
                                                    if (this.step >= 1.0f) {
                                                        Toast.makeText(GameDaFuWengActivity.this, "onAnimationEnd\n" + this.stringBuffer.toString(), 0).show();
                                                    }
                                                }
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                        if (!GameDaFuWengActivity.this.btnRollDice.hasFocus()) {
                                            try {
                                                BtnRollDiceSM.this.effectPlaying = false;
                                                animation.cancel();
                                            } catch (Throwable e2) {
                                                e2.printStackTrace();
                                            }
                                        }
                                    }
                                };
                                animator.addUpdateListener(animatorUpdateListener);
                                final Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                                    public void onAnimationStart(Animator animation) {
                                        try {
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setVisibility(0);
                                            if (DfwConfig.showDebugInfo) {
                                                Toast.makeText(GameDaFuWengActivity.this, "onAnimationStart01", 0).show();
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    public void onAnimationEnd(Animator animation) {
                                        try {
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setVisibility(4);
                                            BtnRollDiceSM.this.effectPlaying = false;
                                            GameDaFuWengActivity.this.btnRollDice.removeCallbacks(BtnRollDiceSM.this.check);
                                            GameDaFuWengActivity.this.btnRollDice.postDelayed(BtnRollDiceSM.this.check, 3000);
                                            if (DfwConfig.showDebugInfo) {
                                                Toast.makeText(GameDaFuWengActivity.this, "onAnimationEnd01", 0).show();
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    public void onAnimationCancel(Animator animation) {
                                        try {
                                            GameDaFuWengActivity.this.btnRollDiceEffect.setVisibility(4);
                                            BtnRollDiceSM.this.effectPlaying = false;
                                            GameDaFuWengActivity.this.btnRollDice.removeCallbacks(BtnRollDiceSM.this.check);
                                            if (DfwConfig.showDebugInfo) {
                                                Toast.makeText(GameDaFuWengActivity.this, "onAnimationCancel01", 0).show();
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    public void onAnimationRepeat(Animator animation) {
                                    }
                                };
                                animator.addListener(animatorListener);
                                GameDaFuWengActivity.this.renderThread.postTask(new Runnable() {
                                    ValueAnimator animation = new ValueAnimator() {
                                        public Object getAnimatedValue() {
                                            AnonymousClass3.this.val = (((float) (System.currentTimeMillis() - AnonymousClass3.this.startTime)) * 1.0f) / ((float) DfwConfig.rollDiceBtnEffectDuration);
                                            if (AnonymousClass3.this.val < 0.0f) {
                                                AnonymousClass3.this.val = 0.0f;
                                            }
                                            if (AnonymousClass3.this.val > 1.0f) {
                                                AnonymousClass3.this.val = 1.0f;
                                            }
                                            return Float.valueOf(AnonymousClass3.this.val);
                                        }

                                        public void cancel() {
                                            animatorListener.onAnimationCancel(this);
                                        }
                                    };
                                    Runnable doRealAnimTask = new Runnable() {
                                        public void run() {
                                            if (AnonymousClass3.this.startTime == -1) {
                                                AnonymousClass3.this.startTime = System.currentTimeMillis();
                                                animatorListener.onAnimationStart((Animator) null);
                                            }
                                            animatorUpdateListener.onAnimationUpdate(AnonymousClass3.this.animation);
                                            if (AnonymousClass3.this.val == 1.0f) {
                                                animatorListener.onAnimationEnd(AnonymousClass3.this.animation);
                                            }
                                        }
                                    };
                                    long startTime = -1;
                                    float val = 0.0f;

                                    public void run() {
                                        if (!BtnRollDiceSM.this.effectPlaying) {
                                            this.startTime = -1;
                                            return;
                                        }
                                        GameDaFuWengActivity.this.btnRollDice.post(this.doRealAnimTask);
                                        GameDaFuWengActivity.this.renderThread.postTask(this);
                                    }
                                });
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            };
            Drawable normalFocus;
            Drawable normalPress;
            Drawable normalUnFocus;
            Drawable remainTimeTipBubbleBg;

            BtnRollDiceSM() {
            }

            /* access modifiers changed from: package-private */
            public void load() {
                final Runnable stateChecktask = new Runnable() {
                    public void run() {
                        if (GameDaFuWengActivity.this.btnRollDice.hasFocus()) {
                            if (GameDaFuWengActivity.this.btnRollDice.getDrawable() == null) {
                                GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalFocus);
                            }
                        } else if (GameDaFuWengActivity.this.btnRollDice.getDrawable() == null) {
                            GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalUnFocus);
                        }
                    }
                };
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnRollDiceImgUrl_normal_focus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnRollDiceSM.this.normalFocus = new BitmapDrawable(bitmap);
                        stateChecktask.run();
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnRollDiceImgUrl_normal_unfocus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnRollDiceSM.this.normalUnFocus = new BitmapDrawable(bitmap);
                        stateChecktask.run();
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnRollDiceImgUrl_normal_press(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnRollDiceSM.this.normalPress = new BitmapDrawable(bitmap);
                        stateChecktask.run();
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getRemainTimeTipBubbleImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnRollDiceSM.this.remainTimeTipBubbleBg = new BitmapDrawable(bitmap);
                        GameDaFuWengActivity.this.btnRollDiceTip.setBackgroundDrawable(BtnRollDiceSM.this.remainTimeTipBubbleBg);
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnRollDiceEffectImgUrl(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnRollDiceSM.this.effect = new BitmapDrawable(bitmap);
                        GameDaFuWengActivity.this.btnRollDiceEffect.setBackgroundDrawable(BtnRollDiceSM.this.effect);
                    }

                    public void onFailed() {
                    }
                });
            }

            /* access modifiers changed from: package-private */
            public void applyBtnRollDiceTipState() {
                GameDaFuWengActivity.this.btnRollDiceTip.setBackgroundDrawable(this.remainTimeTipBubbleBg);
                int remainTimes = 0;
                if (GameDaFuWengActivity.this.iBoGameData != null) {
                    remainTimes = GameDaFuWengActivity.this.iBoGameData.getRemainTime();
                }
                if (!GameDaFuWengActivity.this.gameflag.hasFlag(1)) {
                    GameDaFuWengActivity.this.btnRollDiceTip.setVisibility(0);
                }
                GameDaFuWengActivity.this.btnRollDiceTip.setText("" + remainTimes + "次机会");
            }

            /* access modifiers changed from: package-private */
            public void apply() {
                applyBtnRollDiceTipState();
                GameDaFuWengActivity.this.btnRollDice.setImageDrawable(this.normalFocus);
                GameDaFuWengActivity.this.btnRollDice.setOnKeyListener(new View.OnKeyListener() {
                    AtomicInteger balance = new AtomicInteger(0);
                    Runnable checkState = new Runnable() {
                        public void run() {
                            if (GameDaFuWengActivity.this.btnRollDice.isPressed()) {
                                GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalPress);
                                GameDaFuWengActivity.this.gameSceneChoreographer.onBtnRollDicePress(true);
                                return;
                            }
                            GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalFocus);
                            GameDaFuWengActivity.this.gameSceneChoreographer.onBtnRollDicePress(false);
                        }
                    };

                    /* access modifiers changed from: package-private */
                    public boolean isConfirmKey(int keyCode) {
                        switch (keyCode) {
                            case 23:
                            case 62:
                            case 66:
                            case 160:
                                return true;
                            default:
                                return false;
                        }
                    }

                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (isConfirmKey(keyCode) && !GameDaFuWengActivity.this.gameSceneChoreographer.isRollOrJumpDoing()) {
                            if (event.getAction() == 0) {
                                if (this.balance.compareAndSet(0, 1)) {
                                    GameDaFuWengActivity.this.btnRollDice.post(this.checkState);
                                }
                            } else if (event.getAction() == 1 && this.balance.compareAndSet(1, 0)) {
                                GameDaFuWengActivity.this.btnRollDice.post(this.checkState);
                            }
                        }
                        return false;
                    }
                });
                GameDaFuWengActivity.this.btnRollDice.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        boolean handled = false;
                        try {
                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                handled = GameDaFuWengActivity.this.dfwLauncher.eventListener.onBtnRollDiceClick(GameDaFuWengActivity.this, GameDaFuWengActivity.this.btnAwardList);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        if (!handled && !GameDaFuWengActivity.this.gameSceneChoreographer.isRollOrJumpDoing()) {
                            GameDaFuWengActivity.this.gameSceneChoreographer.playDice(new ObjDice.DiceRltListener() {
                                public void onDiceRlt(int rlt) {
                                    int beforeRollPos = GameDaFuWengActivity.this.iBoGameData.getPlayerPosBeforeRoll();
                                    Log.i(GameDaFuWengActivity.TAG, "onDiceRlt " + rlt);
                                    ObjPlayer.JumpDirection direction = null;
                                    if (rlt > 0) {
                                        direction = ObjPlayer.JumpDirection.loop_0_to_n;
                                    } else if (rlt < 0) {
                                        direction = ObjPlayer.JumpDirection.loop_n_to_0;
                                    }
                                    if (direction != null) {
                                        int from = beforeRollPos;
                                        GameDaFuWengActivity.this.gameSceneChoreographer.jump(from, from + rlt, direction);
                                    }
                                    GameDaFuWengActivity.this.stateManager.refreshRemainTimes();
                                }
                            });
                        }
                    }
                });
                GameDaFuWengActivity.this.btnRollDice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalFocus);
                            StateManager.this.syncGlobalFocusState();
                            BtnRollDiceSM.this.playEffect();
                            return;
                        }
                        GameDaFuWengActivity.this.btnRollDice.setImageDrawable(BtnRollDiceSM.this.normalUnFocus);
                    }
                });
            }

            /* access modifiers changed from: package-private */
            public void playEffect() {
                if (!this.effectPlaying) {
                    this.effectPlaying = true;
                    GameDaFuWengActivity.this.btnRollDice.removeCallbacks(this.effectTask);
                    GameDaFuWengActivity.this.btnRollDice.post(this.effectTask);
                }
            }

            public void syncFocusState() {
                if (!GameDaFuWengActivity.this.btnRollDice.hasFocus()) {
                    GameDaFuWengActivity.this.btnRollDice.setImageDrawable(this.normalUnFocus);
                } else {
                    GameDaFuWengActivity.this.btnRollDice.setImageDrawable(this.normalFocus);
                }
            }
        }

        class BtnAwardListSM {
            Drawable focus;
            Drawable unFocus;

            BtnAwardListSM() {
            }

            /* access modifiers changed from: package-private */
            public void load() {
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnAwardListImgUrl_unfocus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnAwardListSM.this.unFocus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnAwardListImgUrl_focus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnAwardListSM.this.focus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
            }

            /* access modifiers changed from: package-private */
            public void apply() {
                GameDaFuWengActivity.this.btnAwardList.setImageDrawable(this.unFocus);
                GameDaFuWengActivity.this.btnAwardList.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                GameDaFuWengActivity.this.dfwLauncher.eventListener.onBtnAwardListClick(GameDaFuWengActivity.this, GameDaFuWengActivity.this.btnAwardList);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                GameDaFuWengActivity.this.btnAwardList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            GameDaFuWengActivity.this.btnAwardList.setImageDrawable(BtnAwardListSM.this.focus);
                            StateManager.this.syncGlobalFocusState();
                            return;
                        }
                        GameDaFuWengActivity.this.btnAwardList.setImageDrawable(BtnAwardListSM.this.unFocus);
                    }
                });
                if (TextUtils.isEmpty(GameDaFuWengActivity.this.iBoGameData.getBtnAwardListImgUrl_focus())) {
                    GameDaFuWengActivity.this.btnAwardList.setVisibility(4);
                }
            }

            public void syncFocusState() {
                if (!GameDaFuWengActivity.this.btnAwardList.hasFocus()) {
                    GameDaFuWengActivity.this.btnAwardList.setImageDrawable(this.unFocus);
                } else {
                    GameDaFuWengActivity.this.btnAwardList.setImageDrawable(this.focus);
                }
            }
        }

        class BtnTaskListSM {
            Drawable focus;
            Drawable unFocus;

            BtnTaskListSM() {
            }

            /* access modifiers changed from: package-private */
            public void load() {
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnTaskListImgUrl_unfocus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnTaskListSM.this.unFocus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnTaskListImgUrl_focus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnTaskListSM.this.focus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
            }

            /* access modifiers changed from: package-private */
            public void apply() {
                GameDaFuWengActivity.this.btnTaskList.setImageDrawable(this.unFocus);
                GameDaFuWengActivity.this.btnTaskList.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                GameDaFuWengActivity.this.dfwLauncher.eventListener.onBtnTaskListClick(GameDaFuWengActivity.this, GameDaFuWengActivity.this.btnTaskList);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                GameDaFuWengActivity.this.btnTaskList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            GameDaFuWengActivity.this.btnTaskList.setImageDrawable(BtnTaskListSM.this.focus);
                            StateManager.this.syncGlobalFocusState();
                            return;
                        }
                        GameDaFuWengActivity.this.btnTaskList.setImageDrawable(BtnTaskListSM.this.unFocus);
                    }
                });
                if (TextUtils.isEmpty(GameDaFuWengActivity.this.iBoGameData.getBtnTaskListImgUrl_focus())) {
                    GameDaFuWengActivity.this.btnTaskList.setVisibility(4);
                }
            }

            public void syncFocusState() {
                if (!GameDaFuWengActivity.this.btnTaskList.hasFocus()) {
                    GameDaFuWengActivity.this.btnTaskList.setImageDrawable(this.unFocus);
                } else {
                    GameDaFuWengActivity.this.btnTaskList.setImageDrawable(this.focus);
                }
            }
        }

        class BtnGameRuleSM {
            Drawable focus;
            Drawable unFocus;

            BtnGameRuleSM() {
            }

            /* access modifiers changed from: package-private */
            public void load() {
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnGameRuleImgUrl_unfocus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnGameRuleSM.this.unFocus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
                GameDaFuWengActivity.this.dfwLauncher.imageLoader.load(GameDaFuWengActivity.this, GameDaFuWengActivity.this.iBoGameData.getBtnGameRuleImgUrl_focus(), new IImageLoader.ImgLoadListener() {
                    public void onSuccess(Bitmap bitmap) {
                        BtnGameRuleSM.this.focus = new BitmapDrawable(bitmap);
                    }

                    public void onFailed() {
                    }
                });
            }

            /* access modifiers changed from: package-private */
            public void apply() {
                GameDaFuWengActivity.this.btnRule.setImageDrawable(this.unFocus);
                GameDaFuWengActivity.this.btnRule.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            if (GameDaFuWengActivity.this.dfwLauncher.eventListener != null) {
                                GameDaFuWengActivity.this.dfwLauncher.eventListener.onBtnGameRuleClick(GameDaFuWengActivity.this, GameDaFuWengActivity.this.btnRule);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                GameDaFuWengActivity.this.btnRule.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            GameDaFuWengActivity.this.btnRule.setImageDrawable(BtnGameRuleSM.this.focus);
                            StateManager.this.syncGlobalFocusState();
                            return;
                        }
                        GameDaFuWengActivity.this.btnRule.setImageDrawable(BtnGameRuleSM.this.unFocus);
                    }
                });
                if (TextUtils.isEmpty(GameDaFuWengActivity.this.iBoGameData.getBtnGameRuleImgUrl_focus())) {
                    GameDaFuWengActivity.this.btnRule.setVisibility(4);
                }
            }

            public void syncFocusState() {
                if (!GameDaFuWengActivity.this.btnRule.hasFocus()) {
                    GameDaFuWengActivity.this.btnRule.setImageDrawable(this.unFocus);
                } else {
                    GameDaFuWengActivity.this.btnRule.setImageDrawable(this.focus);
                }
            }
        }
    }
}
