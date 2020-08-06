package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ResourceObserver<T> implements Observer<T>, Disposable {
    private final ListCompositeDisposable resources = new ListCompositeDisposable();
    private final AtomicReference<Disposable> s = new AtomicReference<>();

    public final void add(@NonNull Disposable resource) {
        ObjectHelper.requireNonNull(resource, "resource is null");
        this.resources.add(resource);
    }

    public final void onSubscribe(Disposable s2) {
        if (EndConsumerHelper.setOnce(this.s, s2, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void dispose() {
        if (DisposableHelper.dispose(this.s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed(this.s.get());
    }
}
