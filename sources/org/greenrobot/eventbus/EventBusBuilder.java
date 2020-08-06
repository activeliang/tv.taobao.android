package org.greenrobot.eventbus;

import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.MainThreadSupport;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

public class EventBusBuilder {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    boolean eventInheritance = true;
    ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
    boolean ignoreGeneratedIndex;
    boolean logNoSubscriberMessages = true;
    boolean logSubscriberExceptions = true;
    Logger logger;
    MainThreadSupport mainThreadSupport;
    boolean sendNoSubscriberEvent = true;
    boolean sendSubscriberExceptionEvent = true;
    List<Class<?>> skipMethodVerificationForClasses;
    boolean strictMethodVerification;
    List<SubscriberInfoIndex> subscriberInfoIndexes;
    boolean throwSubscriberException;

    EventBusBuilder() {
    }

    public EventBusBuilder logSubscriberExceptions(boolean logSubscriberExceptions2) {
        this.logSubscriberExceptions = logSubscriberExceptions2;
        return this;
    }

    public EventBusBuilder logNoSubscriberMessages(boolean logNoSubscriberMessages2) {
        this.logNoSubscriberMessages = logNoSubscriberMessages2;
        return this;
    }

    public EventBusBuilder sendSubscriberExceptionEvent(boolean sendSubscriberExceptionEvent2) {
        this.sendSubscriberExceptionEvent = sendSubscriberExceptionEvent2;
        return this;
    }

    public EventBusBuilder sendNoSubscriberEvent(boolean sendNoSubscriberEvent2) {
        this.sendNoSubscriberEvent = sendNoSubscriberEvent2;
        return this;
    }

    public EventBusBuilder throwSubscriberException(boolean throwSubscriberException2) {
        this.throwSubscriberException = throwSubscriberException2;
        return this;
    }

    public EventBusBuilder eventInheritance(boolean eventInheritance2) {
        this.eventInheritance = eventInheritance2;
        return this;
    }

    public EventBusBuilder executorService(ExecutorService executorService2) {
        this.executorService = executorService2;
        return this;
    }

    public EventBusBuilder skipMethodVerificationFor(Class<?> clazz) {
        if (this.skipMethodVerificationForClasses == null) {
            this.skipMethodVerificationForClasses = new ArrayList();
        }
        this.skipMethodVerificationForClasses.add(clazz);
        return this;
    }

    public EventBusBuilder ignoreGeneratedIndex(boolean ignoreGeneratedIndex2) {
        this.ignoreGeneratedIndex = ignoreGeneratedIndex2;
        return this;
    }

    public EventBusBuilder strictMethodVerification(boolean strictMethodVerification2) {
        this.strictMethodVerification = strictMethodVerification2;
        return this;
    }

    public EventBusBuilder addIndex(SubscriberInfoIndex index) {
        if (this.subscriberInfoIndexes == null) {
            this.subscriberInfoIndexes = new ArrayList();
        }
        this.subscriberInfoIndexes.add(index);
        return this;
    }

    public EventBusBuilder logger(Logger logger2) {
        this.logger = logger2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public Logger getLogger() {
        if (this.logger != null) {
            return this.logger;
        }
        return (!Logger.AndroidLogger.isAndroidLogAvailable() || getAndroidMainLooperOrNull() == null) ? new Logger.SystemOutLogger() : new Logger.AndroidLogger("EventBus");
    }

    /* access modifiers changed from: package-private */
    public MainThreadSupport getMainThreadSupport() {
        Object looperOrNull;
        if (this.mainThreadSupport != null) {
            return this.mainThreadSupport;
        }
        if (!Logger.AndroidLogger.isAndroidLogAvailable() || (looperOrNull = getAndroidMainLooperOrNull()) == null) {
            return null;
        }
        return new MainThreadSupport.AndroidHandlerMainThreadSupport((Looper) looperOrNull);
    }

    /* access modifiers changed from: package-private */
    public Object getAndroidMainLooperOrNull() {
        try {
            return Looper.getMainLooper();
        } catch (RuntimeException e) {
            return null;
        }
    }

    public EventBus installDefaultEventBus() {
        EventBus eventBus;
        synchronized (EventBus.class) {
            if (EventBus.defaultInstance != null) {
                throw new EventBusException("Default instance already exists. It may be only set once before it's used the first time to ensure consistent behavior.");
            }
            EventBus.defaultInstance = build();
            eventBus = EventBus.defaultInstance;
        }
        return eventBus;
    }

    public EventBus build() {
        return new EventBus(this);
    }
}
