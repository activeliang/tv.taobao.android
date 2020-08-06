package anet.channel.flow;

import anet.channel.statist.RequestStatistic;

public class FlowStat {
    public long downstream;
    public String protocoltype;
    public String refer;
    public String req_identifier;
    public long upstream;

    public FlowStat() {
    }

    public FlowStat(String f_refer, RequestStatistic rs) {
        this.refer = f_refer;
        this.protocoltype = rs.protocolType;
        this.req_identifier = rs.url;
        this.upstream = rs.sendDataSize;
        this.downstream = rs.recDataSize;
    }

    public String toString() {
        return "FlowStat{refer='" + this.refer + '\'' + ", protocoltype='" + this.protocoltype + '\'' + ", req_identifier='" + this.req_identifier + '\'' + ", upstream=" + this.upstream + ", downstream=" + this.downstream + '}';
    }
}
