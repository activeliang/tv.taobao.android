package com.taobao.windvane.extra.ut;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UTCrashCaughtListner implements IUTCrashCaughtListener {
    public static String wv_currentStatus = "0";
    /* access modifiers changed from: private */
    public LinkedList mUrlList = null;
    /* access modifiers changed from: private */
    public String wv_currentUrl = "";

    public UTCrashCaughtListner() {
        init();
    }

    public class PageStartWVEventListener implements WVEventListener {
        public PageStartWVEventListener() {
        }

        public WVEventResult onEvent(int eventId, WVEventContext context, Object... data) {
            switch (eventId) {
                case 1001:
                    if (!(context == null || context.url == null)) {
                        String url = context.url;
                        if (UTCrashCaughtListner.this.mUrlList != null) {
                            if (UTCrashCaughtListner.this.mUrlList.size() > 9) {
                                UTCrashCaughtListner.this.mUrlList.removeFirst();
                            }
                            UTCrashCaughtListner.this.mUrlList.addLast(url);
                        }
                        String unused = UTCrashCaughtListner.this.wv_currentUrl = url;
                        TaoLog.v("WV_URL_CHANGE", "current Url : " + url);
                    }
                    UTCrashCaughtListner.wv_currentStatus = "2";
                    return null;
                case WVEventId.PAGE_onPause:
                case WVEventId.PAGE_destroy:
                    UTCrashCaughtListner.wv_currentStatus = "1";
                    return null;
                case WVEventId.PAGE_onResume:
                    UTCrashCaughtListner.wv_currentStatus = "0";
                    return null;
                default:
                    return null;
            }
        }
    }

    private void init() {
        this.mUrlList = new LinkedList();
        WVEventService.getInstance().addEventListener(new PageStartWVEventListener());
    }

    public Map<String, Object> onCrashCaught(Thread arg0, Throwable arg1) {
        int size = this.mUrlList.size();
        if (this.mUrlList == null || size < 1) {
            return null;
        }
        for (int i = 3; i < size; i++) {
            String url = (String) this.mUrlList.get(i);
            if (!TextUtils.isEmpty(url)) {
                this.mUrlList.set(i, WVUrlUtil.removeQueryParam(url));
            }
        }
        HashMap map = new HashMap();
        map.put("crash_url_list", this.mUrlList.toString());
        map.put("wv_currentUrl", this.wv_currentUrl);
        map.put("wv_currentStatus", wv_currentStatus);
        return map;
    }
}
