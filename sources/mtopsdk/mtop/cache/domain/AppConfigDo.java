package mtopsdk.mtop.cache.domain;

import java.io.Serializable;

public class AppConfigDo implements Serializable {
    public String appConf;
    public long appConfigVersion;

    public AppConfigDo(String appConf2, long appConfigVersion2) {
        this.appConf = appConf2;
        this.appConfigVersion = appConfigVersion2;
    }
}
