package com.yunos.tv.blitz;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import java.lang.ref.WeakReference;

public class GLEnvironment {
    static final String TAG = GLEnvironment.class.getSimpleName();
    static boolean bSetAppEnv = false;
    private static SparseArray<WeakReference<GLEnvironment>> envList = new SparseArray<>();
    private boolean bSurfaceCreated = false;
    private int bgColorA = -1;
    private int bgColorB = -1;
    private int bgColorG = -1;
    private int bgColorR = -1;
    private int glEnvId = 0;
    WeakReference<Context> mContext = null;
    private boolean mManageContext = true;
    private WeakReference<GLEnvironment> mWeakRefThis;

    private native boolean nativeActivate();

    private native boolean nativeActivateSurfaceId(int i);

    private native int nativeAddSurface(Surface surface);

    private native int nativeAddSurfaceFromMediaRecorder(MediaRecorder mediaRecorder);

    private native int nativeAddSurfaceWidthHeight(Surface surface, int i, int i2);

    private native boolean nativeAllocate(String str, String str2, int i, int i2);

    private native boolean nativeCommitString(String str);

    private native boolean nativeDeactivate();

    private native boolean nativeDeallocate();

    private native boolean nativeDisconnectSurfaceMediaSource(MediaRecorder mediaRecorder);

    private native boolean nativeInitWithCurrentContext();

    private native boolean nativeInitWithNewContext();

    private native boolean nativeIsActive();

    private static native boolean nativeIsAnyContextActive();

    private native boolean nativeIsContextActive();

    private native boolean nativeIsLastPage();

    private native boolean nativeKeyeventToSurface(int i, int i2, boolean z);

    private native boolean nativeLoadData(String str, int i);

    private native boolean nativeNotifyEvent2Core(int i, String str);

    private native boolean nativeNotifyImeShowStatus(boolean z);

    private native boolean nativeOnTouchEvent(int i, int i2, int i3);

    private native boolean nativeRemoveSurfaceId(int i);

    private native boolean nativeResumeRender();

    private native boolean nativeSendKeyEvent(String str);

    private native boolean nativeSetEntryUrl(String str);

    private native boolean nativeSetEntryUrlAndPagedata(String str, String str2);

    private native boolean nativeSetPreEntryUrl(String str, int i, int i2);

    private native boolean nativeSetRetained(boolean z);

    private native boolean nativeSetSurfaceTimestamp(long j);

    private native boolean nativeStopRender();

    private native boolean nativeSurfaceChanged(int i, int i2, int i3);

    private native boolean nativeSurfaceCreated(Surface surface, int i);

    private native boolean nativeSurfaceDestroyed();

    private native boolean nativeSwapBuffers();

    private native boolean nativeUpdateSurface();

    private native boolean nativescrollBy(int i, int i2);

    private native boolean nativesetScrollBarColor(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private native boolean nativesetScrollBarFade(int i, int i2, int i3);

    private native boolean nativesetScrollBarWidth(int i, int i2);

    private native boolean nativesetWebViewBgColor(int i, int i2, int i3, int i4);

    private native boolean nativeshowScrollBar(int i);

    public GLEnvironment(WeakReference<Context> context, String initStr, int type) {
        this.mContext = context;
        BzResult appEnv = new BzResult();
        this.mWeakRefThis = new WeakReference<>(this);
        envList.append(this.mWeakRefThis.hashCode(), this.mWeakRefThis);
        if (!bSetAppEnv) {
            Context cnt = (Context) this.mContext.get();
            if (cnt != null) {
                appEnv.addData("packagename", cnt.getPackageName());
                try {
                    appEnv.addData("packageversion", cnt.getPackageManager().getPackageInfo(cnt.getPackageName(), 0).versionCode);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                appEnv.addData("packagedir", cnt.getApplicationInfo().dataDir);
            }
            Log.d("GLEnvironment", appEnv.toJsonString());
            bSetAppEnv = true;
            nativeAllocate(appEnv.toJsonString(), initStr, type, this.mWeakRefThis.hashCode());
            return;
        }
        nativeAllocate((String) null, initStr, type, this.mWeakRefThis.hashCode());
    }

    public static void lastPageQuit() {
        Log.d("GLEnvironment", "lastPageQuit");
        WeakReference<Activity> wa = BzAppConfig.context.getCurrentActivity();
        Activity a = wa != null ? (Activity) wa.get() : null;
        if (a == null || !(a instanceof BzBaseActivity)) {
            Log.w("GLEnvironment", "lastPageQuit: no BzBaseActivity");
            return;
        }
        BzBaseActivity ba = (BzBaseActivity) a;
        if (ba.getBlitzContext() != null) {
            ba.getBlitzContext().postlastPageQuit();
        }
    }

    public static void onFirstFrame() {
        Log.d("GLEnvironment", "onFirstFrame");
        WeakReference<Activity> wa = BzAppConfig.context.getCurrentActivity();
        Activity a = wa != null ? (Activity) wa.get() : null;
        if (a == null || !(a instanceof BzBaseActivity)) {
            Log.w("GLEnvironment", "lastPageQuit: no BzBaseActivity");
            return;
        }
        final BzBaseActivity ba = (BzBaseActivity) a;
        ba.runOnUiThread(new Runnable() {
            public void run() {
                ba.setBgDrawBeforeUrlLoaded(new ColorDrawable(ViewCompat.MEASURED_STATE_MASK));
            }
        });
    }

    public static void createVideoView() {
        Log.d("GLEnvironment", "createVideoView");
        WeakReference<Activity> wa = BzAppConfig.context.getCurrentActivity();
        Activity a = wa != null ? (Activity) wa.get() : null;
        if (a == null || !(a instanceof BzBaseActivity)) {
            Log.w("GLEnvironment", "createVideoView: no BzBaseActivity");
            return;
        }
        BzBaseActivity ba = (BzBaseActivity) a;
        if (ba.getBlitzContext() != null) {
            ba.getBlitzContext().postCreateVideoView();
        }
    }

    public static void finishActivity(int hashcode) {
        Context context;
        Log.d("GLEnvironment", "finishActivity hashcode:" + hashcode);
        WeakReference<GLEnvironment> glenv = envList.get(hashcode);
        if (glenv != null && glenv.get() != null && (context = (Context) ((GLEnvironment) glenv.get()).mContext.get()) != null && (context instanceof BzBaseActivity)) {
            final BzBaseActivity ba = (BzBaseActivity) context;
            ba.runOnUiThread(new Runnable() {
                public void run() {
                    BzDebugLog.w(GLEnvironment.TAG, "finish activity:" + ba);
                    ba.finish();
                }
            });
        }
    }

    public synchronized void tearDown() {
        if (this.glEnvId != -1) {
            nativeDeallocate();
            this.glEnvId = -1;
        }
    }

    public void finalize() throws Throwable {
        tearDown();
        envList.remove(this.mWeakRefThis.hashCode());
    }

    public void initWithNewContext() {
        this.mManageContext = true;
        if (!nativeInitWithNewContext()) {
            throw new RuntimeException("Could not initialize GLEnvironment with new context!");
        }
    }

    public void initWithCurrentContext() {
        this.mManageContext = false;
        if (!nativeInitWithCurrentContext()) {
            throw new RuntimeException("Could not initialize GLEnvironment with current context!");
        }
    }

    public boolean isActive() {
        return nativeIsActive();
    }

    public boolean isContextActive() {
        return nativeIsContextActive();
    }

    public static boolean isAnyContextActive() {
        return nativeIsAnyContextActive();
    }

    public void activate() {
        if (Looper.myLooper() != null && Looper.myLooper().equals(Looper.getMainLooper())) {
            Log.e("FilterFramework", "Activating GL context in UI thread!");
        }
        if (this.mManageContext && !nativeActivate()) {
            throw new RuntimeException("Could not activate GLEnvironment!");
        }
    }

    public boolean isLastPage() {
        return nativeIsLastPage();
    }

    public void deactivate() {
        if (this.mManageContext && !nativeDeactivate()) {
            throw new RuntimeException("Could not deactivate GLEnvironment!");
        }
    }

    public void swapBuffers() {
        if (!nativeSwapBuffers()) {
            throw new RuntimeException("Error swapping EGL buffers!");
        }
    }

    public void updateSurface() {
        if (!nativeUpdateSurface()) {
            throw new RuntimeException("Error update surface !");
        }
    }

    public int registerSurface(Surface surface) {
        int result = nativeAddSurface(surface);
        if (result >= 0) {
            return result;
        }
        throw new RuntimeException("Error registering surface " + surface + Constant.BLURRY);
    }

    public int registerSurfaceTexture(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        int result = nativeAddSurfaceWidthHeight(surface, width, height);
        surface.release();
        if (result >= 0) {
            return result;
        }
        throw new RuntimeException("Error registering surfaceTexture " + surfaceTexture + Constant.BLURRY);
    }

    public int registerSurfaceFromMediaRecorder(MediaRecorder mediaRecorder) {
        int result = nativeAddSurfaceFromMediaRecorder(mediaRecorder);
        if (result >= 0) {
            return result;
        }
        throw new RuntimeException("Error registering surface from MediaRecorder" + mediaRecorder + Constant.BLURRY);
    }

    public void activateSurfaceWithId(int surfaceId) {
        if (!nativeActivateSurfaceId(surfaceId)) {
            throw new RuntimeException("Could not activate surface " + surfaceId + Constant.BLURRY);
        }
    }

    public void unregisterSurfaceId(int surfaceId) {
        if (!nativeRemoveSurfaceId(surfaceId)) {
            throw new RuntimeException("Could not unregister surface " + surfaceId + Constant.BLURRY);
        }
    }

    public void setSurfaceTimestamp(long timestamp) {
        if (!nativeSetSurfaceTimestamp(timestamp)) {
            throw new RuntimeException("Could not set timestamp for current surface!");
        }
    }

    public void surfaceCreated(int surfaceid, Surface surface) {
        if (!nativeSurfaceCreated(surface, surfaceid)) {
            throw new RuntimeException("Could not nativeSurfaceCreated!");
        }
    }

    public void surfaceChanged(int surfaceId, int width, int height) {
        if (!nativeSurfaceChanged(surfaceId, width, height)) {
            throw new RuntimeException("Could not nativeSurfaceChanged!");
        }
    }

    public void surfaceDestroyed() {
        if (!nativeSurfaceDestroyed()) {
            throw new RuntimeException("Could not nativeSurfaceDestroyed!");
        }
    }

    public void keyeventToSurface(int surfaceid, int keycode, boolean isDown) {
        if (!nativeKeyeventToSurface(surfaceid, keycode, isDown)) {
            throw new RuntimeException("Could not nativeKeyeventToSurface!");
        }
    }

    public void onTouchEvent(int type, int x, int y) {
        if (!nativeOnTouchEvent(type, x, y)) {
            throw new RuntimeException("Could not nativeOnTouchEvent!");
        }
    }

    public void loadData(String data) {
        if (!nativeLoadData(data, data.length())) {
            Log.e(TAG, "loaddata fail!!");
        }
    }

    public void stopRender() {
        if (!nativeStopRender()) {
            Log.e(TAG, "fail to stop render");
        }
    }

    public void resumeRender() {
        if (!nativeResumeRender()) {
            Log.e(TAG, "fail to resume render");
        }
    }

    public boolean notifyEvent2Core(int code, String param) {
        boolean ret = nativeNotifyEvent2Core(code, param);
        if (!ret) {
            Log.w(TAG, "reply callback fail!");
        }
        return ret;
    }

    public boolean scrollBy(int x, int y) {
        boolean ret = nativescrollBy(x, y);
        if (!ret) {
            Log.w(TAG, "scrollBy fail!");
        }
        return ret;
    }

    public boolean setScrollBarWidth(int bgwidth, int blockwidth) {
        boolean ret = nativesetScrollBarWidth(bgwidth, blockwidth);
        if (!ret) {
            Log.w(TAG, "setScrollBarWidth fail!");
        }
        return ret;
    }

    public boolean setScrollBarColor(int bg_r, int bg_g, int bg_b, int bg_a, int r, int g, int b, int a) {
        boolean ret = nativesetScrollBarColor(bg_r, bg_g, bg_b, bg_a, r, g, b, a);
        if (!ret) {
            Log.w(TAG, "setScrollBarColor fail!");
        }
        return ret;
    }

    public boolean showScrollBar(int isShow) {
        boolean ret = nativeshowScrollBar(isShow);
        if (!ret) {
            Log.w(TAG, "showScrollBar fail!");
        }
        return ret;
    }

    public boolean setScrollBarFade(int isfade, int fadeDuration, int fadeWait) {
        boolean ret = nativesetScrollBarFade(isfade, fadeDuration, fadeWait);
        if (!ret) {
            Log.w(TAG, "showScrollBar fail!");
        }
        return ret;
    }

    public boolean setWebViewBackgroundColor(int r, int g, int b, int a) {
        boolean ret = nativesetWebViewBgColor(r, g, b, a);
        if (!ret) {
            Log.w(TAG, "setWebViewBackgroundColor fail!");
        }
        return ret;
    }

    public boolean CommitStringForIME(String comStr) {
        return nativeCommitString(comStr);
    }

    public boolean SendKeyEventForIME(String keyEvent) {
        return nativeSendKeyEvent(keyEvent);
    }

    public void notifyImeShowStatus(boolean opened) {
        nativeNotifyImeShowStatus(opened);
    }

    public void setRetained(boolean retained) {
        nativeSetRetained(retained);
    }

    public void setEntryUrl(String mEntryUrl) {
        nativeSetEntryUrl(mEntryUrl);
    }

    public void setEntryUrlAndPagedata(String mEntryUrl, String pagedata) {
        nativeSetEntryUrlAndPagedata(mEntryUrl, pagedata);
    }

    public void setPreEntryUrl(String url, int width, int height) {
        nativeSetPreEntryUrl(url, width, height);
    }
}
