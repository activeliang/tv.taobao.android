package com.yunos.tv.blitz.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.yunos.tv.blitz.BlitzContextWrapper;
import com.yunos.tv.blitz.broadcast.BlitzBroadcastManager;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.utils.KeySoundUtil;
import com.yunos.tv.blitz.view.BlitzBridgeSurfaceView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class BzBaseActivity extends Activity {
    private static final ArrayList<BzBaseActivity> ActivityList = new ArrayList<>();
    private static final boolean RELEASE_BOTTOM = true;
    private static final int RELEASE_BOTTOM_COUNT = 5;
    static String TAG = "BzBaseActivity";
    private boolean bBackKeyPressed = false;
    private int mActivityResultCb = 0;
    /* access modifiers changed from: private */
    public BlitzBridgeSurfaceView mBlitzBridgeView;
    private BlitzContextWrapper mBlitzContext;
    private BlitzBroadcastManager mBroadcastManager;
    protected boolean mDirectionKeyDownTriggered = false;
    private boolean mDisableDecorViewBgColor = false;
    public Runnable mPlayKeySoundRunnale = new Runnable() {
        public void run() {
            Log.d(BzBaseActivity.TAG, "mPlayKeySoundRunnale run ");
            if (BzBaseActivity.this.mDirectionKeyDownTriggered) {
                KeySoundUtil.playSoundEffect(BzBaseActivity.this.findBzBridgeView((FrameLayout) BzBaseActivity.this.getWindow().getDecorView()));
                BzBaseActivity.this.mDirectionKeyDownTriggered = false;
            }
        }
    };
    private PowerManager.WakeLock mScreenWakeLock;
    private SoftKeyboardGlobalLayoutListener mSoftKeyboardStatusListener;

    /* access modifiers changed from: protected */
    public void initBlitzContext(String initStr, int type) {
        this.mBroadcastManager = new BlitzBroadcastManager(this);
        FrameLayout rootView = (FrameLayout) getWindow().getDecorView();
        this.mBlitzBridgeView = (BlitzBridgeSurfaceView) findBzBridgeView(rootView);
        if (this.mBlitzBridgeView != null) {
            Log.d(TAG, "find blitz bridge surfaceview");
            this.mBlitzContext = new BlitzContextWrapper(this, true, this.mBlitzBridgeView);
        } else {
            Log.d(TAG, "not found bridge surfaceview. create one");
            this.mBlitzContext = new BlitzContextWrapper(this, false, (View) null);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
            this.mBlitzBridgeView = (BlitzBridgeSurfaceView) this.mBlitzContext.getmBlitzSurface();
            rootView.addView(this.mBlitzBridgeView, lp);
        }
        if (!this.mDisableDecorViewBgColor) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(17170444));
        }
        this.mBlitzContext.initContext(initStr, type);
        ActivityList.add(this);
        if (ActivityList.size() > 5) {
            ActivityList.remove(0);
            ActivityList.get(0).finish();
        }
        addKeyboardVisibilityListener();
    }

    /* access modifiers changed from: protected */
    public void disableDefaultDecorViewBgColor(boolean disable) {
        this.mDisableDecorViewBgColor = disable;
    }

    /* access modifiers changed from: private */
    public View findBzBridgeView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View vFound = findBzBridgeView(vg.getChildAt(i));
                if (vFound != null) {
                    return vFound;
                }
            }
        } else if (v instanceof BlitzBridgeSurfaceView) {
            return v;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "lifecycle activity:" + this + "  onCreate");
        getWindow().getDecorView().setFocusable(true);
        if (BzAppConfig.context != null && BzAppConfig.context.getUCacheEnable() && BzAppConfig.context.getCurrentActivityCount() == 1) {
            BzAppConfig.context.updateBlitzUCache();
        }
    }

    /* access modifiers changed from: protected */
    public void handleBackPress() {
        super.onBackPressed();
    }

    public void onBackPressed() {
        Log.d(TAG, "pagestack onbackpress");
        if (this.mBlitzContext == null || (this.mBlitzContext != null && this.mBlitzContext.isLastPage())) {
            Log.d(TAG, "pagestack not h5 page or there is only one page in this activity.actually handle back press");
            handleBackPress();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (4 == event.getKeyCode()) {
            Log.d(TAG, "back key up!");
            if (!this.bBackKeyPressed) {
                this.bBackKeyPressed = false;
                return super.onKeyUp(keyCode, event);
            }
            this.bBackKeyPressed = false;
        }
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onKeyEvent(keyCode, false);
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (4 == event.getKeyCode()) {
            Log.d(TAG, "back key down!");
            this.bBackKeyPressed = true;
        }
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onKeyEvent(keyCode, true);
        }
        this.mDirectionKeyDownTriggered = true;
        runOnUiThread(this.mPlayKeySoundRunnale);
        return super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mBlitzContext == null) {
            return false;
        }
        if (event.getAction() == 0) {
            this.mBlitzContext.onTouchEvent(0, (int) event.getX(), (int) event.getY());
        } else if (event.getAction() == 1) {
            this.mBlitzContext.onTouchEvent(2, (int) event.getX(), (int) event.getY());
        } else if (event.getAction() == 2) {
            this.mBlitzContext.onTouchEvent(1, (int) event.getX(), (int) event.getY());
        }
        return true;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (this.mBlitzContext == null) {
            return false;
        }
        if (event.getAction() != 7) {
            return true;
        }
        this.mBlitzContext.onTouchEvent(1, (int) event.getX(), (int) event.getY());
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(TAG, "lifecycle activity:" + this + "  onresume");
        this.bBackKeyPressed = false;
        if (this.mBlitzContext != null) {
            this.mBlitzContext.resumeRender();
            this.mBlitzContext.onResume();
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.d(TAG, "lifecycle activity:" + this + "  onstop");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.d(TAG, "lifecycle activity:" + this + "  onPause");
        this.bBackKeyPressed = false;
        if (this.mBlitzContext != null) {
            this.mBlitzContext.stopRender();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.d(TAG, "lifecycle activity:" + this + "  ondestroy");
        super.onDestroy();
        removeKeyboardVisibilityListener();
        if (this.mBroadcastManager != null) {
            this.mBroadcastManager.onDestory();
        }
        this.mBroadcastManager = null;
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onDestroy();
        }
        this.mBlitzContext = null;
        int index = ActivityList.indexOf(this);
        if (index >= 0) {
            ActivityList.remove(index);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        JSONObject json = new JSONObject();
        try {
            json.put("requestCode", requestCode);
            json.put("resultCode", resultCode);
            if (!(data == null || (bundle = data.getExtras()) == null)) {
                JSONObject jsonIntent = new JSONObject();
                for (String key : bundle.keySet()) {
                    try {
                        if (Build.VERSION.SDK_INT >= 19) {
                            jsonIntent.put(key, JSONObject.wrap(bundle.get(key)));
                        } else {
                            jsonIntent.put(key, bundle.get(key));
                        }
                    } catch (JSONException e) {
                    }
                }
                json.put("data", jsonIntent);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.i("BLITZ", "onActivityResult: " + json.toString());
        if (this.mBlitzContext != null && this.mActivityResultCb != 0) {
            this.mBlitzContext.replyCallBack(this.mActivityResultCb, true, json.toString());
        }
    }

    public void setActivityResultCbData(int callback) {
        this.mActivityResultCb = callback;
    }

    static class UpdateHandler extends Handler {
        WeakReference<BzBaseActivity> mWeakRefMainActivity = null;

        public UpdateHandler(BzBaseActivity activity) {
            this.mWeakRefMainActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private void checkBlitzContextInit(String intStr, int type) {
        if (this.mBlitzContext == null) {
            initBlitzContext(intStr, type);
        }
    }

    public BlitzContextWrapper getBlitzContext() {
        return this.mBlitzContext;
    }

    public void loadWithUrl(String url) {
        BzAppConfig.context.pageLoadStart();
        checkBlitzContextInit(url, 1);
        this.mBlitzContext.setmEntryUrl(url);
    }

    public void loadWithUrlAndPagedata(String url, String pagedata) {
        BzAppConfig.context.pageLoadStart();
        checkBlitzContextInit(url, 1);
        this.mBlitzContext.setEntryUrlAndPagedata(url, pagedata);
    }

    public void loadWithData(String data) {
        BzAppConfig.context.pageLoadStart();
        checkBlitzContextInit(data, 2);
        this.mBlitzContext.loadData(data);
    }

    public void setActivityRetained(boolean retained) {
        this.mBlitzContext.setRetained(retained);
    }

    public void scrollBy(int x, int y) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.scrollBy(x, y);
        }
    }

    public void setScrollBarWidth(int bgwidth, int blockwidth) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.setScrollBarWidth(bgwidth, blockwidth);
        }
    }

    public void setScrollBarColor(int bg_r, int bg_g, int bg_b, int bg_a, int r, int g, int b, int a) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.setScrollBarColor(bg_r, bg_g, bg_b, bg_a, r, g, b, a);
        }
    }

    public void showScrollBar(int isShow) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.showScrollBar(isShow);
        }
    }

    public void setScrollBarFade(int isfade, int fadeDuration, int fadeWait) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.setScrollBarFade(isfade, fadeDuration, fadeWait);
        }
    }

    public void setWebViewBackgroundColor(int r, int g, int b, int a) {
        if (this.mBlitzContext != null) {
            this.mBlitzContext.setWebViewBackgroundColor(r, g, b, a);
        }
    }

    public void setBgDrawBeforeUrlLoaded(Drawable bgDrawable) {
        Log.d(TAG, "setBgDrawable");
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().getDecorView().setBackgroundDrawable(bgDrawable);
        } else {
            getWindow().getDecorView().setBackground(bgDrawable);
        }
    }

    public BlitzBroadcastManager getBroadcastManager() {
        return this.mBroadcastManager;
    }

    public PowerManager.WakeLock getScreenWakeLock() {
        return this.mScreenWakeLock;
    }

    public void setScreenWakeLock(PowerManager.WakeLock wakeLock) {
        this.mScreenWakeLock = wakeLock;
    }

    class SoftKeyboardGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private Activity mActivity;

        SoftKeyboardGlobalLayoutListener(Activity activity) {
            this.mActivity = activity;
        }

        public void onGlobalLayout() {
            View contentView = this.mActivity.findViewById(16908290);
            Rect rect = new Rect();
            contentView.getWindowVisibleDisplayFrame(rect);
            int heightDiff = contentView.getRootView().getHeight() - (rect.bottom - rect.top);
            Log.d(BzBaseActivity.TAG, "SoftKeyboardGlobalLayoutListener onGlobalLayout, heightDiff = " + heightDiff);
            if (heightDiff >= 300) {
                Log.d(BzBaseActivity.TAG, "SoftKeyboardGlobalLayoutListener onGlobalLayout, softkeyboard shown!");
                BzAppConfig.context.setSoftKeyboardHeight(heightDiff);
                BzAppConfig.context.setSoftKeyboardStatus(true);
                BzBaseActivity.this.mBlitzBridgeView.notifyImeShowStatus(true);
            } else if (heightDiff == 0) {
                Log.d(BzBaseActivity.TAG, "SoftKeyboardGlobalLayoutListener onGlobalLayout, softkeyboard hide!");
                BzAppConfig.context.setSoftKeyboardStatus(false);
                BzBaseActivity.this.mBlitzBridgeView.notifyImeShowStatus(false);
            }
        }
    }

    private void addKeyboardVisibilityListener() {
        this.mSoftKeyboardStatusListener = new SoftKeyboardGlobalLayoutListener(this);
        findViewById(16908290).getViewTreeObserver().addOnGlobalLayoutListener(this.mSoftKeyboardStatusListener);
    }

    private void removeKeyboardVisibilityListener() {
        View contentView = findViewById(16908290);
        if (Build.VERSION.SDK_INT < 16) {
            contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this.mSoftKeyboardStatusListener);
        } else {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this.mSoftKeyboardStatusListener);
        }
    }

    public void showInputMethod() {
        if (this.mBlitzBridgeView != null) {
            this.mBlitzBridgeView.showInputMethod();
        }
    }

    public void hideInputMethod() {
        if (this.mBlitzBridgeView != null) {
            this.mBlitzBridgeView.hideInputMethod();
        }
    }
}
