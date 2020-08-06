package com.bumptech.glide.load.resource.transcode;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class TranscoderRegistry {
    private final List<Entry<?, ?>> transcoders = new ArrayList();

    public synchronized <Z, R> void register(@NonNull Class<Z> decodedClass, @NonNull Class<R> transcodedClass, @NonNull ResourceTranscoder<Z, R> transcoder) {
        this.transcoders.add(new Entry(decodedClass, transcodedClass, transcoder));
    }

    @NonNull
    public synchronized <Z, R> ResourceTranscoder<Z, R> get(@NonNull Class<Z> resourceClass, @NonNull Class<R> transcodedClass) {
        ResourceTranscoder<Z, R> resourceTranscoder;
        if (transcodedClass.isAssignableFrom(resourceClass)) {
            resourceTranscoder = UnitTranscoder.get();
        } else {
            for (Entry<?, ?> entry : this.transcoders) {
                if (entry.handles(resourceClass, transcodedClass)) {
                    resourceTranscoder = entry.transcoder;
                }
            }
            throw new IllegalArgumentException("No transcoder registered to transcode from " + resourceClass + " to " + transcodedClass);
        }
        return resourceTranscoder;
    }

    @NonNull
    public synchronized <Z, R> List<Class<R>> getTranscodeClasses(@NonNull Class<Z> resourceClass, @NonNull Class<R> transcodeClass) {
        List<Class<R>> transcodeClasses;
        transcodeClasses = new ArrayList<>();
        if (transcodeClass.isAssignableFrom(resourceClass)) {
            transcodeClasses.add(transcodeClass);
        } else {
            for (Entry<?, ?> entry : this.transcoders) {
                if (entry.handles(resourceClass, transcodeClass)) {
                    transcodeClasses.add(transcodeClass);
                }
            }
        }
        return transcodeClasses;
    }

    private static final class Entry<Z, R> {
        private final Class<Z> fromClass;
        private final Class<R> toClass;
        final ResourceTranscoder<Z, R> transcoder;

        Entry(@NonNull Class<Z> fromClass2, @NonNull Class<R> toClass2, @NonNull ResourceTranscoder<Z, R> transcoder2) {
            this.fromClass = fromClass2;
            this.toClass = toClass2;
            this.transcoder = transcoder2;
        }

        public boolean handles(@NonNull Class<?> fromClass2, @NonNull Class<?> toClass2) {
            return this.fromClass.isAssignableFrom(fromClass2) && toClass2.isAssignableFrom(this.toClass);
        }
    }
}
