package com.taobao.alimama.threads;

import android.os.HandlerThread;
import android.os.Looper;
import java.util.HashMap;

public final class AdLooper {
    private static final String a = "alimama_ads";
    private static final HashMap<String, HandlerThread> b = new HashMap<>();

    private static HandlerThread a(String str) {
        HandlerThread handlerThread;
        synchronized (b) {
            handlerThread = b.get(str);
            if (handlerThread != null && handlerThread.getLooper() == null) {
                b.remove(str);
                handlerThread = null;
            }
            if (handlerThread == null) {
                handlerThread = new HandlerThread(str);
                handlerThread.start();
                b.put(str, handlerThread);
            }
        }
        return handlerThread;
    }

    public static Looper getLooper() {
        return a(a).getLooper();
    }
}
