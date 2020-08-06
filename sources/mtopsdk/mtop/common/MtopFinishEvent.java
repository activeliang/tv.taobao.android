package mtopsdk.mtop.common;

import mtopsdk.mtop.domain.MtopResponse;

public class MtopFinishEvent extends MtopEvent {
    public MtopResponse mtopResponse;
    public String seqNo;

    public MtopFinishEvent(MtopResponse mtopResponse2) {
        this.mtopResponse = mtopResponse2;
    }

    public MtopResponse getMtopResponse() {
        return this.mtopResponse;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("MtopFinishEvent [seqNo=").append(this.seqNo);
        builder.append(", mtopResponse").append(this.mtopResponse).append("]");
        return builder.toString();
    }
}
