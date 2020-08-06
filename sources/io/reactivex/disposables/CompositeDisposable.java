package io.reactivex.disposables;

import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.OpenHashSet;
import java.util.ArrayList;
import java.util.List;

public final class CompositeDisposable implements Disposable, DisposableContainer {
    volatile boolean disposed;
    OpenHashSet<Disposable> resources;

    public CompositeDisposable() {
    }

    public CompositeDisposable(@NonNull Disposable... resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new OpenHashSet<>(resources2.length + 1);
        for (Disposable d : resources2) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public CompositeDisposable(@NonNull Iterable<? extends Disposable> resources2) {
        ObjectHelper.requireNonNull(resources2, "resources is null");
        this.resources = new OpenHashSet<>();
        for (Disposable d : resources2) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public void dispose() {
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    this.disposed = true;
                    OpenHashSet<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean add(@NonNull Disposable d) {
        ObjectHelper.requireNonNull(d, "d is null");
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    if (set == null) {
                        set = new OpenHashSet<>();
                        this.resources = set;
                    }
                    set.add(d);
                    return true;
                }
            }
        }
        d.dispose();
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v3, types: [int] */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addAll(@io.reactivex.annotations.NonNull io.reactivex.disposables.Disposable... r6) {
        /*
            r5 = this;
            r2 = 0
            java.lang.String r3 = "ds is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r6, r3)
            boolean r3 = r5.disposed
            if (r3 != 0) goto L_0x0033
            monitor-enter(r5)
            boolean r3 = r5.disposed     // Catch:{ all -> 0x003f }
            if (r3 != 0) goto L_0x0032
            io.reactivex.internal.util.OpenHashSet<io.reactivex.disposables.Disposable> r1 = r5.resources     // Catch:{ all -> 0x003f }
            if (r1 != 0) goto L_0x001e
            io.reactivex.internal.util.OpenHashSet r1 = new io.reactivex.internal.util.OpenHashSet     // Catch:{ all -> 0x003f }
            int r3 = r6.length     // Catch:{ all -> 0x003f }
            int r3 = r3 + 1
            r1.<init>(r3)     // Catch:{ all -> 0x003f }
            r5.resources = r1     // Catch:{ all -> 0x003f }
        L_0x001e:
            int r3 = r6.length     // Catch:{ all -> 0x003f }
        L_0x001f:
            if (r2 >= r3) goto L_0x002f
            r0 = r6[r2]     // Catch:{ all -> 0x003f }
            java.lang.String r4 = "d is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r0, r4)     // Catch:{ all -> 0x003f }
            r1.add(r0)     // Catch:{ all -> 0x003f }
            int r2 = r2 + 1
            goto L_0x001f
        L_0x002f:
            r2 = 1
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
        L_0x0031:
            return r2
        L_0x0032:
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
        L_0x0033:
            int r4 = r6.length
            r3 = r2
        L_0x0035:
            if (r3 >= r4) goto L_0x0031
            r0 = r6[r3]
            r0.dispose()
            int r3 = r3 + 1
            goto L_0x0035
        L_0x003f:
            r2 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.disposables.CompositeDisposable.addAll(io.reactivex.disposables.Disposable[]):boolean");
    }

    public boolean remove(@NonNull Disposable d) {
        if (!delete(d)) {
            return false;
        }
        d.dispose();
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean delete(@io.reactivex.annotations.NonNull io.reactivex.disposables.Disposable r4) {
        /*
            r3 = this;
            r1 = 0
            java.lang.String r2 = "Disposable item is null"
            io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r2)
            boolean r2 = r3.disposed
            if (r2 == 0) goto L_0x000c
        L_0x000b:
            return r1
        L_0x000c:
            monitor-enter(r3)
            boolean r2 = r3.disposed     // Catch:{ all -> 0x0013 }
            if (r2 == 0) goto L_0x0016
            monitor-exit(r3)     // Catch:{ all -> 0x0013 }
            goto L_0x000b
        L_0x0013:
            r1 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0013 }
            throw r1
        L_0x0016:
            io.reactivex.internal.util.OpenHashSet<io.reactivex.disposables.Disposable> r0 = r3.resources     // Catch:{ all -> 0x0013 }
            if (r0 == 0) goto L_0x0020
            boolean r2 = r0.remove(r4)     // Catch:{ all -> 0x0013 }
            if (r2 != 0) goto L_0x0022
        L_0x0020:
            monitor-exit(r3)     // Catch:{ all -> 0x0013 }
            goto L_0x000b
        L_0x0022:
            monitor-exit(r3)     // Catch:{ all -> 0x0013 }
            r1 = 1
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.disposables.CompositeDisposable.delete(io.reactivex.disposables.Disposable):boolean");
    }

    public void clear() {
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    this.resources = null;
                    dispose(set);
                }
            }
        }
    }

    public int size() {
        int i = 0;
        if (!this.disposed) {
            synchronized (this) {
                if (!this.disposed) {
                    OpenHashSet<Disposable> set = this.resources;
                    if (set != null) {
                        i = set.size();
                    }
                }
            }
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public void dispose(OpenHashSet<Disposable> set) {
        if (set != null) {
            List<Throwable> errors = null;
            for (Object o : set.keys()) {
                if (o instanceof Disposable) {
                    try {
                        ((Disposable) o).dispose();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (errors == null) {
                            errors = new ArrayList<>();
                        }
                        errors.add(ex);
                    }
                }
            }
            if (errors == null) {
                return;
            }
            if (errors.size() == 1) {
                throw ExceptionHelper.wrapOrThrow(errors.get(0));
            }
            throw new CompositeException((Iterable<? extends Throwable>) errors);
        }
    }
}
