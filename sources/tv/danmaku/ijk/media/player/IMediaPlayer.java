package tv.danmaku.ijk.media.player;

import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.IOException;

public interface IMediaPlayer {
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_MEDIACODEC_DECODE_ERROR = -111;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_INFO_ARTP_END_TO_END_DELAY = 820;
    public static final int MEDIA_INFO_AUDIO_RENDERING_START = 10002;
    public static final int MEDIA_INFO_AVFORMAT_TIME = 711;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_FACE_DETECT_INFO = 716;
    public static final int MEDIA_INFO_FRAME_QUEUE_NULL = 713;
    public static final int MEDIA_INFO_HTTPDNS_CONNECT_FAIL = 720;
    public static final int MEDIA_INFO_KEYFRAME_PTS = 704;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    public static final int MEDIA_INFO_NETWORK_SHAKE = 710;
    public static final int MEDIA_INFO_NETWORK_TRAFFIC = 714;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_SEI_USERDEFINED_STRUCT = 715;
    public static final int MEDIA_INFO_SHOULD_SWITCH_TO_LOW_QUALITY_STREAM = 903;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_STREAM_ABNORMAL_ADJOIN = 705;
    public static final int MEDIA_INFO_STREAM_ABNORMAL_AUDIO = 707;
    public static final int MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM = 708;
    public static final int MEDIA_INFO_STREAM_ABNORMAL_VIDEO = 706;
    public static final int MEDIA_INFO_STREAM_ABNORMAL_VIDEO_DTS = 709;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
    public static final int MEDIA_INFO_SWITCH_PATH_SYNC_FRAME = 717;
    public static final int MEDIA_INFO_SWITCH_PATH_SYNC_FRAME_FAIL = 718;
    public static final int MEDIA_INFO_SWITCH_PATH_SYNC_FRAME_SUCCESS = 719;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_VIDEO_CODEC_ID_CHANGE = 10003;
    public static final int MEDIA_INFO_VIDEO_DECODE_ERROR = 712;
    public static final int MEDIA_INFO_VIDEO_MEDIACODEC_DECODE_ERROR = 10004;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    public static final int MEDIA_OUT_OF_BUFFERING = 300;
    public static final int MEDIA_RESUME_BUFFERING = 301;

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i);
    }

    public interface OnCompletionListener {
        void onCompletion(IMediaPlayer iMediaPlayer);
    }

    public interface OnErrorListener {
        boolean onError(IMediaPlayer iMediaPlayer, int i, int i2);
    }

    public interface OnInfoListener {
        boolean onInfo(IMediaPlayer iMediaPlayer, long j, long j2, long j3, Object obj);
    }

    public interface OnLoopCompletionListener {
        void onLoopCompletion(IMediaPlayer iMediaPlayer);
    }

    public interface OnPreparedListener {
        void onPrepared(IMediaPlayer iMediaPlayer);
    }

    public interface OnSeekCompletionListener {
        void onSeekComplete(IMediaPlayer iMediaPlayer);
    }

    public interface OnVideoClickListener {
        void onClick(int i, int i2, int i3, int i4, int i5, String str);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4);
    }

    long getCurrentPosition();

    long getDuration();

    int getVideoHeight();

    int getVideoSarDen();

    int getVideoSarNum();

    int getVideoWidth();

    void instantSeekTo(long j);

    boolean isPlaying();

    void pause() throws IllegalStateException;

    void prepareAsync() throws IllegalStateException, IjkMediaException;

    void release();

    void reset();

    void seekTo(long j) throws IllegalStateException;

    void setDataSource(String str) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDisplay(SurfaceHolder surfaceHolder);

    void setLooping(boolean z);

    void setMuted(boolean z);

    void setPlayRate(float f);

    void setScreenOnWhilePlaying(boolean z);

    void setSurface(Surface surface);

    void setSurfaceSize(int i, int i2);

    void setVolume(float f, float f2);

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;
}
