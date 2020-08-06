package anet.channel.heartbeat;

import anet.channel.Session;

public interface IHeartbeatFactory {
    IHeartbeat createHeartbeat(Session session);
}
