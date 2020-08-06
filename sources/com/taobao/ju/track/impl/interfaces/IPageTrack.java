package com.taobao.ju.track.impl.interfaces;

import android.app.Activity;

public interface IPageTrack extends ITrack {
    String getPageName(Activity activity);

    String getPageName(String str);

    String getSpm(Activity activity);

    String getSpm(String str);

    String getSpmAB(Activity activity);

    String getSpmAB(String str);
}
