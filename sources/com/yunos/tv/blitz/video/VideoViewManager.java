package com.yunos.tv.blitz.video;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.video.data.Config;
import com.yunos.tv.blitz.video.data.LDebug;
import com.yunos.tv.blitz.video.data.VideoItem;
import com.yunos.tv.blitz.video.data.VideoViewInfo;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import com.yunos.tv.tvsdk.media.view.IVideo;
import com.yunos.tv.tvsdk.media.view.YunosVideoView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoViewManager {
    static final int OPER_CODE_HIDE = 6;
    static final int OPER_CODE_PAUSE = 1;
    static final int OPER_CODE_PLAY = 0;
    static final int OPER_CODE_RELEASE = 4;
    static final int OPER_CODE_REPLAY = 5;
    static final int OPER_CODE_RESUME = 2;
    static final int OPER_CODE_SHOW = 7;
    static final int OPER_CODE_STOP = 3;
    private final String TAG = "VideoViewManager";
    private Context mContext;
    /* access modifiers changed from: private */
    public FrameLayout mControllLayout;
    private List<VideoItem> mItems;
    private int mPlayerType = Config.getPlayerType();
    /* access modifiers changed from: private */
    public FrameLayout mVideoLayout;
    /* access modifiers changed from: private */
    public BzVideoView mVideoView;
    private HashMap<String, BzVideoView> mVideoViewMap;
    /* access modifiers changed from: private */
    public Map<String, VideoViewInfo> mViewMap = new HashMap();
    private FrameLayout mWebView;
    private String mXuanjiItemClickFunc;

    private native boolean videoNativeInit();

    private native boolean videoNativeRelease();

    private native void videoNativeResumeContext();

    /* access modifiers changed from: private */
    public native boolean videoSetCurrentState(byte[] bArr, int i);

    public void Init(FrameLayout webView) {
        Log.d("VideoViewManager", "Init");
        this.mWebView = webView;
        this.mContext = webView.getContext();
        this.mVideoLayout = new FrameLayout(this.mContext);
        this.mVideoLayout.setBackgroundColor(Color.parseColor("#00000000"));
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, -1);
        this.mVideoLayout.setLayoutParams(lp);
        webView.addView(this.mVideoLayout, lp);
        this.mVideoViewMap = new HashMap<>();
        this.mVideoViewMap.clear();
        videoNativeInit();
    }

    public BzVideoView getVideoView() {
        return this.mVideoView;
    }

    public boolean isVideoViewExist(byte[] id) {
        if (this.mVideoViewMap.containsKey(Arrays.toString(id))) {
            return true;
        }
        return false;
    }

    public BzVideoView getVideoViewById(byte[] id) {
        if (this.mVideoViewMap.containsKey(Arrays.toString(id))) {
            return this.mVideoViewMap.get(Arrays.toString(id));
        }
        return null;
    }

    public BzVideoView getVideoViewById(String id) {
        if (this.mVideoViewMap.containsKey(id)) {
            return this.mVideoViewMap.get(id);
        }
        return null;
    }

    private void addVideoView(byte[] id, BzVideoView view) {
        this.mVideoViewMap.put(Arrays.toString(id), view);
        Log.d("VideoViewManager", "mVideoViewMap contained key = " + this.mVideoViewMap.containsKey(Arrays.toString(id)));
    }

    /* access modifiers changed from: private */
    public void removeVideoViewInMap(byte[] id) {
        if (this.mVideoViewMap.get(Arrays.toString(id)) != null) {
            this.mVideoViewMap.remove(Arrays.toString(id));
        }
    }

    public void initControllLayout() {
        if (this.mControllLayout == null) {
            this.mControllLayout = new FrameLayout(this.mContext);
            this.mControllLayout.setBackgroundColor(Color.parseColor("#00000000"));
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, -1);
            this.mControllLayout.setLayoutParams(lp);
            this.mWebView.addView(this.mControllLayout, lp);
        }
    }

    public void initVideo(byte[] id) {
        LDebug.i("VideoViewManager", "initVideo id=" + id + ", mPlayerType=" + this.mPlayerType);
        if (!isVideoViewExist(id)) {
            this.mVideoView = new BzVideoView(this.mContext);
            Log.d("VideoViewManager", "mVideoView new 1");
            this.mVideoView.setWebViewId(id);
            this.mVideoView.setDimensionFull();
            this.mVideoView.setBackgroundColor(Color.parseColor("#000000"));
            this.mVideoView.setVideoViewType(0);
            this.mVideoView.setMediaPlayerType(this.mPlayerType);
            FrameLayout.LayoutParams videolp = new FrameLayout.LayoutParams(-2, -2);
            this.mVideoView.setLayoutParams(videolp);
            this.mVideoLayout.addView(this.mVideoView, 0, videolp);
            addVideoView(id, this.mVideoView);
        }
        if (this.mControllLayout == null) {
            initControllLayout();
        }
        this.mControllLayout.bringToFront();
        LDebug.i("VideoViewManager", "VideoViewManager.intiVideo id=" + id + ", id=" + Arrays.toString(id));
        this.mVideoView.setFullScreenChangedListener(new YunosVideoView.FullScreenChangedListener() {
            public void onBeforeUnFullScreen() {
            }

            public void onBeforeFullScreen() {
            }

            public void onAfterUnFullScreen() {
                LDebug.i("VideoViewManager", "VideoViewManager.FullScreenChangedListener.onAfterUnFullScreen");
                VideoViewManager.this.mControllLayout.bringToFront();
            }

            public void onAfterFullScreen() {
                LDebug.i("VideoViewManager", "VideoViewManager.FullScreenChangedListener.onAfterFullScreen key=" + Arrays.toString(VideoViewManager.this.mVideoView.getWebViewId()));
            }
        });
        this.mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            public boolean onError(Object mp, int what, int extra) {
                LDebug.w("VideoViewManager", "VideoViewManager.intiVideo.OnErrorListener mp=" + mp + ", what=" + what + ", extra=" + extra);
                boolean unused = VideoViewManager.this.videoSetCurrentState(VideoViewManager.this.mVideoView.getWebViewId(), -1);
                if (what != 300) {
                    return false;
                }
                boolean unused2 = VideoViewManager.this.videoSetCurrentState(VideoViewManager.this.mVideoView.getWebViewId(), 14);
                return false;
            }
        });
        this.mVideoView.setOnVideoStateChangeListener(new IVideo.VideoStateChangeListener() {
            public void onStateChange(int state) {
                Log.d("VideoViewManager", "onStateChange =" + state);
                boolean unused = VideoViewManager.this.videoSetCurrentState(VideoViewManager.this.mVideoView.getWebViewId(), state);
            }
        });
        this.mVideoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete() {
                boolean unused = VideoViewManager.this.videoSetCurrentState(VideoViewManager.this.mVideoView.getWebViewId(), BzVideoView.EVENT_SEEK_COMPLETE);
            }
        });
        this.mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(Object mp) {
            }
        });
        this.mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(Object mp) {
                BzDebugLog.d("VideoViewManager", "onCompletion mp" + mp.toString());
            }
        });
    }

    public void pause(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.pause();
        }
    }

    public void stop(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.stop();
        }
    }

    public void stop(String id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.stop();
        }
    }

    public void rePlay(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.rePlay();
        }
    }

    public void rePlay(boolean seek, byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.rePlay(seek);
        }
    }

    public void clear(byte[] id) {
        LDebug.i("VideoViewManager", "clear");
        if (BzVideoView.mWakeLock != null) {
            if (BzVideoView.mWakeLock.isHeld()) {
                BzVideoView.mWakeLock.release();
            }
            BzVideoView.mWakeLock = null;
        }
        if (getVideoViewById(id) != null) {
            stop(id);
            Log.d("VideoViewManager", "mVideoView null 1");
            removeVideoViewInMap(id);
        }
        if (this.mVideoLayout != null) {
            this.mVideoLayout.removeAllViews();
            this.mVideoLayout = null;
        }
        if (this.mViewMap != null) {
            this.mViewMap.clear();
        }
        if (this.mItems != null) {
            this.mItems.clear();
        }
    }

    public void destroy() {
        LDebug.i("VideoViewManager", "destroy");
        if (BzVideoView.mWakeLock != null) {
            if (BzVideoView.mWakeLock.isHeld()) {
                BzVideoView.mWakeLock.release();
            }
            BzVideoView.mWakeLock = null;
        }
        for (String key : this.mVideoViewMap.keySet()) {
            BzVideoView view = this.mVideoViewMap.get(key);
            if (view != null) {
                stop(key);
                view.release();
                Log.d("VideoViewManager", "mVideoView null 2");
            }
        }
        this.mVideoViewMap.clear();
        if (this.mViewMap != null) {
            this.mViewMap.clear();
        }
        if (this.mItems != null) {
            this.mItems.clear();
            this.mItems = null;
        }
        videoNativeRelease();
        this.mVideoLayout = null;
        this.mControllLayout = null;
        this.mWebView = null;
        this.mContext = null;
    }

    public void destroy(byte[] id) {
        LDebug.i("VideoViewManager", "destroy");
        if (BzVideoView.mWakeLock != null) {
            if (BzVideoView.mWakeLock.isHeld()) {
                BzVideoView.mWakeLock.release();
            }
            BzVideoView.mWakeLock = null;
        }
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            stop(id);
            view.release();
            Log.d("VideoViewManager", "mVideoView null 2");
            removeVideoViewInMap(id);
        }
        if (this.mViewMap != null) {
            this.mViewMap.clear();
        }
        if (this.mItems != null) {
            this.mItems.clear();
            this.mItems = null;
        }
        videoNativeRelease();
        this.mVideoLayout = null;
        this.mControllLayout = null;
        this.mWebView = null;
        this.mContext = null;
    }

    public void play(VideoItem item, Integer position, byte[] id) {
        LDebug.i("VideoViewManager", "VideoViewManager.play position=" + position + ", item=" + item);
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            if (view.isPlaying()) {
                view.stop();
            }
            view.play(item);
        }
    }

    public void AddVideoView(byte[] id, float x, float y, float width, float height) {
        LDebug.i("VideoViewManager", "VideoViewManager.AddVideoView id=" + Arrays.toString(id) + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
        if (this.mVideoLayout != null) {
            final byte[] bArr = id;
            final float f = x;
            final float f2 = y;
            final float f3 = width;
            final float f4 = height;
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    if (!VideoViewManager.this.isVideoViewExist(bArr)) {
                        VideoViewManager.this.initVideo(bArr);
                    }
                    VideoViewManager.this.mViewMap.put(Arrays.toString(bArr), new VideoViewInfo(VideoViewManager.this.getLayoutParams(f, f2, f3, f4)));
                }
            });
        }
    }

    public void SetVideoPath(final byte[] id, final String path) {
        Log.d("VideoViewManager", "SetVideoPath id" + Arrays.toString(id) + "path:" + path);
        if (this.mVideoLayout == null || path == null || path.isEmpty()) {
            Log.w("VideoViewManager", "videolayout not init return");
            return;
        }
        this.mVideoLayout.post(new Runnable() {
            public void run() {
                Log.d("VideoViewManager", "call SetVideoPath runnable");
                BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                if (view != null) {
                    if (view.isPlaying()) {
                        view.stopPlayback();
                    }
                    LDebug.i("VideoViewManager", "VideoViewManager.SetVideoPath path=" + path);
                    VideoItem item = new VideoItem();
                    item.setVideo(path);
                    VideoViewManager.this.play(item, (Integer) null, id);
                    view.setWebViewId(id);
                    VideoViewInfo info = (VideoViewInfo) VideoViewManager.this.mViewMap.get(Arrays.toString(id));
                    if (info != null) {
                        info.setVideoItem(item);
                    }
                    if (info != null && !view.isFullScreen()) {
                        view.setLayoutParams(info.getLayoutParams());
                        return;
                    }
                    return;
                }
                Log.d("VideoViewManager", "mVideoView is null");
            }
        });
    }

    public void SetAutoPlay(byte[] id, boolean autoPlay) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                }
            });
        }
    }

    public void SetLoop(byte[] id, boolean loop) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                }
            });
        }
    }

    public void Pause(final byte[] id) {
        LDebug.i("in Java", "Pause id=" + Arrays.toString(id));
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.pause();
                    }
                }
            });
        }
    }

    public void Hide(final byte[] id) {
        LDebug.i("in Java", "hide id=" + Arrays.toString(id));
        this.mVideoLayout.post(new Runnable() {
            public void run() {
                BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                if (view != null) {
                    view.setVideoViewInvisible();
                }
            }
        });
    }

    public void Show(final byte[] id) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.setVideoViewVisible();
                    }
                }
            });
        }
    }

    public void Resume(final byte[] id) {
        LDebug.i("in Java", "Resume id=" + Arrays.toString(id));
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    VideoViewManager.this.rePlay(id);
                }
            });
        }
    }

    public void Play(final byte[] id) {
        LDebug.i("in Java", "play --->");
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        if (Arrays.toString(id).equals(Arrays.toString(view.getWebViewId()))) {
                            LDebug.i("VideoViewManager", "VideoViewManager.Play id=" + Arrays.toString(id) + "!=" + Arrays.toString(view.getWebViewId()));
                        }
                        LDebug.i("VideoViewManager", "VideoViewManager.Play id=" + Arrays.toString(id));
                        VideoViewInfo info = (VideoViewInfo) VideoViewManager.this.mViewMap.get(Arrays.toString(id));
                        LDebug.i("VideoViewManager", "VideoViewManager.Play id=" + Arrays.toString(id) + "path:" + info.getVideoItem().getVideo());
                        VideoViewManager.this.play(info.getVideoItem(), (Integer) null, id);
                        if (view != null) {
                            view.setVideoViewVisible();
                        }
                    }
                }
            });
        }
    }

    public void Stop(final byte[] id) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.stopPlayback();
                    }
                }
            });
        }
    }

    public int GetDuration(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            return view.getDuration();
        }
        return 0;
    }

    public int GetCurrentPosition(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view == null) {
            return 0;
        }
        try {
            return view.getCurrentPosition();
        } catch (IllegalStateException e) {
            LDebug.i("VideoViewManager", "IllegalStateException in GetCurrentPosition");
            return 0;
        }
    }

    public boolean IsPlaying(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            return view.isPlaying();
        }
        return false;
    }

    public boolean IsPause(byte[] id) {
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            return view.isPause();
        }
        return false;
    }

    public void Fullscreen(byte[] id) {
        LDebug.i("VideoViewManager", "VideoViewManager.Fullscreen id=" + id);
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            view.fullScreen();
        }
    }

    public void ToggleScreen(byte[] id) {
        LDebug.i("VideoViewManager", "VideoViewManager.ToggleScreen id=" + id);
        BzVideoView view = getVideoViewById(id);
        if (view != null) {
            LDebug.i("VideoViewManager", "VideoViewManager.ToggleScreen2 id=" + id);
            view.toggleScreen();
        }
    }

    public void SetCurrentPosition(final byte[] id, final int pos) {
        LDebug.i("in Java", "SetCurrentPosition --->" + pos);
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.setLastPosition(pos);
                        view.seekTo(pos);
                    }
                }
            });
        }
    }

    public void setAlpha(final byte[] id, final float alpha) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.setAlpha(alpha);
                    }
                }
            });
        }
    }

    public void setEnable(final byte[] id, final boolean isEnable) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null) {
                        view.setEnabled(isEnable);
                    }
                }
            });
        }
    }

    public void RemoveVideoView(final byte[] id) {
        if (this.mVideoLayout == null) {
            LDebug.w("VideoViewManager", "VideoViewManager.RemoveVideoView RemoveVideoView is null");
        } else {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                    if (view != null && view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                        VideoViewManager.this.removeVideoViewInMap(id);
                        Log.d("VideoViewManager", "mVideoView null 3");
                    }
                }
            });
        }
    }

    public void ResizeViewTo(final byte[] id, final float width, final float height) {
        LDebug.i("VideoViewManager", "VideoViewManager.ResizeViewTo id=" + id + ", width=" + width + ", heith=" + height);
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    String key = Arrays.toString(id);
                    VideoViewInfo info = (VideoViewInfo) VideoViewManager.this.mViewMap.get(key);
                    if (info != null) {
                        int swidth = (int) (((float) VideoViewManager.this.mVideoLayout.getWidth()) * width);
                        int sheight = (int) (((float) VideoViewManager.this.mVideoLayout.getHeight()) * height);
                        FrameLayout.LayoutParams lp = info.getLayoutParams();
                        if (lp == null) {
                            lp = new FrameLayout.LayoutParams(swidth, sheight);
                        } else {
                            lp.width = swidth;
                            lp.height = sheight;
                        }
                        info.setLayoutParams(lp);
                        VideoViewManager.this.mViewMap.put(key, info);
                        BzVideoView view = VideoViewManager.this.getVideoViewById(id);
                        if (view != null && key.equals(Arrays.toString(view.getWebViewId())) && !view.isFullScreen()) {
                            LDebug.i("VideoViewManager", "VideoViewManager.ResizeViewTo setLayoutParams lp=[" + lp.leftMargin + "," + lp.topMargin + "," + lp.rightMargin + "," + lp.bottomMargin + "][" + lp.width + ", " + lp.height + "]");
                            view.setLayoutParams(lp);
                        }
                    }
                }
            });
        }
    }

    public void MoveViewTo(byte[] id, float x, float y, float width, float height) {
        if (this.mVideoLayout != null) {
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                }
            });
        }
    }

    public void SetViewRect(byte[] id, float x, float y, float width, float height) {
        LDebug.i("VideoViewManager", "VideoViewManager.SetViewRect id=" + Arrays.toString(id) + ", width=" + width + ", heith=" + height);
        if (this.mVideoLayout != null) {
            final float f = x;
            final float f2 = y;
            final float f3 = width;
            final float f4 = height;
            final byte[] bArr = id;
            this.mVideoLayout.post(new Runnable() {
                public void run() {
                    FrameLayout.LayoutParams lp = VideoViewManager.this.getLayoutParams(f, f2, f3, f4);
                    String key = Arrays.toString(bArr);
                    VideoViewInfo info = (VideoViewInfo) VideoViewManager.this.mViewMap.get(key);
                    if (info == null) {
                        info = new VideoViewInfo(lp);
                    } else {
                        info.setLayoutParams(lp);
                    }
                    VideoViewManager.this.mViewMap.put(key, info);
                    BzVideoView view = VideoViewManager.this.getVideoViewById(bArr);
                    if (view != null && view.getWebViewId() != null && !view.isFullScreen() && key.equals(Arrays.toString(view.getWebViewId()))) {
                        LDebug.i("VideoViewManager", "VideoViewManager.SetViewRect setLayoutParams lp=[" + info.getLayoutParams().leftMargin + "," + info.getLayoutParams().topMargin + "," + info.getLayoutParams().rightMargin + "," + info.getLayoutParams().bottomMargin + "][" + info.getLayoutParams().width + ", " + info.getLayoutParams().height + "]");
                        view.setLayoutParams(info.getLayoutParams());
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public FrameLayout.LayoutParams getLayoutParams(float x, float y, float width, float height) {
        int swidth = (int) (((float) this.mVideoLayout.getWidth()) * width);
        int sheight = (int) (((float) this.mVideoLayout.getHeight()) * height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(swidth, sheight);
        lp.width = swidth;
        lp.height = sheight;
        lp.leftMargin = Math.round(((float) this.mVideoLayout.getWidth()) * x);
        lp.topMargin = Math.round(((float) this.mVideoLayout.getHeight()) * y);
        return lp;
    }

    private List<VideoItem> buildMenuInfoFromJson(String menuInfo) {
        if (menuInfo == null) {
            return null;
        }
        List<VideoItem> videos = null;
        try {
            JSONArray menujson = new JSONObject(menuInfo).optJSONArray("data");
            if (menujson == null) {
                return null;
            }
            List<VideoItem> videos2 = new ArrayList<>();
            try {
                int count = menujson.length();
                for (int i = 0; i < count; i++) {
                    VideoItem item = VideoItem.fromJson(menujson.getJSONObject(i));
                    if (item != null) {
                        videos2.add(item);
                    }
                }
                return videos2;
            } catch (JSONException e) {
                e = e;
                videos = videos2;
                e.printStackTrace();
                return videos;
            }
        } catch (JSONException e2) {
            e = e2;
            e.printStackTrace();
            return videos;
        }
    }

    /* access modifiers changed from: package-private */
    public void onOperationComm(byte[] id, int operCode) {
        LDebug.i("VideoViewManager", "VideoViewManageronOperationComm id=" + Arrays.toString(id) + "opercode:" + operCode);
        switch (operCode) {
            case 0:
                Play(id);
                return;
            case 1:
                Pause(id);
                return;
            case 2:
                Resume(id);
                return;
            case 3:
                Stop(id);
                return;
            case 4:
                RemoveVideoView(id);
                return;
            case 5:
                rePlay(id);
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                return;
        }
        Hide(id);
        Show(id);
    }

    public void java2cResumeContext() {
        videoNativeResumeContext();
    }
}
