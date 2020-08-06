package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock = new Object();
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<>();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<>();
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap<>();

    private static class ReceiverRecord {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter _filter, BroadcastReceiver _receiver) {
            this.filter = _filter;
            this.receiver = _receiver;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(128);
            builder.append("Receiver{");
            builder.append(this.receiver);
            builder.append(" filter=");
            builder.append(this.filter);
            builder.append("}");
            return builder.toString();
        }
    }

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent _intent, ArrayList<ReceiverRecord> _receivers) {
            this.intent = _intent;
            this.receivers = _receivers;
        }
    }

    public static LocalBroadcastManager getInstance(Context context) {
        LocalBroadcastManager localBroadcastManager;
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            localBroadcastManager = mInstance;
        }
        return localBroadcastManager;
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        LocalBroadcastManager.this.executePendingBroadcasts();
                        return;
                    default:
                        super.handleMessage(msg);
                        return;
                }
            }
        };
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        synchronized (this.mReceivers) {
            ReceiverRecord entry = new ReceiverRecord(filter, receiver);
            ArrayList<IntentFilter> filters = this.mReceivers.get(receiver);
            if (filters == null) {
                filters = new ArrayList<>(1);
                this.mReceivers.put(receiver, filters);
            }
            filters.add(filter);
            for (int i = 0; i < filter.countActions(); i++) {
                String action = filter.getAction(i);
                ArrayList<ReceiverRecord> entries = this.mActions.get(action);
                if (entries == null) {
                    entries = new ArrayList<>(1);
                    this.mActions.put(action, entries);
                }
                entries.add(entry);
            }
        }
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        synchronized (this.mReceivers) {
            ArrayList<IntentFilter> filters = this.mReceivers.remove(receiver);
            if (filters != null) {
                for (int i = 0; i < filters.size(); i++) {
                    IntentFilter filter = filters.get(i);
                    for (int j = 0; j < filter.countActions(); j++) {
                        String action = filter.getAction(j);
                        ArrayList<ReceiverRecord> receivers = this.mActions.get(action);
                        if (receivers != null) {
                            int k = 0;
                            while (k < receivers.size()) {
                                if (receivers.get(k).receiver == receiver) {
                                    receivers.remove(k);
                                    k--;
                                }
                                k++;
                            }
                            if (receivers.size() <= 0) {
                                this.mActions.remove(action);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x018a, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendBroadcast(android.content.Intent r18) {
        /*
            r17 = this;
            r0 = r17
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>> r15 = r0.mReceivers
            monitor-enter(r15)
            java.lang.String r2 = r18.getAction()     // Catch:{ all -> 0x0118 }
            r0 = r17
            android.content.Context r1 = r0.mAppContext     // Catch:{ all -> 0x0118 }
            android.content.ContentResolver r1 = r1.getContentResolver()     // Catch:{ all -> 0x0118 }
            r0 = r18
            java.lang.String r3 = r0.resolveTypeIfNeeded(r1)     // Catch:{ all -> 0x0118 }
            android.net.Uri r5 = r18.getData()     // Catch:{ all -> 0x0118 }
            java.lang.String r4 = r18.getScheme()     // Catch:{ all -> 0x0118 }
            java.util.Set r6 = r18.getCategories()     // Catch:{ all -> 0x0118 }
            int r1 = r18.getFlags()     // Catch:{ all -> 0x0118 }
            r1 = r1 & 8
            if (r1 == 0) goto L_0x00d8
            r8 = 1
        L_0x002c:
            if (r8 == 0) goto L_0x0066
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r7.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = "Resolving type "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.StringBuilder r7 = r7.append(r3)     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = " scheme "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.StringBuilder r7 = r7.append(r4)     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = " of intent "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            r0 = r18
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0118 }
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
        L_0x0066:
            r0 = r17
            java.util.HashMap<java.lang.String, java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord>> r1 = r0.mActions     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r18.getAction()     // Catch:{ all -> 0x0118 }
            java.lang.Object r9 = r1.get(r7)     // Catch:{ all -> 0x0118 }
            java.util.ArrayList r9 = (java.util.ArrayList) r9     // Catch:{ all -> 0x0118 }
            if (r9 == 0) goto L_0x0189
            if (r8 == 0) goto L_0x0094
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r7.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = "Action list: "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0118 }
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
        L_0x0094:
            r14 = 0
            r10 = 0
        L_0x0096:
            int r1 = r9.size()     // Catch:{ all -> 0x0118 }
            if (r10 >= r1) goto L_0x0150
            java.lang.Object r13 = r9.get(r10)     // Catch:{ all -> 0x0118 }
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r13 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r13     // Catch:{ all -> 0x0118 }
            if (r8 == 0) goto L_0x00c6
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r7.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = "Matching against filter "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            android.content.IntentFilter r0 = r13.filter     // Catch:{ all -> 0x0118 }
            r16 = r0
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0118 }
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
        L_0x00c6:
            boolean r1 = r13.broadcasting     // Catch:{ all -> 0x0118 }
            if (r1 == 0) goto L_0x00db
            if (r8 == 0) goto L_0x00d5
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.String r7 = "  Filter's target already added"
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
        L_0x00d5:
            int r10 = r10 + 1
            goto L_0x0096
        L_0x00d8:
            r8 = 0
            goto L_0x002c
        L_0x00db:
            android.content.IntentFilter r1 = r13.filter     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = "LocalBroadcastManager"
            int r11 = r1.match(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0118 }
            if (r11 < 0) goto L_0x011b
            if (r8 == 0) goto L_0x010a
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r7.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = "  Filter matched!  match=0x"
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = java.lang.Integer.toHexString(r11)     // Catch:{ all -> 0x0118 }
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0118 }
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
        L_0x010a:
            if (r14 != 0) goto L_0x0111
            java.util.ArrayList r14 = new java.util.ArrayList     // Catch:{ all -> 0x0118 }
            r14.<init>()     // Catch:{ all -> 0x0118 }
        L_0x0111:
            r14.add(r13)     // Catch:{ all -> 0x0118 }
            r1 = 1
            r13.broadcasting = r1     // Catch:{ all -> 0x0118 }
            goto L_0x00d5
        L_0x0118:
            r1 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x0118 }
            throw r1
        L_0x011b:
            if (r8 == 0) goto L_0x00d5
            switch(r11) {
                case -4: goto L_0x0144;
                case -3: goto L_0x0140;
                case -2: goto L_0x0148;
                case -1: goto L_0x014c;
                default: goto L_0x0120;
            }
        L_0x0120:
            java.lang.String r12 = "unknown reason"
        L_0x0123:
            java.lang.String r1 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r7.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r16 = "  Filter did not match: "
            r0 = r16
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ all -> 0x0118 }
            java.lang.StringBuilder r7 = r7.append(r12)     // Catch:{ all -> 0x0118 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0118 }
            android.util.Log.v(r1, r7)     // Catch:{ all -> 0x0118 }
            goto L_0x00d5
        L_0x0140:
            java.lang.String r12 = "action"
            goto L_0x0123
        L_0x0144:
            java.lang.String r12 = "category"
            goto L_0x0123
        L_0x0148:
            java.lang.String r12 = "data"
            goto L_0x0123
        L_0x014c:
            java.lang.String r12 = "type"
            goto L_0x0123
        L_0x0150:
            if (r14 == 0) goto L_0x0189
            r10 = 0
        L_0x0153:
            int r1 = r14.size()     // Catch:{ all -> 0x0118 }
            if (r10 >= r1) goto L_0x0165
            java.lang.Object r1 = r14.get(r10)     // Catch:{ all -> 0x0118 }
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r1 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r1     // Catch:{ all -> 0x0118 }
            r7 = 0
            r1.broadcasting = r7     // Catch:{ all -> 0x0118 }
            int r10 = r10 + 1
            goto L_0x0153
        L_0x0165:
            r0 = r17
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r1 = r0.mPendingBroadcasts     // Catch:{ all -> 0x0118 }
            android.support.v4.content.LocalBroadcastManager$BroadcastRecord r7 = new android.support.v4.content.LocalBroadcastManager$BroadcastRecord     // Catch:{ all -> 0x0118 }
            r0 = r18
            r7.<init>(r0, r14)     // Catch:{ all -> 0x0118 }
            r1.add(r7)     // Catch:{ all -> 0x0118 }
            r0 = r17
            android.os.Handler r1 = r0.mHandler     // Catch:{ all -> 0x0118 }
            r7 = 1
            boolean r1 = r1.hasMessages(r7)     // Catch:{ all -> 0x0118 }
            if (r1 != 0) goto L_0x0186
            r0 = r17
            android.os.Handler r1 = r0.mHandler     // Catch:{ all -> 0x0118 }
            r7 = 1
            r1.sendEmptyMessage(r7)     // Catch:{ all -> 0x0118 }
        L_0x0186:
            r1 = 1
            monitor-exit(r15)     // Catch:{ all -> 0x0118 }
        L_0x0188:
            return r1
        L_0x0189:
            monitor-exit(r15)     // Catch:{ all -> 0x0118 }
            r1 = 0
            goto L_0x0188
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.LocalBroadcastManager.sendBroadcast(android.content.Intent):boolean");
    }

    public void sendBroadcastSync(Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
        if (r3 >= r2.length) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        r1 = r2[r3];
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        if (r4 >= r1.receivers.size()) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002a, code lost:
        r1.receivers.get(r4).receiver.onReceive(r8.mAppContext, r1.intent);
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0041, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        r3 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executePendingBroadcasts() {
        /*
            r8 = this;
        L_0x0000:
            r2 = 0
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>> r6 = r8.mReceivers
            monitor-enter(r6)
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r5 = r8.mPendingBroadcasts     // Catch:{ all -> 0x003e }
            int r0 = r5.size()     // Catch:{ all -> 0x003e }
            if (r0 > 0) goto L_0x000e
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            return
        L_0x000e:
            android.support.v4.content.LocalBroadcastManager$BroadcastRecord[] r2 = new android.support.v4.content.LocalBroadcastManager.BroadcastRecord[r0]     // Catch:{ all -> 0x003e }
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r5 = r8.mPendingBroadcasts     // Catch:{ all -> 0x003e }
            r5.toArray(r2)     // Catch:{ all -> 0x003e }
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$BroadcastRecord> r5 = r8.mPendingBroadcasts     // Catch:{ all -> 0x003e }
            r5.clear()     // Catch:{ all -> 0x003e }
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            r3 = 0
        L_0x001c:
            int r5 = r2.length
            if (r3 >= r5) goto L_0x0000
            r1 = r2[r3]
            r4 = 0
        L_0x0022:
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord> r5 = r1.receivers
            int r5 = r5.size()
            if (r4 >= r5) goto L_0x0041
            java.util.ArrayList<android.support.v4.content.LocalBroadcastManager$ReceiverRecord> r5 = r1.receivers
            java.lang.Object r5 = r5.get(r4)
            android.support.v4.content.LocalBroadcastManager$ReceiverRecord r5 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r5
            android.content.BroadcastReceiver r5 = r5.receiver
            android.content.Context r6 = r8.mAppContext
            android.content.Intent r7 = r1.intent
            r5.onReceive(r6, r7)
            int r4 = r4 + 1
            goto L_0x0022
        L_0x003e:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            throw r5
        L_0x0041:
            int r3 = r3 + 1
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.LocalBroadcastManager.executePendingBroadcasts():void");
    }
}
