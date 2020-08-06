package com.taobao.mediaplay.playercontrol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import com.taobao.mediaplay.common.IMediaBackKeyEvent;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaKeyBackController implements Window.Callback {
    private Activity mActivity;
    CopyOnWriteArrayList<IMediaBackKeyEvent> mBackKeyEventList = new CopyOnWriteArrayList<>();
    Window.Callback mCallback;
    private final Object[] mLock = new Object[0];
    private Window mWindow;

    public MediaKeyBackController(Activity activity) {
        if (activity != null) {
            this.mActivity = activity;
            this.mCallback = activity.getWindow().getCallback();
            activity.getWindow().setCallback(this);
        }
    }

    public void registerKeyBackEventListener(IMediaBackKeyEvent backKeyEvent) {
        if (this.mBackKeyEventList == null || !this.mBackKeyEventList.contains(backKeyEvent)) {
            this.mBackKeyEventList.add(0, backKeyEvent);
        }
    }

    public void unregisterKeyBackEventListener(IMediaBackKeyEvent backKeyEvent) {
        if (this.mBackKeyEventList != null) {
            this.mBackKeyEventList.remove(backKeyEvent);
        }
    }

    public void handleKeyBack() {
        KeyEvent event = new KeyEvent(4, 0);
        Iterator<IMediaBackKeyEvent> it = this.mBackKeyEventList.iterator();
        while (it.hasNext()) {
            it.next().onBackKeyDown(event);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4 && event.getAction() == 0) {
            Iterator<IMediaBackKeyEvent> it = this.mBackKeyEventList.iterator();
            while (it.hasNext()) {
                if (it.next().onBackKeyDown(event)) {
                    return true;
                }
            }
        }
        return this.mCallback.dispatchKeyEvent(event);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return this.mCallback.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        return this.mCallback.dispatchTouchEvent(event);
    }

    public boolean dispatchTrackballEvent(MotionEvent event) {
        return this.mCallback.dispatchTrackballEvent(event);
    }

    @TargetApi(12)
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return this.mCallback.dispatchGenericMotionEvent(event);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return this.mCallback.dispatchPopulateAccessibilityEvent(event);
    }

    public View onCreatePanelView(int featureId) {
        return this.mCallback.onCreatePanelView(featureId);
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return this.mCallback.onCreatePanelMenu(featureId, menu);
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return this.mCallback.onPreparePanel(featureId, view, menu);
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        return this.mCallback.onMenuOpened(featureId, menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return this.mCallback.onMenuItemSelected(featureId, item);
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        this.mCallback.onWindowAttributesChanged(attrs);
    }

    public void onContentChanged() {
        this.mCallback.onContentChanged();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        this.mCallback.onWindowFocusChanged(hasFocus);
    }

    public void onAttachedToWindow() {
        this.mCallback.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        this.mCallback.onDetachedFromWindow();
    }

    public void onPanelClosed(int featureId, Menu menu) {
        this.mCallback.onPanelClosed(featureId, menu);
    }

    public boolean onSearchRequested() {
        return this.mCallback.onSearchRequested();
    }

    @TargetApi(23)
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return this.mCallback.onSearchRequested(searchEvent);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return this.mCallback.onWindowStartingActionMode(callback);
    }

    @TargetApi(23)
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return this.mCallback.onWindowStartingActionMode(callback, type);
    }

    public void onActionModeStarted(ActionMode mode) {
        this.mCallback.onActionModeStarted(mode);
    }

    public void onActionModeFinished(ActionMode mode) {
        this.mCallback.onActionModeFinished(mode);
    }

    public void destroy() {
        this.mBackKeyEventList.clear();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindWindow(android.view.Window r4) {
        /*
            r3 = this;
            java.lang.Object[] r1 = r3.mLock
            monitor-enter(r1)
            if (r4 == 0) goto L_0x0013
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x002d }
            if (r0 == 0) goto L_0x0013
            android.view.Window r0 = r3.mWindow     // Catch:{ all -> 0x002d }
            if (r0 != 0) goto L_0x0013
            android.view.Window$Callback r0 = r4.getCallback()     // Catch:{ all -> 0x002d }
            if (r0 != r3) goto L_0x0015
        L_0x0013:
            monitor-exit(r1)     // Catch:{ all -> 0x002d }
        L_0x0014:
            return
        L_0x0015:
            r3.mWindow = r4     // Catch:{ all -> 0x002d }
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x002d }
            android.view.Window r0 = r0.getWindow()     // Catch:{ all -> 0x002d }
            android.view.Window$Callback r2 = r3.mCallback     // Catch:{ all -> 0x002d }
            r0.setCallback(r2)     // Catch:{ all -> 0x002d }
            android.view.Window$Callback r0 = r4.getCallback()     // Catch:{ all -> 0x002d }
            r3.mCallback = r0     // Catch:{ all -> 0x002d }
            r4.setCallback(r3)     // Catch:{ all -> 0x002d }
            monitor-exit(r1)     // Catch:{ all -> 0x002d }
            goto L_0x0014
        L_0x002d:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002d }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.mediaplay.playercontrol.MediaKeyBackController.bindWindow(android.view.Window):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unbindWindow() {
        /*
            r3 = this;
            java.lang.Object[] r1 = r3.mLock
            monitor-enter(r1)
            android.view.Window r0 = r3.mWindow     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x001f
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x001f
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x0042 }
            android.view.Window r0 = r0.getWindow()     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x001f
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x0042 }
            android.view.Window r0 = r0.getWindow()     // Catch:{ all -> 0x0042 }
            android.view.Window$Callback r0 = r0.getCallback()     // Catch:{ all -> 0x0042 }
            if (r0 != r3) goto L_0x0021
        L_0x001f:
            monitor-exit(r1)     // Catch:{ all -> 0x0042 }
        L_0x0020:
            return
        L_0x0021:
            android.view.Window r0 = r3.mWindow     // Catch:{ all -> 0x0042 }
            android.view.Window$Callback r2 = r3.mCallback     // Catch:{ all -> 0x0042 }
            r0.setCallback(r2)     // Catch:{ all -> 0x0042 }
            r0 = 0
            r3.mWindow = r0     // Catch:{ all -> 0x0042 }
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x0042 }
            android.view.Window r0 = r0.getWindow()     // Catch:{ all -> 0x0042 }
            android.view.Window$Callback r0 = r0.getCallback()     // Catch:{ all -> 0x0042 }
            r3.mCallback = r0     // Catch:{ all -> 0x0042 }
            android.app.Activity r0 = r3.mActivity     // Catch:{ all -> 0x0042 }
            android.view.Window r0 = r0.getWindow()     // Catch:{ all -> 0x0042 }
            r0.setCallback(r3)     // Catch:{ all -> 0x0042 }
            monitor-exit(r1)     // Catch:{ all -> 0x0042 }
            goto L_0x0020
        L_0x0042:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0042 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.mediaplay.playercontrol.MediaKeyBackController.unbindWindow():void");
    }
}
