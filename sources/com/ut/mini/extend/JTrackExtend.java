package com.ut.mini.extend;

import android.app.Activity;
import android.net.Uri;
import com.taobao.ju.track.JTrack;
import java.util.Map;

public class JTrackExtend {
    public static String getPageName(String activityName) {
        return JTrack.Page.getPageName(activityName);
    }

    public static Map<String, String> getArgsMap(String pageNameOrActivityName, Uri uri) {
        return JTrack.Page.getArgsMap(pageNameOrActivityName, uri);
    }

    public static Map<String, String> getArgsMap(Activity activity, Uri uri) {
        return JTrack.Page.getArgsMap(activity, uri);
    }
}
