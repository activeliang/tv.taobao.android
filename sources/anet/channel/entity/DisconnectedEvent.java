package anet.channel.entity;

public class DisconnectedEvent extends Event {
    public boolean sdkForceClose;

    public DisconnectedEvent(EventType type, boolean forceClose, int errorCode, String reason) {
        super(type, errorCode, reason);
        this.sdkForceClose = forceClose;
    }
}
