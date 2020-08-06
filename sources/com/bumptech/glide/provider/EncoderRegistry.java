package com.bumptech.glide.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    @Nullable
    public synchronized <T> Encoder<T> getEncoder(@NonNull Class<T> dataClass) {
        Encoder<T> encoder;
        Iterator<Entry<?>> it = this.encoders.iterator();
        while (true) {
            if (!it.hasNext()) {
                encoder = null;
                break;
            }
            Entry<?> entry = it.next();
            if (entry.handles(dataClass)) {
                encoder = entry.encoder;
                break;
            }
        }
        return encoder;
    }

    public synchronized <T> void append(@NonNull Class<T> dataClass, @NonNull Encoder<T> encoder) {
        this.encoders.add(new Entry(dataClass, encoder));
    }

    public synchronized <T> void prepend(@NonNull Class<T> dataClass, @NonNull Encoder<T> encoder) {
        this.encoders.add(0, new Entry(dataClass, encoder));
    }

    private static final class Entry<T> {
        private final Class<T> dataClass;
        final Encoder<T> encoder;

        Entry(@NonNull Class<T> dataClass2, @NonNull Encoder<T> encoder2) {
            this.dataClass = dataClass2;
            this.encoder = encoder2;
        }

        /* access modifiers changed from: package-private */
        public boolean handles(@NonNull Class<?> dataClass2) {
            return this.dataClass.isAssignableFrom(dataClass2);
        }
    }
}
