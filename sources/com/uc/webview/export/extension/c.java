package com.uc.webview.export.extension;

import android.support.v4.media.session.PlaybackStateCompat;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: ProGuard */
final class c implements ValueCallback<Object[]> {
    c() {
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        Object[] objArr = (Object[]) obj;
        switch (((Integer) objArr[0]).intValue()) {
            case 1:
                d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
                return;
            case 2:
                d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
                return;
            case 3:
                d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM));
                return;
            case 4:
                d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE));
                return;
            case 5:
                d.a(10001, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH));
                return;
            case 6:
                d.v.put(IWaStat.VIDEO_ERROR_CODE_UPDATE_CHECK_REQUEST, (Integer) objArr[1]);
                return;
            case 7:
                d.v.put(IWaStat.VIDEO_ERROR_CODE_DOWNLOAD, (Integer) objArr[1]);
                return;
            case 8:
                d.v.put(IWaStat.VIDEO_ERROR_CODE_VERIFY, (Integer) objArr[1]);
                return;
            case 9:
                d.v.put(IWaStat.VIDEO_ERROR_CODE_UNZIP, (Integer) objArr[1]);
                return;
            default:
                return;
        }
    }
}
