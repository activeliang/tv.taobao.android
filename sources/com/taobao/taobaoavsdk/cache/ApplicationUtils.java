package com.taobao.taobaoavsdk.cache;

import android.app.Application;
import com.edge.pcdn.PcdnManager;
import com.edge.pcdn.PcdnType;
import com.taobao.media.MediaSystemUtils;

public class ApplicationUtils {
    public static volatile boolean bUseMediacodecForLive = true;
    public static volatile boolean bUseMediacodecForVideo = true;
    public static volatile boolean mIsPCDNStarted;
    public static volatile Application sApplication;

    public static void setApplicationOnce(Application application) {
        if (sApplication == null) {
            synchronized (ApplicationUtils.class) {
                if (sApplication == null) {
                    sApplication = application;
                    MediaSystemUtils.sApplication = application;
                }
            }
        }
    }

    public static void pcdnStartOnce() {
        if (sApplication != null && !mIsPCDNStarted) {
            synchronized (ApplicationUtils.class) {
                if (!mIsPCDNStarted) {
                    try {
                        PcdnManager.start(sApplication, PcdnType.LIVE, "60009801005b3f250fd98c66d2ad8e812983eb5febf57a949f", (String) null, (String) null, (String) null);
                        mIsPCDNStarted = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
        return;
    }
}
