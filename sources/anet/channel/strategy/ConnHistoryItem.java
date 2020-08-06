package anet.channel.strategy;

import java.io.Serializable;

class ConnHistoryItem implements Serializable {
    private static final int BAN_THRESHOLD = 3;
    private static final int BAN_TIME = 300000;
    private static final long UPDATE_INTERVAL = 10000;
    private static final long VALID_PERIOD = 86400000;
    private static final long serialVersionUID = 5245740801355223771L;
    byte history = 0;
    long lastFail = 0;
    long lastSuccess = 0;

    ConnHistoryItem() {
    }

    /* access modifiers changed from: package-private */
    public void update(boolean isSuccess) {
        long now = System.currentTimeMillis();
        if (now - (isSuccess ? this.lastSuccess : this.lastFail) > UPDATE_INTERVAL) {
            this.history = (byte) ((isSuccess ? 0 : 1) | (this.history << 1));
            if (isSuccess) {
                this.lastSuccess = now;
            } else {
                this.lastFail = now;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int countFail() {
        int count = 0;
        for (int tmp = this.history; tmp > 0; tmp >>= 1) {
            count += tmp & 1;
        }
        return count;
    }

    /* access modifiers changed from: package-private */
    public boolean latestFail() {
        return (this.history & 1) == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldBan() {
        if (countFail() >= 3 && System.currentTimeMillis() - this.lastFail <= 300000) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isExpire() {
        long tmp = this.lastSuccess > this.lastFail ? this.lastSuccess : this.lastFail;
        return tmp != 0 && System.currentTimeMillis() - tmp > 86400000;
    }
}
