package com.bftv.fui.thirdparty.notify;

import com.bftv.fui.thirdparty.InterceptionData;
import java.util.ArrayList;

public class VoiceObservable {
    private boolean changed = false;
    private final ArrayList<IVoiceObserver> observers = new ArrayList<>();

    public synchronized void addObserver(IVoiceObserver o) {
        if (o == null) {
            throw new NullPointerException();
        } else if (!this.observers.contains(o)) {
            this.observers.add(o);
        }
    }

    public synchronized void deleteObserver(IVoiceObserver o) {
        this.observers.remove(o);
    }

    public void notifyObservers() {
        notifyObservers((InterceptionData) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        if (r1 < 0) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r2 = r0[r1].update(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r2 == null) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        if (r2.isHasResult != false) goto L_0x000a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        r1 = r1 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        r1 = r0.length - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.bftv.fui.thirdparty.VoiceFeedback notifyObservers(com.bftv.fui.thirdparty.InterceptionData r7) {
        /*
            r6 = this;
            r3 = 0
            monitor-enter(r6)
            boolean r4 = r6.hasChanged()     // Catch:{ all -> 0x0033 }
            if (r4 != 0) goto L_0x000b
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            r2 = r3
        L_0x000a:
            return r2
        L_0x000b:
            java.util.ArrayList<com.bftv.fui.thirdparty.notify.IVoiceObserver> r4 = r6.observers     // Catch:{ all -> 0x0033 }
            java.util.ArrayList<com.bftv.fui.thirdparty.notify.IVoiceObserver> r5 = r6.observers     // Catch:{ all -> 0x0033 }
            int r5 = r5.size()     // Catch:{ all -> 0x0033 }
            com.bftv.fui.thirdparty.notify.IVoiceObserver[] r5 = new com.bftv.fui.thirdparty.notify.IVoiceObserver[r5]     // Catch:{ all -> 0x0033 }
            java.lang.Object[] r0 = r4.toArray(r5)     // Catch:{ all -> 0x0033 }
            com.bftv.fui.thirdparty.notify.IVoiceObserver[] r0 = (com.bftv.fui.thirdparty.notify.IVoiceObserver[]) r0     // Catch:{ all -> 0x0033 }
            r6.clearChanged()     // Catch:{ all -> 0x0033 }
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            int r4 = r0.length
            int r1 = r4 + -1
        L_0x0022:
            if (r1 < 0) goto L_0x0036
            r4 = r0[r1]
            com.bftv.fui.thirdparty.VoiceFeedback r2 = r4.update(r7)
            if (r2 == 0) goto L_0x0030
            boolean r4 = r2.isHasResult
            if (r4 != 0) goto L_0x000a
        L_0x0030:
            int r1 = r1 + -1
            goto L_0x0022
        L_0x0033:
            r3 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0033 }
            throw r3
        L_0x0036:
            r2 = r3
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bftv.fui.thirdparty.notify.VoiceObservable.notifyObservers(com.bftv.fui.thirdparty.InterceptionData):com.bftv.fui.thirdparty.VoiceFeedback");
    }

    public synchronized void deleteObservers() {
        this.observers.clear();
    }

    /* access modifiers changed from: protected */
    public synchronized void setChanged() {
        this.changed = true;
    }

    /* access modifiers changed from: protected */
    public synchronized void clearChanged() {
        this.changed = false;
    }

    public synchronized boolean hasChanged() {
        return this.changed;
    }

    public synchronized int countObservers() {
        return this.observers.size();
    }
}
