package mtopsdk.mtop.domain;

import java.util.Arrays;

public abstract class BaseOutDo implements IMTOPDataObject {
    private String api;
    private String[] ret;
    private String v;

    public abstract Object getData();

    public String getApi() {
        return this.api;
    }

    public void setApi(String api2) {
        this.api = api2;
    }

    public String getV() {
        return this.v;
    }

    public void setV(String v2) {
        this.v = v2;
    }

    public String[] getRet() {
        return this.ret;
    }

    public void setRet(String[] ret2) {
        this.ret = ret2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("BaseOutDo [api=").append(this.api);
        builder.append(", v=").append(this.v);
        builder.append(", ret=").append(Arrays.toString(this.ret)).append("]");
        return builder.toString();
    }
}
