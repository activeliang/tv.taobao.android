package com.yunos.tv.blitz.performance;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.service.WVEventId;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.performance.IPerformanceService;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.json.JSONObject;

public class PerformanceService extends Service {
    private static final String TAG = "PerformanceService";
    private long cpu_idletime = 0;
    private long cpu_usetime = 0;
    private float currentCPUUsage = 0.0f;
    private long current_process_usetime = 0;
    private IPerformanceService.Stub mBinder = new IPerformanceService.Stub() {
        public PerformanceInfo getPerformanceInfo() {
            return PerformanceService.this.getPFInfo();
        }

        public HashMap<String, String> getVersionInfo() {
            HashMap<String, String> hashMap = new HashMap<>();
            String lightcoreVersion = "unknown";
            String sdkVersion = "unknown";
            try {
                JSONObject versionJson = new JSONObject(PerformanceService.this.getSharedPreferences("blitz_version", 0).getString("version", ""));
                lightcoreVersion = versionJson.getString("lightcore");
                sdkVersion = versionJson.getString("sdk");
            } catch (Exception e) {
                BzDebugLog.i(PerformanceService.TAG, "getVersionInfo failed: " + e.toString());
            }
            hashMap.put("lightcore", lightcoreVersion);
            hashMap.put("sdk", sdkVersion);
            String appVersionName = "unknown";
            String appVersionCode = "unknown";
            try {
                PackageInfo packageInfo = BzAppConfig.context.getContext().getApplicationContext().getPackageManager().getPackageInfo(PerformanceService.this.getPackageName(), 0);
                appVersionCode = Integer.toString(packageInfo.versionCode);
                appVersionName = packageInfo.versionName;
            } catch (Exception e2) {
                BzDebugLog.i(PerformanceService.TAG, "getAppInfo failed: " + e2.toString());
            }
            hashMap.put("appVersionName", appVersionName);
            hashMap.put("appVersionCode", appVersionCode);
            return hashMap;
        }

        public void registerCallback(IPerformanceCallback callback) {
            IPerformanceCallback unused = PerformanceService.this.mPerformanceCallback = callback;
        }

        public void unregisterCallback() {
            IPerformanceCallback unused = PerformanceService.this.mPerformanceCallback = null;
        }
    };
    /* access modifiers changed from: private */
    public IPerformanceCallback mPerformanceCallback = null;
    private WindowManager.LayoutParams params;
    private int statusBarHeight;
    private SurfaceView sv;
    private float totalCPUUsage = 0.0f;
    private boolean viewAdded = false;
    private WindowManager wm;

    /* access modifiers changed from: private */
    public PerformanceInfo getPFInfo() {
        PerformanceInfo info = new PerformanceInfo();
        info.loadTime = (int) BzAppConfig.context.getPageLoadTime();
        info.dalvMaxSize = (int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        info.dalvUsedSize = (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        Debug.MemoryInfo utInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(utInfo);
        info.dalvikPrivateDirty = utInfo.dalvikPrivateDirty;
        info.dalvikPss = utInfo.dalvikPss;
        info.dalvikSharedDirty = utInfo.dalvikSharedDirty;
        info.nativePrivateDirty = utInfo.nativePrivateDirty;
        info.nativePss = utInfo.nativePss;
        info.nativeSharedDirty = utInfo.nativeSharedDirty;
        info.otherPss = utInfo.otherPss;
        info.otherPrivateDirty = utInfo.otherPrivateDirty;
        info.otherSharedDirty = utInfo.otherSharedDirty;
        info.totalPss = utInfo.getTotalPss();
        info.currentCpu = this.currentCPUUsage;
        info.totalCpu = this.totalCPUUsage;
        try {
            info.fps = Double.parseDouble(getSharedPreferences("blitz_fps", 0).getString("fps_count", "0"));
        } catch (NumberFormatException e) {
            info.fps = ClientTraceData.b.f47a;
        }
        return info;
    }

    private void updateCpuTimes() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", UploadQueueMgr.MSGTYPE_REALTIME);
            String[] toks = reader.readLine().split(" ");
            this.cpu_idletime = Long.parseLong(toks[5]);
            this.cpu_usetime = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
            reader.close();
            RandomAccessFile reader2 = new RandomAccessFile("/proc/" + Process.myPid() + "/stat", UploadQueueMgr.MSGTYPE_REALTIME);
            String[] toks2 = reader2.readLine().split(" ");
            this.current_process_usetime = Long.parseLong(toks2[13]) + Long.parseLong(toks2[14]) + Long.parseLong(toks2[15]) + Long.parseLong(toks2[16]);
            reader2.close();
        } catch (Exception e) {
            BzDebugLog.v(TAG, e.toString());
        }
    }

    /* access modifiers changed from: private */
    public void updateCPUUsage() {
        long last_usetime = this.cpu_usetime;
        long last_idletime = this.cpu_idletime;
        long last_process_usetime = this.current_process_usetime;
        updateCpuTimes();
        long totalCPUTime = (this.cpu_usetime - last_usetime) + Math.max(1, this.cpu_idletime - last_idletime);
        this.totalCPUUsage = ((float) (this.cpu_usetime - last_usetime)) / ((float) totalCPUTime);
        this.currentCPUUsage = ((float) (this.current_process_usetime - last_process_usetime)) / ((float) totalCPUTime);
    }

    public IBinder onBind(Intent it) {
        refresh();
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        this.sv = new SurfaceView(this);
        this.sv.setMinimumWidth(50);
        this.sv.setMinimumHeight(80);
        this.sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            private DrawThread dt;

            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean unused = this.dt.isRun = false;
            }

            public void surfaceCreated(SurfaceHolder holder) {
                this.dt = new DrawThread(holder);
                boolean unused = this.dt.isRun = true;
                this.dt.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            /* renamed from: com.yunos.tv.blitz.performance.PerformanceService$2$DrawThread */
            class DrawThread extends Thread {
                private SurfaceHolder holder;
                /* access modifiers changed from: private */
                public boolean isRun = true;

                public DrawThread(SurfaceHolder h) {
                    this.holder = h;
                }

                public void run() {
                    this.holder.setFormat(-3);
                    Paint p = new Paint(1);
                    p.setARGB(255, 255, 0, 0);
                    p.setTextSize(10.0f);
                    PerformanceService.this.updateCPUUsage();
                    while (this.isRun) {
                        Canvas c = null;
                        try {
                            Thread.sleep(AbstractClientManager.BIND_SERVICE_TIMEOUT);
                            synchronized (this.holder) {
                                c = this.holder.lockCanvas();
                                drawMemory(c, p);
                                drawFPS(c, p);
                                drawLoadTime(c, p);
                                PerformanceService.this.updateCPUUsage();
                                if (PerformanceService.this.mPerformanceCallback != null) {
                                    try {
                                        PerformanceService.this.mPerformanceCallback.onPerfomanceData(PerformanceService.this.getPFInfo());
                                    } catch (DeadObjectException e) {
                                        IPerformanceCallback unused = PerformanceService.this.mPerformanceCallback = null;
                                    }
                                }
                            }
                            if (c != null) {
                                this.holder.unlockCanvasAndPost(c);
                            }
                        } catch (Exception e2) {
                            try {
                                e2.printStackTrace();
                            } finally {
                                if (c != null) {
                                    this.holder.unlockCanvasAndPost(c);
                                }
                            }
                        }
                    }
                }

                private void drawLoadTime(Canvas c, Paint p) {
                    p.setColor(-16711681);
                    long time = BzAppConfig.context.getPageLoadTime();
                    if (time > 3000) {
                        p.setColor(SupportMenu.CATEGORY_MASK);
                    }
                    c.drawText("load: " + (time > 0 ? String.valueOf(time) + "ms" : "unloaded"), 10.0f, 10.0f, p);
                }

                private void drawFPS(Canvas c, Paint p) {
                    String fps_count = PerformanceService.this.getSharedPreferences("blitz_fps", 0).getString("fps_count", "0");
                    p.setColor(-16711681);
                    Double fps = Double.valueOf(ClientTraceData.b.f47a);
                    try {
                        fps = Double.valueOf(Double.parseDouble(fps_count));
                    } catch (NumberFormatException e) {
                    }
                    if (fps.doubleValue() < 30.0d && fps.doubleValue() > ClientTraceData.b.f47a) {
                        p.setColor(SupportMenu.CATEGORY_MASK);
                    }
                    c.drawText("fps: " + fps_count, 10.0f, 25.0f, p);
                }

                private void drawMemory(Canvas c, Paint p) {
                    p.setColor(-16711681);
                    int leftSize = ((int) ((Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) - ((int) (((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
                    c.drawColor(0, PorterDuff.Mode.CLEAR);
                    c.drawText("pss: " + String.format("%.2f", new Object[]{Double.valueOf(getPss())}) + "MB", 10.0f, 40.0f, p);
                    if (leftSize < 10) {
                        p.setColor(SupportMenu.CATEGORY_MASK);
                    }
                    c.drawText("DavLeft: " + leftSize + "MB", 10.0f, 55.0f, p);
                }

                private double getPss() {
                    Debug.MemoryInfo utInfo = new Debug.MemoryInfo();
                    Debug.getMemoryInfo(utInfo);
                    return ((double) utInfo.getTotalPss()) / 1024.0d;
                }
            }
        });
        this.wm = (WindowManager) getSystemService("window");
        this.params = new WindowManager.LayoutParams();
        this.params.width = -2;
        this.params.height = -2;
        this.params.type = WVEventId.PAGE_onCloseWindow;
        this.params.flags = 40;
        this.params.gravity = 8388659;
    }

    /* access modifiers changed from: protected */
    public void refreshView(int x, int y) {
        if (this.statusBarHeight == 0) {
            View rootView = this.sv.getRootView();
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            this.statusBarHeight = r.top;
        }
        this.params.x = x;
        this.params.y = y - this.statusBarHeight;
        refresh();
    }

    private void refresh() {
        if (this.viewAdded) {
            this.wm.updateViewLayout(this.sv, this.params);
            return;
        }
        this.wm.addView(this.sv, this.params);
        this.viewAdded = true;
    }

    private void removeView() {
        if (this.viewAdded) {
            this.wm.removeView(this.sv);
            this.viewAdded = false;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("blitz_fps", 3).edit();
        editor.clear();
        editor.apply();
        BzDebugLog.v("floating window", "on destroy");
        removeView();
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        BzDebugLog.v("floating window", "onstart");
        refresh();
    }
}
