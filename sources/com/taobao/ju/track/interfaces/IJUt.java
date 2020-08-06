package com.taobao.ju.track.interfaces;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import java.util.Map;

public interface IJUt {
    void click(View view, Object obj, Map<String, String> map);

    void enter(Activity activity);

    void enter(Activity activity, String str);

    void enter(Activity activity, String str, Object obj);

    void ext(String str, Map<String, String> map);

    void init(Application application);

    void leave(Activity activity);

    void unInit();

    void update(Activity activity, Object obj);

    void update(Activity activity, String str);

    void update(Activity activity, String str, Object obj);
}
