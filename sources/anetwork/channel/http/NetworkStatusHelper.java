package anetwork.channel.http;

@Deprecated
public class NetworkStatusHelper {

    public enum NetworkStatus {
        NONE,
        NO,
        GPRS,
        CDMA,
        EDGE,
        G3,
        G4,
        WIFI
    }

    @Deprecated
    public static NetworkStatus getStatus() {
        switch (anet.channel.status.NetworkStatusHelper.getStatus()) {
            case NO:
                return NetworkStatus.NO;
            case G2:
                return NetworkStatus.GPRS;
            case G3:
                return NetworkStatus.G3;
            case G4:
                return NetworkStatus.G4;
            case WIFI:
                return NetworkStatus.WIFI;
            default:
                return NetworkStatus.NONE;
        }
    }

    @Deprecated
    public static boolean isNetworkAvailable() {
        return anet.channel.status.NetworkStatusHelper.isConnected();
    }
}
