package com.bumptech.glide.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.load.ResourceEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResourceEncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    public synchronized <Z> void append(@NonNull Class<Z> resourceClass, @NonNull ResourceEncoder<Z> encoder) {
        this.encoders.add(new Entry(resourceClass, encoder));
    }

    public synchronized <Z> void prepend(@NonNull Class<Z> resourceClass, @NonNull ResourceEncoder<Z> encoder) {
        this.encoders.add(0, new Entry(resourceClass, encoder));
    }

    @Nullable
    public synchronized <Z> ResourceEncoder<Z> get(@NonNull Class<Z> resourceClass) {
        ResourceEncoder<T> resourceEncoder;
        int i = 0;
        int size = this.encoders.size();
        while (true) {
            if (i >= size) {
                resourceEncoder = null;
                break;
            }
            Entry<?> entry = this.encoders.get(i);
            if (entry.handles(resourceClass)) {
                resourceEncoder = entry.encoder;
                break;
            }
            i++;
        }
        return resourceEncoder;
    }

    private static final class Entry<T> {
        final ResourceEncoder<T> encoder;
        private final Class<T> resourceClass;

        Entry(@NonNull Class<T> resourceClass2, @NonNull ResourceEncoder<T> encoder2) {
            this.resourceClass = resourceClass2;
            this.encoder = encoder2;
        }

        /* access modifiers changed from: package-private */
        public boolean handles(@NonNull Class<?> resourceClass2) {
            return this.resourceClass.isAssignableFrom(resourceClass2);
        }
    }
}
