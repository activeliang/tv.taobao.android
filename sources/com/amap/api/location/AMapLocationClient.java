package com.amap.api.location;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;
import com.bumptech.glide.BuildConfig;
import com.loc.d;
import com.loc.en;
import com.loc.er;
import com.loc.n;
import org.json.JSONObject;

public class AMapLocationClient {
    Context a;
    d b;

    public AMapLocationClient(Context context) {
        if (context == null) {
            try {
                throw new IllegalArgumentException("Context参数不能为null");
            } catch (Throwable th) {
                en.a(th, "AMapLocationClient", "AMapLocationClient 1");
            }
        } else {
            this.a = context.getApplicationContext();
            this.b = a(this.a, (Intent) null);
        }
    }

    public AMapLocationClient(Context context, Intent intent) {
        if (context == null) {
            try {
                throw new IllegalArgumentException("Context参数不能为null");
            } catch (Throwable th) {
                en.a(th, "AMapLocationClient", "AMapLocationClient 2");
            }
        } else {
            this.a = context.getApplicationContext();
            this.b = a(this.a, intent);
        }
    }

    private static d a(Context context, Intent intent) {
        return new d(context, intent);
    }

    public static String getDeviceId(Context context) {
        return n.y(context);
    }

    public static void setApiKey(String str) {
        try {
            AMapLocationClientOption.a = str;
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "setApiKey");
        }
    }

    public void disableBackgroundLocation(boolean z) {
        try {
            if (this.b != null) {
                this.b.a(z);
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "disableBackgroundLocation");
        }
    }

    public void enableBackgroundLocation(int i, Notification notification) {
        try {
            if (this.b != null) {
                this.b.a(i, notification);
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "enableBackgroundLocation");
        }
    }

    public AMapLocation getLastKnownLocation() {
        try {
            if (this.b != null) {
                return this.b.e();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "getLastKnownLocation");
        }
        return null;
    }

    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isStarted() {
        try {
            if (this.b != null) {
                return this.b.a();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "isStarted");
        }
        return false;
    }

    public void onDestroy() {
        try {
            if (this.b != null) {
                this.b.d();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "onDestroy");
        }
    }

    public void setLocationListener(AMapLocationListener aMapLocationListener) {
        if (aMapLocationListener == null) {
            try {
                throw new IllegalArgumentException("listener参数不能为null");
            } catch (Throwable th) {
                en.a(th, "AMapLocationClient", "setLocationListener");
            }
        } else if (this.b != null) {
            this.b.a(aMapLocationListener);
        }
    }

    public void setLocationOption(AMapLocationClientOption aMapLocationClientOption) {
        if (aMapLocationClientOption == null) {
            try {
                throw new IllegalArgumentException("LocationManagerOption参数不能为null");
            } catch (Throwable th) {
                en.a(th, "AMapLocationClient", "setLocationOption");
            }
        } else {
            if (this.b != null) {
                this.b.a(aMapLocationClientOption);
            }
            if (aMapLocationClientOption.b) {
                aMapLocationClientOption.b = false;
                JSONObject jSONObject = new JSONObject();
                if (!TextUtils.isEmpty(aMapLocationClientOption.c)) {
                    jSONObject.put("amap_loc_scenes_type", aMapLocationClientOption.c);
                }
                er.a(this.a, "O019", jSONObject);
            }
        }
    }

    public void startAssistantLocation() {
        try {
            if (this.b != null) {
                this.b.f();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "startAssistantLocation");
        }
    }

    public void startAssistantLocation(WebView webView) {
        try {
            if (this.b != null) {
                this.b.a(webView);
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "startAssistantLocation1");
        }
    }

    public void startLocation() {
        try {
            if (this.b != null) {
                this.b.b();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "startLocation");
        }
    }

    public void stopAssistantLocation() {
        try {
            if (this.b != null) {
                this.b.g();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "stopAssistantLocation");
        }
    }

    public void stopLocation() {
        try {
            if (this.b != null) {
                this.b.c();
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "stopLocation");
        }
    }

    public void unRegisterLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            if (this.b != null) {
                this.b.b(aMapLocationListener);
            }
        } catch (Throwable th) {
            en.a(th, "AMapLocationClient", "unRegisterLocationListener");
        }
    }
}
