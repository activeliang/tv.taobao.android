package com.taobao.taobaoavsdk.widget.extra;

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
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class KeyBackController implements Window.Callback {
    CopyOnWriteArrayList<OnBackKeyListener> mBackKeyEventList = new CopyOnWriteArrayList<>();
    Window.Callback mCallback;

    public interface OnBackKeyListener {
        boolean onBackKeyDown(KeyEvent keyEvent);
    }

    public KeyBackController(Activity activity) {
        this.mCallback = activity.getWindow().getCallback();
        activity.getWindow().setCallback(this);
    }

    public void registerKeyBackEventListener(OnBackKeyListener backKeyEvent) {
        if (this.mBackKeyEventList == null || !this.mBackKeyEventList.contains(backKeyEvent)) {
            this.mBackKeyEventList.add(0, backKeyEvent);
        }
    }

    public void unregisterKeyBackEventListener(OnBackKeyListener backKeyEvent) {
        if (this.mBackKeyEventList != null) {
            this.mBackKeyEventList.remove(backKeyEvent);
        }
    }

    public void handleKeyBack() {
        KeyEvent event = new KeyEvent(4, 0);
        Iterator<OnBackKeyListener> it = this.mBackKeyEventList.iterator();
        while (it.hasNext()) {
            it.next().onBackKeyDown(event);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4 && event.getAction() == 0) {
            Iterator<OnBackKeyListener> it = this.mBackKeyEventList.iterator();
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
}
