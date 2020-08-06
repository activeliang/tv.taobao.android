package com.alibaba.analytics.utils;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

public class SPHelper {
    @TargetApi(9)
    public static void apply(SharedPreferences.Editor pEditor) {
        pEditor.apply();
    }

    public static void fastCommit(SharedPreferences.Editor editor) {
        Logger.i("4.3.8 cacheLog [fastCommit]", "");
        if (editor == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            apply(editor);
        } else {
            editor.commit();
        }
    }
}
