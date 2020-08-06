package com.yunos.tv.blitz.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.yunos.tv.blitz.BlitzContextWrapper;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.global.BzAppMain;
import com.yunos.tv.blitz.listener.BzPageStatusListener;
import com.yunos.tv.blitz.service.BlitzServiceClient;

public abstract class BlitzPopView extends Dialog {
    /* access modifiers changed from: private */
    public static final String TAG = BlitzPopView.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final Handler mHandler = new Handler(Looper.getMainLooper());
    private BlitzContextWrapper mBlitzContext;
    private BlitzServiceClient mBlitzServiceClient;
    private BzAppMain mBzAppMain;
    private BzPageStatusListener mBzPageStatusListener = new BzPageStatusListener() {
        public void onPageLoadStart(Context context, String url) {
            BlitzPopView.mHandler.removeCallbacks(BlitzPopView.this.mWebLoadStartedRunnable);
            BlitzPopView.mHandler.post(BlitzPopView.this.mWebLoadStartedRunnable);
        }

        public void onPageLoadFinished(Context context, String url) {
            Log.i(BlitzPopView.TAG, "onPageLoadFinished, url = " + url);
            BlitzPopView.mHandler.removeCallbacks(BlitzPopView.this.mWebLoadFinishedRunnable);
            BlitzPopView.mHandler.post(BlitzPopView.this.mWebLoadFinishedRunnable);
        }

        public void onPageLoadError(Context context, String url, String jsonData) {
            Log.i(BlitzPopView.TAG, "onPageLoadError, url = " + url);
            BlitzPopView.mHandler.removeCallbacks(BlitzPopView.this.mWebLoadErrorRunnable);
            BlitzPopView.mHandler.post(BlitzPopView.this.mWebLoadErrorRunnable);
        }
    };
    private Context mContext;
    private boolean mIsBackKeyPressed = false;
    /* access modifiers changed from: private */
    public String mUrl;
    /* access modifiers changed from: private */
    public final Runnable mWebLoadErrorRunnable = new Runnable() {
        public void run() {
            BlitzPopView.this.onWebPageError(BlitzPopView.this.mUrl);
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mWebLoadFinishedRunnable = new Runnable() {
        public void run() {
            BlitzPopView.this.onWebPageFinished(BlitzPopView.this.mUrl);
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mWebLoadStartedRunnable = new Runnable() {
        public void run() {
            BlitzPopView.this.onWebPageStarted(BlitzPopView.this.mUrl);
        }
    };
    private int mWebViewGravity;
    private int mWebViewHeight;
    private int mWebViewResId;
    private int mWebViewWidth;

    /* access modifiers changed from: protected */
    public abstract void onWebPageError(String str);

    /* access modifiers changed from: protected */
    public abstract void onWebPageFinished(String str);

    /* access modifiers changed from: protected */
    public abstract void onWebPageStarted(String str);

    public BlitzPopView(Context context, String url, int layoutResID, int webViewResId, int styleID, int width, int height, int gravity) {
        super(context, styleID);
        this.mContext = context;
        this.mWebViewWidth = width;
        this.mWebViewHeight = height;
        this.mWebViewGravity = gravity;
        this.mWebViewResId = webViewResId;
        this.mBzAppMain = BzAppConfig.context;
        initPopView(url, layoutResID);
    }

    private void initPopView(String url, int layoutResID) {
        this.mUrl = url;
        setContentView(layoutResID);
        initWindowLayoutParams();
    }

    private void initWindowLayoutParams() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.type = 2003;
        Point winSize = new Point();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getSize(winSize);
        params.width = winSize.x;
        params.height = winSize.y;
        window.setAttributes(params);
    }

    /* access modifiers changed from: protected */
    public boolean initBlitzContext() {
        SurfaceView blitzSurfaceView = (SurfaceView) findViewById(this.mWebViewResId);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) blitzSurfaceView.getLayoutParams();
        params.width = this.mWebViewWidth;
        params.height = this.mWebViewHeight;
        params.gravity = this.mWebViewGravity;
        blitzSurfaceView.setLayoutParams(params);
        this.mBlitzServiceClient = new BlitzServiceClient(super.getContext().getApplicationContext());
        if (!this.mBlitzServiceClient.init()) {
            this.mBlitzServiceClient = null;
            return false;
        }
        this.mBlitzContext = new BlitzContextWrapper(this.mContext, true, findViewById(this.mWebViewResId));
        if (!this.mBlitzContext.initContext((String) null, 1)) {
            this.mBlitzServiceClient = null;
            this.mBlitzContext = null;
            return false;
        }
        this.mBzAppMain.setCurrentDialog(this);
        this.mBzAppMain.setPageStatusListener(this.mBzPageStatusListener);
        this.mBlitzContext.setPreEntryUrl(this.mUrl, this.mWebViewWidth, this.mWebViewHeight);
        return true;
    }

    /* access modifiers changed from: protected */
    public void setBlitzViewBackgroundColor(int r, int g, int b, int a) {
        this.mBlitzContext.setWebViewBackgroundColor(r, g, b, a);
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        Log.i(TAG, "dismiss");
        this.mBzPageStatusListener = null;
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onDestroy();
        }
        this.mBzAppMain.setCurrentDialog((Dialog) null);
        this.mBlitzServiceClient.deinit();
        this.mBlitzServiceClient = null;
        this.mBlitzContext = null;
        super.dismiss();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (4 == event.getKeyCode()) {
            Log.d(TAG, "back key up!");
            if (!this.mIsBackKeyPressed) {
                this.mIsBackKeyPressed = false;
                return super.onKeyUp(keyCode, event);
            }
            this.mIsBackKeyPressed = false;
        }
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onKeyEvent(keyCode, false);
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (4 == event.getKeyCode()) {
            Log.d(TAG, "back key down!");
            this.mIsBackKeyPressed = true;
        }
        if (this.mBlitzContext != null) {
            this.mBlitzContext.onKeyEvent(keyCode, true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
