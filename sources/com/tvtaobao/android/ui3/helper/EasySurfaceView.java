package com.tvtaobao.android.ui3.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.tvtaobao.android.ui3.helper.RenderThread;

public class EasySurfaceView extends SurfaceView {
    private static final String TAG = SurfaceView.class.getSimpleName();
    private RenderThread.RenderClient renderClient;
    private RenderThread renderThread;

    public interface FrameMaker {
        void makeFrame(Canvas canvas, long j, long j2);
    }

    public EasySurfaceView(Context context) {
        this(context, (AttributeSet) null);
    }

    public EasySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public RenderThread.RenderClient getRenderClient() {
        return this.renderClient;
    }

    public EasySurfaceView setRenderClient(RenderThread.RenderClient renderClient2) {
        this.renderClient = renderClient2;
        return this;
    }

    public RenderThread getRenderThread() {
        return this.renderThread;
    }

    public EasySurfaceView setRenderThread(RenderThread renderThread2) {
        if (!(this.renderThread == null || this.renderThread == renderThread2)) {
            this.renderThread.unregister(this.renderClient);
        }
        this.renderThread = renderThread2;
        this.renderThread.register(this.renderClient);
        return this;
    }

    public DefaultRenderClient makeDefaultRenderClient(FrameMaker frameMaker) {
        return new DefaultRenderClient(frameMaker);
    }

    public class DefaultRenderClient implements RenderThread.RenderClient {
        private FrameMaker frameMaker;

        public DefaultRenderClient(FrameMaker frameMaker2) {
            this.frameMaker = frameMaker2;
        }

        public void doRender(long frameTime, long frameCount) {
            SurfaceHolder surfaceHolder = EasySurfaceView.this.getHolder();
            if (surfaceHolder != null) {
                surfaceHolder.setFormat(-2);
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    if (this.frameMaker != null) {
                        try {
                            this.frameMaker.makeFrame(canvas, frameCount, System.currentTimeMillis());
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        public void onThreadKilled() {
        }
    }
}
