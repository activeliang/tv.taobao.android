package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.QueueDrainObserver;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.subjects.UnicastSubject;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowTimed<T> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int bufferSize;
    final long maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    public ObservableWindowTimed(ObservableSource<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, long maxSize2, int bufferSize2, boolean restartTimerOnMaxSize2) {
        super(source);
        this.timespan = timespan2;
        this.timeskip = timeskip2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.maxSize = maxSize2;
        this.bufferSize = bufferSize2;
        this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
    }

    public void subscribeActual(Observer<? super Observable<T>> t) {
        SerializedObserver<Observable<T>> actual = new SerializedObserver<>(t);
        if (this.timespan != this.timeskip) {
            this.source.subscribe(new WindowSkipObserver(actual, this.timespan, this.timeskip, this.unit, this.scheduler.createWorker(), this.bufferSize));
        } else if (this.maxSize == Long.MAX_VALUE) {
            this.source.subscribe(new WindowExactUnboundedObserver(actual, this.timespan, this.unit, this.scheduler, this.bufferSize));
        } else {
            this.source.subscribe(new WindowExactBoundedObserver(actual, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize));
        }
    }

    static final class WindowExactUnboundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Observer<T>, Disposable, Runnable {
        static final Object NEXT = new Object();
        final int bufferSize;
        Disposable s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        UnicastSubject<T> window;

        WindowExactUnboundedObserver(Observer<? super Observable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.window = UnicastSubject.create(this.bufferSize);
                Observer<? super Observable<T>> a = this.actual;
                a.onSubscribe(this);
                a.onNext(this.window);
                if (!this.cancelled) {
                    DisposableHelper.replace(this.timer, this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit));
                }
            }
        }

        public void onNext(T t) {
            if (!this.terminated) {
                if (fastEnter()) {
                    this.window.onNext(t);
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    this.queue.offer(NotificationLite.next(t));
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            disposeTimer();
            this.actual.onError(t);
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            disposeTimer();
            this.actual.onComplete();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void disposeTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public void run() {
            if (this.cancelled) {
                this.terminated = true;
                disposeTimer();
            }
            this.queue.offer(NEXT);
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            UnicastSubject<T> w = this.window;
            int missed = 1;
            while (true) {
                boolean term = this.terminated;
                boolean d = this.done;
                Object o = q.poll();
                if (!d || !(o == null || o == NEXT)) {
                    if (o == null) {
                        missed = leave(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else if (o == NEXT) {
                        w.onComplete();
                        if (!term) {
                            w = UnicastSubject.create(this.bufferSize);
                            this.window = w;
                            a.onNext(w);
                        } else {
                            this.s.dispose();
                        }
                    } else {
                        w.onNext(NotificationLite.getValue(o));
                    }
                }
            }
            this.window = null;
            q.clear();
            disposeTimer();
            Throwable err = this.error;
            if (err != null) {
                w.onError(err);
            } else {
                w.onComplete();
            }
        }
    }

    static final class WindowExactBoundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable {
        final int bufferSize;
        long count;
        final long maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;
        Disposable s;
        final Scheduler scheduler;
        volatile boolean terminated;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;
        UnicastSubject<T> window;
        final Scheduler.Worker worker;

        WindowExactBoundedObserver(Observer<? super Observable<T>> actual, long timespan2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, long maxSize2, boolean restartTimerOnMaxSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.bufferSize = bufferSize2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
            if (restartTimerOnMaxSize2) {
                this.worker = scheduler2.createWorker();
            } else {
                this.worker = null;
            }
        }

        public void onSubscribe(Disposable s2) {
            Disposable d;
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                Observer<? super Observable<T>> a = this.actual;
                a.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
                    this.window = w;
                    a.onNext(w);
                    ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
                    if (this.restartTimerOnMaxSize) {
                        d = this.worker.schedulePeriodically(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                    } else {
                        d = this.scheduler.schedulePeriodicallyDirect(consumerIndexHolder, this.timespan, this.timespan, this.unit);
                    }
                    DisposableHelper.replace(this.timer, d);
                }
            }
        }

        public void onNext(T t) {
            if (!this.terminated) {
                if (fastEnter()) {
                    UnicastSubject<T> w = this.window;
                    w.onNext(t);
                    long c = this.count + 1;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w.onComplete();
                        UnicastSubject<T> w2 = UnicastSubject.create(this.bufferSize);
                        this.window = w2;
                        this.actual.onNext(w2);
                        if (this.restartTimerOnMaxSize) {
                            this.timer.get().dispose();
                            DisposableHelper.replace(this.timer, this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit));
                        }
                    } else {
                        this.count = c;
                    }
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    this.queue.offer(NotificationLite.next(t));
                    if (!enter()) {
                        return;
                    }
                }
                drainLoop();
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(t);
            disposeTimer();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            disposeTimer();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void disposeTimer() {
            DisposableHelper.dispose(this.timer);
            Scheduler.Worker w = this.worker;
            if (w != null) {
                w.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            UnicastSubject<T> w = this.window;
            int missed = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object o = q.poll();
                boolean empty = o == null;
                boolean isHolder = o instanceof ConsumerIndexHolder;
                if (d && (empty || isHolder)) {
                    this.window = null;
                    q.clear();
                    disposeTimer();
                    Throwable err = this.error;
                    if (err != null) {
                        w.onError(err);
                        return;
                    } else {
                        w.onComplete();
                        return;
                    }
                } else if (empty) {
                    missed = leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else if (isHolder) {
                    ConsumerIndexHolder consumerIndexHolder = (ConsumerIndexHolder) o;
                    if (this.restartTimerOnMaxSize || this.producerIndex == consumerIndexHolder.index) {
                        w.onComplete();
                        this.count = 0;
                        w = UnicastSubject.create(this.bufferSize);
                        this.window = w;
                        a.onNext(w);
                    }
                } else {
                    w.onNext(NotificationLite.getValue(o));
                    long c = this.count + 1;
                    if (c >= this.maxSize) {
                        this.producerIndex++;
                        this.count = 0;
                        w.onComplete();
                        w = UnicastSubject.create(this.bufferSize);
                        this.window = w;
                        this.actual.onNext(w);
                        if (this.restartTimerOnMaxSize) {
                            Disposable tm = this.timer.get();
                            tm.dispose();
                            Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit);
                            if (!this.timer.compareAndSet(tm, task)) {
                                task.dispose();
                            }
                        }
                    } else {
                        this.count = c;
                    }
                }
            }
            this.s.dispose();
            q.clear();
            disposeTimer();
        }

        static final class ConsumerIndexHolder implements Runnable {
            final long index;
            final WindowExactBoundedObserver<?> parent;

            ConsumerIndexHolder(long index2, WindowExactBoundedObserver<?> parent2) {
                this.index = index2;
                this.parent = parent2;
            }

            public void run() {
                WindowExactBoundedObserver<?> p = this.parent;
                if (!p.cancelled) {
                    p.queue.offer(this);
                } else {
                    p.terminated = true;
                    p.disposeTimer();
                }
                if (p.enter()) {
                    p.drainLoop();
                }
            }
        }
    }

    static final class WindowSkipObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable, Runnable {
        final int bufferSize;
        Disposable s;
        volatile boolean terminated;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;
        final List<UnicastSubject<T>> windows = new LinkedList();
        final Scheduler.Worker worker;

        WindowSkipObserver(Observer<? super Observable<T>> actual, long timespan2, long timeskip2, TimeUnit unit2, Scheduler.Worker worker2, int bufferSize2) {
            super(actual, new MpscLinkedQueue());
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.worker = worker2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                if (!this.cancelled) {
                    UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
                    this.windows.add(w);
                    this.actual.onNext(w);
                    this.worker.schedule(new CompletionTask(w), this.timespan, this.unit);
                    this.worker.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                }
            }
        }

        public void onNext(T t) {
            if (fastEnter()) {
                for (UnicastSubject<T> w : this.windows) {
                    w.onNext(t);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                this.queue.offer(t);
                if (!enter()) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onError(t);
            disposeWorker();
        }

        public void onComplete() {
            this.done = true;
            if (enter()) {
                drainLoop();
            }
            this.actual.onComplete();
            disposeWorker();
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void disposeWorker() {
            this.worker.dispose();
        }

        /* access modifiers changed from: package-private */
        public void complete(UnicastSubject<T> w) {
            this.queue.offer(new SubjectWork(w, false));
            if (enter()) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        public void drainLoop() {
            MpscLinkedQueue<Object> q = (MpscLinkedQueue) this.queue;
            Observer<? super Observable<T>> a = this.actual;
            List<UnicastSubject<T>> ws = this.windows;
            int missed = 1;
            while (!this.terminated) {
                boolean d = this.done;
                Object v = q.poll();
                boolean empty = v == null;
                boolean sw = v instanceof SubjectWork;
                if (d && (empty || sw)) {
                    q.clear();
                    Throwable e = this.error;
                    if (e != null) {
                        for (UnicastSubject<T> w : ws) {
                            w.onError(e);
                        }
                    } else {
                        for (UnicastSubject<T> w2 : ws) {
                            w2.onComplete();
                        }
                    }
                    disposeWorker();
                    ws.clear();
                    return;
                } else if (empty) {
                    missed = leave(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else if (sw) {
                    SubjectWork<T> work = (SubjectWork) v;
                    if (!work.open) {
                        ws.remove(work.w);
                        work.w.onComplete();
                        if (ws.isEmpty() && this.cancelled) {
                            this.terminated = true;
                        }
                    } else if (!this.cancelled) {
                        UnicastSubject<T> w3 = UnicastSubject.create(this.bufferSize);
                        ws.add(w3);
                        a.onNext(w3);
                        this.worker.schedule(new CompletionTask(w3), this.timespan, this.unit);
                    }
                } else {
                    for (UnicastSubject<T> w4 : ws) {
                        w4.onNext(v);
                    }
                }
            }
            this.s.dispose();
            disposeWorker();
            q.clear();
            ws.clear();
        }

        public void run() {
            SubjectWork<T> sw = new SubjectWork<>(UnicastSubject.create(this.bufferSize), true);
            if (!this.cancelled) {
                this.queue.offer(sw);
            }
            if (enter()) {
                drainLoop();
            }
        }

        static final class SubjectWork<T> {
            final boolean open;
            final UnicastSubject<T> w;

            SubjectWork(UnicastSubject<T> w2, boolean open2) {
                this.w = w2;
                this.open = open2;
            }
        }

        final class CompletionTask implements Runnable {
            private final UnicastSubject<T> w;

            CompletionTask(UnicastSubject<T> w2) {
                this.w = w2;
            }

            public void run() {
                WindowSkipObserver.this.complete(this.w);
            }
        }
    }
}
