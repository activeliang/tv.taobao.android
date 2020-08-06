package android.taobao.windvane.thread;

public class LockObject {
    private boolean needwait = true;
    public int result = 0;

    public synchronized void lwait() {
        while (this.needwait) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void lnotify() {
        if (this.needwait) {
            this.needwait = false;
            notify();
        }
    }
}
