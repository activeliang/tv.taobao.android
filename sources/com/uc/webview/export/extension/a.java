package com.uc.webview.export.extension;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import java.util.Map;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
final class a extends Thread {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ Callable c;
    final /* synthetic */ Map d;

    a(Context context, String str, Callable callable, Map map) {
        this.a = context;
        this.b = str;
        this.c = callable;
        this.d = map;
    }

    public final void run() {
        while (!((Boolean) d.a(10010, new Object[0])).booleanValue()) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
        if (((Boolean) d.a(10003, 1L)).booleanValue()) {
            Log.i("tag_test_log", "force system webview, don't download uc player");
            return;
        }
        if (((Boolean) d.a(10003, Long.valueOf(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED))).booleanValue()) {
            Log.i("tag_test_log", "use ucmobile apollo, don't download uc player");
        } else if (!d.q) {
            Log.i("tag_test_log", "sUseUCPlayer is false, don't download uc player");
        } else {
            try {
                UCCore.a(this.a, this.b, this.c, this.d);
            } catch (Throwable th) {
            }
        }
    }
}
