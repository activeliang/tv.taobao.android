package com.yunos.tv.tvsdk.media.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.yunos.adoplayer.aidl.InfoExtend;
import com.yunos.tv.blitz.utils.NetworkUtil;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import com.yunos.tv.tvsdk.media.MediaPlayer;
import com.yunos.tv.tvsdk.media.MediaPlayerRender;
import com.yunos.tv.tvsdk.media.adoplayer.AdoMediaPlayer;
import com.yunos.tv.tvsdk.media.data.IPlaybackInfo;
import com.yunos.tv.tvsdk.media.data.MTopInfoBase;
import com.yunos.tv.tvsdk.media.data.MTopTaoTvInfo;
import com.yunos.tv.tvsdk.media.view.IBaseVideo;
import com.yunos.tv.tvsdk.media.view.IVideo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class VideoView extends SurfaceView implements IVideo {
    private static final boolean BYPASS_METADATA_FILTER = false;
    private static final boolean DEBUG = false;
    public static final String HEADER_DATASOURCE_M3U8 = "m3u8_data";
    public static final String HEADER_DATASOURCE_START_TIME_KEY = "datasource_start_time_ms";
    public static final String HEADER_DATASOURCE_VIDEO_ID = "video-id";
    public static final String HEADER_DATASOURCE_VIDEO_NAME = "video-name";
    private static final int MEDIA_RETRY_TIME = 100;
    private static final int MEDIA_RETRY_TIMEOUT = 30000;
    private static final boolean METADATA_ALL = false;
    public static final int PAUSE_AVAILABLE = 1;
    public static final int SEEK_AVAILABLE = 4;
    public static final int SEEK_BACKWARD_AVAILABLE = 2;
    public static final int SEEK_FORWARD_AVAILABLE = 3;
    public static final int SURFACE_STATE_CHANGED = 1;
    public static final int SURFACE_STATE_CREATED = 0;
    public static final int SURFACE_STATE_DESTROYED = 2;
    public static final int SURFACE_STATE_UNINIT = -1;
    private static final String TAG = "VideoView";
    /* access modifiers changed from: private */
    public boolean isStretch = false;
    /* access modifiers changed from: private */
    public int mAudioType = -1;
    /* access modifiers changed from: private */
    public long mBitRate;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(Object mp, int percent) {
            if (VideoView.this.mOnBufferingUpdateListener != null) {
                VideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mCanPause;
    /* access modifiers changed from: private */
    public boolean mCanSeekBack;
    /* access modifiers changed from: private */
    public boolean mCanSeekForward;
    private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        public void onCompletion(Object mp) {
            Log.d(VideoView.TAG, "onCompletion");
            VideoView.this.setCurrentState(5);
            int unused = VideoView.this.mTargetState = 5;
            if (VideoView.this.mOnCompletionListener != null) {
                VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mCurrentState = 0;
    /* access modifiers changed from: private */
    public int mCustomHeight = -1;
    /* access modifiers changed from: private */
    public int mCustomWidth = -1;
    private int mDimenMode = 0;
    /* access modifiers changed from: private */
    public int mErrorCode = 0;
    /* access modifiers changed from: private */
    public int mErrorExtend = 0;
    /* access modifiers changed from: private */
    public InfoExtend mErrorInfoExtend;
    private IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() {
        public boolean onError(Object mp, int framework_err, int impl_err) {
            Log.w(VideoView.TAG, "Error: " + framework_err + "," + impl_err + " mTrayAgainCount=" + VideoView.this.mTrayAgainCount + ",url:" + VideoView.this.mUri + ",subError:" + (impl_err >> 8) + ",errorType:" + (impl_err & 255));
            if (VideoView.this.mMediaPlayerRender != null) {
                VideoView.this.mMediaPlayerRender.stop();
                VideoView.this.mMediaPlayerRender.setInitColor(0.0f, 0.0f, 0.0f, 1.0f);
            }
            int unused = VideoView.this.mErrorCode = framework_err;
            if (VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) {
                VideoView.this.setCurrentState(-1);
                int unused2 = VideoView.this.mTargetState = -1;
                if (-1004 == framework_err) {
                    VideoView.this.stopPlayback();
                }
            }
            return true;
        }
    };
    private Map<String, String> mHeaders;
    private boolean mIgnoreDestroy = false;
    private IMediaPlayer.OnInfoExtendListener mInfoExtendListener = new IMediaPlayer.OnInfoExtendListener() {
        public boolean onInfoExtend(Object arg0, int what, int extra, Object obj) {
            if (304 == what) {
                if (obj != null && (obj instanceof InfoExtend)) {
                    InfoExtend info = (InfoExtend) obj;
                    if (402 == extra) {
                        long unused = VideoView.this.mRadio = info.getCurrentDownRatio();
                        long unused2 = VideoView.this.mBitRate = info.getCurrentVideoBitRate();
                        int unused3 = VideoView.this.mProgressPercent = info.getProgressPrecent();
                    } else if (414 == extra) {
                        if (VideoView.this.mOnVideoRequestTsListener != null) {
                            VideoView.this.mOnVideoRequestTsListener.onRequestTs(info);
                        }
                    } else if (412 == extra && info.getAbnormalMainCode() == 100 && VideoView.this.mOnVideoHttpDnsListener != null) {
                        VideoView.this.mOnVideoHttpDnsListener.onHttpDns((long) info.getAbnormalSubCode());
                    }
                }
            } else if (301 == what) {
                if (411 == extra || 407 == extra) {
                    int unused4 = VideoView.this.mErrorExtend = extra;
                    if (411 == extra && (obj instanceof InfoExtend)) {
                        InfoExtend unused5 = VideoView.this.mErrorInfoExtend = (InfoExtend) obj;
                        Log.d(VideoView.TAG, "netStatus:" + VideoView.this.mErrorInfoExtend.getNetServerStatus());
                    }
                }
            } else if (306 == what) {
                if (VideoView.this.mOnFirstFrameListener != null) {
                    VideoView.this.mOnFirstFrameListener.onFirstFrame();
                }
            } else if (302 == what && (obj instanceof InfoExtend)) {
                InfoExtend info2 = (InfoExtend) obj;
                if (info2.isDoblyAudio()) {
                    int unused6 = VideoView.this.mAudioType = 3;
                } else if (info2.isDolbyPlusAudio()) {
                    int unused7 = VideoView.this.mAudioType = 4;
                } else if (info2.isDtsAudio()) {
                    int unused8 = VideoView.this.mAudioType = 0;
                }
                Log.d(VideoView.TAG, "mAudioType:" + VideoView.this.mAudioType);
                if (VideoView.this.mOnAudioInfoListener != null) {
                    VideoView.this.mOnAudioInfoListener.onAudioInfo(VideoView.this.mAudioType);
                }
            }
            if (VideoView.this.mOnInfoExtendListener != null) {
                return VideoView.this.mOnInfoExtendListener.onInfoExtend(arg0, what, extra, obj);
            }
            return false;
        }
    };
    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {
        public boolean onInfo(Object mp, int what, int extra) {
            if (VideoView.this.mOnInfoListener != null && VideoView.this.mOnInfoListener.onInfo(mp, what, extra)) {
                return true;
            }
            switch (what) {
                case 701:
                    if (VideoView.this.mMediaPlayType != 1 || NetworkUtil.isNetworkAvailable()) {
                        VideoView.this.setCurrentState(6);
                        return true;
                    }
                    if (VideoView.this.mOnErrorListener != null) {
                        VideoView.this.mOnErrorListener.onError(mp, what, extra);
                    }
                    int unused = VideoView.this.mErrorCode = -1004;
                    return true;
                case 702:
                    int unused2 = VideoView.this.mErrorCode = -1;
                    VideoView.this.setCurrentState(VideoView.this.mTargetState);
                    return true;
                case 2000:
                    int unused3 = VideoView.this.mAudioType = extra;
                    if (VideoView.this.mOnAudioInfoListener == null) {
                        return true;
                    }
                    VideoView.this.mOnAudioInfoListener.onAudioInfo(VideoView.this.mAudioType);
                    return true;
                default:
                    return false;
            }
        }
    };
    private boolean mIsCustomError = false;
    /* access modifiers changed from: private */
    public int mMediaPlayType = 2;
    /* access modifiers changed from: private */
    public IMediaPlayer mMediaPlayer = null;
    /* access modifiers changed from: private */
    public MediaPlayerRender mMediaPlayerRender;
    String mNetadaption;
    private Parcel mNetworkTimeoutParam;
    /* access modifiers changed from: private */
    public IBaseVideo.OnAudioInfoListener mOnAudioInfoListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnCompletionListener mOnCompletionListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public IBaseVideo.OnFirstFrameListener mOnFirstFrameListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoExtendListener mOnInfoExtendListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoListener mOnInfoListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        public void onSeekComplete() {
            if (VideoView.this.mSeekCompleteListener != null) {
                VideoView.this.mSeekCompleteListener.onSeekComplete();
            }
        }
    };
    /* access modifiers changed from: private */
    public IBaseVideo.VideoHttpDnsListener mOnVideoHttpDnsListener;
    /* access modifiers changed from: private */
    public IBaseVideo.VideoRequestTsListener mOnVideoRequestTsListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private IVideo.VideoStateChangeListener mOnVideoStateChangeListener;
    private IVideo.VideoStateChangeListener mOnVideoStateChangeListenerForMediaCenterview;
    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        /* JADX WARNING: Removed duplicated region for block: B:11:0x00b8 A[Catch:{ Exception -> 0x0278 }] */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x00ec A[Catch:{ Exception -> 0x0278 }] */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0118 A[Catch:{ Exception -> 0x0278 }] */
        /* JADX WARNING: Removed duplicated region for block: B:50:0x0261  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onPrepared(java.lang.Object r15) {
            /*
                r14 = this;
                java.lang.String r9 = "VideoView"
                java.lang.String r10 = "onPrepared!"
                android.util.Log.d(r9, r10)
                r6 = r15
                com.yunos.tv.tvsdk.media.IMediaPlayer r6 = (com.yunos.tv.tvsdk.media.IMediaPlayer) r6
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r10 = 0
                int unused = r9.mTrayAgainCount = r10
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r10 = 2
                r9.setCurrentState(r10)
                java.lang.Class r9 = r6.getClass()     // Catch:{ Exception -> 0x0278 }
                java.lang.String r10 = "getMetadata"
                r11 = 2
                java.lang.Class[] r11 = new java.lang.Class[r11]     // Catch:{ Exception -> 0x0278 }
                r12 = 0
                java.lang.Class r13 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0278 }
                r11[r12] = r13     // Catch:{ Exception -> 0x0278 }
                r12 = 1
                java.lang.Class r13 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0278 }
                r11[r12] = r13     // Catch:{ Exception -> 0x0278 }
                java.lang.reflect.Method r5 = r9.getMethod(r10, r11)     // Catch:{ Exception -> 0x0278 }
                r9 = 2
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r10 = 0
                r11 = 0
                java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)     // Catch:{ Exception -> 0x0278 }
                r9[r10] = r11     // Catch:{ Exception -> 0x0278 }
                r10 = 1
                r11 = 0
                java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)     // Catch:{ Exception -> 0x0278 }
                r9[r10] = r11     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r7 = r5.invoke(r6, r9)     // Catch:{ Exception -> 0x0278 }
                if (r7 == 0) goto L_0x0264
                java.lang.Class r0 = r7.getClass()     // Catch:{ Exception -> 0x0278 }
                java.lang.String r9 = "has"
                r10 = 1
                java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0278 }
                r10[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.reflect.Method r4 = r0.getMethod(r9, r10)     // Catch:{ Exception -> 0x0278 }
                java.lang.String r9 = "getBoolean"
                r10 = 1
                java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0278 }
                r10[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.reflect.Method r3 = r0.getMethod(r9, r10)     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 1
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r4.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x009b
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 1
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r3.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x0258
            L_0x009b:
                r9 = 1
            L_0x009c:
                boolean unused = r10.mCanPause = r9     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 2
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r4.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x00cf
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 2
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r3.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x025b
            L_0x00cf:
                r9 = 1
            L_0x00d0:
                boolean unused = r10.mCanSeekBack = r9     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 3
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r4.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x0103
                r9 = 1
                java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x0278 }
                r11 = 0
                r12 = 3
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0278 }
                r9[r11] = r12     // Catch:{ Exception -> 0x0278 }
                java.lang.Object r9 = r3.invoke(r7, r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ Exception -> 0x0278 }
                boolean r9 = r9.booleanValue()     // Catch:{ Exception -> 0x0278 }
                if (r9 == 0) goto L_0x025e
            L_0x0103:
                r9 = 1
            L_0x0104:
                boolean unused = r10.mCanSeekForward = r9     // Catch:{ Exception -> 0x0278 }
                java.lang.String r10 = "VideoView"
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0278 }
                r9.<init>()     // Catch:{ Exception -> 0x0278 }
                java.lang.String r11 = "objMeta:"
                java.lang.StringBuilder r11 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                if (r7 != 0) goto L_0x0261
                r9 = 1
            L_0x0119:
                java.lang.StringBuilder r9 = r11.append(r9)     // Catch:{ Exception -> 0x0278 }
                java.lang.String r11 = ",mCanPause:"
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                boolean r11 = r11.mCanPause     // Catch:{ Exception -> 0x0278 }
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                java.lang.String r11 = ",mCanSeekBack:"
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                boolean r11 = r11.mCanSeekBack     // Catch:{ Exception -> 0x0278 }
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                java.lang.String r11 = ",mCanSeekForward:"
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                boolean r11 = r11.mCanSeekForward     // Catch:{ Exception -> 0x0278 }
                java.lang.StringBuilder r9 = r9.append(r11)     // Catch:{ Exception -> 0x0278 }
                java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0278 }
                android.util.Log.i(r10, r9)     // Catch:{ Exception -> 0x0278 }
            L_0x0157:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                com.yunos.tv.tvsdk.media.IMediaPlayer$OnPreparedListener r9 = r9.mOnPreparedListener
                if (r9 == 0) goto L_0x016e
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                com.yunos.tv.tvsdk.media.IMediaPlayer$OnPreparedListener r9 = r9.mOnPreparedListener
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                com.yunos.tv.tvsdk.media.IMediaPlayer r10 = r10.mMediaPlayer
                r9.onPrepared(r10)
            L_0x016e:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r6.getVideoWidth()
                int unused = r9.mVideoWidth = r10
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r6.getVideoHeight()
                int unused = r9.mVideoHeight = r10
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r9.resizeByDAR(r6)
                java.lang.String r9 = "VideoView"
                java.lang.StringBuilder r10 = new java.lang.StringBuilder
                r10.<init>()
                java.lang.String r11 = "onPrepared videoSize:("
                java.lang.StringBuilder r10 = r10.append(r11)
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r11 = r11.mVideoWidth
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r11 = ","
                java.lang.StringBuilder r10 = r10.append(r11)
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r11 = r11.mVideoHeight
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r11 = ")"
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r10 = r10.toString()
                android.util.Log.d(r9, r10)
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r8 = r9.mSeekWhenPrepared
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mVideoWidth
                if (r9 == 0) goto L_0x02d3
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mVideoHeight
                if (r9 == 0) goto L_0x02d3
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                boolean r9 = r9.isStretch
                if (r9 != 0) goto L_0x0290
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                android.view.SurfaceHolder r9 = r9.getHolder()
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r10.mVideoWidth
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r11 = r11.mVideoHeight
                r9.setFixedSize(r10, r11)
            L_0x01f0:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                boolean r9 = r9.isStretch
                if (r9 == 0) goto L_0x029e
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mSurfaceWidth
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r10.getWidth()
                if (r9 != r10) goto L_0x029b
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mSurfaceHeight
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r10.getHeight()
                if (r9 != r10) goto L_0x029b
                r2 = 1
            L_0x0215:
                if (r2 == 0) goto L_0x0257
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mTargetState
                r10 = 3
                if (r9 != r10) goto L_0x02c0
                java.lang.String r9 = "VideoView"
                java.lang.StringBuilder r10 = new java.lang.StringBuilder
                r10.<init>()
                java.lang.String r11 = "start onprepared! mSeekWhenPrepared:"
                java.lang.StringBuilder r10 = r10.append(r11)
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r11 = r11.mSeekWhenPrepared
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.String r11 = ",seekToPosition:"
                java.lang.StringBuilder r10 = r10.append(r11)
                java.lang.StringBuilder r10 = r10.append(r8)
                java.lang.String r10 = r10.toString()
                android.util.Log.d(r9, r10)
                if (r8 == 0) goto L_0x0252
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r9.seekTo(r8)
            L_0x0252:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r9.start()
            L_0x0257:
                return
            L_0x0258:
                r9 = 0
                goto L_0x009c
            L_0x025b:
                r9 = 0
                goto L_0x00d0
            L_0x025e:
                r9 = 0
                goto L_0x0104
            L_0x0261:
                r9 = 0
                goto L_0x0119
            L_0x0264:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this     // Catch:{ Exception -> 0x0278 }
                r12 = 1
                boolean r11 = r11.mCanSeekForward = r12     // Catch:{ Exception -> 0x0278 }
                boolean r10 = r10.mCanSeekBack = r11     // Catch:{ Exception -> 0x0278 }
                boolean unused = r9.mCanPause = r10     // Catch:{ Exception -> 0x0278 }
                goto L_0x0157
            L_0x0278:
                r1 = move-exception
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                com.yunos.tv.tvsdk.media.view.VideoView r11 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r12 = 1
                boolean r11 = r11.mCanSeekForward = r12
                boolean r10 = r10.mCanSeekBack = r11
                boolean unused = r9.mCanPause = r10
                r1.printStackTrace()
                goto L_0x0157
            L_0x0290:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                android.view.SurfaceHolder r9 = r9.getHolder()
                r9.setSizeFromLayout()
                goto L_0x01f0
            L_0x029b:
                r2 = 0
                goto L_0x0215
            L_0x029e:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mSurfaceWidth
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r10.mVideoWidth
                if (r9 != r10) goto L_0x02bd
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mSurfaceHeight
                com.yunos.tv.tvsdk.media.view.VideoView r10 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r10 = r10.mVideoHeight
                if (r9 != r10) goto L_0x02bd
                r2 = 1
                goto L_0x0215
            L_0x02bd:
                r2 = 0
                goto L_0x0215
            L_0x02c0:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                boolean r9 = r9.isPlaying()
                if (r9 != 0) goto L_0x0257
                if (r8 != 0) goto L_0x0257
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.getCurrentPosition()
                if (r9 <= 0) goto L_0x0257
                goto L_0x0257
            L_0x02d3:
                java.lang.String r9 = "VideoView"
                java.lang.String r10 = "can't get video size!"
                android.util.Log.w(r9, r10)
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                android.view.SurfaceHolder r9 = r9.getHolder()
                r9.setSizeFromLayout()
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                int r9 = r9.mTargetState
                r10 = 3
                if (r9 != r10) goto L_0x0257
                if (r8 == 0) goto L_0x02fe
                java.lang.String r9 = "VideoView"
                java.lang.String r10 = "seekto on prepared2!"
                android.util.Log.d(r9, r10)
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r9.seekTo(r8)
            L_0x02fe:
                com.yunos.tv.tvsdk.media.view.VideoView r9 = com.yunos.tv.tvsdk.media.view.VideoView.this
                r9.start()
                goto L_0x0257
            */
            throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.media.view.VideoView.AnonymousClass2.onPrepared(java.lang.Object):void");
        }
    };
    private IBaseVideo.OnPreviewInfoListener mPreviewInfoListener;
    /* access modifiers changed from: private */
    public int mProgressPercent;
    /* access modifiers changed from: private */
    public long mRadio;
    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            boolean isValidState;
            boolean hasValidSize;
            int unused = VideoView.this.mSurfaceState = 1;
            Log.d(VideoView.TAG, "surfaceChanged!");
            int unused2 = VideoView.this.mSurfaceWidth = w;
            int unused3 = VideoView.this.mSurfaceHeight = h;
            if (VideoView.this.mTargetState != 3 || VideoView.this.mCurrentState == 6 || VideoView.this.mCurrentState == 3 || VideoView.this.mErrorCode == -1004) {
                isValidState = false;
            } else {
                isValidState = true;
            }
            if (VideoView.this.isStretch) {
                hasValidSize = VideoView.this.mSurfaceWidth == VideoView.this.getWidth() && VideoView.this.mSurfaceHeight == VideoView.this.getHeight();
            } else {
                hasValidSize = VideoView.this.mSurfaceWidth == VideoView.this.mVideoWidth && VideoView.this.mSurfaceHeight == VideoView.this.mVideoHeight;
            }
            if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (VideoView.this.mSeekWhenPrepared != 0) {
                    VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                }
                VideoView.this.start();
            }
            if (VideoView.this.mSurfaceCallback != null) {
                VideoView.this.mSurfaceCallback.surfaceChanged(holder, format, w, h);
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            int unused = VideoView.this.mSurfaceState = 1;
            Log.d(VideoView.TAG, "surfaceCreated");
            VideoView.this.mSurfaceHolder = holder;
            VideoView.this.openVideo();
            if (VideoView.this.mSurfaceCallback != null) {
                VideoView.this.mSurfaceCallback.surfaceCreated(holder);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            int unused = VideoView.this.mSurfaceState = 2;
            Log.d(VideoView.TAG, "surfaceDestroyed");
            int unused2 = VideoView.this.mCustomWidth = 0;
            int unused3 = VideoView.this.mCustomHeight = 0;
            VideoView.this.mSurfaceHolder = null;
            if (VideoView.this.mMediaPlayerRender != null) {
                VideoView.this.mMediaPlayerRender.releaseSurface();
            }
            VideoView.this.release(true);
            Uri unused4 = VideoView.this.mUri = null;
            if (VideoView.this.mSurfaceCallback != null) {
                VideoView.this.mSurfaceCallback.surfaceDestroyed(holder);
            }
        }
    };
    /* access modifiers changed from: private */
    public IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    /* access modifiers changed from: private */
    public int mSeekWhenPrepared;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(Object player, int width, int height) {
            Log.d(VideoView.TAG, "onVideoSizeChanged");
            IMediaPlayer mp = (IMediaPlayer) player;
            int unused = VideoView.this.mVideoWidth = mp.getVideoWidth();
            int unused2 = VideoView.this.mVideoHeight = mp.getVideoHeight();
            VideoView.this.resizeByDAR(mp);
            VideoView.this.reBuildRoundedSizeData();
            if (VideoView.this.isStretch) {
                VideoView.this.getHolder().setSizeFromLayout();
            } else if (!(VideoView.this.mVideoWidth == 0 || VideoView.this.mVideoHeight == 0)) {
                VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
            }
            if (VideoView.this.mOnVideoSizeChangedListener != null) {
                VideoView.this.mOnVideoSizeChangedListener.onVideoSizeChanged(mp, width, height);
            }
        }
    };
    /* access modifiers changed from: private */
    public SurfaceHolder.Callback mSurfaceCallback;
    /* access modifiers changed from: private */
    public int mSurfaceHeight;
    protected SurfaceHolder mSurfaceHolder = null;
    /* access modifiers changed from: private */
    public int mSurfaceState = -1;
    /* access modifiers changed from: private */
    public int mSurfaceWidth;
    /* access modifiers changed from: private */
    public int mTargetState = 0;
    /* access modifiers changed from: private */
    public int mTrayAgainCount;
    private Handler mTryAgainHandler = new Handler();
    /* access modifiers changed from: private */
    public Uri mUri;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoWidth;

    public VideoView(Context context) {
        super(context);
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public void setIgnoreDestroy(boolean ignore) {
        this.mIgnoreDestroy = ignore;
    }

    public boolean getIgnoreDestroy() {
        return this.mIgnoreDestroy;
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int visibility) {
        if (!this.mIgnoreDestroy) {
            super.onWindowVisibilityChanged(visibility);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (!this.mIgnoreDestroy) {
            super.onDetachedFromWindow();
        }
    }

    public void setStretch(boolean isStretch2) {
        if (this.isStretch != isStretch2) {
            this.isStretch = isStretch2;
            requestLayout();
        }
    }

    public boolean getStretch() {
        return this.isStretch;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        if (this.mCustomWidth <= 0 || this.mCustomHeight <= 0) {
            if (!this.isStretch) {
                width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
                height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
                if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                    int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
                    int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
                    int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
                    int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
                    if (widthSpecMode == 1073741824 && heightSpecMode == 1073741824) {
                        width = widthSpecSize;
                        height = heightSpecSize;
                        if (this.mVideoWidth * height < this.mVideoHeight * width) {
                            width = (this.mVideoWidth * height) / this.mVideoHeight;
                        } else if (this.mVideoWidth * height > this.mVideoHeight * width) {
                            height = (this.mVideoHeight * width) / this.mVideoWidth;
                        }
                    } else if (widthSpecMode == 1073741824) {
                        width = widthSpecSize;
                        height = (this.mVideoHeight * width) / this.mVideoWidth;
                        if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                            height = heightSpecSize;
                        }
                    } else if (heightSpecMode == 1073741824) {
                        height = heightSpecSize;
                        width = (this.mVideoWidth * height) / this.mVideoHeight;
                        if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                            width = widthSpecSize;
                        }
                    } else {
                        width = this.mVideoWidth;
                        height = this.mVideoHeight;
                        if (heightSpecMode == Integer.MIN_VALUE && height > heightSpecSize) {
                            height = heightSpecSize;
                            width = (this.mVideoWidth * height) / this.mVideoHeight;
                        }
                        if (widthSpecMode == Integer.MIN_VALUE && width > widthSpecSize) {
                            width = widthSpecSize;
                            height = (this.mVideoHeight * width) / this.mVideoWidth;
                        }
                    }
                }
            } else {
                width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
                height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            }
            int[] size = getSizebyByProportion(width, height);
            if (size != null) {
                width = size[0];
                height = size[1];
            }
            Log.d(TAG, "onmeasure2 (" + width + "," + height + ")");
            setMeasuredDimension(width, height);
            return;
        }
        int width2 = this.mCustomWidth;
        int height2 = this.mCustomHeight;
        Log.d(TAG, "onmeasure1 (" + width2 + "," + height2 + ")");
        setMeasuredDimension(width2, height2);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoView.class.getName());
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        return getDefaultSize(desiredSize, measureSpec);
    }

    @SuppressLint({"Recycle"})
    private void initVideoView() {
        this.mNetworkTimeoutParam = Parcel.obtain();
        this.mNetworkTimeoutParam.writeInt(100);
        this.mNetworkTimeoutParam.writeInt(30000);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        SurfaceHolder surfaceHolder = getHolder();
        if (surfaceHolder != null) {
            surfaceHolder.addCallback(this.mSHCallback);
            surfaceHolder.setKeepScreenOn(true);
            surfaceHolder.setType(3);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        setCurrentState(0);
        this.mTargetState = 0;
        setZOrderMediaOverlay(true);
    }

    public void setInitColor(float r, float g, float b, float a) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setInitColor(r, g, b, a);
        }
    }

    public void setRotate(float angle) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setRotate(angle);
        }
    }

    public void setRenderRoundedRectSize(int width, int height) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setRoundedRectSize(width, height);
        }
    }

    public void setRenderRoundedRadius(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setRoundedRadiusSize(leftTop, rightTop, leftBottom, rightBottom);
            this.mMediaPlayerRender.reBuildData();
        }
    }

    public void reBuildRoundedSizeData() {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.reBuildData();
        }
    }

    private boolean isJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setVideoInfo(Object... params) {
        if (params == null || params.length == 0) {
            Log.w(TAG, "error params! params:" + params);
            return;
        }
        AdoVideo adoVideo = null;
        try {
            if ((params[0] instanceof String) && isJson(params[0])) {
                adoVideo = new AdoVideo(params[0]);
            }
        } catch (Exception e) {
            Log.w(TAG, Log.getStackTraceString(e));
        }
        Log.i(TAG, "VideoView.setVideoInfo.adoVideo = " + adoVideo);
        if (adoVideo == null || adoVideo.getUri() == null) {
            Log.w(TAG, "invalid params! params:" + params);
            return;
        }
        int seekTo = adoVideo.getSeekToPos();
        if (isAdoPlayer()) {
            HashMap<String, String> headers = new HashMap<>();
            if (seekTo > 0) {
                headers.put(HEADER_DATASOURCE_START_TIME_KEY, String.valueOf(seekTo));
            }
            if (!TextUtils.isEmpty(adoVideo.getM3u8())) {
                headers.put(HEADER_DATASOURCE_M3U8, adoVideo.getM3u8());
            }
            if (!TextUtils.isEmpty(adoVideo.getVideoId())) {
                headers.put(HEADER_DATASOURCE_VIDEO_ID, adoVideo.getVideoId());
            }
            if (!TextUtils.isEmpty(adoVideo.getVideoName())) {
                headers.put(HEADER_DATASOURCE_VIDEO_NAME, adoVideo.getVideoName());
            }
            if (headers.size() == 0) {
                headers = null;
            }
            setVideoURI(adoVideo.getUri(), (Map<String, String>) headers);
            return;
        }
        setVideoURI(adoVideo.getUri());
        seekTo(seekTo);
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, (Map<String, String>) null);
    }

    public void setVideoURI(Uri uri, int startTime) {
        if (this.mMediaPlayType == 2) {
            Map<String, String> headers = null;
            if (startTime > 0) {
                headers = new HashMap<>();
                headers.put(HEADER_DATASOURCE_START_TIME_KEY, String.valueOf(startTime));
            }
            setVideoURI(uri, headers);
            return;
        }
        setVideoURI(uri, (Map<String, String>) null);
        seekTo(startTime);
    }

    public void setVideoM3u8URI(Uri uri, int startTime, String m3u8) {
        Map<String, String> headers = new HashMap<>();
        if (startTime > 0) {
            headers.put(HEADER_DATASOURCE_START_TIME_KEY, String.valueOf(startTime));
        }
        if (!TextUtils.isEmpty(m3u8)) {
            headers.put(HEADER_DATASOURCE_M3U8, m3u8);
        }
        setVideoURI(uri, headers);
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mTrayAgainCount = 0;
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        Log.d(TAG, "start stop mediaplayer!");
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.stop();
            this.mMediaPlayerRender.setInitColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
        if (this.mTryAgainHandler != null) {
            this.mTryAgainHandler.removeMessages(0);
        }
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
            } catch (IllegalStateException ex) {
                Log.e(TAG, ".stopPlayback.stop IllegalStateException");
                ex.printStackTrace();
            } catch (Exception ex2) {
                Log.e(TAG, ".stopPlayback.stop Excetion");
                ex2.printStackTrace();
            }
            try {
                this.mMediaPlayer.reset();
            } catch (Exception ex3) {
                Log.e(TAG, ".stopPlayback.reset Excetion");
                ex3.printStackTrace();
            }
            try {
                this.mMediaPlayer.release();
            } catch (Exception ex4) {
                Log.e(TAG, ".stopPlayback.release Excetion");
                ex4.printStackTrace();
            }
            this.mMediaPlayer = null;
            setCurrentState(0);
            this.mTargetState = 0;
        }
        Log.d(TAG, "finish stop mediaplayer!");
    }

    public void setMediaPlayerType(int type) {
        if (type == 2 || type == 1) {
            this.mMediaPlayType = type;
            return;
        }
        this.mMediaPlayType = 1;
        Log.e(TAG, "invalid MediaPlayerType:" + type);
    }

    public int getMediaPlayerType() {
        return this.mMediaPlayType;
    }

    public boolean isAdoPlayer() {
        return this.mMediaPlayType == 2;
    }

    /* access modifiers changed from: protected */
    public void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            if (this.mMediaPlayerRender == null || this.mMediaPlayerRender.getSurface() != null) {
                this.mIsCustomError = false;
                this.mErrorCode = 0;
                this.mErrorExtend = 0;
                this.mErrorInfoExtend = null;
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "pause");
                getContext().sendBroadcast(i);
                release(false);
                try {
                    setCurrentState(1);
                    if (this.mMediaPlayType == 1) {
                        this.mMediaPlayer = MediaPlayer.create(getContext(), 1);
                    } else if (this.mMediaPlayType == 3) {
                        this.mMediaPlayer = MediaPlayer.create(getContext(), 3);
                    } else {
                        this.mMediaPlayer = MediaPlayer.create(getContext(), 2);
                        if (!TextUtils.isEmpty(this.mNetadaption)) {
                            ((AdoMediaPlayer) this.mMediaPlayer).setNetadaption(this.mNetadaption);
                        }
                    }
                    if (this.mMediaPlayer == null) {
                        Log.e(TAG, "MediaPlayer=null mUri=" + this.mUri);
                        return;
                    }
                    this.mMediaPlayer.setPlayerParameter(1505, this.mNetworkTimeoutParam);
                    this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                    this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                    this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                    this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                    this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                    this.mMediaPlayer.setOnInfoExtendListener(this.mInfoExtendListener);
                    this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                    this.mMediaPlayer.setOnSeekCompleteListener(this.mOnSeekCompleteListener);
                    boolean bException = true;
                    try {
                        this.mMediaPlayer.setDataSource(getContext(), this.mUri, this.mHeaders);
                        bException = false;
                    } catch (IOException e) {
                        Log.e(TAG, "MediaPlayer.setDataSource IOException mUri=" + this.mUri + ", mHeaders=" + this.mHeaders);
                        e.printStackTrace();
                    } catch (IllegalArgumentException e2) {
                        Log.e(TAG, "MediaPlayer.setDataSource IllegalArgumentException mUri=" + this.mUri + ", mHeaders=" + this.mHeaders);
                        e2.printStackTrace();
                    } catch (SecurityException e3) {
                        Log.e(TAG, "MediaPlayer.setDataSource SecurityException mUri=" + this.mUri + ", mHeaders=" + this.mHeaders);
                        e3.printStackTrace();
                    } catch (IllegalStateException e4) {
                        Log.e(TAG, "MediaPlayer.setDataSource IllegalStateException mUri=" + this.mUri + ", mHeaders=" + this.mHeaders);
                        e4.printStackTrace();
                    } catch (Exception e5) {
                        Log.e(TAG, "MediaPlayer.setDataSource Exception mUri=" + this.mUri + ", mHeaders=" + this.mHeaders);
                        e5.printStackTrace();
                    }
                    if (bException) {
                        setCurrentState(-1);
                        this.mTargetState = -1;
                        this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
                        return;
                    }
                    this.mMediaPlayer.setAudioStreamType(3);
                    try {
                        this.mMediaPlayer.prepareAsync();
                        try {
                            if (this.mMediaPlayerRender != null && this.mMediaPlayType != 1) {
                                this.mMediaPlayer.setSurface(this.mMediaPlayerRender.getSurface());
                            } else if (this.mSurfaceHolder != null) {
                                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                            }
                        } catch (IllegalArgumentException e6) {
                            Log.e(TAG, "setDisplay failed IllegalArgumentException....");
                            e6.printStackTrace();
                        } catch (IllegalStateException e7) {
                            Log.e(TAG, "setDisplay failed IllegalStateException....");
                            e7.printStackTrace();
                        } catch (Exception e8) {
                            Log.e(TAG, "setDisplay failed Exception....");
                            e8.printStackTrace();
                        }
                    } catch (IllegalStateException ex) {
                        Log.e(TAG, "Unable to prepareAsync: " + ex.toString());
                        ex.printStackTrace();
                        setCurrentState(-1);
                        this.mTargetState = -1;
                        this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
                    }
                } catch (Exception ex2) {
                    Log.e(TAG, "Unable to create MediaPlayer Exception: " + this.mUri);
                    ex2.printStackTrace();
                    setCurrentState(-1);
                    this.mTargetState = -1;
                    this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
                }
            } else {
                Log.w(TAG, "mMediaPlayerRender.getSurface() null!");
            }
        }
    }

    /* access modifiers changed from: private */
    public void resizeByDAR(IMediaPlayer mp) {
        Parcel parcel;
        if (this.isStretch) {
            Log.d(TAG, "cancel resizeByDAR,on customsize or isStretch mode");
        } else if (mp != null && (parcel = mp.getParcelParameter(1506)) != null) {
            parcel.setDataPosition(0);
            int darWidth = parcel.readInt();
            int darHeight = parcel.readInt();
            Log.d(TAG, "getVideoSize dar:(" + darWidth + "," + darHeight + "),");
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0 && darWidth > 0 && darHeight > 0 && this.mVideoWidth * darHeight != this.mVideoHeight * darWidth) {
                Log.d(TAG, "resizeByDAR origin:(" + this.mVideoWidth + "," + this.mVideoHeight + ")");
                this.mVideoHeight = (this.mVideoWidth * darHeight) / darWidth;
                Log.d(TAG, "resizeByDAR resize:(" + this.mVideoWidth + "," + this.mVideoHeight + ")");
            }
        }
    }

    public void setOnVideoSizeChangeListener(IMediaPlayer.OnVideoSizeChangedListener l) {
        this.mOnVideoSizeChangedListener = l;
    }

    public void setOnInfoExtendListener(IMediaPlayer.OnInfoExtendListener l) {
        this.mOnInfoExtendListener = l;
    }

    public void setOnAudioInfoListener(IBaseVideo.OnAudioInfoListener l) {
        this.mOnAudioInfoListener = l;
    }

    public long getRadio() {
        return this.mRadio;
    }

    public long getBitRate() {
        return this.mBitRate;
    }

    public int getProgressPercent() {
        return this.mProgressPercent;
    }

    public int getAudioType() {
        return this.mAudioType;
    }

    public int getErrorcode() {
        return this.mErrorCode;
    }

    public int getErrorExtend() {
        return this.mErrorExtend;
    }

    public InfoExtend getErrorInfoExtend() {
        return this.mErrorInfoExtend;
    }

    public boolean isCustomError() {
        return this.mIsCustomError;
    }

    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        this.mOnBufferingUpdateListener = l;
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener l) {
        this.mSeekCompleteListener = l;
    }

    public void setRenderColor(float r, float g, float b, float a) {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.setInitColor(r, g, b, a);
        }
    }

    public void stopRender() {
        if (this.mMediaPlayerRender != null) {
            this.mMediaPlayerRender.stop();
        }
    }

    /* access modifiers changed from: private */
    public void release(boolean cleartargetstate) {
        if (cleartargetstate) {
        }
        this.mTryAgainHandler.removeMessages(0);
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setOnBufferingUpdateListener((IMediaPlayer.OnBufferingUpdateListener) null);
            this.mMediaPlayer.setOnCompletionListener((IMediaPlayer.OnCompletionListener) null);
            this.mMediaPlayer.setOnErrorListener((IMediaPlayer.OnErrorListener) null);
            this.mMediaPlayer.setOnInfoExtendListener((IMediaPlayer.OnInfoExtendListener) null);
            this.mMediaPlayer.setOnInfoListener((IMediaPlayer.OnInfoListener) null);
            this.mMediaPlayer.setOnPreparedListener((IMediaPlayer.OnPreparedListener) null);
            this.mMediaPlayer.setOnSeekCompleteListener((IMediaPlayer.OnSeekCompleteListener) null);
            this.mMediaPlayer.setOnTimedTextListener((IMediaPlayer.OnTimedTextListener) null);
            this.mMediaPlayer.setOnVideoSizeChangedListener((IMediaPlayer.OnVideoSizeChangedListener) null);
            try {
                this.mMediaPlayer.stop();
            } catch (IllegalStateException ex) {
                Log.e(TAG, ".MediaPlayer.stop IllegalStateException");
                ex.printStackTrace();
            } catch (Exception ex2) {
                Log.e(TAG, ".MediaPlayer.stop Excetion");
                ex2.printStackTrace();
            }
            try {
                this.mMediaPlayer.reset();
            } catch (Exception ex3) {
                Log.e(TAG, ".MediaPlayer.reset Excetion");
                ex3.printStackTrace();
            }
            try {
                this.mMediaPlayer.release();
            } catch (Exception ex4) {
                Log.e(TAG, ".MediaPlayer.release Excetion");
                ex4.printStackTrace();
            }
            this.mMediaPlayer = null;
            setCurrentState(0);
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
        }
        this.mAudioType = -1;
    }

    public void start() {
        Log.d(TAG, "start isInPlaybackState:" + isInPlaybackState() + ",state:" + this.mCurrentState);
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            setCurrentState(3);
        }
        this.mTargetState = 3;
    }

    public void replay(Object... objects) {
        if (objects == null || objects.length == 0) {
        }
    }

    public void pauseWithoutStateChange() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void pause() {
        boolean _isInPlaybackState = isInPlaybackState();
        Log.i(TAG, "pause(): isInPlaybackState:" + _isInPlaybackState + ", isPlaying:" + (this.mMediaPlayer != null ? this.mMediaPlayer.isPlaying() : false));
        if (_isInPlaybackState) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
            }
            if (this.mCurrentState != 6) {
                setCurrentState(4);
            }
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        start();
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public void customError(int error, int extend) {
        this.mIsCustomError = true;
        this.mErrorCode = error;
        this.mErrorExtend = extend;
        if (this.mOnErrorListener != null) {
            this.mOnErrorListener.onError(this.mMediaPlayer, 0, 0);
        }
        if (isPlaying()) {
            stopPlayback();
        }
        setCurrentState(-1);
    }

    public void setCurrentState(int state) {
        Log.d(TAG, "state:" + state);
        this.mCurrentState = state;
        if (this.mOnVideoStateChangeListener != null) {
            this.mOnVideoStateChangeListener.onStateChange(state);
        }
        if (this.mOnVideoStateChangeListenerForMediaCenterview != null) {
            this.mOnVideoStateChangeListenerForMediaCenterview.onStateChange(state);
        }
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public int getTargetState() {
        return this.mTargetState;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (!isInPlaybackState() || this.mSurfaceState != 1 || this.mCurrentState < 2) {
            this.mSeekWhenPrepared = msec;
            return;
        }
        Log.d(TAG, "invoke seek:" + msec);
        this.mMediaPlayer.seekTo(msec);
        this.mSeekWhenPrepared = 0;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public boolean isPause() {
        return isInPlaybackState() && this.mCurrentState == 4;
    }

    public boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void setOnVideoStateChangeListenerForMediaCenterview(IVideo.VideoStateChangeListener l) {
        this.mOnVideoStateChangeListenerForMediaCenterview = l;
    }

    public void setOnVideoStateChangeListener(IVideo.VideoStateChangeListener l) {
        this.mOnVideoStateChangeListener = l;
    }

    public void setOnVideoRequestTsListener(IBaseVideo.VideoRequestTsListener l) {
        this.mOnVideoRequestTsListener = l;
    }

    public void setOnVideoHttpDnsListener(IBaseVideo.VideoHttpDnsListener l) {
        this.mOnVideoHttpDnsListener = l;
    }

    public void setOnFirstFrameListener(IBaseVideo.OnFirstFrameListener l) {
        this.mOnFirstFrameListener = l;
    }

    public void setSurfaceCallback(SurfaceHolder.Callback surfaceCallback) {
        this.mSurfaceCallback = surfaceCallback;
    }

    public void setOnThrowableCallback(IBaseVideo.OnThrowableCallback l) {
    }

    public void setOnAdRemainTimeListener(IBaseVideo.OnAdRemainTimeListener l) {
    }

    public void setOnAdRemainTimeListenerForMediaCenterView(IBaseVideo.OnAdRemainTimeListener l) {
    }

    public long getSourceBitrate() {
        if (this.mMediaPlayer != null && isInPlaybackState()) {
            long bit = this.mMediaPlayer.getSourceBitrate();
            if (bit > 0) {
                return bit;
            }
        }
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = r2.mMediaPlayer.getNetSourceURL();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getNetSourceURL() {
        /*
            r2 = this;
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            if (r1 == 0) goto L_0x001c
            boolean r1 = r2.isInPlaybackState()
            if (r1 == 0) goto L_0x001c
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            java.lang.String r0 = r1.getNetSourceURL()
            if (r0 == 0) goto L_0x001c
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)
            if (r1 != 0) goto L_0x001c
        L_0x001b:
            return r0
        L_0x001c:
            java.lang.String r0 = ""
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.media.view.VideoView.getNetSourceURL():java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.mMediaPlayer.getHttpHeader();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getHttpHeader() {
        /*
            r2 = this;
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            if (r1 == 0) goto L_0x0016
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            java.lang.String r0 = r1.getHttpHeader()
            if (r0 == 0) goto L_0x0016
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)
            if (r1 != 0) goto L_0x0016
        L_0x0015:
            return r0
        L_0x0016:
            java.lang.String r0 = ""
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.media.view.VideoView.getHttpHeader():java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.mMediaPlayer.getCodecInfo();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getCodecInfo() {
        /*
            r2 = this;
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            if (r1 == 0) goto L_0x0016
            com.yunos.tv.tvsdk.media.IMediaPlayer r1 = r2.mMediaPlayer
            java.lang.String r0 = r1.getCodecInfo()
            if (r0 == 0) goto L_0x0016
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)
            if (r1 != 0) goto L_0x0016
        L_0x0015:
            return r0
        L_0x0016:
            java.lang.String r0 = ""
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.tvsdk.media.view.VideoView.getCodecInfo():java.lang.String");
    }

    public void setHttpDNS(String httpdns) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setHttpDNS(httpdns);
        }
    }

    public void setNetadaption(String netadaption) {
        Log.d(TAG, "setNetadaption " + netadaption + ", mMediaPlayer:" + this.mMediaPlayer);
        this.mNetadaption = netadaption;
        if (this.mMediaPlayer != null && (this.mMediaPlayer instanceof AdoMediaPlayer)) {
            ((AdoMediaPlayer) this.mMediaPlayer).setNetadaption(netadaption);
        }
    }

    public void release() {
        release(true);
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public SurfaceView getSurfaceView() {
        return this;
    }

    public void setDefinition(int definition, int currentPosition) {
    }

    private int[] getSizebyByProportion(int width, int height) {
        int pw;
        int ph;
        int vWidth = getVideoWidth();
        int vHeight = getVideoHeight();
        if (vWidth <= 0 || vHeight <= 0 || width <= 0 || height <= 0) {
            return null;
        }
        if (this.mDimenMode == 0) {
            pw = this.mVideoWidth;
            ph = vHeight;
        } else if (this.mDimenMode == 2) {
            pw = 16;
            ph = 9;
        } else if (this.mDimenMode == 3) {
            pw = 4;
            ph = 3;
        } else {
            pw = width;
            ph = height;
        }
        int[] size = getLargestSizeByProportion(width, height, pw, ph);
        if (size[0] > 0 && size[1] > 0) {
            return size;
        }
        size[0] = width;
        size[1] = height;
        return size;
    }

    private int[] getLargestSizeByProportion(int width, int height, int pw, int ph) {
        if (((long) (width * ph)) > ((long) (height * pw))) {
            return new int[]{(int) (((long) (height * pw)) / ((long) ph)), height};
        }
        return new int[]{width, (int) (((long) (width * ph)) / ((long) pw))};
    }

    private void setDimensionByDimenMode() {
        if (this.mSurfaceHolder != null) {
            this.mSurfaceHolder.setSizeFromLayout();
        }
        requestLayout();
    }

    public void setDimension(int width, int height) {
        if (width == this.mCustomWidth && height == this.mCustomHeight) {
            Log.d(TAG, "same size!");
            return;
        }
        this.isStretch = true;
        this.mCustomWidth = width;
        this.mCustomHeight = height;
        requestLayout();
    }

    public void setDimensionOrigin() {
        if (this.mDimenMode != 0) {
            this.mDimenMode = 0;
            setDimensionByDimenMode();
        }
    }

    public void setDimensionFull() {
        if (this.mDimenMode != 1) {
            this.mDimenMode = 1;
            setDimensionByDimenMode();
        }
    }

    public void setDimension_16_9() {
        if (this.mDimenMode != 2) {
            this.mDimenMode = 2;
            setDimensionByDimenMode();
        }
    }

    public void setDimension_4_3() {
        if (this.mDimenMode != 3) {
            this.mDimenMode = 3;
            setDimensionByDimenMode();
        }
    }

    public void setPauseADTopMarginPercent(int topMarginPercent) {
    }

    public void hidePauseAd() {
    }

    public boolean isAdPlaying() {
        return false;
    }

    public int getAdRemainTime() {
        return -1;
    }

    public void onActivityResume(Activity activity) {
    }

    public void onActivityStop(Activity activity) {
        stopPlayback();
    }

    public View getPlayerView() {
        return this;
    }

    public IMediaPlayer getIMediaPlayer() {
        return this.mMediaPlayer;
    }

    public void setDefinitionChangedListener(IBaseVideo.OnDefinitionChangedListener lis) {
    }

    public void setSkipHeadTailInfoListener(IBaseVideo.OnSkipHeadTailInfoListener lis) {
    }

    public void setPreviewInfoListener(IBaseVideo.OnPreviewInfoListener lis) {
        this.mPreviewInfoListener = lis;
    }

    public void onAuthorityResult(boolean isSuccess, MTopInfoBase result) {
        MTopTaoTvInfo info;
        if (isSuccess && (result instanceof MTopTaoTvInfo) && (info = (MTopTaoTvInfo) result) != null && !info.isDataEmpty() && this.mPreviewInfoListener != null) {
            this.mPreviewInfoListener.onPreviewInfoReady(info.isTrial(), info.getDataResult().duration);
        }
    }

    static class AdoVideo implements IPlaybackInfo {
        private static final String TAG = "AdoVideo";
        private static final String TAG_M3U8 = "m3u8";
        private static final String TAG_STARTTIME = "starttime";
        private static final String TAG_URI = "uri";
        private static final String TAG_VID = "vid";
        private static final String TAG_VNAME = "name";
        private static final long serialVersionUID = 7276396421517386106L;
        @SerializedName("m3u8")
        private String mM3u8;
        @SerializedName("name")
        private String mName;
        @SerializedName("starttime")
        private String mSeekTo;
        @SerializedName("uri")
        private String mUri;
        @SerializedName("vid")
        private String mVid;

        public AdoVideo(String json) throws Exception {
            parseFromJson(json);
        }

        /* access modifiers changed from: protected */
        public void parseFromJson(String json) throws Exception {
            if (!TextUtils.isEmpty(json)) {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj.has("uri")) {
                    this.mUri = jsonObj.optString("uri");
                }
                if (jsonObj.has("m3u8")) {
                    this.mM3u8 = jsonObj.optString("m3u8");
                }
                if (jsonObj.has("starttime")) {
                    this.mSeekTo = jsonObj.optString("starttime");
                }
                if (jsonObj.has("vid")) {
                    this.mVid = jsonObj.optString("vid");
                }
                if (jsonObj.has("name")) {
                    this.mName = jsonObj.optString("name");
                }
            }
        }

        public Uri getUri() {
            if (TextUtils.isEmpty(this.mUri)) {
            }
            return Uri.parse(this.mUri);
        }

        public String getM3u8() {
            return this.mM3u8;
        }

        public int getSeekToPos() {
            if (this.mSeekTo == null || !TextUtils.isDigitsOnly(this.mSeekTo)) {
                return 0;
            }
            return Integer.valueOf(this.mSeekTo).intValue();
        }

        public String getVideoId() {
            return this.mVid;
        }

        public String getVideoName() {
            return this.mName;
        }

        public String toString() {
            return "mUri = " + this.mUri + ", mM3u8 = " + this.mM3u8 + ", mSeekTo = " + this.mSeekTo + ", mVid = " + this.mVid + ", mName = " + this.mName;
        }
    }
}
