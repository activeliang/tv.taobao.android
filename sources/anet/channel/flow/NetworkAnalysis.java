package anet.channel.flow;

public class NetworkAnalysis {
    private static volatile INetworkAnalysis networkAnalysis = new AnalysisProxy((INetworkAnalysis) null);

    public static INetworkAnalysis getInstance() {
        return networkAnalysis;
    }

    public static void setInstance(INetworkAnalysis analysis) {
        networkAnalysis = new AnalysisProxy(analysis);
    }

    private static class AnalysisProxy implements INetworkAnalysis {
        private INetworkAnalysis networkAnalysis = null;

        AnalysisProxy(INetworkAnalysis networkAnalysis2) {
            this.networkAnalysis = networkAnalysis2;
        }

        public void commitFlow(FlowStat flow) {
            if (this.networkAnalysis != null) {
                this.networkAnalysis.commitFlow(flow);
            }
        }
    }
}
