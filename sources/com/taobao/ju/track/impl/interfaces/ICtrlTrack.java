package com.taobao.ju.track.impl.interfaces;

import android.app.Activity;

public interface ICtrlTrack extends ITrack {
    String getSpm(Activity activity, String str);

    String getSpm(String str, String str2);
}
