package com.uc.webview.export.extension;

import android.support.v4.media.session.PlaybackStateCompat;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class b implements ValueCallback<UpdateTask> {
    b() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        IWaStat.WaStat.stat(IWaStat.VIDEO_DOWNLOAD);
        d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID));
    }
}
