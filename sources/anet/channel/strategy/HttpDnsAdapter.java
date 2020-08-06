package anet.channel.strategy;

import anet.channel.strategy.dispatch.HttpDispatcher;
import java.util.ArrayList;
import java.util.List;

public class HttpDnsAdapter {
    public static void setHosts(ArrayList<String> list) {
        HttpDispatcher.getInstance().addHosts(list);
    }

    public static HttpDnsOrigin getOriginByHttpDns(String host) {
        List<IConnStrategy> ret = StrategyCenter.getInstance().getConnStrategyListByHost(host);
        if (ret.isEmpty()) {
            return null;
        }
        return new HttpDnsOrigin(ret.get(0));
    }

    public static ArrayList<HttpDnsOrigin> getOriginsByHttpDns(String host) {
        List<IConnStrategy> ret = StrategyCenter.getInstance().getConnStrategyListByHost(host);
        if (ret.isEmpty()) {
            return null;
        }
        ArrayList<HttpDnsOrigin> list = new ArrayList<>(ret.size());
        for (IConnStrategy connStrategy : ret) {
            list.add(new HttpDnsOrigin(connStrategy));
        }
        return list;
    }

    public static String getIpByHttpDns(String host) {
        List<IConnStrategy> ret = StrategyCenter.getInstance().getConnStrategyListByHost(host);
        if (ret.isEmpty()) {
            return null;
        }
        return ret.get(0).getIp();
    }

    public static final class HttpDnsOrigin {
        private final IConnStrategy connStrategy;

        HttpDnsOrigin(IConnStrategy connStrategy2) {
            this.connStrategy = connStrategy2;
        }

        public String getOriginIP() {
            return this.connStrategy.getIp();
        }

        public int getOriginPort() {
            return this.connStrategy.getPort();
        }

        public boolean canWithSPDY() {
            String protocol = this.connStrategy.getProtocol().protocol;
            return !protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https");
        }

        public String toString() {
            return this.connStrategy.toString();
        }
    }
}
