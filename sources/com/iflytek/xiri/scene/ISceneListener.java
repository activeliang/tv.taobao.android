package com.iflytek.xiri.scene;

import android.content.Intent;

public interface ISceneListener {
    void onExecute(Intent intent);

    String onQuery();
}
