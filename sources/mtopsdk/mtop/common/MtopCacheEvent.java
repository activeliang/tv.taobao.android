package mtopsdk.mtop.common;

import mtopsdk.mtop.domain.MtopResponse;

public class MtopCacheEvent extends MtopFinishEvent {
    public MtopCacheEvent(MtopResponse mtopResponse) {
        super(mtopResponse);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("MtopCacheEvent [seqNo=").append(this.seqNo);
        builder.append(", mtopResponse=").append(this.mtopResponse).append("]");
        return builder.toString();
    }
}
