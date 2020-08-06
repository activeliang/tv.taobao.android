package com.amap.api.location;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.loc.en;
import com.loc.f;

public class APSService extends Service {
    f a;
    int b = 0;
    boolean c = false;

    public IBinder onBind(Intent intent) {
        try {
            return this.a.a(intent);
        } catch (Throwable th) {
            en.a(th, "APSService", "onBind");
            return null;
        }
    }

    public void onCreate() {
        onCreate(this);
    }

    public void onCreate(Context context) {
        try {
            if (this.a == null) {
                this.a = new f(context);
            }
        } catch (Throwable th) {
        }
        try {
            if (this.a == null) {
                this.a = new f(context);
            }
            this.a.a();
        } catch (Throwable th2) {
            en.a(th2, "APSService", "onCreate");
        }
        super.onCreate();
    }

    public void onDestroy() {
        try {
            this.a.b();
            if (this.c) {
                stopForeground(true);
            }
        } catch (Throwable th) {
            en.a(th, "APSService", "onDestroy");
        }
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                int intExtra = intent.getIntExtra("g", 0);
                if (intExtra == 1) {
                    int intExtra2 = intent.getIntExtra(UploadQueueMgr.MSGTYPE_INTERVAL, 0);
                    Notification notification = (Notification) intent.getParcelableExtra("h");
                    if (!(intExtra2 == 0 || notification == null)) {
                        startForeground(intExtra2, notification);
                        this.c = true;
                        this.b++;
                    }
                } else if (intExtra == 2) {
                    if (intent.getBooleanExtra("j", true) && this.b > 0) {
                        this.b--;
                    }
                    if (this.b <= 0) {
                        stopForeground(true);
                        this.c = false;
                    } else {
                        stopForeground(false);
                    }
                }
            } catch (Throwable th) {
            }
        }
        return 0;
    }
}
