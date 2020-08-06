package com.zhiping.dev.android.logcat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.taobao.windvane.service.WVEventId;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.atomic.AtomicBoolean;

final class LogcatToast {
    /* access modifiers changed from: private */
    public LinearLayout contentView;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public LogcatToast(final Context tmp) {
        if (tmp == null) {
            throw new RuntimeException("context is null !!!");
        }
        final Runnable initTask = new Runnable() {
            public void run() {
                Context context = tmp.getApplicationContext();
                LinearLayout unused = LogcatToast.this.contentView = new LinearLayout(context);
                LogcatToast.this.contentView.setBackgroundColor(Color.parseColor("#33000000"));
                LogcatToast.this.contentView.setOrientation(1);
                TextView tv2 = new TextView(context);
                tv2.setTextColor(-1);
                tv2.setTextSize(1, 24.0f);
                tv2.setPadding(10, 10, 10, 10);
                tv2.setText("日志功能已开启");
                LogcatToast.this.contentView.addView(tv2);
                TextView tv22 = new TextView(context);
                tv22.setTag("tvMsg");
                tv22.setTextColor(-1);
                tv22.setTextSize(1, 24.0f);
                tv22.setPadding(10, 10, 10, 10);
                tv22.setText("...");
                LogcatToast.this.contentView.addView(tv22);
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            initTask.run();
            return;
        }
        final AtomicBoolean initDoneFlag = new AtomicBoolean(false);
        this.uiHandler.post(new Runnable() {
            public void run() {
                initTask.run();
                initDoneFlag.set(true);
            }
        });
        do {
        } while (!initDoneFlag.get());
    }

    public void show(final String msg) {
        this.uiHandler.post(new Runnable() {
            public void run() {
                WindowManager wm;
                try {
                    TextView tvMsg = (TextView) LogcatToast.this.contentView.findViewWithTag("tvMsg");
                    if (tvMsg == null) {
                        tvMsg = new TextView(LogcatToast.this.contentView.getContext());
                        tvMsg.setTextColor(Color.parseColor("#aaffffff"));
                        tvMsg.setTextSize(1, 24.0f);
                        tvMsg.setPadding(10, 10, 10, 10);
                        LogcatToast.this.contentView.addView(tvMsg);
                    }
                    tvMsg.setText(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!LogcatToast.this.contentView.isShown() && (wm = (WindowManager) LogcatToast.this.contentView.getContext().getSystemService("window")) != null) {
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(400, -2);
                        lp.type = WVEventId.PAGE_onReceivedTitle;
                        lp.format = -2;
                        lp.flags = 264;
                        lp.width = 500;
                        lp.height = 500;
                        lp.gravity = 51;
                        wm.addView(LogcatToast.this.contentView, lp);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    try {
                        Activity act = ZpLogCat.getInstance((Application) null).getAlcb().getTop();
                        if (act != null) {
                            Toast toast = new Toast(act);
                            toast.setView(LogcatToast.this.contentView);
                            toast.setDuration(1);
                            toast.show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void dismiss() {
        this.uiHandler.post(new Runnable() {
            public void run() {
                WindowManager wm;
                if (LogcatToast.this.contentView.isShown() && (wm = (WindowManager) LogcatToast.this.contentView.getContext().getSystemService("window")) != null) {
                    try {
                        wm.removeView(LogcatToast.this.contentView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
