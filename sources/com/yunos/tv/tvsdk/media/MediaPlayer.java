package com.yunos.tv.tvsdk.media;

import android.content.Context;
import android.util.Log;
import com.yunos.tv.tvsdk.media.adoplayer.AdoMediaPlayer;
import com.yunos.tv.tvsdk.media.mediacodec.MediaCodecPlayer;
import com.yunos.tv.tvsdk.media.systemplayer.SystemMediaPlayer;

public class MediaPlayer {
    public static final int ADO_MEDIA_PALER = 2;
    public static final int MEDIA_CODEC_MEDIA_PLAYER = 3;
    public static final int SYSTEM_MEDIA_PLAYER = 1;

    public static IMediaPlayer create(Context context, int mediaPlayerType) {
        Log.i("MediaPlayer", "MediaPlayer create mediaPlayerType=" + mediaPlayerType);
        switch (mediaPlayerType) {
            case 1:
                return new SystemMediaPlayer();
            case 2:
                return new AdoMediaPlayer(context);
            case 3:
                return new MediaCodecPlayer();
            default:
                return null;
        }
    }
}
