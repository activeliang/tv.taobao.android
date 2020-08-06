package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import java.util.ArrayList;

@Deprecated
public class TransportMediator extends TransportController {
    @Deprecated
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    @Deprecated
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    @Deprecated
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    @Deprecated
    public static final int KEYCODE_MEDIA_PLAY = 126;
    @Deprecated
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final KeyEvent.Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;

    static boolean isMediaKey(int keyCode) {
        switch (keyCode) {
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 130:
                return true;
            default:
                return false;
        }
    }

    @Deprecated
    public TransportMediator(Activity activity, TransportPerformer callbacks) {
        this(activity, (View) null, callbacks);
    }

    @Deprecated
    public TransportMediator(View view, TransportPerformer callbacks) {
        this((Activity) null, view, callbacks);
    }

    private TransportMediator(Activity activity, View view, TransportPerformer callbacks) {
        this.mListeners = new ArrayList<>();
        this.mTransportKeyCallback = new TransportMediatorCallback() {
            public void handleKey(KeyEvent key) {
                key.dispatch(TransportMediator.this.mKeyEventCallback);
            }

            public void handleAudioFocusChange(int focusChange) {
                TransportMediator.this.mCallbacks.onAudioFocusChange(focusChange);
            }

            public long getPlaybackPosition() {
                return TransportMediator.this.mCallbacks.onGetCurrentPosition();
            }

            public void playbackPositionUpdate(long newPositionMs) {
                TransportMediator.this.mCallbacks.onSeekTo(newPositionMs);
            }
        };
        this.mKeyEventCallback = new KeyEvent.Callback() {
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (TransportMediator.isMediaKey(keyCode)) {
                    return TransportMediator.this.mCallbacks.onMediaButtonDown(keyCode, event);
                }
                return false;
            }

            public boolean onKeyLongPress(int keyCode, KeyEvent event) {
                return false;
            }

            public boolean onKeyUp(int keyCode, KeyEvent event) {
                if (TransportMediator.isMediaKey(keyCode)) {
                    return TransportMediator.this.mCallbacks.onMediaButtonUp(keyCode, event);
                }
                return false;
            }

            public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
                return false;
            }
        };
        this.mContext = activity != null ? activity : view.getContext();
        this.mCallbacks = callbacks;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mView = activity != null ? activity.getWindow().getDecorView() : view;
        this.mDispatcherState = this.mView.getKeyDispatcherState();
        if (Build.VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
        } else {
            this.mController = null;
        }
    }

    @Deprecated
    public Object getRemoteControlClient() {
        if (this.mController != null) {
            return this.mController.getRemoteControlClient();
        }
        return null;
    }

    @Deprecated
    public boolean dispatchKeyEvent(KeyEvent event) {
        return event.dispatch(this.mKeyEventCallback, (KeyEvent.DispatcherState) this.mDispatcherState, this);
    }

    @Deprecated
    public void registerStateListener(TransportStateListener listener) {
        this.mListeners.add(listener);
    }

    @Deprecated
    public void unregisterStateListener(TransportStateListener listener) {
        this.mListeners.remove(listener);
    }

    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener[] listeners = new TransportStateListener[this.mListeners.size()];
        this.mListeners.toArray(listeners);
        return listeners;
    }

    private void reportPlayingChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener listener : listeners) {
                listener.onPlayingChanged(this);
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            for (TransportStateListener listener : listeners) {
                listener.onTransportControlsChanged(this);
            }
        }
    }

    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }

    @Deprecated
    public void refreshState() {
        pushControllerState();
        reportPlayingChanged();
        reportTransportControlsChanged();
    }

    @Deprecated
    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        pushControllerState();
        reportPlayingChanged();
    }

    @Deprecated
    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        pushControllerState();
        reportPlayingChanged();
    }

    @Deprecated
    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        pushControllerState();
        reportPlayingChanged();
    }

    @Deprecated
    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }

    @Deprecated
    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }

    @Deprecated
    public void seekTo(long pos) {
        this.mCallbacks.onSeekTo(pos);
    }

    @Deprecated
    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }

    @Deprecated
    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }

    @Deprecated
    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }

    @Deprecated
    public void destroy() {
        this.mController.destroy();
    }
}
