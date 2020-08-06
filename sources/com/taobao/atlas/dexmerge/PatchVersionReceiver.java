package com.taobao.atlas.dexmerge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.taobao.atlas.runtime.RuntimeVariables;

public class PatchVersionReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        RuntimeVariables.patchVersion = intent.getIntExtra("patch_version", 2);
        RuntimeVariables.androidApplication.unregisterReceiver(this);
    }
}
