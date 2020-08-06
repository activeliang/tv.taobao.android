package mtopsdk.mtop.intf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface MtopEnablePropertyType {
    public static final String ENABLE_HEADER_URL_ENCODE = "ENABLE_HEADER_URL_ENCODE";
    public static final String ENABLE_NEW_DEVICE_ID = "ENABLE_NEW_DEVICE_ID";
    public static final String ENABLE_NOTIFY_SESSION_RET = "ENABLE_NOTIFY_SESSION_RET";

    @Retention(RetentionPolicy.SOURCE)
    public @interface Definition {
    }
}
