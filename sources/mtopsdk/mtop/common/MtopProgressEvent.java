package mtopsdk.mtop.common;

@Deprecated
public class MtopProgressEvent extends MtopEvent {
    String desc;
    public String seqNo;
    int size;
    int total;

    public MtopProgressEvent(String desc2, int size2, int total2) {
        this.desc = desc2;
        this.size = size2;
        this.total = total2;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getSize() {
        return this.size;
    }

    public int getTotal() {
        return this.total;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("MtopProgressEvent [seqNo=").append(this.seqNo);
        builder.append(", desc=").append(this.desc);
        builder.append(", size=").append(this.size);
        builder.append(", total=").append(this.total);
        builder.append("]");
        return builder.toString();
    }
}
