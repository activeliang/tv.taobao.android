package android.taobao.atlas.runtime.newcomponent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy;
import android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager;
import java.util.List;

public class ReceiverBridge {
    private static DelegateReceiver receiver;
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static synchronized void registerAdditionalReceiver() {
        synchronized (ReceiverBridge.class) {
            if (receiver == null) {
                receiver = new DelegateReceiver();
                RuntimeVariables.androidApplication.registerReceiver(receiver, AdditionalPackageManager.getInstance().getAdditionIntentFilter());
            }
        }
    }

    public static void postOnReceived(final Intent intent, final ActivityInfo info) {
        sMainHandler.post(new Runnable() {
            public void run() {
                try {
                    ((BroadcastReceiver) RuntimeVariables.androidApplication.getClassLoader().loadClass(info.name).newInstance()).onReceive(RuntimeVariables.androidApplication, intent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class DelegateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            List<ResolveInfo> infos;
            if (intent != null && (infos = AdditionalPackageManager.getInstance().queryIntentReceivers(intent)) != null) {
                for (ResolveInfo info : infos) {
                    if (info.activityInfo.processName.equals(context.getPackageName())) {
                        ReceiverBridge.postOnReceived(intent, info.activityInfo);
                    } else {
                        AdditionalActivityManagerProxy.notifyonReceived(intent, info.activityInfo);
                    }
                }
            }
        }
    }
}
