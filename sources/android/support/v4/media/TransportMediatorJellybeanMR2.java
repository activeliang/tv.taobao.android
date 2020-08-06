package android.support.v4.media;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;

@TargetApi(18)
@RequiresApi(18)
class TransportMediatorJellybeanMR2 {
    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            TransportMediatorJellybeanMR2.this.mTransportCallback.handleAudioFocusChange(focusChange);
        }
    };
    boolean mAudioFocused;
    final AudioManager mAudioManager;
    final Context mContext;
    boolean mFocused;
    final RemoteControlClient.OnGetPlaybackPositionListener mGetPlaybackPositionListener = new RemoteControlClient.OnGetPlaybackPositionListener() {
        public long onGetPlaybackPosition() {
            return TransportMediatorJellybeanMR2.this.mTransportCallback.getPlaybackPosition();
        }
    };
    final Intent mIntent;
    final BroadcastReceiver mMediaButtonReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                TransportMediatorJellybeanMR2.this.mTransportCallback.handleKey((KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
            } catch (ClassCastException e) {
                Log.w("TransportController", e);
            }
        }
    };
    PendingIntent mPendingIntent;
    int mPlayState = 0;
    final RemoteControlClient.OnPlaybackPositionUpdateListener mPlaybackPositionUpdateListener = new RemoteControlClient.OnPlaybackPositionUpdateListener() {
        public void onPlaybackPositionUpdate(long newPositionMs) {
            TransportMediatorJellybeanMR2.this.mTransportCallback.playbackPositionUpdate(newPositionMs);
        }
    };
    final String mReceiverAction;
    final IntentFilter mReceiverFilter;
    RemoteControlClient mRemoteControl;
    final View mTargetView;
    final TransportMediatorCallback mTransportCallback;
    final ViewTreeObserver.OnWindowAttachListener mWindowAttachListener = new ViewTreeObserver.OnWindowAttachListener() {
        public void onWindowAttached() {
            TransportMediatorJellybeanMR2.this.windowAttached();
        }

        public void onWindowDetached() {
            TransportMediatorJellybeanMR2.this.windowDetached();
        }
    };
    final ViewTreeObserver.OnWindowFocusChangeListener mWindowFocusListener = new ViewTreeObserver.OnWindowFocusChangeListener() {
        public void onWindowFocusChanged(boolean hasFocus) {
            if (hasFocus) {
                TransportMediatorJellybeanMR2.this.gainFocus();
            } else {
                TransportMediatorJellybeanMR2.this.loseFocus();
            }
        }
    };

    public TransportMediatorJellybeanMR2(Context context, AudioManager audioManager, View view, TransportMediatorCallback transportCallback) {
        this.mContext = context;
        this.mAudioManager = audioManager;
        this.mTargetView = view;
        this.mTransportCallback = transportCallback;
        this.mReceiverAction = context.getPackageName() + ":transport:" + System.identityHashCode(this);
        this.mIntent = new Intent(this.mReceiverAction);
        this.mIntent.setPackage(context.getPackageName());
        this.mReceiverFilter = new IntentFilter();
        this.mReceiverFilter.addAction(this.mReceiverAction);
        this.mTargetView.getViewTreeObserver().addOnWindowAttachListener(this.mWindowAttachListener);
        this.mTargetView.getViewTreeObserver().addOnWindowFocusChangeListener(this.mWindowFocusListener);
    }

    public Object getRemoteControlClient() {
        return this.mRemoteControl;
    }

    public void destroy() {
        windowDetached();
        this.mTargetView.getViewTreeObserver().removeOnWindowAttachListener(this.mWindowAttachListener);
        this.mTargetView.getViewTreeObserver().removeOnWindowFocusChangeListener(this.mWindowFocusListener);
    }

    /* access modifiers changed from: package-private */
    public void windowAttached() {
        this.mContext.registerReceiver(this.mMediaButtonReceiver, this.mReceiverFilter);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, this.mIntent, 268435456);
        this.mRemoteControl = new RemoteControlClient(this.mPendingIntent);
        this.mRemoteControl.setOnGetPlaybackPositionListener(this.mGetPlaybackPositionListener);
        this.mRemoteControl.setPlaybackPositionUpdateListener(this.mPlaybackPositionUpdateListener);
    }

    /* access modifiers changed from: package-private */
    public void gainFocus() {
        if (!this.mFocused) {
            this.mFocused = true;
            this.mAudioManager.registerMediaButtonEventReceiver(this.mPendingIntent);
            this.mAudioManager.registerRemoteControlClient(this.mRemoteControl);
            if (this.mPlayState == 3) {
                takeAudioFocus();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void takeAudioFocus() {
        if (!this.mAudioFocused) {
            this.mAudioFocused = true;
            this.mAudioManager.requestAudioFocus(this.mAudioFocusChangeListener, 3, 1);
        }
    }

    public void startPlaying() {
        if (this.mPlayState != 3) {
            this.mPlayState = 3;
            this.mRemoteControl.setPlaybackState(3);
        }
        if (this.mFocused) {
            takeAudioFocus();
        }
    }

    public void refreshState(boolean playing, long position, int transportControls) {
        if (this.mRemoteControl != null) {
            this.mRemoteControl.setPlaybackState(playing ? 3 : 1, position, playing ? 1.0f : 0.0f);
            this.mRemoteControl.setTransportControlFlags(transportControls);
        }
    }

    public void pausePlaying() {
        if (this.mPlayState == 3) {
            this.mPlayState = 2;
            this.mRemoteControl.setPlaybackState(2);
        }
        dropAudioFocus();
    }

    public void stopPlaying() {
        if (this.mPlayState != 1) {
            this.mPlayState = 1;
            this.mRemoteControl.setPlaybackState(1);
        }
        dropAudioFocus();
    }

    /* access modifiers changed from: package-private */
    public void dropAudioFocus() {
        if (this.mAudioFocused) {
            this.mAudioFocused = false;
            this.mAudioManager.abandonAudioFocus(this.mAudioFocusChangeListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void loseFocus() {
        dropAudioFocus();
        if (this.mFocused) {
            this.mFocused = false;
            this.mAudioManager.unregisterRemoteControlClient(this.mRemoteControl);
            this.mAudioManager.unregisterMediaButtonEventReceiver(this.mPendingIntent);
        }
    }

    /* access modifiers changed from: package-private */
    public void windowDetached() {
        loseFocus();
        if (this.mPendingIntent != null) {
            this.mContext.unregisterReceiver(this.mMediaButtonReceiver);
            this.mPendingIntent.cancel();
            this.mPendingIntent = null;
            this.mRemoteControl = null;
        }
    }
}
