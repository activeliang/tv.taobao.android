package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelMapTry<T, R> extends ParallelFlowable<R> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Function<? super T, ? extends R> mapper;
    final ParallelFlowable<T> source;

    public ParallelMapTry(ParallelFlowable<T> source2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.mapper = mapper2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super R> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelMapTryConditionalSubscriber<>((ConditionalSubscriber) a, this.mapper, this.errorHandler);
                } else {
                    parents[i] = new ParallelMapTrySubscriber<>(a, this.mapper, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    static final class ParallelMapTrySubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
        final Subscriber<? super R> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Function<? super T, ? extends R> mapper;
        Subscription s;

        ParallelMapTrySubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.mapper = mapper2;
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

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0009 A[LOOP:0: B:3:0x0009->B:11:0x003f, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0007, B:11:0x003f] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0009] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r13) {
            /*
                r12 = this;
                r7 = 1
                r6 = 0
                boolean r8 = r12.done
                if (r8 == 0) goto L_0x0007
            L_0x0006:
                return r6
            L_0x0007:
                r4 = 0
            L_0x0009:
                io.reactivex.functions.Function<? super T, ? extends R> r8 = r12.mapper     // Catch:{ Throwable -> 0x001d }
                java.lang.Object r8 = r8.apply(r13)     // Catch:{ Throwable -> 0x001d }
                java.lang.String r9 = "The mapper returned a null value"
                java.lang.Object r3 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r9)     // Catch:{ Throwable -> 0x001d }
                org.reactivestreams.Subscriber<? super R> r6 = r12.actual
                r6.onNext(r3)
                r6 = r7
                goto L_0x0006
            L_0x001d:
                r0 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r8 = r12.errorHandler     // Catch:{ Throwable -> 0x0049 }
                r10 = 1
                long r4 = r4 + r10
                java.lang.Long r9 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0049 }
                java.lang.Object r8 = r8.apply(r9, r0)     // Catch:{ Throwable -> 0x0049 }
                java.lang.String r9 = "The errorHandler returned a null item"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r9)     // Catch:{ Throwable -> 0x0049 }
                io.reactivex.parallel.ParallelFailureHandling r2 = (io.reactivex.parallel.ParallelFailureHandling) r2     // Catch:{ Throwable -> 0x0049 }
                int[] r8 = io.reactivex.internal.operators.parallel.ParallelMapTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r9 = r2.ordinal()
                r8 = r8[r9]
                switch(r8) {
                    case 1: goto L_0x0009;
                    case 2: goto L_0x0006;
                    case 3: goto L_0x0060;
                    default: goto L_0x0042;
                }
            L_0x0042:
                r12.cancel()
                r12.onError(r0)
                goto L_0x0006
            L_0x0049:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r12.cancel()
                io.reactivex.exceptions.CompositeException r8 = new io.reactivex.exceptions.CompositeException
                r9 = 2
                java.lang.Throwable[] r9 = new java.lang.Throwable[r9]
                r9[r6] = r0
                r9[r7] = r1
                r8.<init>((java.lang.Throwable[]) r9)
                r12.onError(r8)
                goto L_0x0006
            L_0x0060:
                r12.cancel()
                r12.onComplete()
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelMapTry.ParallelMapTrySubscriber.tryOnNext(java.lang.Object):boolean");
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

    static final class ParallelMapTryConditionalSubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super R> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Function<? super T, ? extends R> mapper;
        Subscription s;

        ParallelMapTryConditionalSubscriber(ConditionalSubscriber<? super R> actual2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.mapper = mapper2;
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

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0008 A[LOOP:0: B:3:0x0008->B:11:0x003e, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0006, B:11:0x003e] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0008] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r11) {
            /*
                r10 = this;
                r6 = 0
                boolean r7 = r10.done
                if (r7 == 0) goto L_0x0006
            L_0x0005:
                return r6
            L_0x0006:
                r4 = 0
            L_0x0008:
                io.reactivex.functions.Function<? super T, ? extends R> r7 = r10.mapper     // Catch:{ Throwable -> 0x001c }
                java.lang.Object r7 = r7.apply(r11)     // Catch:{ Throwable -> 0x001c }
                java.lang.String r8 = "The mapper returned a null value"
                java.lang.Object r3 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r7, r8)     // Catch:{ Throwable -> 0x001c }
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super R> r6 = r10.actual
                boolean r6 = r6.tryOnNext(r3)
                goto L_0x0005
            L_0x001c:
                r0 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r7 = r10.errorHandler     // Catch:{ Throwable -> 0x0048 }
                r8 = 1
                long r4 = r4 + r8
                java.lang.Long r8 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0048 }
                java.lang.Object r7 = r7.apply(r8, r0)     // Catch:{ Throwable -> 0x0048 }
                java.lang.String r8 = "The errorHandler returned a null item"
                java.lang.Object r2 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r7, r8)     // Catch:{ Throwable -> 0x0048 }
                io.reactivex.parallel.ParallelFailureHandling r2 = (io.reactivex.parallel.ParallelFailureHandling) r2     // Catch:{ Throwable -> 0x0048 }
                int[] r7 = io.reactivex.internal.operators.parallel.ParallelMapTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r8 = r2.ordinal()
                r7 = r7[r8]
                switch(r7) {
                    case 1: goto L_0x0008;
                    case 2: goto L_0x0005;
                    case 3: goto L_0x0060;
                    default: goto L_0x0041;
                }
            L_0x0041:
                r10.cancel()
                r10.onError(r0)
                goto L_0x0005
            L_0x0048:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r10.cancel()
                io.reactivex.exceptions.CompositeException r7 = new io.reactivex.exceptions.CompositeException
                r8 = 2
                java.lang.Throwable[] r8 = new java.lang.Throwable[r8]
                r8[r6] = r0
                r9 = 1
                r8[r9] = r1
                r7.<init>((java.lang.Throwable[]) r8)
                r10.onError(r7)
                goto L_0x0005
            L_0x0060:
                r10.cancel()
                r10.onComplete()
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelMapTry.ParallelMapTryConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
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
