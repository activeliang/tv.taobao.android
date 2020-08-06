package io.reactivex.internal.operators.parallel;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelFilterTry<T> extends ParallelFlowable<T> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Predicate<? super T> predicate;
    final ParallelFlowable<T> source;

    public ParallelFilterTry(ParallelFlowable<T> source2, Predicate<? super T> predicate2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.predicate = predicate2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super T> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelFilterConditionalSubscriber<>((ConditionalSubscriber) a, this.predicate, this.errorHandler);
                } else {
                    parents[i] = new ParallelFilterSubscriber<>(a, this.predicate, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    static abstract class BaseFilterSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Predicate<? super T> predicate;
        Subscription s;

        BaseFilterSubscriber(Predicate<? super T> predicate2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.predicate = predicate2;
            this.errorHandler = errorHandler2;
        }

        public final void request(long n) {
            this.s.request(n);
        }

        public final void cancel() {
            this.s.cancel();
        }

        public final void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.s.request(1);
            }
        }
    }

    static final class ParallelFilterSubscriber<T> extends BaseFilterSubscriber<T> {
        final Subscriber<? super T> actual;

        ParallelFilterSubscriber(Subscriber<? super T> actual2, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
            super(predicate, errorHandler);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.s, s)) {
                this.s = s;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0008 A[LOOP:0: B:3:0x0008->B:12:0x0038, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0006, B:12:0x0038] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0008] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r13) {
            /*
                r12 = this;
                r6 = 1
                r7 = 0
                boolean r8 = r12.done
                if (r8 != 0) goto L_0x0067
                r4 = 0
            L_0x0008:
                io.reactivex.functions.Predicate r8 = r12.predicate     // Catch:{ Throwable -> 0x0016 }
                boolean r0 = r8.test(r13)     // Catch:{ Throwable -> 0x0016 }
                if (r0 == 0) goto L_0x0065
                org.reactivestreams.Subscriber<? super T> r7 = r12.actual
                r7.onNext(r13)
            L_0x0015:
                return r6
            L_0x0016:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                io.reactivex.functions.BiFunction r8 = r12.errorHandler     // Catch:{ Throwable -> 0x0043 }
                r10 = 1
                long r4 = r4 + r10
                java.lang.Long r9 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0043 }
                java.lang.Object r8 = r8.apply(r9, r1)     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r9 = "The errorHandler returned a null item"
                java.lang.Object r3 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r9)     // Catch:{ Throwable -> 0x0043 }
                io.reactivex.parallel.ParallelFailureHandling r3 = (io.reactivex.parallel.ParallelFailureHandling) r3     // Catch:{ Throwable -> 0x0043 }
                int[] r8 = io.reactivex.internal.operators.parallel.ParallelFilterTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r9 = r3.ordinal()
                r8 = r8[r9]
                switch(r8) {
                    case 1: goto L_0x0008;
                    case 2: goto L_0x005b;
                    case 3: goto L_0x005d;
                    default: goto L_0x003b;
                }
            L_0x003b:
                r12.cancel()
                r12.onError(r1)
                r6 = r7
                goto L_0x0015
            L_0x0043:
                r2 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
                r12.cancel()
                io.reactivex.exceptions.CompositeException r8 = new io.reactivex.exceptions.CompositeException
                r9 = 2
                java.lang.Throwable[] r9 = new java.lang.Throwable[r9]
                r9[r7] = r1
                r9[r6] = r2
                r8.<init>((java.lang.Throwable[]) r9)
                r12.onError(r8)
                r6 = r7
                goto L_0x0015
            L_0x005b:
                r6 = r7
                goto L_0x0015
            L_0x005d:
                r12.cancel()
                r12.onComplete()
                r6 = r7
                goto L_0x0015
            L_0x0065:
                r6 = r7
                goto L_0x0015
            L_0x0067:
                r6 = r7
                goto L_0x0015
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelFilterTry.ParallelFilterSubscriber.tryOnNext(java.lang.Object):boolean");
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

    static final class ParallelFilterConditionalSubscriber<T> extends BaseFilterSubscriber<T> {
        final ConditionalSubscriber<? super T> actual;

        ParallelFilterConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
            super(predicate, errorHandler);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.s, s)) {
                this.s = s;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0008 A[LOOP:0: B:3:0x0008->B:14:0x003c, LOOP_START, PHI: r4 
          PHI: (r4v1 'retries' long) = (r4v0 'retries' long), (r4v2 'retries' long) binds: [B:2:0x0006, B:14:0x003c] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:3:0x0008] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r13) {
            /*
                r12 = this;
                r6 = 1
                r7 = 0
                boolean r8 = r12.done
                if (r8 != 0) goto L_0x0019
                r4 = 0
            L_0x0008:
                io.reactivex.functions.Predicate r8 = r12.predicate     // Catch:{ Throwable -> 0x001a }
                boolean r0 = r8.test(r13)     // Catch:{ Throwable -> 0x001a }
                if (r0 == 0) goto L_0x0064
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super T> r8 = r12.actual
                boolean r8 = r8.tryOnNext(r13)
                if (r8 == 0) goto L_0x0064
            L_0x0018:
                r7 = r6
            L_0x0019:
                return r7
            L_0x001a:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                io.reactivex.functions.BiFunction r8 = r12.errorHandler     // Catch:{ Throwable -> 0x0046 }
                r10 = 1
                long r4 = r4 + r10
                java.lang.Long r9 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0046 }
                java.lang.Object r8 = r8.apply(r9, r1)     // Catch:{ Throwable -> 0x0046 }
                java.lang.String r9 = "The errorHandler returned a null item"
                java.lang.Object r3 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r8, r9)     // Catch:{ Throwable -> 0x0046 }
                io.reactivex.parallel.ParallelFailureHandling r3 = (io.reactivex.parallel.ParallelFailureHandling) r3     // Catch:{ Throwable -> 0x0046 }
                int[] r8 = io.reactivex.internal.operators.parallel.ParallelFilterTry.AnonymousClass1.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r9 = r3.ordinal()
                r8 = r8[r9]
                switch(r8) {
                    case 1: goto L_0x0008;
                    case 2: goto L_0x0019;
                    case 3: goto L_0x005d;
                    default: goto L_0x003f;
                }
            L_0x003f:
                r12.cancel()
                r12.onError(r1)
                goto L_0x0019
            L_0x0046:
                r2 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
                r12.cancel()
                io.reactivex.exceptions.CompositeException r8 = new io.reactivex.exceptions.CompositeException
                r9 = 2
                java.lang.Throwable[] r9 = new java.lang.Throwable[r9]
                r9[r7] = r1
                r9[r6] = r2
                r8.<init>((java.lang.Throwable[]) r9)
                r12.onError(r8)
                goto L_0x0019
            L_0x005d:
                r12.cancel()
                r12.onComplete()
                goto L_0x0019
            L_0x0064:
                r6 = r7
                goto L_0x0018
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelFilterTry.ParallelFilterConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
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
