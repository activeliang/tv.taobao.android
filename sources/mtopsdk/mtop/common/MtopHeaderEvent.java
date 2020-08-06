package mtopsdk.mtop.common;

import java.util.List;
import java.util.Map;

public class MtopHeaderEvent extends MtopEvent {
    private int code;
    private Map<String, List<String>> headers;
    public String seqNo;

    public MtopHeaderEvent(int code2, Map<String, List<String>> header) {
        this.code = code2;
        this.headers = header;
    }

    public int getCode() {
        return this.code;
    }

    public Map<String, List<String>> getHeader() {
        return this.headers;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("MtopHeaderEvent [seqNo=").append(this.seqNo);
        builder.append(", code=").append(this.code);
        builder.append(", headers=").append(this.headers);
        builder.append("]");
        return builder.toString();
    }
}
