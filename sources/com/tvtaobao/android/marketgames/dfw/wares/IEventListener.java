package com.tvtaobao.android.marketgames.dfw.wares;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import com.tvtaobao.android.marketgames.dfw.DfwHandler;

public interface IEventListener extends Application.ActivityLifecycleCallbacks {
    boolean onBtnAwardListClick(Activity activity, View view);

    boolean onBtnGameRuleClick(Activity activity, View view);

    boolean onBtnRollDiceClick(Activity activity, View view);

    boolean onBtnTaskListClick(Activity activity, View view);

    void onDfwHandlerPrepared(DfwHandler dfwHandler);

    boolean onExitGame(Activity activity);

    void onJumpDone(Activity activity, int i);

    boolean onLoadingClick(Activity activity, View view);

    void onLoadingDismiss(Activity activity, View view);

    boolean onLoadingExit(Activity activity, View view);

    void onLoadingShow(Activity activity, View view);

    boolean onNewGamerGuideClick(Activity activity, View view);

    void onNewGamerGuideDismiss(Activity activity, View view);

    boolean onNewGamerGuideExit(Activity activity, View view);

    void onNewGamerGuideShow(Activity activity, View view);
}
