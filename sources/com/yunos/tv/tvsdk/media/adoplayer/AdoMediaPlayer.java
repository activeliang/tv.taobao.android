package com.yunos.tv.tvsdk.media.adoplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.view.InputDeviceCompat;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.media.MediaConstant;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import java.io.IOException;
import yunos.media.AdoPlayer;

public class AdoMediaPlayer extends AdoPlayer implements IMediaPlayer {
    private static final int KEY_PARAMETER_HTTPDNS = 1508;
    private static final int KEY_PARAMETER_NETADAPTION = 1509;

    public AdoMediaPlayer(Context context) {
        super(context);
    }

    public void setOnPreparedListener(final IMediaPlayer.OnPreparedListener listener) {
        AdoMediaPlayer.super.setOnPreparedListener(new AdoPlayer.OnPreparedListener() {
            public void onPrepared(AdoPlayer mp) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onPrepared((IMediaPlayer) mp);
                }
            }
        });
    }

    public void setOnCompletionListener(final IMediaPlayer.OnCompletionListener listener) {
        AdoMediaPlayer.super.setOnCompletionListener(new AdoPlayer.OnCompletionListener() {
            public void onCompletion(AdoPlayer mp) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onCompletion((IMediaPlayer) mp);
                }
            }
        });
    }

    public void setOnBufferingUpdateListener(final IMediaPlayer.OnBufferingUpdateListener listener) {
        AdoMediaPlayer.super.setOnBufferingUpdateListener(new AdoPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(AdoPlayer mp, int percent) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onBufferingUpdate((IMediaPlayer) mp, percent);
                }
            }
        });
    }

    public void setOnSeekCompleteListener(final IMediaPlayer.OnSeekCompleteListener listener) {
        setOnSeekCompleteListener(new AdoPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(AdoPlayer mp) {
                if (listener != null) {
                    listener.onSeekComplete();
                }
            }
        });
    }

    public void setOnVideoSizeChangedListener(final IMediaPlayer.OnVideoSizeChangedListener listener) {
        setOnVideoSizeChangedListener(new AdoPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(AdoPlayer mp, int width, int height) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onVideoSizeChanged((IMediaPlayer) mp, width, height);
                }
            }
        });
    }

    public void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener) {
    }

    public void setOnErrorListener(final IMediaPlayer.OnErrorListener listener) {
        AdoMediaPlayer.super.setOnErrorListener(new AdoPlayer.OnErrorListener() {
            public boolean onError(AdoPlayer mp, int what, int extra) {
                if (listener == null || !(mp instanceof IMediaPlayer)) {
                    return false;
                }
                return listener.onError((IMediaPlayer) mp, what, extra);
            }
        });
    }

    public void setOnInfoListener(final IMediaPlayer.OnInfoListener listener) {
        AdoMediaPlayer.super.setOnInfoListener(new AdoPlayer.OnInfoListener() {
            public boolean onInfo(AdoPlayer mp, int what, int extra) {
                if (listener == null || !(mp instanceof IMediaPlayer)) {
                    return false;
                }
                return listener.onInfo((IMediaPlayer) mp, what, extra);
            }
        });
    }

    public void setOnInfoExtendListener(final IMediaPlayer.OnInfoExtendListener listener) {
        AdoMediaPlayer.super.setOnInfoExtendListener(new AdoPlayer.OnInfoExtendListener() {
            public boolean onInfoExtend(AdoPlayer mp, int arg1, int arg2, Object arg3) {
                if (listener == null || !(mp instanceof IMediaPlayer)) {
                    return false;
                }
                return listener.onInfoExtend((IMediaPlayer) mp, arg1, arg2, arg3);
            }
        });
    }

    public long getSourceBitrate() {
        Parcel parBitRate = getParcelParameter(1500);
        if (parBitRate == null) {
            return -1;
        }
        parBitRate.setDataPosition(0);
        return parBitRate.readLong();
    }

    public String getNetSourceURL() {
        Parcel parURL = getParcelParameter(1501);
        if (parURL == null) {
            return null;
        }
        parURL.setDataPosition(0);
        return parURL.readString();
    }

    public void prepare() throws IOException, IllegalStateException {
    }

    public String getHttpHeader() {
        Parcel parcel = getParcelParameter(1507);
        if (parcel == null) {
            return null;
        }
        parcel.setDataPosition(0);
        return parcel.readString();
    }

    public void setHttpDNS(String httpdns) {
        try {
            if (!TextUtils.isEmpty(httpdns)) {
                Bundle bundle = new Bundle();
                bundle.putString("httpdns", httpdns);
                setBundleParameter(KEY_PARAMETER_HTTPDNS, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setPlayerParameter(int key, Parcel params) {
        return setParameter(key, params);
    }

    public void setNetadaption(String netadaption) {
        try {
            if (!TextUtils.isEmpty(netadaption)) {
                Log.d("AdoMediaPlayer", "setNetadaption " + netadaption);
                Bundle bundle = new Bundle();
                bundle.putString(netadaption, netadaption);
                setBundleParameter(KEY_PARAMETER_NETADAPTION, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCodecInfo() {
        try {
            Bundle bundle = getBundleParameter(InputDeviceCompat.SOURCE_TRACKBALL);
            if (bundle != null) {
                String codecInfo = bundle.getString("video_codec_info");
                if (!TextUtils.isEmpty(codecInfo)) {
                    return codecInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MediaConstant.H264;
    }
}
