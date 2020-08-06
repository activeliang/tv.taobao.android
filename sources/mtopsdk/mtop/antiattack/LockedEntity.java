package mtopsdk.mtop.antiattack;

/* compiled from: ApiLockHelper */
class LockedEntity {
    public String key;
    public long lockInterval;
    public long lockStartTime;

    public LockedEntity(String key2, long lockStartTime2, long lockInterval2) {
        this.key = key2;
        this.lockStartTime = lockStartTime2;
        this.lockInterval = lockInterval2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("LockedEntity [key=").append(this.key);
        builder.append(", lockStartTime=").append(this.lockStartTime);
        builder.append(", lockInterval=").append(this.lockInterval);
        builder.append("]");
        return builder.toString();
    }
}
