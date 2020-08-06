package anet.channel.entity;

public class Event {
    public int errorCode;
    public String errorDetail;
    EventType type;

    public Event(EventType type2) {
        this.type = type2;
    }

    public Event(EventType type2, int errorCode2, String errorDetail2) {
        this.type = type2;
        this.errorCode = errorCode2;
        this.errorDetail = errorDetail2;
    }

    public EventType getEventType() {
        return this.type;
    }
}
