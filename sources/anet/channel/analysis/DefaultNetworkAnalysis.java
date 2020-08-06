package anet.channel.analysis;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.flow.FlowStat;
import anet.channel.flow.INetworkAnalysis;
import anet.channel.util.ALog;
import com.taobao.analysis.FlowCenter;

public class DefaultNetworkAnalysis implements INetworkAnalysis {
    private static final String TAG = "DefaultNetworkAnalysis";
    private boolean isNetAnalyVaild;

    public DefaultNetworkAnalysis() {
        try {
            Class.forName("com.taobao.analysis.FlowCenter");
            this.isNetAnalyVaild = true;
        } catch (Exception e) {
            this.isNetAnalyVaild = false;
            ALog.e(TAG, "no NWNetworkAnalysisSDK sdk", (String) null, new Object[0]);
        }
    }

    public void commitFlow(FlowStat flow) {
        if (this.isNetAnalyVaild) {
            FlowCenter.getInstance().commitFlow(GlobalAppRuntimeInfo.getContext(), flow.refer, flow.protocoltype, flow.req_identifier, flow.upstream, flow.downstream);
        }
    }
}
