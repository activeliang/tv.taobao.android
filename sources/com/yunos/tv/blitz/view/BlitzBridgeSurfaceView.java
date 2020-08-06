package com.yunos.tv.blitz.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.yunos.tv.blitz.GLEnvironment;
import com.yunos.tv.blitz.global.BzAppConfig;
import java.util.concurrent.TimeUnit;

public class BlitzBridgeSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static int STATE_ALLOCATED = 0;
    private static int STATE_CREATED = 1;
    private static int STATE_INITIALIZED = 2;
    static String TAG = BlitzBridgeSurfaceView.class.getSimpleName();
    private static Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public static ExtractedText sExtractedText = null;
    private Context mContext;
    private int mFormat;
    /* access modifiers changed from: private */
    public GLEnvironment mGLEnv;
    private int mHeight;
    private SurfaceHolder.Callback mListener;
    private int mState = STATE_ALLOCATED;
    private int mSurfaceId = -1;
    private int mWidth;

    public BlitzBridgeSurfaceView(Context context) {
        super(context);
        this.mContext = context;
        getHolder().addCallback(this);
    }

    public BlitzBridgeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getHolder().addCallback(this);
    }

    public synchronized void bindToListener(SurfaceHolder.Callback listener, GLEnvironment glEnv) {
        if (listener == null) {
            throw new NullPointerException("Attempting to bind null filter to SurfaceView!");
        } else if (this.mListener == null || this.mListener == listener) {
            this.mListener = listener;
            if (!(this.mGLEnv == null || this.mGLEnv == glEnv)) {
                this.mGLEnv.unregisterSurfaceId(this.mSurfaceId);
            }
            this.mGLEnv = glEnv;
            if (this.mState >= STATE_CREATED) {
                registerSurface();
                this.mListener.surfaceCreated(getHolder());
                if (this.mState == STATE_INITIALIZED) {
                    this.mListener.surfaceChanged(getHolder(), this.mFormat, this.mWidth, this.mHeight);
                }
            }
        } else {
            throw new RuntimeException("Attempting to bind filter " + listener + " to SurfaceView with another open filter " + this.mListener + " attached already!");
        }
    }

    public synchronized void unbind() {
        this.mListener = null;
    }

    public synchronized int getSurfaceId() {
        return this.mSurfaceId;
    }

    public synchronized GLEnvironment getGLEnv() {
        return this.mGLEnv;
    }

    public synchronized void surfaceCreated(SurfaceHolder holder) {
        this.mState = STATE_CREATED;
        if (this.mGLEnv != null) {
            registerSurface();
        }
        if (this.mListener != null) {
            this.mListener.surfaceCreated(holder);
        }
    }

    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mFormat = format;
        this.mWidth = width;
        this.mHeight = height;
        this.mState = STATE_INITIALIZED;
        Log.d(TAG, "surfacechanage width:" + this.mWidth + "height:" + this.mHeight);
        if (this.mListener != null) {
            this.mListener.surfaceChanged(holder, format, width, height);
        }
    }

    public synchronized void surfaceDestroyed(SurfaceHolder holder) {
        this.mState = STATE_ALLOCATED;
        if (this.mListener != null) {
            this.mListener.surfaceDestroyed(holder);
        }
        unregisterSurface();
    }

    private void registerSurface() {
        this.mSurfaceId = this.mGLEnv.registerSurface(getHolder().getSurface());
        if (this.mSurfaceId < 0) {
            throw new RuntimeException("Could not register Surface: " + getHolder().getSurface() + " in FilterSurfaceView!");
        }
    }

    private void unregisterSurface() {
        if (this.mGLEnv != null && this.mSurfaceId > 0) {
            this.mGLEnv.unregisterSurfaceId(this.mSurfaceId);
        }
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
            showInputMethod();
            TimeUnit.SECONDS.sleep(6);
            hideInputMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class BlitzInputConnection extends BaseInputConnection {
        public BlitzInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        public boolean commitText(CharSequence text, int newCursorPosition) {
            Log.d(BlitzBridgeSurfaceView.TAG, "commitText, text size: " + text.length());
            BlitzBridgeSurfaceView.this.mGLEnv.CommitStringForIME(text.toString());
            return true;
        }

        public boolean sendKeyEvent(KeyEvent event) {
            Log.d(BlitzBridgeSurfaceView.TAG, "event.getKeyCode() = " + event.getKeyCode());
            if (event.getAction() == 1) {
                BlitzBridgeSurfaceView.this.mGLEnv.SendKeyEventForIME(Integer.toString(event.getKeyCode(), 10));
            }
            return super.sendKeyEvent(event);
        }

        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            Log.d(BlitzBridgeSurfaceView.TAG, "deleteSurroundingText beforeLength:" + beforeLength + ", afterLength:" + afterLength);
            BlitzBridgeSurfaceView.this.mGLEnv.SendKeyEventForIME(Integer.toString(65288, 10));
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

        public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
            if (BlitzBridgeSurfaceView.sExtractedText == null) {
                ExtractedText unused = BlitzBridgeSurfaceView.sExtractedText = new ExtractedText();
                BlitzBridgeSurfaceView.sExtractedText.text = "";
            }
            return BlitzBridgeSurfaceView.sExtractedText;
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = 1;
        outAttrs.imeOptions = 301989888;
        return new BlitzInputConnection(this, false);
    }

    public void showInputMethod() {
        mHandler.post(new Runnable() {
            public void run() {
                BlitzBridgeSurfaceView.this.showInputMethodInMainThread();
            }
        });
    }

    public void showInputMethodInMainThread() {
        if (!BzAppConfig.context.getSoftKeyboardStatus()) {
            setFocusable(true);
            requestFocus();
            ((InputMethodManager) this.mContext.getSystemService("input_method")).showSoftInput(this, 0);
        }
    }

    public void hideInputMethod() {
        if (BzAppConfig.context.getSoftKeyboardStatus()) {
            ((InputMethodManager) this.mContext.getSystemService("input_method")).toggleSoftInput(0, 2);
        }
    }

    public void notifyImeShowStatus(boolean opened) {
        this.mGLEnv.notifyImeShowStatus(opened);
    }
}
