package anet.channel.entity;

import anet.channel.Session;

public interface EventCb {
    void onEvent(Session session, EventType eventType, Event event);
}
