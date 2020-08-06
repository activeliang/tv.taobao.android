package anet.channel.heartbeat;

import anet.channel.Session;

public class HeartbeatManager {
    private static volatile IHeartbeatFactory heartbeatFactory = new IHeartbeatFactory() {
        public IHeartbeat createHeartbeat(Session session) {
            if (session == null || session.getConnStrategy() == null || session.getConnStrategy().getHeartbeat() <= 0) {
                return null;
            }
            return new DefaultHeartbeatImpl(session);
        }
    };

    public static IHeartbeatFactory getHeartbeatFactory() {
        return heartbeatFactory;
    }

    public static void setHeartbeatFactory(IHeartbeatFactory factory) {
        heartbeatFactory = factory;
    }
}
