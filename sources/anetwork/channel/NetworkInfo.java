package anetwork.channel;

import android.content.Context;

@Deprecated
public class NetworkInfo {
    public static final String RESULT_BACKGROUND = "BACKGROUND ACTIVITY";
    public static final String RESULT_UNAUTHORIZED = "NETWORK_UNAUTHROIZED";
    public static final String RESULT_UNCONNECTED = "NETWORK_UNCONNECTED";

    @Deprecated
    public interface NetworkInfoListener {
        void onFinished(String str);
    }

    @Deprecated
    public static void getNetworkInfo(Context context, NetworkInfoListener listener) {
    }

    @Deprecated
    public static String getNetworkInfo(Context context) {
        return null;
    }
}
