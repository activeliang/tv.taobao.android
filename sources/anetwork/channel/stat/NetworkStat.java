package anetwork.channel.stat;

public class NetworkStat {
    public static INetworkStat getNetworkStat() {
        return NetworkStatCache.getInstance();
    }
}
