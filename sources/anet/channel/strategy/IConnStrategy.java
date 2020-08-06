package anet.channel.strategy;

public interface IConnStrategy {
    int getConnectionTimeout();

    int getHeartbeat();

    String getIp();

    int getIpSource();

    int getIpType();

    int getPort();

    ConnProtocol getProtocol();

    int getReadTimeout();

    int getRetryTimes();
}
