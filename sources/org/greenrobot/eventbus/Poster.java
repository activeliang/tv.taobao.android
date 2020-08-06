package org.greenrobot.eventbus;

interface Poster {
    void enqueue(Subscription subscription, Object obj);
}
