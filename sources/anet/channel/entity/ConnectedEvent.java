package anet.channel.entity;

public class ConnectedEvent extends Event {
    public long mConnectedTime;
    public long mSSLTime;

    public ConnectedEvent(EventType type) {
        super(type);
    }
}
