package com.uc.webview.export.internal.utility;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import java.lang.reflect.Method;

/* compiled from: ProGuard */
public final class a extends BroadcastReceiver {
    private static String c = "ScreenObserver";
    private static Method d;
    public Context a;
    public C0010a b;

    /* renamed from: com.uc.webview.export.internal.utility.a$a  reason: collision with other inner class name */
    /* compiled from: ProGuard */
    public interface C0010a {
        void a();

        void b();

        void c();
    }

    public a(Context context) {
        this.a = context.getApplicationContext();
        try {
            d = PowerManager.class.getMethod("isScreenOn", new Class[0]);
        } catch (Exception e) {
            new StringBuilder("API < 7,").append(e);
        }
    }

    public final void onReceive(Context context, Intent intent) {
        if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            this.b.a();
        } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
            this.b.b();
        } else if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
            this.b.c();
        }
    }

    public static boolean a(PowerManager powerManager) {
        try {
            return ((Boolean) d.invoke(powerManager, new Object[0])).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean a(Context context) {
        return ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }
}
