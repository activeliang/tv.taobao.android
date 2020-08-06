package com.tvtaobao.android.focus3;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.tvtaobao.android.focus3.FocusLayout;

public class FocusAnimUtil {
    private static final String TAG = FocusAnimUtil.class.getSimpleName();
    private static long animDuration = 300;

    public static void scale(final View view, float... radios) {
        Focus3Logger.i(TAG, ".scale for " + Focus3Util.getString(view));
        ValueAnimator animator = ValueAnimator.ofFloat(radios);
        animator.setTarget(view);
        animator.setDuration(animDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    FocusAnimUtil.invalidateFocusLayout(view);
                    view.setScaleX(((Float) animation.getAnimatedValue()).floatValue());
                    view.setScaleY(((Float) animation.getAnimatedValue()).floatValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        animator.start();
    }

    public static ValueAnimator getBindAnimator(final View view) {
        Focus3Logger.i(TAG, ".getBindAnimator for " + Focus3Util.getString(view));
        ValueAnimator animator = ValueAnimator.ofFloat(new float[0]);
        animator.setTarget(view);
        animator.setDuration(animDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    FocusAnimUtil.invalidateFocusLayout(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return animator;
    }

    /* access modifiers changed from: private */
    public static void invalidateFocusLayout(View view) {
        View focusLayout;
        Focus3Logger.i(TAG, ".invalidateFocusLayout for " + Focus3Util.getString(view));
        if (view != null && view.getRootView() != null && (focusLayout = view.getRootView().findViewWithTag(FocusLayout.VIEW_TREE_TAG)) != null && (focusLayout instanceof FocusLayout)) {
            ((FocusLayout) focusLayout).refresh(FocusLayout.RefreshReason.byAnimateUpdate);
        }
    }
}
