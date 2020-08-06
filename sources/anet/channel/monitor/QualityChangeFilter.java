package anet.channel.monitor;

public class QualityChangeFilter {
    private final double DEFAULT_SPEED_THRESHOLD = 40.0d;
    protected long filterAddTime;
    private boolean isNetSpeedSlow = false;
    private boolean shouldCheck = true;

    /* access modifiers changed from: package-private */
    public final boolean isNetSpeedSlow() {
        return this.isNetSpeedSlow;
    }

    /* access modifiers changed from: package-private */
    public final void setNetSpeedSlow(boolean netSpeedSlow) {
        this.isNetSpeedSlow = netSpeedSlow;
    }

    public int getDelay() {
        return 0;
    }

    public boolean detectNetSpeedSlow(double speed) {
        if (speed < 40.0d) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean checkShouldDelay() {
        if (!this.shouldCheck) {
            return false;
        }
        if (System.currentTimeMillis() - this.filterAddTime <= ((long) (getDelay() * 1000))) {
            return true;
        }
        this.shouldCheck = false;
        return false;
    }
}
