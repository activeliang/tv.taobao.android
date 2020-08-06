package com.yunos.tv.tvsdk.media.mediacodec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import anetwork.channel.util.RequestConstant;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import com.yunos.tv.tvsdk.media.mediacodec.MediaDecoderThread;
import java.io.IOException;
import java.util.Map;

@SuppressLint({"NewApi"})
public class MediaCodecPlayer implements IMediaPlayer {
    private final int MIN_SEEK_TIME = 1000;
    private final String TAG = "MediaCodecPlayer";
    private VIDEO_PLAY_STATE mCurrState;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    MediaCodecPlayer.this.stop();
                    if (MediaCodecPlayer.this.mOnCompletionListener != null) {
                        MediaCodecPlayer.this.mOnCompletionListener.onCompletion(MediaCodecPlayer.this);
                        return;
                    }
                    return;
                case 3:
                    Log.i("MediaCodecPlayer", "try later");
                    return;
                case 4:
                    Log.i("MediaCodecPlayer", "thread already");
                    if (MediaCodecPlayer.this.mOnVideoSizeChangedListener != null) {
                        MediaCodecPlayer.this.mOnVideoSizeChangedListener.onVideoSizeChanged(MediaCodecPlayer.this, MediaCodecPlayer.this.mWidth, MediaCodecPlayer.this.mHeight);
                    }
                    if (MediaCodecPlayer.this.mOnPreparedListener != null) {
                        MediaCodecPlayer.this.mOnPreparedListener.onPrepared(MediaCodecPlayer.this);
                        return;
                    }
                    return;
                case 5:
                    boolean unused = MediaCodecPlayer.this.mIsSeeking = false;
                    if (MediaCodecPlayer.this.mOnSeekCompleteListener != null) {
                        MediaCodecPlayer.this.mOnSeekCompleteListener.onSeekComplete();
                        return;
                    }
                    return;
                case 6:
                    if (MediaCodecPlayer.this.mOnInfoListener != null) {
                        MediaCodecPlayer.this.mOnInfoListener.onInfo(MediaCodecPlayer.this, 701, -1);
                        return;
                    }
                    return;
                case 7:
                    if (MediaCodecPlayer.this.mOnInfoListener != null) {
                        MediaCodecPlayer.this.mOnInfoListener.onInfo(MediaCodecPlayer.this, 702, -1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int mHeight;
    /* access modifiers changed from: private */
    public Boolean mIsDecodeThreadEnd = false;
    private boolean mIsRenderSurfaceReady;
    /* access modifiers changed from: private */
    public boolean mIsSeeking;
    private MediaCodec mMediaCodec;
    private MediaDecoderThread mMediaDecoderThread;
    private long mMediaDuration;
    private MediaExtractor mMediaExtractor;
    private MediaFormat mMediaFormat;
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoListener mOnInfoListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnPreparedListener mOnPreparedListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private String mVideoPath;
    /* access modifiers changed from: private */
    public int mWidth;

    private enum VIDEO_PLAY_STATE {
        INIT,
        START,
        PAUSE,
        STOP
    }

    public MediaCodecPlayer() {
        init();
    }

    public void getVideoInfo() {
        if (this.mMediaExtractor != null) {
            long cachedDuration = this.mMediaExtractor.getCachedDuration();
            long sampleTime = this.mMediaExtractor.getSampleTime();
            Log.i("MediaCodecPlayer", "getVideoInfo cachedDuration=" + cachedDuration + " sampleTime=" + sampleTime + " cachedEnd=" + this.mMediaExtractor.hasCacheReachedEndOfStream());
        }
    }

    public void setDisplay(SurfaceHolder sh) {
        Log.i("MediaCodecPlayer", "setDisplay");
        if (this.mCurrState == VIDEO_PLAY_STATE.START) {
            throw new IllegalStateException("setSurface is running can not set.");
        } else if (this.mMediaCodec != null) {
            this.mMediaCodec.configure(this.mMediaFormat, sh.getSurface(), (MediaCrypto) null, 0);
            this.mMediaDecoderThread.start();
        }
    }

    public void setSurface(Surface surface) {
        Log.i("MediaCodecPlayer", "setSurface");
        if (this.mCurrState == VIDEO_PLAY_STATE.START) {
            throw new IllegalStateException("setSurface is running can not set.");
        } else if (this.mMediaCodec != null) {
            this.mMediaCodec.configure(this.mMediaFormat, surface, (MediaCrypto) null, 0);
            this.mMediaDecoderThread.start();
        }
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mVideoPath = uri.toString();
        resetMedia(this.mVideoPath);
    }

    public void release() {
        stop();
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
    }

    public void prepare() throws IOException, IllegalStateException {
    }

    public void prepareAsync() throws IllegalStateException {
    }

    public int getVideoWidth() {
        return this.mWidth;
    }

    public int getVideoHeight() {
        return this.mHeight;
    }

    public boolean isPlaying() {
        return this.mCurrState == VIDEO_PLAY_STATE.START;
    }

    public void seekTo(int msec) throws IllegalStateException {
        if (this.mMediaExtractor != null && this.mCurrState == VIDEO_PLAY_STATE.START) {
            Log.i("MediaCodecPlayer", "seekTo msec=" + msec + " mIsSeeking=" + this.mIsSeeking);
            if (!this.mIsSeeking) {
                this.mIsSeeking = true;
                if (((long) msec) > this.mMediaDuration) {
                    msec = (int) this.mMediaDuration;
                } else if (msec < 0) {
                    msec = 0;
                }
                int diff = msec - getCurrentPosition();
                if (diff > 0 && diff >= 1000) {
                    this.mMediaDecoderThread.setSeekTime((long) msec, true);
                    this.mMediaExtractor.seekTo((long) (msec * 1000), 1);
                } else if (diff >= 0 || diff > -1000) {
                    Log.i("MediaCodecPlayer", "seekTo too small");
                    this.mHandler.sendEmptyMessage(5);
                } else {
                    this.mMediaDecoderThread.setSeekTime((long) msec, false);
                    this.mMediaExtractor.seekTo((long) (msec * 1000), 0);
                }
            }
        }
    }

    public int getCurrentPosition() {
        boolean isEnd;
        long curr;
        if (this.mMediaExtractor == null) {
            return 0;
        }
        synchronized (this.mIsDecodeThreadEnd) {
            isEnd = this.mIsDecodeThreadEnd.booleanValue();
        }
        if (this.mCurrState == VIDEO_PLAY_STATE.STOP) {
            isEnd = true;
        }
        if (isEnd) {
            curr = this.mMediaDuration;
        } else {
            curr = this.mMediaExtractor.getSampleTime();
        }
        return (int) (curr / 1000);
    }

    public int getDuration() {
        if (this.mMediaFormat != null) {
            return (int) (this.mMediaDuration / 1000);
        }
        return 0;
    }

    public void reset() {
    }

    public void start() throws IllegalStateException {
        if (this.mCurrState == VIDEO_PLAY_STATE.PAUSE || this.mCurrState == VIDEO_PLAY_STATE.INIT) {
            startDecoderRender();
            this.mCurrState = VIDEO_PLAY_STATE.START;
        }
    }

    public void pause() throws IllegalStateException {
        if (this.mCurrState == VIDEO_PLAY_STATE.START) {
            this.mCurrState = VIDEO_PLAY_STATE.PAUSE;
            pauseDecoderRender();
        }
    }

    public void stop() throws IllegalStateException {
        this.mCurrState = VIDEO_PLAY_STATE.STOP;
        stopDecoderRender();
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener) {
        this.mOnTimedTextListener = listener;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void setOnInfoExtendListener(IMediaPlayer.OnInfoExtendListener listener) {
    }

    private void init() {
    }

    private boolean checkIsReady() {
        boolean ready = true;
        if (!this.mIsRenderSurfaceReady) {
            ready = false;
        }
        if (this.mMediaDecoderThread == null || !this.mMediaDecoderThread.isReady()) {
            return false;
        }
        return ready;
    }

    private void resetMedia(String path) throws IOException {
        stopDecoderRender();
        this.mCurrState = VIDEO_PLAY_STATE.INIT;
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat format = null;
        MediaCodec mediaCodec = null;
        try {
            extractor.setDataSource(path);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        int i = 0;
        while (true) {
            if (i >= extractor.getTrackCount()) {
                break;
            }
            format = extractor.getTrackFormat(i);
            String mime = format.getString("mime");
            if (mime.startsWith("video/")) {
                extractor.selectTrack(i);
                mediaCodec = MediaCodec.createDecoderByType(mime);
                break;
            }
            i++;
        }
        this.mWidth = 0;
        this.mHeight = 0;
        try {
            this.mWidth = format.getInteger("width");
            this.mHeight = format.getInteger("height");
            long durationUs = format.getLong("durationUs");
            this.mMediaDuration = durationUs;
            Log.i(RequestConstant.ENV_TEST, "resetMedia durationUs=" + (durationUs / 1000) + " width=" + this.mWidth + " height=" + this.mHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mediaCodec == null) {
            Log.e("MediaCodecPlayer", "mediaCodec is null ,return");
            return;
        }
        this.mMediaExtractor = extractor;
        this.mMediaCodec = mediaCodec;
        this.mMediaFormat = format;
        initDecoderThread();
    }

    private void pauseDecoderRender() {
        if (this.mMediaDecoderThread != null) {
            this.mMediaDecoderThread.setPause();
        }
    }

    private void stopDecoderRender() {
        if (this.mMediaDecoderThread != null) {
            if (!this.mMediaDecoderThread.isReady()) {
                Log.i("MediaCodecPlayer", "stopDecoderRender release");
                if (this.mMediaExtractor != null) {
                    this.mMediaExtractor.release();
                    this.mMediaExtractor = null;
                }
                if (this.mMediaCodec != null) {
                    this.mMediaCodec.stop();
                    this.mMediaCodec.release();
                }
            }
            this.mMediaDecoderThread.interrupt();
            this.mMediaDecoderThread.setStop();
            this.mMediaDecoderThread = null;
        }
    }

    private void startDecoderRender() {
        if (this.mMediaDecoderThread != null) {
            this.mMediaDecoderThread.setStart();
        }
    }

    private void initDecoderThread() {
        Log.d("MediaCodecPlayer", " start to preparing mediaPlayer thread");
        if (this.mMediaCodec == null || this.mMediaDecoderThread != null) {
            Log.d("MediaCodecPlayer", "MediaCodec  in startDecoderThread is null,return");
            return;
        }
        this.mMediaDecoderThread = new MediaDecoderThread(this.mMediaCodec, this.mMediaExtractor);
        this.mMediaDecoderThread.setOnDecoderListener(new MediaDecoderThread.OnDecoderListener() {
            public void onDecoder(int msgCode) {
                MediaCodecPlayer.this.mHandler.sendEmptyMessage(msgCode);
                if (msgCode == 2) {
                    synchronized (MediaCodecPlayer.this.mIsDecodeThreadEnd) {
                        Boolean unused = MediaCodecPlayer.this.mIsDecodeThreadEnd = true;
                    }
                }
            }
        });
    }

    public long getSourceBitrate() {
        return 0;
    }

    public String getNetSourceURL() {
        return null;
    }

    public boolean setPlayerParameter(int key, Parcel params) {
        return false;
    }

    public Parcel getParcelParameter(int key) {
        return null;
    }

    public String getHttpHeader() {
        return null;
    }

    public void setHttpDNS(String httpdns) {
    }

    public String getCodecInfo() {
        return null;
    }
}
