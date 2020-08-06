package com.yunos.tv.blitz.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.SparseArray;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.blitz.account.BzDebugLog;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class AudioPlayerManager {
    static final int E_AUDIO_OPERATION_COMM_PAUSE = 2;
    static final int E_AUDIO_OPERATION_COMM_PLAY = 1;
    static final int E_AUDIO_OPERATION_COMM_PREPARE_ASYNC = 0;
    static final int E_AUDIO_OPERATION_COMM_RELEASE = 5;
    static final int E_AUDIO_OPERATION_COMM_RESUME = 3;
    static final int E_AUDIO_OPERATION_COMM_STOP = 4;
    private static String TAG = AudioPlayerManager.class.getSimpleName();
    private static SparseArray<MediaPlayer> audioplayerList = new SparseArray<>();
    private WeakReference<Context> mContext;

    private native boolean audioNativeInit();

    private native boolean audioNativeRelease();

    private native boolean audioNativeResumeContext();

    private native boolean audioPostEvent(int i, int i2, int i3, int i4);

    public AudioPlayerManager(WeakReference<Context> context) {
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public void audio_player_m_operationCommand(int playerid, int operationComm) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_operationCommand dummy Error,player hashcode is -1");
            return;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_operationCommand player not exist");
            return;
        }
        BzDebugLog.d(TAG, "playerid:" + playerid + "operation:" + operationComm);
        switch (operationComm) {
            case 0:
                try {
                    player.prepare();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            case 1:
                player.start();
                BzDebugLog.d(TAG, "isplaying:" + player.isPlaying());
                return;
            case 2:
                player.pause();
                return;
            case 3:
                if (player.isPlaying()) {
                    BzDebugLog.w(TAG, "E_AUDIO_OPERATION_COMM_RESUME player is in playing state");
                }
                player.start();
                return;
            case 4:
                BzDebugLog.w(TAG, "E_AUDIO_OPERATION_COMM_STOP player is in playing state:" + player.isPlaying());
                player.reset();
                player.stop();
                return;
            case 5:
                player.release();
                audioplayerList.remove(player.hashCode());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public int audio_player_m_createPlayer() {
        MediaPlayer player = new BzAudioPlayer(this);
        audioplayerList.append(player.hashCode(), player);
        if (player.hashCode() == -1) {
            BzDebugLog.e(TAG, "dummy Error,audio_player_m_createPlayer player hashcode is -1");
        }
        return player.hashCode();
    }

    /* access modifiers changed from: package-private */
    public void audio_player_m_setPath(int playerid, String path) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_setPath dummy Error,player hashcode is -1");
            return;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_setPath player not exist");
            return;
        }
        BzDebugLog.d(TAG, "audio_player_m_setPath playerid:" + playerid + "path:" + path);
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            BzDebugLog.e(TAG, "audio_player_m_setPath ioexception occurs");
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public void audio_player_m_setLooping(int playerid, boolean isLoop) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_setLooping dummy Error,player hashcode is -1");
            return;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_setLooping player not exist");
            return;
        }
        BzDebugLog.d(TAG, "audio_player_m_setLooping playerid:" + playerid + "isLoop:" + isLoop);
        player.setLooping(isLoop);
    }

    /* access modifiers changed from: package-private */
    public boolean audio_player_m_isLooping(int playerid) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_isLooping dummy Error,player hashcode is -1");
            return false;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_isLooping player not exist");
            return false;
        }
        BzDebugLog.d(TAG, "audio_player_m_isLooping playerid:" + playerid);
        return player.isLooping();
    }

    /* access modifiers changed from: package-private */
    public double audio_player_m_getCurrPosition(int playerid) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_getCurrPosition dummy Error,player hashcode is -1");
            return ClientTraceData.b.f47a;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_getCurrPosition player not exist");
            return ClientTraceData.b.f47a;
        }
        double position = (double) player.getCurrentPosition();
        BzDebugLog.d(TAG, "audio_player_m_getCurrPosition playerid:" + playerid + "currposition:" + position);
        return position;
    }

    /* access modifiers changed from: package-private */
    public double audio_player_m_getDuration(int playerid) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_getDuration dummy Error,player hashcode is -1");
            return ClientTraceData.b.f47a;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_getDuration player not exist");
            return ClientTraceData.b.f47a;
        }
        double duration = (double) player.getDuration();
        BzDebugLog.d(TAG, "audio_player_m_getDuration playerid:" + playerid + "duration:" + duration);
        return duration;
    }

    /* access modifiers changed from: package-private */
    public boolean audio_player_m_isPlaying(int playerid) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_isPlaying dummy Error,player hashcode is -1");
            return false;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_isPlaying player not exist");
            return false;
        }
        BzDebugLog.d(TAG, "audio_player_m_isPlaying playerid:" + playerid);
        return player.isPlaying();
    }

    /* access modifiers changed from: package-private */
    public void audio_player_m_seekto(int playerid, int ms) {
        if (playerid == -1) {
            BzDebugLog.e(TAG, "audio_player_m_seekto dummy Error,player hashcode is -1");
            return;
        }
        MediaPlayer player = audioplayerList.get(playerid);
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_seekto player not exist");
            return;
        }
        BzDebugLog.d(TAG, "audio_player_m_seekto playerid:" + playerid + "ms:" + ms);
        player.seekTo(ms);
    }

    public void audio_player_m_postEvent(MediaPlayer player, int eventid, int what, int extra) {
        if (player == null) {
            BzDebugLog.e(TAG, "audio_player_m_postEvent dummy Error,player not exist");
            return;
        }
        int playerid = audioplayerList.indexOfValue(player);
        if (playerid < 0) {
            BzDebugLog.e(TAG, "audio_player_m_postEvent playerid invalid");
        } else {
            audioPostEvent(playerid, eventid, what, extra);
        }
    }

    private void traverseAudioPlayer() {
        for (int i = 0; i < audioplayerList.size(); i++) {
            BzDebugLog.d(TAG, "player hashcode:" + audioplayerList.keyAt(i));
        }
    }

    public void onContextResume() {
        traverseAudioPlayer();
        audioNativeResumeContext();
    }

    public void onContextDestroy() {
        for (int i = 0; i < audioplayerList.size(); i++) {
            int key = audioplayerList.keyAt(i);
            BzDebugLog.d(TAG, "onContextDestroy player hashcode:" + key);
            audioplayerList.remove(key);
            audioplayerList.get(key).release();
            BzDebugLog.d(TAG, "onContextDestroy released player hashcode:" + key);
        }
        audioNativeRelease();
    }

    public void audioPlayerManagerInit() {
        audioNativeInit();
    }
}
