package com.yunos.tv.core.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.taobao.windvane.service.WVEventId;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.yunos.tv.core.common.DeviceJudge;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public class DeviceStateViewer {
    private static final int FLAG_SHOWING = 1;
    /* access modifiers changed from: private */
    public static final String TAG = DeviceStateViewer.class.getSimpleName();
    private static DeviceStateViewer instance;
    /* access modifiers changed from: private */
    public WeakReference<Context> context;
    private int flag = 0;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public WeakReference<HoverLayout> hoverLayoutWeakReference;
    /* access modifiers changed from: private */
    public Handler mainHandler;
    private Runnable oneSecondTask = new Runnable() {
        public void run() {
            try {
                final String txt = DeviceJudge.getCpuStat() + "\n" + System.currentTimeMillis();
                ZpLogger.i(DeviceStateViewer.TAG, ".oneSecondTask() " + txt);
                DeviceStateViewer.this.mainHandler.post(new Runnable() {
                    public void run() {
                        ((HoverLayout) DeviceStateViewer.this.hoverLayoutWeakReference.get()).textView.setText(txt);
                        ((HoverLayout) DeviceStateViewer.this.hoverLayoutWeakReference.get()).textView.invalidate();
                        ((HoverLayout) DeviceStateViewer.this.hoverLayoutWeakReference.get()).invalidate();
                    }
                });
                DeviceStateViewer.this.handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Thread thread;

    private DeviceStateViewer() {
    }

    private DeviceStateViewer(Context context2) {
        this.context = new WeakReference<>(context2);
        this.thread = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                Handler unused = DeviceStateViewer.this.handler = new Handler(Looper.myLooper());
                Looper.loop();
            }
        });
        this.thread.start();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static DeviceStateViewer get(Context context2) {
        if (instance == null) {
            synchronized (DeviceStateViewer.class) {
                if (instance == null) {
                    instance = new DeviceStateViewer(context2);
                }
            }
        }
        return instance;
    }

    public void show() {
        try {
            if ((this.flag & 1) != 1) {
                this.flag |= 1;
                if (this.hoverLayoutWeakReference == null) {
                    this.hoverLayoutWeakReference = new WeakReference<>(new HoverLayout((Context) this.context.get()));
                }
                WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
                wl.gravity = 51;
                wl.flags = 264;
                wl.type = WVEventId.PAGE_onReceivedTitle;
                wl.width = 400;
                wl.height = -2;
                wl.alpha = 0.8f;
                ((WindowManager) ((Context) this.context.get()).getSystemService("window")).addView((View) this.hoverLayoutWeakReference.get(), wl);
                this.oneSecondTask.run();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        try {
            if ((this.flag & 1) == 1) {
                this.flag &= -2;
                ((WindowManager) ((Context) this.context.get()).getSystemService("window")).removeView((View) this.hoverLayoutWeakReference.get());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            this.flag &= -2;
            WindowManager wm = (WindowManager) ((Context) this.context.get()).getSystemService("window");
            if (this.hoverLayoutWeakReference != null) {
                wm.removeView((View) this.hoverLayoutWeakReference.get());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            this.handler.removeCallbacks(this.oneSecondTask);
            this.handler.getLooper().quit();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        try {
            this.handler = null;
            instance = null;
            this.thread = null;
        } catch (Throwable e3) {
            e3.printStackTrace();
        }
    }

    class HoverLayout extends FrameLayout {
        TextView textView;

        public HoverLayout(@NonNull Context context) {
            super(context);
            init();
        }

        public HoverLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public HoverLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            this.textView = new TextView((Context) DeviceStateViewer.this.context.get());
            this.textView.setTextColor(-1);
            this.textView.setFocusable(false);
            addView(this.textView, new FrameLayout.LayoutParams(-1, -2));
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.argb(33, 0, 0, 0));
            ZpLogger.i(DeviceStateViewer.TAG, "[HoverLayout].onDraw");
        }
    }
}
