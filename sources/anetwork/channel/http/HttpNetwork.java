package anetwork.channel.http;

import android.content.Context;
import anetwork.channel.aidl.adapter.NetworkProxy;

public class HttpNetwork extends NetworkProxy {
    protected static final String TAG = "anet.HttpNetwork";

    public HttpNetwork(Context context) {
        super(context, 0);
    }
}
