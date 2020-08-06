package mtopsdk.mtop.domain;

import java.io.Serializable;
import java.util.Map;
import mtopsdk.common.util.StringUtils;

public class MtopRequest implements Serializable, IMTOPDataObject {
    private static final long serialVersionUID = -439476282014493612L;
    private String apiName;
    private String data = "{}";
    public Map<String, String> dataParams;
    private boolean needEcode;
    private boolean needSession;
    private String version;

    public String getApiName() {
        return this.apiName;
    }

    public void setApiName(String apiName2) {
        this.apiName = apiName2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public boolean isNeedEcode() {
        return this.needEcode;
    }

    public void setNeedEcode(boolean needEcode2) {
        this.needEcode = needEcode2;
    }

    public boolean isNeedSession() {
        return this.needSession;
    }

    public void setNeedSession(boolean needSession2) {
        this.needSession = needSession2;
    }

    public boolean isLegalRequest() {
        return StringUtils.isNotBlank(this.apiName) && StringUtils.isNotBlank(this.version) && StringUtils.isNotBlank(this.data);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("MtopRequest [apiName=").append(this.apiName);
        builder.append(", version=").append(this.version);
        builder.append(", data=").append(this.data);
        builder.append(", needEcode=").append(this.needEcode);
        builder.append(", needSession=").append(this.needSession);
        builder.append("]");
        return builder.toString();
    }

    public String getKey() {
        if (StringUtils.isBlank(this.apiName) || StringUtils.isBlank(this.version)) {
            return null;
        }
        return StringUtils.concatStr2LowerCase(this.apiName, this.version);
    }
}
