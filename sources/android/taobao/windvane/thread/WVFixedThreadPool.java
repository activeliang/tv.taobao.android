package android.taobao.windvane.thread;

import android.taobao.windvane.util.TaoLog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WVFixedThreadPool {
    private static final int CORE_POOL_SIZE = 1;
    private static ExecutorService SingleExecutor = null;
    public static int bufferSize = 4096;
    private static WVFixedThreadPool threadManager = null;
    private BufferWrapper tempBuffer = null;

    public static WVFixedThreadPool getInstance() {
        if (threadManager == null) {
            threadManager = new WVFixedThreadPool();
        }
        return threadManager;
    }

    public void execute(Runnable command) {
        if (SingleExecutor == null) {
            SingleExecutor = Executors.newFixedThreadPool(1);
        }
        if (command == null) {
            TaoLog.w("WVThreadPool", "executeSingle is null.");
        } else {
            SingleExecutor.execute(command);
        }
    }

    public BufferWrapper getTempBuffer() {
        if (this.tempBuffer == null) {
            this.tempBuffer = new BufferWrapper();
        }
        return this.tempBuffer;
    }

    public void reSetTempBuffer() {
        if (this.tempBuffer != null || this.tempBuffer.isFree) {
            this.tempBuffer.tempBuffer = null;
            boolean unused = this.tempBuffer.isFree = false;
            this.tempBuffer = null;
        }
    }

    public static class BufferWrapper {
        /* access modifiers changed from: private */
        public boolean isFree;
        public byte[] tempBuffer;

        BufferWrapper() {
            this.tempBuffer = null;
            this.isFree = false;
            this.tempBuffer = new byte[WVFixedThreadPool.bufferSize];
        }

        public boolean isFree() {
            return this.isFree;
        }

        public void setIsFree(boolean isfree) {
            this.isFree = isfree;
        }
    }
}
