package mtopsdk.mtop.cache.domain;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;
import mtopsdk.common.util.StringUtils;

public class ApiCacheDo implements Serializable {
    private static final long serialVersionUID = -6169477447314447135L;
    public String api;
    public String blockName;
    public String cacheControlHeader;
    public List<String> cacheKeyItems;
    public String cacheKeyType = "ALL";
    public boolean offline = false;
    public boolean privateScope = true;
    public String v;

    public interface CacheKeyType {
        public static final String ALL = "ALL";
        public static final String EXC = "EXC";
        public static final String INC = "INC";
        public static final String NONE = "NONE";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    public String getKey() {
        return StringUtils.concatStr2LowerCase(this.api, this.v);
    }

    public ApiCacheDo(String api2, String v2, String blockName2) {
        this.api = api2;
        this.v = v2;
        this.blockName = blockName2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("ApiCacheDo [ api=").append(this.api);
        builder.append(", v=").append(this.v);
        builder.append(", blockName=").append(this.blockName);
        builder.append(", cacheControlHeader=").append(this.cacheControlHeader);
        builder.append(", privateScope=").append(this.privateScope);
        builder.append(", offline=").append(this.offline);
        builder.append(", cacheKeyType=").append(this.cacheKeyType);
        builder.append(", cacheKeyItems=").append(this.cacheKeyItems);
        builder.append("]");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiCacheDo)) {
            return false;
        }
        ApiCacheDo that = (ApiCacheDo) o;
        if (this.privateScope != that.privateScope || this.offline != that.offline || !Objects.equals(this.api, that.api) || !Objects.equals(this.v, that.v) || !Objects.equals(this.blockName, that.blockName) || !Objects.equals(this.cacheControlHeader, that.cacheControlHeader) || !Objects.equals(this.cacheKeyType, that.cacheKeyType) || !Objects.equals(this.cacheKeyItems, that.cacheKeyItems)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.api, this.v, this.blockName, this.cacheControlHeader, Boolean.valueOf(this.privateScope), Boolean.valueOf(this.offline), this.cacheKeyType, this.cacheKeyItems});
    }
}
