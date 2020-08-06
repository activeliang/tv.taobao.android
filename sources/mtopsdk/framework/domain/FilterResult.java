package mtopsdk.framework.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface FilterResult {
    public static final String CONTINUE = "CONTINUE";
    public static final String STOP = "STOP";

    @Retention(RetentionPolicy.SOURCE)
    public @interface Definition {
    }
}
