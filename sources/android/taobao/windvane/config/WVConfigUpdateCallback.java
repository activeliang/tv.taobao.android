package android.taobao.windvane.config;

public interface WVConfigUpdateCallback {

    public enum CONFIG_UPDATE_STATUS {
        SUCCESS,
        UPDATE_DISABLED,
        UPDATE_CANCELED,
        NULL_DATA,
        ENCODING_ERROR,
        NOT_AVAIABLE,
        NO_VERSION,
        UNKNOWN_ERROR,
        UPDATE_COUNT_LIMIT,
        NETWORK_NOT_SUPPORT
    }

    void updateStatus(CONFIG_UPDATE_STATUS config_update_status, int i);
}
