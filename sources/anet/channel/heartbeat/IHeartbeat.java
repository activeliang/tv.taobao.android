package anet.channel.heartbeat;

public interface IHeartbeat {
    long getInterval();

    void heartbeat();

    void reSchedule();

    void start();

    void stop();
}
