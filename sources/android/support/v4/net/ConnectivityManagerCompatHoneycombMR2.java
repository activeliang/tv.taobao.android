package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresApi;

@TargetApi(13)
@RequiresApi(13)
class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case 1:
            case 7:
            case 9:
                return false;
            default:
                return true;
        }
    }
}
