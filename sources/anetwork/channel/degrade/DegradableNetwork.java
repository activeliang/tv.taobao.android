package anetwork.channel.degrade;

import android.content.Context;
import anetwork.channel.aidl.adapter.NetworkProxy;

public class DegradableNetwork extends NetworkProxy {
    public DegradableNetwork(Context context) {
        super(context, 1);
    }
}
