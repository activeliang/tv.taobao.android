package com.yunos.tv.blitz.audio;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.TimedText;
import com.yunos.tv.blitz.account.BzDebugLog;

@TargetApi(16)
public class BzAudioPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnTimedTextListener {
    static final int AudioEventCanplay = 0;
    static final int AudioEventEnded = 6;
    static final int AudioEventError = 5;
    static final int AudioEventLoadStart = 8;
    static final int AudioEventPause = 2;
    static final int AudioEventPlay = 1;
    static final int AudioEventSeeked = 4;
    static final int AudioEventSeeking = 3;
    static final int AudioEventTimeUpdate = 7;
    final String TAG = BzAudioPlayer.class.getSimpleName();
    AudioPlayerManager mAudioPlayerManager = null;

    public BzAudioPlayer(AudioPlayerManager audioplayerManager) {
        this.mAudioPlayerManager = audioplayerManager;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        BzDebugLog.e(this.TAG, "onBufferingUpdate mp:" + mediaPlayer + "code1:" + i);
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        if (this.mAudioPlayerManager == null) {
            BzDebugLog.e(this.TAG, "onCompletion audiomanager is null");
        } else {
            this.mAudioPlayerManager.audio_player_m_postEvent(mediaPlayer, 6, 0, 0);
        }
    }

    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        BzDebugLog.e(this.TAG, "onerror mp:" + mediaPlayer + "what:" + what + "extra:" + extra);
        if (this.mAudioPlayerManager == null) {
            BzDebugLog.e(this.TAG, "onCompletion audiomanager is null");
        } else {
            this.mAudioPlayerManager.audio_player_m_postEvent(mediaPlayer, 5, what, extra);
        }
        return false;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        BzDebugLog.w(this.TAG, "onInfo code1" + i + "code2:" + i1);
        return false;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        if (this.mAudioPlayerManager == null) {
            BzDebugLog.e(this.TAG, "onPrepared audiomanager is null");
        } else {
            this.mAudioPlayerManager.audio_player_m_postEvent(mediaPlayer, 0, 0, 0);
        }
    }

    public void onSeekComplete(MediaPlayer mediaPlayer) {
        if (this.mAudioPlayerManager == null) {
            BzDebugLog.e(this.TAG, "onSeekComplete audiomanager is null");
        } else {
            this.mAudioPlayerManager.audio_player_m_postEvent(mediaPlayer, 4, 0, 0);
        }
    }

    public void onTimedText(MediaPlayer mediaPlayer, TimedText timedText) {
    }
}
