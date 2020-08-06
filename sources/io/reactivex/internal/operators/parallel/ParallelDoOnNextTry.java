package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelDoOnNextTry<T> extends ParallelFlowable<T> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Consumer<? super T> onNext;
    final ParallelFlowable<T> source;

    public ParallelDoOnNextTry(ParallelFlowable<T> source2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.onNext = onNext2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super T> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelDoOnNextConditionalSubscriber<>((ConditionalSubscriber) a, this.onNext, this.errorHandler);
                } else {
                    parents[i] = new ParallelDoOnNextSubscriber<>(a, this.onNext, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    static final class ParallelDoOnNextSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;
        Subscription s;

        ParallelDoOnNextSubscriber(Subscriber<? super T> actual2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.onNext = onNext2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0009 A[LOOP:0: B:3:0x0009->B:11:0x0037, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0007, B:11:0x0037] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0009] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r11) {
            /*
                r10 = this;
                r6 = 1
                r3 = 0
                boolean r7 = r10.done
                if (r7 == 0) goto L_0x0007
            L_0x0006:
                return r3
            L_0x0007:
                r4 = 0
            L_0x0009:
                io.reactivex.functions.Consumer<? super T> r7 = r10.onNext     // Catch:{ Throwable -> 0x0015 }
                r7.accept(r11)     // Catch:{ Throwable -> 0x0015 }
                org.reactivestreams.Subscriber<? super T> r3 = r10.actual
                r3.onNext(r11)
                r3 = r6
                goto L_0x0006
            L_0x0015:
                r0 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r7 = r10.errorHandler     // Catch:{ Throwable -> 0x0041 }
                r8 = 1
                long r4 = r4 + r8
                java.lang.Long r8 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0041 }
                java.lang.Object r7 = r7.apply(r8, r0)     // Catch:{ Throwable -> 0x0041 }
                java.lang.String r8 = "The errorHandler returned a null item"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r7, r8)     // Catch:{ Throwable -> 0x0041 }
                io.reactivex.parallel.ParallelFailureHandling r2 = (io.reactivex.parallel.ParallelFailureHandling) r2     // Catch:{ Throwable -> 0x0041 }
                int[] r7 = io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r8 = r2.ordinal()
                r7 = r7[r8]
                switch(r7) {
                    case 1: goto L_0x0009;
                    case 2: goto L_0x0006;
                    case 3: goto L_0x0058;
                    default: goto L_0x003a;
                }
            L_0x003a:
                r10.cancel()
                r10.onError(r0)
                goto L_0x0006
            L_0x0041:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r10.cancel()
                io.reactivex.exceptions.CompositeException r7 = new io.reactivex.exceptions.CompositeException
                r8 = 2
                java.lang.Throwable[] r8 = new java.lang.Throwable[r8]
                r8[r3] = r0
                r8[r6] = r1
                r7.<init>((java.lang.Throwable[]) r8)
                r10.onError(r7)
                goto L_0x0006
            L_0x0058:
                r10.cancel()
                r10.onComplete()
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.ParallelDoOnNextSubscriber.tryOnNext(java.lang.Object):boolean");
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }
    }

    static final class ParallelDoOnNextConditionalSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;
        Subscription s;

        ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.onNext = onNext2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.s.cancel();
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0008 A[LOOP:0: B:3:0x0008->B:11:0x0036, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0006, B:11:0x0036] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0008] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r11) {
            /*
                r10 = this;
                r3 = 0
                boolean r6 = r10.done
                if (r6 == 0) goto L_0x0006
            L_0x0005:
                return r3
            L_0x0006:
                r4 = 0
            L_0x0008:
                io.reactivex.functions.Consumer<? super T> r6 = r10.onNext     // Catch:{ Throwable -> 0x0014 }
                r6.accept(r11)     // Catch:{ Throwable -> 0x0014 }
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super T> r3 = r10.actual
                boolean r3 = r3.tryOnNext(r11)
                goto L_0x0005
            L_0x0014:
                r0 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r6 = r10.errorHandler     // Catch:{ Throwable -> 0x0040 }
                r8 = 1
                long r4 = r4 + r8
                java.lang.Long r7 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0040 }
                java.lang.Object r6 = r6.apply(r7, r0)     // Catch:{ Throwable -> 0x0040 }
                java.lang.String r7 = "The errorHandler returned a null item"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r6, r7)     // Catch:{ Throwable -> 0x0040 }
                io.reactivex.parallel.ParallelFailureHandling r2 = (io.reactivex.parallel.ParallelFailureHandling) r2     // Catch:{ Throwable -> 0x0040 }
                int[] r6 = io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r7 = r2.ordinal()
                r6 = r6[r7]
                switch(r6) {
                    case 1: goto L_0x0008;
                    case 2: goto L_0x0005;
                    case 3: goto L_0x0058;
                    default: goto L_0x0039;
                }
            L_0x0039:
                r10.cancel()
                r10.onError(r0)
                goto L_0x0005
            L_0x0040:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r10.cancel()
                io.reactivex.exceptions.CompositeException r6 = new io.reactivex.exceptions.CompositeException
                r7 = 2
                java.lang.Throwable[] r7 = new java.lang.Throwable[r7]
                r7[r3] = r0
                r8 = 1
                r7[r8] = r1
                r6.<init>((java.lang.Throwable[]) r7)
                r10.onError(r6)
                goto L_0x0005
            L_0x0058:
                r10.cancel()
                r10.onComplete()
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.ParallelDoOnNextConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }
    }
}
