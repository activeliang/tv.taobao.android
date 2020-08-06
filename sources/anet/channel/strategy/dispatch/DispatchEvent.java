package anet.channel.strategy.dispatch;

public class DispatchEvent {
    public static final int DNSFAIL = 0;
    public static final int DNSSUCCESS = 1;
    public final int eventType;
    public final Object extraObject;

    public DispatchEvent(int eventType2, Object extraObject2) {
        this.eventType = eventType2;
        this.extraObject = extraObject2;
    }
}
