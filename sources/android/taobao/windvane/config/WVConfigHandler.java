package android.taobao.windvane.config;

public abstract class WVConfigHandler {
    private boolean isUpdating = false;
    private String snapshotN = "0";

    public abstract void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback);

    public void setUpdateStatus(boolean updating) {
        this.isUpdating = updating;
    }

    public boolean getUpdateStatus() {
        return this.isUpdating;
    }

    public void setSnapshotN(String version) {
        this.snapshotN = version;
    }

    public String getSnapshotN() {
        return this.snapshotN;
    }
}
