package com.taobao.atlas.dexmerge.dx.util;

public class MutabilityControl {
    private boolean mutable;

    public MutabilityControl() {
        this.mutable = true;
    }

    public MutabilityControl(boolean mutable2) {
        this.mutable = mutable2;
    }

    public void setImmutable() {
        this.mutable = false;
    }

    public final boolean isImmutable() {
        return !this.mutable;
    }

    public final boolean isMutable() {
        return this.mutable;
    }

    public final void throwIfImmutable() {
        if (!this.mutable) {
            throw new MutabilityException("immutable instance");
        }
    }

    public final void throwIfMutable() {
        if (this.mutable) {
            throw new MutabilityException("mutable instance");
        }
    }
}
