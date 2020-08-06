package mtopsdk.network.impl;

import android.content.Context;
import mtopsdk.network.Call;
import mtopsdk.network.domain.Request;

public class ANetworkCallFactory implements Call.Factory {
    private Context mContext;

    public ANetworkCallFactory(Context context) {
        this.mContext = context;
    }

    public Call newCall(Request request) {
        return new ANetworkCallImpl(request, this.mContext);
    }
}
