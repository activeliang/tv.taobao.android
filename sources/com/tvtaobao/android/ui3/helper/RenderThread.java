package com.tvtaobao.android.ui3.helper;

import android.util.Log;
import com.tvtaobao.android.values.Flag;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class RenderThread extends Thread {
    private static final int FLAG_RENDER_THREAD_DEAD = 2;
    private static final int FLAG_RENDER_THREAD_EXIT = 1;
    private static final int FLAG_RENDER_THREAD_LOG = 8;
    private static final int FLAG_RENDER_THREAD_PAUSE = 16;
    private static final int FLAG_RENDER_THREAD_RUNNING = 4;
    private static String TAG = RenderThread.class.getSimpleName();
    /* access modifiers changed from: private */
    public List<WeakReference<RenderClient>> clients = new ArrayList();
    private Flag flag = new Flag();
    private long frameCost;
    private AtomicLong frameCount = new AtomicLong();
    private long frameInterval = 16;
    private long frameTime;
    private long nowTime;
    private Queue<Runnable> renderMsgQueue = new LinkedBlockingQueue(24);
    private long tmpDuration;
    private Queue<Runnable> tmpRenderMsgQueue = new LinkedBlockingQueue(48);

    public interface RenderClient {
        void doRender(long j, long j2);

        void onThreadKilled();
    }

    public void register(final RenderClient renderClient) {
        if (renderClient != null) {
            postTask(new Runnable() {
                public void run() {
                    RenderThread.this.clients.add(new WeakReference(renderClient));
                }
            });
        }
    }

    public void unregister(final RenderClient renderClient) {
        if (renderClient != null) {
            postTask(new Runnable() {
                public void run() {
                    int i = 0;
                    while (i < RenderThread.this.clients.size()) {
                        WeakReference<RenderClient> wrrc = (WeakReference) RenderThread.this.clients.get(i);
                        if (wrrc == null || wrrc.get() != renderClient) {
                            i++;
                        } else {
                            RenderThread.this.clients.remove(wrrc);
                            return;
                        }
                    }
                }
            });
        }
    }

    public void postTask(Runnable task) {
        synchronized (this.renderMsgQueue) {
            this.renderMsgQueue.add(task);
        }
    }

    public void pause(boolean b) {
        log(TAG, "pause " + b);
        if (b) {
            this.flag.setFlag(16);
            return;
        }
        this.flag.clrFlag(16);
        try {
            synchronized (this.flag) {
                this.flag.notify();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0041, code lost:
        if (r1 >= r11.clients.size()) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0043, code lost:
        r3 = r11.clients.get(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        if (r3 == null) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0051, code lost:
        if (r3.get() == null) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        ((com.tvtaobao.android.ui3.helper.RenderThread.RenderClient) r3.get()).onThreadKilled();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r11 = this;
            r10 = 4
            com.tvtaobao.android.values.Flag r4 = r11.flag
            r4.setFlag(r10)
            java.lang.String r4 = TAG
            java.lang.String r5 = "run enter"
            r11.log(r4, r5)
        L_0x000e:
            com.tvtaobao.android.values.Flag r4 = r11.flag     // Catch:{ Throwable -> 0x0067 }
            r5 = 16
            boolean r4 = r4.hasFlag(r5)     // Catch:{ Throwable -> 0x0067 }
            if (r4 == 0) goto L_0x0031
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x0062 }
            java.lang.String r5 = "RenderThread pause"
            r11.log(r4, r5)     // Catch:{ Throwable -> 0x0062 }
            com.tvtaobao.android.values.Flag r5 = r11.flag     // Catch:{ Throwable -> 0x0062 }
            monitor-enter(r5)     // Catch:{ Throwable -> 0x0062 }
            com.tvtaobao.android.values.Flag r4 = r11.flag     // Catch:{ all -> 0x005f }
            r4.wait()     // Catch:{ all -> 0x005f }
            monitor-exit(r5)     // Catch:{ all -> 0x005f }
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x0062 }
            java.lang.String r5 = "RenderThread resume"
            r11.log(r4, r5)     // Catch:{ Throwable -> 0x0062 }
        L_0x0031:
            com.tvtaobao.android.values.Flag r4 = r11.flag     // Catch:{ Throwable -> 0x0067 }
            r5 = 1
            boolean r4 = r4.hasFlag(r5)     // Catch:{ Throwable -> 0x0067 }
            if (r4 == 0) goto L_0x0084
            r1 = 0
        L_0x003b:
            java.util.List<java.lang.ref.WeakReference<com.tvtaobao.android.ui3.helper.RenderThread$RenderClient>> r4 = r11.clients     // Catch:{ Throwable -> 0x0067 }
            int r4 = r4.size()     // Catch:{ Throwable -> 0x0067 }
            if (r1 >= r4) goto L_0x006b
            java.util.List<java.lang.ref.WeakReference<com.tvtaobao.android.ui3.helper.RenderThread$RenderClient>> r4 = r11.clients     // Catch:{ Throwable -> 0x0067 }
            java.lang.Object r3 = r4.get(r1)     // Catch:{ Throwable -> 0x0067 }
            java.lang.ref.WeakReference r3 = (java.lang.ref.WeakReference) r3     // Catch:{ Throwable -> 0x0067 }
            if (r3 == 0) goto L_0x005c
            java.lang.Object r4 = r3.get()     // Catch:{ Throwable -> 0x0067 }
            if (r4 == 0) goto L_0x005c
            java.lang.Object r4 = r3.get()     // Catch:{ Throwable -> 0x007f }
            com.tvtaobao.android.ui3.helper.RenderThread$RenderClient r4 = (com.tvtaobao.android.ui3.helper.RenderThread.RenderClient) r4     // Catch:{ Throwable -> 0x007f }
            r4.onThreadKilled()     // Catch:{ Throwable -> 0x007f }
        L_0x005c:
            int r1 = r1 + 1
            goto L_0x003b
        L_0x005f:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x005f }
            throw r4     // Catch:{ Throwable -> 0x0062 }
        L_0x0062:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0067 }
            goto L_0x0031
        L_0x0067:
            r0 = move-exception
            r0.printStackTrace()
        L_0x006b:
            java.lang.String r4 = TAG
            java.lang.String r5 = "run leave"
            r11.log(r4, r5)
            com.tvtaobao.android.values.Flag r4 = r11.flag
            r4.clrFlag(r10)
            com.tvtaobao.android.values.Flag r4 = r11.flag
            r5 = 2
            r4.setFlag(r5)
            return
        L_0x007f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0067 }
            goto L_0x005c
        L_0x0084:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0067 }
            r11.nowTime = r4     // Catch:{ Throwable -> 0x0067 }
            long r4 = r11.nowTime     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameTime     // Catch:{ Throwable -> 0x0067 }
            long r4 = r4 - r6
            r11.tmpDuration = r4     // Catch:{ Throwable -> 0x0067 }
            long r4 = r11.tmpDuration     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameInterval     // Catch:{ Throwable -> 0x0067 }
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 < 0) goto L_0x0170
            long r4 = r11.nowTime     // Catch:{ Throwable -> 0x0067 }
            r11.frameTime = r4     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x0067 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0067 }
            r5.<init>()     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r6 = "RenderThread drawFrame "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameTime     // Catch:{ Throwable -> 0x0067 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0067 }
            r11.log(r4, r5)     // Catch:{ Throwable -> 0x0067 }
            java.util.Queue<java.lang.Runnable> r5 = r11.renderMsgQueue     // Catch:{ Throwable -> 0x0067 }
            monitor-enter(r5)     // Catch:{ Throwable -> 0x0067 }
            java.util.Queue<java.lang.Runnable> r4 = r11.tmpRenderMsgQueue     // Catch:{ all -> 0x00f9 }
            java.util.Queue<java.lang.Runnable> r6 = r11.renderMsgQueue     // Catch:{ all -> 0x00f9 }
            r4.addAll(r6)     // Catch:{ all -> 0x00f9 }
            java.util.Queue<java.lang.Runnable> r4 = r11.renderMsgQueue     // Catch:{ all -> 0x00f9 }
            r4.clear()     // Catch:{ all -> 0x00f9 }
            java.lang.String r4 = TAG     // Catch:{ all -> 0x00f9 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f9 }
            r6.<init>()     // Catch:{ all -> 0x00f9 }
            java.lang.String r7 = "tmpRenderMsgQueue size:"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00f9 }
            java.util.Queue<java.lang.Runnable> r7 = r11.tmpRenderMsgQueue     // Catch:{ all -> 0x00f9 }
            int r7 = r7.size()     // Catch:{ all -> 0x00f9 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00f9 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00f9 }
            r11.log(r4, r6)     // Catch:{ all -> 0x00f9 }
        L_0x00e6:
            java.util.Queue<java.lang.Runnable> r4 = r11.tmpRenderMsgQueue     // Catch:{ all -> 0x00f9 }
            java.lang.Object r2 = r4.poll()     // Catch:{ all -> 0x00f9 }
            java.lang.Runnable r2 = (java.lang.Runnable) r2     // Catch:{ all -> 0x00f9 }
            if (r2 == 0) goto L_0x00fc
            r2.run()     // Catch:{ Throwable -> 0x00f4 }
            goto L_0x00e6
        L_0x00f4:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x00f9 }
            goto L_0x00e6
        L_0x00f9:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00f9 }
            throw r4     // Catch:{ Throwable -> 0x0067 }
        L_0x00fc:
            monitor-exit(r5)     // Catch:{ all -> 0x00f9 }
            r1 = 0
        L_0x00fe:
            java.util.List<java.lang.ref.WeakReference<com.tvtaobao.android.ui3.helper.RenderThread$RenderClient>> r4 = r11.clients     // Catch:{ Throwable -> 0x0067 }
            int r4 = r4.size()     // Catch:{ Throwable -> 0x0067 }
            if (r1 >= r4) goto L_0x012f
            java.util.List<java.lang.ref.WeakReference<com.tvtaobao.android.ui3.helper.RenderThread$RenderClient>> r4 = r11.clients     // Catch:{ Throwable -> 0x0067 }
            java.lang.Object r3 = r4.get(r1)     // Catch:{ Throwable -> 0x0067 }
            java.lang.ref.WeakReference r3 = (java.lang.ref.WeakReference) r3     // Catch:{ Throwable -> 0x0067 }
            if (r3 == 0) goto L_0x0127
            java.lang.Object r4 = r3.get()     // Catch:{ Throwable -> 0x0067 }
            if (r4 == 0) goto L_0x0127
            java.lang.Object r4 = r3.get()     // Catch:{ Throwable -> 0x012a }
            com.tvtaobao.android.ui3.helper.RenderThread$RenderClient r4 = (com.tvtaobao.android.ui3.helper.RenderThread.RenderClient) r4     // Catch:{ Throwable -> 0x012a }
            long r6 = r11.frameTime     // Catch:{ Throwable -> 0x012a }
            java.util.concurrent.atomic.AtomicLong r5 = r11.frameCount     // Catch:{ Throwable -> 0x012a }
            long r8 = r5.get()     // Catch:{ Throwable -> 0x012a }
            r4.doRender(r6, r8)     // Catch:{ Throwable -> 0x012a }
        L_0x0127:
            int r1 = r1 + 1
            goto L_0x00fe
        L_0x012a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0067 }
            goto L_0x0127
        L_0x012f:
            java.util.concurrent.atomic.AtomicLong r4 = r11.frameCount     // Catch:{ Throwable -> 0x0067 }
            r4.incrementAndGet()     // Catch:{ Throwable -> 0x0067 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameTime     // Catch:{ Throwable -> 0x0067 }
            long r4 = r4 - r6
            r11.frameCost = r4     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r4 = TAG     // Catch:{ Throwable -> 0x0067 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0067 }
            r5.<init>()     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r6 = "RenderThread drawFrame cost "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameCost     // Catch:{ Throwable -> 0x0067 }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Throwable -> 0x0067 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0067 }
            r11.log(r4, r5)     // Catch:{ Throwable -> 0x0067 }
            long r4 = r11.frameInterval     // Catch:{ Throwable -> 0x0067 }
            long r6 = r11.frameCost     // Catch:{ Throwable -> 0x0067 }
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 <= 0) goto L_0x000e
            long r4 = r11.frameInterval     // Catch:{ Throwable -> 0x016a }
            long r6 = r11.frameCost     // Catch:{ Throwable -> 0x016a }
            long r4 = r4 - r6
            java.lang.Thread.sleep(r4)     // Catch:{ Throwable -> 0x016a }
            goto L_0x000e
        L_0x016a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0067 }
            goto L_0x000e
        L_0x0170:
            long r4 = r11.frameInterval     // Catch:{ Throwable -> 0x017a }
            long r6 = r11.tmpDuration     // Catch:{ Throwable -> 0x017a }
            long r4 = r4 - r6
            java.lang.Thread.sleep(r4)     // Catch:{ Throwable -> 0x017a }
            goto L_0x000e
        L_0x017a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0067 }
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvtaobao.android.ui3.helper.RenderThread.run():void");
    }

    private void log(String tag, String msg) {
        if (this.flag.hasFlag(8)) {
            Log.v(tag, msg);
        }
    }

    public void turnOnLog(boolean b) {
        if (b) {
            this.flag.setFlag(8);
        } else {
            this.flag.clrFlag(8);
        }
    }

    public synchronized void startDrawThread() {
        if (!this.flag.hasFlag(4)) {
            start();
            do {
            } while (!this.flag.hasFlag(4));
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized void killDrawThread() {
        /*
            r3 = this;
            monitor-enter(r3)
            com.tvtaobao.android.values.Flag r1 = r3.flag     // Catch:{ all -> 0x0034 }
            r2 = 1
            r1.setFlag(r2)     // Catch:{ all -> 0x0034 }
            com.tvtaobao.android.values.Flag r1 = r3.flag     // Catch:{ all -> 0x0034 }
            r2 = 16
            boolean r1 = r1.hasFlag(r2)     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0021
            com.tvtaobao.android.values.Flag r1 = r3.flag     // Catch:{ all -> 0x0034 }
            r2 = 16
            r1.clrFlag(r2)     // Catch:{ all -> 0x0034 }
            com.tvtaobao.android.values.Flag r2 = r3.flag     // Catch:{ Throwable -> 0x002f }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x002f }
            com.tvtaobao.android.values.Flag r1 = r3.flag     // Catch:{ all -> 0x002c }
            r1.notify()     // Catch:{ all -> 0x002c }
            monitor-exit(r2)     // Catch:{ all -> 0x002c }
        L_0x0021:
            com.tvtaobao.android.values.Flag r1 = r3.flag     // Catch:{ all -> 0x0034 }
            r2 = 2
            boolean r1 = r1.hasFlag(r2)     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0021
            monitor-exit(r3)
            return
        L_0x002c:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x002c }
            throw r1     // Catch:{ Throwable -> 0x002f }
        L_0x002f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0034 }
            goto L_0x0021
        L_0x0034:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvtaobao.android.ui3.helper.RenderThread.killDrawThread():void");
    }

    public long getFrameInterval() {
        return this.frameInterval;
    }

    public void setFrameInterval(long frameInterval2) {
        this.frameInterval = frameInterval2;
    }
}
