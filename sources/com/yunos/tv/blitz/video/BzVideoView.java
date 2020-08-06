package com.yunos.tv.blitz.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.intertrust.wasabi.media.PlaylistProxy;
import com.yunos.tv.blitz.video.data.ChannelVideoInfo;
import com.yunos.tv.blitz.video.data.LDebug;
import com.yunos.tv.blitz.video.data.MTopVideoInfo;
import com.yunos.tv.blitz.video.data.MtopReponse;
import com.yunos.tv.blitz.video.data.VideoItem;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tv.tvsdk.media.view.YunosVideoView;
import org.json.JSONException;
import org.json.JSONObject;
import yunos.media.drm.DrmManagerCreator;
import yunos.media.drm.interfc.DrmManager;

public class BzVideoView extends YunosVideoView {
    public static int EVENT_EXIT_FULLSCREEN = 10;
    public static int EVENT_FULLSCREEN = 9;
    public static int EVENT_SEEK_COMPLETE = 7;
    public static int EVENT_VOLUME_CHANGED = 8;
    public static final int STATE_STALLED = 14;
    public static final int VIDEO_TYPE_DIANBO = 2;
    public static final int VIDEO_TYPE_SOHU = 3;
    public static final int VIDEO_TYPE_ZHIBO = 1;
    public static PowerManager.WakeLock mWakeLock;
    private final String TAG = "BzVideoView";
    private DrmManager mDrmManager;
    private byte[] mId;
    private Object[] mLastParams;
    /* access modifiers changed from: private */
    public int mLastPosition = 0;

    public BzVideoView(Context context) {
        super(context);
    }

    public BzVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BzVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setWebViewId(byte[] id) {
        this.mId = id;
    }

    public byte[] getWebViewId() {
        return this.mId;
    }

    public void pause() {
        if (isAdPlaying()) {
            LDebug.d("BzVideoView", "invalid pause! ad is playing");
            return;
        }
        LDebug.i("BzVideoView", "pause state:" + getCurrentState());
        if (isPlaying()) {
            this.mLastPosition = getCurrentPosition();
            LDebug.i("BzVideoView", "BzVideoView.pause mLastPosition=" + this.mLastPosition);
        }
        super.pause();
    }

    public void customError(int errorCode) {
        customError(errorCode, 0);
        setVideoViewBg();
    }

    public void play(VideoItem item) {
        LDebug.i("BzVideoView", "BzVideoView.play item=" + item);
        if ((item == null || item.getId() == null) && item.getVideo() == null) {
            this.mLastPosition = 0;
        } else {
            item.setStart(this.mLastPosition);
        }
        this.mLastParams = null;
        if (item == null || TextUtils.isEmpty(item.getVideo())) {
            LDebug.w("BzVideoView", "BzVideoView.play item=" + item);
            return;
        }
        String video = item.getVideo();
        switch (item.getType()) {
            case 1:
                LDebug.d("BzVideoView", "BzVideoView.play zhibo");
                if (getVideoViewType() != 0) {
                    setVideoViewType(0);
                }
                if (!video.startsWith("{") || !video.endsWith("}")) {
                    setVideoInfo(video);
                    return;
                }
                MtopReponse reponse = MtopReponse.fromJson(video);
                if (reponse == null) {
                    LDebug.w("BzVideoView", "BzVideoView.play json reponse is null ");
                    return;
                }
                JSONObject obj = reponse.getResult();
                if (obj == null) {
                    LDebug.w("BzVideoView", "BzVideoView.play json reponse is null ");
                    return;
                }
                ChannelVideoInfo info = ChannelVideoInfo.fromJson(obj);
                if (TextUtils.isEmpty(info.getHttpUrl())) {
                }
                setVideoInfo(info.getHttpUrl());
                return;
            case 2:
                LDebug.d("BzVideoView", "BzVideoView.play dianbo");
                if (getVideoViewType() != 0) {
                    setVideoViewType(0);
                }
                if (video.startsWith("{") && video.endsWith("}")) {
                    LDebug.i("BzVideoView", "BzVideoView.play json");
                    MtopReponse reponse2 = MtopReponse.fromJson(video);
                    if (reponse2 == null) {
                        LDebug.w("BzVideoView", "BzVideoView.play json reponse is null ");
                        return;
                    }
                    JSONObject obj2 = reponse2.getResult();
                    if (obj2 == null) {
                        LDebug.w("BzVideoView", "BzVideoView.play json info is null ");
                        return;
                    }
                    MTopVideoInfo info2 = MTopVideoInfo.fromJson(obj2);
                    if (!TextUtils.isEmpty(info2.drmToken)) {
                        drmVideoPlay(info2.getVideoUrl(), info2.drmToken);
                    } else if (getMediaPlayerType() == 1) {
                        String url = info2.getVideoUrl();
                        if (TextUtils.isEmpty(url)) {
                            LDebug.w("BzVideoView", "BzVideoView.play SYSTEM_MEDIA_PLAYER json error video=" + video);
                            return;
                        } else if (!TextUtils.isEmpty(item.getM3u8_data())) {
                            setVideoInfo(url, Integer.valueOf(item.getStart()), item.getM3u8_data());
                        } else if (item.getStart() > 0) {
                            setVideoInfo(url, Integer.valueOf(item.getStart()));
                        } else {
                            setVideoInfo(url);
                        }
                    } else if (getMediaPlayerType() == 2) {
                        if (TextUtils.isEmpty(info2.hlsContent)) {
                            LDebug.w("BzVideoView", "BzVideoView.play ADO_MEDIA_PALER json error ");
                            return;
                        } else {
                            setVideoInfo(Uri.parse("m3u8://string.m3u8"), Integer.valueOf(item.getStart()), info2.hlsContent);
                        }
                    }
                } else if (video.startsWith("#EXTM3U") || video.indexOf("#EXT-X-STREAM-INF") != -1) {
                    LDebug.i("BzVideoView", "BzVideoView.play m3u8");
                    if (getMediaPlayerType() != 2) {
                        setMediaPlayerType(2);
                    }
                    LDebug.i("BzVideoView", "BzVideoView.play m3u8 video=" + video);
                    setVideoInfo(Uri.parse("m3u8://string.m3u8"), Integer.valueOf(item.getStart()), video);
                } else {
                    LDebug.i("BzVideoView", "BzVideoView.play http");
                    if (!TextUtils.isEmpty(item.getM3u8_data())) {
                        setVideoInfo(video, Integer.valueOf(item.getStart()), item.getM3u8_data());
                    } else if (item.getStart() > 0) {
                        setVideoInfo(video, Integer.valueOf(item.getStart()));
                    } else {
                        setVideoInfo(video);
                    }
                }
                LDebug.i("BzVideoView", "BzVideoView.play to start");
                start();
                return;
            case 3:
                LDebug.d("BzVideoView", "BzVideoView.play sohu");
                if (!item.isUseFul()) {
                }
                if (getVideoViewType() != 1) {
                    setVideoViewType(1);
                }
                if (!TextUtils.isEmpty(item.getVideo())) {
                    setVideoInfo(item.getVideo());
                    return;
                } else if (!TextUtils.isEmpty(item.getId())) {
                    setVideoInfo(item.getId());
                    return;
                } else {
                    return;
                }
            default:
                LDebug.e("BzVideoView", "BzVideoView.play other type http");
                JSONObject object = new JSONObject();
                try {
                    object.put(HuasuVideo.TAG_URI, video);
                    object.put(HuasuVideo.TAG_M3U8, item.getM3u8_data());
                    object.put(HuasuVideo.TAG_STARTTIME, item.getStart());
                    object.put(HuasuVideo.TAG_VID, item.getId());
                    object.put("name", item.getTitle());
                    super.setVideoInfo(object.toString());
                } catch (JSONException e) {
                }
                start();
                return;
        }
    }

    public void setVideoInfo(Object... params) {
        if (params != null && params.length > 0) {
            LDebug.i("BzVideoView", "BzVideoView.setVideoInfo params=" + params[0]);
        }
        this.mLastParams = params;
        if (params != null) {
            super.setVideoInfo(params);
        }
    }

    public void stopPlayback() {
        if (isPlaying()) {
            this.mLastPosition = getCurrentPosition();
            LDebug.i("BzVideoView", "BzVideoView.stopPlayback mLastPosition=" + this.mLastPosition);
        }
        releaseDrm();
        super.stopPlayback();
    }

    public void stop() {
        LDebug.i("BzVideoView", "BzVideoView.stop ");
        stopPlayback();
    }

    public void rePlay() {
        rePlay(false);
    }

    public void rePlay(boolean seek) {
        LDebug.i("BzVideoView", "BzVideoView.rePlay seek=" + seek + ", mLastPosition=" + this.mLastPosition);
        if (!isPause()) {
            setVideoInfo(this.mLastParams);
            if (seek) {
                seekTo(this.mLastPosition);
            }
        }
        start();
    }

    @SuppressLint({"Wakelock"})
    public void setCurrentState(int state) {
        super.setCurrentState(state);
        LDebug.i("BzVideoView", "BzVideoView.setCurrentState state=" + state);
        if (state == 0 || state == -1) {
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWakeLock = null;
            LDebug.i("BzVideoView", "BzVideoView.setCurrentState mWakeLock unlock");
        } else {
            if (mWakeLock == null) {
                mWakeLock = ((PowerManager) getContext().getSystemService("power")).newWakeLock(805306394, getClass().getSimpleName());
            }
            if (!mWakeLock.isHeld()) {
                mWakeLock.acquire();
            }
            LDebug.i("BzVideoView", "BzVideoView.setCurrentState mWakeLock lock");
        }
        switch (state) {
            case 1:
            case 2:
            case 3:
            case 6:
                clearVideoViewBg();
                return;
            default:
                return;
        }
    }

    public void fullScreen() {
        super.fullScreen();
        requestFocus();
        LDebug.i("BzVideoView", "BzVideoView.fullScreen end ");
    }

    public void setLastPosition(int position) {
        this.mLastPosition = position;
    }

    private void drmVideoPlay(String url, String drmToken) {
        try {
            this.mDrmManager = new DrmManagerCreator(url, getContext()).createDrmManager();
            this.mDrmManager.setOnDrmErrorListener(new DrmManager.DrmErrorListener() {
                public void onErrorListener(DrmManager arg0, int arg1, int arg2, Object arg3) {
                }
            });
            this.mDrmManager.makeUrl(drmToken, PlaylistProxy.MediaSourceType.HLS, new PlaylistProxy.MediaSourceParams(), new DrmManager.ICallBack() {
                public void onComplete(Uri uri, int errorCode) {
                    if (uri == null || uri.toString().length() <= 0) {
                        LDebug.w("BzVideoView", "drmManager Exception:errorCode=" + errorCode);
                    } else if (BzVideoView.this.getMediaPlayerType() == 2) {
                        BzVideoView.this.setVideoInfo(uri, Integer.valueOf(BzVideoView.this.mLastPosition));
                    } else {
                        BzVideoView.this.setVideoInfo(uri);
                        BzVideoView.this.seekTo(BzVideoView.this.mLastPosition);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            sendTryMessage();
            LDebug.w("BzVideoView", "drmManager Exception");
        }
    }

    public void sendTryMessage() {
    }

    public void releaseDrm() {
        try {
            if (this.mDrmManager != null) {
                this.mDrmManager.shutDown();
            }
        } catch (Exception e) {
        }
    }

    public void onCompletion(Object mp) {
        super.onCompletion(mp);
        this.mLastPosition = 0;
    }
}
