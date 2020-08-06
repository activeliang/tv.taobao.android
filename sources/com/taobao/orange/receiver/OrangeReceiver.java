package com.taobao.orange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OThreadFactory;

public class OrangeReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    synchronized (OrangeReceiver.class) {
                        ConfigCenter.getInstance().retryFailRequests();
                    }
                }
            });
        }
    }
}
