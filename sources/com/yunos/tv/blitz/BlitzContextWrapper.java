package com.yunos.tv.blitz;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.audio.AudioPlayerManager;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.utils.NetworkUtil;
import com.yunos.tv.blitz.video.VideoViewManager;
import com.yunos.tv.blitz.view.BlitzBridgeSurfaceView;
import java.lang.ref.WeakReference;

public class BlitzContextWrapper implements SurfaceHolder.Callback, NetworkUtil.BzNetworkChangeListener {
    public static final int CONTEXT_INIT_TYPE_DATA = 2;
    public static final int CONTEXT_INIT_TYPE_URL = 1;
    static final int E_JAVA2C_NOTIFY_EVENT_LOGIN_STATUS_CHANGE = 3;
    static final int E_JAVA2C_NOTIFY_EVENT_NETWORK_CHANGE = 0;
    static final int E_JAVA2C_NOTIFY_EVENT_PAGE_HIDE = 2;
    static final int E_JAVA2C_NOTIFY_EVENT_PAGE_SHOW = 1;
    static final int MSG_ID_CREATE_VIDEO_VIEW = 1;
    static final int MSG_ID_LAST_PAGE_QUIT = 2;
    static String TAG = BlitzContextWrapper.class.getSimpleName();
    boolean bLastPage = false;
    boolean bSurfaceReady = false;
    AudioPlayerManager mAudioManager = null;
    private BlitzBridgeSurfaceView mBlitzSurface;
    WeakReference<Context> mContext = null;
    Handler mContextWrapperHandler;
    String mEntryUrl = null;
    GLEnvironment mGlEnv;
    String mPagedata = null;
    VideoViewManager mVideoViewMgr = null;

    public String getmEntryUrl() {
        return this.mEntryUrl;
    }

    public void setmEntryUrl(String mEntryUrl2) {
        this.mEntryUrl = mEntryUrl2;
        if (this.mGlEnv != null) {
            this.mGlEnv.setEntryUrl(mEntryUrl2);
        }
        this.mBlitzSurface.setZOrderOnTop(true);
        this.mBlitzSurface.getHolder().setFormat(-2);
    }

    public void setEntryUrlAndPagedata(String mEntryUrl2, String pagedata) {
        this.mEntryUrl = mEntryUrl2;
        this.mPagedata = pagedata;
        if (this.mGlEnv != null) {
            if (pagedata == null || pagedata.isEmpty()) {
                this.mGlEnv.setEntryUrl(mEntryUrl2);
            } else {
                this.mGlEnv.setEntryUrlAndPagedata(mEntryUrl2, this.mPagedata);
            }
        }
        this.mBlitzSurface.setZOrderOnTop(true);
        this.mBlitzSurface.getHolder().setFormat(-2);
    }

    public void setPreEntryUrl(String url, int width, int height) {
        this.mEntryUrl = url;
        if (this.mGlEnv != null) {
            this.mGlEnv.setPreEntryUrl(url, width, height);
        }
    }

    public BlitzContextWrapper(Context context, boolean bzBridgeViewExist, View bridgeView) {
        this.mContext = new WeakReference<>(context);
        if (bzBridgeViewExist) {
            this.mBlitzSurface = (BlitzBridgeSurfaceView) bridgeView;
        } else {
            this.mBlitzSurface = new BlitzBridgeSurfaceView(context);
        }
        this.mContextWrapperHandler = new BlitzContextWrapperHandler(this);
    }

    public boolean initContext(String initStr, int type) {
        this.mGlEnv = new GLEnvironment(this.mContext, initStr, type);
        if (this.mGlEnv == null) {
            Log.e(TAG, "glenvironment create fail!!");
            return false;
        }
        this.mGlEnv.initWithNewContext();
        this.mBlitzSurface.bindToListener(this, this.mGlEnv);
        NetworkUtil.getInstance().addNetworkChangeListner(this);
        createVideoView();
        createAudioPlayerManager();
        return true;
    }

    public void deinitContext() {
        if (this.mGlEnv != null) {
            try {
                this.mGlEnv.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        this.mGlEnv = null;
        this.mBlitzSurface = null;
        NetworkUtil.getInstance().removeNetworkChangeListener(this);
        this.mBlitzSurface = null;
        this.mContextWrapperHandler = null;
    }

    public void setRetained(boolean retained) {
        if (this.mGlEnv != null) {
            this.mGlEnv.setRetained(retained);
        }
    }

    public SurfaceView getmBlitzSurface() {
        return this.mBlitzSurface;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        this.bSurfaceReady = true;
        if (this.mGlEnv != null) {
            this.mGlEnv.surfaceChanged(this.mBlitzSurface.getSurfaceId(), width, height);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        if (this.mGlEnv != null) {
            this.mGlEnv.surfaceCreated(this.mBlitzSurface.getSurfaceId(), holder.getSurface());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "sufacedestroyed");
        if (this.mGlEnv != null) {
            this.mGlEnv.surfaceDestroyed();
        }
    }

    public void update() {
        if (this.mGlEnv == null || this.bSurfaceReady) {
        }
    }

    public void onDestroy() {
        deinitContext();
        if (this.mVideoViewMgr != null) {
            this.mVideoViewMgr.destroy();
        }
        this.mVideoViewMgr = null;
        if (this.mAudioManager != null) {
            this.mAudioManager.onContextDestroy();
        }
        this.mAudioManager = null;
    }

    public void onResume() {
        if (this.mVideoViewMgr != null) {
            this.mVideoViewMgr.java2cResumeContext();
        }
        if (this.mAudioManager != null) {
            this.mAudioManager.onContextResume();
        }
    }

    public void onKeyEvent(int keyCode, boolean isDown) {
        if (this.mGlEnv != null) {
            this.mGlEnv.keyeventToSurface(this.mBlitzSurface.getSurfaceId(), keyCode, isDown);
        }
    }

    public void onTouchEvent(int type, int x, int y) {
        if (this.mGlEnv != null) {
            this.mGlEnv.onTouchEvent(type, x, y);
        }
    }

    public void loadData(String data) {
        if (this.mGlEnv != null) {
            this.mGlEnv.loadData(data);
        }
    }

    public void stopRender() {
        if (this.mGlEnv != null) {
            this.mGlEnv.notifyEvent2Core(2, "");
            this.mGlEnv.stopRender();
        }
    }

    public void resumeRender() {
        if (this.mGlEnv != null) {
            this.mGlEnv.resumeRender();
            this.mGlEnv.notifyEvent2Core(1, "");
        }
    }

    private void createAudioPlayerManager() {
        if (this.mAudioManager != null) {
            BzDebugLog.w(TAG, "audio player manager already exist");
            return;
        }
        this.mAudioManager = new AudioPlayerManager(this.mContext);
        this.mAudioManager.audioPlayerManagerInit();
    }

    public void createVideoView() {
        if (this.mContext.get() != null && this.mVideoViewMgr == null && (this.mContext.get() instanceof Activity)) {
            this.mVideoViewMgr = new VideoViewManager();
            this.mVideoViewMgr.Init((FrameLayout) ((Activity) this.mContext.get()).getWindow().getDecorView());
        }
    }

    public void lastPageQuit() {
        Log.d(TAG, "pagestack lastPageQuit");
        if (this.mContext.get() != null) {
            this.bLastPage = true;
            Activity activity = (Activity) this.mContext.get();
            if (activity != null) {
                activity.onBackPressed();
            }
        }
    }

    public void postCreateVideoView() {
        this.mContextWrapperHandler.sendMessage(this.mContextWrapperHandler.obtainMessage(1));
    }

    public boolean isLastPage() {
        return this.bLastPage;
    }

    public void postlastPageQuit() {
        this.mContextWrapperHandler.sendMessage(this.mContextWrapperHandler.obtainMessage(2));
    }

    static class BlitzContextWrapperHandler extends Handler {
        BlitzContextWrapper mContextWrapper = null;

        public BlitzContextWrapperHandler(BlitzContextWrapper ctxWrapper) {
            this.mContextWrapper = ctxWrapper;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (this.mContextWrapper != null) {
                        this.mContextWrapper.createVideoView();
                        break;
                    }
                    break;
                case 2:
                    if (this.mContextWrapper != null) {
                        this.mContextWrapper.lastPageQuit();
                        break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void onNetworkChanged(boolean isAvailable, String netType) {
        if (this.mGlEnv != null) {
            BzResult result = new BzResult();
            if (netType == null) {
                netType = "";
            }
            result.addData("type", netType);
            result.addData("connected", isAvailable);
            result.addData("name", "network");
            result.setSuccess();
            this.mGlEnv.notifyEvent2Core(0, result.toJsonString());
        }
    }

    public void loginStatusChange(boolean isLogin) {
        if (this.mGlEnv != null) {
            BzResult result = new BzResult();
            result.addData("login", isLogin ? "true" : "false");
            result.setSuccess();
            this.mGlEnv.notifyEvent2Core(3, result.toJsonString());
        }
    }

    public void scrollBy(int x, int y) {
        if (this.mGlEnv != null) {
            this.mGlEnv.scrollBy(x, y);
        }
    }

    public void setScrollBarWidth(int bgwidth, int blockwidth) {
        if (this.mGlEnv != null) {
            this.mGlEnv.setScrollBarWidth(bgwidth, blockwidth);
        }
    }

    public void setScrollBarColor(int bg_r, int bg_g, int bg_b, int bg_a, int r, int g, int b, int a) {
        if (this.mGlEnv != null) {
            this.mGlEnv.setScrollBarColor(bg_r, bg_g, bg_b, bg_a, r, g, b, a);
        }
    }

    public void showScrollBar(int isShow) {
        if (this.mGlEnv != null) {
            this.mGlEnv.showScrollBar(isShow);
        }
    }

    public void setScrollBarFade(int isfade, int fadeDuration, int fadeWait) {
        if (this.mGlEnv != null) {
            this.mGlEnv.setScrollBarFade(isfade, fadeDuration, fadeWait);
        }
    }

    public void setWebViewBackgroundColor(int r, int g, int b, int a) {
        if (this.mGlEnv != null) {
            this.mGlEnv.setWebViewBackgroundColor(r, g, b, a);
        }
    }

    public boolean replyCallBack(int callback, boolean success, String result) {
        if (BzAppConfig.context != null) {
            return BzAppConfig.context.replyCallBack(callback, success, result);
        }
        return false;
    }
}
